package com.blackmooss.thaumaturgeadditions.client.equipment;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.item.VoidTravellerBootsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID, value = Dist.CLIENT)
public final class VoidTravellerBootsClientHandler {
    private VoidTravellerBootsClientHandler() {
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
