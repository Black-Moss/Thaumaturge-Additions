package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetRegistry;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;

public final class TBArcaneWorkbenchWidgetRegistry {
    private TBArcaneWorkbenchWidgetRegistry() {
    }

    public static void register() {
        UpgradeWidgetRegistry.register(TBArcaneWorkbenchUpgrade.class, (screen, upgrade, x, y) ->
                new TBArcaneWorkbenchWidget(screen, upgrade, new Point(screen.getLeftPos() + x, screen.getTopPos() + y)));
    }
}
