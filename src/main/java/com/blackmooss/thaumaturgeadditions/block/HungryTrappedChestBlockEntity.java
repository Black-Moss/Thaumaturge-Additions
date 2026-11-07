package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.leclowndu93150.thaumaturge.api.items.InvHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

public class HungryTrappedChestBlockEntity extends ChestBlockEntity {
    private static final int PULSE_DURATION_TICKS = 4;
    private static final int PULSE_EVENT = 2;
    private final AABB swallowBox;
    private int lidCloseTimer;
    private int pulseStrength;
    private int pulseTicks;

    public HungryTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        super(TABlockEntities.HUNGRY_TRAPPED_CHEST.get(), pos, state);
        this.swallowBox = (new AABB(pos))
                .inflate(0.1, 0.0F, 0.1)
                .expandTowards(0.0F, 0.3, 0.0F);
    }

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            HungryTrappedChestBlockEntity chest) {
        if (chest.lidCloseTimer > 0 && --chest.lidCloseTimer == 0) {
            level.blockEvent(pos, state.getBlock(), 1, 0);
        }

        boolean pulseChanged = false;
        if (chest.pulseTicks > 0 && --chest.pulseTicks == 0) {
            chest.pulseStrength = 0;
            pulseChanged = true;
        }

        boolean swallowed = false;
        int pulse = 0;
        RandomSource random = level.getRandom();

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, chest.swallowBox)) {
            if (item.isRemoved()) {
                continue;
            }
            ItemStack stack = item.getItem();
            int limit = stack.isEmpty() ? 0 : stack.getMaxStackSize();
            int accepted = chest.swallow(level, pos, item, random);
            if (accepted > 0) {
                swallowed = true;
                pulse = Math.max(pulse, signalFor(accepted, limit));
            }
        }

        if (swallowed) {
            chest.setChanged();
            chest.lidCloseTimer = 4;
            level.blockEvent(pos, state.getBlock(), 1, 1);
        }
        if (pulse > 0) {
            chest.pulseStrength = pulse;
            chest.pulseTicks = PULSE_DURATION_TICKS;
            pulseChanged = true;
        }
        if (pulseChanged) {
            level.blockEvent(pos, state.getBlock(), PULSE_EVENT, chest.pulseStrength);
            notifyRedstone(level, pos, state);
        }
    }

    private static int signalFor(int count, int limit) {
        if (count <= 0 || limit <= 0) {
            return 0;
        }
        return Mth.clamp(Mth.floor((float) count / limit * 15.0F), 0, 15);
    }

    private static void notifyRedstone(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide() || !state.hasProperty(TrappedChestBlock.FACING)) {
            return;
        }
        Block block = state.getBlock();
        Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level,
                state.getValue(TrappedChestBlock.FACING).getOpposite(), Direction.UP);
        level.updateNeighborsAt(pos, block, orientation);
        level.updateNeighbourForOutputSignal(pos, block);
    }

    public int getPulseStrength() {
        return pulseStrength;
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if (id == PULSE_EVENT) {
            pulseStrength = Mth.clamp(param, 0, 15);
            return true;
        }
        return super.triggerEvent(id, param);
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return TABlocks.HUNGRY_TRAPPED_CHEST.get().getName();
    }

    @Override
    protected void signalOpenCount(
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull BlockState state,
            int previous,
            int current) {
        super.signalOpenCount(level, pos, state, previous, current);
        if (previous != current) {
            Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level,
                    state.getValue(TrappedChestBlock.FACING).getOpposite(), Direction.UP);
            Block block = state.getBlock();
            level.updateNeighborsAt(pos, block, orientation);
            level.updateNeighborsAt(pos.below(), block, orientation);
        }
    }

    private int swallow(Level level, BlockPos pos, ItemEntity item, RandomSource random) {
        ItemStack stack = item.getItem();
        int originalCount = stack.getCount();
        int remaining = InvHelper.insertStackAt(level, pos, Direction.UP, stack, false).getCount();
        int accepted = originalCount - remaining;
        if (accepted <= 0) {
            return 0;
        }
        if (remaining <= 0) {
            item.discard();
        } else {
            stack.shrink(accepted);
        }
        item.playSound(SoundEvents.GENERIC_EAT.value(), 0.25F,
                (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        return accepted;
    }
}
