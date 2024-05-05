package net.mcreator.target.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AmmobarXianShiYouXiNeiDieJiaCengProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("target:gun")));
    }
}
