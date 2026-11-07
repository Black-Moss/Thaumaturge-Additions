package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.block.HungryTrappedChestBlockEntity;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.jspecify.annotations.NonNull;

public final class HungryTrappedChestRenderer extends ChestRenderer<HungryTrappedChestBlockEntity> {
    private static final MultiblockChestResources<SpriteId> SPRITES = new MultiblockChestResources<>(
            Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_trapped")),
            Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_trapped_left")),
            Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_trapped_right")));

    public HungryTrappedChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected @NonNull SpriteId getCustomSprite(
            @NonNull HungryTrappedChestBlockEntity blockEntity,
            ChestRenderState renderState) {
        return SPRITES.select(renderState.type);
    }
}
