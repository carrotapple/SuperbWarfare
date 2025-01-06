package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;

public class AnnihilatorEntity extends EnergyVehicleEntity implements GeoEntity, ICannonEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> LASER_LEFT_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> LASER_MIDDLE_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> LASER_RIGHT_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> OFFSET_ANGLE = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final float MAX_HEALTH = CannonConfig.ANNIHILATOR_HP.get();
    public static final int MAX_ENERGY = CannonConfig.ANNIHILATOR_MAX_ENERGY.get();
    public static final int SHOOT_COST = CannonConfig.ANNIHILATOR_SHOOT_COST.get();
    public Vec3 barrelLookAt;

    public AnnihilatorEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.ANNIHILATOR.get(), world);
    }

    public AnnihilatorEntity(EntityType<AnnihilatorEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(LASER_LEFT_LENGTH, 0f);
        this.entityData.define(LASER_MIDDLE_LENGTH, 0f);
        this.entityData.define(LASER_RIGHT_LENGTH, 0f);
        this.entityData.define(OFFSET_ANGLE, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("CoolDown", this.entityData.get(COOL_DOWN));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COOL_DOWN, compound.getInt("CoolDown"));
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            float f1 = (float) ((this.isRemoved() ? 0.009999999776482582 : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
            Vec3 vec3 = (new Vec3(1, 0.0, 0.0)).yRot(-this.getYRot() * 0.017453292F - 1.5707964F);
            pCallback.accept(pPassenger, this.getX() + vec3.x, this.getY() + (double) f1, this.getZ() + vec3.z);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.75;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 2.5, this.getZ(), 4, 0.2, 0.2, 0.2, 0.2, false);
        }

        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            amount *= 0.5f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 1.4f;
        }
        if (source.is(ModDamageTypes.GUN_FIRE)) {
            amount = 0;
        }
        if (source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)) {
            amount *= 0.1f;
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(0.5f * Math.max(amount - 40, 0));

        return true;
    }

    @Override
    public Vec3 getDeltaMovement() {
        return new Vec3(0, Math.min(super.getDeltaMovement().y, 0), 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.entityData.get(COOL_DOWN) > 0) {
            this.entityData.set(COOL_DOWN, this.entityData.get(COOL_DOWN) - 1);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getHealth() <= 0.4 * this.getMaxHealth()) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
            }

            if (this.getHealth() <= 0.25 * this.getMaxHealth()) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }

            if (this.getHealth() <= 0.15 * this.getMaxHealth()) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }

            if (this.getHealth() <= 0.1 * this.getMaxHealth()) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.FLAME, this.getX(), this.getY() + 3.2, this.getZ(), 4, 0.6, 0.1, 0.6, 0.05, false);
                ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 3, this.getZ(), 4, 0.1, 0.1, 0.1, 0.4, false);
                if (this.tickCount % 15 == 0) {
                    this.level().playSound(null, this.getOnPos(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1, 1);
                }
            }
        }

        float delta = Math.abs(getYRot() - yRotO);
        while (getYRot() > 180F) {
            setYRot(getYRot() - 360F);
            yRotO = getYRot() - delta;
        }
        while (getYRot() <= -180F) {
            setYRot(getYRot() + 360F);
            yRotO = delta + getYRot();
        }

        float yRot = this.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var BarrelRoot = new Vector3d(4.95, 2.25, 0);
        BarrelRoot.rotateY(-yRot * Mth.DEG_TO_RAD);

        // 中间炮管transform origin（？）世界坐标
        Vec3 BarrelRootPos = new Vec3(this.getX() + BarrelRoot.x, this.getY() + BarrelRoot.y, this.getZ() + BarrelRoot.z);

        var leftPos = new Vector3d(16, 0, -2.703125);
        leftPos.rotateZ(-this.getXRot() * Mth.DEG_TO_RAD);
        leftPos.rotateY(-yRot * Mth.DEG_TO_RAD);

        Vec3 BarrelLeftPos = new Vec3(BarrelRootPos.x + leftPos.x, BarrelRootPos.y + leftPos.y, BarrelRootPos.z + leftPos.z);

        var middlePos = new Vector3d(16, 0, 0);
        middlePos.rotateZ(-this.getXRot() * Mth.DEG_TO_RAD);
        middlePos.rotateY(-yRot * Mth.DEG_TO_RAD);

        Vec3 BarrelMiddlePos = new Vec3(BarrelRootPos.x + middlePos.x, BarrelRootPos.y + middlePos.y, BarrelRootPos.z + middlePos.z);

        var rightPos = new Vector3d(16, 0, 2.703125);
        rightPos.rotateZ(-this.getXRot() * Mth.DEG_TO_RAD);
        rightPos.rotateY(-yRot * Mth.DEG_TO_RAD);

        Vec3 BarrelRightPos = new Vec3(BarrelRootPos.x + rightPos.x, BarrelRootPos.y + rightPos.y, BarrelRootPos.z + rightPos.z);

        if (this.entityData.get(COOL_DOWN) > 88) {
            this.entityData.set(LASER_LEFT_LENGTH, Math.min(laserLength(BarrelLeftPos, this), laserLengthEntity(BarrelLeftPos, this)));
            this.entityData.set(LASER_MIDDLE_LENGTH, Math.min(laserLength(BarrelMiddlePos, this), laserLengthEntity(BarrelMiddlePos, this)));
            this.entityData.set(LASER_RIGHT_LENGTH, Math.min(laserLength(BarrelRightPos, this), laserLengthEntity(BarrelRightPos, this)));
        }

