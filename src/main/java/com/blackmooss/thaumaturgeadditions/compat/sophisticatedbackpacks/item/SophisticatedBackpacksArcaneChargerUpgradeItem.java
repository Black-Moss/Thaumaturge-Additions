package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SophisticatedBackpacksArcaneChargerUpgradeWrapper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SophisticatedBackpacksArcaneChargerUpgradeItem
        extends UpgradeItemBase<SophisticatedBackpacksArcaneChargerUpgradeWrapper> {
    public static final UpgradeType<SophisticatedBackpacksArcaneChargerUpgradeWrapper> TYPE =
            new UpgradeType<>(SophisticatedBackpacksArcaneChargerUpgradeWrapper::new);

    private static final IUpgradeCountLimitConfig LIMIT_CONFIG = new IUpgradeCountLimitConfig() {
        @Override
        public int getMaxUpgradesPerStorage(@NonNull String storageType, Identifier itemId) {
            return 1;
        }

        @Override
        public int getMaxUpgradesInGroupPerStorage(@NonNull String storageType, @NonNull UpgradeGroup group) {
            return 1;
        }
    };

    public SophisticatedBackpacksArcaneChargerUpgradeItem(Properties properties) {
        super(LIMIT_CONFIG, properties);
    }

    @Override
    public @NonNull UpgradeType<SophisticatedBackpacksArcaneChargerUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NonNull List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }

    public static boolean hasCharger(Player player) {
        return PlayerInventoryProvider.get().runOnBackpacks(player, (backpack, handlerName, identifier, slot) ->
                BackpackWrapper.fromStack(backpack).getUpgradeHandler().hasUpgrade(TYPE));
    }
}
