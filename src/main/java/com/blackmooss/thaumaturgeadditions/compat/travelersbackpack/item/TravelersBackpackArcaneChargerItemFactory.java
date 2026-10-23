package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class TravelersBackpackArcaneChargerItemFactory {
    private TravelersBackpackArcaneChargerItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new TravelersBackpackArcaneChargerUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
