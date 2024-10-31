package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.entity.projectile.CannonShellEntity;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.item.common.ammo.CannonShellItem;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.mcreator.superbwarfare.tools.ParticleTool.sendParticle;

public class Mk42Entity extends Entity implements GeoEntity, ICannonEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected int interpolationSteps;
    protected double serverYRot;
    protected double serverXRot;

    public Mk42Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MK_42.get(), world);
    }

    public Mk42Entity(EntityType<Mk42Entity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(TYPE, 0);
        this.entityData.define(HEALTH, 500f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("CoolDown", this.entityData.get(COOL_DOWN));
        compound.putInt("Type", this.entityData.get(TYPE));
        compound.putFloat("Health", this.entityData.get(HEALTH));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(COOL_DOWN, compound.getInt("CoolDown"));
        this.entityData.set(TYPE, compound.getInt("Type"));
        this.entityData.set(HEALTH, compound.getFloat("Health"));
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
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 2.5, this.getZ(), 4, 0.2, 0.2, 0.2, 0.2, false);
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
        if (amount < 32) {
            return false;
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.5f * amount);

        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.CROWBAR.get() && this.getFirstPassenger() == null) {
            this.discard();
        } else {
            player.setXRot(this.getXRot());
            player.setYRot(this.getYRot());
            player.startRiding(this);
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.entityData.get(COOL_DOWN) > 0) {
            this.entityData.set(COOL_DOWN, this.entityData.get(COOL_DOWN) - 1);
        }

        if (this.entityData.get(COOL_DOWN) > 28) {
            if (Math.random() < 0.5) {
                this.entityData.set(TYPE, -1);
            } else {
                this.entityData.set(TYPE, 1);
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        if (this.entityData.get(HEALTH) <= 300) {
            if (this.level() instanceof ServerLevel serverLevel) {
                sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 200) {
            if (this.level() instanceof ServerLevel serverLevel) {
                sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 150) {
            if (this.level() instanceof ServerLevel serverLevel) {
                sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
                sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 1, 0.75, 0.5, 0.75, 0.01, false);
            }
        }

        if (this.entityData.get(HEALTH) <= 100) {
            if (this.level() instanceof ServerLevel serverLevel) {
                sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 2.5, this.getZ(), 2, 0.75, 0.5, 0.75, 0.01, false);
                sendParticle(serverLevel, ParticleTypes.FLAME, this.getX(), this.getY() + 3.2, this.getZ(), 4, 0.6, 0.1, 0.6, 0.05, false);
                sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 3, this.getZ(), 4, 0.1, 0.1, 0.1, 0.4, false);
            }
            this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.1f);
        } else {
            this.entityData.set(HEALTH, this.entityData.get(HEALTH) + 0.05f);
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
                this.getX(), this.getY(), this.getZ(), 9.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
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
                hitDamage = 180;
                explosionRadius = 10;
                explosionDamage = 220;
                fireProbability = 0.18F;
                fireTime = 5;
            }

            if (stack.is(ModItems.AP_5_INCHES.get())) {
                hitDamage = 360;
                explosionRadius = 3;
                explosionDamage = 100;
                fireProbability = 0;
                fireTime = 0;
                durability = 25;
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
        ItemStack stack = entity.getMainHandItem();

        if (!stack.isEmpty() && this.isVehicle() && !stack.is(ModTags.Items.GUN)) {
            float diffY = entity.getYHeadRot() - this.getYRot();
            float diffX = entity.getXRot() - 1.3f - this.getXRot();
            if (diffY > 180.0f) {
                diffY -= 360.0f;
            } else if (diffY < -180.0f) {
                diffY += 360.0f;
            }
            diffY = diffY * 0.15f;
            diffX = diffX * 0.15f;

            this.setYRot(this.getYRot() + Mth.clamp(diffY, -1.75f, 1.75f));
            this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -3f, 3f), -85, 15));
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    protected void clampRotation(Entity entity) {
        ItemStack stack = ItemStack.EMPTY;
        if (entity instanceof Player player) {
            stack = player.getMainHandItem();
        }

        if (!stack.is(ModTags.Items.GUN)) {
            float f = Mth.wrapDegrees(entity.getXRot());
            float f1 = Mth.clamp(f, -85.0F, 15.0F);
            entity.xRotO += f1 - f;
            entity.setXRot(entity.getXRot() + f1 - f);
        }
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState movementPredicate(AnimationState<Mk42Entity> event) {
        if (this.entityData.get(COOL_DOWN) > 0) {
            if (this.entityData.get(TYPE) == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire2"));
            }
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
