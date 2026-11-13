package com.blackmooss.thaumaturgeadditions.client.render.item;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class HungryTrappedChestItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Material MATERIAL = new Material(Sheets.CHEST_SHEET,
            ThaumaturgeAdditions.identifier("entity/chest/hungry_trapped"));

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public HungryTrappedChestItemRenderer(
            BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        ModelPart root = modelSet.bakeLayer(ModelLayers.CHEST);
        this.bottom = root.getChild("bottom");
        this.lid = root.getChild("lid");
        this.lock = root.getChild("lock");
    }

    @Override
    public void renderByItem(
            @NonNull ItemStack stack,
            @NonNull ItemDisplayContext context,
            PoseStack poseStack,
            @NonNull MultiBufferSource buffer,
            int packedLight,
            int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        this.lid.xRot = 0.0F;
        VertexConsumer consumer = MATERIAL.buffer(buffer, RenderType::entityCutout);
        this.lid.render(poseStack, consumer, packedLight, packedOverlay);
        this.lock.render(poseStack, consumer, packedLight, packedOverlay);
        this.bottom.render(poseStack, consumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
