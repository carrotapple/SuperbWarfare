package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.mojang.math.Axis;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Ah6Entity extends MobileVehicleEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 200;
    public static final int MAX_ENERGY = 4000000;
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.FLOAT);

    public Ah6Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.AH_6.get(), world);
    }

    public Ah6Entity(EntityType<Ah6Entity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.1f);
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
        super.baseTick();

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.06, 0.0));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 0.95, 0.6));
            this.setZRot(this.roll * 0.4f);
            this.setXRot(this.getXRot() * 0.4f);
        } else {
            float f = (float) Mth.clamp(0.93f + 0.03f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).scale(0.03 * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.95, f));
        }
        this.refreshDimensions();
    }

    @Override
    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        float diffX;
        float diffY;

        if (passenger == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
        } else if (passenger instanceof Player player) {
            if (level().isClientSide && this.getEnergy() > 0) {
                level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.forwardInputDown || this.backInputDown ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
            }

            if (!this.onGround()) {
                diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
                diffX = Math.clamp(-60f, 60f, Mth.wrapDegrees(passenger.getXRot() - this.getXRot()));

                if (rightInputDown) {
                    this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.8f);
                } else if (this.leftInputDown) {
                    this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.8f);
                }

                this.setYRot(this.getYRot() + 0.75f * diffY);
                this.setXRot(Mth.clamp(this.getXRot() + 0.5f * diffX, -85, 85));
                this.setZRot(Mth.clamp(this.getRoll() - this.entityData.get(DELTA_ROT) + 0.2f * diffY, -85, 85));
            }

            if (this.upInputDown || this.forwardInputDown) {
                this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.005f, 0.12f));
            }

            if (this.downInputDown || this.backInputDown) {
                this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.005f, 0));
            }

//            player.displayClientMessage(Component.literal("Power:" + new java.text.DecimalFormat("##.####").format(this.entityData.get(POWER))), true);
        }

        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.9f);
        this.entityData.set(POWER, this.entityData.get(POWER) * 0.9998f);


        setDeltaMovement(getDeltaMovement().add(0.0f, Math.sin((90 - this.getXRot()) * Mth.DEG_TO_RAD) * this.entityData.get(POWER), 0.0f));

        Vector3f direction = getRightDirection().mul(Math.cos((this.getRoll() + 90) * Mth.DEG_TO_RAD) * this.entityData.get(POWER));
        setDeltaMovement(getDeltaMovement().add(new Vec3(direction.x, direction.y, direction.z).scale(0.8)));

        Vector3f directionZ = getForwardDirection().mul(-Math.cos((this.getXRot() + 90) * Mth.DEG_TO_RAD) * this.entityData.get(POWER));
        setDeltaMovement(getDeltaMovement().add(new Vec3(directionZ.x, directionZ.y, directionZ.z).scale(0.5)));
    }

    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.WHEEL_CHAIR_ENGINE.get();
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -80.0F, 80F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);

        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -10.0F, 10.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction positionUpdater) {
        // From Immersive_Aircraft
        if (!hasPassenger(passenger)) {
            return;
        }

        Matrix4f transform = getVehicleTransform();

        float x = 0;
        float y = 0.8f;
        float z = 0;


        y += (float) passenger.getMyRidingOffset();

        Vector4f worldPosition = transformPosition(transform, x, y, z);

        passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);

        positionUpdater.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
    }

    // From Immersive_Aircraft
    public Matrix4f getVehicleTransform() {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) getX(), (float) getY(), (float) getZ());
        transform.rotate(Axis.YP.rotationDegrees(-getYRot()));
        transform.rotate(Axis.XP.rotationDegrees(getXRot()));
        transform.rotate(Axis.ZP.rotationDegrees(getRoll()));
        return transform;
    }

    protected Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
            CustomExplosion explosion = new CustomExplosion(this.level(), attacker == null ? this : attacker,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), attacker == null ? this : attacker, attacker == null ? this : attacker), 25.0f,
                    this.getX(), this.getY(), this.getZ(), 5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            this.discard();
        }
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