//        if (this.getPassengers().isEmpty()) {
//            autoAim();
//        } else {
//            travel();
//        }

        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        if (passenger instanceof ServerPlayer serverPlayer && this.entityData.get(COOL_DOWN) == 20) {
            SoundTool.playLocalSound(serverPlayer, ModSounds.ANNIHILATOR_RELOAD.get(), 1, 1);
        }

        this.refreshDimensions();
    }

    private float laserLength(Vec3 pos, Entity cannon) {
        if (this.level() instanceof ServerLevel) {
            BlockHitResult result = cannon.level().clip(new ClipContext(pos, pos.add(cannon.getViewVector(1).scale(512)),
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, cannon));

            Vec3 looking = Vec3.atLowerCornerOf(result.getBlockPos());
            Vec3 hitPos = result.getLocation();
            BlockPos _pos = BlockPos.containing(looking.x, looking.y, looking.z);

            float hardness = this.level().getBlockState(_pos).getBlock().defaultDestroyTime();

            if (ExplosionDestroyConfig.EXPLOSION_DESTROY.get() && hardness != -1) {
                Block.dropResources(this.level().getBlockState(_pos), this.level(), _pos, null);
                this.level().destroyBlock(_pos, true);
            }

            if (this.entityData.get(COOL_DOWN) > 98) {
                laserExplosion(hitPos);
            }
            this.level().explode(this, hitPos.x, hitPos.y, hitPos.z, 5, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);

        }

        return (float) pos.distanceTo((Vec3.atLowerCornerOf(cannon.level().clip(
                new ClipContext(pos, pos.add(cannon.getViewVector(1).scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, cannon)).getBlockPos())));
    }

    private float laserLengthEntity(Vec3 pos, Entity cannon) {
        if (this.level() instanceof ServerLevel) {
            double distance = 512 * 512;
            HitResult hitResult = cannon.pick(512, 1.0f, false);
            if (hitResult.getType() != HitResult.Type.MISS) {
                distance = hitResult.getLocation().distanceToSqr(pos);
                double blockReach = 5;
                if (distance > blockReach * blockReach) {
                    Vec3 posB = hitResult.getLocation();
                    hitResult = BlockHitResult.miss(posB, Direction.getNearest(pos.x, pos.y, pos.z), BlockPos.containing(posB));
                }
            }
            Vec3 viewVec = cannon.getViewVector(1.0F);
            Vec3 toVec = pos.add(viewVec.x * 512, viewVec.y * 512, viewVec.z * 512);
            AABB aabb = cannon.getBoundingBox().expandTowards(viewVec.scale(512)).inflate(1.0D, 1.0D, 1.0D);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(cannon, pos, toVec, aabb, p -> !p.isSpectator(), distance);
            if (entityhitresult != null) {
                Vec3 targetPos = entityhitresult.getLocation();
                double distanceToTarget = pos.distanceToSqr(targetPos);
                if (distanceToTarget > distance || distanceToTarget > 512 * 512) {
                    hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), BlockPos.containing(targetPos));
                } else if (distanceToTarget < distance) {
                    hitResult = entityhitresult;
                }
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
                    Entity target = ((EntityHitResult) hitResult).getEntity();
                    target.hurt(ModDamageTypes.causeLaserDamage(this.level().registryAccess(), passenger, passenger), (float) 200);
                    target.invulnerableTime = 0;
                    if (this.entityData.get(COOL_DOWN) > 98) {
                        laserExplosion(targetPos);
                    }
                    return (float) pos.distanceTo(target.position());
                }
            }
        }
        return 512;
    }

    private void laserExplosion(Vec3 pos) {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        CustomExplosion explosion = new CustomExplosion(this.level(), passenger,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), passenger, passenger), 300f,
                pos.x, pos.y, pos.z, 15f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnHugeExplosionParticles(this.level(), pos);
    }

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 200f,
                    this.getX(), this.getY(), this.getZ(), 10f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            this.discard();
        }
    }

    @Override
    public void vehicleShoot(Player player) {
        if (this.entityData.get(COOL_DOWN) > 0) {
            return;
        }

        if (this.getEnergy() < SHOOT_COST) {
            player.displayClientMessage(Component.translatable("tips.superbwarfare.annihilator.energy_not_enough").withStyle(ChatFormatting.RED), true);
            return;
        }

        Level level = player.level();
        if (level instanceof ServerLevel) {
            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.ANNIHILATOR_FIRE_1P.get(), 1, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.ANNIHILATOR_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.ANNIHILATOR_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.ANNIHILATOR_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.entityData.set(COOL_DOWN, 100);
            this.extraEnergy(SHOOT_COST);
            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(20), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(15, 15, 25, this.getX(), this.getEyeY(), this.getZ()));
                }
            }
        }
    }

    @Override
    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (!(passenger instanceof LivingEntity entity)) return;
        if (this.getEnergy() <= 0) return;

        float yRot = this.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var BarrelRoot = new Vector3d(4.95, 2.25, 0);
        BarrelRoot.rotateY(-yRot * Mth.DEG_TO_RAD);

        Vec3 barrelRootPos = new Vec3(this.getX() + BarrelRoot.x, this.getY() + BarrelRoot.y, this.getZ() + BarrelRoot.z);

        Vec3 passengersEyePos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());

        Entity lookingAt = TraceTool.findLookingEntity(entity, 512);

        if (lookingAt == null) {
            HitResult result = entity.level().clip(new ClipContext(passengersEyePos, passengersEyePos.add(entity.getViewVector(1).scale(512)),
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
            Vec3 blockHitPos = result.getLocation();
            barrelLookAt = new Vec3(blockHitPos.x - barrelRootPos.x, blockHitPos.y - barrelRootPos.y, blockHitPos.z - barrelRootPos.z);
        } else {
            barrelLookAt = new Vec3(lookingAt.getX() - barrelRootPos.x, lookingAt.getEyeY() - barrelRootPos.y, lookingAt.getZ() - barrelRootPos.z);
        }

        this.entityData.set(OFFSET_ANGLE, (float) calculateAngle(entity.getViewVector(1), barrelLookAt));

        float diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(entity.getYHeadRot() - this.getYRot()));
        float diffX = entity.getXRot() - this.entityData.get(OFFSET_ANGLE) - this.getXRot();

        diffX = diffX * 0.15f;

        this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -0.6f, 0.6f));
        this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -2f, 2f), -45, 5f + this.entityData.get(OFFSET_ANGLE)));
    }

    public void autoAim() {
        if (this.entityData.get(ENERGY) <= 0) return;

        Entity target = SeekTool.seekLivingEntity(this, this.level(), 64, 30);

        if (target == null) return;

        float yRot = this.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var BarrelRoot = new Vector3d(4.95, 2.25, 0);
        BarrelRoot.rotateY(-yRot * Mth.DEG_TO_RAD);

        Vec3 barrelRootPos = new Vec3(this.getX() + BarrelRoot.x, this.getY() + BarrelRoot.y, this.getZ() + BarrelRoot.z);
        Vec3 targetVec = new Vec3(target.getX() - barrelRootPos.x, target.getEyeY() - barrelRootPos.y, target.getZ() - barrelRootPos.z).normalize();

        double d0 = targetVec.x;
        double d1 = targetVec.y;
        double d2 = targetVec.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        this.setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
        float targetY = Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F);

        float diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(targetY - this.getYRot()));

        this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -1f, 1f));
        this.setRot(this.getYRot(), this.getXRot());
    }

    public static double calculateAngle(Vec3 passenger, Vec3 barrel) {
        double startLength = passenger.length();
        double endLength = barrel.length();
        if (startLength > 0.0D && endLength > 0.0D) {
            return Math.toDegrees(Math.acos(Mth.clamp(passenger.dot(barrel) / (startLength * endLength), -1, 1)));
        } else {
            return 0.0D;
        }
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -45.0F, 5f + this.entityData.get(OFFSET_ANGLE));
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState movementPredicate(AnimationState<AnnihilatorEntity> event) {
        if (this.entityData.get(COOL_DOWN) > 85) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.annihilator.fire"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.annihilator.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
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
