package net.mcreator.superbwarfare.config;

import net.mcreator.superbwarfare.config.server.SpawnConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SpawnConfig.init(builder);

        return builder.build();
    }

}
