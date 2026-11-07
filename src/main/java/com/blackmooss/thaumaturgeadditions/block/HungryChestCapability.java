package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.leclowndu93150.thaumaturge.registry.TTBlocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class HungryChestCapability {
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, ResourceHandler<ItemResource>> CHEST_HANDLER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public @NonNull ResourceHandler<ItemResource> acceptDouble(
                @NonNull ChestBlockEntity first,
                @NonNull ChestBlockEntity second) {
            return new CombinedResourceHandler<>(VanillaContainerWrapper.of(first), VanillaContainerWrapper.of(second));
        }

        @Override
        public @NonNull ResourceHandler<ItemResource> acceptSingle(
                @NonNull ChestBlockEntity single) {
            return VanillaContainerWrapper.of(single);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public @Nullable ResourceHandler<ItemResource> acceptNone() {
            return null;
        }
    };

    private HungryChestCapability() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.Item.BLOCK, (level, pos, state, _, _) -> {
            if (!(state.getBlock() instanceof ChestBlock chestBlock)) {
                return null;
            }
            return chestBlock.combine(state, level, pos, true).apply(CHEST_HANDLER);
        }, TTBlocks.HUNGRY_CHEST.get(), TABlocks.HUNGRY_TRAPPED_CHEST.get());
    }
}
