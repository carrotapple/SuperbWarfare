package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class TargetdeployerDangYouJianDianJiFangKuaiShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (world instanceof ServerLevel _level) {
            Entity entityToSpawn = TargetModEntities.TARGET_1.get().spawn(_level, BlockPos.containing(x + 0.5, y + 1, z + 0.5), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }
        if (entity instanceof Player _player) {
            ItemStack _stktoremove = new ItemStack(ItemRegistry.TARGET_DEPLOYER.get());
            _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
        }
        if (entity instanceof LivingEntity _entity)
            _entity.swing(InteractionHand.MAIN_HAND, true);
    }
}
