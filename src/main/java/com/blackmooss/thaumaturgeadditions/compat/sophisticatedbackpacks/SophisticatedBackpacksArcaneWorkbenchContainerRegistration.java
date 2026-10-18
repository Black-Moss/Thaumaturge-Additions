package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SophisticatedBackpacksArcaneWorkbenchUpgradeContainer;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;

public final class SophisticatedBackpacksArcaneWorkbenchContainerRegistration {
    private SophisticatedBackpacksArcaneWorkbenchContainerRegistration() {
    }

    public static void register() {
        UpgradeContainerRegistry.register(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.getId(),
                SophisticatedBackpacksArcaneWorkbenchUpgradeContainer.TYPE);
    }
}
