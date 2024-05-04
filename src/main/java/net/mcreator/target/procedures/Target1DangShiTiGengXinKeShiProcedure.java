package net.mcreator.target.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class Target1DangShiTiGengXinKeShiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setCustomName(Component.literal(("HP:" + (new java.text.DecimalFormat("##.##").format(entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) + "/"
				+ (new java.text.DecimalFormat("##.##").format(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)))));

		double[] recoilTimer = {0};
		double totalTime = 6;
		int sleepTime = 2;
		double recoilDuration = totalTime / sleepTime;
		Runnable recoilRunnable = () -> {
			while (recoilTimer[0] < recoilDuration) {
				
		if (entity.getPersistentData().getDouble("targetdown") > -1) {
			entity.getPersistentData().putDouble("targetdown", (entity.getPersistentData().getDouble("targetdown") - 1));
		}
		if (entity.getPersistentData().getDouble("targetdown") > 195) {
			{
				Entity _ent = entity;
				_ent.setYRot(entity.getYRot());
				_ent.setXRot((float) ((201 - entity.getPersistentData().getDouble("targetdown")) * (-18)));
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
		if (entity.getPersistentData().getDouble("targetdown") < 20 && entity.getPersistentData().getDouble("targetdown") > -1) {
			{
				Entity _ent = entity;
				_ent.setYRot(entity.getYRot());
				_ent.setXRot((float) (-90 + (20 - entity.getPersistentData().getDouble("targetdown")) * 4.5f));
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

		recoilTimer[0]++;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Thread recoilThread = new Thread(recoilRunnable);
		recoilThread.start();

	}
}

