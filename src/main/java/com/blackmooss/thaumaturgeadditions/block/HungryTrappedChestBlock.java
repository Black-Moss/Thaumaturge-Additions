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
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.NonNull;

public class HungryTrappedChestBlock extends ChestBlock {
    public static final MapCodec<HungryTrappedChestBlock> CODEC = simpleCodec(HungryTrappedChestBlock::new);

    public HungryTrappedChestBlock(BlockBehaviour.Properties properties) {
        super(TABlockEntities.HUNGRY_TRAPPED_CHEST::get, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
    }

    private static int stackSignal(@NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull BlockState state) {
        float ratio = 0.0F;
        if (level.getBlockEntity(pos) instanceof Container container) {
            ratio = maxStackRatio(container);
        }
        if (state.getValue(ChestBlock.TYPE) != ChestType.SINGLE
                && level.getBlockEntity(pos.relative(ChestBlock.getConnectedDirection(state)))
                instanceof Container neighbour) {
            ratio = Math.max(ratio, maxStackRatio(neighbour));
        }
        return Mth.clamp(Mth.floor(ratio * 15.0F), 0, 15);
    }

    private static float maxStackRatio(@NonNull Container container) {
        float ratio = 0.0F;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            int limit = Math.min(container.getMaxStackSize(), stack.getMaxStackSize());
            if (limit > 0) {
                ratio = Math.max(ratio, (float) stack.getCount() / limit);
            }
        }
        return ratio;
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
                stackSignal(level, pos, state));
    }

    @Override
    protected int getAnalogOutputSignal(
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull Direction direction) {
        return stackSignal(level, pos, state);
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
