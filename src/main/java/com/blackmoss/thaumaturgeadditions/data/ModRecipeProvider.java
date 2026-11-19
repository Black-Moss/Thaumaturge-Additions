package com.blackmoss.thaumaturgeadditions.data;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.ThaumaturgeUtils;
import com.blackmoss.lazycat.recipe.LazyCatRecipeProvider;
import com.blackmoss.lazycat.recipe.minecraft.ShapelessBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.ArcaneShapedBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.ArcaneShapelessBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.InfusionBuilder;
import com.blackmoss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends LazyCatRecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, ThaumaturgeAdditions.MODID);
    }

    private static ResearchGate ttGate(String path) {
        return new ResearchGate(TTIds.rl(path), Optional.empty(), false);
    }

    private static ResearchGate taGate(String path) {
        return new ResearchGate(ThaumaturgeAdditions.identifier(path), Optional.empty(), false);
    }

    @Override
    protected void addRecipes() {
        voidTravellerBoots();
        rainbowScribingTools(context);
        backpackUpgrades(context);

        // 虚空缸中之脑
        new ArcaneShapelessBuilder(context, TAItems.VOID_BRAIN_JAR, 50)
                .aspect(TTAspects.PERDITIO)
                .requires(TTItems.JAR_BRAIN)
                .gate(ttGate("warded_jars"))
                .save();
        new InfusionBuilder(context,
                TAItems.VOID_BRAIN_JAR,
                TTItems.JAR_VOID)
                .component(TTItems.BRAIN)
                .component(Items.SPIDER_EYE)
                .component(Tags.Items.BUCKETS_WATER)
                .component(Items.SPIDER_EYE)
                .aspect(TTAspects.COGNITIO, 25)
                .aspect(TTAspects.SENSUS, 25)
                .aspect(TTAspects.EXANIMIS, 25)
                .gate(ttGate("warded_jars"))
                .save();

        // 永燃之瓮
        new InfusionBuilder(context,
                TAItems.EVERBURNING_URN,
                TTItems.EVERFULL_URN)
                .component(Items.NETHER_BRICK)
                .component(Items.NETHER_BRICK)
                .component(Items.LAVA_BUCKET)
                .component(crystal(TTAspects.IGNIS))
                .component(Items.OBSIDIAN)
                .component(Items.LAVA_BUCKET)
                .aspect(TTAspects.IGNIS, 40)
                .aspect(TTAspects.TERRA, 20)
                .aspect(TTAspects.POTENTIA, 10)
                .aspect(TTAspects.FABRICO, 10)
                .instability(6)
                .gate(taGate("everburning_urn"))
                .save();

        // 饕餮陷阱箱
        new ArcaneShapelessBuilder(context,
                TAItems.HUNGRY_TRAPPED_CHEST,
                10)
                .requires(TTItems.HUNGRY_CHEST)
                .requires(Items.TRIPWIRE_HOOK)
                .gate(taGate("hungry_trapped_chest"))
                .save("hungry_trapped_chest_shapless");
        new ArcaneShapedBuilder(context,
                TAItems.HUNGRY_TRAPPED_CHEST,
                25)
                .pattern("PTP")
                .pattern("PHP")
                .pattern("PPP")
                .define('P', TTItemTags.PLANKS_GREATWOOD)
                .define('T', ItemTags.WOODEN_TRAPDOORS)
                .define('H', Items.TRIPWIRE_HOOK)
                .gate(taGate("hungry_trapped_chest"))
                .save("hungry_trapped_chest_shapd");
    }

    // 彩虹笔与墨
    private void rainbowScribingTools(RecipeContext context) {
        new InfusionBuilder(context,
                TAItems.RAINBOW_SCRIBING_TOOLS,
                TTItems.SCRIBING_TOOLS)
                .component(Tags.Items.DYES_BLACK)
                .component(Tags.Items.DYES_BLUE)
                .component(Tags.Items.DYES_BROWN)
                .component(Tags.Items.DYES_CYAN)
                .component(Tags.Items.DYES_GRAY)
                .component(Tags.Items.DYES_GREEN)
                .component(Tags.Items.DYES_LIGHT_BLUE)
                .component(Tags.Items.DYES_LIGHT_GRAY)
                .component(Tags.Items.DYES_LIME)
                .component(Tags.Items.DYES_MAGENTA)
                .component(Tags.Items.DYES_ORANGE)
                .component(Tags.Items.DYES_PINK)
                .component(Tags.Items.DYES_PURPLE)
                .component(Tags.Items.DYES_RED)
                .component(Tags.Items.DYES_WHITE)
                .component(Tags.Items.DYES_YELLOW)
                .aspect(TTAspects.FABRICO, 25)
                .aspect(TTAspects.ORDO, 25)
                .instability(2)
                .gate(taGate("rainbow_scribing_tools"))
                .save();
        // 填充
        new ShapelessBuilder(context,
                TAItems.RAINBOW_SCRIBING_TOOLS)
                .requires(TAItems.RAINBOW_SCRIBING_TOOLS)
                .requires(Tags.Items.DYES)
                .category(RecipeCategory.TOOLS)
                .save(ThaumaturgeAdditions.identifier("rainbow_scribing_tools_alt"));
    }

    // 虚空旅行者之靴
    private void voidTravellerBoots() {
        new InfusionBuilder(context,
                TAItems.VOID_TRAVELLER_BOOTS,
                TTItems.VOID_BOOTS)
                .component(TTItems.TRAVELLER_BOOTS)
                .component(TTItems.PLATE_VOID)
                .component(TTItems.PLATE_VOID)
                .component(TTItems.SALIS_MUNDUS)
                .component(TTItems.FABRIC)
                .component(Items.LEATHER)
                .aspect(TTAspects.METALLUM, 25)
                .aspect(TTAspects.AQUA, 25)
                .aspect(TTAspects.VACUOS, 30)
                .aspect(TTAspects.FABRICO, 25)
                .aspect(TTAspects.ALIENIS, 25)
                .aspect(TTAspects.VOLATUS, 50)
                .aspect(TTAspects.MOTUS, 50)
                .instability(6)
                .gate(taGate("void_traveller_boots"))
                .save();

        customCrafting(TAItems.VOID_TRAVELLER_BOOTS,
                (commonInfo, bookInfo) ->
                        new DyeRecipe(
                                commonInfo,
                                bookInfo,
                                Ingredient.of(TAItems.VOID_TRAVELLER_BOOTS.get()),
                                this.tag(ItemTags.DYES),
                                new ItemStackTemplate(TAItems.VOID_TRAVELLER_BOOTS.get())))
                .group("cloth_robes")
                .save(ThaumaturgeAdditions.identifier("void_traveller_boots_dyed"));
    }

    // 背包升级
    private void backpackUpgrades(RecipeContext context) {
        backpackUpgrade(context, "sophisticatedbackpacks_arcane_workbench_upgrade",
                TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE,
                "sophisticatedbackpacks", "sophisticatedbackpacks:crafting_upgrade",
                "sophisticatedbackpacks_arcane_workbench_upgrade", false, false);
        backpackUpgrade(context, "sophisticatedbackpacks_arcane_workbench_upgrade_and",
                TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE,
                "sophisticatedbackpacks", "sophisticatedbackpacks:crafting_upgrade",
                "arcane_workbench_upgrade", true, false);

        backpackUpgrade(context, "travelersbackpack_arcane_workbench_upgrade",
                TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE,
                "travelersbackpack", "travelersbackpack:crafting_upgrade",
                "travelersbackpack_arcane_workbench_upgrade", false, false);
        backpackUpgrade(context, "travelersbackpack_arcane_workbench_upgrade_and",
                TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE,
                "travelersbackpack", "travelersbackpack:crafting_upgrade",
                "arcane_workbench_upgrade", true, false);

        backpackUpgrade(context, "sophisticatedbackpacks_arcane_charger_upgrade",
                TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE,
                "sophisticatedbackpacks", "sophisticatedbackpacks:upgrade_base",
                "sophisticatedbackpacks_arcane_workbench_upgrade", false, true);
        backpackUpgrade(context, "sophisticatedbackpacks_arcane_charger_upgrade_and",
                TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE,
                "sophisticatedbackpacks", "sophisticatedbackpacks:upgrade_base",
                "arcane_workbench_upgrade", true, true);

        backpackUpgrade(context, "travelersbackpack_arcane_charger_upgrade",
                TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE,
                "travelersbackpack", "travelersbackpack:blank_upgrade",
                "travelersbackpack_arcane_workbench_upgrade", false, true);
        backpackUpgrade(context, "travelersbackpack_arcane_charger_upgrade_and",
                TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE,
                "travelersbackpack", "travelersbackpack:blank_upgrade",
                "arcane_workbench_upgrade", true, true);
    }

    private void backpackUpgrade(
            RecipeContext context,
            String path,
            Object result,
            String modId,
            String baseItem,
            String research,
            boolean both,
            boolean charger) {
        ArcaneShapedBuilder builder = new ArcaneShapedBuilder(context, result, charger ? 200 : 100);
        if (charger) {
            builder.aspect(TTAspects.AER, 5)
                    .aspect(TTAspects.TERRA, 5)
                    .aspect(TTAspects.IGNIS, 5)
                    .aspect(TTAspects.AQUA, 5)
                    .aspect(TTAspects.ORDO, 5)
                    .aspect(TTAspects.PERDITIO, 5)
                    .pattern(" C ")
                    .pattern("RBR")
                    .pattern(" T ")
                    .define('B', baseItem)
                    .define('C', TTItems.ARCANE_WORKBENCH_CHARGER)
                    .define('R', TTItems.VIS_RESONATOR)
                    .define('T', TTItemTags.INGOTS_THAUMIUM);
        } else {
            builder.aspect(TTAspects.AER)
                    .aspect(TTAspects.TERRA)
                    .aspect(TTAspects.IGNIS)
                    .aspect(TTAspects.AQUA)
                    .aspect(TTAspects.ORDO)
                    .aspect(TTAspects.PERDITIO)
                    .pattern(" S ")
                    .pattern("SCS")
                    .pattern(" S ")
                    .define('S', TTItems.SALIS_MUNDUS)
                    .define('C', baseItem);
        }
        String other = "sophisticatedbackpacks".equals(modId) ? "travelersbackpack" : "sophisticatedbackpacks";
        if (both) {
            builder.ifModLoaded(modId, other);
        } else {
            builder.ifModLoaded(modId).unlessModLoaded(other);
        }
        builder.gate(taGate(research)).save(path);
    }

    private Ingredient crystal(ResourceKey<IAspect> aspect) {
        return ThaumaturgeUtils.crystal(registries, aspect);
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