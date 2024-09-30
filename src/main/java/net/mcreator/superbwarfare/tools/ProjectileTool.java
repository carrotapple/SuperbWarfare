package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Explosion;

import javax.annotation.Nullable;

public class ProjectileTool {

    public static void causeCustomExplode(ThrowableItemProjectile projectile, @Nullable DamageSource source, Entity target, float damage, float radius, float damageMultiplier) {
        CustomExplosion explosion = new CustomExplosion(projectile.level(), projectile, source, damage,
                target.getX(), target.getY(), target.getZ(), radius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(damageMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(projectile.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(projectile.level(), projectile.position());
        projectile.discard();
    }

    public static void causeCustomExplode(ThrowableItemProjectile projectile, Entity target, float damage, float radius, float damageMultiplier) {
        causeCustomExplode(projectile, ModDamageTypes.causeCustomExplosionDamage(projectile.level().registryAccess(), projectile, projectile.getOwner()),
                target, damage, radius, damageMultiplier);
    }

    public static void causeCustomExplode(ThrowableItemProjectile projectile, float damage, float radius, float damageMultiplier) {
        causeCustomExplode(projectile, projectile, damage, radius, damageMultiplier);
    }

    public static void causeCustomExplode(ThrowableItemProjectile projectile, float damage, float radius) {
        causeCustomExplode(projectile, damage, radius, 0.0f);
    }

}
