package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> GUN = tag("gun");

        public static final TagKey<Item> USE_HANDGUN_AMMO = tag("use_handgun_ammo");
        public static final TagKey<Item> USE_RIFLE_AMMO = tag("use_rifle_ammo");
        public static final TagKey<Item> USE_SHOTGUN_AMMO = tag("use_shotgun_ammo");
        public static final TagKey<Item> USE_SNIPER_AMMO = tag("use_sniper_ammo");
        public static final TagKey<Item> EXTRA_ONE_AMMO = tag("extra_one_ammo");

        public static final TagKey<Item> SMG = tag("smg");
        public static final TagKey<Item> HANDGUN = tag("handgun");
        public static final TagKey<Item> RIFLE = tag("rifle");
        public static final TagKey<Item> SNIPER_RIFLE = tag("sniper_rifle");
        public static final TagKey<Item> MACHINE_GUN = tag("machine_gun");
        public static final TagKey<Item> SHOTGUN = tag("shotgun");
        public static final TagKey<Item> LAUNCHER = tag("launcher");

        public static final TagKey<Item> NORMAL_GUN = tag("normal_gun");
        public static final TagKey<Item> OPEN_BOLT = tag("open_bolt");
        public static final TagKey<Item> CANNOT_RELOAD = tag("cannot_reload");
        public static final TagKey<Item> IS_AUTO_WEAPON = tag("is_auto_weapon");
        public static final TagKey<Item> CAN_CUSTOM_GUN = tag("can_custom_gun");

        public static final TagKey<Item> CAN_APPLY_BARREL = tag("can_apply_barrel");
        public static final TagKey<Item> CAN_APPLY_SCOPE = tag("can_apply_scope");
        public static final TagKey<Item> CAN_APPLY_GRIP = tag("can_apply_grip");
        public static final TagKey<Item> CAN_APPLY_MAGAZINE = tag("can_apply_magazine");
        public static final TagKey<Item> CAN_APPLY_STOCK= tag("can_apply_stock");

        public static final TagKey<Item> HAS_SHELL_EFFECT= tag("has_shell_effect");

        public static final TagKey<Item> MILITARY_ARMOR = tag("military_armor");
        public static final TagKey<Item> MILITARY_ARMOR_HEAVY = tag("military_armor_heavy");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ModUtils.MODID, name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> PROJECTILE = tag("projectile");
        public static final TagKey<DamageType> PROJECTILE_ABSOLUTE = tag("projectile_absolute");

        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, name));
        }
    }
}
