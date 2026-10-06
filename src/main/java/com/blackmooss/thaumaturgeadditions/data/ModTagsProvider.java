package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModTagsProvider implements DataProvider {
    private final IntrinsicHolderTagsProvider<Item> itemTags;
    private final IntrinsicHolderTagsProvider<Block> blockTags;


    public ModTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        //noinspection deprecation
        this.itemTags = new IntrinsicHolderTagsProvider<>(output, Registries.ITEM, lookup,
                item -> item.builtInRegistryHolder().key(), ThaumaturgeAdditions.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
            }
        };

        //noinspection deprecation
        this.blockTags = new IntrinsicHolderTagsProvider<>(output, Registries.BLOCK, lookup,
                block -> block.builtInRegistryHolder().key(), ThaumaturgeAdditions.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
            }
        };
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        return CompletableFuture.allOf(itemTags.run(cache), blockTags.run(cache));
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge Additions Tags";
    }
}
