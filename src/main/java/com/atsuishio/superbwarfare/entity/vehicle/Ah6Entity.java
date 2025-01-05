package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.GunGrenadeEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class Ah6Entity extends MobileVehicleEntity implements GeoEntity, IHelicopterEntity, MultiWeaponVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 300;
    public static final int MAX_ENERGY = 4000000;
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> PROPELLER_ROT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> WEAPON_TYPE = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.INT);
    public boolean engineStart;
    public boolean engineStartOver;
    public float propellerRot;
    public float propellerRotO;

    public double velocity;

    public int fireIndex;

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
        this.entityData.define(WEAPON_TYPE, 0);
        this.entityData.define(PROPELLER_ROT, 0f);
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
        if (this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 2.5, this.getZ(), 4, 0.2, 0.2, 0.2, 0.2, false);
        }
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            amount *= 2f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 3f;
        }
        if (source.is(ModDamageTypes.GUN_FIRE)) {
            amount *= 0.3f;
        }
        if (source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)) {
            amount *= 0.7f;
        }
        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(0.75f * Math.max(amount - 5, 0));
        return true;
    }
    @Override
    public void baseTick() {
        propellerRotO = this.getPropellerRot();
        super.baseTick();

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 0.95, 0.8));
            this.setZRot(this.roll * 0.9f);
            this.setXRot(this.getXRot() * 0.9f);
        } else {
            float f = (float) Mth.clamp(0.945f + 0.02f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).scale((this.getXRot() < 0 ? -0.032 : (this.getXRot() > 0 ? 0.032 : 0)) * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.95, f));
        }

        if (this.isInWater() && this.tickCount %4 == 0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), 26 + (float) (60 * ((lastTickSpeed - 0.4) * (lastTickSpeed - 0.4))));
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
            this.setZRot(this.roll * 0.8f);
            this.setXRot(this.getXRot() * 0.8f);
            this.entityData.set(POWER, this.entityData.get(POWER) * 0.98f);
        } else if (passenger instanceof Player player) {
//            if (level().isClientSide && this.getEnergy() > 0) {
//                level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.forwardInputDown || this.backInputDown ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
//            }

            diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
            diffX = Math.clamp(-60f, 60f, Mth.wrapDegrees(passenger.getXRot() - this.getXRot()));

            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.25f);
            } else if (this.leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.25f);
            }

            this.setYRot(this.getYRot() + Mth.clamp((this.onGround() ? 0.1f : 2f) * diffY * this.entityData.get(POWER) - 0.5f * this.entityData.get(DELTA_ROT), -8f, 8f));
            this.setXRot(Mth.clamp(this.getXRot() + (this.onGround() ? 0 : 1.4f) * diffX * this.entityData.get(POWER), -80, 80));
            this.setZRot(Mth.clamp(this.getRoll() - this.entityData.get(DELTA_ROT) + (this.onGround() ? 0 : 0.2f) * diffY * this.entityData.get(POWER), -80, 80));
        }

        if (this.level() instanceof ServerLevel) {
            boolean up = this.upInputDown || this.forwardInputDown;
            boolean down = this.downInputDown || this.backInputDown;

            if (!engineStart && up) {
                engineStart = true;
                this.level().playSound(null, this, ModSounds.HELICOPTER_ENGINE_START.get(), this.getSoundSource(), 3, 1);
            }

            if (up && engineStartOver) {
                this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.002f, 0.12f));
            }

            if (down && engineStartOver) {
                this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.0015f, this.onGround() ? 0 : 0.0375f));
            }

            if (engineStart && !engineStartOver) {
                this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.0012f, 0.045f));
            }

            if (!(up || down) && engineStartOver) {
                if (this.getDeltaMovement().y() + 0.06 < 0) {
                    this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.0002f, 0.12f));
                } else {
                    this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - (this.onGround() ? 0.00005f : 0.0006f), 0));
                }
            }
        }

        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.95f);
        this.entityData.set(PROPELLER_ROT, Mth.lerp(0.0001f, this.entityData.get(POWER), this.entityData.get(POWER)));
        this.setPropellerRot(this.getPropellerRot() + 30 * this.entityData.get(PROPELLER_ROT));
        this.entityData.set(PROPELLER_ROT, this.entityData.get(PROPELLER_ROT) * 0.9995f);

        setDeltaMovement(getDeltaMovement().add(0.0f, Math.min(Math.sin((90 - this.getXRot()) * Mth.DEG_TO_RAD), Math.sin((90 + this.getRoll()) * Mth.DEG_TO_RAD)) * this.entityData.get(POWER), 0.0f));

        Vector3f direction = getRightDirection().mul(Math.cos((this.getRoll() + 90) * Mth.DEG_TO_RAD) * this.entityData.get(POWER));
        setDeltaMovement(getDeltaMovement().add(new Vec3(direction.x, direction.y, direction.z).scale(0.85)));

        Vector3f directionZ = getForwardDirection().mul(-Math.cos((this.getXRot() + 90) * Mth.DEG_TO_RAD) * this.entityData.get(POWER));
        setDeltaMovement(getDeltaMovement().add(new Vec3(directionZ.x, directionZ.y, directionZ.z).scale(0.35)));

        if (this.entityData.get(POWER) > 0.04f) {
            engineStartOver = true;
        }

        if (this.entityData.get(POWER) < 0.0004f) {
            engineStart = false;
            engineStartOver = false;
        }

