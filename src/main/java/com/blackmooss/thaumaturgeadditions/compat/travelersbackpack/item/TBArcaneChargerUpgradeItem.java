package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneChargerUpgrade;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.items.TravelersBackpackItem;
import com.tiviacz.travelersbackpack.items.upgrades.UpgradeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TBArcaneChargerUpgradeItem extends UpgradeItem {
    public TBArcaneChargerUpgradeItem(Properties properties) {
        super(properties, "arcane_charger_upgrade");
    }

    public static boolean hasCharger(Player player) {
        ItemStack worn = AttachmentUtils.isWearingBackpack(player) ? AttachmentUtils.getWearingBackpack(player) : null;
        if (hasChargerIn(worn)) {
            return true;
        }
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (hasChargerIn(player.getInventory().getItem(slot))) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasChargerIn(@Nullable ItemStack backpack) {
        if (backpack == null || backpack.isEmpty() || !(backpack.getItem() instanceof TravelersBackpackItem)) {
            return false;
        }
        return BackpackWrapper.fromStack(backpack).getUpgradeManager()
                .getUpgrade(TBArcaneChargerUpgrade.class)
                .filter(charger -> charger.isEnabled(charger))
                .isPresent();
    }

    @Override
    public Class<? extends UpgradeBase<?>> getUpgradeClass() {
        return TBArcaneChargerUpgrade.class;
    }

    @Override
    public boolean requiresEquippedBackpack() {
        return false;
    }

    @Override
    public TriFunction<UpgradeManager, Integer, ItemStack, Optional<? extends UpgradeBase<?>>> getUpgrade() {
        return (upgradeManager, dataHolderSlot, provider) -> Optional.of(new TBArcaneChargerUpgrade(upgradeManager, dataHolderSlot));
    }
}
