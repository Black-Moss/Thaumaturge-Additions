package com.blackmooss.thaumaturgeadditions.recipe;

import com.blackmooss.thaumaturgeadditions.registry.TAItems;
import com.blackmooss.thaumaturgeadditions.registry.TARecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class VoidTravellerBootsDyeRecipe extends CustomRecipe {
    public static final VoidTravellerBootsDyeRecipe INSTANCE = new VoidTravellerBootsDyeRecipe();
    public static final MapCodec<VoidTravellerBootsDyeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, VoidTravellerBootsDyeRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private VoidTravellerBootsDyeRecipe() {
        super(CraftingBookCategory.EQUIPMENT);
    }

    @Override
    public boolean matches(CraftingInput input, @NonNull Level level) {
        ItemStack boots = ItemStack.EMPTY;
        int dyes = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(TAItems.VOID_TRAVELLER_BOOTS.get())) {
                if (!boots.isEmpty()) {
                    return false;
                }
                boots = stack;
            } else if (stack.getItem() instanceof DyeItem) {
                dyes++;
            } else {
                return false;
            }
        }
        return !boots.isEmpty() && dyes > 0;
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput input, HolderLookup.@NonNull Provider registries) {
        ItemStack boots = ItemStack.EMPTY;
        int r = 0;
        int g = 0;
        int b = 0;
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(TAItems.VOID_TRAVELLER_BOOTS.get())) {
                boots = stack;
            } else if (stack.getItem() instanceof DyeItem dye) {
                int color = dye.getDyeColor().getTextureDiffuseColor();
                r += (color >> 16) & 0xFF;
                g += (color >> 8) & 0xFF;
                b += color & 0xFF;
                count++;
            }
        }
        if (boots.isEmpty() || count == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack result = boots.copy();
        DyedItemColor existing = result.get(DataComponents.DYED_COLOR);
        if (existing != null) {
            int color = existing.rgb();
            r += (color >> 16) & 0xFF;
            g += (color >> 8) & 0xFF;
            b += color & 0xFF;
            count++;
        }
        int rgb = ((r / count) << 16) | ((g / count) << 8) | (b / count);
        result.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb, true));
        return result;
    }

    @Override
    public @NonNull ItemStack getResultItem(HolderLookup.@NonNull Provider registries) {
        ItemStack stack = new ItemStack(TAItems.VOID_TRAVELLER_BOOTS.get());
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(0xFF6A3880, true));
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NonNull RecipeSerializer<VoidTravellerBootsDyeRecipe> getSerializer() {
        return TARecipes.VOID_TRAVELLER_BOOTS_DYE.get();
    }
}
