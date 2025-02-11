package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> GUN = tag("gun");

        public static final TagKey<Item> USE_HANDGUN_AMMO = tag("use_handgun_ammo");
        public static final TagKey<Item> USE_RIFLE_AMMO = tag("use_rifle_ammo");
        public static final TagKey<Item> USE_SHOTGUN_AMMO = tag("use_shotgun_ammo");
        public static final TagKey<Item> USE_SNIPER_AMMO = tag("use_sniper_ammo");

        public static final TagKey<Item> SMG = tag("smg");
        public static final TagKey<Item> HANDGUN = tag("handgun");
        public static final TagKey<Item> RIFLE = tag("rifle");
        public static final TagKey<Item> SNIPER_RIFLE = tag("sniper_rifle");
        public static final TagKey<Item> MACHINE_GUN = tag("machine_gun");
        public static final TagKey<Item> SHOTGUN = tag("shotgun");
        public static final TagKey<Item> LAUNCHER = tag("launcher");
        public static final TagKey<Item> REVOLVER = tag("revolver");

        public static final TagKey<Item> NORMAL_GUN = tag("normal_gun");

        public static final TagKey<Item> MILITARY_ARMOR = tag("military_armor");
        public static final TagKey<Item> MILITARY_ARMOR_HEAVY = tag("military_armor_heavy");

        public static final TagKey<Item> INGOTS_STEEL = tag("ingots/steel");
        public static final TagKey<Item> STORAGE_BLOCK_STEEL = tag("storage_blocks/steel");

        public static final TagKey<Item> INGOTS_CEMENTED_CARBIDE = tag("ingots/cemented_carbide");
        public static final TagKey<Item> STORAGE_BLOCK_CEMENTED_CARBIDE = tag("storage_blocks/cemented_carbide");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ModUtils.loc(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> SOFT_COLLISION = tag("soft_collision");
        public static final TagKey<Block> HARD_COLLISION = tag("hard_collision");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ModUtils.loc(name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> PROJECTILE = tag("projectile");
        public static final TagKey<DamageType> PROJECTILE_ABSOLUTE = tag("projectile_absolute");

        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ModUtils.loc(name));
        }
    }
}
