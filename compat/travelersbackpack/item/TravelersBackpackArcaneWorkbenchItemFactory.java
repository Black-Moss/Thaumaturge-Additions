package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class TravelersBackpackArcaneWorkbenchItemFactory {
    private TravelersBackpackArcaneWorkbenchItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new TravelersBackpackArcaneWorkbenchUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
