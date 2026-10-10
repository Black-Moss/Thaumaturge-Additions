package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.spell.SalisMundusEffect;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.part.AspectInput;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPartKind;
import com.leclowndu93150.thaumaturge.registry.TTSounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public final class TASpellBehaviors {
    public static final String SALIS_MUNDUS_PATH = "salis_mundus";

    public static final DeferredRegister<SpellBehaviorType<?>> BEHAVIORS =
            DeferredRegister.create(SpellBehaviorType.REGISTRY_KEY, ThaumaturgeAdditions.MODID);

    public static final DeferredHolder<SpellBehaviorType<?>, SpellBehaviorType<SalisMundusEffect>> SALIS_MUNDUS =
            BEHAVIORS.register(SALIS_MUNDUS_PATH,
                    () -> new SpellBehaviorType<>(SpellPartKind.EFFECT, SalisMundusEffect.CODEC));

    private TASpellBehaviors() {
    }

    public static void bootstrapParts(BootstrapContext<SpellPart> ctx) {
        ctx.register(TASpellParts.SALIS_MUNDUS, new SpellPart(
                new SalisMundusEffect(50),
                ThaumaturgeAdditions.identifier("textures/foci/" + SALIS_MUNDUS_PATH + ".png"),
                0x4A6E8A,
                3,
                0.0F,
                1.0F,
                new AspectInput.Fixed(TTAspects.PRAECANTATIO),
                List.of(),
                Optional.of(new ResearchGate(ThaumaturgeAdditions.identifier("focus_salis_mundus"), Optional.empty(), false)),
                1,
                false,
                Optional.of(TTSounds.DUST),
                1.0F,
                1.0F,
                TTIds.rl("sparkle")));
    }
}