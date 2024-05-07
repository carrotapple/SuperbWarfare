package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public class TargetModTags {

    public static class Items {
        public static final TagKey<Item> GUN = tag("gun");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(TargetMod.MODID, name));
        }
    }
}
