package com.blackmoss.thaumaturgeadditions;

import com.blackmoss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmoss.thaumaturgeadditions.config.ThaumaturgeAdditionsServerConfig;
import com.blackmoss.thaumaturgeadditions.data.ModBlockLootSubProvider;
import com.blackmoss.thaumaturgeadditions.data.ModModelProvider;
import com.blackmoss.thaumaturgeadditions.data.ModRecipeProvider;
import com.blackmoss.thaumaturgeadditions.data.ModResearchProvider;
import com.blackmoss.thaumaturgeadditions.data.lang.EnUsProvider;
import com.blackmoss.thaumaturgeadditions.data.lang.ZhCnProvider;
import com.blackmoss.thaumaturgeadditions.data.tag.ModBlockTagsProvider;
import com.blackmoss.thaumaturgeadditions.data.tag.ModItemTagsProvider;
import com.blackmoss.thaumaturgeadditions.registry.*;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import com.leclowndu93150.thaumaturge.data.worldgen.aspect.AspectBootstrap;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
@Mod(ThaumaturgeAdditions.MODID)
public class ThaumaturgeAdditions {
    public static final String MODID = "thaumaturgeadditions";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ThaumaturgeAdditions(IEventBus modEventBus, ModContainer modContainer) {
        TACreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        TABlocks.BLOCKS.register(modEventBus);
        TABlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TADataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        TAItems.ITEMS.register(modEventBus);
        TASpellBehaviors.SPELL_BEHAVIORS.register(modEventBus);
        modEventBus.addListener(SophisticatedBackpacksCompat::onRegisterUpgradeContainers);

        modContainer.registerConfig(ModConfig.Type.SERVER, ThaumaturgeAdditionsServerConfig.SPEC);
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        RegistrySetBuilder registries = new RegistrySetBuilder()
                .add(IAspect.REGISTRY_KEY, AspectBootstrap::bootstrap)
                .add(SpellPart.REGISTRY_KEY, TASpellBehaviors::bootstrapParts);
        event.createDatapackRegistryObjects(registries);

        event.createProvider(EnUsProvider::new);
        event.createProvider(ZhCnProvider::new);

        event.createProvider(ModRecipeProvider.Runner::new);

        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModBlockTagsProvider::new);

        event.createProvider(ModModelProvider::new);

        event.createProvider(ModResearchProvider::new);


        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
    }
}
