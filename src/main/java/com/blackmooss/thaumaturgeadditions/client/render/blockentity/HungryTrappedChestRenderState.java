package com.blackmooss.thaumaturgeadditions.client.render.blockentity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class HungryTrappedChestRenderState extends BlockEntityRenderState {
    public Direction facing;
    public float open;

    public HungryTrappedChestRenderState() {
        this.facing = Direction.SOUTH;
    }
}
