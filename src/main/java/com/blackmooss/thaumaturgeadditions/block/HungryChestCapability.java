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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jspecify.annotations.NonNull;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class HungryChestCapability {
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, IItemHandler> CHEST_HANDLER =
            new DoubleBlockCombiner.Combiner<>() {
                @Override
                public @NonNull IItemHandler acceptDouble(@NonNull ChestBlockEntity first, @NonNull ChestBlockEntity second) {
                    return new CombinedInvWrapper(new InvWrapper(first), new InvWrapper(second));
                }

                @Override
                public @NonNull IItemHandler acceptSingle(@NonNull ChestBlockEntity single) {
                    return new InvWrapper(single);
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public IItemHandler acceptNone() {
                    return null;
                }
            };

    private HungryChestCapability() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
            if (!(state.getBlock() instanceof ChestBlock chestBlock)) {
                return null;
            }
            return chestBlock.combine(state, level, pos, true).apply(CHEST_HANDLER);
        }, TTBlocks.HUNGRY_CHEST.get(), TABlocks.HUNGRY_TRAPPED_CHEST.get());
    }
}
