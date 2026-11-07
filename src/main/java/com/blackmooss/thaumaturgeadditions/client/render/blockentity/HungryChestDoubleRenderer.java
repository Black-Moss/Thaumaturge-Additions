package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.content.device.BlockEntityHungryChest;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.jspecify.annotations.NonNull;

public final class HungryChestDoubleRenderer extends ChestRenderer<BlockEntityHungryChest> {
    private static final MultiblockChestResources<SpriteId> SPRITES = new MultiblockChestResources<>(
            Sheets.CHEST_MAPPER.apply(TTIds.rl("hungry")),
            Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_left")),
            Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_right")));

    public HungryChestDoubleRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected @NonNull SpriteId getCustomSprite(@NonNull BlockEntityHungryChest blockEntity, ChestRenderState renderState) {
        return SPRITES.select(renderState.type);
    }
}
