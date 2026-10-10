package com.blackmooss.thaumaturgeadditions.focus;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.registry.TATags;
import com.leclowndu93150.thaumaturge.api.casters.CasterTriggerRegistry;
import com.leclowndu93150.thaumaturge.api.casters.ICasterTriggerManager;
import com.leclowndu93150.thaumaturge.api.recipe.DustTrigger;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerInput;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerPlacement;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerSwapQueue;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import com.leclowndu93150.thaumaturge.registry.TTRecipeTypes;
import com.leclowndu93150.thaumaturge.registry.TTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Optional;

@EventBusSubscriber(modid = ThaumaturgeAdditions.MODID)
public final class SalisMundusCasterTrigger implements ICasterTriggerManager {
    private static final int EVENT_SALIS_MUNDUS = 0;
    private static final int SWAP_DELAY_TICKS = 50;
    private static final float SOUND_VOLUME = 0.33F;

    public SalisMundusCasterTrigger() {
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CasterTriggerRegistry.registerCasterBlockTagTrigger(
                new SalisMundusCasterTrigger(), EVENT_SALIS_MUNDUS, TATags.MAGIC_TRIGGER_BLOCKS);
    }

    @Override
    public boolean performTrigger(Level level, ItemStack casterStack, Player player, BlockPos pos, Direction side, int event) {
        BlockState clicked = level.getBlockState(pos);

        ItemStack dust = new ItemStack(TTItems.SALIS_MUNDUS.get());
        DustTriggerInput input = new DustTriggerInput(dust, level, pos, clicked);
        if (!(level instanceof ServerLevel serverLevel)) {
            return true;
        }
        Optional<RecipeHolder<DustTrigger>> match =
                serverLevel.recipeAccess().getRecipeFor(TTRecipeTypes.DUST_TRIGGER.get(), input, serverLevel);
        if (match.isEmpty()) {
            return false;
        }
        RecipeHolder<DustTrigger> holder = match.get();
        DustTrigger trigger = holder.value();
        if (!trigger.doesPassGate(player)) {
            return false;
        }
        ItemStack result = trigger.assemble(input);
        if (result.isEmpty()) {
            return false;
        }

        serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                8, 0.0, 0.05, 0.0, 0.0);

        if (trigger.isMultiblock()) {
            DustTriggerPlacement placement = trigger.findPlacement(input);
            if (placement == null) {
                return false;
            }
            trigger.execute(input, player, placement, side);
        } else if (result.getItem() instanceof BlockItem blockItem) {
            DustTriggerSwapQueue.enqueuePlace(serverLevel, pos, clicked, blockItem.getBlock().defaultBlockState(), SWAP_DELAY_TICKS);
        } else {
            DustTriggerSwapQueue.enqueueDrop(serverLevel, pos, clicked, result, SWAP_DELAY_TICKS);
        }

        serverLevel.playSound(
                null,
                pos,
                TTSounds.DUST.get(),
                SoundSource.PLAYERS,
                SOUND_VOLUME,
                1.0F + (float) serverLevel.getRandom().nextGaussian() * 0.05F);
        return true;
    }
}