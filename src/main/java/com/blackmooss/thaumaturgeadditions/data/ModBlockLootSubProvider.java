package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {

    public ModBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return TABlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.value()).toList();
    }

    @Override
    protected void generate() {
        dropSelf(TABlocks.VOID_BRAIN_JAR.get());
        dropSelf(TABlocks.EVERBURNING_URN.get());
    }
}
