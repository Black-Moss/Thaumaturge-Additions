package com.blackmooss.thaumaturgeadditions.registry;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;

import net.minecraft.resources.ResourceKey;

public final class TASpellParts {
    public static final ResourceKey<SpellPart> SALIS_MUNDUS =
            ResourceKey.create(SpellPart.REGISTRY_KEY, ThaumaturgeAdditions.identifier("salis_mundus"));

    private TASpellParts() {
    }
}