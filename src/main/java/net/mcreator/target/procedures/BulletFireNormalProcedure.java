package net.mcreator.target.procedures;

import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BulletFireNormalProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack heldItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
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
            float damage;
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            float velocity = 4 * (float) heldItem.getOrCreateTag().getDouble("power");

            if (heldItem.getItem() == TargetModItems.BOCEK.get()) {

                damage = 0.2f * (float) heldItem.getOrCreateTag().getDouble("speed") * (float) heldItem.getOrCreateTag().getDouble("damageadd");

                ProjectileEntity projectile = new ProjectileEntity(entity.level(), living, damage, headshot);

                projectile.setPos((living.getX() + (-0.5) * living.getLookAngle().x), (living.getEyeY() - 0.1 + (-0.5) * living.getLookAngle().y), (living.getZ() + (-0.5) * living.getLookAngle().z));
                projectile.shoot(living.getLookAngle().x, living.getLookAngle().y, living.getLookAngle().z, velocity, 2);
                entity.level().addFreshEntity(projectile);

            } else {
                damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("adddamage"))
                        * (float) heldItem.getOrCreateTag().getDouble("damageadd");

                ProjectileEntity projectile = new ProjectileEntity(entity.level(), living, damage, headshot);

                projectile.setPos((living.getX() + (-0.5) * living.getLookAngle().x), (living.getEyeY() - 0.1 + (-0.5) * living.getLookAngle().y), (living.getZ() + (-0.5) * living.getLookAngle().z));
                projectile.shoot(living.getLookAngle().x, living.getLookAngle().y, living.getLookAngle().z, (float) heldItem.getOrCreateTag().getDouble("velocity"),
                        (float) living.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                entity.level().addFreshEntity(projectile);
            }
        }
    }
}
