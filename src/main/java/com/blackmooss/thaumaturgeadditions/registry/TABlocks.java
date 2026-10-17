package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.block.EverburningUrnBlock;
import com.blackmooss.thaumaturgeadditions.block.VoidBrainJarBlock;
import com.leclowndu93150.thaumaturge.registry.TTSoundTypes;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TABlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgeAdditions.MODID);

    public static final DeferredBlock<VoidBrainJarBlock> VOID_BRAIN_JAR = BLOCKS.registerBlock(
            "void_brain_jar", VoidBrainJarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .strength(0.3F)
                    .sound(TTSoundTypes.JAR)
                    .noOcclusion());

    public static final DeferredBlock<EverburningUrnBlock> EVERBURNING_URN = BLOCKS.registerBlock(
            "everburning_urn", EverburningUrnBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 10.0F)
                    .sound(TTSoundTypes.URN)
                    .noOcclusion());

    private TABlocks() {
    }
}