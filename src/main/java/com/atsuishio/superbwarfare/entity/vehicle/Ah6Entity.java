package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.projectile.FlareDecoyEntity;
import com.atsuishio.superbwarfare.entity.projectile.HeliRocketEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
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

public class Ah6Entity extends ContainerMobileEntity implements GeoEntity, IHelicopterEntity, MultiWeaponVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = VehicleConfig.AH_6_HP.get();
    public static final int MAX_ENERGY = VehicleConfig.AH_6_MAX_ENERGY.get();

    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> PROPELLER_ROT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> WEAPON_TYPE = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DECOY_COUNT = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LOADED_ROCKET = SynchedEntityData.defineId(Ah6Entity.class, EntityDataSerializers.INT);

    public boolean engineStart;
    public boolean engineStartOver;
    public float propellerRot;
    public float propellerRotO;

    public double velocity;
    public int reloadCoolDown;
    public int decoyReloadCoolDown;
    public int fireIndex;
    public boolean cannotFire;
    public int heat;

    public int holdTick;
    public int holdPowerTick;

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
        this.entityData.define(AMMO, 0);
        this.entityData.define(LOADED_ROCKET, 0);
        this.entityData.define(DELTA_ROT, 0f);
        this.entityData.define(WEAPON_TYPE, 0);
        this.entityData.define(PROPELLER_ROT, 0f);
        this.entityData.define(DECOY_COUNT, 6);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LoadedRocket", this.entityData.get(LOADED_ROCKET));
        compound.putFloat("PropellerRot", this.entityData.get(PROPELLER_ROT));
        compound.putInt("DecoyCount", this.entityData.get(DECOY_COUNT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(LOADED_ROCKET, compound.getInt("LoadedRocket"));
        this.entityData.set(PROPELLER_ROT, compound.getFloat("PropellerRot"));
        this.entityData.set(DECOY_COUNT, compound.getInt("DecoyCount"));
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

        if (source.is(DamageTypes.ARROW)) {
            amount *= 0.1f;
        }
        if (source.is(DamageTypes.TRIDENT)) {
            amount *= 0.2f;
        }
        if (source.is(DamageTypes.MOB_ATTACK)) {
            amount *= 0.2f;
        }
        if (source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
            amount *= 0.2f;
        }
        if (source.is(DamageTypes.MOB_PROJECTILE)) {
            amount *= 0.2f;
        }
        if (source.is(DamageTypes.PLAYER_ATTACK)) {
            amount *= 0.2f;
        }
        if (source.is(DamageTypes.LAVA)) {
            amount *= 2f;
        }
        if (source.is(DamageTypes.EXPLOSION)) {
            amount *= 2f;
        }
        if (source.is(DamageTypes.PLAYER_EXPLOSION)) {
            amount *= 2f;
        }

        if (source.is(ModDamageTypes.CUSTOM_EXPLOSION)) {
            amount *= 1f;
        }
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            amount *= 1f;
        }
        if (source.is(ModDamageTypes.MINE)) {
            amount *= 0.5f;
        }
        if (source.is(ModDamageTypes.LUNGE_MINE)) {
            amount *= 0.5f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 0.4f;
        }
        if (source.is(ModTags.DamageTypes.PROJECTILE)) {
            amount *= 0.08f;
        }
        if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            amount *= 0.5f;
        }
        if (source.is(ModDamageTypes.VEHICLE_STRIKE)) {
            amount *= 5f;
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(Math.max(amount - 2, 0), source.getEntity(), true);
        return true;
    }

    @Override
    public void baseTick() {
        propellerRotO = this.getPropellerRot();
        super.baseTick();

        setZRot(getRoll() * 0.99f);

        if (heat > 0) {
            heat--;
        }

        if (heat < 40) {
            cannotFire = false;
        }

        Entity driver = this.getFirstPassenger();
        if (driver instanceof Player player) {
            if (heat > 100) {
                cannotFire = true;
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 1f, 1f);
                }
            }
        }

        if (this.level() instanceof ServerLevel) {
            if (reloadCoolDown > 0) {
                reloadCoolDown--;
            }
            if (decoyReloadCoolDown > 0) {
                decoyReloadCoolDown--;
            }
            if (this.getFirstPassenger() instanceof Player player) {
                if ((this.getItemStacks().stream().filter(stack -> stack.is(ModItems.ROCKET_70.get())).mapToInt(ItemStack::getCount).sum() > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) && reloadCoolDown == 0 && this.getEntityData().get(LOADED_ROCKET) < 14) {
                    this.entityData.set(LOADED_ROCKET, this.getEntityData().get(LOADED_ROCKET) + 1);
                    reloadCoolDown = 25;
                    if (!player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                        this.getItemStacks().stream().filter(stack -> stack.is(ModItems.ROCKET_70.get())).findFirst().ifPresent(stack -> stack.shrink(1));
                    }
                    this.level().playSound(null, this, ModSounds.MISSILE_RELOAD.get(), this.getSoundSource(), 1, 1);
                }
            }

            if (this.getEntityData().get(WEAPON_TYPE) == 0) {
                this.entityData.set(AMMO, this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).mapToInt(ItemStack::getCount).sum());
            } else {
                this.entityData.set(AMMO, this.getEntityData().get(LOADED_ROCKET));
            }
        }

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 1, 0.8));
            this.setZRot(this.roll * 0.9f);
            this.setXRot(this.getXRot() * 0.9f);
        } else {
            float f = (float) Mth.clamp(0.9f - 0.015 * getDeltaMovement().length() + 0.02f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).scale((this.getXRot() < 0 ? -0.035 : (this.getXRot() > 0 ? 0.035 : 0)) * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.95, f));
        }

        if (this.isInWater() && this.tickCount % 4 == 0 && getSubmergedHeight(this) > 0.5 * getBbHeight()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), 1 + (float) (20 * ((lastTickSpeed - 0.4) * (lastTickSpeed - 0.4))));
        }

        releaseDecoy();
        lowHealthWarning();

        this.refreshDimensions();
    }

    public void releaseDecoy() {
        if (decoyInputDown) {
            if (this.entityData.get(DECOY_COUNT) > 0 && this.level() instanceof ServerLevel) {
                Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
                for (int i = 0; i < 4; i++) {
                    FlareDecoyEntity flareDecoyEntity = new FlareDecoyEntity((LivingEntity) passenger, this.level());
                    flareDecoyEntity.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + 0.5 + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
                    flareDecoyEntity.decoyShoot(this, this.getViewVector(1).yRot((45 + 90 * i) * Mth.DEG_TO_RAD), 0.8f, 8);
                    this.level().addFreshEntity(flareDecoyEntity);
                }
                this.level().playSound(null, this, ModSounds.DECOY_FIRE.get(), this.getSoundSource(), 1, 1);
                if (this.getEntityData().get(DECOY_COUNT) == 6) {
                    decoyReloadCoolDown = 300;
                }
                this.getEntityData().set(DECOY_COUNT, this.getEntityData().get(DECOY_COUNT) - 1);
            }
            decoyInputDown = false;
        }
        if (this.entityData.get(DECOY_COUNT) < 6 && decoyReloadCoolDown == 0 && this.level() instanceof ServerLevel) {
            this.entityData.set(DECOY_COUNT, this.entityData.get(DECOY_COUNT) + 1);
            this.level().playSound(null, this, ModSounds.DECOY_RELOAD.get(), this.getSoundSource(), 1, 1);
            decoyReloadCoolDown = 300;
        }
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
        } else if (passenger instanceof Player) {
            diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
            diffX = Math.clamp(-60f, 60f, Mth.wrapDegrees(passenger.getXRot() - this.getXRot()));

            if (rightInputDown) {
                holdTick++;
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 2f * Math.min(holdTick, 7) * this.entityData.get(POWER));
            } else if (this.leftInputDown) {
                holdTick++;
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 2f * Math.min(holdTick, 7) * this.entityData.get(POWER));
            } else {
                holdTick = 0;
            }

            this.setYRot(this.getYRot() + Mth.clamp((this.onGround() ? 0.1f : 2f) * diffY * this.entityData.get(PROPELLER_ROT), -10f, 10f));
            this.setXRot(Mth.clamp(this.getXRot() + ((this.onGround()) ? 0 : 1.5f) * diffX * this.entityData.get(PROPELLER_ROT), -80, 80));
            this.setZRot(this.getRoll() - this.entityData.get(DELTA_ROT) + (this.onGround() ? 0 : 0.25f) * diffY * this.entityData.get(PROPELLER_ROT));
        }

        if (this.level() instanceof ServerLevel) {
            if (this.getEnergy() > 0) {
                boolean up = upInputDown || forwardInputDown;
                boolean down = this.downInputDown;

                if (!engineStart && up) {
                    engineStart = true;
                    this.level().playSound(null, this, ModSounds.HELICOPTER_ENGINE_START.get(), this.getSoundSource(), 3, 1);
                }

                if (up && engineStartOver) {
                    holdPowerTick++;
                    this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.0007f * Math.min(holdPowerTick, 10), 0.12f));
                }

                if (engineStartOver) {
                    if (down) {
                        holdPowerTick++;
                        this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.0004f * Math.min(holdPowerTick, 10), this.onGround() ? 0 : 0.01f));
                    } else if (backInputDown) {
                        holdPowerTick++;
                        this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.0004f * Math.min(holdPowerTick, 10), this.onGround() ? 0 : 0.052f));
                        if (passenger != null) {
                            passenger.setXRot(0.8f * passenger.getXRot());
                        }
                    }
                }

                if (engineStart && !engineStartOver) {
                    this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.0012f, 0.045f));
                }

                if (!(up || down || backInputDown) && engineStartOver) {
                    if (this.getDeltaMovement().y() < 0) {
                        this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.0002f, 0.12f));
                    } else {
                        this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - (this.onGround() ? 0.00005f : 0.0002f), 0));
                    }
                    holdPowerTick = 0;
                }
            } else {
                this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.0001f, 0));
                this.forwardInputDown = false;
                this.backInputDown = false;
                engineStart = false;
                engineStartOver = false;
            }
        }

        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.9f);
        this.entityData.set(PROPELLER_ROT, Mth.lerp(0.18f, this.entityData.get(PROPELLER_ROT), this.entityData.get(POWER)));
        this.setPropellerRot(this.getPropellerRot() + 30 * this.entityData.get(PROPELLER_ROT));
        this.entityData.set(PROPELLER_ROT, this.entityData.get(PROPELLER_ROT) * 0.9995f);

        if (engineStart) {
            this.consumeEnergy((int) (VehicleConfig.AH_6_MIN_ENERGY_COST.get() + this.entityData.get(POWER) * ((VehicleConfig.AH_6_MAX_ENERGY_COST.get() - VehicleConfig.AH_6_MIN_ENERGY_COST.get()) / 0.12)));
        }

        setDeltaMovement(getDeltaMovement().add(0.0f, Math.min(Math.sin((90 - this.getXRot()) * Mth.DEG_TO_RAD), Math.sin((90 + this.getRoll()) * Mth.DEG_TO_RAD)) * this.entityData.get(POWER), 0.0f));

        Vector3f direction = getRightDirection().mul(-Math.sin(this.getRoll() * Mth.DEG_TO_RAD) * this.entityData.get(PROPELLER_ROT));
        setDeltaMovement(getDeltaMovement().add(new Vec3(direction.x, direction.y, direction.z).scale(backInputDown ? 0.1 : 2.5)));

