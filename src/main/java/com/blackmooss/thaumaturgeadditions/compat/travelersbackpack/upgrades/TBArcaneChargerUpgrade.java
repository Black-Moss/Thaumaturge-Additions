package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.client.screens.widgets.WidgetBase;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.IEnable;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class TBArcaneChargerUpgrade extends UpgradeBase<TBArcaneChargerUpgrade> implements IEnable {
    public TBArcaneChargerUpgrade(UpgradeManager manager, int dataHolderSlot) {
        super(manager, dataHolderSlot, new Point(40, 28));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public WidgetBase<BackpackScreen> createWidget(BackpackScreen screen, int x, int y) {
        return new UpgradeWidgetBase<>(screen, this, new Point(screen.getGuiLeft() + x, screen.getGuiTop() + y), new Point(137, 0),
                "item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade");
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
