package com.blackmooss.thaumaturgeadditions.data.lang;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnUsProvider extends LanguageProvider {
    public EnUsProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgeadditions", "Thaumaturge Additions");
        research();
        configuration();

        addItem(TAItems.VOID_TRAVELLER_BOOTS, "Void Traveller's Boots");
        addItem(TAItems.RAINBOW_SCRIBING_TOOLS, "Rainbow Scribing Tools");
        addItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE, "Sophisticated Backpack Arcane Workbench Upgrade");
        addItem(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE, "Traveler's Backpack Arcane Workbench Upgrade");
        addItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE, "Sophisticated Backpack Arcane Charger Upgrade");
        addItem(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE, "Traveler's Backpack Arcane Charger Upgrade");

        addBlock(TABlocks.VOID_BRAIN_JAR, "Void Brain in a Jar");
        addBlock(TABlocks.EVERBURNING_URN, "Everburning Urn");

        add("focus.thaumaturgeadditions.salis_mundus.name", "Salis Mundus");
        add("focus.thaumaturgeadditions.salis_mundus.text", "A focus-form of magical dust: triggers transmutations on certain blocks without consuming Salis Mundus.");
        add("spell.thaumaturgeadditions.salis_mundus.name", "Salis Mundus");
        add("spell.thaumaturgeadditions.salis_mundus.desc", "Cast on a block to transmute it according to that block's transmutation recipe, without spending Salis Mundus.");

        add("screen.thaumaturgeadditions.arcane_workbench_upgrade", "Arcane Workbench");
        add("item.thaumaturgeadditions.travelersbackpack_arcane_workbench_upgrade.tooltip", "Consume the aura of the chunk to craft the arcane recipes anytime, anywhere, just like an Arcane Workbench!");
        add("item.thaumaturgeadditions.sophisticatedbackpacks_arcane_workbench_upgrade.tooltip", "Consume the aura of the chunk to craft the arcane recipes anytime, anywhere, just like an Arcane Workbench!");
        add("item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade.tooltip", "In the backpack: lets the Arcane Workbench upgrade draw vis from the surrounding 3x3 chunks.");
        add("item.thaumaturgeadditions.sophisticatedbackpacks_arcane_charger_upgrade.tooltip", "In the backpack: lets the Arcane Workbench upgrade draw vis from the surrounding 3x3 chunks.");
    }

    private void research() {
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
                "By infusing the Everfull Urn with a lattice of microscopic portals to the Nether, I'm able to siphon all the lava that I could ever need. The process is not perfect, however." +
                        "<BR>For one thing, it is bound to the aura. Whereas its watery brethren fill up in moments, this device, which I've dubbed the Everburning Urn, can only conjure lava as fast as it draws vis from the local aura, a trickle at a time." +
                        "<BR>The cost is modest, though: keeping the portal mesh alive spends barely a point of vis for every bucket it yields." +
                        "<BR>What I did not expect was its lack of restraint. Left to itself, the urn reaches out and pours into any nearby cauldron or fluid container it can find. I've since learned to keep that impulse in check, though the option to let it run wild remains.");

        addResearch("rainbow_scribing_tools", "Rainbow Scribing Tools",
                "Honestly, I'm sick of diving for squid. That ink is practically staining my research table black... Why does writing have to use black dye? Can't it be a \"Gorgeous Black\"?",
                "No matter what dye you put in this little bottle, it always writes in black — truly a \"Gorgeous Black\".");

        addResearch("arcane_workbench_upgrade", "Arcane Workbench Upgrade",
                "Since you can already craft inside a backpack, who wouldn't want to craft with vis inside a backpack too?",
                "This backpack upgrade consumes the aura of the chunk to craft arcane recipes anytime, anywhere, just like an Arcane Workbench!");

        addResearchAddenda("arcane_workbench_upgrade", "Don't forget to install a charger in the backpack!");
    }

    private void configuration() {
        addConfiguration("title", "Thaumaturge Additions Config");

        addConfiguration("section.thaumaturgeadditions.client.toml", "Thaumaturge Additions Client Settings");
        addConfiguration("section.thaumaturgeadditions.client.toml.title", "Thaumaturge Additions Client Settings");

        addConfiguration("everburning_urn_auto_fill", "Everburning Urn Auto Fill");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgeAdditions.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgeAdditions.MODID, researchId, i), stage[i]);
        }
    }

    private void addResearchAddenda(String researchId, String... stage) {
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(ThaumaturgeAdditions.MODID, researchId, i), stage[i]);
        }
    }

    private void addConfiguration(String configID, String translations) {
        add("%s.configuration.%s".formatted(ThaumaturgeAdditions.MODID, configID), translations);
    }
}
