package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WheelChairEntity extends MobileVehicleEntity implements GeoEntity, IVehicleEntity {

    public static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(WheelChairEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(WheelChairEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 50;

    public WheelChairEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.WHEEL_CHAIR.get(), world);
    }

    public WheelChairEntity(EntityType<WheelChairEntity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.1f);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, MAX_HEALTH);
        this.entityData.define(POWER, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        } else {
            this.entityData.set(HEALTH, MAX_HEALTH);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !this.isPassengerOfSameVehicle(pEntity);
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.75F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.05;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypes.FALL))
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - amount);
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.getFirstPassenger() == null) {
            player.startRiding(this);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.078, 0.0));
        if (this.onGround()) {
            float f = 0.7f + 0.2f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.99, f));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.99, 0.99));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.entityData.get(HEALTH) <= 0) {
            this.ejectPassengers();
            destroy();
        }

        travel();
        this.refreshDimensions();
    }

    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (!(passenger instanceof LivingEntity entity)) return;

        float diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(entity.getYHeadRot() - this.getYRot()));

        this.setYRot(this.getYRot() + Mth.clamp(0.4f * diffY,-5f, 5f));

        if (this.forwardInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.01f);
        }

        if (this.backInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.01f);
        }

        if (this.upInputDown && this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.58, 0));
        }

//        if (this.getPersistentData().getBoolean("forward") || this.getPersistentData().getBoolean("backward")) {
//            this.entityData.set(ENERGY, Math.max(this.entityData.get(ENERGY) - CannonConfig.SPEEDBOAT_ENERGY_COST.get().floatValue(), 0));
//        }


        this.entityData.set(POWER, this.entityData.get(POWER) * 0.87f);

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add((double)(Mth.sin(-this.getYRot() * 0.017453292F) * this.entityData.get(POWER)), 0.0, (double)(Mth.cos(this.getYRot() * 0.017453292F) * this.entityData.get(POWER))));
        }
    }

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -90F, 90.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private void destroy() {
        if (level() instanceof ServerLevel) {
            level().explode(null, this.getX(), this.getY(), this.getZ(), 0, Level.ExplosionInteraction.NONE);
        }
        this.discard();
    }

    @Override
    public void vehicleShoot(Player player) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getHealth() {
        return this.entityData.get(HEALTH).intValue();
    }

    @Override
    public float getMaxHealth() {
        return (int) MAX_HEALTH;
    }

    @Override
    public boolean isDriver(Player player) {
        return player == this.getFirstPassenger();
    }

    @Override
    public int mainGunRpm() {
        return 0;
    }

    @Override
    public boolean canShoot(Player player) {
        return true;
    }

    @Override
    public int getAmmoCount(Player player) {
        return -1;
    }
}