//        if (passenger instanceof Player player) {
//            player.displayClientMessage(Component.literal(this.getRoll() + ""), true);
//        }

        Vector3f directionZ = getForwardDirection().mul(-Math.cos((this.getXRot() + 90) * Mth.DEG_TO_RAD) * this.entityData.get(PROPELLER_ROT));
        setDeltaMovement(getDeltaMovement().add(new Vec3(directionZ.x, directionZ.y, directionZ.z).scale(backInputDown ? 0.1 : 2)));

        if (this.entityData.get(POWER) > 0.04f) {
            engineStartOver = true;
        }

        if (this.entityData.get(POWER) < 0.0004f) {
            engineStart = false;
            engineStartOver = false;
        }
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

        if (passenger != this.getFirstPassenger()) {
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

    public int getMaxPassengers() {
        return 2;
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        if (this.crash) {
            List<Entity> passengers = this.getPassengers();
            for (var entity : passengers) {
                if (entity instanceof LivingEntity living) {
                    var tempAttacker = living == attacker ? null : attacker;

                    living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                }
            }
        } else {
            List<Entity> passengers = this.getPassengers();
            for (var entity : passengers) {
                if (entity instanceof LivingEntity living) {
                    var tempAttacker = living == attacker ? null : attacker;

                    living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                    living.invulnerableTime = 0;
                    living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                }
            }
        }

        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, attacker), 300.0f,
                    this.getX(), this.getY(), this.getZ(), 8f, ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());
        }

        this.discard();
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
            if (this.cannotFire) return;

            x = 1.15f;
            y = 0.62f;
            z = 0.8f;

            worldPositionRight = transformPosition(transform, -x, y, z);
            worldPositionLeft = transformPosition(transform, x, y, z);

            if (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                ProjectileEntity projectileRight = new ProjectileEntity(player.level())
                        .shooter(player)
                        .damage(VehicleConfig.AH_6_CANNON_DAMAGE.get())
                        .headShot(2f)
                        .zoom(false)
                        .heBullet(2)
                        .bypassArmorRate(0.2f);

                projectileRight.setPos(worldPositionRight.x, worldPositionRight.y, worldPositionRight.z);
                projectileRight.shoot(player, this.getLookAngle().x, this.getLookAngle().y + 0.018, this.getLookAngle().z, 20,
                        (float) 0.2);
                this.level().addFreshEntity(projectileRight);
                sendParticle((ServerLevel) this.level(), ParticleTypes.LARGE_SMOKE, worldPositionRight.x, worldPositionRight.y, worldPositionRight.z, 1, 0, 0, 0, 0, false);
                if (!player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                    this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
                }
            }

            if (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                ProjectileEntity projectileLeft = new ProjectileEntity(player.level())
                        .shooter(player)
                        .damage(VehicleConfig.AH_6_CANNON_DAMAGE.get())
                        .headShot(2f)
                        .zoom(false)
                        .heBullet(2)
                        .bypassArmorRate(0.2f);

                projectileLeft.setPos(worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z);
                projectileLeft.shoot(player, this.getLookAngle().x, this.getLookAngle().y + 0.018, this.getLookAngle().z, 20,
                        (float) 0.2);
                this.level().addFreshEntity(projectileLeft);
                sendParticle((ServerLevel) this.level(), ParticleTypes.LARGE_SMOKE, worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z, 1, 0, 0, 0, 0, false);
                if (!player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                    this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
                }
            }

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_FIRE_3P.get(), 4, 1);
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_FAR.get(), 12, 1);
                    serverPlayer.playSound(ModSounds.HELICOPTER_CANNON_VERYFAR.get(), 24, 1);
                }
            }

            Level level = player.level();
            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 7, this.getX(), this.getEyeY(), this.getZ()));
                }
            }
        } else if (entityData.get(WEAPON_TYPE) == 1 && this.getEntityData().get(LOADED_ROCKET) > 0) {
            x = 1.7f;
            y = 0.62f;
            z = 0.8f;

            worldPositionRight = transformPosition(transform, -x, y, z);
            worldPositionLeft = transformPosition(transform, x, y, z);

            if (fireIndex == 0) {
                HeliRocketEntity heliRocketEntityRight = new HeliRocketEntity(player, player.level(),
                        VehicleConfig.AH_6_ROCKET_DAMAGE.get(),
                        VehicleConfig.AH_6_ROCKET_EXPLOSION_DAMAGE.get(),
                        VehicleConfig.AH_6_ROCKET_EXPLOSION_RADIUS.get());

                heliRocketEntityRight.setPos(worldPositionRight.x, worldPositionRight.y, worldPositionRight.z);
                heliRocketEntityRight.shoot(this.getLookAngle().x, this.getLookAngle().y + 0.008, this.getLookAngle().z, 7, 0.25f);
                player.level().addFreshEntity(heliRocketEntityRight);
                fireIndex = 1;
            } else if (fireIndex == 1) {
                HeliRocketEntity heliRocketEntityLeft = new HeliRocketEntity(player, player.level(),
                        VehicleConfig.AH_6_ROCKET_DAMAGE.get(),
                        VehicleConfig.AH_6_ROCKET_EXPLOSION_DAMAGE.get(),
                        VehicleConfig.AH_6_ROCKET_EXPLOSION_RADIUS.get());

                heliRocketEntityLeft.setPos(worldPositionLeft.x, worldPositionLeft.y, worldPositionLeft.z);
                heliRocketEntityLeft.shoot(this.getLookAngle().x, this.getLookAngle().y + 0.008, this.getLookAngle().z, 7, 0.25f);
                player.level().addFreshEntity(heliRocketEntityLeft);
                fireIndex = 0;
            }

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.HELICOPTER_ROCKET_FIRE_3P.get(), 6, 1);
                }
            }

            this.entityData.set(LOADED_ROCKET, this.getEntityData().get(LOADED_ROCKET) - 1);
            reloadCoolDown = 30;

            Level level = player.level();
            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 7, this.getX(), this.getEyeY(), this.getZ()));
                }
            }
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3d = getDismountOffset(getBbWidth() * Mth.SQRT_OF_TWO, passenger.getBbWidth() * Mth.SQRT_OF_TWO);
        double ox = getX() + vec3d.x;
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
        return 360;
    }

    @Override
    public boolean canShoot(Player player) {
        if (entityData.get(WEAPON_TYPE) == 0) {
            return (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) && !cannotFire;
        } else if (entityData.get(WEAPON_TYPE) == 1) {
            return this.entityData.get(AMMO) > 0;
        }
        return false;
    }

    @Override
    public int getAmmoCount(Player player) {
        return this.entityData.get(AMMO);
    }

    @Override
    public boolean banHand(Player player) {
        return player == this.getFirstPassenger();
    }

    @Override
    public boolean hidePassenger() {
        return false;
    }

    @Override
    public int zoomFov() {
        return 3;
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
    public int getDecoy() {
        return this.entityData.get(DECOY_COUNT);
    }

    @Override
    public void changeWeapon(int scroll) {
        var type = (entityData.get(WEAPON_TYPE) + scroll + 2) % 2;
        entityData.set(WEAPON_TYPE, type);

        var sound = switch (type) {
            case 0 -> ModSounds.INTO_MISSILE.get();
            case 1 -> ModSounds.INTO_CANNON.get();
            default -> throw new IllegalStateException("Unexpected type: " + type);
        };

        this.level().playSound(null, this, sound, this.getSoundSource(), 1, 1);
    }

    @Override
    public int getWeaponType() {
        return entityData.get(WEAPON_TYPE);
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/ah_6_icon.png");
    }
    @Override
    public boolean allowFreeCam() {
        return true;
    }
}
