package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.TravelersBackpackCompat;
import com.blackmooss.thaumaturgeadditions.item.VoidTravellerBootsItem;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TAItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeAdditions.MODID);

    public static final DeferredItem<BlockItem> VOID_BRAIN_JAR = ITEMS.registerSimpleBlockItem(TABlocks.VOID_BRAIN_JAR);
    public static final DeferredItem<BlockItem> EVERBURNING_URN = ITEMS.registerSimpleBlockItem(TABlocks.EVERBURNING_URN);

    public static final DeferredItem<VoidTravellerBootsItem> VOID_TRAVELLER_BOOTS = ITEMS.registerItem(
            "void_traveller_boots", VoidTravellerBootsItem::new, props -> props
                    .humanoidArmor(TAMaterials.ARMOR_VOID_TRAVELLER, ArmorType.BOOTS)
                    .durability(350)
                    .rarity(Rarity.EPIC)
                    .attributes(TAMaterials.ARMOR_VOID_TRAVELLER.createAttributes(ArmorType.BOOTS)
                            .withModifierAdded(Attributes.SAFE_FALL_DISTANCE, VoidTravellerBootsItem.SAFE_FALL_MODIFIER, EquipmentSlotGroup.FEET)));

    public static final DeferredItem<Item> RAINBOW_SCRIBING_TOOLS = ITEMS.registerItem(
            "rainbow_scribing_tools", Item::new, props -> props
                    .stacksTo(1)
                    .durability(100));

    public static final DeferredItem<Item> TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE = ITEMS.registerItem(
            "travelersbackpack_arcane_workbench_upgrade", TravelersBackpackCompat::createArcaneWorkbenchUpgradeItem, props -> props
                    .stacksTo(16));


    private TAItems() {
    }
}
