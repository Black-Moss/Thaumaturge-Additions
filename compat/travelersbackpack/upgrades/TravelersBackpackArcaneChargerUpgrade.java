package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.IEnable;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TravelersBackpackArcaneChargerUpgrade extends UpgradeBase<TravelersBackpackArcaneChargerUpgrade> implements IEnable {
    public TravelersBackpackArcaneChargerUpgrade(UpgradeManager manager, int dataHolderSlot) {
        // 与 TB 自带的灯笼升级保持一致（同为无标签页升级）
        super(manager, dataHolderSlot, new Point(40, 28));
    }

    @Override
    public boolean hasTab() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        updateDataHolderUnchecked(ModDataComponents.UPGRADE_ENABLED.get(), enabled);
    }

    @Override
    public List<Pair<Integer, Integer>> getUpgradeSlotsPosition(int x, int y) {
        return List.of();
    }

    @Override
    public List<? extends Slot> getUpgradeSlots(BackpackBaseMenu menu, BackpackWrapper wrapper, int x, int y) {
        return List.of();
    }

    @Override
    public void onUpgradeRemoved(ItemStack removedStack) {
        super.onUpgradeRemoved(removedStack);
        this.setEnabled(false);
    }
}
