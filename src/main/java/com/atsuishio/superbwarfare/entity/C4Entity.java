package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class C4Entity extends Entity implements GeoEntity, AnimatedEntity, OwnableEntity {

    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Float> REL_X = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> REL_Y = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> REL_Z = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public C4Entity(EntityType<C4Entity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public C4Entity(LivingEntity owner, Level level) {
        super(ModEntities.C_4.get(), level);
        this.setOwnerUUID(owner.getUUID());
        ModUtils.queueServerWork(1, () -> {
            if (this.level().isClientSide()) return;
            CompoundTag compoundTag = owner.serializeNBT();
            compoundTag.putUUID("C4UUID", this.getUUID());
            this.getOwner().deserializeNBT(compoundTag);
        });
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(LAST_ATTACKER_UUID, "undefined");
        this.entityData.define(HEALTH, 10f);
        this.entityData.define(TARGET_UUID, Optional.empty());
        this.entityData.define(REL_X, 0.0f);
        this.entityData.define(REL_Y, 0.0f);
        this.entityData.define(REL_Z, 0.0f);
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

    public void setTargetUUID(@Nullable UUID uuid) {
        this.entityData.set(TARGET_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Nullable
    public UUID getTargetUUID() {
        return this.entityData.get(TARGET_UUID).orElse(null);
    }

    public boolean isOwnedBy(LivingEntity pEntity) {
        return pEntity == this.getOwner();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));
        if (this.getTargetUUID() != null) {
            compound.putUUID("Target", this.getTargetUUID());
            compound.putFloat("RelativeX", this.entityData.get(REL_X));
            compound.putFloat("RelativeY", this.entityData.get(REL_Y));
            compound.putFloat("RelativeZ", this.entityData.get(REL_Z));
        }
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        }

        if (compound.contains("LastAttacker")) {
            this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        }

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
            } catch (Throwable ignored){}
        }

        /*
        * Target
        **/
        if (compound.hasUUID("Target")) {
            uuid = compound.getUUID("Target");
        } else {
            String s = compound.getString("Target");

            assert this.getServer() != null;
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setTargetUUID(uuid);

                this.entityData.set(REL_X, compound.getFloat("RelativeX"));
                this.entityData.set(REL_Y, compound.getFloat("RelativeY"));
                this.entityData.set(REL_Z, compound.getFloat("RelativeZ"));
            } catch (Throwable ignored){}
        }
    }

    public boolean isPlaced() {
        return this.onGround() || this.getTargetUUID() != null;
    }


    @Override
    public void tick() {
        super.tick();
        var level = this.level();
        var x = this.getX();
        var y = this.getY();
        var z = this.getZ();

        if (this.tickCount >= ExplosionConfig.C4_EXPLOSION_COUNTDOWN.get()) {
            this.explode();
        }

        if (!this.isPlaced()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.98, 0.98, 0.98));

            for (Entity target : level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(this))).toList()) {
                if (this.getUUID() == target.getUUID() || this.getOwnerUUID() == target.getUUID() || !(target instanceof LivingEntity || target instanceof VehicleEntity)) {
                    continue;
                }
                this.setTargetUUID(target.getUUID());;

                // var relpos = this.calcRelativePos(target);
                // this.setRelativePos(((float) relpos.x), ((float) relpos.y), ((float) relpos.z));
                // if (!this.startRiding(target)) this.destroy();
                // ModUtils.LOGGER.info("Start Riding!");
                this.setInvisible(true);
                break;
            }
            if (this.onGround()) {
                this.destroy();
            }
        } else {
            ModUtils.queueServerWork(1, () -> {
                if (level.isClientSide()) return;
                if (this.getTargetUUID() == null) {
                    this.destroy();
                    return;
                }
                Entity target = EntityFindUtil.findEntity(this.level(), this.getTargetUUID().toString());
                if (target == null) {
                    this.destroy();
                    return;
                }
                if (!this.isInvisible()) {
                    this.setInvisible(true);
                }
                this.setPos(target.position().x, target.position().y + target.getBoundingBox().getYsize(), target.position().z);
            });
        }



        this.refreshDimensions();
    }

    public void explode() {
        if (!this.level().isClientSide()) {
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            this.discard();
        }

        ModUtils.queueServerWork(1, () -> {
            if (this.level().isClientSide()) return;

            this.triggerExplode(this);
        });
        this.discard();
    }

    public Vec3 getRelativePos() {
        if (this.isPlaced()) {
            return new Vec3(this.entityData.get(REL_X), this.entityData.get(REL_Y), this.entityData.get(REL_Z));
        }
        return null;
    }

    public Vec3 calcRelativePos(Entity target) {
        return this.position().subtract(target.position());
    }

    public void setRelativePos(float x, float y, float z) {
        this.entityData.set(REL_X, x);
        this.entityData.set(REL_X, y);
        this.entityData.set(REL_X, z);
    }

    public void destroy() {
        if (this.level() instanceof ServerLevel && this.tickCount < ExplosionConfig.C4_EXPLOSION_COUNTDOWN.get()) {
            ItemEntity c4 = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.C4_BOMB.get()));
            c4.setPickUpDelay(10);
            this.level().addFreshEntity(c4);
            this.discard();
        }
    }

    private void triggerExplode(Entity target) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeC4Damage(this.level().registryAccess(), this.getOwner()), ExplosionConfig.C4_EXPLOSION_DAMAGE.get(),
                target.getX(), target.getY(), target.getZ(), ExplosionConfig.C4_EXPLOSION_RADIUS.get(), ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.5);
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