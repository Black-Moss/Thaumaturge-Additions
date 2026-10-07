package com.blackmooss.thaumaturgeadditions.client.tint;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.awt.*;

public record RainbowTintSource() implements ItemTintSource {
    public static final Identifier ID = ThaumaturgeAdditions.identifier("rainbow");
    public static final MapCodec<RainbowTintSource> MAP_CODEC = MapCodec.unit(RainbowTintSource::new);

    private static final long CYCLE_MS = 3333L;

    @Override
    public int calculate(
            @NonNull ItemStack itemStack,
            @Nullable ClientLevel level,
            @Nullable LivingEntity owner) {
        float hue = (System.currentTimeMillis() % CYCLE_MS) / (float) CYCLE_MS;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }

    @Override
    public @NonNull MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
