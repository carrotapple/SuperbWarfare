package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class BocekreloadProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double ammo1 = 0;
        double id = 0;
        if (itemstack.getOrCreateTag().getDouble("arrowempty") > 0) {
            itemstack.getOrCreateTag().putDouble("arrowempty", (itemstack.getOrCreateTag().getDouble("arrowempty") - 1));
        }
        WeaponDrawProcedure.execute(entity, itemstack);
    }
}
