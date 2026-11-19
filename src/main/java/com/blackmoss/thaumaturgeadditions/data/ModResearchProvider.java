package com.blackmoss.thaumaturgeadditions.data;

import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import com.blackmoss.lazycat.thaumaturge.research.ResearchEntryBuilder;
import com.blackmoss.lazycat.thaumaturge.research.ResearchStageBuilder;
import com.blackmoss.lazycat.thaumaturge.research.ThaumaturgeResearchProvider;
import com.blackmoss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.ResearchIcon;
import com.leclowndu93150.thaumaturge.api.research.TTResearchCategories;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public final class ModResearchProvider extends ThaumaturgeResearchProvider {
    public ModResearchProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    @Override
    protected void addResearch(ResearchContext context) {
        arcaneWorkbenchUpgrade(context,
                "arcane_workbench_upgrade",
                true, true,
                true);

        arcaneWorkbenchUpgrade(context,
                "sophisticatedbackpacks_arcane_workbench_upgrade",
                true, false,
                false);

        arcaneWorkbenchUpgrade(context,
                "travelersbackpack_arcane_workbench_upgrade",
                false, true,
                false);

        everburningUrn(context);
        voidTravellerBoots(context);
        focusSalisMundus(context);
        hungryTrappedChest(context);
        rainbowScribingTools(context);
    }

    private void everburningUrn(ResearchContext context) {
        new ResearchEntryBuilder(context, TTResearchCategories.ALCHEMY, "everburning_urn")
                .parent(TTIds.rl("everfull_urn"))
                .position(-2, 4)
                .icon(TAItems.EVERBURNING_URN)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.ALCHEMY)
                .obtain(TTItems.EVERFULL_URN)
                .end()

                .stage()
                .recipes(ThaumaturgeAdditions.identifier("infusion/everburning_urn"))
                .end()

                .noteAspect(TTAspects.IGNIS, 5)
                .noteAspect(TTAspects.TERRA, 3)
                .noteAspect(TTAspects.VACUOS, 2)
                .complexity(2)
                .save();
    }

    private void voidTravellerBoots(ResearchContext context) {
        new ResearchEntryBuilder(context, TTResearchCategories.ELDRITCH, "void_traveller_boots")
                .parent(TTIds.rl("void_robe_armor"))
                .parent(TTIds.rl("boots_traveller"))
                .position(-1, 4)
                .icon(TAItems.VOID_TRAVELLER_BOOTS)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.INFUSION)
                .requiredKnowledge(KnowledgeType.OBSERVATION, TTResearchCategories.ELDRITCH)
                .warp(1)
                .end()

                .stage()
                .recipes(
                        ThaumaturgeAdditions.identifier("infusion/void_traveller_boots"),
                        ThaumaturgeAdditions.identifier("void_traveller_boots_dyed"))
                .end()

                .noteAspect(TTAspects.MOTUS, 3)
                .noteAspect(TTAspects.VOLATUS, 3)
                .noteAspect(TTAspects.AQUA, 3)
                .noteAspect(TTAspects.VACUOS, 6)
                .noteAspect(TTAspects.FABRICO, 4)
                .complexity(3)
                .save();
    }

    private void focusSalisMundus(ResearchContext context) {
        new ResearchEntryBuilder(context, TTResearchCategories.AUROMANCY, "focus_salis_mundus")
                .parent(TTIds.rl("focus_elemental"))
                .position(-1, 3)
                .icon(ResearchIcon.ofFocus(ThaumaturgeAdditions.identifier("salis_mundus")))

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.AUROMANCY)
                .obtain(TTItems.SALIS_MUNDUS)
                .end()

                .stage()
                .end()
                .noteAspect(TTAspects.IGNIS, 4)
                .noteAspect(TTAspects.GELUM, 4)
                .noteAspect(TTAspects.TERRA, 4)
                .complexity(2)
                .save();
    }

    private void hungryTrappedChest(ResearchContext context) {
        new ResearchEntryBuilder(context, TTResearchCategories.ARTIFICE, "hungry_trapped_chest")
                .parent(TTIds.rl("hungry_chest"))
                .position(3, 3)
                .icon(TAItems.HUNGRY_TRAPPED_CHEST)

                .stage()
                .obtain(Tags.Items.CHESTS_TRAPPED)
                .obtain(Items.TRIPWIRE_HOOK)
                .requiredKnowledge(KnowledgeType.OBSERVATION, TTResearchCategories.GOLEMANCY)
                .end()

                .stage()
                .recipes(ThaumaturgeAdditions.identifier("arcane_workbench/hungry_trapped_chest"))
                .end()

                .noteAspect(TTAspects.DESIDERIUM, 3)
                .noteAspect(TTAspects.VACUOS, 3)
                .complexity(1)
                .save();
    }

    private void rainbowScribingTools(ResearchContext context) {
        new ResearchEntryBuilder(context, TTResearchCategories.BASICS, "rainbow_scribing_tools")
                .parent(TTIds.rl("research"))
                .parent(TTIds.rl("base_infusion"))
                .position(4, 0)
                .icon(TAItems.RAINBOW_SCRIBING_TOOLS)
                .meta(ResearchEntryMeta.ROUND)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.INFUSION)
                .obtain(TTItems.SCRIBING_TOOLS)
                .obtain(Tags.Items.DYES_BLACK)
                .obtain(Tags.Items.DYES_RED)
                .obtain(Tags.Items.DYES_GREEN)
                .obtain(Tags.Items.DYES_BROWN)
                .obtain(Tags.Items.DYES_BLUE)
                .obtain(Tags.Items.DYES_PURPLE)
                .obtain(Tags.Items.DYES_CYAN)
                .obtain(Tags.Items.DYES_LIGHT_GRAY)
                .obtain(Tags.Items.DYES_GRAY)
                .obtain(Tags.Items.DYES_PINK)
                .obtain(Tags.Items.DYES_LIME)
                .obtain(Tags.Items.DYES_YELLOW)
                .obtain(Tags.Items.DYES_LIGHT_BLUE)
                .obtain(Tags.Items.DYES_MAGENTA)
                .obtain(Tags.Items.DYES_ORANGE)
                .obtain(Tags.Items.DYES_WHITE)
                .end()

                .stage()
                .recipes(
                        ThaumaturgeAdditions.identifier("infusion/rainbow_scribing_tools"),
                        ThaumaturgeAdditions.identifier("rainbow_scribing_tools_alt"))
                .end()

                .noteAspect(TTAspects.ORDO, 3)
                .noteAspect(TTAspects.FABRICO, 4)
                .complexity(3)
                .save();
    }

    private void arcaneWorkbenchUpgrade(
            ResearchContext context,
            String id,
            boolean sophisticated,
            boolean travelers,
            boolean both) {
        ResearchEntryBuilder builder = new ResearchEntryBuilder(context,
                TTResearchCategories.BASICS, "arcane_workbench_upgrade")
                .parent(TTIds.rl("first_steps"))
                .position(-1, 1);
        if (both) {
            builder.ifModLoaded("sophisticatedbackpacks", "travelersbackpack");
        } else if (sophisticated) {
            builder.ifModLoaded("sophisticatedbackpacks").unlessModLoaded("travelersbackpack");
        } else {
            builder.ifModLoaded("travelersbackpack").unlessModLoaded("sophisticatedbackpacks");
        }
        if (sophisticated) {
            builder.icon(ResearchIcon.ofItem(
                    ThaumaturgeAdditions.identifier("sophisticatedbackpacks_arcane_workbench_upgrade")));
        }
        if (travelers) {
            builder.icon(ResearchIcon.ofItem(
                    ThaumaturgeAdditions.identifier("travelersbackpack_arcane_workbench_upgrade")));
        }

        ResearchStageBuilder stage = builder.stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.BASICS);
        if (both) {
            stage.requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.BASICS);
        }
        if (sophisticated) {
            stage.obtain(ResourceLocation.parse("sophisticatedbackpacks:crafting_upgrade"));
        }
        if (travelers) {
            stage.obtain(ResourceLocation.parse("travelersbackpack:crafting_upgrade"));
        }
        stage.end();

        builder.stage()
                .recipes(upgradeRecipe(id))
                .end()
                .addendum()
                .requiredResearch(TTIds.rl("workbench_charger"))
                .recipes(chargerRecipe(sophisticated, travelers))
                .end();

        builder.noteAspect(TTAspects.INSTRUMENTUM)
                .noteAspect(TTAspects.HUMANUS)
                .noteAspect(TTAspects.FABRICO)
                .noteAspect(TTAspects.MOTUS)
                .complexity(2)
                .save(id);
    }

    private Object[] upgradeRecipe(String id) {
        if ("arcane_workbench_upgrade".equals(id)) {
            return new Object[]{
                    ThaumaturgeAdditions.identifier("arcane_workbench/sophisticatedbackpacks_arcane_workbench_upgrade_and"),
                    ThaumaturgeAdditions.identifier("arcane_workbench/travelersbackpack_arcane_workbench_upgrade_and")
            };
        }
        return new Object[]{
                ThaumaturgeAdditions.identifier("arcane_workbench/" + id)
        };
    }

    private Object[] chargerRecipe(boolean sophisticated, boolean travelers) {
        if (sophisticated && travelers) {
            return new Object[]{
                    ThaumaturgeAdditions.identifier("arcane_workbench/sophisticatedbackpacks_arcane_charger_upgrade_and"),
                    ThaumaturgeAdditions.identifier("arcane_workbench/travelersbackpack_arcane_charger_upgrade_and")
            };
        }
        String mod = sophisticated ? "sophisticatedbackpacks" : "travelersbackpack";
        return new Object[]{
                ThaumaturgeAdditions.identifier("arcane_workbench/" + mod + "_arcane_charger_upgrade")
        };
    }
}
