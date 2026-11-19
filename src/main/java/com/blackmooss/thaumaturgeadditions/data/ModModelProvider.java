package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.model.LazyCatModelProvider;
import net.minecraft.data.PackOutput;

public final class ModModelProvider extends LazyCatModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgeAdditions.MODID);
    }

    @Override
    protected void addModels(ModelContext context) {
        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE.get());
        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE.get());
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());

        itemTwoLayered(TAItems.RAINBOW_SCRIBING_TOOLS.get(),
                getItemModelId(TAItems.RAINBOW_SCRIBING_TOOLS.get()),
                ThaumaturgeAdditions.identifier("item/rainbow_scribing_tools_overlay"));
        itemTwoLayered(TAItems.VOID_TRAVELLER_BOOTS.get(),
                getItemModelId(TAItems.VOID_TRAVELLER_BOOTS.get()),
                ThaumaturgeAdditions.identifier("item/void_traveller_boots_overlay"));
    }
}
