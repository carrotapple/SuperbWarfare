package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Aa12fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        ItemStack usehand;
        if (entity instanceof Player player && !player.isSpectator()) {
            usehand = player.getMainHandItem();
            if (usehand.getItem() == TargetModItems.AA_12.get()) {
                entity.getPersistentData().putDouble("firing", 1);
            }
        }
    }
}
