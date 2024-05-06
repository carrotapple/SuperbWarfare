package net.mcreator.target.procedures;

import net.mcreator.target.entity.ClaymoreEntity;
import net.mcreator.target.init.TargetModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class ClaymoreMineYouJiFangKuaiShiFangKuaiDeWeiZhiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if (entity == null) return;
        if (entity instanceof LivingEntity _entity)
            _entity.swing(InteractionHand.MAIN_HAND, true);
        if (world instanceof ServerLevel _level) {
            TamableAnimal entityToSpawn = new ClaymoreEntity(TargetModEntities.CLAYMORE.get(), _level);
            entityToSpawn.moveTo(x, (y + 1.1), z, entity.getYRot(), entity.getXRot());
            entityToSpawn.setYBodyRot(entity.getYRot());
            entityToSpawn.setYHeadRot(entity.getYRot());
            entityToSpawn.setDeltaMovement((0.5 * entity.getLookAngle().x), (0.5 * entity.getLookAngle().y), (0.5 * entity.getLookAngle().z));
            entityToSpawn.finalizeSpawn(_level, _level.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            if (entity instanceof Player _owner) {
                entityToSpawn.tame(_owner);
            }
            _level.addFreshEntity(entityToSpawn);
        }
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(itemstack.getItem(), 20);
            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> itemstack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
        }
    }
}
