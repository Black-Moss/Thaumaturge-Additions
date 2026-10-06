package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.TravelersBackpackCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class TravelersBackpackClientCompat {
    private TravelersBackpackClientCompat() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (!TravelersBackpackCompat.isLoaded()) {
            return;
        }
        event.enqueueWork(TravelersBackpackArcaneWorkbenchWidgetRegistry::register);
    }
}
