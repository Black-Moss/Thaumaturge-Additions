package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.content.workbench.SlotWorkbenchWand;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.Objects;
import java.util.function.Predicate;

public class TravelersBackpackArcaneWorkbenchSlot extends UpgradeSlotItemHandler<TravelersBackpackArcaneWorkbenchUpgrade> {
    private final Predicate<ItemStack> filter;

    public TravelersBackpackArcaneWorkbenchSlot(TravelersBackpackArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y, Predicate<ItemStack> filter) {
        super(upgrade, items, index, x, y);
        this.filter = Objects.requireNonNull(filter, "filter");
    }

    public static TravelersBackpackArcaneWorkbenchSlot grid(TravelersBackpackArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y) {
        return new TravelersBackpackArcaneWorkbenchSlot(upgrade, items, index, x, y, stack -> true);
    }

    public static TravelersBackpackArcaneWorkbenchSlot wand(TravelersBackpackArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y) {
        return new TravelersBackpackArcaneWorkbenchSlot(upgrade, items, index, x, y, SlotWorkbenchWand::isUsableWand);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && this.filter.test(stack);
    }
}
