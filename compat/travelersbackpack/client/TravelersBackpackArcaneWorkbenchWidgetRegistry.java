package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetRegistry;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;

public final class TravelersBackpackArcaneWorkbenchWidgetRegistry {
    private TravelersBackpackArcaneWorkbenchWidgetRegistry() {
    }

    public static void register() {
        UpgradeWidgetRegistry.register(TravelersBackpackArcaneWorkbenchUpgrade.class, (screen, upgrade, x, y) ->
                new TravelersBackpackArcaneWorkbenchWidget(screen, upgrade, new Point(screen.getLeftPos() + x, screen.getTopPos() + y)));
    }
}
