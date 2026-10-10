package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class TATags {
    private TATags() {
    }

    public static final TagKey<Block> MAGIC_TRIGGER_BLOCKS =
            TagKey.create(Registries.BLOCK, ThaumaturgeAdditions.identifier("magic_trigger_blocks"));

    public static final TagKey<Block> SALIS_MUNDUS_TRIGGER_BLOCKS =
            TagKey.create(Registries.BLOCK, ThaumaturgeAdditions.identifier("salis_mundus_trigger_blocks"));
}