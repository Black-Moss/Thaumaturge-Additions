package com.blackmooss.thaumaturgeadditions.mixin;

import com.leclowndu93150.thaumaturge.content.device.BlockHungryChest;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMenuProviderMixin {
    @Inject(method = "getMenuProvider", at = @At("RETURN"), cancellable = true)
    private void thaumaturgeadditions$nameLargeHungryChest(
            BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> callback) {
        MenuProvider provider = callback.getReturnValue();
        if (provider == null || level == null || state == null
                || !(state.getBlock() instanceof BlockHungryChest)
                || state.getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
            return;
        }
        if (thaumaturgeadditions$hasCustomName(level, pos)
                || thaumaturgeadditions$hasCustomName(level, pos.relative(ChestBlock.getConnectedDirection(state)))) {
            return;
        }
        callback.setReturnValue(new MenuProvider() {
            @Override
            public @NonNull Component getDisplayName() {
                return Component.translatable("gui.thaumaturgeadditions.large_hungry_chest.title");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(
                    int containerId,
                    @NonNull Inventory inventory,
                    @NonNull Player player) {
                return provider.createMenu(containerId, inventory, player);
            }
        });
    }

    @Unique
    private static boolean thaumaturgeadditions$hasCustomName(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof BaseContainerBlockEntity container
                && container.getCustomName() != null;
    }
}
