package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.recipe.VoidTravellerBootsDyeRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.SimpleRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TARecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ThaumaturgeAdditions.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<VoidTravellerBootsDyeRecipe>> VOID_TRAVELLER_BOOTS_DYE =
            RECIPE_SERIALIZERS.register("void_traveller_boots_dye",
                    () -> new SimpleRecipeSerializer<>(VoidTravellerBootsDyeRecipe.MAP_CODEC, VoidTravellerBootsDyeRecipe.STREAM_CODEC));

    private TARecipes() {
    }
}
