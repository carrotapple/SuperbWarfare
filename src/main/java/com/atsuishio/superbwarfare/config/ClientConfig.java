package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.config.client.KillMessageConfig;
import com.atsuishio.superbwarfare.config.client.ReloadConfig;
import com.atsuishio.superbwarfare.config.client.VehicleControlConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        ReloadConfig.init(builder);
        KillMessageConfig.init(builder);
        DisplayConfig.init(builder);
        VehicleControlConfig.init(builder);

        return builder.build();
    }

}
