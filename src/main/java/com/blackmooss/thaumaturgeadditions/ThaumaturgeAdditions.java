package com.blackmooss.thaumaturgeadditions;

import com.blackmooss.thaumaturgeadditions.client.TAColorHandlers;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.blackmooss.thaumaturgeadditions.data.ModBlockLootSubProvider;
import com.blackmooss.thaumaturgeadditions.data.ModModelProvider;
import com.blackmooss.thaumaturgeadditions.data.ModRecipeProvider;
import com.blackmooss.thaumaturgeadditions.data.lang.EnUsProvider;
import com.blackmooss.thaumaturgeadditions.data.lang.ZhCnProvider;
import com.blackmooss.thaumaturgeadditions.data.tag.ModBlockTagsProvider;
import com.blackmooss.thaumaturgeadditions.data.tag.ModItemTagsProvider;
import com.blackmooss.thaumaturgeadditions.registry.*;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
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
        // TAFocusElements.ELEMENTS.register(modEventBus);
        TABlocks.BLOCKS.register(modEventBus);
        TABlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TADataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        TAItems.ITEMS.register(modEventBus);
        TAColorHandlers.register(modEventBus);
        modEventBus.addListener(SophisticatedBackpacksCompat::onRegisterUpgradeContainers);
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        RegistrySetBuilder registries = new RegistrySetBuilder()
                .add(IAspect.REGISTRY_KEY, AspectBootstrap::bootstrap);
        event.createDatapackRegistryObjects(registries);

        event.createProvider(EnUsProvider::new);
        event.createProvider(ZhCnProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModBlockTagsProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
    }

}
