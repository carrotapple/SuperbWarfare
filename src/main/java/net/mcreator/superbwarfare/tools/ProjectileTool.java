package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Explosion;

import javax.annotation.Nullable;

public class ProjectileTool {

    public static void causeCustomExplode(ThrowableItemProjectile projectile, Entity target, float damage, float radius, float damageMultiplier) {
        CustomExplosion explosion = new CustomExplosion(projectile.level(), projectile,
                ModDamageTypes.causeProjectileBoomDamage(projectile.level().registryAccess(), projectile, projectile.getOwner()), damage,
                target.getX(), target.getY(), target.getZ(), radius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(damageMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(projectile.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(projectile.level(), projectile.position());
        projectile.discard();
    }

    public static void causeCustomExplode(ThrowableItemProjectile projectile, float damage, float radius, float damageMultiplier) {
        causeCustomExplode(projectile, projectile, damage, radius, damageMultiplier);
    }

    public static void causeCustomExplode(ThrowableItemProjectile projectile, float damage, float radius) {
        causeCustomExplode(projectile, damage, radius, 0.0f);
    }

    public static void causeCustomExplode(LivingEntity projectile, @Nullable LivingEntity owner, float damage, float radius, float damageMultiplier) {
        CustomExplosion explosion = new CustomExplosion(projectile.level(), projectile,
                ModDamageTypes.causeProjectileBoomDamage(projectile.level().registryAccess(), projectile, owner), damage,
                projectile.getX(), projectile.getY(), projectile.getZ(), radius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(damageMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(projectile.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(projectile.level(), projectile.position());
        projectile.discard();
    }
}
