package com.blackmooss.thaumaturgeaddon.data;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeaddon.client.tint.RainbowTintSource;
import com.blackmooss.thaumaturgeaddon.registry.TABlocks;
import com.blackmooss.thaumaturgeaddon.registry.TAItems;
import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.client.model.JarBrainItemSpecialRenderer;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.CompositeModel;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        voidJar(blockModels, itemModels);
        voidTravellerBoots(itemModels);
        everburningUrn(blockModels, itemModels);
        rainbowScribingTools(itemModels);
    }

    private static void voidJar(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        Identifier jarVoid = TCIds.rl("block/jar_void");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.VOID_BRAIN_JAR.get(), BlockModelGenerators.plainVariant(jarVoid)));
        itemModels.itemModelOutput.accept(TAItems.VOID_BRAIN_JAR.get(),
                new CompositeModel.Unbaked(
                        List.of(new CuboidItemModelWrapper.Unbaked(jarVoid, Optional.empty(), List.of()),
                                new SpecialModelWrapper.Unbaked(jarVoid, Optional.empty(), new JarBrainItemSpecialRenderer.Unbaked())),
                        Optional.empty()));
    }

    private static void voidTravellerBoots(@NonNull ItemModelGenerators itemModels) {
        Identifier identifier = ThaumaturgeAdditions.identifier("item/void_traveller_boots");
        Material clothTex = new Material(ThaumaturgeAdditions.identifier("item/void_traveller_boots_overlay"));
        Material metalTex = new Material(identifier);
        ModelTemplates.TWO_LAYERED_ITEM.create(identifier, TextureMapping.layered(clothTex, metalTex), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.VOID_TRAVELLER_BOOTS.get(), ItemModelUtils.tintedModel(identifier, new Dye(-9815936)));
    }

    private static void everburningUrn(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        Identifier urnModel = ThaumaturgeAdditions.identifier("block/everburning_urn");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.EVERBURNING_URN.get(), BlockModelGenerators.plainVariant(urnModel)));
        itemModels.itemModelOutput.accept(TAItems.EVERBURNING_URN.get(), ItemModelUtils.plainModel(urnModel));
    }

    private static void rainbowScribingTools(@NonNull ItemModelGenerators itemModels) {
        Identifier modelId = ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools");
        Material base = new Material(ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools"));
        Material overlay = new Material(ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools_overlay"));
        ModelTemplates.TWO_LAYERED_ITEM.create(modelId, TextureMapping.layered(base, overlay), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.RAINBOW_SCRIBING_TOOLS.get(),
                ItemModelUtils.tintedModel(modelId, new Constant(0xFFFFFF), new RainbowTintSource()));
    }
}
