package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.WorkbenchChargerAura;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item.SophisticatedBackpacksArcaneChargerUpgradeItem;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item.SophisticatedBackpacksArcaneWorkbenchUpgradeItem;
import com.blackmooss.thaumaturgeadditions.registry.TADataComponents;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.content.workbench.SlotCrystalEssentia;
import com.leclowndu93150.thaumaturge.content.workbench.SlotWorkbenchWand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper
        extends UpgradeWrapperBase<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper, SophisticatedBackpacksArcaneWorkbenchUpgradeItem>
        implements ITickableUpgrade {
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_SLOTS = GRID_WIDTH * GRID_HEIGHT;
    public static final int CRYSTAL_SLOTS = 6;
    public static final int CRYSTAL_START = GRID_SLOTS;
    public static final int WAND_SLOT = GRID_SLOTS + CRYSTAL_SLOTS;
    public static final int SIZE = WAND_SLOT + 1;

    public static final List<ResourceKey<IAspect>> PRIMAL_ORDER = List.of(
            TTAspects.AER, TTAspects.IGNIS, TTAspects.AQUA, TTAspects.TERRA, TTAspects.ORDO, TTAspects.PERDITIO);

    private static final int AURA_REFRESH_INTERVAL = 10;

    private final StatefulComponentItemHandler inventory;
    private Runnable resultRefreshHandler = () -> {
    };
    private boolean suppressResultRefresh = false;

    public SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        ThaumaturgeAdditions.LOGGER.info("SB 奥术工作台升级: 实例化 Wrapper, item={}", upgrade.getItemHolder().getRegisteredName());
        if (upgrade.has(DataComponents.CONTAINER)) {
            upgrade.set(ModCoreDataComponents.LENIENT_CONTAINER.get(), upgrade.get(DataComponents.CONTAINER));
        }
        upgrade.remove(DataComponents.CONTAINER);

        this.inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), SIZE) {
            @Override
            protected void onContentsChanged(int index, @NonNull ItemStack previousContents, @NonNull ItemStack newContents) {
                super.onContentsChanged(index, previousContents, newContents);
                SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.this.save();
                // 合成消耗期间会重入调用 resultRefreshHandler -> refreshResult -> ArcaneCraftingTransaction.preview，
                // 而 preview 会在已打开的事务里再开 root 事务（TT 明确禁止），导致只消耗第一个材料。故合成时抑制刷新。
                if (!SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.this.suppressResultRefresh) {
                    SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.this.resultRefreshHandler.run();
                }
            }

            @Override
            public boolean isItemValid(int slot, @NonNull ItemStack stack) {
                return SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.this.isValidSlot(slot, stack);
            }
        };
    }

    public void setSuppressResultRefresh(boolean suppress) {
        this.suppressResultRefresh = suppress;
    }

    private static boolean extractFromPlayer(Player player, ItemStack stack) {
        int slot = player.getInventory().findSlotMatchingItem(stack);
        if (slot < 0) {
            return false;
        }
        player.getInventory().removeItem(slot, 1);
        return true;
    }

    public StatefulComponentItemHandler getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    public void setResultRefreshHandler(Runnable handler) {
        this.resultRefreshHandler = handler;
    }

    public void requestResultRefresh() {
        this.resultRefreshHandler.run();
    }

    public boolean isValidSlot(int slot, ItemStack stack) {
        // 空堆叠视为合法：SB 的快移/清空会用 setStackInSlot(slot, EMPTY)，
        // StatefulComponentItemHandler.setStackInSlot 会先走 isItemValid，空堆叠若返回 false 会抛 "Invalid stack ... air"
        if (stack.isEmpty()) {
            return true;
        }
        if (slot >= CRYSTAL_START && slot < CRYSTAL_START + CRYSTAL_SLOTS) {
            return SlotCrystalEssentia.isValidCrystal(stack, PRIMAL_ORDER.get(slot - CRYSTAL_START));
        }
        if (slot == WAND_SLOT) {
            return SlotWorkbenchWand.isUsableWand(stack);
        }
        return stack.getItem().canFitInsideContainerItems(stack);
    }

    public int getStoredAura() {
        return this.upgrade.getOrDefault(TADataComponents.BACKPACK_WORKBENCH_AURA.get(), 0);
    }

    public void setStoredAura(int aura) {
        if (aura == this.getStoredAura()) {
            return;
        }
        if (aura <= 0) {
            this.upgrade.remove(TADataComponents.BACKPACK_WORKBENCH_AURA.get());
        } else {
            this.upgrade.set(TADataComponents.BACKPACK_WORKBENCH_AURA.get(), aura);
        }
        this.save();
    }

    public void updateStoredAura(Level level, @Nullable BlockPos pos) {
        if (pos == null) {
            return;
        }
        int aura;
        if (level instanceof ServerLevel serverLevel && this.hasChargerInstalled()) {
            aura = WorkbenchChargerAura.total(serverLevel, WorkbenchChargerAura.anchors(pos));
        } else {
            aura = (int) AuraHelper.getVis(level, pos);
        }
        if (aura != this.getStoredAura()) {
            ThaumaturgeAdditions.LOGGER.debug("Sophisticated Backpacks arcane workbench: aura at {} -> {}", pos, aura);
        }
        this.setStoredAura(aura);
    }

    private boolean hasChargerInstalled() {
        return this.storageWrapper.getUpgradeHandler()
                .hasUpgrade(SophisticatedBackpacksArcaneChargerUpgradeItem.TYPE);
    }

    @Override
    public void tick(@Nullable Entity entity, Level level, @NonNull BlockPos pos) {
        if (level.isClientSide() || this.isInCooldown(level)) {
            return;
        }
        this.setCooldown(level, AURA_REFRESH_INTERVAL);
        this.updateStoredAura(level, entity == null ? pos : entity.blockPosition());
        this.resultRefreshHandler.run();
    }

    public boolean shouldShiftClickIntoStorage() {
        return this.upgrade.getOrDefault(ModCoreDataComponents.SHIFT_CLICK_INTO_STORAGE, true);
    }

    public void setShiftClickIntoStorage(boolean shiftClickIntoStorage) {
        this.upgrade.set(ModCoreDataComponents.SHIFT_CLICK_INTO_STORAGE, shiftClickIntoStorage);
        this.save();
    }

    public boolean shouldRefillCraftingGrid() {
        return this.upgrade.getOrDefault(ModCoreDataComponents.REFILL_CRAFTING_GRID, false);
    }

    public void setRefillCraftingGrid(boolean refillCraftingGrid) {
        this.upgrade.set(ModCoreDataComponents.REFILL_CRAFTING_GRID, refillCraftingGrid);
        this.save();
    }

    public boolean extractFromStorageOrPlayer(Player player, ItemStack stack) {
        return extractFromStorage(stack) || extractFromPlayer(player, stack);
    }

    private boolean extractFromStorage(ItemStack stack) {
        return !InventoryHelper.extractFromInventory(
                s -> ItemStack.isSameItemSameComponents(s, stack), 1, this.storageWrapper.getInventoryHandler(), false).isEmpty();
    }
}
