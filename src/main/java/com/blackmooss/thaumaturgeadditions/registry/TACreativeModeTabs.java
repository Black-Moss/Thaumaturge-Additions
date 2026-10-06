package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TACreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThaumaturgeAdditions.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THAUMATURGE_ADDITIONS = CREATIVE_MODE_TABS.register(ThaumaturgeAdditions.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thaumaturgeadditions"))
                    .icon(() -> TAItems.VOID_TRAVELLER_BOOTS.get().getDefaultInstance()).displayItems((_, output) -> {
                        output.accept(TAItems.VOID_TRAVELLER_BOOTS.get());
                        output.accept(TAItems.VOID_BRAIN_JAR.get());
                        output.accept(TAItems.EVERBURNING_URN.get());
                        output.accept(TAItems.RAINBOW_SCRIBING_TOOLS.get());
                        output.accept(TAItems.TRAVELERS_BACKPACK_ARCANE_WORKBENCH_UPGRADE.get());
                    }).build());
}
