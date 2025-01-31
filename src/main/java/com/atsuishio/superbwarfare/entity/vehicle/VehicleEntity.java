package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.VectorTool;
import com.google.common.collect.Lists;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class VehicleEntity extends Entity {

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<String> LAST_DRIVER_UUID = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.STRING);

    protected int interpolationSteps;
    protected double x;
    protected double y;
    protected double z;
    protected double serverYRot;
    protected double serverXRot;

    public float roll;
    public float prevRoll;
    public int lastHurtTick;
    public boolean crash;

    public float getRoll() {
        return roll;
    }

    public float getRoll(float tickDelta) {
        return Mth.lerp(tickDelta, prevRoll, getRoll());
    }

    public float getYaw(float tickDelta) {
        return Mth.lerp(tickDelta, yRotO, getYRot());
    }

    public float getPitch(float tickDelta) {
        return Mth.lerp(tickDelta, xRotO, getXRot());
    }

    public void setZRot(float rot) {
        roll = rot;
    }

    public VehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, this.getMaxHealth());
        this.entityData.define(LAST_ATTACKER_UUID, "undefined");
        this.entityData.define(LAST_DRIVER_UUID, "undefined");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        this.entityData.set(LAST_DRIVER_UUID, compound.getString("LastDriver"));
        this.entityData.set(HEALTH, compound.getFloat("Health"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));
        compound.putString("LastDriver", this.entityData.get(LAST_DRIVER_UUID));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;

        ItemStack stack = player.getMainHandItem();
        if (player.isShiftKeyDown() && stack.is(ModItems.CROWBAR.get()) && this.getFirstPassenger() == null) {
            ItemStack container = ContainerBlockItem.createInstance(this);
            if (!player.addItem(container)) {
                player.drop(container, false);
            }
            this.remove(RemovalReason.DISCARDED);
            this.discard();
            return InteractionResult.SUCCESS;
        } else if (this.getHealth() < this.getMaxHealth() && stack.is(Items.IRON_INGOT)) {
            this.heal(Math.min(50, this.getMaxHealth()));
            stack.shrink(1);
            if (!this.level().isClientSide) {
                this.level().playSound(null, this, SoundEvents.IRON_GOLEM_REPAIR, this.getSoundSource(), 0.5f, 1);
            }
            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown()) {
            if (this.getFirstPassenger() == null) {
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else if (!(this.getFirstPassenger() instanceof Player)) {
                this.getFirstPassenger().stopRiding();
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            if (this.canAddPassenger(player)) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
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
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        if (source.is(ModDamageTypes.VEHICLE_STRIKE)) {
            amount -= 20;
            crash = true;
        } else {
            crash = false;
        }
        if (source.getEntity() != null) {
            this.entityData.set(LAST_ATTACKER_UUID, source.getEntity().getStringUUID());
        }
        lastHurtTick = 0;

        return super.hurt(source, amount);
    }

    public void heal(float pHealAmount) {
        if (this.level() instanceof ServerLevel) {
            this.setHealth(this.getHealth() + pHealAmount);
        }

    }

    public void hurt(float pHealAmount) {
        if (this.level() instanceof ServerLevel) {
            this.setHealth(this.getHealth() - pHealAmount);
        }
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }

    public void setHealth(float pHealth) {
        this.entityData.set(HEALTH, Mth.clamp(pHealth, 0.0F, this.getMaxHealth()));
    }

    public float getMaxHealth() {
        return 50;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return super.isPushable();
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity attacker) {
        return hasPassenger(attacker) || super.skipAttackInteraction(attacker);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    public int getMaxPassengers() {
        return 1;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.lastHurtTick++;

        this.prevRoll = this.getRoll();

        float delta = Math.abs(getYRot() - yRotO);
        while (getYRot() > 180F) {
            setYRot(getYRot() - 360F);
            yRotO = getYRot() - delta;
        }
        while (getYRot() <= -180F) {
            setYRot(getYRot() + 360F);
            yRotO = delta + getYRot();
        }

        float deltaZ = Math.abs(getRoll() - prevRoll);
        while (getRoll() > 180F) {
            setZRot(getRoll() - 360F);
            prevRoll = getRoll() - deltaZ;
        }
        while (getRoll() <= -180F) {
            setZRot(getRoll() + 360F);
            prevRoll = deltaZ + getRoll();
        }

        handleClientSync();

        if (this.level() instanceof ServerLevel && this.getHealth() <= 0) {
            destroy();
        }

        travel();

        if (this.getHealth() <= 0.1 * this.getMaxHealth()) {
            this.hurt(0.1f);
        } else {
            if (!(this instanceof DroneEntity)) {
                this.heal(0.05f);
            }
        }

        if (getFirstPassenger() != null) {
            this.entityData.set(LAST_DRIVER_UUID, getFirstPassenger().getStringUUID());
        }

        this.refreshDimensions();
    }

    public void lowHealthWarning() {
        if (this.getHealth() <= 0.4 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
            }
        }

        if (this.getHealth() <= 0.25 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
            }
        }

        if (this.getHealth() <= 0.15 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
            }
        }

        if (this.getHealth() <= 0.1 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.FLAME, this.getX(), this.getY() + 0.85f * getBbHeight(), this.getZ(), 4, 0.35 * this.getBbWidth(), 0.12 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.05, true);
                ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 0.85f * getBbHeight(), this.getZ(), 4, 0.1 * this.getBbWidth(), 0.05 * this.getBbHeight(), 0.1 * this.getBbWidth(), 0.4, true);
            }
            if (this.tickCount % 15 == 0) {
                this.level().playSound(null, this.getOnPos(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1, 1);
            }
        }

        if (this.getHealth() < 0.1f * this.getMaxHealth() && tickCount % 13 == 0) {
            this.level().playSound(null, this.getOnPos(), ModSounds.NO_HEALTH.get(), SoundSource.PLAYERS, 1, 1);
        } else if (this.getHealth() >= 0.1f && this.getHealth() < 0.4f * this.getMaxHealth() && tickCount % 10 == 0) {
            this.level().playSound(null, this.getOnPos(), ModSounds.LOW_HEALTH.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public void destroy() {
    }

    public void travel() {
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

    public Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }

    protected void handleClientSync() {
        if (isControlledByLocalInstance()) {
            interpolationSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }
        if (interpolationSteps <= 0) {
            return;
        }
        double interpolatedX = getX() + (x - getX()) / (double) interpolationSteps;
        double interpolatedY = getY() + (y - getY()) / (double) interpolationSteps;
        double interpolatedZ = getZ() + (z - getZ()) / (double) interpolationSteps;
        double interpolatedYaw = Mth.wrapDegrees(serverYRot - (double) getYRot());
        setYRot(getYRot() + (float) interpolatedYaw / (float) interpolationSteps);
        setXRot(getXRot() + (float) (serverXRot - (double) getXRot()) / (float) interpolationSteps);

        setPos(interpolatedX, interpolatedY, interpolatedZ);
        setRot(getYRot(), getXRot());

        --interpolationSteps;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.x = x;
        this.y = y;
        this.z = z;
        serverYRot = yaw;
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    public static double calculateAngle(Vec3 move, Vec3 view) {
        move = move.multiply(1, 0, 1).normalize();
        view = view.multiply(1, 0, 1).normalize();

        return VectorTool.calculateAngle(move, view);
    }

    protected Vec3 getDismountOffset(double vehicleWidth, double passengerWidth) {
        double offset = (vehicleWidth + passengerWidth + (double) 1.0E-5f) / 2.0;
        float yaw = getYRot() + 90.0f;
        float x = -Mth.sin(yaw * ((float) java.lang.Math.PI / 180));
        float z = Mth.cos(yaw * ((float) java.lang.Math.PI / 180));
        float n = java.lang.Math.max(java.lang.Math.abs(x), java.lang.Math.abs(z));
        return new Vec3((double) x * offset / (double) n, 0.0, (double) z * offset / (double) n);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3d = getDismountOffset(getBbWidth() * Mth.SQRT_OF_TWO, passenger.getBbWidth() * Mth.SQRT_OF_TWO);
        double ox = getX() - vec3d.x;
        double oz = getZ() + vec3d.z;
        BlockPos exitPos = new BlockPos((int) ox, (int) getY(), (int) oz);
        BlockPos floorPos = exitPos.below();
        if (!level().isWaterAt(floorPos)) {
            ArrayList<Vec3> list = Lists.newArrayList();
            double exitHeight = level().getBlockFloorHeight(exitPos);
            if (DismountHelper.isBlockFloorValid(exitHeight)) {
                list.add(new Vec3(ox, (double) exitPos.getY() + exitHeight, oz));
            }
            double floorHeight = level().getBlockFloorHeight(floorPos);
            if (DismountHelper.isBlockFloorValid(floorHeight)) {
                list.add(new Vec3(ox, (double) floorPos.getY() + floorHeight, oz));
            }
            for (Pose entityPose : passenger.getDismountPoses()) {
                for (Vec3 vec3d2 : list) {
                    if (!DismountHelper.canDismountTo(level(), vec3d2, passenger, entityPose)) continue;
                    passenger.setPose(entityPose);
                    return vec3d2;
                }
            }
        }

        return super.getDismountLocationForPassenger(passenger);
    }

    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/gun_icon/default_icon.png");
    }
}
