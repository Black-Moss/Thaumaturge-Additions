package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public final class TravelersBackpackArcaneResultSlot extends Slot {
    private final TravelersBackpackArcaneWorkbenchUpgrade upgrade;
    private final Player player;
    private int amountCrafted;

    public TravelersBackpackArcaneResultSlot(TravelersBackpackArcaneWorkbenchUpgrade upgrade, Player player, Container container, int x, int y) {
        super(container, 0, x, y);
        this.upgrade = Objects.requireNonNull(upgrade, "upgrade");
        this.player = Objects.requireNonNull(player, "player");
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NonNull Player player) {
        return this.upgrade.canCraft();
    }

    @Override
    public @NonNull ItemStack remove(int amount) {
        if (this.upgrade.craftsOnServer()) {
            return super.remove(amount);
        }
        return this.upgrade.takeCraft();
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        this.amountCrafted += numItemsCrafted;
        if (this.upgrade.craftsOnServer()) {
            return;
        }
        ItemStack displayed = getItem().copy();
        if (this.upgrade.takeCraft().isEmpty()) {
            undoSwapTake(displayed);
        }
    }

    @Override
    protected void onQuickCraft(@NonNull ItemStack stack, int amount) {
        this.amountCrafted += amount;
    }

    @Override
    public void onTake(@NonNull Player player, @NonNull ItemStack stack) {
        this.amountCrafted = 0;
        this.upgrade.refreshResult();
    }

    public ItemStack craftForQuickMove(Player player) {
        if (this.upgrade.craftsOnServer()) {
            return ItemStack.EMPTY;
        }
        return this.upgrade.takeCraft();
    }

    public boolean shiftClickToBackpack() {
        return this.upgrade.shiftClickToBackpack(this.upgrade.getDataHolderStack());
    }

    private void undoSwapTake(ItemStack displayed) {
        if (displayed.isEmpty()) {
            return;
        }
        Inventory inventory = this.player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (ItemStack.isSameItemSameComponents(stack, displayed)) {
                stack.shrink(1);
                inventory.setChanged();
                return;
            }
        }
    }
}
