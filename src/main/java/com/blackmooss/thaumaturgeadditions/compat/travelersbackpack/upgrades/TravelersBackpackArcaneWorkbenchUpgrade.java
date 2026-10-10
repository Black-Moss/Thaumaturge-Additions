package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.CraftingTableRecipes;
import com.blackmooss.thaumaturgeadditions.compat.WorkbenchChargerAura;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.TravelersBackpackCompat;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot.ArcaneCrystalSlot;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot.TravelersBackpackArcaneResultSlot;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot.TravelersBackpackArcaneWorkbenchSlot;
import com.blackmooss.thaumaturgeadditions.registry.TADataComponents;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftingTransaction;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.*;
import com.tiviacz.travelersbackpack.util.ContainerContentsHelper;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelersBackpackArcaneWorkbenchUpgrade extends UpgradeBase<TravelersBackpackArcaneWorkbenchUpgrade>
        implements ITickableUpgrade, IEnable, IMoveSelector {
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_SLOTS = GRID_WIDTH * GRID_HEIGHT;
    public static final int CRYSTAL_SLOTS = 6;
    public static final int CRYSTAL_START = GRID_SLOTS;
    public static final int WAND_SLOT = GRID_SLOTS + CRYSTAL_SLOTS;
    public static final int SIZE = WAND_SLOT + 1;

    public static final List<ResourceKey<IAspect>> PRIMAL_ORDER = List.of(
            TTAspects.AER, TTAspects.IGNIS, TTAspects.AQUA, TTAspects.TERRA, TTAspects.ORDO, TTAspects.PERDITIO);

    public static final int TAB_WIDTH = 100;
    public static final int TAB_HEIGHT = 131;

    public static final List<Point> GRID_SLOT_POSITIONS = List.of(
            new Point(23, 23), new Point(42, 23), new Point(61, 23),
            new Point(23, 42), new Point(42, 42), new Point(61, 42),
            new Point(23, 61), new Point(42, 61), new Point(61, 61));
    public static final List<Point> CRYSTAL_SLOT_POSITIONS = List.of(
            new Point(80, 51), new Point(5, 32), new Point(80, 32),
            new Point(5, 51), new Point(33, 4), new Point(52, 4));
    public static final Point RESULT_SLOT_POSITION = new Point(42, 109);
    public static final Point WAND_SLOT_POSITION = new Point(9, 109);

    private static final int AURA_TICK_RATE = 10;

    private final ItemStacksResourceHandler items;
    private final ResultContainer resultContainer = new ResultContainer();

    public TravelersBackpackArcaneWorkbenchUpgrade(UpgradeManager manager, int dataHolderSlot, NonNullList<ItemStack> contents) {
        super(manager, dataHolderSlot, new Point(TAB_WIDTH, TAB_HEIGHT));
        this.items = createHandler(Objects.requireNonNull(contents, "contents"));
    }

    @Override
    public boolean isEnabled(UpgradeBase<?> upgrade) {
        return true;
    }

    @Override
    public void initializeContainers(BackpackBaseMenu menu, BackpackWrapper wrapper) {
        Level level = menu.player.level();
        if (!level.isClientSide()) {
            refreshAura(level, menu.player.blockPosition());
        }
        refreshResult();
    }

    @Override
    public void onUpgradeRemoved(ItemStack removedStack, @Nullable Player player) {
        if (player == null) {
            return;
        }
        BackpackBaseMenu.clearSlotsAndPlaySound(player, this.items, SIZE, false);
        removedStack.set(ModDataComponents.BACKPACK_CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(SIZE, ItemStack.EMPTY)));
    }

    @Override
    public List<Pair<Integer, Integer>> getUpgradeSlotsPosition(int x, int y) {
        List<Pair<Integer, Integer>> positions = new ArrayList<>(SIZE + 1);
        for (Point point : GRID_SLOT_POSITIONS) {
            positions.add(Pair.of(x + point.x(), y + point.y()));
        }
        for (Point point : CRYSTAL_SLOT_POSITIONS) {
            positions.add(Pair.of(x + point.x(), y + point.y()));
        }
        positions.add(Pair.of(x + WAND_SLOT_POSITION.x(), y + WAND_SLOT_POSITION.y()));
        positions.add(Pair.of(x + RESULT_SLOT_POSITION.x(), y + RESULT_SLOT_POSITION.y()));
        return positions;
    }

    @Override
    public List<Slot> getUpgradeSlots(BackpackBaseMenu menu, BackpackWrapper wrapper, int x, int y) {
        List<Slot> slots = new ArrayList<>(SIZE + 1);
        for (int index = 0; index < GRID_SLOTS; index++) {
            Point point = GRID_SLOT_POSITIONS.get(index);
            slots.add(TravelersBackpackArcaneWorkbenchSlot.grid(this, this.items, index, x + point.x(), y + point.y()));
        }
        for (int index = 0; index < CRYSTAL_SLOTS; index++) {
            Point point = CRYSTAL_SLOT_POSITIONS.get(index);
            slots.add(new ArcaneCrystalSlot(this, this.items, CRYSTAL_START + index, x + point.x(), y + point.y(), PRIMAL_ORDER.get(index)));
        }
        slots.add(TravelersBackpackArcaneWorkbenchSlot.wand(this, this.items, WAND_SLOT, x + WAND_SLOT_POSITION.x(), y + WAND_SLOT_POSITION.y()));
        slots.add(new TravelersBackpackArcaneResultSlot(this, menu.player, this.resultContainer, x + RESULT_SLOT_POSITION.x(), y + RESULT_SLOT_POSITION.y()));
        return slots;
    }

    @Override
    public int getTickRate() {
        return AURA_TICK_RATE;
    }

    @Override
    public void tick(@Nullable Player player, Level level, BlockPos pos, int currentTick) {
        if (level.isClientSide() || !isTabOpened()) {
            return;
        }
        if (!hasCooldown() || getCooldown() != getTickRate()) {
            setCooldown(getTickRate());
        }
        refreshAura(level, pos);
        refreshResult();
    }

    public boolean canCraft() {
        return isTabOpened() && isEnabled(this);
    }

    public boolean craftsOnServer() {
        return craftingPlayer() != null;
    }

    public int getStoredAura() {
        return getDataHolderStack().getOrDefault(TADataComponents.BACKPACK_WORKBENCH_AURA, 0);
    }

    private void setStoredAura(int aura) {
        ItemStack dataHolderStack = getDataHolderStack().copy();
        if (dataHolderStack.isEmpty()) {
            return;
        }
        if (aura <= 0) {
            dataHolderStack.remove(TADataComponents.BACKPACK_WORKBENCH_AURA.get());
        } else {
            dataHolderStack.set(TADataComponents.BACKPACK_WORKBENCH_AURA.get(), aura);
        }
        StacksHandlerUtils.setStackInSlot(getUpgradeManager().getUpgradesHandler(), getDataHolderSlot(), dataHolderStack);
    }

    public void refreshResult() {
        ServerPlayer player = craftingPlayer();
        if (player == null) {
            return;
        }
        refreshAura(player.level(), player.blockPosition());
        if (!canCraft()) {
            this.resultContainer.setItem(0, ItemStack.EMPTY);
            return;
        }
        ArcaneCraftingInput.Positioned positioned = buildPositionedInput();
        ArcaneCraftingInput input = positioned.input();
        if (input.width() <= 0 || input.height() <= 0) {
            this.resultContainer.setItem(0, ItemStack.EMPTY);
            return;
        }
        ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.preview(context(player), player, input);
        if (result.successful()) {
            this.resultContainer.setItem(0, result.output());
            return;
        }
        if (result.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
            CraftingTableRecipes.VanillaCraft vanilla = CraftingTableRecipes.plan(player.level(), gridStacks());
            this.resultContainer.setItem(0, vanilla == null ? ItemStack.EMPTY : vanilla.output());
            return;
        }
        if (input.ingredientCount() > 0) {
            ThaumaturgeAdditions.LOGGER.debug("Traveler's Backpack Arcane workbench upgrade: craft preview failed - {}", result.failure());
        }
        this.resultContainer.setItem(0, ItemStack.EMPTY);
    }

    public ItemStack takeCraft() {
        ServerPlayer player = craftingPlayer();
        ItemStack displayed = this.resultContainer.getItem(0);
        if (player == null || displayed.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack crafted = ItemStack.EMPTY;
        try (Transaction transaction = Transaction.openRoot()) {
            ArcaneCraftingInput.Positioned positioned = buildPositionedInput();
            ArcaneCraftingInput input = positioned.input();
            TravelersBackpackArcaneWorkbenchCraftingStore store = new TravelersBackpackArcaneWorkbenchCraftingStore(
                    this.items, player, positioned.left(), positioned.top(), input.width(), input.height());
            ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.craft(context(player), player, input, store, transaction);
            if (result.successful() && ItemStack.matches(result.output(), displayed)) {
                crafted = result.output();
                transaction.commit();
            } else if (result.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
                crafted = takeVanillaCraft(player, displayed, transaction);
            }
        }
        refreshResult();
        return crafted;
    }

    private ItemStack takeVanillaCraft(ServerPlayer player, ItemStack displayed, TransactionContext transaction) {
        List<ItemStack> slots = gridStacks();
        CraftingTableRecipes.VanillaCraft vanilla = CraftingTableRecipes.plan(player.level(), slots);
        if (vanilla == null || !ItemStack.matches(vanilla.output(), displayed)) {
            return ItemStack.EMPTY;
        }
        TravelersBackpackArcaneWorkbenchCraftingStore store = new TravelersBackpackArcaneWorkbenchCraftingStore(
                this.items, player, 0, 0, CraftingTableRecipes.GRID_WIDTH, CraftingTableRecipes.GRID_HEIGHT);
        if (!CraftingTableRecipes.consume(store, vanilla, slots, StacksHandlerUtils.getStackInSlot(this.items, WAND_SLOT), transaction)) {
            return ItemStack.EMPTY;
        }
        return vanilla.output();
    }

    private List<ItemStack> gridStacks() {
        List<ItemStack> slots = new ArrayList<>(GRID_SLOTS);
        for (int index = 0; index < GRID_SLOTS; index++) {
            slots.add(StacksHandlerUtils.getStackInSlot(this.items, index));
        }
        return slots;
    }

    public ArcaneCraftingInput asArcaneCraftInput() {
        return buildPositionedInput().input();
    }

    private ArcaneCraftingInput.Positioned buildPositionedInput() {
        List<ItemStack> stacks = new ArrayList<>(SIZE);
        for (int index = 0; index < SIZE; index++) {
            stacks.add(StacksHandlerUtils.getStackInSlot(this.items, index));
        }
        return ArcaneCraftingInput.ofPositioned(GRID_WIDTH, GRID_HEIGHT, stacks);
    }

    private ArcaneWorkbenchContext context(ServerPlayer player) {
        return ArcaneWorkbenchContext.virtual(player, TravelersBackpackCompat.WORKBENCH_IDENTITY, player.getUUID());
    }

    private @Nullable ServerPlayer craftingPlayer() {
        for (Player player : getUpgradeManager().getWrapper().getPlayersUsing()) {
            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }
        }
        return null;
    }

    private void refreshAura(Level level, BlockPos pos) {
        int aura;
        if (level instanceof ServerLevel serverLevel && hasChargerInstalled()) {
            aura = WorkbenchChargerAura.total(serverLevel, WorkbenchChargerAura.anchors(pos));
        } else {
            aura = (int) AuraHelper.getVis(level, pos);
        }
        if (aura != getStoredAura()) {
            ThaumaturgeAdditions.LOGGER.debug("Traveler's Backpack arcane workbench upgrade: aura at {} -> {}", pos, aura);
            setStoredAura(aura);
        }
    }

    private boolean hasChargerInstalled() {
        return getUpgradeManager()
                .getUpgrade(TravelersBackpackArcaneChargerUpgrade.class)
                .filter(charger -> charger.isEnabled(charger))
                .isPresent();
    }

    private void setSlotChanged(ItemStack dataHolderStack, int index, ItemStack stack) {
        dataHolderStack.update(ModDataComponents.BACKPACK_CONTAINER, ItemContainerContents.EMPTY,
                currentContents -> ContainerContentsHelper.updateStack(currentContents, SIZE, stack, index));
    }

    private ItemStacksResourceHandler createHandler(NonNullList<ItemStack> contents) {
        return new ItemStacksResourceHandler(contents) {
            @Override
            protected void onContentsChanged(int slot, @NonNull ItemStack previousStack) {
                TravelersBackpackArcaneWorkbenchUpgrade.this.updateDataHolderUnchecked(
                        dataHolderStack -> TravelersBackpackArcaneWorkbenchUpgrade.this.setSlotChanged(dataHolderStack, slot, StacksHandlerUtils.getStackInSlot(this, slot)));
                TravelersBackpackArcaneWorkbenchUpgrade.this.refreshResult();
            }
        };
    }
}
