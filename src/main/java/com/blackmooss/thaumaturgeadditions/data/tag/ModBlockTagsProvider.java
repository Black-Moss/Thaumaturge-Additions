package com.blackmooss.thaumaturgeadditions.data.tag;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ThaumaturgeAdditions.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TABlocks.EVERBURNING_URN.get());
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge Additions Block Tags";
    }
}
