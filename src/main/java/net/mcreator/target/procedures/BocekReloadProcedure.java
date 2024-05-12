package net.mcreator.target.procedures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class BocekReloadProcedure {
    // TODO 合并至GunReload
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null) return;

        CompoundTag tag = itemstack.getOrCreateTag();
        if (tag.getDouble("arrowempty") > 0) {
            tag.putDouble("arrowempty", tag.getDouble("arrowempty") - 1);
        }
        WeaponDrawProcedure.execute(entity, itemstack);
    }
}
