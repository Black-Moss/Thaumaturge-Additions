package com.blackmooss.thaumaturgeaddon.client;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeaddon.client.render.blockentity.VoidBrainJarRenderer;
import com.blackmooss.thaumaturgeaddon.registry.TABlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class TABlockEntityRenderers {
    private TABlockEntityRenderers() {
    }

    @SubscribeEvent
    public static void onRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TABlockEntities.VOID_BRAIN_JAR.get(), VoidBrainJarRenderer::new);
    }
}
