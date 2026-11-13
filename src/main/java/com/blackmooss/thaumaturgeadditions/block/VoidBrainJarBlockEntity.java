package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.leclowndu93150.thaumaturge.content.blockentity.AbstractSyncedBlockEntity;
import com.leclowndu93150.thaumaturge.mixin.world.entity.ExperienceOrbAccessor;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class VoidBrainJarBlockEntity extends AbstractSyncedBlockEntity {
    public static final int XP_MAX = 2000;
    private static final String XP_KEY = "XP";
    private static final double PULL_RANGE = 8.0;
    private static final double PULL_FALLOFF_RANGE = 25.0;
    private static final double PULL_HORIZONTAL_STRENGTH = 0.3;
    private static final double PULL_VERTICAL_STRENGTH = 0.5;
    private static final double COLLECT_MARGIN = 0.1;
    private static final float EAT_VOLUME = 0.1F;
    private static final float EAT_BASE_PITCH = 1.0F;
    private static final float EAT_PITCH_SPREAD = 0.2F;
    private static final double PLAYER_RANGE = 6.0;
    private static final long SIGH_FIRST_DELAY = 30L;
    private static final long SIGH_MIN_DELAY = 100L;
    private static final int SIGH_DELAY_SPREAD = 500;
    private static final long UNSCHEDULED = -1L;
    private static final float SIGH_VOLUME = 0.15F;
    private static final float SIGH_BASE_PITCH = 0.8F;
    private static final float SIGH_PITCH_SPREAD = 0.4F;
    private static final float IDLE_SPIN = 0.01F;
    private static final float TURN_RATE = 0.04F;
    private static final float TWO_PI = (float) Math.PI * 2.0F;

    public float rota;
    public float rotb;
    private int xp;
    private int eatDelay;
    private float targetYaw;
    private long nextSigh = UNSCHEDULED;

    public VoidBrainJarBlockEntity(BlockPos pos, BlockState state) {
        super(TABlockEntities.VOID_BRAIN_JAR.get(), pos, state);
    }

    public int xp() {
        return xp;
    }

    public void setXp(int newXp) {
        xp = Mth.clamp(newXp, 0, XP_MAX);
        setChangedAndSync();
    }

    public void setEatDelay(int ticks) {
        eatDelay = ticks;
    }

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            VoidBrainJarBlockEntity brain) {
        if (brain.xp > XP_MAX) {
            brain.xp = XP_MAX;
        }
        if (brain.eatDelay > 0) {
            brain.eatDelay--;
        } else if (brain.xp < XP_MAX) {
            brain.attractOrbs(level);
            brain.collectOrbs(level);
        }
    }

    public static void clientTick(
            Level level,
            BlockPos pos,
            BlockState state,
            VoidBrainJarBlockEntity brain) {
        brain.rotb = brain.rota;
        if (brain.eatDelay > 0) {
            brain.eatDelay--;
        }
        ExperienceOrb orb = brain.xp < XP_MAX && brain.eatDelay <= 0 ? brain.attractOrbs(level) : null;
        Vec3 center = Vec3.atCenterOf(pos);
        Player player = orb == null
                ? level.getNearestPlayer(center.x, center.y, center.z, PLAYER_RANGE, EntitySelector.NO_SPECTATORS)
                : null;
        if (player != null) {
            brain.sigh(level, center);
        }
        Vec3 focus = orb != null ? orb.position() : (player != null ? player.position() : null);
        if (focus == null) {
            brain.targetYaw += IDLE_SPIN;
        } else {
            brain.targetYaw = (float) Mth.atan2(focus.z - center.z, focus.x - center.x);
        }
        float difference = brain.targetYaw - brain.rota;
        difference -= TWO_PI * (float) Math.floor((difference + Math.PI) / TWO_PI);
        brain.rota += difference * TURN_RATE;
    }

    private @Nullable ExperienceOrb attractOrbs(Level level) {
        Vec3 center = Vec3.atCenterOf(getBlockPos());
        ExperienceOrb nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class,
                new AABB(getBlockPos()).inflate(PULL_RANGE))) {
            double distance = center.distanceTo(orb.position());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = orb;
            }
        }
        if (nearest != null && nearestDistance > 0.0) {
            Vec3 direction = center.subtract(nearest.position()).scale(1.0 / nearestDistance);
            double falloff = 1.0 - nearestDistance / PULL_FALLOFF_RANGE;
            double scale = falloff * falloff;
            nearest.setDeltaMovement(nearest.getDeltaMovement().add(
                    direction.x * PULL_HORIZONTAL_STRENGTH * scale,
                    direction.y * PULL_VERTICAL_STRENGTH * scale,
                    direction.z * PULL_HORIZONTAL_STRENGTH * scale));
        }
        return nearest;
    }

    private void collectOrbs(Level level) {
        boolean collected = false;
        RandomSource random = level.getRandom();
        for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class,
                new AABB(getBlockPos()).inflate(COLLECT_MARGIN))) {
            xp += orb.getValue() * ((ExperienceOrbAccessor) orb).thaumaturge$getCount();
            orb.playSound(SoundEvents.GENERIC_EAT, EAT_VOLUME,
                    EAT_BASE_PITCH + (random.nextFloat() - random.nextFloat()) * EAT_PITCH_SPREAD);
            orb.discard();
            collected = true;
        }
        if (collected) {
            setChangedAndSync();
        }
    }

    private void sigh(Level level, Vec3 center) {
        long now = level.getGameTime();
        if (nextSigh == UNSCHEDULED) {
            nextSigh = now + SIGH_FIRST_DELAY;
        } else if (now >= nextSigh) {
            RandomSource random = level.getRandom();
            level.playLocalSound(center.x, center.y, center.z, TTSounds.BRAIN.get(),
                    SoundSource.AMBIENT, SIGH_VOLUME,
                    SIGH_BASE_PITCH + random.nextFloat() * SIGH_PITCH_SPREAD, false);
            nextSigh = now + SIGH_MIN_DELAY + random.nextInt(SIGH_DELAY_SPREAD);
        }
    }

    @Override
    protected void loadAdditional(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        super.loadAdditional(tag, registries);
        xp = tag.getInt(XP_KEY);
    }

    @Override
    protected void saveAdditional(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(XP_KEY, xp);
    }

    @Override
    protected void writeClientData(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        tag.putInt(XP_KEY, xp);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        if (xp > 0) {
            builder.set(TTDataComponents.STORED_XP.get(), xp);
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NonNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        Integer stored = input.get(TTDataComponents.STORED_XP.get());
        if (stored != null) {
            setXp(stored);
        }
    }
}
