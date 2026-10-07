package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class EverburningUrnBlock extends BaseEntityBlock {
    public static final MapCodec<EverburningUrnBlock> CODEC = simpleCodec(EverburningUrnBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
            box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0),
            box(5.0, 10.0, 5.0, 11.0, 14.0, 11.0),
            box(4.0, 14.0, 4.0, 12.0, 16.0, 12.0));

    public EverburningUrnBlock(Properties properties) {
        super(properties);
    }

    private static void playLavaSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 0.33F,
                1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F);
    }

    @Override
    protected @NonNull MapCodec<EverburningUrnBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull VoxelShape getShape(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new EverburningUrnBlockEntity(pos, state);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(
            @NonNull ItemStack stack,
            @NonNull BlockState state,
            Level level,
            @NonNull BlockPos pos,
            @NonNull Player player,
            @NonNull InteractionHand hand,
            @NonNull BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockEntity(pos) instanceof EverburningUrnBlockEntity urn)) {
            return InteractionResult.PASS;
        }
        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection())) {
            urn.setChanged();
            playLavaSound(level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            @NonNull BlockState state,
            @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, TABlockEntities.EVERBURNING_URN.get(), EverburningUrnBlockEntity::serverTick);
    }
}
