package net.mcreator.target.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Target1ShiTiChuShiShengChengShiProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        {
            Entity _ent = entity;
            _ent.setYRot(0);
            _ent.setXRot(0);
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
                _entity.yBodyRotO = _entity.getYRot();
                _entity.yHeadRotO = _entity.getYRot();
            }
        }
    }
}