//        if (level().isClientSide) {
//            level().playLocalSound(this.getX() + this.getDeltaMovement().x, this.getY() + this.getBbHeight() + this.getDeltaMovement().y + 0.06, this.getZ() + this.getDeltaMovement().z, this.getEngineSound(), this.getSoundSource(), Math.max((this.upInputDown ? 10f : 3f) * 6 * this.entityData.get(POWER) - 0.038f, 0), (random.nextFloat() * 0.1f + 1.0f), false);
//        }
    }

    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.HELICOPTER_ENGINE.get();
    }

    public float getPropellerRot() {
        return this.propellerRot;
    }

    public void setPropellerRot(float pPropellerRot) {
        this.propellerRot = pPropellerRot;
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -80.0F, 80F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);

        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -80.0F, 80.0F);
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

        float x = 0.6f;
        float y = 1.2f;
        float z = 1f;
        y += (float) passenger.getMyRidingOffset();

        int i = this.getPassengers().indexOf(passenger);

        if (i == 0) {
            Vector4f worldPosition = transformPosition(transform, x, y, z);
            passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
            callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
        } else if (i == 1) {
            Vector4f worldPosition = transformPosition(transform, -x, y, z);
            passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
            callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
        }

        if (passenger != this.getFirstPassenger()){
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
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    public int getMaxPassengers() {
        return 2;
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        if (crash) {
            List<Entity> passenger = this.getPassengers();
            for (var e : passenger) {
                if (e instanceof LivingEntity living) {
                    if (e instanceof ServerPlayer victim) {
                        living.setHealth(0);
                        if (attacker == null || attacker == victim) {
                            living.level().players().forEach(
                                    p -> p.sendSystemMessage(
                                            Component.translatable("death.attack.air_crash", victim.getDisplayName())
                                    )
                            );
                        } else {
                            living.level().players().forEach(
                                    p -> p.sendSystemMessage(
                                            Component.translatable("death.attack.air_crash_entity",
                                                    victim.getDisplayName(),
                                                    attacker.getDisplayName()
                                            )
                                    )
                            );
                        }
                    } else {
                        living.setHealth(0);
                        living.level().broadcastEntityEvent(living, (byte) 60);
                        living.remove(RemovalReason.KILLED);
                        living.gameEvent(GameEvent.ENTITY_DIE);
                    }
                }
            }
        }

        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, attacker), 300.0f,
                    this.getX(), this.getY(), this.getZ(), 8f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());
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

    @Override
    public void vehicleShoot(Player player) {

        Matrix4f transform = getVehicleTransform();
        float x;
        float y;
        float z;

        Vector4f worldPositionRight;
        Vector4f worldPositionLeft;

        if (entityData.get(WEAPON_TYPE) == 0) {
            x = 1f;
            y = 0.62f;
            z = 0.8f;

            worldPositionRight = transformPosition(transform, -x, y, z);
            worldPositionLeft = transformPosition(transform, x, y, z);

            ProjectileEntity projectileRight = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(25)
                    .headShot(2f)
                    .zoom(false);

            projectileRight.heBullet(true, 5);
            projectileRight.bypassArmorRate(1);
            projectileRight.setPos(worldPositionRight.x, worldPositionRight.y, worldPositionRight.z);
            projectileRight.shoot(player, this.getLookAngle().x, this.getLookAngle().y+ 0.03, this.getLookAngle().z, 20,
                    (float) 0.2);
            this.level().addFreshEntity(projectileRight);
            sendParticle((ServerLevel) this.level(), ParticleTypes.LARGE_SMOKE, worldPositionRight.x, worldPositionRight.y, worldPositionRight.z, 1, 0, 0, 0, 0, false);

            ProjectileEntity projectileLeft = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(25)
                    .headShot(2f)
                    .zoom(false);

            projectileLeft.heBullet(true, 5);
            projectileLeft.bypassArmorRate(1);
            projectileLeft.setPos(worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z);
            projectileLeft.shoot(player, this.getLookAngle().x, this.getLookAngle().y + 0.03, this.getLookAngle().z, 20,
                    (float) 0.2);
            this.level().addFreshEntity(projectileLeft);
            sendParticle((ServerLevel) this.level(), ParticleTypes.LARGE_SMOKE, worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z, 1, 0, 0, 0, 0, false);

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_FIRE_3P.get(), 4, 1);
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_FAR.get(), 12, 1);
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_VERYFAR.get(), 24, 1);
                }
            }
        } else if (entityData.get(WEAPON_TYPE) == 1) {
            x = 1.15f;
            y = 0.62f;
            z = 0.8f;

            worldPositionRight = transformPosition(transform, -x, y, z);
            worldPositionLeft = transformPosition(transform, x, y, z);

            if (fireIndex == 0) {
                GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, player.level(),
                        110,
                        40,
                        6);

                gunGrenadeEntity.setPos(worldPositionRight.x, worldPositionRight.y, worldPositionRight.z);
                gunGrenadeEntity.shoot(this.getLookAngle().x, this.getLookAngle().y+ 0.03, this.getLookAngle().z, 8, 0.25f);
                player.level().addFreshEntity(gunGrenadeEntity);
                fireIndex = 1;
            } else if (fireIndex == 1){
                GunGrenadeEntity gunGrenadeEntityLeft = new GunGrenadeEntity(player, player.level(),
                        110,
                        40,
                        5);

                gunGrenadeEntityLeft.setPos(worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z);
                gunGrenadeEntityLeft.shoot(this.getLookAngle().x, this.getLookAngle().y+ 0.03, this.getLookAngle().z, 8, 0.25f);
                player.level().addFreshEntity(gunGrenadeEntityLeft);
                fireIndex = 0;
            }

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.HELICOPTER_ROCKET_FIRE_3P.get(), 6, 1);
                }
            }
        }

        Level level = player.level();
        final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

        for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
            if (target instanceof ServerPlayer serverPlayer) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 7, this.getX(), this.getEyeY(), this.getZ()));
            }
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3d = getDismountOffset(getBbWidth() * Mth.SQRT_OF_TWO, passenger.getBbWidth() * Mth.SQRT_OF_TWO);
        double ox = getX() + vec3d.x;;
        int i = this.getPassengers().indexOf(passenger);
        if (i == 0 || i == 2) {
            ox = getX() - vec3d.x;
        }

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

    @Override
    public boolean isDriver(Player player) {
        return player == this.getFirstPassenger();
    }

    @Override
    public int mainGunRpm() {
        return 300;
    }

    @Override
    public boolean canShoot(Player player) {
        return true;
    }

    @Override
    public int getAmmoCount(Player player) {
        return -1;
    }


    @Override
    public float getRotX(float tickDelta) {
        return this.getPitch(tickDelta);
    }

    @Override
    public float getRotY(float tickDelta) {
        return this.getYaw(tickDelta);
    }

    @Override
    public float getRotZ(float tickDelta) {
        return this.getRoll(tickDelta);
    }

    @Override
    public float getPower() {
        return this.entityData.get(POWER);
    }

    @Override
    public void changeWeapon() {
        entityData.set(WEAPON_TYPE, entityData.get(WEAPON_TYPE) + 1);
        if (entityData.get(WEAPON_TYPE) == 2) {
            entityData.set(WEAPON_TYPE, 0);
        }
    }

    @Override
    public int getWeaponType() {
        return entityData.get(WEAPON_TYPE);
    }
}
