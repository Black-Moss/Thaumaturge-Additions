package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.item.SBArcaneChargerUpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import java.util.function.Consumer;

public class SBArcaneChargerUpgradeWrapper
        extends UpgradeWrapperBase<SBArcaneChargerUpgradeWrapper, SBArcaneChargerUpgradeItem> {
    public SBArcaneChargerUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
    }
}
