package com.blackmooss.thaumaturgeadditions.mixin;

import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.TravelersBackpackCompat;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.menu.slot.TravelersBackpackArcaneResultSlot;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow
    protected abstract boolean moveItemStackTo(
            ItemStack itemStack,
            int startSlot,
            int endSlot,
            boolean backwards);

    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void thaumaturgeadditions$quickMoveArcaneResult(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo callback) {
        if (containerInput != ContainerInput.QUICK_MOVE || !TravelersBackpackCompat.isLoaded()) {
            return;
        }
        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
        if (slotIndex < 0 || slotIndex >= menu.slots.size()) {
            return;
        }
        if (!(menu.slots.get(slotIndex) instanceof TravelersBackpackArcaneResultSlot resultSlot) || !(menu instanceof BackpackBaseMenu backpackMenu)) {
            return;
        }
        if (!resultSlot.mayPickup(player)) {
            callback.cancel();
            return;
        }
        boolean toBackpack = resultSlot.shiftClickToBackpack();
        while (true) {
            ItemStack crafted = resultSlot.craftForQuickMove(player);
            if (crafted.isEmpty()) {
                break;
            }
            ItemStack remaining = crafted.copy();
            boolean moved = toBackpack
                    ? this.moveItemStackTo(remaining, backpackMenu.BACKPACK_INV_START, backpackMenu.BACKPACK_INV_END, false)
                    : this.moveItemStackTo(remaining, backpackMenu.PLAYER_INV_START, backpackMenu.PLAYER_HOT_END, true);
            if (!moved) {
                player.getInventory().placeItemBackInInventory(remaining);
                break;
            }
            if (!remaining.isEmpty()) {
                player.getInventory().placeItemBackInInventory(remaining);
            }
        }
        callback.cancel();
    }
}
