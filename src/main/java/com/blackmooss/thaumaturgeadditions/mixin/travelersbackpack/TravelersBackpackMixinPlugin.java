package com.blackmooss.thaumaturgeadditions.mixin.travelersbackpack;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.List;

public final class TravelersBackpackMixinPlugin implements IMixinConfigPlugin {
    private static final String ABSTRACT_CONTAINER_MENU_MIXIN =
            "com.blackmooss.thaumaturgeadditions.mixin.travelersbackpack.AbstractContainerMenuMixin";

    private static final String TRAVELERS_BACKPACK_MENU =
            "com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu";

    private static boolean isClassPresent() {
        try {
            Class.forName(TravelersBackpackMixinPlugin.TRAVELERS_BACKPACK_MENU, false, TravelersBackpackMixinPlugin.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }

    @Override
    public @Nullable String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (ABSTRACT_CONTAINER_MENU_MIXIN.equals(mixinClassName)) {
            return isClassPresent();
        }
        return true;
    }

    @Override
    public @Nullable List<String> getMixins() {
        return null;
    }
}
