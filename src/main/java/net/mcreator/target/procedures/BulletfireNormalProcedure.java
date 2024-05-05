package net.mcreator.target.procedures;

import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BulletfireNormalProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand;
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

        if (!entity.level().isClientSide() && entity instanceof LivingEntity living) {
            ProjectileEntity projectile = new ProjectileEntity(entity.level(), living);
            projectile.setOwner(living);

            projectile.setPos((living.getX() + (-0.5) * living.getLookAngle().x), (living.getEyeY() - 0.1 + (-0.5) * living.getLookAngle().y), (living.getZ() + (-0.5) * living.getLookAngle().z));
            projectile.shoot(living.getLookAngle().x, living.getLookAngle().y, living.getLookAngle().z, (float) usehand.getOrCreateTag().getDouble("velocity"),
                    (float) living.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
            entity.level().addFreshEntity(projectile);
        }
    }
}
