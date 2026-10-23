package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades.SBArcaneWorkbenchUpgradeWrapper;
import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SBArcaneWorkbenchUpgradeItem
        extends UpgradeItemBase<SBArcaneWorkbenchUpgradeWrapper> {
    private static final UpgradeType<SBArcaneWorkbenchUpgradeWrapper> TYPE =
            new UpgradeType<>(SBArcaneWorkbenchUpgradeWrapper::new);

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

    public SBArcaneWorkbenchUpgradeItem(Properties properties) {
        super(LIMIT_CONFIG);
    }

    @Override
    public @NonNull UpgradeType<SBArcaneWorkbenchUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NonNull List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
