package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui;

import com.blackmooss.thaumaturgeadditions.compat.CraftingTableRecipes;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.slot.SBArcaneResultSlot;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SBArcaneWorkbenchCraftingStore;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SBArcaneWorkbenchUpgradeWrapper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftingTransaction;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;
import com.leclowndu93150.thaumaturge.api.transaction.Transaction;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SBArcaneWorkbenchUpgradeContainer
        extends UpgradeContainerBase<SBArcaneWorkbenchUpgradeWrapper, SBArcaneWorkbenchUpgradeContainer> {
    public static final UpgradeContainerType<SBArcaneWorkbenchUpgradeWrapper, SBArcaneWorkbenchUpgradeContainer> TYPE =
            new UpgradeContainerType<>(SBArcaneWorkbenchUpgradeContainer::new);

    private static final String DATA_SHIFT_CLICK_INTO_STORAGE = "shiftClickIntoStorage";
    private static final String DATA_REFILL_CRAFTING_GRID = "refill_crafting_grid";

    private final ResultContainer craftResult = new ResultContainer();
    private final SBArcaneResultSlot resultSlot;
    private String lastGridLog = "";

    public SBArcaneWorkbenchUpgradeContainer(
            Player player,
            int upgradeContainerId,
            SBArcaneWorkbenchUpgradeWrapper upgradeWrapper,
            UpgradeContainerType<SBArcaneWorkbenchUpgradeWrapper, SBArcaneWorkbenchUpgradeContainer> type
    ) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        for (int slot = 0; slot < SBArcaneWorkbenchUpgradeWrapper.SIZE; slot++) {
            this.slots.add(new SlotSuppliedHandler(this.supplyFromWrapper(SBArcaneWorkbenchUpgradeWrapper::getInventory), slot, -100, -100));
        }
        this.resultSlot = new SBArcaneResultSlot(this.craftResult, this, -100, -100);
        this.slots.add(this.resultSlot);
    }

    @Override
    public void onInit() {
        super.onInit();
        this.upgradeWrapper.setResultRefreshHandler(this::refreshResult);
        if (this.isServerSide()) {
            this.upgradeWrapper.updateStoredAura(this.player.level(), this.player.blockPosition());
        }
        this.refreshResult();
    }

    @Override
    public void handlePacket(@NonNull CompoundTag data) {
        NBTHelper.getBoolean(data, DATA_SHIFT_CLICK_INTO_STORAGE).ifPresent(this::setShiftClickIntoStorage);
        NBTHelper.getBoolean(data, DATA_REFILL_CRAFTING_GRID).ifPresent(this::setRefillCraftingGrid);
    }

    public boolean isServerSide() {
        return this.player instanceof ServerPlayer;
    }

    public SBArcaneResultSlot getResultSlot() {
        return this.resultSlot;
    }

    public int getStoredAura() {
        return this.upgradeWrapper.getStoredAura();
    }

    public void refreshResult() {
        if (!this.isServerSide()) {
            return;
        }
        ArcaneCraftingInput.Positioned positioned = this.buildPositionedInput();
        ArcaneCraftingInput input = positioned.input();
        if (input.width() <= 0 || input.height() <= 0) {
            this.craftResult.setItem(0, ItemStack.EMPTY);
            this.lastGridLog = "";
            return;
        }
        ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.preview(this.context(), (ServerPlayer) this.player, input);
        if (result.successful()) {
            this.craftResult.setItem(0, result.output());
            return;
        }
        if (result.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
            CraftingTableRecipes.VanillaCraft vanilla = CraftingTableRecipes.plan(this.player.level(), this.gridStacks());
            this.craftResult.setItem(0, vanilla == null ? ItemStack.EMPTY : vanilla.output());
            return;
        }
        this.craftResult.setItem(0, ItemStack.EMPTY);
    }

    private void logGridOnce(String tag, ArcaneCraftingInput input) {
        String summary = this.gridSummary();
        if (summary.equals(this.lastGridLog)) {
            return;
        }
        this.lastGridLog = summary;
    }

    private String gridSummary() {
        StringBuilder sb = new StringBuilder();
        List<ItemStack> stacks = this.gridStacks();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack s = stacks.get(i);
            if (s.isEmpty()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(i).append(':').append(BuiltInRegistries.ITEM.getKey(s.getItem())).append('x').append(s.getCount());
        }
        return sb.isEmpty() ? "Null" : sb.toString();
    }

    private List<ItemStack> gridStacks() {
        List<ItemStack> slots = new ArrayList<>(SBArcaneWorkbenchUpgradeWrapper.GRID_SLOTS);
        for (int index = 0; index < SBArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; index++) {
            slots.add(this.upgradeWrapper.getInventory().getStackInSlot(index));
        }
        return slots;
    }

    public void onResultTaken(Player player, ItemStack takenStack) {
        if (!this.isServerSide()) {
            return;
        }
        ItemStack remaining = this.craftResult.getItem(0);
        List<ItemStack> gridBefore = this.snapshotCraftingGrid();
        boolean crafted = this.performCraft((ServerPlayer) player, takenStack);
        this.upgradeWrapper.updateStoredAura(this.player.level(), this.player.blockPosition());
        if (!crafted) {
            this.craftResult.setItem(0, ItemStack.EMPTY);
            return;
        }
        if (!remaining.isEmpty()) {
            player.drop(remaining, false);
        }
        this.refillCraftingGrid(player, gridBefore);
        this.craftResult.setItem(0, ItemStack.EMPTY);
        this.refreshResult();
    }

    private List<ItemStack> snapshotCraftingGrid() {
        List<ItemStack> snapshot = new ArrayList<>(SBArcaneWorkbenchUpgradeWrapper.GRID_SLOTS);
        for (int slot = 0; slot < SBArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; slot++) {
            snapshot.add(this.upgradeWrapper.getInventory().getStackInSlot(slot).copy());
        }
        return snapshot;
    }

    private void refillCraftingGrid(Player player, List<ItemStack> gridBefore) {
        if (!this.upgradeWrapper.shouldRefillCraftingGrid()) {
            return;
        }
        StatefulComponentItemHandler inventory = this.upgradeWrapper.getInventory();
        for (int slot = 0; slot < SBArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; slot++) {
            ItemStack before = gridBefore.get(slot);
            if (before.isEmpty() || !inventory.getStackInSlot(slot).isEmpty()) {
                continue;
            }
            if (this.upgradeWrapper.extractFromStorageOrPlayer(player, before)) {
                inventory.setStackInSlot(slot, before.copyWithCount(1));
            }
        }
    }

    private boolean performCraft(ServerPlayer serverPlayer, ItemStack takenStack) {
        ArcaneCraftingInput.Positioned positioned = this.buildPositionedInput();
        ArcaneCraftingInput input = positioned.input();
        if (input.width() <= 0 || input.height() <= 0) {
            return false;
        }
        boolean crafted = false;
        this.upgradeWrapper.setSuppressResultRefresh(true);
        try (Transaction transaction = Transaction.openRoot()) {
            SBArcaneWorkbenchCraftingStore store = new SBArcaneWorkbenchCraftingStore(
                    this.upgradeWrapper.getInventory(), serverPlayer, positioned.left(), positioned.top(), input.width(), input.height());
            ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.craft(this.context(), serverPlayer, input, store, transaction);
            if (result.successful() && ItemStack.isSameItemSameComponents(result.output(), takenStack)) {
                crafted = true;
                transaction.commit();
            } else if (result.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
                crafted = this.performVanillaCraft(serverPlayer, takenStack, transaction);
            }
        } finally {
            this.upgradeWrapper.setSuppressResultRefresh(false);
        }
        return crafted;
    }

    private boolean performVanillaCraft(ServerPlayer serverPlayer, ItemStack takenStack, Transaction transaction) {
        List<ItemStack> slots = this.gridStacks();
        CraftingTableRecipes.VanillaCraft vanilla = CraftingTableRecipes.plan(serverPlayer.level(), slots);
        if (vanilla == null || !ItemStack.isSameItemSameComponents(vanilla.output(), takenStack)) {
            return false;
        }
        SBArcaneWorkbenchCraftingStore store = new SBArcaneWorkbenchCraftingStore(
                this.upgradeWrapper.getInventory(), serverPlayer, 0, 0, CraftingTableRecipes.GRID_WIDTH, CraftingTableRecipes.GRID_HEIGHT);
        ItemStack wand = this.upgradeWrapper.getInventory().getStackInSlot(SBArcaneWorkbenchUpgradeWrapper.WAND_SLOT);
        return CraftingTableRecipes.consume(store, vanilla, slots, wand, transaction);
    }

    public ArcaneCraftingInput asArcaneCraftInput() {
        return this.buildPositionedInput().input();
    }

    private ArcaneCraftingInput.Positioned buildPositionedInput() {
        List<ItemStack> stacks = new ArrayList<>(SBArcaneWorkbenchUpgradeWrapper.SIZE);
        for (int index = 0; index < SBArcaneWorkbenchUpgradeWrapper.SIZE; index++) {
            stacks.add(this.upgradeWrapper.getInventory().getStackInSlot(index));
        }
        return ArcaneCraftingInput.ofPositioned(SBArcaneWorkbenchUpgradeWrapper.GRID_WIDTH,
                SBArcaneWorkbenchUpgradeWrapper.GRID_HEIGHT, stacks);
    }

    private ArcaneWorkbenchContext context() {
        return ArcaneWorkbenchContext.virtual((ServerPlayer) this.player, SophisticatedBackpacksCompat.WORKBENCH_IDENTITY, this.player.getUUID());
    }

    @Override
    public @NonNull ItemStack getSlotStackToTransfer(@NonNull Slot slot) {
        if (slot != this.resultSlot) {
            return super.getSlotStackToTransfer(slot);
        }
        this.refreshResult();
        ItemStack displayed = slot.getItem();
        if (!displayed.isEmpty()) {
            displayed.getItem().onCraftedBy(displayed, this.player.level(), this.player);
        }
        return displayed;
    }

    @Override
    public void onTakeFromSlot(@NonNull Slot slot, @NonNull Player player, @NonNull ItemStack slotStack) {
        super.onTakeFromSlot(slot, player, slotStack);
        if (slot == this.resultSlot) {
            this.refreshResult();
        }
    }

    @Override
    public boolean mergeIntoStorageFirst(@NonNull Slot slot) {
        return slot != this.resultSlot || this.upgradeWrapper.shouldShiftClickIntoStorage();
    }

    @Override
    public boolean allowsPickupAll(@NonNull Slot slot) {
        return slot != this.resultSlot;
    }

    public boolean shouldShiftClickIntoStorage() {
        return this.upgradeWrapper.shouldShiftClickIntoStorage();
    }

    public void setShiftClickIntoStorage(boolean shiftClickIntoStorage) {
        this.upgradeWrapper.setShiftClickIntoStorage(shiftClickIntoStorage);
        this.sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHIFT_CLICK_INTO_STORAGE, shiftClickIntoStorage));
    }

    public boolean shouldRefillCraftingGrid() {
        return this.upgradeWrapper.shouldRefillCraftingGrid();
    }

    public void setRefillCraftingGrid(boolean refillCraftingGrid) {
        this.upgradeWrapper.setRefillCraftingGrid(refillCraftingGrid);
        this.sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_REFILL_CRAFTING_GRID, refillCraftingGrid));
    }
}
