package net.mcreator.superbwarfare.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class DisplayConfig {

    public static ForgeConfigSpec.BooleanValue KILL_INDICATION;
    public static ForgeConfigSpec.BooleanValue GLOBAL_INDICATION;
    public static ForgeConfigSpec.BooleanValue AMMO_HUD;
    public static ForgeConfigSpec.BooleanValue FLOAT_CROSS_HAIR;
    public static ForgeConfigSpec.BooleanValue CAMERA_ROTATE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("display");

        builder.comment("Set TRUE if you want to show kill indication while killing an entity");
        KILL_INDICATION = builder.define("kill_indication", true);

        builder.comment("Set FALSE if you want to show kill indication ONLY while killing an entity with a gun");
        GLOBAL_INDICATION = builder.define("global_indication", false);

        builder.comment("Set TRUE to show ammo and gun info on HUD");
        AMMO_HUD = builder.define("ammo_hud", true);

        builder.comment("Set TRUE to enable float cross hair");
        FLOAT_CROSS_HAIR = builder.define("float_cross_hair", true);

        builder.comment("Set TRUE to enable camera rotate when holding a gun");
        CAMERA_ROTATE = builder.define("camera_rotate", true);

        builder.pop();
    }
}
