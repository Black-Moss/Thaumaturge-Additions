package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.block.HungryTrappedChestBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class HungryTrappedChestRenderer implements BlockEntityRenderer<HungryTrappedChestBlockEntity, HungryTrappedChestRenderState> {
    private static final SpriteId SPRITE = Sheets.CHEST_MAPPER.apply(ThaumaturgeAdditions.identifier("hungry_trapped"));
    private final SpriteGetter sprites;
    private final ChestModel model;

    public HungryTrappedChestRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
    }

    @Override
    public @NonNull HungryTrappedChestRenderState createRenderState() {
        return new HungryTrappedChestRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull HungryTrappedChestBlockEntity chest,
            @NonNull HungryTrappedChestRenderState state,
            float partialTicks,
            @NonNull Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(chest, state, partialTicks, cameraPosition, breakProgress);
        state.facing = chest.getBlockState().getValue(ChestBlock.FACING);
        state.open = chest.getOpenNess(partialTicks);
    }

    @Override
    public void submit(
            @NonNull HungryTrappedChestRenderState state,
            @NonNull PoseStack stack,
            @NonNull SubmitNodeCollector collector,
            @NonNull CameraRenderState cameraRenderState) {
        stack.pushPose();
        stack.mulPose(ChestRenderer.modelTransformation(state.facing));
        float open = 1.0F - state.open;
        open = 1.0F - open * open * open;
        collector.submitModel(
                this.model,
                open, stack,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1, SPRITE, this.sprites,
                0, state.breakProgress);
        stack.popPose();
    }
}
