package com.blackmooss.thaumaturgeadditions.compat.jei;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.TravelersBackpackCompat;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client.TravelersBackpackArcaneWorkbenchWidget;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.compat.jei.category.ArcaneWorkbenchCategory;
import com.leclowndu93150.thaumaturge.content.workbench.MenuArcaneWorkbench;
import com.leclowndu93150.thaumaturge.registry.TCMenus;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public final class ThaumaturgeAdditionsJEIPlugin implements IModPlugin {
    private static final Identifier PLUGIN_UID = ThaumaturgeAdditions.identifier("jei_plugin");

    public ThaumaturgeAdditionsJEIPlugin() {
    }

    @Override
    public @NonNull Identifier getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipeCatalysts(@NonNull IRecipeCatalystRegistration registration) {
        if (SophisticatedBackpacksCompat.isLoaded()) {
            registration.addCraftingStation(RecipeTypes.CRAFTING, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
            registration.addCraftingStation(ArcaneWorkbenchCategory.RECIPE_TYPE, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
        }
        if (TravelersBackpackCompat.isLoaded()) {
            registration.addCraftingStation(RecipeTypes.CRAFTING, TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());
            registration.addCraftingStation(ArcaneWorkbenchCategory.RECIPE_TYPE, TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());
        }
    }
}
