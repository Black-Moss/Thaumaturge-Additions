package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public final class TAMaterials {
    public static final ResourceKey<EquipmentAsset> ASSET_VOID_TRAVELLER =
            ResourceKey.create(EquipmentAssets.ROOT_ID, ThaumaturgeAdditions.identifier("void_traveller"));

    public static final ArmorMaterial ARMOR_VOID_TRAVELLER = new ArmorMaterial(
            25,
            Map.of(
                    ArmorType.BOOTS, 4,
                    ArmorType.LEGGINGS, 7,
                    ArmorType.CHESTPLATE, 9,
                    ArmorType.HELMET, 4),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            2.0F,
            0.0F,
            TTItemTags.INGOTS_VOID_METAL,
            ASSET_VOID_TRAVELLER
    );

    private TAMaterials() {
    }
}
