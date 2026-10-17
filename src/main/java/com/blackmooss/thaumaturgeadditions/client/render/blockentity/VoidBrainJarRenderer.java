package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import com.blackmooss.thaumaturgeadditions.block.VoidBrainJarBlockEntity;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.client.entity.TTModelLayers;
import com.leclowndu93150.thaumaturge.client.model.entity.BrainModel;
import com.leclowndu93150.thaumaturge.client.model.entity.JarBrineModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public final class VoidBrainJarRenderer implements BlockEntityRenderer<VoidBrainJarBlockEntity> {
    private static final ResourceLocation TEX_BRAIN = TTIds.rl("textures/entity/brain2.png");
    private static final ResourceLocation TEX_BRINE = TTIds.rl("textures/entity/jarbrine.png");
    private static final float BRAIN_SCALE = 0.4F;
    private static final float BRAIN_LIFT = -0.8F;
    private static final float BOB_PERIOD = 14.0F;
    private static final float BOB_AMPLITUDE = 0.03F;
    private static final float XP_PULSE_PERIOD = 5.0F;

    private final BrainModel brain;
    private final JarBrineModel brine;

    public VoidBrainJarRenderer(BlockEntityRendererProvider.Context context) {
        this.brain = new BrainModel(context.bakeLayer(TTModelLayers.BRAIN));
        this.brine = new JarBrineModel(context.bakeLayer(TTModelLayers.JAR_BRINE));
    }

    @Override
    public void render(
            VoidBrainJarBlockEntity jar,
            float partialTick,
            @NonNull PoseStack poseStack,
            @NonNull MultiBufferSource buffers,
            int light,
            int overlay) {
        float delta = jar.rota - jar.rotb;
        while (delta >= (float) Math.PI) {
            delta -= ((float) Math.PI * 2F);
        }
        while (delta < -(float) Math.PI) {
            delta += ((float) Math.PI * 2F);
        }

        float yawRadians = jar.rotb + delta * partialTick;
        float time = (float) (Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount) + partialTick;
        float bobOffset = Mth.sin(time / BOB_PERIOD) * BOB_AMPLITUDE + BOB_AMPLITUDE;

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.01F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        poseStack.pushPose();
        poseStack.translate(0.0F, BRAIN_LIFT + bobOffset, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yawRadians * (180F / (float) Math.PI)));
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        poseStack.scale(BRAIN_SCALE, BRAIN_SCALE, BRAIN_SCALE);
        this.brain.setupAnim(time, Mth.sin(time / XP_PULSE_PERIOD) * (float) jar.xp() / 2000.0F);
        this.brain.root.render(poseStack, buffers.getBuffer(RenderType.entityCutout(TEX_BRAIN)), light, overlay);
        poseStack.popPose();

        this.brine.root.render(poseStack, buffers.getBuffer(RenderType.entityTranslucent(TEX_BRINE)), light, overlay);
        poseStack.popPose();
    }
}
