package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExplosionConfig {

    public static ForgeConfigSpec.IntValue EXPLOSION_PENETRATION_RATIO;

    public static ForgeConfigSpec.IntValue RGO_GRENADE_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue RGO_GRENADE_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue M67_GRENADE_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue M67_GRENADE_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue MORTAR_SHELL_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue MORTAR_SHELL_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue DRONE_KAMIKAZE_HIT_DAMAGE;
    public static ForgeConfigSpec.IntValue DRONE_KAMIKAZE_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue DRONE_KAMIKAZE_EXPLOSION_RADIUS;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("explosion");

        builder.comment("The percentage of explosion damage you take behind cover");
        EXPLOSION_PENETRATION_RATIO = builder.defineInRange("explosion_destroy", 15, 0, 100);

        builder.pop();


        builder.push("RGO Grenade");

        builder.comment("The explosion damage of RGO grenade");
        RGO_GRENADE_EXPLOSION_DAMAGE = builder.defineInRange("Rgo grenade explosion damage", 90, 1, 10000000);

        builder.comment("The explosion radius of RGO grenade");
        RGO_GRENADE_EXPLOSION_RADIUS = builder.defineInRange("Rgo grenade explosion radius", 5, 1, 50);

        builder.pop();


        builder.push("M67 Grenade");

        builder.comment("The explosion damage of M67 grenade");
        M67_GRENADE_EXPLOSION_DAMAGE = builder.defineInRange("Rgo grenade explosion damage", 120, 1, 10000000);

        builder.comment("The explosion radius of M67 grenade");
        M67_GRENADE_EXPLOSION_RADIUS = builder.defineInRange("Rgo grenade explosion radius", 6, 1, 50);

        builder.pop();


        builder.push("Mortar shell");

        builder.comment("The explosion damage of Mortar shell");
        MORTAR_SHELL_EXPLOSION_DAMAGE = builder.defineInRange("Rgo grenade explosion damage", 160, 1, 10000000);

        builder.comment("The explosion radius of Mortar shell");
        MORTAR_SHELL_EXPLOSION_RADIUS = builder.defineInRange("Rgo grenade explosion radius", 9, 1, 50);

        builder.pop();

        builder.push("Drone Kamikaze");

        builder.comment("The hit damage of Drone Kamikaze");
        DRONE_KAMIKAZE_HIT_DAMAGE = builder.defineInRange("Rgo grenade explosion damage", 300, 1, 10000000);

        builder.comment("The explosion damage of Drone Kamikaze");
        DRONE_KAMIKAZE_EXPLOSION_DAMAGE = builder.defineInRange("Rgo grenade explosion damage", 160, 1, 10000000);

        builder.comment("The explosion radius of Drone Kamikaze");
        DRONE_KAMIKAZE_EXPLOSION_RADIUS = builder.defineInRange("Rgo grenade explosion radius", 9, 1, 50);

        builder.pop();
    }

}
