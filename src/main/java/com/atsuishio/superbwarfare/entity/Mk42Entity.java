package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.CannonShellEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.ammo.CannonShellItem;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
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
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;

public class Mk42Entity extends Entity implements GeoEntity, ICannonEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final float MAX_HEALTH = CannonConfig.MK42_HP.get();

    protected int interpolationSteps;
    protected double serverYRot;
    protected double serverXRot;

    public Mk42Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MK_42.get(), world);
    }

    public Mk42Entity(EntityType<Mk42Entity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(HEALTH, MAX_HEALTH);
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
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 2.16F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25;
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
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.5f * Math.max(amount - 5, 0));

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

        if (this.entityData.get(HEALTH) <= 0.4 * CannonConfig.MK42_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.25 * CannonConfig.MK42_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.15 * CannonConfig.MK42_HP.get()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 0.1 * CannonConfig.MK42_HP.get()) {
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

        travel();
        this.refreshDimensions();
    }

    private void destroy() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 130f,
                this.getX(), this.getY(), this.getZ(), 9.5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
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

            if (!(stack.getItem() instanceof CannonShellItem)) {
                return;
            }

            float hitDamage = 0;
            float explosionRadius = 0;
            float explosionDamage = 0;
            float fireProbability = 0;
            int fireTime = 0;
            int durability = 0;

            if (stack.is(ModItems.HE_5_INCHES.get())) {
                hitDamage = CannonConfig.MK42_HE_DAMAGE.get();
                explosionRadius = CannonConfig.MK42_HE_EXPLOSION_RADIUS.get();
                explosionDamage = CannonConfig.MK42_HE_EXPLOSION_DAMAGE.get();
                fireProbability = 0.18F;
                fireTime = 2;
                durability = 1;
            }

            if (stack.is(ModItems.AP_5_INCHES.get())) {
                hitDamage = CannonConfig.MK42_AP_DAMAGE.get();
                explosionRadius = CannonConfig.MK42_AP_EXPLOSION_RADIUS.get();
                explosionDamage = CannonConfig.MK42_AP_EXPLOSION_DAMAGE.get();
                fireProbability = 0;
                fireTime = 0;
                durability = 60;
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            CannonShellEntity entityToSpawn = new CannonShellEntity(ModEntities.CANNON_SHELL.get(),
                    player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime).durability(durability);

            entityToSpawn.setPos(this.getX(), this.getEyeY(), this.getZ());
            entityToSpawn.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 15, 0.05f);
            level.addFreshEntity(entityToSpawn);

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_FIRE_1P.get(), 2, 1);
                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_RELOAD.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.entityData.set(COOL_DOWN, 30);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 5 * this.getLookAngle().x,
                    this.getY(),
                    this.getZ() + 5 * this.getLookAngle().z,
                    100, 7, 0.02, 7, 0.005);

            double x = this.getX() + 9 * this.getLookAngle().x;
            double y = this.getEyeY() + 9 * this.getLookAngle().y;
            double z = this.getZ() + 9 * this.getLookAngle().z;

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);
            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);

            int count = 6;

            for (float i = 9.5f; i < 16; i += .5f) {
                server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        this.getX() + i * this.getLookAngle().x,
                        this.getEyeY() + i * this.getLookAngle().y,
                        this.getZ() + i * this.getLookAngle().z,
                        Mth.clamp(count--, 1, 5), 0.15, 0.15, 0.15, 0.0025);
            }

            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(20), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {

                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(15, 15, 45, this.getX(), this.getEyeY(), this.getZ()));
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
        float diffX = entity.getXRot() - 1.3f - this.getXRot();
        if (diffY > 180.0f) {
            diffY -= 360.0f;
        } else if (diffY < -180.0f) {
            diffY += 360.0f;
        }
        diffY = Mth.clamp(diffY * 0.15f, -1.75f, 1.75f);
        diffX = diffX * 0.15f;

        this.setYRot(this.getYRot() + diffY);
        this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -3f, 3f), -85, 16.3f));
        this.setRot(this.getYRot(), this.getXRot());
    }

    protected void clampRotation(Entity entity) {

        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -85.0F, 16.3F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState movementPredicate(AnimationState<Mk42Entity> event) {
        if (this.entityData.get(COOL_DOWN) > 10) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mk42.idle"));
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
