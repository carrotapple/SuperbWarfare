package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M60fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.M_60.get()) {
                entity.getPersistentData().putDouble("firing", 1);
            }
        }
    }
}
