package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class SophisticatedBackpacksArcaneChargerItemFactory {
    private SophisticatedBackpacksArcaneChargerItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new SophisticatedBackpacksArcaneChargerUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
