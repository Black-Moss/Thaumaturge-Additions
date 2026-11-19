package com.blackmoss.thaumaturgeadditions.client.render.blockentity;

import com.blackmoss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.thaumaturgeadditions.block.HungryTrappedChestBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;

public final class HungryTrappedChestRenderer extends ChestRenderer<HungryTrappedChestBlockEntity> {
    private static final Material SINGLE = chestMaterial("hungry_trapped");
    private static final Material LEFT = chestMaterial("hungry_trapped_left");
    private static final Material RIGHT = chestMaterial("hungry_trapped_right");

    public HungryTrappedChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private static Material chestMaterial(String name) {
        return new Material(Sheets.CHEST_SHEET,
                ThaumaturgeAdditions.identifier("entity/chest/" + name));
    }

    @Override
    protected Material getMaterial(HungryTrappedChestBlockEntity blockEntity, ChestType chestType) {
        return switch (chestType) {
            case LEFT -> LEFT;
            case RIGHT -> RIGHT;
            default -> SINGLE;
        };
    }
}
