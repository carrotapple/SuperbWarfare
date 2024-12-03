package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExplosionConfig {

    public static ForgeConfigSpec.IntValue EXPLOSION_PENETRATION_RATIO;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("explosion_penetration");

        builder.comment("The percentage of explosion damage you take behind cover");
        EXPLOSION_PENETRATION_RATIO = builder.defineInRange("explosion_destroy", 15, 0, 100);

        builder.pop();
    }

}
