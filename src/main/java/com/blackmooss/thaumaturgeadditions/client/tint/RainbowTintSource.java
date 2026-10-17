package com.blackmooss.thaumaturgeadditions.client.tint;

import net.minecraft.world.item.ItemStack;

import java.awt.Color;

public final class RainbowTintSource {
    private static final long CYCLE_MS = 3333L;

    private RainbowTintSource() {
    }

    public static int tint(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) {
            return -1;
        }
        float hue = (System.currentTimeMillis() % CYCLE_MS) / (float) CYCLE_MS;
        return Color.HSBtoRGB(hue, 0.8F, 1.0F);
    }
}