package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

public class MobileVehicleEntity extends EnergyVehicleEntity {
    public float power;
    public boolean leftInputDown;
    public boolean rightInputDown;
    public boolean forwardInputDown;
    public boolean backInputDown;
    public boolean upInputDown;
    public boolean downInputDown;
    public float roll;

    public MobileVehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        crushEntities(this.getDeltaMovement());
        this.refreshDimensions();
    }

    /**
     * 撞击实体并造成伤害
     * @param velocity 动量
     */
    public void crushEntities(Vec3 velocity) {
        var frontBox = getBoundingBox().move(velocity.scale(0.5));
        var velAdd = velocity.add(0, 0, 0).scale(1.5);
        for (var entity : level().getEntities(EntityTypeTest.forClass(Entity.class), frontBox, entity -> entity != this && entity != getFirstPassenger() && entity.getVehicle() == null)) {

            double entitySize = entity.getBbWidth() * entity.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            double f1 = Math.min(thisSize / entitySize, 4);
            if (!(entity instanceof TargetEntity)) {
                this.push(-f * velAdd.x, -f * velAdd.y, -f * velAdd.z);
            }

            if (velocity.length() > 0.25 && entity.isAlive() && !(entity instanceof ItemEntity || entity instanceof Projectile || entity instanceof ProjectileEntity)) {
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                }
                entity.push(f1 * velAdd.x, f1 * velAdd.y, f1 * velAdd.z);
                entity.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), (float) (20 * velocity.length()));
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }
}
