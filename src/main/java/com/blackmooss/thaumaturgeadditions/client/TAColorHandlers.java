package com.blackmooss.thaumaturgeadditions.client;

import com.blackmooss.thaumaturgeadditions.client.tint.RainbowTintSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class TAColorHandlers {
    private TAColorHandlers() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(TAColorHandlers::onRegisterItemTintSources);
    }

    private static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(RainbowTintSource.ID, RainbowTintSource.MAP_CODEC);
    }
}
