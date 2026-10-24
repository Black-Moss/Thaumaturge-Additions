package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SBArcaneWorkbenchUpgradeContainer;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;

public final class SBArcaneWorkbenchContainerRegistration {
    private SBArcaneWorkbenchContainerRegistration() {
    }

    public static void register() {
        UpgradeContainerRegistry.register(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.getId(),
                SBArcaneWorkbenchUpgradeContainer.TYPE);
    }
}
