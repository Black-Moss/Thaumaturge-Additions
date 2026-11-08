package com.blackmooss.thaumaturgeadditions.data.tag;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ThaumaturgeAdditions.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(TTItemTags.SCRIBING_TOOLS)
                .add(TAItems.RAINBOW_SCRIBING_TOOLS.get());

        tag(TTItemTags.TRIMMABLE_ARMOR)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(TTItemTags.ARMORS_BOOTS)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.ARMOR_ENCHANTABLE)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(Tags.Items.ARMORS)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());
        tag(Tags.Items.ENCHANTABLES)
                .add(TAItems.VOID_TRAVELLER_BOOTS.get());

        tag(Tags.Items.CHESTS)
                .add(TAItems.HUNGRY_TRAPPED_CHEST.get());
        tag(Tags.Items.CHESTS_WOODEN)
                .add(TAItems.HUNGRY_TRAPPED_CHEST.get());
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge Additions Item Tags";
    }
}
