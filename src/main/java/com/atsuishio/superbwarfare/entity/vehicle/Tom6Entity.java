package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Tom6Entity extends MobileVehicleEntity implements GeoEntity {
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(Tom6Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 50;
    public static final int MAX_ENERGY = 100000;

    public Tom6Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.TOM_6.get(), world);
    }

    public Tom6Entity(EntityType<Tom6Entity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(0.5f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DELTA_ROT, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(amount);
        return true;
    }

    @Override
    public void baseTick() {
        setZRot(roll * (onGround() ? 0.9f : 0.995f));
        super.baseTick();
        float f;

        if (this.onGround()) {
            f = (float) Mth.clamp(0.403f + 0.34f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
        } else {
            f = (float) Mth.clamp(0.683f + 0.06f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
        }

        this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).scale((0.33) * this.getDeltaMovement().length())));
        this.setDeltaMovement(this.getDeltaMovement().multiply(f, f, f));
        this.refreshDimensions();
    }

    @Override
    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

//        if (this.getEnergy() <= 0) return;

        float diffX;
        float diffY;

        if (passenger == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
            this.setZRot(this.roll * 0.8f);
            this.setXRot(this.getXRot() * 0.7f);
            this.entityData.set(POWER, this.entityData.get(POWER) * 0.98f);
        } else if (passenger instanceof Player) {

            diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
            diffX = Math.clamp(-60f, 60f, Mth.wrapDegrees(passenger.getXRot() - this.getXRot()));

            if (!onGround()) {
                if (rightInputDown) {
                    this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.35f);
                } else if (this.leftInputDown) {
                    this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.35f);
                }
            }

            this.setYRot(this.getYRot() + Mth.clamp(Math.min((this.onGround() ? 1.5f : 0.8f) * (float) Math.max(getDeltaMovement().length() - 0.06, 0), 0.9f) * diffY - 0.5f * this.entityData.get(DELTA_ROT), -3f, 3f));
            this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(Math.min(((this.onGround()) ? 0 : 0.3f) * (float) Math.max(getDeltaMovement().length() - 0.02, 0), 0.9f) * diffX, -2f, 2f), onGround() ? -10 : -120, onGround() ? 2 : 120));
            this.setZRot(this.getRoll() - this.entityData.get(DELTA_ROT) + (this.onGround() ? 0 : 0.004f) * diffY * (float) getDeltaMovement().length());
        }

        if (forwardInputDown) {
            this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.002f, 0.12f));
        }

        if (backInputDown) {
            this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.004f, -0.08f));
        }


//        if (this.forwardInputDown || this.backInputDown) {
//            this.extraEnergy(VehicleConfig.SPEEDBOAT_ENERGY_COST.get());
//        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.99f);
        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.95f);


        this.setDeltaMovement(this.getDeltaMovement().add(
                Mth.sin(-this.getYRot() * 0.017453292F) * 0.16f * this.entityData.get(POWER),
                Mth.clamp(Math.sin((onGround() ? 45 : -(getXRot() - 30)) * Mth.DEG_TO_RAD) * getDeltaMovement().horizontalDistance() * 0.092f, -0.04, 0.09),
                Mth.cos(this.getYRot() * 0.017453292F) * 0.16f * this.entityData.get(POWER)
                ));

    }

    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.WHEEL_CHAIR_ENGINE.get();
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -90.0F, 90F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);

        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -80.0F, 50.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYBodyRot(this.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        // From Immersive_Aircraft
        if (!this.hasPassenger(passenger)) {
            return;
        }

        Matrix4f transform = getVehicleTransform();

        float x = 0f;
        float y = 0.95f;
        float z = -0.2f;
        y += (float) passenger.getMyRidingOffset();

        int i = this.getPassengers().indexOf(passenger);

        if (i == 0) {
            Vector4f worldPosition = transformPosition(transform, x, y, z);
            passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
            callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
        }

        if (passenger != this.getFirstPassenger()) {
            passenger.setXRot(passenger.getXRot() + (getXRot() - xRotO));
        }

        copyEntityData(passenger);
    }

    public void copyEntityData(Entity entity) {
        float f = Mth.wrapDegrees(entity.getYRot() - getYRot());
        float g = Mth.clamp(f, -105.0f, 105.0f);
        entity.yRotO += g - f;
        entity.setYRot(entity.getYRot() + g - f);
        entity.setYHeadRot(entity.getYRot());
        entity.setYBodyRot(getYRot());
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));

        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 10f,
                    this.getX(), this.getY(), this.getZ(), 2f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnSmallExplosionParticles(this.level(), this.position());
        }

        this.discard();
    }

    @Override
    public float ignoreExplosionHorizontalKnockBack() {
        return -0.2f;
    }

    @Override
    public float ignoreExplosionVerticalKnockBack() {
        return -0.3f;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
}
