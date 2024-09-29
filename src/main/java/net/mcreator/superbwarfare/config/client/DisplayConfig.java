package net.mcreator.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class DisplayConfig {

    public static ForgeConfigSpec.BooleanValue KILL_INDICATION;
    public static ForgeConfigSpec.BooleanValue GLOBAL_INDICATION;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("display");

        builder.comment("Set TRUE if you want to show kill indication while killing an entity");
        KILL_INDICATION = builder.define("kill_indication", true);

        builder.comment("Set FALSE if you want to show kill indication ONLY while killing an entity with a gun");
        GLOBAL_INDICATION = builder.define("global_indication", false);

        builder.pop();
    }
}
