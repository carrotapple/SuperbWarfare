package net.mcreator.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ReloadConfig {

    public static ForgeConfigSpec.BooleanValue EMPTY_AUTO_RELOAD;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("reload");

        builder.comment("Set TRUE if you want to reload guns when ammo is empty");
        EMPTY_AUTO_RELOAD = builder.define("empty_auto_reload", true);

        builder.pop();
    }
}
