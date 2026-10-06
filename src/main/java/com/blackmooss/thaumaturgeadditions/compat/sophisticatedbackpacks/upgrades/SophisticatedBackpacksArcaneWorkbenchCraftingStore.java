package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.upgrades;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneCraftingStore;
import com.leclowndu93150.thaumaturge.content.taint.item.ItemEssentiaCrystal;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.RootCommitJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.p3pp3rf1y.sophisticatedcore.inventory.ComponentItemStacksHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SophisticatedBackpacksArcaneWorkbenchCraftingStore implements IArcaneCraftingStore {
    private final ComponentItemStacksHandler items;
    private final Player player;
    private final int left;
    private final int top;
    private final int width;
    private final int height;

    public SophisticatedBackpacksArcaneWorkbenchCraftingStore(ComponentItemStacksHandler items, Player player, int left, int top, int width, int height) {
        this.items = Objects.requireNonNull(items, "items");
        this.player = Objects.requireNonNull(player, "player");
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean consume(Consumption consumption, TransactionContext transaction) {
        Objects.requireNonNull(consumption, "consumption");
        Objects.requireNonNull(transaction, "transaction");
        if (!gridMatches(consumption.grid())) {
            return false;
        }
        List<ItemStack> overflow = new ArrayList<>();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                consumeGridSlot(x, y, consumption, overflow, transaction);
            }
        }
        if (!consumeCrystals(consumption.crystals(), transaction)) {
            return false;
        }
        replaceWand(consumption.wand(), transaction);
        if (!overflow.isEmpty()) {
            new RootCommitJournal(() -> overflow.forEach(stack -> this.player.getInventory().placeItemBackInInventory(stack))).updateSnapshots(transaction);
        }
        return true;
    }

    private boolean gridMatches(List<ItemStack> grid) {
        if (grid.size() != this.width * this.height) {
            return false;
        }
        for (int slot = 0; slot < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_SLOTS; slot++) {
            int x = slot % SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_WIDTH - this.left;
            int y = slot / SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_WIDTH - this.top;
            boolean inside = x >= 0 && x < this.width && y >= 0 && y < this.height;
            ItemStack expected = inside ? grid.get(x + y * this.width) : ItemStack.EMPTY;
            if (!ItemStack.matches(getStack(slot), expected)) {
                return false;
            }
        }
        return true;
    }

    private void consumeGridSlot(int x, int y, Consumption consumption, List<ItemStack> overflow, TransactionContext transaction) {
        int compact = x + y * this.width;
        int slot = x + this.left + (y + this.top) * SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.GRID_WIDTH;
        ItemStack current = getStack(slot);
        if (current.isEmpty()) {
            return;
        }
        if (this.items.extract(slot, ItemResource.of(current), 1, transaction) <= 0) {
            return;
        }
        ItemStack remainder = compact < consumption.remainders().size() ? consumption.remainders().get(compact) : ItemStack.EMPTY;
        if (remainder.isEmpty()) {
            return;
        }
        ItemStack leftOver = getStack(slot);
        if (leftOver.isEmpty() || ItemStack.isSameItemSameComponents(leftOver, remainder)) {
            this.items.insert(slot, ItemResource.of(remainder), remainder.getCount(), transaction);
        } else {
            overflow.add(remainder.copy());
        }
    }

    private boolean consumeCrystals(AspectList crystals, TransactionContext transaction) {
        for (AspectInstance entry : crystals.entries()) {
            int needed = entry.amount();
            for (int index = 0; index < SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.CRYSTAL_SLOTS && needed > 0; index++) {
                int slot = SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.CRYSTAL_START + index;
                ItemStack crystal = getStack(slot);
                if (crystal.isEmpty() || !(crystal.getItem() instanceof ItemEssentiaCrystal)) {
                    continue;
                }
                Holder<IAspect> aspect = ItemEssentiaCrystal.aspectOf(crystal);
                if (aspect == null || !aspect.value().tag().equals(entry.aspect().value().tag())) {
                    continue;
                }
                int removed = Math.min(needed, crystal.getCount());
                needed -= this.items.extract(slot, ItemResource.of(crystal), removed, transaction);
            }
            if (needed > 0) {
                return false;
            }
        }
        return true;
    }

    private void replaceWand(ItemStack wand, TransactionContext transaction) {
        int slot = SophisticatedBackpacksArcaneWorkbenchUpgradeWrapper.WAND_SLOT;
        ItemStack current = getStack(slot);
        if (ItemStack.matches(current, wand)) {
            return;
        }
        if (!current.isEmpty()) {
            this.items.extract(slot, ItemResource.of(current), current.getCount(), transaction);
        }
        if (!wand.isEmpty()) {
            this.items.insert(slot, ItemResource.of(wand), wand.getCount(), transaction);
        }
    }

    private ItemStack getStack(int slot) {
        return this.items.getStackInSlot(slot);
    }
}
