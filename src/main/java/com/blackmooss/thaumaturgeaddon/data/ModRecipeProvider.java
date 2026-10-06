package com.blackmooss.thaumaturgeaddon.data;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeaddon.registry.TAItems;
import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TCAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.data.recipe.builders.CrucibleRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapelessRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import com.leclowndu93150.thaumaturge.registry.TCItems;
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

    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.lookupProvider = provider;
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<IAspect> aspects = registries.lookupOrThrow(IAspect.REGISTRY_KEY);

        voidTravellerBoots(aspects);
        clusters(aspects);

        // 虚空缸中之脑
        new ArcaneWorkbenchShapelessRecipeBuilder(RecipeCategory.MISC,
                new ItemStackTemplate(TAItems.VOID_BRAIN_JAR.get()),
                aspects,
                50,
                registries.lookupOrThrow(Registries.ITEM))
                .aspect(TCAspects.PERDITIO)
                .requires(TCItems.JAR_BRAIN)
                .gate(tcGate("warded_jars"))
                .unlockedBy("has", this.has(TCItems.JAR_BRAIN))
                .save(output);


        // 永燃之瓮
        infusion(TAItems.EVERBURNING_URN.get(),
                RecipeCategory.BUILDING_BLOCKS,
                TCItems.EVERFULL_URN.get())
                .component(Ingredient.of(Items.NETHER_BRICK))
                .component(Ingredient.of(Items.NETHER_BRICK))
                .component(Ingredient.of(Items.LAVA_BUCKET))
                .component(crystal(aspects, TCAspects.IGNIS))
                .component(Ingredient.of(Items.OBSIDIAN))
                .component(Ingredient.of(Items.LAVA_BUCKET))
                .aspect(TCAspects.IGNIS, 40)
                .aspect(TCAspects.TERRA, 20)
                .aspect(TCAspects.POTENTIA, 10)
                .aspect(TCAspects.FABRICO, 10)
                .instability(6)
                .gate(taGate("everburning_urn"))
                .unlockedBy("has", this.has(TAItems.EVERBURNING_URN.get()))
                .save(output);

        // 彩虹笔与墨
        infusion(TAItems.RAINBOW_SCRIBING_TOOLS.get(), RecipeCategory.TOOLS,
                TCItems.SCRIBING_TOOLS.get())
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
                .aspect(TCAspects.FABRICO, 25)
                .aspect(TCAspects.ORDO, 25)
                .instability(2)
                .gate(taGate("rainbow_scribing_tools"))
                .unlockedBy("has", this.has(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .save(output);

        // 填充彩虹笔与墨
        shapeless(RecipeCategory.TOOLS, TAItems.RAINBOW_SCRIBING_TOOLS.get())
                .requires(TAItems.RAINBOW_SCRIBING_TOOLS.get())
                .requires(Tags.Items.DYES)
                .unlockedBy("has", this.has(TAItems.RAINBOW_SCRIBING_TOOLS.get()))
                .save(output, ThaumaturgeAdditions.MODID + ":rainbow_scribing_tools_alt");
    }

    private void voidTravellerBoots(HolderGetter<IAspect> aspects) {
        infusion(TAItems.VOID_TRAVELLER_BOOTS.get(),
                RecipeCategory.COMBAT,
                TCItems.VOID_BOOTS.get())
                .component(Ingredient.of(TCItems.TRAVELLER_BOOTS.get()))
                .component(Ingredient.of(TCItems.PLATE_VOID.get()))
                .component(Ingredient.of(TCItems.PLATE_VOID.get()))
                .component(Ingredient.of(TCItems.SALIS_MUNDUS.get()))
                .component(Ingredient.of(TCItems.FABRIC.get()))
                .component(Ingredient.of(Items.LEATHER))
                .aspect(TCAspects.METALLUM, 25)
                .aspect(TCAspects.AQUA, 25)
                .aspect(TCAspects.VACUOS, 30)
                .aspect(TCAspects.FABRICO, 25)
                .aspect(TCAspects.ALIENIS, 25)
                .aspect(TCAspects.VOLATUS, 50)
                .aspect(TCAspects.MOTUS, 50)
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

    private void clusters(HolderGetter<IAspect> aspects) {
        crucible(TCItems.CLUSTER_IRON.get(),
                RecipeCategory.MISC,
                Items.RAW_IRON)
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_IRON))
                .save(output);

        // 粗金变金原矿簇
        crucible(TCItems.CLUSTER_GOLD.get(),
                RecipeCategory.MISC,
                Items.RAW_GOLD)
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_GOLD))
                .save(output);

        // 粗铜变铜原矿簇
        crucible(TCItems.CLUSTER_COPPER.get(),
                RecipeCategory.MISC,
                Items.RAW_COPPER)
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_COPPER))
                .save(output);

        // 粗锡变锡原矿簇
        crucible(TCItems.CLUSTER_TIN.get(),
                RecipeCategory.MISC,
                commonTag("raw_materials/tin"))
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_TIN))
                .save(output);

        // 粗银变银原矿簇
        crucible(TCItems.CLUSTER_SILVER.get(),
                RecipeCategory.MISC,
                commonTag("raw_materials/silver"))
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_SILVER))
                .save(output);

        // 粗铅变铅原矿簇
        crucible(TCItems.CLUSTER_LEAD.get(),
                RecipeCategory.MISC,
                commonTag("raw_materials/lead"))
                .aspect(TCAspects.METALLUM, 5)
                .aspect(TCAspects.ORDO, 5)
                .gate(tcGate("metal_purification"))
                .unlockedBy("has", this.has(TCItems.CLUSTER_LEAD))
                .save(output);
    }

    private HolderSet<Item> tag(String space, String path) {
        return this.lookupProvider.getOrThrow(TagKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(
                        space, path)));
    }

    private Ingredient crystal(HolderGetter<IAspect> aspects, ResourceKey<IAspect> aspect) {
        return DataComponentIngredient.of(
                TCDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(aspects.getOrThrow(aspect), 1),
                TCItems.ESSENTIA_CRYSTAL.get());
    }

    private Ingredient commonTag(String path) {
        return Ingredient.of(tag("c", path));
    }

    private static ResearchGate tcGate(String path) {
        return new ResearchGate(TCIds.rl(path), Optional.empty(), false);
    }

    private static ResearchGate taGate(String path) {
        return new ResearchGate(ThaumaturgeAdditions.identifier(path), Optional.empty(), false);
    }

    private InfusionRecipeBuilder infusion(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return new InfusionRecipeBuilder(this.registries.lookupOrThrow(IAspect.REGISTRY_KEY), category, new ItemStackTemplate(result.asItem()), Ingredient.of(catalyst));
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, RecipeCategory category, Ingredient catalyst) {
        return new CrucibleRecipeBuilder(this.registries.lookupOrThrow(IAspect.REGISTRY_KEY), category, new ItemStackTemplate(result.asItem()), catalyst);
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return crucible(result, category, Ingredient.of(catalyst));
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