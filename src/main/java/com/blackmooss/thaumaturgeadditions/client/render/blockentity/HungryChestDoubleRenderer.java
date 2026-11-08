package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.content.device.BlockEntityHungryChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.NonNull;

public final class HungryChestDoubleRenderer extends ChestRenderer<BlockEntityHungryChest> {
    private static final Material SINGLE = chestMaterial(TTIds.rl("entity/chest/hungry"));
    private static final Material LEFT = chestMaterial(ThaumaturgeAdditions.identifier("entity/chest/hungry_left"));
    private static final Material RIGHT = chestMaterial(ThaumaturgeAdditions.identifier("entity/chest/hungry_right"));

    private static Material chestMaterial(ResourceLocation texture) {
        return new Material(Sheets.CHEST_SHEET, texture);
    }

    public HungryChestDoubleRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected @NonNull Material getMaterial(@NonNull BlockEntityHungryChest blockEntity, ChestType chestType) {
        return switch (chestType) {
            case LEFT -> LEFT;
            case RIGHT -> RIGHT;
            default -> SINGLE;
        };
    }
}
