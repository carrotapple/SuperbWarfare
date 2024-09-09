package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

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

        public static final TagKey<Item> NORMAL_GUN = tag("normal_gun");
        public static final TagKey<Item> OPEN_BOLT = tag("open_bolt");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ModUtils.MODID, name));
        }
    }
}
