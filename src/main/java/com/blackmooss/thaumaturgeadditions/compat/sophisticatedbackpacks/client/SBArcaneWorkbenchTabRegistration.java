package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.client;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SBArcaneWorkbenchUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;

public final class SBArcaneWorkbenchTabRegistration {
    private SBArcaneWorkbenchTabRegistration() {
    }

    public static void register() {
        UpgradeGuiManager.registerTab(SBArcaneWorkbenchUpgradeContainer.TYPE, SBArcaneWorkbenchUpgradeTab::new);
    }
}
