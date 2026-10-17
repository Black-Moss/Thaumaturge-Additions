package com.blackmooss.thaumaturgeadditions.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.client.render.blockentity.VoidBrainJarRenderer;
import com.blackmooss.thaumaturgeadditions.client.tint.RainbowTintSource;
import com.blackmooss.thaumaturgeadditions.item.VoidTravellerBootsItem;
import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class ThaumaturgeAdditionsClient {
    private ThaumaturgeAdditionsClient() {
    }

    @SubscribeEvent
    public static void onRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TABlockEntities.VOID_BRAIN_JAR.get(), VoidBrainJarRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        //noinspection deprecation
        event.getItemColors().register(RainbowTintSource::tint, TAItems.RAINBOW_SCRIBING_TOOLS.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isPaused()) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null || !player.isAlive()) {
            return;
        }
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() instanceof VoidTravellerBootsItem) {
            VoidTravellerBootsItem.clientMovementTick(player, boots);
        }
    }
}
