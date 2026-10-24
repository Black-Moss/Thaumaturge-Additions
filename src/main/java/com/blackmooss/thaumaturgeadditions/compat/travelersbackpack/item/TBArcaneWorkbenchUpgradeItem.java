package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.components.BackpackContainerContents;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.items.upgrades.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;

public class TBArcaneWorkbenchUpgradeItem extends UpgradeItem {
    public TBArcaneWorkbenchUpgradeItem(Properties properties) {
        super(properties, "arcane_workbench_upgrade");
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
            BackpackContainerContents contents = provider.getOrDefault(
                    ModDataComponents.BACKPACK_CONTAINER, new BackpackContainerContents(TBArcaneWorkbenchUpgrade.SIZE));
            return Optional.of(new TBArcaneWorkbenchUpgrade(upgradeManager, dataHolderSlot, contents.getItems()));
        };
    }
}
