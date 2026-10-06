package com.blackmooss.thaumaturgeaddon.data.lang;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeaddon.registry.TABlocks;
import com.blackmooss.thaumaturgeaddon.registry.TAItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnUsProvider extends LanguageProvider {

    public EnUsProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgeadditions", "Thaumaturge Additions");

        addItem(TAItems.VOID_TRAVELLER_BOOTS, "Void Traveller's Boots");
        addItem(TAItems.RAINBOW_SCRIBING_TOOLS, "Rainbow Scribing Tools");
        addBlock(TABlocks.VOID_BRAIN_JAR, "Void Brain in a Jar");
        addBlock(TABlocks.EVERBURNING_URN, "Everburning Urn");

        addResearch("void_traveller_boots", "Void Traveller's Boots",
                "I've worn these boots for a while now, and I'm really tired of having to re-craft them every time they break. Since the Void Metal can repair itself, why not combine the two?",
                "These boots are powered by the Void, and will make your travels even more efficient." +
                        "<BR>They grant the same boons as a normal pair of Traveller's Boots, but twice as potent! They also repair themselves and hold far more Vis - yet they leave the wearer even more vulnerable to Warp..." +
                        "<BR>By the way, these boots can be dyed!");

        addResearch("focus_salis_mundus", "Focus Effect: Salis Mundus",
                "Although Salis Mundus is cheap, it's also a lot of salt. Every time I need to build something, I'll always be short on it." +
                        "<BR>It's just a formality to apply some magic to it, why not just smelt it?",
                "A focus version of the magical dust. Triggers transmutations on certain blocks without consuming Salis Mundus." +
                        "<BR>Nothing more to it than that.");

        addResearch("everburning_urn", "Everburning Urn",
                "The Everfull Urn is an incredibly useful device, but I'm convinced that I can do better. Something similar, but for other types of fluids. Fluids like, say... lava?",
                "By infusing the Everfull Urn with a series of microscopic portals to the Nether, I'm able to siphon all the lava that I could ever need. The process is not perfect, however." +
                        "<BR>For one thing, it's slow. Whereas its watery brethren fills up in moments, this device, which I've dubbed the Everburning Urn, takes about a minute to generate a single bucket." +
                        "<BR>The process is much more draining to the aura, as well. My calculations indicate that maintaining the portal mesh consumes about 25 points of vis from the aura." +
                        "<BR>Finally, for safety's sake, I've disabled the urn's ability to automatically fill nearby vessels. Can't have liquid hot magma just flying around the place.");

        addResearch("rainbow_scribing_tools", "Rainbow Scribing Tools",
                "Honestly, I'm sick of diving for squid. That ink is practically staining my research table black... Why does writing have to use black dye? Can't it be a \"Gorgeous Black\"?",
                "No matter what dye you put in this little bottle, it always writes in black — truly a \"Gorgeous Black\"." +
                        "<BR>And animal rights activists won't sue you!");

        add("focus.thaumaturgeadditions.salis_mundus.name", "Salis Mundus");
        add("focus.thaumaturgeadditions.salis_mundus.text", "A focus-form of magical dust: triggers transmutations on certain blocks without consuming Salis Mundus.");

        add("trim_material.thaumaturge.brass", "Brass");
        add("trim_material.thaumaturge.thaumium", "Thaumium");
        add("trim_material.thaumaturge.void", "Void");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s".formatted(ThaumaturgeAdditions.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgeAdditions.MODID, researchId, i), stage[i]);
        }
    }

    private void addResearchAddenda(String researchId, String... stage) {
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(ThaumaturgeAdditions.MODID, researchId, i), stage[i]);
        }
    }
}
