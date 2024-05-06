package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class RengchumortarProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;
        if (world instanceof ServerLevel _level) {
            Entity entityToSpawn = TargetModEntities.MORTAR.get().spawn(_level, BlockPos.containing(x + 1.5 * entity.getLookAngle().x, y, z + 1.5 * entity.getLookAngle().z), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                entityToSpawn.setYRot(entity.getYRot());
                entityToSpawn.setYBodyRot(entity.getYRot());
                entityToSpawn.setYHeadRot(entity.getYRot());
                entityToSpawn.setXRot(-70);
                entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }
        if (entity instanceof Player player && !player.isCreative()) {
            ItemStack _stktoremove = new ItemStack(TargetModItems.MORTAR_DEPOLYER.get());
            player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            player.swing(InteractionHand.MAIN_HAND, true);
        }
    }
}
