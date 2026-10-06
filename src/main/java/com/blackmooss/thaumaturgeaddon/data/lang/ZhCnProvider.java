package com.blackmooss.thaumaturgeaddon.data.lang;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeaddon.registry.TABlocks;
import com.blackmooss.thaumaturgeaddon.registry.TAItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ZhCnProvider extends LanguageProvider {

    public ZhCnProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgeadditions", "Thaumaturge 附加");

        addItem(TAItems.VOID_TRAVELLER_BOOTS, "虚空旅行者之靴");
        addItem(TAItems.RAINBOW_SCRIBING_TOOLS, "彩虹笔与墨");
        addBlock(TABlocks.VOID_BRAIN_JAR, "虚空缸中之脑");
        addBlock(TABlocks.EVERBURNING_URN, "永燃之瓮");

        addResearch("void_traveller_boots", "虚空旅行者之靴",
                "我脚上的这双旅行者之靴已经换了一遍又一遍，我实在是受够每次坏掉都要重新合成了！既然虚空金属可以自我修复，何不将二者合二为一？",
                "这双富含虚空力量的靴子能让旅途变得更上一层楼，物理上的。" +
                        "<BR>属性较普通的旅行者之靴直接翻了一倍！而且还会自我修复，可储存的灵气也更多了，不过扭曲还是不可避免的…" +
                        "<BR>顺带一提，这款靴子支持染色！");

        addResearch("focus_salis_mundus", "核心效果：世界盐",
                "虽然世界盐本身足够便宜，但是这玩意用量还是挺大的，每次要搭建一些结构需要世界盐的时候，总会缺少一点点。" +
                        "<BR>本来也就是走个形式往上面撒点魔法，何必专门去研磨几粒盐呢？",
                "世界盐的核心版，施加于特定方块时可触发转化，且不消耗世界盐。" +
                        "<BR>仅此而已，也没什么别的了。");

        addResearch("everburning_urn", "永燃之瓮",
                "虽然无尽之瓮极其便利，但我确信还能更进一步。制造一种类似的、但适用于其他类型流体的装置。比如说……熔岩？",
                "通过向无尽之瓮中注入一系列通往下界的微型传送门，我能够汲取到我所需的全部熔岩。然而，这个过程并非完美无缺。" +
                        "<BR>首先，速度很慢。它水版本的兄弟顷刻间就能填满，但这个被我称为“永燃之瓮”的装置，需要大约一分钟才能生成一桶熔岩。" +
                        "<BR>同时，这个过程对灵气场的消耗也大得多。据我计算，维持传送门网络大约会消耗灵气场中25点灵气。" +
                        "<BR>最后，为了安全起见，我禁用了这个瓮自动填充附近容器的功能。可不能让滚烫的熔岩四处飞溅。");

        addResearch("rainbow_scribing_tools", "彩虹笔与墨",
                "老实说，我真的受够下水打鱿鱼了，那些乌漆麻黑的染料都快把我的研究台染黑了……凭什么写东西必须要黑色染料？不能是五彩斑斓的黑吗？",
                "这个小瓶子里面无论装什么染料，写出来都是黑色的，名副其实“五彩斑斓的黑”。" +
                        "<BR>而且动保不会起诉你！");

        add("focus.thaumaturgeadditions.salis_mundus.name", "世界盐");
        add("focus.thaumaturgeadditions.salis_mundus.text", "魔法尘埃的核心版，施加于特定方块时可触发转化，且不消耗世界盐。");

        add("trim_material.thaumaturge.brass", "黄铜");
        add("trim_material.thaumaturge.thaumium", "神秘");
        add("trim_material.thaumaturge.void", "虚空金属");
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
}
