package net.mcreator.target.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.entity.ProjectileEntity;

import net.mcreator.target.init.TargetCustomModEntities;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.init.TargetModAttributes;

public class BulletfireNormalProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack usehand = ItemStack.EMPTY;
		double dam = 0;
		usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		if (Math.random() < 0.5) {
			entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.recoilhorizon = -1;
				capability.syncPlayerVariables(entity);
			});
		} else {
			entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.recoilhorizon = 1;
				capability.syncPlayerVariables(entity);
			});
		}

		entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			capability.recoil = 0.1;
			capability.syncPlayerVariables(entity);
		});
		entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			capability.firing = 1;
			capability.syncPlayerVariables(entity);
		});

		if (usehand.getOrCreateTag().getDouble("level") >= 10) {
			dam = usehand.getOrCreateTag().getDouble("damage") * (1 + 0.05 * (usehand.getOrCreateTag().getDouble("level") - 10));
		} else {
			dam = usehand.getOrCreateTag().getDouble("damage");
		}

		if (!entity.level().isClientSide() && entity instanceof LivingEntity living) {
			ProjectileEntity projectile = new ProjectileEntity(entity.level(), living, (float) dam);
			projectile.setOwner(living);

			projectile.setPos(living.getX(), living.getEyeY() - 0.1, living.getZ());
			projectile.shoot(living.getLookAngle().x, living.getLookAngle().y, living.getLookAngle().z, (float) usehand.getOrCreateTag().getDouble("velocity"),
					(float) living.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
			entity.level().addFreshEntity(projectile);
		}
	}
}
