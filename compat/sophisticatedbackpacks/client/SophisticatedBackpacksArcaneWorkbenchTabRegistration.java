package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.client;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SophisticatedBackpacksArcaneWorkbenchUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;

public final class SophisticatedBackpacksArcaneWorkbenchTabRegistration {
    private SophisticatedBackpacksArcaneWorkbenchTabRegistration() {
    }

    public static void register() {
        UpgradeGuiManager.registerTab(SophisticatedBackpacksArcaneWorkbenchUpgradeContainer.TYPE, SophisticatedBackpacksArcaneWorkbenchUpgradeTab::new);
    }
}
