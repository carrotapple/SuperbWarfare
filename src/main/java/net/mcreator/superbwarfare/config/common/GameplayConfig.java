package net.mcreator.superbwarfare.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class GameplayConfig {

    public static ForgeConfigSpec.BooleanValue GLOBAL_INDICATION;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("gameplay");

        builder.comment("Set FALSE if you want to show kill indication ONLY while killing an entity with a gun");
        GLOBAL_INDICATION = builder.define("global_indication", true);

        builder.pop();
    }

}
