package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.CraftingTableRecipes;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.slot.SophisticatedBackpacksArcaneResultSlot;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SophisticatedBackpacksArcaneWorkbenchCraftingStore;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftingTransaction;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ComponentItemStacksHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SophisticatedBackpacksArcaneWorkbenchUpgradeContainer
        extends UpgradeContainerBase<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper, SophisticatedBackpacksArcaneWorkbenchUpgradeContainer> {
    public static final UpgradeContainerType<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper, SophisticatedBackpacksArcaneWorkbenchUpgradeContainer> TYPE =
            new UpgradeContainerType<>(SophisticatedBackpacksArcaneWorkbenchUpgradeContainer::new);

    private static final String DATA_SHIFT_CLICK_INTO_STORAGE = "shiftClickIntoStorage";
    private static final String DATA_REFILL_CRAFTING_GRID = "refill_crafting_grid";

    private final ResultContainer craftResult = new ResultContainer();
    private final SophisticatedBackpacksArcaneResultSlot resultSlot;

    public SophisticatedBackpacksArcaneWorkbenchUpgradeContainer(
            Player player,
            int upgradeContainerId,
            SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper upgradeWrapper,
            UpgradeContainerType<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper, SophisticatedBackpacksArcaneWorkbenchUpgradeContainer> type
    ) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        for (int slot = 0; slot < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.SIZE; slot++) {
            // 槽位坐标由标签页在打开时统一摆放
            this.slots.add(new SlotSuppliedHandler(this.supplyFromWrapper(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper::getInventory), slot, -100, -100));
        }
        this.resultSlot = new SophisticatedBackpacksArcaneResultSlot(this.craftResult, this, -100, -100);
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
    public void handlePacket(CompoundTag data) {
        data.getBoolean(DATA_SHIFT_CLICK_INTO_STORAGE).ifPresent(this::setShiftClickIntoStorage);
        data.getBoolean(DATA_REFILL_CRAFTING_GRID).ifPresent(this::setRefillCraftingGrid);
    }

    public boolean isServerSide() {
        return this.player instanceof ServerPlayer;
    }

    public SophisticatedBackpacksArcaneResultSlot getResultSlot() {
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
        if (input.ingredientCount() > 0) {
            ThaumaturgeAdditions.LOGGER.debug("Sophisticated Backpacks arcane workbench upgrade: craft preview failed - {}", result.failure());
        }
        this.craftResult.setItem(0, ItemStack.EMPTY);
    }

    private List<ItemStack> gridStacks() {
        List<ItemStack> slots = new ArrayList<>(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS);
        for (int index = 0; index < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; index++) {
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
        if (!this.performCraft((ServerPlayer) player, takenStack)) {
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
        List<ItemStack> snapshot = new ArrayList<>(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS);
        for (int slot = 0; slot < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; slot++) {
            snapshot.add(this.upgradeWrapper.getInventory().getStackInSlot(slot).copy());
        }
        return snapshot;
    }

    private void refillCraftingGrid(Player player, List<ItemStack> gridBefore) {
        if (!this.upgradeWrapper.shouldRefillCraftingGrid()) {
            return;
        }
        ComponentItemStacksHandler inventory = this.upgradeWrapper.getInventory();
        for (int slot = 0; slot < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; slot++) {
            ItemStack before = gridBefore.get(slot);
            if (before.isEmpty() || !inventory.getStackInSlot(slot).isEmpty()) {
                continue;
            }
            if (this.upgradeWrapper.extractFromStorageOrPlayer(player, before)) {
                inventory.set(slot, ItemResource.of(before), 1);
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
        try (Transaction transaction = Transaction.openRoot()) {
            SophisticatedBackpacksArcaneWorkbenchCraftingStore store = new SophisticatedBackpacksArcaneWorkbenchCraftingStore(
                    this.upgradeWrapper.getInventory(), serverPlayer, positioned.left(), positioned.top(), input.width(), input.height());
            ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.craft(this.context(), serverPlayer, input, store, transaction);
            if (result.successful() && ItemStack.isSameItemSameComponents(result.output(), takenStack)) {
                crafted = true;
                transaction.commit();
            } else if (result.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
                crafted = this.performVanillaCraft(serverPlayer, takenStack, transaction);
            }
        }
        return crafted;
    }

    private boolean performVanillaCraft(ServerPlayer serverPlayer, ItemStack takenStack, Transaction transaction) {
        List<ItemStack> slots = this.gridStacks();
        CraftingTableRecipes.VanillaCraft vanilla = CraftingTableRecipes.plan(serverPlayer.level(), slots);
        if (vanilla == null || !ItemStack.isSameItemSameComponents(vanilla.output(), takenStack)) {
            return false;
        }
        SophisticatedBackpacksArcaneWorkbenchCraftingStore store = new SophisticatedBackpacksArcaneWorkbenchCraftingStore(
                this.upgradeWrapper.getInventory(), serverPlayer, 0, 0, CraftingTableRecipes.GRID_WIDTH, CraftingTableRecipes.GRID_HEIGHT);
        ItemStack wand = this.upgradeWrapper.getInventory().getStackInSlot(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.WAND_SLOT);
        return CraftingTableRecipes.consume(store, vanilla, slots, wand, transaction);
    }

    public ArcaneCraftingInput asArcaneCraftInput() {
        return this.buildPositionedInput().input();
    }

    private ArcaneCraftingInput.Positioned buildPositionedInput() {
        List<ItemStack> stacks = new ArrayList<>(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.SIZE);
        for (int index = 0; index < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.SIZE; index++) {
            stacks.add(this.upgradeWrapper.getInventory().getStackInSlot(index));
        }
        return ArcaneCraftingInput.ofPositioned(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_WIDTH,
                SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_HEIGHT, stacks);
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
            displayed.getItem().onCraftedBy(displayed, this.player);
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
