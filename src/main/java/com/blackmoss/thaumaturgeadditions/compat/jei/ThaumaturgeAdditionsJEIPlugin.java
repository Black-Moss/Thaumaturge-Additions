package com.blackmoss.thaumaturgeadditions.compat.jei;

import com.blackmoss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmoss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.compat.jei.category.ArcaneWorkbenchCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public final class ThaumaturgeAdditionsJEIPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = ThaumaturgeAdditions.identifier("jei_plugin");

    @Override
    public @NonNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipeCatalysts(@NonNull IRecipeCatalystRegistration registration) {
        if (SophisticatedBackpacksCompat.isLoaded()) {
            registration.addRecipeCatalysts(RecipeTypes.CRAFTING, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
            registration.addRecipeCatalysts(ArcaneWorkbenchCategory.RECIPE_TYPE, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
        }
    }
}
