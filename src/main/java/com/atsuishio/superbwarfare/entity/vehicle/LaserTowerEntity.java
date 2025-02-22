package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.VectorTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class LaserTowerEntity extends EnergyVehicleEntity implements GeoEntity, OwnableEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(LaserTowerEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> TARGET_UUID = SynchedEntityData.defineId(LaserTowerEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(LaserTowerEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(LaserTowerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Float> LASER_LENGTH = SynchedEntityData.defineId(LaserTowerEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final float MAX_HEALTH = 100;
    public static final int MAX_ENERGY = 500000;
    public static final int SHOOT_COST = 5000;

    public LaserTowerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.LASER_TOWER.get(), world);
    }

    public LaserTowerEntity(EntityType<LaserTowerEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public LaserTowerEntity(LivingEntity owner, Level level) {
        super(ModEntities.LASER_TOWER.get(), level);
        this.setOwnerUUID(owner.getUUID());
    }

    public boolean isOwnedBy(LivingEntity pEntity) {
        return pEntity == this.getOwner();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_UUID, "none");
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(LASER_LENGTH, 0f);
        this.entityData.define(ACTIVE, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("CoolDown", this.entityData.get(COOL_DOWN));
        compound.putBoolean("Active", this.entityData.get(ACTIVE));
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COOL_DOWN, compound.getInt("CoolDown"));
        this.entityData.set(ACTIVE, compound.getBoolean("Active"));

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
    }

    public void setOwnerUUID(@javax.annotation.Nullable UUID pUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(pUuid));
    }

    @javax.annotation.Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 0.8, this.getZ(), 4, 0.1, 0.1, 0.1, 0.2, false);
        }
        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);

        amount = damageModifier.compute(source, amount);
        super.hurt(source, amount);
        this.hurt(amount, source.getEntity(), true);

        return true;
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .multiply(0.1f, DamageTypes.ARROW)
                .multiply(0.2f, DamageTypes.TRIDENT)
                .multiply(0.2f, DamageTypes.MOB_ATTACK)
                .multiply(0.2f, DamageTypes.MOB_ATTACK_NO_AGGRO)
                .multiply(0.4f, DamageTypes.MOB_PROJECTILE)
                .multiply(0.4f, DamageTypes.PLAYER_ATTACK)
                .multiply(1.5f, DamageTypes.EXPLOSION)
                .multiply(1.5f, DamageTypes.PLAYER_EXPLOSION)
                .multiply(0.5f, ModDamageTypes.CUSTOM_EXPLOSION)
                .multiply(0.5f, ModDamageTypes.PROJECTILE_BOOM)
                .multiply(0.5f, ModDamageTypes.MINE)
                .multiply(0.5f, ModDamageTypes.LUNGE_MINE)
                .multiply(0.6f, ModDamageTypes.CANNON_FIRE)
                .multiply(0.5f, ModTags.DamageTypes.PROJECTILE)
                .multiply(0.8f, ModTags.DamageTypes.PROJECTILE_ABSOLUTE)
                .multiply(2f, ModDamageTypes.VEHICLE_STRIKE)
                .reduce(1);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        if (player.isCrouching()) {
            if (stack.is(ModItems.CROWBAR.get()) && (getOwner() == null || player == getOwner())) {
                ItemStack container = ContainerBlockItem.createInstance(this);
                if (!player.addItem(container)) {
                    player.drop(container, false);
                }
                this.remove(RemovalReason.DISCARDED);
                this.discard();
                return InteractionResult.SUCCESS;
            } else if (!entityData.get(ACTIVE)) {
                entityData.set(ACTIVE, true);
                this.setOwnerUUID(player.getUUID());
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.5F, 1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide());
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
        autoAim();
    }

    @Override
    public void handleClientSync() {
        if (isControlledByLocalInstance()) {
            interpolationSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }
        if (interpolationSteps <= 0) {
            return;
        }

        double interpolatedYaw = Mth.wrapDegrees(serverYRot - (double) getYRot());
        setYRot(getYRot() + (float) interpolatedYaw / (float) interpolationSteps);
        setXRot(getXRot() + (float) (serverXRot - (double) getXRot()) / (float) interpolationSteps);
        setRot(getYRot(), getXRot());

    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        serverYRot = yaw;
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 10f,
                    this.getX(), this.getY(), this.getZ(), 3f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }
        this.discard();
    }

    public void autoAim() {
        if (this.getEnergy() <= 0 || !entityData.get(ACTIVE) || this.entityData.get(COOL_DOWN) > 30)
            return;

        if (entityData.get(TARGET_UUID).equals("none") && tickCount % 10 == 0) {
            Entity naerestEntity = seekNearLivingEntity(72);
            if (naerestEntity != null) {
                entityData.set(TARGET_UUID, naerestEntity.getStringUUID());
            }
        }

        Entity target = EntityFindUtil.findEntity(level(), entityData.get(TARGET_UUID));

        if (target != null) {
            if (target instanceof LivingEntity living && living.getHealth() <= 0) {
                entityData.set(TARGET_UUID, "none");
                return;
            }
            Vec3 barrelRootPos = new Vec3(this.getX(), this.getY() + 1.390625f, this.getZ());
            Vec3 targetVec = barrelRootPos.vectorTo(target.getEyePosition()).normalize();

            double d0 = targetVec.x;
            double d1 = targetVec.y;
            double d2 = targetVec.z;
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            this.setXRot(Mth.clamp(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))), -90, 40));
            float targetY = Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F);

            float diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(targetY - this.getYRot()));

            this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -60f, 60f));
            this.setRot(this.getYRot(), this.getXRot());

            if (this.entityData.get(COOL_DOWN) == 0 && VectorTool.calculateAngle(getViewVector(1), targetVec) < 1) {

                this.entityData.set(COOL_DOWN, 40);

                if (level() instanceof ServerLevel serverLevel) {
                    this.level().playSound(this, getOnPos(), ModSounds.LASER_TOWER_SHOOT.get(), SoundSource.PLAYERS, 2, random.nextFloat() * 0.1f + 1);
                    sendParticle(serverLevel, ParticleTypes.END_ROD, target.getX(), target.getEyeY(), target.getZ(), 12, 0, 0, 0, 0.05, true);
                    sendParticle(serverLevel, ParticleTypes.LAVA, target.getX(), target.getEyeY(), target.getZ(), 4, 0, 0, 0, 0.15, true);
                }

                target.hurt(ModDamageTypes.causeLaserStaticDamage(this.level().registryAccess(), this, this.getOwner()), (float) 15);
                target.invulnerableTime = 0;
                entityData.set(LASER_LENGTH, distanceTo(target));
                entityData.set(TARGET_UUID, "none");
                if (Math.random() < 0.25 && target instanceof LivingEntity living) {
                    living.setSecondsOnFire(2);
                }
                this.consumeEnergy(SHOOT_COST);
            }
        } else {
            entityData.set(TARGET_UUID, "none");
        }
    }

    public Entity seekNearLivingEntity(double seekRange) {
        return StreamSupport.stream(EntityFindUtil.getEntities(level()).getAll().spliterator(), false)
                .filter(e -> {
                    // TODO 自定义目标列表
                    if (e.distanceTo(this) <= seekRange && (
                            (e instanceof LivingEntity living && living instanceof Enemy && living.getHealth() > 0)
                    )) {
                        return level().clip(new ClipContext(this.getEyePosition(), e.getEyePosition(),
                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK;
                    }
                    return false;
                }).min(Comparator.comparingDouble(e -> e.distanceTo(this))).orElse(null);
    }

    private PlayState movementPredicate(AnimationState<LaserTowerEntity> event) {
        if (this.entityData.get(COOL_DOWN) > 10) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.lt.fire"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.lt.idle"));
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
}
