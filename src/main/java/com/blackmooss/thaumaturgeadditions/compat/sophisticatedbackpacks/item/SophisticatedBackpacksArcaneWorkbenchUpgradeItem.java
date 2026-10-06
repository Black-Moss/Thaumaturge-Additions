package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Consumer;

public class SophisticatedBackpacksArcaneWorkbenchUpgradeItem
        extends UpgradeItemBase<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper> {
    private static final UpgradeType<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper> TYPE =
            new UpgradeType<>(SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper::new);

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

    public SophisticatedBackpacksArcaneWorkbenchUpgradeItem(Properties properties) {
        super(LIMIT_CONFIG, properties);
    }

    @Override
    public @NonNull UpgradeType<SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NonNull List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
