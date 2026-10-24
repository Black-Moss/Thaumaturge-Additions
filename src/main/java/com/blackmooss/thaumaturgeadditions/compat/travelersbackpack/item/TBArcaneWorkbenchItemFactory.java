package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class TBArcaneWorkbenchItemFactory {
    private TBArcaneWorkbenchItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new TBArcaneWorkbenchUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
