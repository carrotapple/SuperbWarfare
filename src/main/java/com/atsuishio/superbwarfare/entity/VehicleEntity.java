package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

public class VehicleEntity extends Entity {

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.STRING);

    protected int interpolationSteps;
    protected double x;
    protected double y;
    protected double z;
    protected double serverYRot;
    protected double serverXRot;

    public float roll;
    public float prevRoll;

    public float getRoll() {
        return roll;
    }

    public float getRoll(float tickDelta) {
        return Mth.lerp(0.6f * tickDelta, prevRoll, getRoll());
    }

    public float getYaw(float tickDelta) {
        return Mth.lerp(0.6f * tickDelta, yRotO, getYRot());
    }

    public float getPitch(float tickDelta) {
        return Mth.lerp(0.6f * tickDelta, xRotO, getXRot());
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        this.entityData.set(HEALTH, compound.getFloat("Health"));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && stack.is(ModItems.CROWBAR.get())) {
            ItemStack container = ContainerBlockItem.createInstance(this);
            if (!player.addItem(container)) {
                player.drop(container, false);
            }
            this.remove(RemovalReason.DISCARDED);
            this.discard();
        } else if (stack.is(Items.IRON_INGOT)) {
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(Math.min(50, this.getMaxHealth()));
                stack.shrink(1);
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this, SoundEvents.IRON_GOLEM_REPAIR, this.getSoundSource(), 0.5f, 1);
                }
            } else if (!this.level().isClientSide) {
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
            }
        } else if (!this.level().isClientSide) {
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
        if (source.is(DamageTypes.ON_FIRE))
            return false;
        if (source.is(DamageTypes.IN_FIRE))
            return false;
        if (source.getEntity() != null) {
            this.entityData.set(LAST_ATTACKER_UUID, source.getEntity().getStringUUID());
        }
        return true;
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
    public void baseTick() {
        super.baseTick();

        prevRoll = this.getRoll();
        setZRot(roll * 0.9f);

        float delta = Math.abs(getYRot() - yRotO);
        while (getYRot() > 180F) {
            setYRot(getYRot() - 360F);
            yRotO = getYRot() - delta;
        }
        while (getYRot() <= -180F) {
            setYRot(getYRot() + 360F);
            yRotO = delta + getYRot();
        }

        handleClientSync();

        if (this.level() instanceof ServerLevel && this.getHealth() <= 0) {
            this.ejectPassengers();
            destroy();
        }

        travel();
        this.refreshDimensions();
    }

    public void destroy() {
    }

    public void travel() {
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

        double startLength = move.length();
        double endLength = view.length();
        if (startLength > 0.0D && endLength > 0.0D) {
            return Math.toDegrees(Math.acos(Mth.clamp(move.dot(view) / (startLength * endLength), -1, 1)));
        } else {
            return 0.0D;
        }
    }
}
