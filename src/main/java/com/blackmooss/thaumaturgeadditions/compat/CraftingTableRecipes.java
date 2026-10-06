package com.blackmooss.thaumaturgeadditions.compat;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneCraftingStore;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class CraftingTableRecipes {
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_SLOTS = GRID_WIDTH * GRID_HEIGHT;

    private CraftingTableRecipes() {
    }

    public static CraftingInput toInput(List<ItemStack> slots) {
        return CraftingInput.of(GRID_WIDTH, GRID_HEIGHT, List.copyOf(slots.subList(0, GRID_SLOTS)));
    }

    public static @Nullable VanillaCraft plan(Level level, List<ItemStack> slots) {
        CraftingInput input = toInput(slots);
        RecipeHolder<CraftingRecipe> recipe = findRecipe(level, input);
        if (recipe == null) {
            return null;
        }
        try {
            ItemStack output = recipe.value().assemble(input);
            if (output.isEmpty()) {
                return null;
            }
            return new VanillaCraft(output, recipe.value().getRemainingItems(input));
        } catch (Exception exception) {
            ThaumaturgeAdditions.LOGGER.error("Backpack arcane workbench: crafting table recipe {} failed to assemble", recipe.id(), exception);
            return null;
        }
    }

    private static @Nullable RecipeHolder<CraftingRecipe> findRecipe(Level level, CraftingInput input) {
        if (!(level instanceof ServerLevel serverLevel) || input.isEmpty()) {
            return null;
        }
        try {
            return serverLevel.recipeAccess().recipeMap().getRecipesFor(RecipeType.CRAFTING, input, level).findFirst().orElse(null);
        } catch (Exception exception) {
            ThaumaturgeAdditions.LOGGER.error("Backpack arcane workbench: failed to look up crafting table recipe", exception);
            return null;
        }
    }

    public static boolean consume(IArcaneCraftingStore store, VanillaCraft craft, List<ItemStack> slots, ItemStack wand, TransactionContext transaction) {
        return store.consume(new IArcaneCraftingStore.Consumption(slots.subList(0, GRID_SLOTS), craft.remainders(), AspectList.EMPTY, wand), transaction);
    }

    public record VanillaCraft(ItemStack output, List<ItemStack> remainders) {
        public VanillaCraft {
            output = output.copy();
            remainders = remainders.stream().map(ItemStack::copy).toList();
        }
    }
}
