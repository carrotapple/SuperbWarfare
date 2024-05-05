package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CannonAngleProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (entity.getXRot() > -20) {
            {
                entity.setYRot(entity.getYRot());
                entity.setXRot(-20);
                entity.setYBodyRot(entity.getYRot());
                entity.setYHeadRot(entity.getYRot());
                entity.yRotO = entity.getYRot();
                entity.xRotO = entity.getXRot();
                if (entity instanceof LivingEntity _entity) {
                    _entity.yBodyRotO = _entity.getYRot();
                    _entity.yHeadRotO = _entity.getYRot();
                }
            }
        }
    }
}
