package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import com.tiviacz.travelersbackpack.util.ContainerContentsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;
import java.util.function.Consumer;

public class TravelersBackpackArcaneWorkbenchUpgradeItem extends UpgradeItem {
    public TravelersBackpackArcaneWorkbenchUpgradeItem(Properties properties) {
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
        return TravelersBackpackArcaneWorkbenchUpgrade.class;
    }

    @Override
    public TriFunction<UpgradeManager, Integer, ItemStack, Optional<? extends UpgradeBase<?>>> getUpgrade() {
        return (upgradeManager, dataHolderSlot, provider) -> {
            ItemContainerContents contents = provider.getOrDefault(ModDataComponents.BACKPACK_CONTAINER, ItemContainerContents.EMPTY);
            NonNullList<ItemStack> items = ContainerContentsHelper.getItems(contents, TravelersBackpackArcaneWorkbenchUpgrade.SIZE);
            return Optional.of(new TravelersBackpackArcaneWorkbenchUpgrade(upgradeManager, dataHolderSlot, items));
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
        tooltip.accept(Component.translatable("item.thaumaturgeadditions.travelers_backpack_arcane_workbench_upgrade.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
