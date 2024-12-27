package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class CannonConfig {

    public static ForgeConfigSpec.IntValue MK42_HP;
    public static ForgeConfigSpec.IntValue MK42_AP_DAMAGE;
    public static ForgeConfigSpec.IntValue MK42_AP_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue MK42_AP_EXPLOSION_RADIUS;
    public static ForgeConfigSpec.IntValue MK42_HE_DAMAGE;
    public static ForgeConfigSpec.IntValue MK42_HE_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue MK42_HE_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue MLE1934_HP;
    public static ForgeConfigSpec.IntValue MLE1934_AP_DAMAGE;
    public static ForgeConfigSpec.IntValue MLE1934_AP_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue MLE1934_AP_EXPLOSION_RADIUS;
    public static ForgeConfigSpec.IntValue MLE1934_HE_DAMAGE;
    public static ForgeConfigSpec.IntValue MLE1934_HE_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue MLE1934_HE_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue ANNIHILATOR_HP;
    public static ForgeConfigSpec.IntValue ANNIHILATOR_SHOOT_COST;
    public static ForgeConfigSpec.IntValue ANNIHILATOR_MAX_ENERGY;

    public static ForgeConfigSpec.IntValue SPEEDBOAT_HP;
    public static ForgeConfigSpec.IntValue SPEEDBOAT_ENERGY_COST;
    public static ForgeConfigSpec.IntValue SPEEDBOAT_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue SPEEDBOAT_GUN_DAMAGE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("mk_42");

        builder.comment("The HealthPoint of MK-42");
        MK42_HP = builder.defineInRange("mk_42_hp", 500, 1, 10000000);

        builder.comment("The AP shell damage of MK-42");
        MK42_AP_DAMAGE = builder.defineInRange("mk_42_ap_damage", 300, 1, 10000000);

        builder.comment("The AP shell explosion damage of MK-42");
        MK42_AP_EXPLOSION_DAMAGE = builder.defineInRange("mk_42_ap_explosion_damage", 120, 1, 10000000);

        builder.comment("The AP shell explosion radius of MK-42");
        MK42_AP_EXPLOSION_RADIUS = builder.defineInRange("mk_42_ap_explosion_radius", 3, 1, 50);

        builder.comment("The HE shell damage of MK-42");
        MK42_HE_DAMAGE = builder.defineInRange("mk_42_he_damage", 150, 1, 10000000);

        builder.comment("The HE shell explosion damage of MK-42");
        MK42_HE_EXPLOSION_DAMAGE = builder.defineInRange("mk_42_he_explosion_damage", 200, 1, 10000000);

        builder.comment("The HE shell explosion radius of MK-42");
        MK42_HE_EXPLOSION_RADIUS = builder.defineInRange("mk_42_he_explosion_radius", 10, 1, 50);

        builder.pop();

        builder.push("mle_1934");

        builder.comment("The HealthPoint of MLE-1934");
        MLE1934_HP = builder.defineInRange("mle_1934_hp", 600, 1, 10000000);

        builder.comment("The AP shell damage of MLE-1934");
        MLE1934_AP_DAMAGE = builder.defineInRange("mle_1934_ap_damage", 350, 1, 10000000);

        builder.comment("The AP shell explosion damage of MLE-1934");
        MLE1934_AP_EXPLOSION_DAMAGE = builder.defineInRange("mle_1934_ap_explosion_damage", 150, 1, 10000000);

        builder.comment("The AP shell explosion radius of MLE-1934");
        MLE1934_AP_EXPLOSION_RADIUS = builder.defineInRange("mle_1934_ap_explosion_radius", 4, 1, 50);

        builder.comment("The HE shell damage of MLE-1934");
        MLE1934_HE_DAMAGE = builder.defineInRange("mle_1934_he_damage", 180, 1, 10000000);

        builder.comment("The HE shell explosion damage of MLE-1934");
        MLE1934_HE_EXPLOSION_DAMAGE = builder.defineInRange("mle_1934_he_explosion_damage", 240, 1, 10000000);

        builder.comment("The HE shell explosion radius of MLE-1934");
        MLE1934_HE_EXPLOSION_RADIUS = builder.defineInRange("mle_1934_he_explosion_radius", 12, 1, 50);

        builder.pop();

        builder.push("annihilator");

        builder.comment("The HealthPoint of Annihilator");
        ANNIHILATOR_HP = builder.defineInRange("annihilator_hp", 4000, 1, 10000000);

        builder.comment("The energy cost of Annihilator per shoot");
        ANNIHILATOR_SHOOT_COST = builder.defineInRange("annihilator_shoot_cost", 2000000, 0, 2147483647);

        builder.comment("The max energy storage of Annihilator");
        ANNIHILATOR_MAX_ENERGY = builder.defineInRange("annihilator_max_energy", 20000000, 0, 2147483647);

        builder.pop();

        builder.push("speedboat");

        builder.comment("The HealthPoint of Speedboat");
        SPEEDBOAT_HP = builder.defineInRange("speedboat_hp", 300, 1, 10000000);

        builder.comment("The energy cost of Speedboat per control tick");
        SPEEDBOAT_ENERGY_COST = builder.defineInRange("speedboat_energy_cost", 1, 0, 2147483647);

        builder.comment("The max energy storage of Speedboat");
        SPEEDBOAT_MAX_ENERGY = builder.defineInRange("speedboat_max_energy", 100000, 0, 2147483647);

        builder.comment("The gun damage of Speedboat");
        SPEEDBOAT_GUN_DAMAGE = builder.defineInRange("speedboat_gun_damage", 45, 1, 10000000);

        builder.pop();
    }

}
