package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TADataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ThaumaturgeAdditions.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BACKPACK_WORKBENCH_AURA =
            DATA_COMPONENT_TYPES.registerComponentType("backpack_workbench_aura",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    private TADataComponents() {
    }
}
