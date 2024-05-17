package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public class TargetModTags {

    public static class Items {
        public static final TagKey<Item> GUN = tag("gun");
        public static final TagKey<Item> HANDGUN = tag("handgun");
        public static final TagKey<Item> RIFLE = tag("rifle");
        public static final TagKey<Item> SHOTGUN = tag("shotgun");
        public static final TagKey<Item> SNIPER_RIFLE = tag("sniper_rifle");
        public static final TagKey<Item> SMG = tag("smg");
        public static final TagKey<Item> NORMAL_GUN = tag("normal_gun");
        public static final TagKey<Item> LEGENDARY_GUN = tag("legendary_gun");
        public static final TagKey<Item> SPECIAL_GUN = tag("special_gun");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(TargetMod.MODID, name));
        }
    }
}
