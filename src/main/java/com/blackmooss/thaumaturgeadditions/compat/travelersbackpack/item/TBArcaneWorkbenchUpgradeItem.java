package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import com.tiviacz.travelersbackpack.util.ContainerContentsHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;

public class TBArcaneWorkbenchUpgradeItem extends UpgradeItem {
    public TBArcaneWorkbenchUpgradeItem(Properties properties) {
        super(properties, null);
    }

    @Override
    public boolean isTickingUpgrade() {
        return true;
    }

    @Override
    public boolean requiresEquippedBackpack() {
        return false;
    }

    @Override
    public Class<? extends UpgradeBase<?>> getUpgradeClass() {
        return TBArcaneWorkbenchUpgrade.class;
    }

    @Override
    public TriFunction<UpgradeManager, Integer, ItemStack, Optional<? extends UpgradeBase<?>>> getUpgrade() {
        return (upgradeManager, dataHolderSlot, provider) -> {
            ItemContainerContents contents = provider.getOrDefault(ModDataComponents.BACKPACK_CONTAINER, ItemContainerContents.EMPTY);
            NonNullList<ItemStack> items = ContainerContentsHelper.getItems(contents, TBArcaneWorkbenchUpgrade.SIZE);
            return Optional.of(new TBArcaneWorkbenchUpgrade(upgradeManager, dataHolderSlot, items));
        };
    }
}
