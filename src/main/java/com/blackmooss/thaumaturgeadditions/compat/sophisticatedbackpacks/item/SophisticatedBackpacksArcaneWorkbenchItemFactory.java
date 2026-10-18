package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class SophisticatedBackpacksArcaneWorkbenchItemFactory {
    private SophisticatedBackpacksArcaneWorkbenchItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new SophisticatedBackpacksArcaneWorkbenchUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
