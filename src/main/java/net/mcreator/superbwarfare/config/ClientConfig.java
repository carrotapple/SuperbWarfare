package net.mcreator.superbwarfare.config;

import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.config.client.ReloadConfig;
import net.mcreator.superbwarfare.config.client.KillMessageConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        ReloadConfig.init(builder);
        KillMessageConfig.init(builder);
        DisplayConfig.init(builder);

        return builder.build();
    }

}
