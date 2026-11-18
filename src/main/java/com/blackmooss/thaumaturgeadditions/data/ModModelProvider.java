package com.blackmooss.thaumaturgeadditions.data;

import com.blackmooss.thaumaturgeadditions.registry.TABlocks;
import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.model.LazyCatModelProvider;
import net.minecraft.data.PackOutput;

public final class ModModelProvider extends LazyCatModelProvider {
    public ModModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void addModels(ModelContext context) {
        blockCustom(TABlocks.EVERBURNING_URN.get());
        blockBlockState(TABlocks.HUNGRY_TRAPPED_CHEST.get());
        blockBlockState(TABlocks.VOID_BRAIN_JAR.get());

        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_CHARGER_UPGRADE.get());
        itemFlat(TAItems.SOPHISTICATED_BACKPACKS_ARCANE_WORKBENCH_UPGRADE.get());
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_CHARGER_UPGRADE.get());
        itemFlat(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());
    }
}
