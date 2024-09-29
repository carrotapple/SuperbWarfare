package net.mcreator.superbwarfare.config;

import net.mcreator.superbwarfare.config.client.EmptyAutoReloadConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        EmptyAutoReloadConfig.init(builder);

        return builder.build();
    }

}
