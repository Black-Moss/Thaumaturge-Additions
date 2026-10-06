package com.blackmooss.thaumaturgeaddon.block;

import com.blackmooss.thaumaturgeaddon.registry.TABlockEntities;
import com.leclowndu93150.thaumaturge.registry.TCSounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class VoidBrainJarBlock extends BaseEntityBlock {
    public static final MapCodec<VoidBrainJarBlock> CODEC = simpleCodec(VoidBrainJarBlock::new);
    private static final VoxelShape SHAPE = box(3.0F, 0.0F, 3.0F, 13.0F, 12.0F, 13.0F);

    public VoidBrainJarBlock(Properties properties) {
        super(properties);
    }

    protected @NonNull MapCodec<VoidBrainJarBlock> codec() {
        return CODEC;
    }

    protected @NonNull VoxelShape getShape(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return SHAPE;
    }

    protected @NonNull VoxelShape getCollisionShape(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return SHAPE;
    }

    public @NonNull BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new VoidBrainJarBlockEntity(pos, state);
    }

    protected @NonNull InteractionResult useWithoutItem(
            @NonNull BlockState state,
            Level level,
            @NonNull BlockPos pos,
            @NonNull Player player,
            @NonNull BlockHitResult hitResult) {
        BlockEntity var7 = level.getBlockEntity(pos);
        if (var7 instanceof VoidBrainJarBlockEntity jar) {
            jar.setEatDelay(40);
            if (level instanceof ServerLevel server) {
                int release = server.getRandom().nextInt(Math.min(jar.xp() + 1, 64));
                if (release > 0) {
                    jar.setXp(jar.xp() - release);
                    ExperienceOrb.award(server, new Vec3(
                            (double)pos.getX() + (double)0.5F,
                            (double)pos.getY() + (double)0.5F,
                            (double)pos.getZ() + (double)0.5F),
                            release);
                    jar.setChanged();
                    jar.syncToClient();
                }
            } else {
                level.playSound(
                        player,
                        (double)pos.getX() + (double)0.5F,
                        (double)pos.getY() + (double)0.5F,
                        (double)pos.getZ() + (double)0.5F,
                        TCSounds.JAR.get(),
                        SoundSource.BLOCKS,
                        0.2F,
                        1.0F);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            @NonNull BlockState state,
            @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, TABlockEntities.VOID_BRAIN_JAR.get(), level.isClientSide()
                ? VoidBrainJarBlockEntity::clientTick
                : VoidBrainJarBlockEntity::serverTick);
    }
}
