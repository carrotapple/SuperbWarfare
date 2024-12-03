package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExplosionDestroyConfig {

    public static ForgeConfigSpec.BooleanValue EXPLOSION_DESTROY;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("explosion");

        builder.comment("Set TRUE to allow Explosion to destroy blocks");
        EXPLOSION_DESTROY = builder.define("explosion_destroy", false);

        builder.pop();
    }

}
