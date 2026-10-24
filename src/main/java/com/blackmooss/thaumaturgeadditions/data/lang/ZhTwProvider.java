package com.blackmooss.thaumaturgeadditions.data.lang;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ZhTwProvider extends LanguageProvider {

    public ZhTwProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID, "zh_tw");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgeadditions", "神秘附加");
        research();
        configuration();

        addItem(TAItems.VOID_TRAVELLER_BOOTS, "虛空旅人之靴");
        addItem(TAItems.RAINBOW_SCRIBING_TOOLS, "彩虹書記用具");
        addItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE, "精妙背包奧術工作台升級");
        addItem(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE, "旅行背包奧術工作台升級");
        addItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE, "精妙背包奧術充能板升級");
        addItem(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE, "旅行背包奧術充能板升級");

        addBlock(TABlocks.VOID_BRAIN_JAR, "虛空缸中之腦");
        addBlock(TABlocks.EVERBURNING_URN, "永燃之瓮");

        add("focus.thaumaturgeadditions.salis_mundus.name", "世界鹽");
        add("focus.thaumaturgeadditions.salis_mundus.text", "魔法塵埃的核心版，施加於特定方塊時可觸發轉化，且不消耗世界鹽。");
        add("spell.thaumaturgeadditions.salis_mundus.name", "世界鹽");
        add("spell.thaumaturgeadditions.salis_mundus.desc", "對著方塊施放，按該方塊的轉化配方替換它，且不额外消耗世界鹽。");

        add("screen.thaumaturgeadditions.arcane_workbench_upgrade", "奧術工作台");
        add("item.thaumaturgeadditions.travelersbackpack_arcane_workbench_upgrade.tooltip", "消耗所在區塊的靈氣隨時隨地合成奧術配方，就像奧術工作台一樣！");
        add("item.thaumaturgeadditions.sophisticatedbackpacks_arcane_workbench_upgrade.tooltip", "消耗所在區塊的靈氣隨時隨地合成奧術配方，就像奧術工作台一樣！");
        add("item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade.tooltip", "裝在背包裏：讓奧術工作台升級從周圍 3x3 區塊吸取靈氣。");
        add("item.thaumaturgeadditions.sophisticatedbackpacks_arcane_charger_upgrade.tooltip", "裝在背包裏：讓奧術工作台升級從周圍 3x3 區塊吸取靈氣。");
    }

    private void research() {
        addResearch("void_traveller_boots", "虛空旅人之靴",
                "我腳上的這雙旅人之靴已經換了一遍又一遍，我實在是受夠每次壞掉都要重新合成了！既然虛鋼可以自我修復，何不將二者合二爲一？",
                "這雙富含虛空力量的靴子能讓旅途變得更上一層樓，物理上的。" +
                        "<BR>屬性較普通的旅人之靴直接翻了一倍！而且還會自我修復，可儲存的靈氣也更多了，不過扭曲還是不可避免的…" +
                        "<BR>順帶一提，這款靴子支持染色！");

        addResearch("focus_salis_mundus", "核心效果：世界鹽",
                "雖然世界鹽本身足夠便宜，但是這玩意用量還是挺大的，每次要搭建一些結構需要世界鹽的時候，總會缺少一點點。" +
                        "<BR>本來也就是走個形式往上面撒點魔法，何必專門去研磨幾粒鹽呢？",
                "世界鹽的核心版，施加於特定方塊時可觸發轉化，且不消耗世界鹽。" +
                        "<BR>僅此而已，也沒什麼別的了。");

        addResearch("everburning_urn", "永燃之甕",
                "雖然無盡之甕極其便利，但我確信還能更進一步。製造一種類似的、但適用於其他類型流體的裝置。比如說……熔岩？",
                "通過向無盡之甕中注入一組通往下界的微型傳送門，我能夠汲取到所需的全部熔岩。然而，這個過程並非完美無缺。" +
                        "<BR>首先，它受制於靈氣場。它水版本的兄弟頃刻間就能填滿，而這個被我稱爲“永燃之甕”的裝置，只能隨著從局部靈氣場汲取靈氣的速度緩緩凝出熔岩，一次不過涓滴。" +
                        "<BR>不過消耗倒不大：維持傳送門網絡，每產出一桶熔岩大約只耗去靈氣場中1點靈氣。" +
                        "<BR>我沒料到的是它全無節制。放任不管時，它會主動把熔岩灌進附近任何一個煉藥鍋或帶流體能力的容器裏。後來我總算學會約束這股衝動——不過，想讓它撒歡也不是不行。");

        addResearch("rainbow_scribing_tools", "彩虹書記用具",
                "老實說，我真的受夠下水打魷魚了，那些烏漆麻黑的染料都快把我的研究臺染黑了……憑什麼寫東西必須要黑色染料？不能是五彩斑斕的黑嗎？",
                "這個小瓶子裏面無論裝什麼染料，寫出來都是黑色的，名副其實“五彩斑斕的黑”。");

        addResearch("arcane_workbench_upgrade", "奧術工作臺升級",
                "既然可以在背包裏面合成，誰會不想在背包裏面用靈氣合成呢？",
                "這個背包升級可以消耗所在區塊的靈氣隨時隨地合成奧術配方，就像奧術工作臺一樣！");

        addResearchAddenda("arcane_workbench_upgrade", "可不能忘記在背包裏面裝一個充能器！");
    }

    private void configuration() {
        addConfiguration("title", "神秘附加配置文件");

        addConfiguration("section.thaumaturgeadditions.client.toml", "神秘附加客戶端配置文件");
        addConfiguration("section.thaumaturgeadditions.client.toml.title", "神秘附加客戶端配置文件");

        addConfiguration("everburning_urn_auto_fill", "永燃之瓮自動填充");
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
