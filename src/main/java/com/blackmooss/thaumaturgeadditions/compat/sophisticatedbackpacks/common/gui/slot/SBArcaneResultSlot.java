package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.slot;

import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SBArcaneWorkbenchUpgradeContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public final class SBArcaneResultSlot extends Slot {
    private final SBArcaneWorkbenchUpgradeContainer container;

    public SBArcaneResultSlot(Container resultContainer, SBArcaneWorkbenchUpgradeContainer container, int x, int y) {
        super(Objects.requireNonNull(resultContainer, "resultContainer"), 0, x, y);
        this.container = Objects.requireNonNull(container, "container");
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NonNull Player player) {
        this.container.refreshResult();
        return !this.getItem().isEmpty();
    }

    @Override
    public void onTake(@NonNull Player player, @NonNull ItemStack stack) {
        super.onTake(player, stack);
        this.container.onResultTaken(player, stack);
    }
}
