package net.mcreator.target.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class ShockZaiXiaoGuoChiXuShiMeiKeFaShengProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setDeltaMovement(new Vec3(0, (entity.getDeltaMovement().y()), 0));
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 10));
		entity.setShiftKeyDown(true);
		{
			Entity _ent = entity;
			_ent.setYRot((float) (entity.getYRot() + Mth.nextDouble(RandomSource.create(), -15, 15)));
			_ent.setXRot((float) Mth.nextDouble(RandomSource.create(), -20, -23));
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
