package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneChargerUpgrade;
import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.item.TravelersBackpackItem;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class TravelersBackpackArcaneChargerUpgradeItem extends UpgradeItem {
    public TravelersBackpackArcaneChargerUpgradeItem(Properties properties) {
        super(properties, null);
    }

    @Override
    public Class<? extends UpgradeBase<?>> getUpgradeClass() {
        return TravelersBackpackArcaneChargerUpgrade.class;
    }

    @Override
    public boolean requiresEquippedBackpack() {
        return false;
    }

    @Override
    public TriFunction<UpgradeManager, Integer, ItemStack, Optional<? extends UpgradeBase<?>>> getUpgrade() {
        return (upgradeManager, dataHolderSlot, provider) -> Optional.of(new TravelersBackpackArcaneChargerUpgrade(upgradeManager, dataHolderSlot));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
        tooltip.accept(Component.translatable("item.thaumaturgeadditions.travelersbackpack_arcane_charger_upgrade.tooltip").withStyle(ChatFormatting.BLUE));
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
                .getUpgrade(TravelersBackpackArcaneChargerUpgrade.class)
                .filter(charger -> charger.isEnabled(charger))
                .isPresent();
    }
}
