package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class SBArcaneChargerItemFactory {
    private SBArcaneChargerItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new SBArcaneChargerUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
