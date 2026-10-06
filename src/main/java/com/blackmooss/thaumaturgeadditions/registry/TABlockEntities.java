package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import java.util.Set;

import com.blackmooss.thaumaturgeadditions.block.EverburningUrnBlockEntity;
import com.blackmooss.thaumaturgeadditions.block.VoidBrainJarBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class TABlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ThaumaturgeAdditions.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VoidBrainJarBlockEntity>> VOID_BRAIN_JAR =
            BLOCK_ENTITIES.register("void_brain_jar", () -> new BlockEntityType<>(VoidBrainJarBlockEntity::new, Set.of(TABlocks.VOID_BRAIN_JAR.get())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EverburningUrnBlockEntity>> EVERBURNING_URN =
            BLOCK_ENTITIES.register("everburning_urn", () -> new BlockEntityType<>(EverburningUrnBlockEntity::new, Set.of(TABlocks.EVERBURNING_URN.get())));

    private TABlockEntities() {
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, TABlockEntities.EVERBURNING_URN.get(),
                (be, _) -> be.getTank());
    }
}
