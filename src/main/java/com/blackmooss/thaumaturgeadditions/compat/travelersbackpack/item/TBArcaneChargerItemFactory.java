package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class TBArcaneChargerItemFactory {
    public static Item create(Item.Properties properties) {
        return new TBArcaneChargerUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
