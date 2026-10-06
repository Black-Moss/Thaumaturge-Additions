package com.blackmooss.thaumaturgeaddon.focus;

import com.blackmooss.thaumaturgeaddon.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TCAspects;
import com.leclowndu93150.thaumaturge.api.casters.CastContext;
import com.leclowndu93150.thaumaturge.api.casters.FocusEffect;
import com.leclowndu93150.thaumaturge.api.casters.FocusSettings;
import com.leclowndu93150.thaumaturge.api.casters.SettingDefinition;
import com.leclowndu93150.thaumaturge.api.casters.Trajectory;
import com.leclowndu93150.thaumaturge.api.recipe.DustTrigger;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerInput;
import com.leclowndu93150.thaumaturge.api.recipe.DustTriggerPlacement;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.focus.FocusFX;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerSwapQueue;
import com.leclowndu93150.thaumaturge.registry.TCItems;
import com.leclowndu93150.thaumaturge.registry.TCRecipeTypes;
import com.leclowndu93150.thaumaturge.registry.TCSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public final class SalisMundusFocusEffect implements FocusEffect {
    public static final Identifier KEY = ThaumaturgeAdditions.identifier("salis_mundus");

    private static final int SWAP_DELAY_TICKS = 50;
    private static final int POWER_COMPLEXITY_FACTOR = 3;
    private static final float SOUND_VOLUME = 0.33F;

    @Override
    public Identifier id() {
        return KEY;
    }

    @Override
    public @Nullable ResearchGate research() {
        return null;
    }

    @Override
    public ResourceKey<IAspect> aspect() {
        return TCAspects.PRAECANTATIO;
    }

    @Override
    public int complexity(FocusSettings settings) {
        return settings.value("power") * POWER_COMPLEXITY_FACTOR;
    }

    @Override
    public boolean apply(
            CastContext ctx,
            FocusSettings settings,
            HitResult target,
            @Nullable Trajectory trajectory,
            int index) {
        if (!(ctx.level() instanceof ServerLevel level)) {
            return false;
        }
        if (!(target instanceof BlockHitResult blockHit)) {
            return false;
        }
        if (!(ctx.caster() instanceof Player player)) {
            return false;
        }
        BlockPos pos = blockHit.getBlockPos();
        BlockState clicked = level.getBlockState(pos);
        ItemStack dust = new ItemStack(TCItems.SALIS_MUNDUS.get());
        DustTriggerInput input = new DustTriggerInput(dust, level, pos, clicked);
        Optional<RecipeHolder<DustTrigger>> match = level.recipeAccess().getRecipeFor(TCRecipeTypes.DUST_TRIGGER.get(), input, level);
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
        FocusFX.impact(level, Vec3.atCenterOf(pos), id());
        if (trigger.isMultiblock()) {
            DustTriggerPlacement placement = trigger.findPlacement(input);
            if (placement == null) {
                return false;
            }
            trigger.execute(input, player, placement, blockHit.getDirection());
        } else if (result.getItem() instanceof BlockItem blockItem) {
            DustTriggerSwapQueue.enqueuePlace(level, pos, clicked, blockItem.getBlock().defaultBlockState(), SWAP_DELAY_TICKS);
        } else {
            DustTriggerSwapQueue.enqueueDrop(level, pos, clicked, result, SWAP_DELAY_TICKS);
        }
        level.playSound(
                null,
                pos,
                TCSounds.DUST.get(),
                SoundSource.PLAYERS,
                SOUND_VOLUME,
                1.0F + (float) level.getRandom().nextGaussian() * 0.05F);
        return true;
    }

    @Override
    public List<SettingDefinition> settings() {
        return List.of(new SettingDefinition("power", "focus.common.power", new SettingDefinition.IntRange(1, 5)));
    }

    @Override
    public void onCast(LivingEntity caster) {
        caster.level().playSound(
                null,
                caster.blockPosition().above(),
                TCSounds.DUST.get(),
                SoundSource.PLAYERS,
                SOUND_VOLUME,
                1.0F + (float) caster.level().getRandom().nextGaussian() * 0.05F);
    }

    @Override
    public void impactParticles(Level level, Vec3 pos, Vec3 motion, Vec3 drift) {
        level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, motion.x, motion.y + 0.05F, motion.z);
    }
}
