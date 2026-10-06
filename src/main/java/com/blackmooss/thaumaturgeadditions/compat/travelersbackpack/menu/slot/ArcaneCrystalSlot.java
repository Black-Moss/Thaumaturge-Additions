package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.workbench.SlotCrystalEssentia;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeSlotItemHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class ArcaneCrystalSlot extends UpgradeSlotItemHandler<TravelersBackpackArcaneWorkbenchUpgrade> {
    private final ResourceKey<IAspect> aspect;

    public ArcaneCrystalSlot(TravelersBackpackArcaneWorkbenchUpgrade upgrade, ItemStacksResourceHandler items, int index, int x, int y, ResourceKey<IAspect> aspect) {
        super(upgrade, items, index, x, y);
        this.aspect = Objects.requireNonNull(aspect, "aspect");
    }

    public ResourceKey<IAspect> getAspect() {
        return this.aspect;
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        return SlotCrystalEssentia.isValidCrystal(stack, this.aspect);
    }
}
