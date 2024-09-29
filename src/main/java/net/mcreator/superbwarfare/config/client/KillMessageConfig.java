package net.mcreator.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class KillMessageConfig {

    public static ForgeConfigSpec.BooleanValue SHOW_KILL_MESSAGE;
    public static ForgeConfigSpec.IntValue KILL_MESSAGE_COUNT;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("kill_message");

        builder.comment("Set TRUE if you want to show kill message");
        SHOW_KILL_MESSAGE = builder.define("show_kill_message", true);

        builder.comment("The max count of kill messages to show concurrently");
        KILL_MESSAGE_COUNT = builder.defineInRange("kill_message_count", 5, 1, 20);

        builder.pop();
    }

}
