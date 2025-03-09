package com.atsuishio.superbwarfare.entity.vehicle.weapon;

import com.atsuishio.superbwarfare.entity.projectile.HeliRocketEntity;
import net.minecraft.world.entity.LivingEntity;

public class HeliRocketWeapon extends VehicleWeapon {
    public float damage = 140, explosionDamage = 60, explosionRadius = 5;

    public HeliRocketWeapon damage(float damage) {
        this.damage = damage;
        return this;
    }

    public HeliRocketWeapon explosionDamage(float explosionDamage) {
        this.explosionDamage = explosionDamage;
        return this;
    }

    public HeliRocketWeapon explosionRadius(float explosionRadius) {
        this.explosionRadius = explosionRadius;
        return this;
    }

    public HeliRocketEntity create(LivingEntity entity) {
        return new HeliRocketEntity(entity, entity.level(), damage, explosionDamage, explosionRadius);
    }
}
