package net.mcreator.target.procedures;

import net.mcreator.target.init.ItemRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class MortarDangXiaoShiShiJianDaoShiProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        if (world instanceof Level _level && !_level.isClientSide())
            _level.explode(null, x, y, z, 0, Level.ExplosionInteraction.NONE);
        if (world instanceof ServerLevel _level) {
            ItemEntity entityToSpawn = new ItemEntity(_level, x, (y + 1), z, new ItemStack(ItemRegistry.MORTAR_BARREL.get()));
            entityToSpawn.setPickUpDelay(10);
            _level.addFreshEntity(entityToSpawn);
        }
        if (world instanceof ServerLevel _level) {
            ItemEntity entityToSpawn = new ItemEntity(_level, x, (y + 1), z, new ItemStack(ItemRegistry.MORTAR_BIPOD.get()));
            entityToSpawn.setPickUpDelay(10);
            _level.addFreshEntity(entityToSpawn);
        }
        if (world instanceof ServerLevel _level) {
            ItemEntity entityToSpawn = new ItemEntity(_level, x, (y + 1), z, new ItemStack(ItemRegistry.MORTAR_BASE_PLATE.get()));
            entityToSpawn.setPickUpDelay(10);
            _level.addFreshEntity(entityToSpawn);
        }
    }
}
