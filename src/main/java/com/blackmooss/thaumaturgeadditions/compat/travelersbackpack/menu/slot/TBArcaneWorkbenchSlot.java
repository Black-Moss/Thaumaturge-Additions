package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.content.workbench.SlotWorkbenchWand;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.Objects;
import java.util.function.Predicate;

public class TBArcaneWorkbenchSlot extends UpgradeSlotItemHandler<TBArcaneWorkbenchUpgrade> {
    private final Predicate<ItemStack> filter;

    public TBArcaneWorkbenchSlot(TBArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y, Predicate<ItemStack> filter) {
        super(upgrade, items, index, x, y);
        this.filter = Objects.requireNonNull(filter, "filter");
    }

    public static TBArcaneWorkbenchSlot grid(TBArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y) {
        return new TBArcaneWorkbenchSlot(upgrade, items, index, x, y, stack -> true);
    }

    public static TBArcaneWorkbenchSlot wand(TBArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y) {
        return new TBArcaneWorkbenchSlot(upgrade, items, index, x, y, SlotWorkbenchWand::isUsableWand);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && this.filter.test(stack);
    }
}
