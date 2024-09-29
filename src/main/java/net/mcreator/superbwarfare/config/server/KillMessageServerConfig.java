package net.mcreator.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class KillMessageServerConfig {

    public static ForgeConfigSpec.BooleanValue SEND_KILL_MESSAGE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("kill_message");

        SEND_KILL_MESSAGE = builder.define("send_kill_message", false);

        builder.pop();
    }

}
