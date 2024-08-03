package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeTool {

    public static boolean isArrowDamage(DamageSource source) {
        return source.is(ModDamageTypes.ARROW_IN_BRAIN) || source.is(ModDamageTypes.ARROW_IN_KNEE)
                || source.is(ModDamageTypes.ARROW_IN_BRAIN_ABSOLUTE) || source.is(ModDamageTypes.ARROW_IN_KNEE_ABSOLUTE);
    }

    public static boolean isArrowDamage(ResourceKey<DamageType> damageType) {
        return damageType == ModDamageTypes.ARROW_IN_BRAIN || damageType == ModDamageTypes.ARROW_IN_KNEE
                || damageType == ModDamageTypes.ARROW_IN_BRAIN_ABSOLUTE || damageType == ModDamageTypes.ARROW_IN_KNEE_ABSOLUTE;
    }

    public static boolean isGunDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)
                || source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE);
    }

    public static boolean isGunDamage(ResourceKey<DamageType> damageType) {
        return damageType == ModDamageTypes.GUN_FIRE || damageType == ModDamageTypes.GUN_FIRE_HEADSHOT
                || damageType == ModDamageTypes.GUN_FIRE_ABSOLUTE || damageType == ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE;
    }

    public static boolean isGunHeadshotDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE);
    }

    public static boolean isHeadshotDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE)
                || source.is(ModDamageTypes.ARROW_IN_BRAIN) || source.is(ModDamageTypes.ARROW_IN_BRAIN_ABSOLUTE);
    }

    public static boolean isGunFireDamage(DamageSource source) {
        return source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE);
    }

}
