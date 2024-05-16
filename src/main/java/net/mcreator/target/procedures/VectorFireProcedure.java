package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VectorFireProcedure {
    public static void execute(Player entity) {
        if (entity.isSpectator()) return;

        ItemStack mainHandItem = entity.getMainHandItem();
        if (mainHandItem.getItem() != TargetModItems.VECTOR.get()) return;

        CompoundTag tag = mainHandItem.getOrCreateTag();
        if (tag.getInt("firemode") == 1 && tag.getDouble("burst") == 0) {
            tag.putDouble("burst", 3);
        }
    }
}