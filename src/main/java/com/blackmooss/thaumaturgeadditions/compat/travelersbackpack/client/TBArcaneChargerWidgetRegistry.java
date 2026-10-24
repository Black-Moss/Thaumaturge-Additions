package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneChargerUpgrade;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetRegistry;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;

public final class TBArcaneChargerWidgetRegistry {
    private TBArcaneChargerWidgetRegistry() {
    }

    public static void register() {
        UpgradeWidgetRegistry.register(TBArcaneChargerUpgrade.class,
                (screen, upgrade, x, y) -> new UpgradeWidgetBase<>(screen, upgrade,
                        new Point(screen.getLeftPos() + x, screen.getTopPos() + y), new Point(137, 0),
                        "item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade"));
    }
}
