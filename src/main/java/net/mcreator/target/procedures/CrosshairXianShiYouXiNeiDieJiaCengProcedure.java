package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CrosshairXianShiYouXiNeiDieJiaCengProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("target:gun"))) && entity.getPersistentData().getDouble("miaozhunshijian") < 7
                && !((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.M_79.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }
}
