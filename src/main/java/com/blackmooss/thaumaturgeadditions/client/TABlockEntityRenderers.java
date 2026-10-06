package com.blackmooss.thaumaturgeadditions.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.client.render.blockentity.VoidBrainJarRenderer;
import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
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
