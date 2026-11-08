package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class HungryTrappedChestBlock extends ChestBlock {
    public static final MapCodec<HungryTrappedChestBlock> CODEC = simpleCodec(HungryTrappedChestBlock::new);

    public HungryTrappedChestBlock(BlockBehaviour.Properties properties) {
        super(properties, TABlockEntities.HUNGRY_TRAPPED_CHEST::get);
    }

    private static int pulseSignal(@NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull BlockState state) {
        int strength = strengthAt(level, pos);
        if (state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            strength = Math.max(strength,
                    strengthAt(level, pos.relative(ChestBlock.getConnectedDirection(state))));
        }
        return strength;
    }

    private static int strengthAt(@NonNull BlockGetter level, @NonNull BlockPos pos) {
        return level.getBlockEntity(pos) instanceof HungryTrappedChestBlockEntity chest
                ? chest.getPulseStrength()
                : 0;
    }

    private static boolean hasCustomName(@NonNull Level level, @NonNull BlockPos pos) {
        return level.getBlockEntity(pos) instanceof BaseContainerBlockEntity container
                && container.getCustomName() != null;
    }

    @Override
    public @NonNull MapCodec<HungryTrappedChestBlock> codec() {
        return CODEC;
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new HungryTrappedChestBlockEntity(pos, state);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos) {
        MenuProvider provider = super.getMenuProvider(state, level, pos);
        if (provider == null || state.getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
            return provider;
        }
        if (hasCustomName(level, pos)
                || hasCustomName(level, pos.relative(ChestBlock.getConnectedDirection(state)))) {
            return provider;
        }
        return new MenuProvider() {
            @Override
            public @NonNull Component getDisplayName() {
                return Component.translatable("gui.thaumaturgeadditions.large_hungry_trapped_chest.title");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(
                    int containerId,
                    @NonNull Inventory inventory,
                    @NonNull Player player) {
                return provider.createMenu(containerId, inventory, player);
            }
        };
    }

    @Override
    protected @NonNull Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    protected boolean isSignalSource(@NonNull BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull Direction direction) {
        return Math.max(Mth.clamp(ChestBlockEntity.getOpenCount(level, pos), 0, 15),
                pulseSignal(level, pos, state));
    }

    @Override
    protected int getAnalogOutputSignal(
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos) {
        return pulseSignal(level, pos, state);
    }

    @Override
    protected int getDirectSignal(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull Direction direction) {
        return direction == Direction.UP
                ? state.getSignal(level, pos, direction)
                : 0;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, TABlockEntities.HUNGRY_TRAPPED_CHEST.get(),
                HungryTrappedChestBlockEntity::lidAnimateTick)
                : createTickerHelper(type, TABlockEntities.HUNGRY_TRAPPED_CHEST.get(),
                HungryTrappedChestBlockEntity::serverTick);
    }
}
