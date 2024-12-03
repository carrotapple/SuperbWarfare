package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.config.server.SpawnConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SpawnConfig.init(builder);
        ExplosionDestroyConfig.init(builder);
        ExplosionConfig.init(builder);
        CannonConfig.init(builder);

        return builder.build();
    }

}
