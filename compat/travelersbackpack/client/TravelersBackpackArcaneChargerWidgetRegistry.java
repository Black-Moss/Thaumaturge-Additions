package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneChargerUpgrade;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetRegistry;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;

public final class TravelersBackpackArcaneChargerWidgetRegistry {
    private TravelersBackpackArcaneChargerWidgetRegistry() {
    }

    public static void register() {
        UpgradeWidgetRegistry.register(TravelersBackpackArcaneChargerUpgrade.class,
                (screen, upgrade, x, y) -> new UpgradeWidgetBase<>(screen, upgrade,
                        new Point(screen.getLeftPos() + x, screen.getTopPos() + y), new Point(137, 0),
                        "item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade"));
    }
}
