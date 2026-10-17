package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.item.VoidTravellerBootsItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TAItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeAdditions.MODID);

    public static final DeferredItem<BlockItem> VOID_BRAIN_JAR = ITEMS.registerSimpleBlockItem(TABlocks.VOID_BRAIN_JAR);
    public static final DeferredItem<BlockItem> EVERBURNING_URN = ITEMS.registerSimpleBlockItem(TABlocks.EVERBURNING_URN);

    public static final DeferredItem<VoidTravellerBootsItem> VOID_TRAVELLER_BOOTS = ITEMS.registerItem(
            "void_traveller_boots", VoidTravellerBootsItem::new, new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .durability(350));

    public static final DeferredItem<Item> RAINBOW_SCRIBING_TOOLS = ITEMS.registerItem(
            "rainbow_scribing_tools", Item::new, new Item.Properties()
                    .stacksTo(1)
                    .durability(100));

    // public static final DeferredItem<Item> SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE = ITEMS.registerItem(
    //            "sophisticatedbackpacks_arcane_workbench_upgrade", SophisticatedBackpacksCompat::createArcaneWorkbenchUpgradeItem, props -> props
    //                    .stacksTo(16));
    //
    //    public static final DeferredItem<Item> TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE = ITEMS.registerItem(
    //            "travelersbackpack_arcane_workbench_upgrade", TravelersBackpackCompat::createArcaneWorkbenchUpgradeItem, props -> props
    //                    .stacksTo(16));
    //
    //    public static final DeferredItem<Item> SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE = ITEMS.registerItem(
    //            "sophisticatedbackpacks_arcane_charger_upgrade", SophisticatedBackpacksCompat::createArcaneChargerUpgradeItem, props -> props
    //                    .stacksTo(16));
    //    public static final DeferredItem<Item> TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE = ITEMS.registerItem(
    //            "travelersbackpack_arcane_charger_upgrade", TravelersBackpackCompat::createArcaneChargerUpgradeItem, props -> props
    //                    .stacksTo(16));

    private TAItems() {
    }
}
