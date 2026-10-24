package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public final class TBArcaneResultSlot extends Slot {
    private final TBArcaneWorkbenchUpgrade upgrade;

    public TBArcaneResultSlot(TBArcaneWorkbenchUpgrade upgrade, Player player, Container container, int x, int y) {
        super(container, 0, x, y);
        this.upgrade = Objects.requireNonNull(upgrade, "upgrade");
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NonNull Player player) {
        return !this.upgrade.cannotCraft();
    }

    @Override
    public void onTake(@NonNull Player player, @NonNull ItemStack stack) {
        if (!player.level().isClientSide()) {
            this.upgrade.takeCraft();
        }
        this.upgrade.refreshResult();
    }

    public ItemStack craftForQuickMove(Player player) {
        if (player.level().isClientSide()) {
            return ItemStack.EMPTY;
        }
        return this.upgrade.takeCraft();
    }

    public boolean shiftClickToBackpack() {
        return this.upgrade.getDataHolderStack().getOrDefault(ModDataComponents.SHIFT_CLICK_TO_BACKPACK, false);
    }
}
