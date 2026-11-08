package com.blackmooss.thaumaturgeadditions.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.client.render.item.HungryTrappedChestItemRenderer;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.Lazy;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class TAItemRenderExtensions {
    private static final Lazy<BlockEntityWithoutLevelRenderer> RENDERER = Lazy.of(() -> {
        Minecraft minecraft = Minecraft.getInstance();
        return new HungryTrappedChestItemRenderer(
                minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
    });

    private TAItemRenderExtensions() {
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RENDERER.get();
            }
        }, TAItems.HUNGRY_TRAPPED_CHEST.get());
    }
}
