package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item.SophisticatedBackpacksArcaneChargerItemFactory;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item.SophisticatedBackpacksArcaneWorkbenchItemFactory;
import com.leclowndu93150.thaumaturge.api.recipe.RegisterWorkbenchAuraSourcesEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class SophisticatedBackpacksCompat {
    public static final String SOPHISTICATED_BACKPACK_MOD_ID = "sophisticatedbackpacks";

    public static final UUID WORKBENCH_IDENTITY =
            UUID.nameUUIDFromBytes("thaumaturgeadditions:sophisticatedbackpacks_arcane_workbench".getBytes(StandardCharsets.UTF_8));

    private SophisticatedBackpacksCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(SOPHISTICATED_BACKPACK_MOD_ID);
    }

    @SubscribeEvent
    public static void onRegisterAuraSources(RegisterWorkbenchAuraSourcesEvent event) {
        event.register(new SophisticatedBackpacksArcaneWorkbenchAuraSource());
    }

    public static Item createArcaneWorkbenchUpgradeItem(Item.Properties properties) {
        Objects.requireNonNull(properties, "properties");
        return isLoaded() ? SophisticatedBackpacksArcaneWorkbenchItemFactory.create(properties) : new Item(properties);
    }

    public static Item createArcaneChargerUpgradeItem(Item.Properties properties) {
        Objects.requireNonNull(properties, "properties");
        return isLoaded() ? SophisticatedBackpacksArcaneChargerItemFactory.create(properties) : new Item(properties);
    }

    public static void onRegisterUpgradeContainers(RegisterEvent event) {
        if (!isLoaded() || !event.getRegistryKey().equals(Registries.MENU)) {
            return;
        }
        SophisticatedBackpacksArcaneWorkbenchContainerRegistration.register();
    }
}
