package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SBArcaneChargerUpgradeWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SBArcaneChargerUpgradeItem
        extends UpgradeItemBase<SBArcaneChargerUpgradeWrapper> {
    public static final UpgradeType<SBArcaneChargerUpgradeWrapper> TYPE =
            new UpgradeType<>(SBArcaneChargerUpgradeWrapper::new);

    private static final IUpgradeCountLimitConfig LIMIT_CONFIG = new IUpgradeCountLimitConfig() {
        @Override
        public int getMaxUpgradesPerStorage(@NonNull String storageType, ResourceLocation itemId) {
            return 1;
        }

        @Override
        public int getMaxUpgradesInGroupPerStorage(@NonNull String storageType, @NonNull UpgradeGroup group) {
            return 1;
        }
    };

    public SBArcaneChargerUpgradeItem(Properties properties) {
        super(LIMIT_CONFIG);
    }

    public static boolean hasCharger(Player player) {
        return PlayerInventoryProvider.get().runOnBackpacks(player, (backpack, handlerName, ResourceLocation, slot) ->
                BackpackWrapper.fromStack(backpack).getUpgradeHandler().hasUpgrade(TYPE));
    }

    @Override
    public @NonNull UpgradeType<SBArcaneChargerUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NonNull List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
