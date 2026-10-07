package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item.TravelersBackpackArcaneChargerItemFactory;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item.TravelersBackpackArcaneWorkbenchItemFactory;
import com.leclowndu93150.thaumaturge.api.recipe.RegisterWorkbenchAuraSourcesEvent;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class TravelersBackpackCompat {
    public static final String TRAVELERS_BACKPACK_MOD_ID = "travelersbackpack";

    public static final UUID WORKBENCH_IDENTITY =
            UUID.nameUUIDFromBytes("thaumaturgeadditions:travelersbackpack_arcane_workbench".getBytes(StandardCharsets.UTF_8));

    private TravelersBackpackCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(TRAVELERS_BACKPACK_MOD_ID);
    }

    @SubscribeEvent
    public static void onRegisterAuraSources(RegisterWorkbenchAuraSourcesEvent event) {
        event.register(new TravelersBackpackArcaneWorkbenchAuraSource());
    }

    public static Item createArcaneWorkbenchUpgradeItem(Item.Properties properties) {
        Objects.requireNonNull(properties, "properties");
        return isLoaded() ? TravelersBackpackArcaneWorkbenchItemFactory.create(properties) : new Item(properties);
    }

    public static Item createArcaneChargerUpgradeItem(Item.Properties properties) {
        Objects.requireNonNull(properties, "properties");
        return isLoaded() ? TravelersBackpackArcaneChargerItemFactory.create(properties) : new Item(properties);
    }
}
