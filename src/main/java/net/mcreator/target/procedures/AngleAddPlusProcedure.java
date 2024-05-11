package net.mcreator.target.procedures;

import net.mcreator.target.tools.TraceTool;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AngleAddPlusProcedure {
    public static void execute(Entity entity) {
        Entity looking = TraceTool.findLookingEntity(entity, 6);
        if (looking == null || looking.getXRot() <= -78) return;

        looking.setYRot(looking.getYRot());
        looking.setXRot(looking.getXRot() - 10);
        looking.setYBodyRot(looking.getYRot());
        looking.setYHeadRot(looking.getYRot());
        looking.yRotO = looking.getYRot();
        looking.xRotO = looking.getXRot();
        if (looking instanceof LivingEntity living) {
            living.yBodyRotO = living.getYRot();
            living.yHeadRotO = living.getYRot();
        }
    }
}
