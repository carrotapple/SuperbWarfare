package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
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

public class AnnihilatorEntity extends Entity implements GeoEntity, ICannonEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> LASER_LEFT_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> LASER_MIDDLE_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> LASER_RIGHT_LENGTH = SynchedEntityData.defineId(AnnihilatorEntity.class, EntityDataSerializers.FLOAT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final float MAX_HEALTH = CannonConfig.ANNIHILATOR_HP.get();

    protected int interpolationSteps;
    protected double serverYRot;
    protected double serverXRot;

    public AnnihilatorEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.ANNIHILATOR.get(), world);
    }

    public AnnihilatorEntity(EntityType<AnnihilatorEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(HEALTH, MAX_HEALTH);
        this.entityData.define(LASER_LEFT_LENGTH, 0f);
        this.entityData.define(LASER_MIDDLE_LENGTH, 0f);
        this.entityData.define(LASER_RIGHT_LENGTH, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("CoolDown", this.entityData.get(COOL_DOWN));
        compound.putFloat("Health", this.entityData.get(HEALTH));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(COOL_DOWN, compound.getInt("CoolDown"));
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        } else {
            this.entityData.set(HEALTH, MAX_HEALTH);
        }
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.75;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 2.5, this.getZ(), 4, 0.2, 0.2, 0.2, 0.2, false);
        }

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
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            amount *= 0.5f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 1.4f;
        }
        if (source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)) {
            amount *= 1.6f;
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.5f * Math.max(amount - 40, 0));

        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (player.getMainHandItem().is(ModItems.CROWBAR.get()) && this.getFirstPassenger() == null) {
                ItemStack stack = ContainerBlockItem.createInstance(this);
                if (!player.addItem(stack)) {
                    player.drop(stack, false);
                }

                this.discard();
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            return InteractionResult.PASS;
        } else {
            if (this.getFirstPassenger() == null) {
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                player.startRiding(this);
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }

        return InteractionResult.PASS;
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

        if (this.entityData.get(HEALTH) <= 0.4 * CannonConfig.ANNIHILATOR_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.25 * CannonConfig.ANNIHILATOR_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.15 * CannonConfig.ANNIHILATOR_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.1 * CannonConfig.ANNIHILATOR_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.FLAME, this.getX(), this.getY() + 3.2, this.getZ(), 4, 0.6, 0.1, 0.6, 0.05, false);
                ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 3, this.getZ(), 4, 0.1, 0.1, 0.1, 0.4, false);
            }
            if (this.tickCount % 15 == 0) {
                this.level().playSound(null, this.getOnPos(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1, 1);
            }
            this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.1f);
        } else {
            this.entityData.set(HEALTH, Math.min(this.entityData.get(HEALTH) + 0.05f, MAX_HEALTH));
        }

        if (this.entityData.get(HEALTH) <= 0) {
            this.ejectPassengers();
            destroy();
        }

        float yRot = this.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var BarrelRoot = new Vector3d(4.95, 2.25, 0);
        BarrelRoot.rotateY(-yRot * Mth.DEG_TO_RAD);

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
            this.entityData.set(LASER_LEFT_LENGTH, Math.min(laserLength(BarrelLeftPos ,this), laserLengthEntity(BarrelLeftPos ,this)));
            this.entityData.set(LASER_MIDDLE_LENGTH, Math.min(laserLength(BarrelMiddlePos ,this), laserLengthEntity(BarrelMiddlePos ,this)));
            this.entityData.set(LASER_RIGHT_LENGTH, Math.min(laserLength(BarrelRightPos ,this), laserLengthEntity(BarrelRightPos ,this)));
        }

        travel();
        this.refreshDimensions();
    }

    private float laserLength (Vec3 pos, Entity cannon) {
        if (this.entityData.get(COOL_DOWN) > 98) {
            HitResult result = cannon.level().clip(new ClipContext(pos, pos.add(cannon.getViewVector(1).scale(512)),
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, cannon));
            Vec3 hitPos = result.getLocation();
            laserExplosion(hitPos);
        }

        return (float) pos.distanceTo((Vec3.atLowerCornerOf(cannon.level().clip(
                new ClipContext(pos, pos.add(cannon.getViewVector(1).scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, cannon)).getBlockPos())));
    }

    private float laserLengthEntity (Vec3 pos, Entity cannon) {
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

    private void destroy() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 160f,
                this.getX(), this.getY(), this.getZ(), 20f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());

        this.discard();
    }

    @Override
    public void cannonShoot(Player player) {
        if (this.entityData.get(COOL_DOWN) > 0) {
            return;
        }

        Level level = player.level();
        if (level instanceof ServerLevel server) {
            ItemStack stack = player.getMainHandItem();


            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.ANNIHILATOR_FIRE_1P.get(), 1, 1);
//                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_RELOAD.get(), 2, 1);
//                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
//                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
//                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.entityData.set(COOL_DOWN, 100);

            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(20), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {

                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(15, 15, 25, this.getX(), this.getEyeY(), this.getZ()));
                }
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        serverYRot = yaw;
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (!(passenger instanceof LivingEntity entity)) return;

        float passengerY = entity.getYHeadRot();

        if (passengerY > 180.0f) {
            passengerY -= 360.0f;
        } else if (passengerY < -180.0f) {
            passengerY += 360.0f;
        }

        float diffY = passengerY - this.getYRot();
        float diffX = entity.getXRot() - 0.2f - this.getXRot();
        if (diffY > 180.0f) {
            diffY -= 360.0f;
        } else if (diffY < -180.0f) {
            diffY += 360.0f;
        }
        diffY = Mth.clamp(diffY * 0.15f, -0.6f, 0.6f);
        diffX = diffX * 0.15f;

        this.setYRot(this.getYRot() + diffY);
        this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -2f, 2f), -45, 5.2f));
        this.setRot(this.getYRot(), this.getXRot());
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -45.0F, 5.2F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState movementPredicate(AnimationState<AnnihilatorEntity> event) {
        if (this.entityData.get(COOL_DOWN) > 88) {
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

}
