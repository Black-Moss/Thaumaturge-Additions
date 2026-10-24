package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.workbench.SlotCrystalEssentia;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeSlotItemHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ArcaneCrystalSlot extends UpgradeSlotItemHandler<TBArcaneWorkbenchUpgrade> {
    private final ResourceKey<IAspect> aspect;

    public ArcaneCrystalSlot(TBArcaneWorkbenchUpgrade upgrade, ItemStackHandler items, int index, int x, int y, ResourceKey<IAspect> aspect) {
        super(upgrade, items, index, x, y);
        this.aspect = Objects.requireNonNull(aspect, "aspect");
    }

    public ResourceKey<IAspect> getAspect() {
        return this.aspect;
    }

    public static boolean isValidCrystal(ItemStack stack, ResourceKey<IAspect> aspect) {
        return !stack.isEmpty() && SlotCrystalEssentia.isValidCrystal(stack, aspect);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return isValidCrystal(stack, this.aspect);
    }
}
