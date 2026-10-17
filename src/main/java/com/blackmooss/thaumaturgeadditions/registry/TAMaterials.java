package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public final class TAMaterials {
    public static final ArmorMaterial ARMOR_VOID_TRAVELLER = new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.BOOTS, 4,
                    ArmorItem.Type.LEGGINGS, 7,
                    ArmorItem.Type.CHESTPLATE, 9,
                    ArmorItem.Type.HELMET, 4),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(TTItemTags.INGOTS_VOID_METAL),
            // 照抄 VOID_ROBE 双层：基础层(靴形，不染) + overlay 层(可染)
            // 基础 -> void_traveller_layer_1/2.png；overlay(suffix "_over") -> void_traveller_layer_1_over/2_over.png
            List.of(
                    new ArmorMaterial.Layer(ThaumaturgeAdditions.identifier("void_traveller"), "", false),
                    new ArmorMaterial.Layer(ThaumaturgeAdditions.identifier("void_traveller"), "_over", true)),
            2.0F,
            0.0F);

    public static final Holder<ArmorMaterial> ARMOR_VOID_TRAVELLER_HOLDER = Holder.direct(ARMOR_VOID_TRAVELLER);

    private TAMaterials() {
    }
}