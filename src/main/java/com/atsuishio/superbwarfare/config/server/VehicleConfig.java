package com.atsuishio.superbwarfare.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class VehicleConfig {

    public static ForgeConfigSpec.BooleanValue COLLISION_DESTROY_BLOCKS;
    public static ForgeConfigSpec.BooleanValue COLLISION_DESTROY_HARD_BLOCKS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> COLLISION_ENTITY_BLACKLIST;

    @SuppressWarnings("SpellCheckingInspection")
    public static final List<? extends String> DEFAULT_COLLISION_ENTITY_BLACKLIST =
            List.of("create:super_glue", "zombieawareness:scent", "mts:builder_rendering");

    public static ForgeConfigSpec.IntValue REPAIR_COOLDOWN;
    public static ForgeConfigSpec.DoubleValue REPAIR_AMOUNT;

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

    public static ForgeConfigSpec.IntValue AH_6_HP;
    public static ForgeConfigSpec.IntValue AH_6_MIN_ENERGY_COST;
    public static ForgeConfigSpec.IntValue AH_6_MAX_ENERGY_COST;
    public static ForgeConfigSpec.IntValue AH_6_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue AH_6_CANNON_DAMAGE;
    public static ForgeConfigSpec.IntValue AH_6_ROCKET_DAMAGE;
    public static ForgeConfigSpec.IntValue AH_6_ROCKET_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.IntValue AH_6_ROCKET_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue LAV_150_HP;
    public static ForgeConfigSpec.IntValue LAV_150_ENERGY_COST;
    public static ForgeConfigSpec.IntValue LAV_150_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue LAV_150_CANNON_DAMAGE;
    public static ForgeConfigSpec.IntValue LAV_150_CANNON_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.DoubleValue LAV_150_CANNON_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue TOM_6_HP;
    public static ForgeConfigSpec.IntValue TOM_6_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue TOM_6_ENERGY_COST;
    public static ForgeConfigSpec.IntValue TOM_6_BOMB_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.DoubleValue TOM_6_BOMB_EXPLOSION_RADIUS;

    public static ForgeConfigSpec.IntValue BMP_2_HP;
    public static ForgeConfigSpec.IntValue BMP_2_ENERGY_COST;
    public static ForgeConfigSpec.IntValue BMP_2_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue BMP_2_CANNON_DAMAGE;
    public static ForgeConfigSpec.IntValue BMP_2_CANNON_EXPLOSION_DAMAGE;
    public static ForgeConfigSpec.DoubleValue BMP_2_CANNON_EXPLOSION_RADIUS;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("vehicle");

        builder.comment("Allows vehicles to destroy blocks via collision");
        COLLISION_DESTROY_BLOCKS = builder.define("collision_destroy_blocks", false);

        builder.comment("Allows vehicles to destroy hard blocks via collision");
        COLLISION_DESTROY_HARD_BLOCKS = builder.define("collision_destroy_hard_blocks", false);

        builder.comment("List of entities that cannot be damaged by collision");
        COLLISION_ENTITY_BLACKLIST = builder.defineList("collision_entity_blacklist",
                DEFAULT_COLLISION_ENTITY_BLACKLIST,
                e -> e instanceof String);

        builder.push("repair");

        builder.comment("The cooldown of vehicle repair. Set a negative value to disable vehicle repair");
        REPAIR_COOLDOWN = builder.defineInRange("repair_cooldown", 200, -1, 10000000);

        builder.comment("The amount of health restored per tick when a vehicle is self-repairing");
        REPAIR_AMOUNT = builder.defineInRange("repair_amount", 0.05d, 0, 10000000);

        builder.pop();

        builder.push("mk_42");

        builder.comment("The HealthPoint of MK-42");
        MK42_HP = builder.defineInRange("mk_42_hp", 350, 1, 10000000);

        builder.comment("The AP shell damage of MK-42");
        MK42_AP_DAMAGE = builder.defineInRange("mk_42_ap_damage", 450, 1, 10000000);

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
        MLE1934_HP = builder.defineInRange("mle_1934_hp", 350, 1, 10000000);

        builder.comment("The AP shell damage of MLE-1934");
        MLE1934_AP_DAMAGE = builder.defineInRange("mle_1934_ap_damage", 500, 1, 10000000);

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
        ANNIHILATOR_HP = builder.defineInRange("annihilator_hp", 1200, 1, 10000000);

        builder.comment("The energy cost of Annihilator per shoot");
        ANNIHILATOR_SHOOT_COST = builder.defineInRange("annihilator_shoot_cost", 2000000, 0, 2147483647);

        builder.comment("The max energy storage of Annihilator");
        ANNIHILATOR_MAX_ENERGY = builder.defineInRange("annihilator_max_energy", 20000000, 0, 2147483647);

        builder.pop();

        builder.push("speedboat");

        builder.comment("The HealthPoint of Speedboat");
        SPEEDBOAT_HP = builder.defineInRange("speedboat_hp", 200, 1, 10000000);

        builder.comment("The energy cost of Speedboat per tick");
        SPEEDBOAT_ENERGY_COST = builder.defineInRange("speedboat_energy_cost", 16, 0, 2147483647);

        builder.comment("The max energy storage of Speedboat");
        SPEEDBOAT_MAX_ENERGY = builder.defineInRange("speedboat_max_energy", 1000000, 0, 2147483647);

        builder.comment("The gun damage of Speedboat");
        SPEEDBOAT_GUN_DAMAGE = builder.defineInRange("speedboat_gun_damage", 25, 1, 10000000);

        builder.pop();

        builder.push("ah_6");

        builder.comment("The HealthPoint of AH-6");
        AH_6_HP = builder.defineInRange("ah_6_hp", 250, 1, 10000000);

        builder.comment("The min energy cost of AH-6 per tick");
        AH_6_MIN_ENERGY_COST = builder.defineInRange("ah_6_min_energy_cost", 64, 0, 2147483647);

        builder.comment("The max energy cost of AH-6 per tick");
        AH_6_MAX_ENERGY_COST = builder.defineInRange("ah_6_max_energy_cost", 128, 0, 2147483647);

        builder.comment("The max energy storage of AH-6");
        AH_6_MAX_ENERGY = builder.defineInRange("ah_6_max_energy", 4000000, 0, 2147483647);

        builder.comment("The cannon damage of AH-6");
        AH_6_CANNON_DAMAGE = builder.defineInRange("ah_6_cannon_damage", 20, 1, 10000000);

        builder.comment("The rocket damage of AH-6");
        AH_6_ROCKET_DAMAGE = builder.defineInRange("ah_6_rocket_damage", 80, 1, 10000000);

        builder.comment("The rocket explosion damage of AH-6");
        AH_6_ROCKET_EXPLOSION_DAMAGE = builder.defineInRange("ah_6_rocket_explosion_damage", 40, 1, 10000000);

        builder.comment("The rocket explosion radius of AH-6");
        AH_6_ROCKET_EXPLOSION_RADIUS = builder.defineInRange("ah_6_rocket_explosion_radius", 5, 1, 10000000);

        builder.pop();

        builder.push("lav_150");

        builder.comment("The HealthPoint of Lav_150");
        LAV_150_HP = builder.defineInRange("lav_150_hp", 250, 1, 10000000);

        builder.comment("The energy cost of Lav_150 per tick");
        LAV_150_ENERGY_COST = builder.defineInRange("lav_150_energy_cost", 64, 0, 2147483647);

        builder.comment("The max energy storage of Lav_150");
        LAV_150_MAX_ENERGY = builder.defineInRange("lav_150_max_energy", 3000000, 0, 2147483647);

        builder.comment("The cannon damage of Lav_150");
        LAV_150_CANNON_DAMAGE = builder.defineInRange("lav_150_cannon_damage", 45, 1, 10000000);

        builder.comment("The rocket explosion damage of Lav_150");
        LAV_150_CANNON_EXPLOSION_DAMAGE = builder.defineInRange("lav_150_cannon_explosion_damage", 12, 1, 10000000);

        builder.comment("The rocket explosion radius of Lav_150");
        LAV_150_CANNON_EXPLOSION_RADIUS = builder.defineInRange("lav_150_cannon_explosion_radius", 3d, 1d, 10000000d);

        builder.pop();

        builder.push("tom_6");

        builder.comment("The HealthPoint of Tom_6");
        TOM_6_HP = builder.defineInRange("tom_6_hp", 40, 1, 10000000);

        builder.comment("The energy cost of Tom_6 per tick");
        TOM_6_ENERGY_COST = builder.defineInRange("tom_6_energy_cost", 16, 0, 2147483647);

        builder.comment("The max energy storage of Tom_6");
        TOM_6_MAX_ENERGY = builder.defineInRange("tom_6_max_energy", 160000, 0, 2147483647);

        builder.comment("The Melon Bomb explosion damage of Tom_6");
        TOM_6_BOMB_EXPLOSION_DAMAGE = builder.defineInRange("tom_6_bomb_explosion_damage", 500, 1, 10000000);

        builder.comment("The Melon Bomb explosion radius of Tom_6");
        TOM_6_BOMB_EXPLOSION_RADIUS = builder.defineInRange("tom_6_bomb_explosion_radius", 10d, 1d, 10000000d);

        builder.pop();

        builder.push("bmp_2");

        builder.comment("The HealthPoint of Bmp_2");
        BMP_2_HP = builder.defineInRange("bmp_2_hp", 300, 1, 10000000);

        builder.comment("The energy cost of Bmp_2 per tick");
        BMP_2_ENERGY_COST = builder.defineInRange("bmp_2_energy_cost", 64, 0, 2147483647);

        builder.comment("The max energy storage of Bmp_2");
        BMP_2_MAX_ENERGY = builder.defineInRange("bmp_2_max_energy", 3000000, 0, 2147483647);

        builder.comment("The cannon damage of Bmp_2");
        BMP_2_CANNON_DAMAGE = builder.defineInRange("bmp_2_cannon_damage", 55, 1, 10000000);

        builder.comment("The cannon explosion damage of Bmp_2");
        BMP_2_CANNON_EXPLOSION_DAMAGE = builder.defineInRange("bmp_2_cannon_explosion_damage", 15, 1, 10000000);

        builder.comment("The cannon explosion radius of Bmp_2");
        BMP_2_CANNON_EXPLOSION_RADIUS = builder.defineInRange("bmp_2_cannon_explosion_radius", 3.2d, 1d, 10000000d);

        builder.pop();

        builder.pop();
    }
}
