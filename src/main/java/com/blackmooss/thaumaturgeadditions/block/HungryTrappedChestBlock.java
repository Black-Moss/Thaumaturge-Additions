package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class HungryTrappedChestBlock extends ChestBlock {
    public static final MapCodec<HungryTrappedChestBlock> CODEC = simpleCodec(HungryTrappedChestBlock::new);

    public HungryTrappedChestBlock(BlockBehaviour.Properties properties) {
        super(TABlockEntities.HUNGRY_TRAPPED_CHEST::get, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
    }

    private static int pulseSignal(@NonNull BlockGetter level, @NonNull BlockPos pos) {
        return level.getBlockEntity(pos) instanceof HungryTrappedChestBlockEntity chest
                ? chest.getPulseStrength()
                : 0;
    }

    @Override
    public @NonNull MapCodec<HungryTrappedChestBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean chestCanConnectTo(@NonNull BlockState blockState) {
        return blockState.is(this);
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new HungryTrappedChestBlockEntity(pos, state);
    }

    @Override
    protected @NonNull Stat<Identifier> getOpenChestStat() {
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
                pulseSignal(level, pos));
    }

    @Override
    protected int getAnalogOutputSignal(
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull Direction direction) {
        return pulseSignal(level, pos);
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, TABlockEntities.HUNGRY_TRAPPED_CHEST.get(), HungryTrappedChestBlockEntity::lidAnimateTick)
                : createTickerHelper(type, TABlockEntities.HUNGRY_TRAPPED_CHEST.get(), HungryTrappedChestBlockEntity::serverTick);
    }
}
