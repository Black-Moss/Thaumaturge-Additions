package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import com.blackmoss.lazycat.thaumaturge.research.ResearchEntryBuilder;
import com.blackmoss.lazycat.thaumaturge.research.ThaumaturgeResearchProvider;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.TTResearchCategories;
import net.minecraft.data.PackOutput;

public final class ModResearchProvider extends ThaumaturgeResearchProvider {
    public ModResearchProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    @Override
    protected void addResearch(ResearchContext context) {
        new ResearchEntryBuilder(context,
                TTResearchCategories.BASICS,
                "arcane_workbench_upgrade")
                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.BASICS, 1)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.BASICS, 1);
    }
}
