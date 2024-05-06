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
            itemstack.getOrCreateTag().putDouble("heatbar", 51);
        } else {
            itemstack.getOrCreateTag().putDouble("heatbar", (itemstack.getOrCreateTag().getDouble("heat")));
        }
        if (!((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == TargetModItems.MINIGUN.get())) {
            entity.getPersistentData().putDouble("minigunfiring", 0);
        }
        if (itemstack.getOrCreateTag().getDouble("overheat") > 0) {
            entity.getPersistentData().putDouble("minifiring", 0);
            itemstack.getOrCreateTag().putDouble("overheat", (itemstack.getOrCreateTag().getDouble("overheat") - 1));
        }
        WeapondrawhaveyProcedure.execute(entity, itemstack);
    }
}
