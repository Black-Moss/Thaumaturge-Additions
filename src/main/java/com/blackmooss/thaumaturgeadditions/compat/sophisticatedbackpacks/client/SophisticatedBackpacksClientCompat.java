package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class SophisticatedBackpacksClientCompat {
    private SophisticatedBackpacksClientCompat() {
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        if (!SophisticatedBackpacksCompat.isLoaded()) {
            return;
        }
        SophisticatedBackpacksArcaneWorkbenchTabRegistration.register();
    }
}
