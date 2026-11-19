package com.blackmoss.thaumaturgeadditions.data;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.model.LazyCatModelProvider;
import com.blackmoss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmoss.thaumaturgeadditions.client.tint.RainbowTintSource;
import com.blackmoss.thaumaturgeadditions.registry.TABlocks;
import com.blackmoss.thaumaturgeadditions.registry.TAItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.client.model.JarBrainItemSpecialRenderer;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.item.CompositeModel;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;

import java.util.List;
import java.util.Optional;

public class ModModelProvider extends LazyCatModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    private static void voidJar(ModelContext modelContext) {
        Identifier jarVoid = TTIds.rl("block/jar_void");
        modelContext.blockModels().blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.VOID_BRAIN_JAR.get(), BlockModelGenerators.plainVariant(jarVoid)));
        modelContext.itemModels().itemModelOutput.accept(TAItems.VOID_BRAIN_JAR.get(),
                new CompositeModel.Unbaked(
                        List.of(new CuboidItemModelWrapper.Unbaked(jarVoid, Optional.empty(), List.of()),
                                new SpecialModelWrapper.Unbaked(jarVoid, Optional.empty(), new JarBrainItemSpecialRenderer.Unbaked())),
                        Optional.empty()));
    }

    private static void voidTravellerBoots(ModelContext modelContext) {
        var itemModels = modelContext.itemModels();
        Identifier identifier = ThaumaturgeAdditions.identifier("item/void_traveller_boots");
        Material clothTex = new Material(ThaumaturgeAdditions.identifier("item/void_traveller_boots_overlay"));
        Material metalTex = new Material(identifier);
        ModelTemplates.TWO_LAYERED_ITEM.create(identifier, TextureMapping.layered(clothTex, metalTex), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.VOID_TRAVELLER_BOOTS.asItem(), ItemModelUtils.tintedModel(identifier, new Dye(-9815936)));
    }

    private static void everburningUrn(ModelContext modelContext) {
        Identifier urnModel = ThaumaturgeAdditions.identifier("block/everburning_urn");
        modelContext.blockModels().blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.EVERBURNING_URN.get(), BlockModelGenerators.plainVariant(urnModel)));
        modelContext.itemModels().itemModelOutput.accept(TAItems.EVERBURNING_URN.get(), ItemModelUtils.plainModel(urnModel));
    }

    private static void rainbowScribingTools(ModelContext modelContext) {
        var itemModels = modelContext.itemModels();
        Identifier modelId = ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools");
        Material base = new Material(ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools"));
        Material overlay = new Material(ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools_overlay"));
        ModelTemplates.TWO_LAYERED_ITEM.create(modelId, TextureMapping.layered(base, overlay), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TAItems.RAINBOW_SCRIBING_TOOLS.get(),
                ItemModelUtils.tintedModel(modelId, new Constant(0xFFFFFF), new RainbowTintSource()));
    }

    private static void hungryTrappedChest(ModelContext modelContext) {
        Identifier empty = TTIds.rl("block/empty");
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(empty)));
        modelContext.blockModels().blockStateOutput.accept(MultiVariantGenerator.dispatch(TABlocks.HUNGRY_TRAPPED_CHEST.get(), variant));
        modelContext.itemModels().itemModelOutput.accept(TAItems.HUNGRY_TRAPPED_CHEST.get(), new SpecialModelWrapper.Unbaked(
                Identifier.withDefaultNamespace("item/chest"),
                Optional.empty(),
                new ChestSpecialRenderer.Unbaked(ThaumaturgeAdditions.identifier("hungry_trapped"))));
    }

    @Override
    protected void addModels(ModelContext modelContext) {
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());
        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE.get());
        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE.get());

        voidJar(modelContext);
        voidTravellerBoots(modelContext);
        everburningUrn(modelContext);
        rainbowScribingTools(modelContext);
        hungryTrappedChest(modelContext);
    }
}
