package net.mcreator.superbwarfare.config;

import net.mcreator.superbwarfare.config.client.ReloadConfig;
import net.mcreator.superbwarfare.config.client.KillMessageClientConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        ReloadConfig.init(builder);
        KillMessageClientConfig.init(builder);

        return builder.build();
    }

}
