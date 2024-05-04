package net.mcreator.target.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class CannonAngleProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getXRot() > -20) {
			{
				Entity _ent = entity;
				_ent.setYRot(entity.getYRot());
				_ent.setXRot(-20);
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
}
