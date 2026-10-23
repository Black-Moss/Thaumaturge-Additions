package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.data.recipe.builders.CrucibleRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapedRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapelessRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    private RecipeOutput output;
    private HolderLookup.Provider registries;
    private HolderLookup<Item> items;
    private HolderLookup<IAspect> aspects;

    public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    private static ResearchGate taGate(String path) {
        return new ResearchGate(ThaumaturgeAdditions.identifier(path), Optional.empty(), false);
    }

    private static ResearchGate ttGate(String path) {
        return new ResearchGate(TTIds.rl(path), Optional.empty(), false);
    }

    @Override
    protected void buildRecipes(@NonNull RecipeOutput recipeOutput, HolderLookup.Provider provider) {
        this.output = recipeOutput;
        this.registries = provider;
        this.items = provider.lookupOrThrow(Registries.ITEM);
        this.aspects = this.registries.lookupOrThrow(IAspect.REGISTRY_KEY);

        voidTravellerBoots();

        // 虚空缸中之脑
        arcaneShapeless(TAItems.VOID_BRAIN_JAR.get(), 50)
                .aspect(TTAspects.PERDITIO)
                .requires(TTItems.JAR_BRAIN)
                .gate(ttGate("warded_jars"))
                .unlockedBy("has", has(TTItems.JAR_BRAIN))
                .save(output);

        // 永燃之瓮
        infusion(TAItems.EVERBURNING_URN.get(),
                TTItems.EVERFULL_URN.get(),
                RecipeCategory.BUILDING_BLOCKS)
                .component(Ingredient.of(Items.NETHER_BRICK))
                .component(Ingredient.of(Items.NETHER_BRICK))
                .component(Ingredient.of(Items.LAVA_BUCKET))
                .component(crystal(TTAspects.IGNIS))
                .component(Ingredient.of(Items.OBSIDIAN))
                .component(Ingredient.of(Items.LAVA_BUCKET))
                .aspect(TTAspects.IGNIS, 40)
                .aspect(TTAspects.TERRA, 20)
                .aspect(TTAspects.POTENTIA, 10)
                .aspect(TTAspects.FABRICO, 10)
                .instability(6)
                .gate(taGate("everburning_urn"))
                .unlockedBy("has", has(TAItems.EVERBURNING_URN.get()))
                .save(output);

        // 彩虹笔与墨
        infusion((TAItems.RAINBOW_SCRIBING_TOOLS.get()),
                TTItems.SCRIBING_TOOLS.get())
                .component(Ingredient.of(Tags.Items.DYES_BLACK))
                .component(Ingredient.of(Tags.Items.DYES_BLUE))
                .component(Ingredient.of(Tags.Items.DYES_BROWN))
                .component(Ingredient.of(Tags.Items.DYES_CYAN))
                .component(Ingredient.of(Tags.Items.DYES_GRAY))
                .component(Ingredient.of(Tags.Items.DYES_GREEN))
                .component(Ingredient.of(Tags.Items.DYES_LIGHT_BLUE))
                .component(Ingredient.of(Tags.Items.DYES_LIGHT_GRAY))
                .component(Ingredient.of(Tags.Items.DYES_LIME))
                .component(Ingredient.of(Tags.Items.DYES_MAGENTA))
                .component(Ingredient.of(Tags.Items.DYES_ORANGE))
                .component(Ingredient.of(Tags.Items.DYES_PINK))
                .component(Ingredient.of(Tags.Items.DYES_PURPLE))
                .component(Ingredient.of(Tags.Items.DYES_RED))
                .component(Ingredient.of(Tags.Items.DYES_WHITE))
                .component(Ingredient.of(Tags.Items.DYES_YELLOW))
                .aspect(TTAspects.FABRICO, 25)
                .aspect(TTAspects.ORDO, 25)
                .instability(2)
                .gate(taGate("rainbow_scribing_tools"))
                .unlockedBy("has", has(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .save(output);

        // 填充彩虹笔与墨
        new ShapelessRecipeBuilder(RecipeCategory.TOOLS, new ItemStack(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .requires(TAItems.RAINBOW_SCRIBING_TOOLS.get())
                .requires(Tags.Items.DYES)
                .unlockedBy("has", has(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .save(output, ThaumaturgeAdditions.identifier("rainbow_scribing_tools_alt"));
    }

    private void voidTravellerBoots() {
        infusion(TAItems.VOID_TRAVELLER_BOOTS.get(),
                TTItems.VOID_BOOTS.get(),
                RecipeCategory.COMBAT)
                .component(Ingredient.of(TTItems.TRAVELLER_BOOTS.get()))
                .component(Ingredient.of(TTItems.PLATE_VOID.get()))
                .component(Ingredient.of(TTItems.PLATE_VOID.get()))
                .component(Ingredient.of(TTItems.SALIS_MUNDUS.get()))
                .component(Ingredient.of(TTItems.FABRIC.get()))
                .component(Ingredient.of(Items.LEATHER))
                .aspect(TTAspects.METALLUM, 25)
                .aspect(TTAspects.AQUA, 25)
                .aspect(TTAspects.VACUOS, 30)
                .aspect(TTAspects.FABRICO, 25)
                .aspect(TTAspects.ALIENIS, 25)
                .aspect(TTAspects.VOLATUS, 50)
                .aspect(TTAspects.MOTUS, 50)
                .instability(6)
                .gate(taGate("void_traveller_boots"))
                .unlockedBy("has", has(TAItems.VOID_TRAVELLER_BOOTS.get()))
                .save(output);
    }

    private InfusionRecipeBuilder infusion(ItemLike result, ItemLike catalyst, RecipeCategory category) {
        return (InfusionRecipeBuilder) new InfusionRecipeBuilder(
                aspects,
                category,
                new ItemStack(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", has(result));
    }

    private InfusionRecipeBuilder infusion(ItemLike result, ItemLike catalyst) {
        return infusion(result, catalyst, RecipeCategory.MISC);
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, ItemLike catalyst, RecipeCategory category) {
        return (CrucibleRecipeBuilder) new CrucibleRecipeBuilder(
                aspects,
                category,
                new ItemStack(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", has(result));
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, ItemLike catalyst) {
        return crucible(result, catalyst, RecipeCategory.MISC);
    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory recipeCategory, ItemStack result, int vis) {
        return new ArcaneWorkbenchShapelessRecipeBuilder(recipeCategory,
                result,
                aspects,
                vis,
                registries.lookupOrThrow(Registries.ITEM));
    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(ItemLike result, int vis, RecipeCategory recipeCategory) {
        return arcaneShapeless(recipeCategory, new ItemStack(result.asItem()), vis);
    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(ItemLike result, int vis) {
        return arcaneShapeless(result, vis, RecipeCategory.MISC);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(ItemStack result, int vis, RecipeCategory recipeCategory) {
        return new ArcaneWorkbenchShapedRecipeBuilder(recipeCategory,
                result,
                this.items,
                aspects,
                vis);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(ItemLike result, int vis, RecipeCategory recipeCategory) {
        return (ArcaneWorkbenchShapedRecipeBuilder) arcaneShaped(new ItemStack(result.asItem()), vis, recipeCategory)
                .unlockedBy("has", has(result.asItem()));
    }

    private Ingredient crystal(ResourceKey<IAspect> aspect) {
        HolderLookup<IAspect> aspects = this.registries.lookupOrThrow(IAspect.REGISTRY_KEY);
        return DataComponentIngredient.of(false,
                TTDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(aspects.getOrThrow(aspect), 1),
                TTItems.ESSENTIA_CRYSTAL.get());
    }
}
