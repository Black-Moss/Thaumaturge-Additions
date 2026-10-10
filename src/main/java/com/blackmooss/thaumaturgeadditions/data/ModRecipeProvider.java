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
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider lookupProvider;
    private final HolderGetter<IAspect> aspects;

    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.lookupProvider = provider;
        this.aspects = registries.lookupOrThrow(IAspect.REGISTRY_KEY);
    }

    private static ResearchGate tcGate(String path) {
        return new ResearchGate(TTIds.rl(path), Optional.empty(), false);
    }

    private static ResearchGate taGate(String path) {
        return new ResearchGate(ThaumaturgeAdditions.identifier(path), Optional.empty(), false);
    }

    @Override
    protected void buildRecipes() {
        voidTravellerBoots();
//        clusters();

        // 虚空缸中之脑
        arcaneShapeless(RecipeCategory.MISC,
                TAItems.VOID_BRAIN_JAR.get(),
                50)
                .aspect(TTAspects.PERDITIO)
                .requires(TTItems.JAR_BRAIN)
                .gate(tcGate("warded_jars"))
                .unlockedBy("has", this.has(TTItems.JAR_BRAIN))
                .save(output);

        // 永燃之瓮
        infusion(TAItems.EVERBURNING_URN.get(),
                RecipeCategory.BUILDING_BLOCKS,
                TTItems.EVERFULL_URN.get())
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
                .unlockedBy("has", this.has(TAItems.EVERBURNING_URN.get()))
                .save(output);

        // 彩虹笔与墨
        infusion(TAItems.RAINBOW_SCRIBING_TOOLS.get(), RecipeCategory.TOOLS,
                TTItems.SCRIBING_TOOLS.get())
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_BLACK)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_BLUE)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_BROWN)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_CYAN)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_GRAY)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_GREEN)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_LIGHT_BLUE)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_LIGHT_GRAY)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_LIME)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_MAGENTA)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_ORANGE)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_PINK)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_PURPLE)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_RED)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_WHITE)))
                .component(Ingredient.of(lookupProvider.getOrThrow(Tags.Items.DYES_YELLOW)))
                .aspect(TTAspects.FABRICO, 25)
                .aspect(TTAspects.ORDO, 25)
                .instability(2)
                .gate(taGate("rainbow_scribing_tools"))
                .save(output);

        // 填充彩虹笔与墨
        shapeless(RecipeCategory.TOOLS, TAItems.RAINBOW_SCRIBING_TOOLS.get())
                .requires(TAItems.RAINBOW_SCRIBING_TOOLS.get())
                .requires(Tags.Items.DYES)
                .unlockedBy("has", this.has(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .save(output, ThaumaturgeAdditions.MODID + ":rainbow_scribing_tools_alt");

        // 精妙背包奥术工作台升级
        arcaneShaped(RecipeCategory.MISC, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get(), 100)
                .pattern(" S ")
                .pattern("SCS")
                .pattern(" S ")
                .define('S', TTItems.SALIS_MUNDUS)
                .define('C', net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.CRAFTING_UPGRADE.get())
                .aspect(TTAspects.AER)
                .aspect(TTAspects.TERRA)
                .aspect(TTAspects.IGNIS)
                .aspect(TTAspects.AQUA)
                .aspect(TTAspects.ORDO)
                .aspect(TTAspects.PERDITIO)
                .gate(taGate("sophisticatedbackpacks_arcane_workbench_upgrade"))
                .save(output);

        // 旅行者背包奥术工作台升级
        arcaneShaped(RecipeCategory.MISC, TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get(), 100)
                .pattern(" S ")
                .pattern("SCS")
                .pattern(" S ")
                .define('S', TTItems.SALIS_MUNDUS)
                .define('C', com.tiviacz.travelersbackpack.init.ModItems.CRAFTING_UPGRADE.get())
                .aspect(TTAspects.AER)
                .aspect(TTAspects.TERRA)
                .aspect(TTAspects.IGNIS)
                .aspect(TTAspects.AQUA)
                .aspect(TTAspects.ORDO)
                .aspect(TTAspects.PERDITIO)
                .gate(taGate("travelersbackpack_arcane_workbench_upgrade"))
                .save(output);

        // 精妙背包奥术充能板升级
        arcaneShaped(RecipeCategory.MISC, TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE.get(), 200)
                .pattern(" C ")
                .pattern("RBR")
                .pattern(" T ")
                .define('C', TTItems.ARCANE_WORKBENCH_CHARGER.get())
                .define('R', TTItems.VIS_RESONATOR.get())
                .define('B', net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.UPGRADE_BASE.get())
                .define('T', TTItems.INGOT_THAUMIUM.get())
                .aspect(TTAspects.AER, 5)
                .aspect(TTAspects.TERRA, 5)
                .aspect(TTAspects.IGNIS, 5)
                .aspect(TTAspects.AQUA, 5)
                .aspect(TTAspects.ORDO, 5)
                .aspect(TTAspects.PERDITIO, 5)
                .gate(taGate("sophisticatedbackpacks_arcane_workbench_upgrade"))
                .save(output);

        // 旅行者背包奥术充能板升级
        arcaneShaped(RecipeCategory.MISC, TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE.get(), 200)
                .pattern(" C ")
                .pattern("RBR")
                .pattern(" T ")
                .define('C', TTItems.ARCANE_WORKBENCH_CHARGER.get())
                .define('R', TTItems.VIS_RESONATOR.get())
                .define('B', com.tiviacz.travelersbackpack.init.ModItems.BLANK_UPGRADE.get())
                .define('T', TTItems.INGOT_THAUMIUM.get())
                .aspect(TTAspects.AER, 5)
                .aspect(TTAspects.TERRA, 5)
                .aspect(TTAspects.IGNIS, 5)
                .aspect(TTAspects.AQUA, 5)
                .aspect(TTAspects.ORDO, 5)
                .aspect(TTAspects.PERDITIO, 5)
                .gate(taGate("travelersbackpack_arcane_workbench_upgrade"))
                .save(output);
    }

    private void voidTravellerBoots() {
        infusion(TAItems.VOID_TRAVELLER_BOOTS.get(),
                RecipeCategory.COMBAT,
                TTItems.VOID_BOOTS.get())
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
                .unlockedBy("has", this.has(TAItems.VOID_TRAVELLER_BOOTS.get()))
                .save(output);

        CustomCraftingRecipeBuilder.customCrafting(
                        RecipeCategory.MISC,
                        (commonInfo, bookInfo) ->
                                new DyeRecipe(
                                        commonInfo,
                                        bookInfo,
                                        Ingredient.of(TAItems.VOID_TRAVELLER_BOOTS.get()),
                                        this.tag(ItemTags.DYES),
                                        new ItemStackTemplate(TAItems.VOID_TRAVELLER_BOOTS.get())))
                .unlockedBy(getHasName(TAItems.VOID_TRAVELLER_BOOTS.get()), this.has(TAItems.VOID_TRAVELLER_BOOTS.get()))
                .group("cloth_robes")
                .save(output, ThaumaturgeAdditions.MODID + ":void_traveller_boots_dyed");
    }

//    private void clusters() {
//        cluster(TTItems.CLUSTER_IRON.get(), "iron");
//        cluster(TTItems.CLUSTER_GOLD.get(), "gold");
//        cluster(TTItems.CLUSTER_COPPER.get(), "copper");
//        cluster(TTItems.CLUSTER_TIN.get(), "tin");
//        cluster(TTItems.CLUSTER_LEAD.get(), "silver");
//        cluster(TTItems.CLUSTER_LEAD.get(), "lead");
//    }

    private HolderSet<Item> tag(String space, String path) {
        return this.lookupProvider.getOrThrow(TagKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(
                        space, path)));
    }

    private Ingredient crystal(ResourceKey<IAspect> aspect) {
        return DataComponentIngredient.of(
                TTDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(aspects.getOrThrow(aspect), 1),
                TTItems.ESSENTIA_CRYSTAL.get());
    }

    private Ingredient commonTag(String path) {
        return Ingredient.of(tag("c", path));
    }

    private InfusionRecipeBuilder infusion(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return (InfusionRecipeBuilder) new InfusionRecipeBuilder(
                this.registries.lookupOrThrow(IAspect.REGISTRY_KEY),
                category,
                new ItemStackTemplate(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", this.has(result));
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return (CrucibleRecipeBuilder) new CrucibleRecipeBuilder(
                aspects,
                category,
                new ItemStackTemplate(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", this.has(result));
    }

//    private void cluster(ItemLike result, String tag) {
//        new CrucibleRecipeBuilder(
//                aspects,
//                RecipeCategory.MISC,
//                new ItemStackTemplate(result.asItem()),
//                commonTag("raw_materials/" + tag))
//                .aspect(TTAspects.METALLUM, 5)
//                .aspect(TTAspects.ORDO, 5)
//                .gate(tcGate("metal_purification"))
//                .unlockedBy("has", this.has(result.asItem()))
//                .save(output, "%s:crucible/%s".formatted(ThaumaturgeAdditions.MODID, tag));
//    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory recipeCategory, ItemStackTemplate result, int vis) {
        return new ArcaneWorkbenchShapelessRecipeBuilder(recipeCategory,
                result,
                aspects,
                vis,
                registries.lookupOrThrow(Registries.ITEM));
    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory recipeCategory, ItemLike result, int vis) {
        return arcaneShapeless(recipeCategory, new ItemStackTemplate(result.asItem()), vis);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(RecipeCategory recipeCategory, ItemStackTemplate result, int vis) {
        return new ArcaneWorkbenchShapedRecipeBuilder(recipeCategory,
                result,
                this.items,
                aspects,
                vis);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(RecipeCategory recipeCategory, ItemLike result, int vis) {
        return (ArcaneWorkbenchShapedRecipeBuilder) arcaneShaped(recipeCategory, new ItemStackTemplate(result.asItem()), vis)
                .unlockedBy("has", this.has(result.asItem()));
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return "Thaumaturge Additions Recipes";
        }
    }
}