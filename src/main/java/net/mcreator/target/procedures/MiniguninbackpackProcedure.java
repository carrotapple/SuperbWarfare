package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MiniguninbackpackProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        if (itemstack.getOrCreateTag().getDouble("heat") > 0) {
            itemstack.getOrCreateTag().putDouble("heat", (itemstack.getOrCreateTag().getDouble("heat") - 0.5));
        }
        if (itemstack.getOrCreateTag().getDouble("heat") == 0) {
            itemstack.getOrCreateTag().putDouble("heat_bar", 51);
        } else {
            itemstack.getOrCreateTag().putDouble("heat_bar", (itemstack.getOrCreateTag().getDouble("heat")));
        }
        if (!((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.MINIGUN.get())) {
            entity.getPersistentData().putDouble("minigun_firing", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("overheat") > 0) {
            entity.getPersistentData().putDouble("mini_firing", 0);
            itemstack.getOrCreateTag().putDouble("overheat", (itemstack.getOrCreateTag().getDouble("overheat") - 1));
        }
    }
}
