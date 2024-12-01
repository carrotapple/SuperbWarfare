package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Code based on @BobMowzie's MowziesMobs, @EEEAB's EEEABsMobs and @Mercurows's DreamaticVoyage
 */
public class LaserEntity extends AbstractLaserEntity {

    public static final double RADIUS = 512D;

    public LaserEntity(EntityType<? extends LaserEntity> type, Level level) {
        super(type, level, 1);
    }

    public LaserEntity(Level level, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        super(ModEntities.LASER.get(), level, 1);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos(RADIUS);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    public void beamTick() {
        if (!this.level().isClientSide) {
            if (this.caster instanceof Player) {
                this.updateWithPlayer();
            } else if (this.caster != null) {
                this.updateWithEntity(0F, 0.75F);
            }
        }

        if (caster != null) {
            this.yaw = (float) Math.toRadians(caster.yHeadRot + 90);
            this.pitch = (float) -Math.toRadians(caster.getXRot());
        }

        if (this.tickCount >= this.getCountDown()) {
            this.calculateEndPos(RADIUS);
            if (this.blockSide != null) {
                this.spawnExplosionParticles();
            }
        }
    }

    public void spawnExplosionParticles() {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (this.caster == null) {
            discard();
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double radius = RADIUS;
        return distance < (radius * radius) * 2;
    }

    private void updateWithPlayer() {
        this.setYaw((float) Math.toRadians(caster.yHeadRot + 90));
        this.setPitch((float) Math.toRadians(-caster.getXRot()));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1.25);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + caster.getBbHeight() * 0.5F + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    private void updateWithEntity(float offset, float yOffset) {
        double radians = Math.toRadians(this.caster.yHeadRot + 90);
        this.setYaw((float) radians);
        this.setPitch((float) ((double) (-this.caster.getXRot()) * Math.PI / 180.0));
        double offsetX = Math.cos(radians) * offset;
        double offsetZ = Math.sin(radians) * offset;
        this.setPos(this.caster.getX() + offsetX, this.caster.getY(yOffset), this.caster.getZ() + offsetZ);
    }

}
