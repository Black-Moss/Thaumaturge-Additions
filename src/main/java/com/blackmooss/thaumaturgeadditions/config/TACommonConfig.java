package com.blackmooss.thaumaturgeadditions.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TACommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue EVERBURNING_URN_AUTO_FILL = BUILDER
            .define("everburning_urn_auto_fill", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
