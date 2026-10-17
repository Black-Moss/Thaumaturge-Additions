package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.client.tint.RainbowTintSource;
import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.client.model.JarBrainItemSpecialRenderer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    private static void arcaneWorkbenchUpgrade(@NonNull ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
    }

    private static void voidJar(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        ResourceLocation jarVoid = TTIds.rl("block/jar_void");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.VOID_BRAIN_JAR.get(), BlockModelGenerators.plainVariant(jarVoid)));
        itemModels.itemModelOutput.accept(TAItems.VOID_BRAIN_JAR.get(),
                new CompositeModel.Unbaked(
                        List.of(new CuboidItemModelWrapper.Unbaked(jarVoid, Optional.empty(), List.of()),
                                new SpecialModelWrapper.Unbaked(jarVoid, Optional.empty(), new JarBrainItemSpecialRenderer.Unbaked())),
                        Optional.empty()));
    }

    private static void voidTravellerBoots(@NonNull ItemModelGenerators itemModels) {
        ResourceLocation ResourceLocation = ThaumaturgeAdditions.ResourceLocation("item/void_traveller_boots");
        Material clothTex = new Material(ThaumaturgeAdditions.ResourceLocation("item/void_traveller_boots_overlay"));
        Material metalTex = new Material(ResourceLocation);
        ModelTemplates.TWO_LAYERED_ITEM.create(ResourceLocation, TextureMapping.layered(clothTex, metalTex), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.VOID_TRAVELLER_BOOTS.get(), ItemModelUtils.tintedModel(ResourceLocation, new Dye(-9815936)));
    }

    private static void everburningUrn(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        ResourceLocation urnModel = ThaumaturgeAdditions.ResourceLocation("block/everburning_urn");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.EVERBURNING_URN.get(), BlockModelGenerators.plainVariant(urnModel)));
        itemModels.itemModelOutput.accept(TAItems.EVERBURNING_URN.get(), ItemModelUtils.plainModel(urnModel));
    }

    private static void rainbowScribingTools(@NonNull ItemModelGenerators itemModels) {
        ResourceLocation modelId = ThaumaturgeAdditions.ResourceLocation("item/rainbow_scribing_tools");
        Material base = new Material(ThaumaturgeAdditions.ResourceLocation("item/rainbow_scribing_tools"));
        Material overlay = new Material(ThaumaturgeAdditions.ResourceLocation("item/rainbow_scribing_tools_overlay"));
        ModelTemplates.TWO_LAYERED_ITEM.create(modelId, TextureMapping.layered(base, overlay), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.RAINBOW_SCRIBING_TOOLS.get(),
                ItemModelUtils.tintedModel(modelId, new Constant(0xFFFFFF), new RainbowTintSource()));
    }

    @Override
    private void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        voidJar(blockModels, itemModels);
        voidTravellerBoots(itemModels);
        everburningUrn(blockModels, itemModels);
        rainbowScribingTools(itemModels);
        arcaneWorkbenchUpgrade(itemModels);
    }
}
