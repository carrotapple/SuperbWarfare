package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.TraceTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class BeamEntity extends Entity implements GeoEntity, AnimatedEntity, OwnableEntity {
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BeamEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final EntityDataAccessor<Float> LENGTH = SynchedEntityData.defineId(BeamEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected int interpolationSteps;
    protected double serverYRot;
    protected double serverXRot;
    protected double x;
    protected double y;
    protected double z;

    public BeamEntity(EntityType<BeamEntity> type, Level world) {
        super(type, world);
    }

    public BeamEntity(LivingEntity owner, Level level) {
        super(ModEntities.BEAM.get(), level);
        this.setOwnerUUID(owner.getUUID());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(LENGTH, 0f);

    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    public void setOwnerUUID(@Nullable UUID pUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(pUuid));
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }



    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
        compound.putFloat("Length", this.entityData.get(LENGTH));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");

            assert this.getServer() != null;
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable ignored) {
            }
        }

        if (compound.contains("Length")) {
            this.entityData.set(LENGTH, compound.getFloat("Length"));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() == null) {
            this.discard();
        }

        if (!this.getOwner().getMainHandItem().is(ModItems.BEAM_TEST.get()) || (this.getOwner().getMainHandItem().is(ModItems.BEAM_TEST.get()) && !this.getOwner().getMainHandItem().getOrCreateTag().getBoolean("Using"))) {
            this.discard();
        }

        this.updatePositionAndRotation();

        boolean lookAtEntity = false;
        double block_range = this.position().distanceTo((Vec3.atLowerCornerOf(this.level().clip(
                new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos())));

        BlockHitResult blockResult = this.level().clip(
                new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));

        BlockPos resultPos = blockResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);

        double entity_range = 0;

        Entity lookingEntity = TraceTool.findLookingEntity(this, 512);

        if (lookingEntity != null) {
            lookAtEntity = true;
            entity_range = this.distanceTo(lookingEntity);
        }

        if (lookAtEntity && lookingEntity != this.getOwner()) {
            entityData.set(LENGTH, (float)entity_range);
        } else if (!(state.getBlock() instanceof GlassBlock)){
            entityData.set(LENGTH, (float)block_range);
        }

        this.refreshDimensions();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public String getSyncedAnimation() {
        return null;
    }

    @Override
    public void setAnimation(String animation) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void updatePositionAndRotation() {
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            this.setPos(this.getOwner().getX() + 0.5 * this.getOwner().getLookAngle().x,
                    this.getOwner().getEyeY() - 0.3 + 0.5 * this.getOwner().getLookAngle().y,
                    this.getOwner().getZ() + 0.5 * this.getOwner().getLookAngle().z);

            this.setYRot(boundDegrees(owner.getYRot()));
            this.setXRot(boundDegrees(owner.getXRot()));
            this.yRotO = boundDegrees(owner.yRotO);
            this.xRotO = boundDegrees(owner.xRotO);
        }
    }

    private float boundDegrees(float v) {
        return (v % 360 + 360) % 360;
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

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}