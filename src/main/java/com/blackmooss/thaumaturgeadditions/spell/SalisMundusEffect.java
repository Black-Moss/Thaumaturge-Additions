package com.blackmooss.thaumaturgeadditions.spell;

import com.leclowndu93150.thaumaturge.api.recipe.DustTrigger;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerInput;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerPlacement;
import com.leclowndu93150.thaumaturge.api.spell.behavior.AbstractEffectBehavior;
import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerSwapQueue;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import com.leclowndu93150.thaumaturge.registry.TTRecipeTypes;
import com.blackmooss.thaumaturgeadditions.registry.TASpellBehaviors;
import com.leclowndu93150.thaumaturge.registry.TTSounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public final class SalisMundusEffect extends AbstractEffectBehavior {
    private static final float SOUND_VOLUME = 0.33F;

    public static final MapCodec<SalisMundusEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.INT.optionalFieldOf("swap_delay", 50).forGetter(SalisMundusEffect::swapDelay))
            .apply(i, SalisMundusEffect::new));

    private final int swapDelay;

    public SalisMundusEffect(int swapDelay) {
        this.swapDelay = swapDelay;
    }

    public int swapDelay() {
        return this.swapDelay;
    }

    @Override
    public SpellBehaviorType<?> type() {
        return TASpellBehaviors.SALIS_MUNDUS.get();
    }

    @Override
    protected boolean widens() {
        return false;
    }

    @Override
    protected void apply(CastContext ctx, SpellTarget target, float power, int index) {
        if (target.block().isEmpty() || !(ctx.caster() instanceof Player player)) {
            return;
        }
        ServerLevel level = ctx.level();
        BlockPos pos = target.blockPos();
        BlockState clicked = level.getBlockState(pos);

        ItemStack dust = new ItemStack(TTItems.SALIS_MUNDUS.get());
        DustTriggerInput input = new DustTriggerInput(dust, level, pos, clicked);
        Optional<RecipeHolder<DustTrigger>> match = level.recipeAccess().getRecipeFor(TTRecipeTypes.DUST_TRIGGER.get(), input, level);
        if (match.isEmpty()) {
            return;
        }
        DustTrigger trigger = match.get().value();
        if (!trigger.doesPassGate(player)) {
            return;
        }
        ItemStack result = trigger.assemble(input);
        if (result.isEmpty()) {
            return;
        }

        ctx.fx().impact(ctx.part().fx(), Vec3.atCenterOf(pos), ctx.color());

        if (trigger.isMultiblock()) {
            DustTriggerPlacement placement = trigger.findPlacement(input);
            if (placement == null) {
                return;
            }
            trigger.execute(input, player, placement, target.block().get().getDirection());
        } else if (result.getItem() instanceof BlockItem blockItem) {
            DustTriggerSwapQueue.enqueuePlace(level, pos, clicked, blockItem.getBlock().defaultBlockState(), this.swapDelay);
        } else {
            DustTriggerSwapQueue.enqueueDrop(level, pos, clicked, result, this.swapDelay);
        }

        level.playSound(
                null, pos,
                TTSounds.DUST.get(),
                SoundSource.PLAYERS,
                SOUND_VOLUME,
                1.0F + (float) level.getRandom().nextGaussian() * 0.05F);
    }
}