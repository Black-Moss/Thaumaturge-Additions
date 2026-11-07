package com.blackmooss.thaumaturgeadditions.mixin;

import com.leclowndu93150.thaumaturge.content.device.BlockHungryChest;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockHungryChest.class)
public abstract class BlockHungryChestMixin {
    @Inject(method = "chestCanConnectTo", at = @At("HEAD"), cancellable = true)
    private void thaumaturgeadditions$allowConnecting(BlockState blockState, CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(blockState.is((Block) (Object) this));
    }
}
