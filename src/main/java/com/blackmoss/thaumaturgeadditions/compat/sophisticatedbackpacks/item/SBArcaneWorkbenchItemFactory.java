package com.blackmoss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import net.minecraft.world.item.Item;

import java.util.Objects;

public final class SBArcaneWorkbenchItemFactory {
    private SBArcaneWorkbenchItemFactory() {
    }

    public static Item create(Item.Properties properties) {
        return new SBArcaneWorkbenchUpgradeItem(Objects.requireNonNull(properties, "properties"));
    }
}
