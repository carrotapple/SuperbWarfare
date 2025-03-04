package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.projectile.SmallCannonShellEntity;
import com.atsuishio.superbwarfare.entity.projectile.WgMissileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.*;
import com.mojang.math.Axis;
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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class Bmp2Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity {

    public static final EntityDataAccessor<Integer> FIRE_ANIM = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> HEAT = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> COAX_HEAT = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LOADED_MISSILE = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> WEAPON_TYPE = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> TRACK_L = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TRACK_R = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.FLOAT);

    public static final float MAX_HEALTH = VehicleConfig.BMP_2_HP.get();
    public static final int MAX_ENERGY = VehicleConfig.BMP_2_MAX_ENERGY.get();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public float turretYRot;
    public float turretXRot;
    public float turretYRotO;
    public float turretXRotO;
    public float leftWheelRot;
    public float rightWheelRot;
    public float leftWheelRotO;
    public float rightWheelRotO;
    public boolean cannotFire;
    public boolean cannotFireCoax;
    public int reloadCoolDown;

    public Bmp2Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.BMP_2.get(), world);
    }

    public Bmp2Entity(EntityType<Bmp2Entity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.5f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AMMO, 0);
        this.entityData.define(FIRE_ANIM, 0);
        this.entityData.define(DELTA_ROT, 0f);
        this.entityData.define(HEAT, 0);
        this.entityData.define(COAX_HEAT, 0);
        this.entityData.define(WEAPON_TYPE, 0);
        this.entityData.define(LOADED_MISSILE, 0);
        this.entityData.define(TRACK_L, 0f);
        this.entityData.define(TRACK_R, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LoadedMissile", this.entityData.get(LOADED_MISSILE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(LOADED_MISSILE, compound.getInt("LoadedMissile"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .multiply(0.3f, DamageTypes.ARROW)
                .multiply(0.3f, DamageTypes.TRIDENT)
                .multiply(0.5f, DamageTypes.MOB_ATTACK)
                .multiply(0.4f, DamageTypes.MOB_ATTACK_NO_AGGRO)
                .multiply(0.3f, DamageTypes.MOB_PROJECTILE)
                .multiply(0.2f, DamageTypes.PLAYER_ATTACK)
                .multiply(2.5f, DamageTypes.LAVA)
                .multiply(1.2f, DamageTypes.EXPLOSION)
                .multiply(1.2f, DamageTypes.PLAYER_EXPLOSION)
                .multiply(0.4f, ModDamageTypes.CUSTOM_EXPLOSION)
                .multiply(0.4f, ModDamageTypes.PROJECTILE_BOOM)
                .multiply(0.14f, ModDamageTypes.MINE)
                .multiply(0.18f, ModDamageTypes.LUNGE_MINE)
                .multiply(0.3f, ModDamageTypes.CANNON_FIRE)
                .multiply(0.02f, ModTags.DamageTypes.PROJECTILE)
                .multiply(0.14f, ModTags.DamageTypes.PROJECTILE_ABSOLUTE)
                .multiply(1.7f, ModDamageTypes.VEHICLE_STRIKE)
                .reduce(8);
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound(ModSounds.BMP_STEP.get(), Mth.abs(this.entityData.get(POWER)) * 8, random.nextFloat() * 0.15f + 1f);
    }

    @Override
    public double getSubmergedHeight(Entity entity) {
        return super.getSubmergedHeight(entity);
    }

    @Override
    public void baseTick() {
        turretYRotO = this.getTurretYRot();
        turretXRotO = this.getTurretXRot();
        leftWheelRotO = this.getLeftWheelRot();
        rightWheelRotO = this.getRightWheelRot();

        super.baseTick();

        if (this.entityData.get(TRACK_R) < 0) {
            this.entityData.set(TRACK_R, 100f);
        }

        if (this.entityData.get(TRACK_R) > 100) {
            this.entityData.set(TRACK_R, 0f);
        }

        if (this.entityData.get(TRACK_L) < 0) {
            this.entityData.set(TRACK_L, 100f);
        }

        if (this.entityData.get(TRACK_L) > 100) {
            this.entityData.set(TRACK_L, 0f);
        }

        if (this.entityData.get(HEAT) > 0) {
            this.entityData.set(HEAT, this.entityData.get(HEAT) - 1);
        }

        if (this.entityData.get(FIRE_ANIM) > 0) {
            this.entityData.set(FIRE_ANIM, this.entityData.get(FIRE_ANIM) - 1);
        }

        if (this.entityData.get(HEAT) < 40) {
            cannotFire = false;
        }

        if (this.entityData.get(COAX_HEAT) > 0) {
            this.entityData.set(COAX_HEAT, this.entityData.get(COAX_HEAT) - 1);
        }

        if (this.entityData.get(COAX_HEAT) < 40) {
            cannotFireCoax = false;
        }

        if (this.level() instanceof ServerLevel) {
            if (reloadCoolDown > 0) {
                reloadCoolDown--;
            }
            this.handleAmmo();
        }

        Entity driver = this.getFirstPassenger();
        if (driver instanceof Player player) {
            if (this.entityData.get(HEAT) > 100) {
                cannotFire = true;
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 1f, 1f);
                }
            }
            if (this.entityData.get(COAX_HEAT) > 100) {
                cannotFireCoax = true;
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 1f, 1f);
                }
            }
        }

        double fluidFloat;
        fluidFloat = 0.052 * getSubmergedHeight(this);
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, fluidFloat, 0.0));

        if (this.onGround()) {
            float f0 = 0.54f + 0.25f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.05 * this.getDeltaMovement().horizontalDistance())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f0, 0.85, f0));
        } else if (this.isInWater()) {
            float f1 = 0.61f + 0.08f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.04 * this.getDeltaMovement().horizontalDistance())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f1, 0.85, f1));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.95, 0.99));
        }

        if (this.level() instanceof ServerLevel serverLevel && this.isInWater() && this.getDeltaMovement().length() > 0.1) {
            sendParticle(serverLevel, ParticleTypes.CLOUD, this.getX() + 0.5 * this.getDeltaMovement().x, this.getY() + getSubmergedHeight(this) - 0.2, this.getZ() + 0.5 * this.getDeltaMovement().z, (int) (2 + 4 * this.getDeltaMovement().length()), 0.65, 0, 0.65, 0, true);
            sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP, this.getX() + 0.5 * this.getDeltaMovement().x, this.getY() + getSubmergedHeight(this) - 0.2, this.getZ() + 0.5 * this.getDeltaMovement().z, (int) (2 + 10 * this.getDeltaMovement().length()), 0.65, 0, 0.65, 0, true);
        }

        float deltaT = Math.abs(getTurretYRot() - turretYRotO);
        while (getTurretYRot() > 180F) {
            setTurretYRot(getTurretYRot() - 360F);
            turretYRotO = getTurretYRot() - deltaT;
        }
        while (getTurretYRot() <= -180F) {
            setTurretYRot(getTurretYRot() + 360F);
            turretYRotO = deltaT + getTurretYRot();
        }

        collideBlock();
        if (this.getDeltaMovement().length() > 0.15) {
            collideHardBlock();
        }

        gunnerAngle();
        lowHealthWarning();
        this.refreshDimensions();
    }

    private void handleAmmo() {
        if (!(this.getFirstPassenger() instanceof Player player)) return;

        int ammoCount = this.getItemStacks().stream().filter(stack -> {
            if (stack.is(ModItems.AMMO_BOX.get())) {
                return AmmoType.RIFLE.get(stack) > 0;
            }
            return false;
        }).mapToInt(AmmoType.RIFLE::get).sum() + countItem(ModItems.RIFLE_AMMO.get());

        if ((countItem(ModItems.WIRE_GUIDE_MISSILE.get()) > 0
                || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())))
                && this.reloadCoolDown <= 0 && this.getEntityData().get(LOADED_MISSILE) < 1) {
            this.entityData.set(LOADED_MISSILE, this.getEntityData().get(LOADED_MISSILE) + 1);
            this.reloadCoolDown = 160;
            if (!player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {
                this.getItemStacks().stream().filter(stack -> stack.is(ModItems.WIRE_GUIDE_MISSILE.get())).findFirst().ifPresent(stack -> stack.shrink(1));
            }
            this.level().playSound(null, this, ModSounds.BMP_MISSILE_RELOAD.get(), this.getSoundSource(), 1, 1);
        }

        if (getWeaponType(0) == 0) {
            this.entityData.set(AMMO, countItem(ModItems.SMALL_SHELL.get()));
        } else if (getWeaponType(0) == 1) {
            this.entityData.set(AMMO, ammoCount);
        } else {
            this.entityData.set(AMMO, countItem(ModItems.WIRE_GUIDE_MISSILE.get()));
        }
    }

    @Override
    public void move(@NotNull MoverType movementType, @NotNull Vec3 movement) {
        super.move(movementType, movement);
        if (this.isInWater() && horizontalCollision) {
            setDeltaMovement(this.getDeltaMovement().add(0, 0.07, 0));
        }
    }

    @Override
    public void vehicleShoot(Player player) {
        Matrix4f transform = getBarrelTransform();
        if (getWeaponType(0) == 0) {
            if (this.cannotFire) return;
            float x = -0.1125f;
            float y = 0.174025f;
            float z = 4.2f;

            Vector4f worldPosition = transformPosition(transform, x, y, z);
            SmallCannonShellEntity smallCannonShell = new SmallCannonShellEntity(player, this.level(),
                    VehicleConfig.BMP_2_CANNON_DAMAGE.get(),
                    VehicleConfig.BMP_2_CANNON_EXPLOSION_DAMAGE.get(),
                    VehicleConfig.BMP_2_CANNON_EXPLOSION_RADIUS.get().floatValue());

            smallCannonShell.setPos(worldPosition.x - 1.1 * this.getDeltaMovement().x, worldPosition.y, worldPosition.z - 1.1 * this.getDeltaMovement().z);
            smallCannonShell.shoot(getBarrelVector(1).x, getBarrelVector(1).y + 0.005f, getBarrelVector(1).z, 20,
                    0.25f);
            this.level().addFreshEntity(smallCannonShell);

            sendParticle((ServerLevel) this.level(), ParticleTypes.LARGE_SMOKE, worldPosition.x - 1.1 * this.getDeltaMovement().x, worldPosition.y, worldPosition.z - 1.1 * this.getDeltaMovement().z, 1, 0.02, 0.02, 0.02, 0, false);

            float pitch = this.entityData.get(HEAT) <= 60 ? 1 : (float) (1 - 0.011 * Math.abs(60 - this.entityData.get(HEAT)));

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.BMP_CANNON_FIRE_3P.get(), 4, pitch);
                    serverPlayer.playSound(ModSounds.LAV_CANNON_FAR.get(), 12, pitch);
                    serverPlayer.playSound(ModSounds.LAV_CANNON_VERYFAR.get(), 24, pitch);
                }
            }

            Level level = player.level();
            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 9, this.getX(), this.getEyeY(), this.getZ()));
                }
            }

            this.entityData.set(HEAT, this.entityData.get(HEAT) + 7);
            this.entityData.set(FIRE_ANIM, 3);
            this.getItemStacks().stream().filter(stack -> stack.is(ModItems.SMALL_SHELL.get())).findFirst().ifPresent(stack -> stack.shrink(1));
        } else if (getWeaponType(0) == 1) {
            if (this.cannotFireCoax) return;
            float x = 0.1125f;
            float y = 0.174025f;
            float z = 2f;

            Vector4f worldPosition = transformPosition(transform, x, y, z);
            boolean hasCreativeAmmo = player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()));

            if (this.entityData.get(AMMO) > 0 || hasCreativeAmmo) {
                ProjectileEntity projectileRight = new ProjectileEntity(player.level())
                        .shooter(player)
                        .damage(9.5f)
                        .headShot(2f)
                        .zoom(false);

                projectileRight.bypassArmorRate(0.2f);
                projectileRight.setPos(worldPosition.x - 1.1 * this.getDeltaMovement().x, worldPosition.y, worldPosition.z - 1.1 * this.getDeltaMovement().z);
                projectileRight.shoot(player, getBarrelVector(1).x, getBarrelVector(1).y + 0.002f, getBarrelVector(1).z, 36,
                        0.25f);
                this.level().addFreshEntity(projectileRight);

                if (!hasCreativeAmmo) {
                    ItemStack ammoBox = this.getItemStacks().stream().filter(stack -> {
                        if (stack.is(ModItems.AMMO_BOX.get())) {
                            return AmmoType.RIFLE.get(stack) > 0;
                        }
                        return false;
                    }).findFirst().orElse(ItemStack.EMPTY);

                    if (!ammoBox.isEmpty()) {
                        AmmoType.RIFLE.add(ammoBox, -1);
                    } else {
                        this.getItemStacks().stream().filter(stack -> stack.is(ModItems.RIFLE_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
                    }
                }
            }

            this.entityData.set(COAX_HEAT, this.entityData.get(COAX_HEAT) + 3);
            this.entityData.set(FIRE_ANIM, 2);

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.M_60_FIRE_3P.get(), 3, 1);
                    serverPlayer.playSound(ModSounds.M_60_FAR.get(), 6, 1);
                    serverPlayer.playSound(ModSounds.M_60_VERYFAR.get(), 12, 1);
                }
            }
        } else if (getWeaponType(0) == 2 && this.getEntityData().get(LOADED_MISSILE) > 0) {
            Matrix4f transformT = getBarrelTransform();
            Vector4f worldPosition = transformPosition(transformT, 0, 1, 0);

            WgMissileEntity wgMissileEntity = new WgMissileEntity(player, player.level(),
                    ExplosionConfig.WIRE_GUIDE_MISSILE_DAMAGE.get(),
                    ExplosionConfig.WIRE_GUIDE_MISSILE_EXPLOSION_DAMAGE.get(),
                    ExplosionConfig.WIRE_GUIDE_MISSILE_EXPLOSION_RADIUS.get());

            wgMissileEntity.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
            wgMissileEntity.shoot(getBarrelVector(1).x, 0, getBarrelVector(1).z, 2f, 0f);
            player.level().addFreshEntity(wgMissileEntity);

            if (!player.level().isClientSide) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playSound(ModSounds.BMP_MISSILE_FIRE_3P.get(), 6, 1);
                }
            }

            this.entityData.set(LOADED_MISSILE, this.getEntityData().get(LOADED_MISSILE) - 1);
            reloadCoolDown = 160;
        }
    }

    public final Vec3 getBarrelVector(float pPartialTicks) {
        return this.calculateViewVector(this.getBarrelXRot(pPartialTicks), this.getBarrelYRot(pPartialTicks));
    }

    public float getBarrelXRot(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, turretXRotO - this.xRotO, getTurretXRot() - this.getXRot());
    }

    public float getBarrelYRot(float pPartialTick) {
        return -Mth.lerp(pPartialTick, turretYRotO - this.yRotO, getTurretYRot() - this.getYRot());
    }

    @Override
    public void travel() {
        Entity passenger0 = this.getFirstPassenger();
        
        if (this.getEnergy() <= 0) return;

        if (passenger0 == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
            this.entityData.set(POWER, 0f);
        }

        if (forwardInputDown) {
            this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + (this.entityData.get(POWER) < 0 ? 0.016f : 0.0024f), 0.21f));
        }

        if (backInputDown) {
            this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - (this.entityData.get(POWER) > 0 ? 0.016f : 0.0024f), -0.16f));
            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.1f);
            } else if (this.leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.1f);
            }
        } else {
            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.1f);
            } else if (this.leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.1f);
            }
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.consumeEnergy(VehicleConfig.BMP_2_ENERGY_COST.get());
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * (upInputDown ? 0.5f : (rightInputDown || leftInputDown) ? 0.947f : 0.96f));
        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * (float) Math.max(0.76f - 0.1f * this.getDeltaMovement().horizontalDistance(), 0.3));

        float angle = (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1));
        double s0;

        if (Mth.abs(angle) < 90) {
            s0 = this.getDeltaMovement().horizontalDistance();
        } else {
            s0 = -this.getDeltaMovement().horizontalDistance();
        }

        this.setLeftWheelRot((float) ((this.getLeftWheelRot() - 1.25 * s0) + Mth.clamp(0.75f * this.entityData.get(DELTA_ROT), -5f, 5f)));
        this.setRightWheelRot((float) ((this.getRightWheelRot() - 1.25 * s0) - Mth.clamp(0.75f * this.entityData.get(DELTA_ROT), -5f, 5f)));

        this.entityData.set(TRACK_L, (float) ((entityData.get(TRACK_L) - 1.9 * Math.PI * s0) + Mth.clamp(0.4f * Math.PI * this.entityData.get(DELTA_ROT), -5f, 5f)));
        this.entityData.set(TRACK_R, (float) ((entityData.get(TRACK_R) - 1.9 * Math.PI * s0) - Mth.clamp(0.4f * Math.PI * this.entityData.get(DELTA_ROT), -5f, 5f)));

        if (this.isInWater() || onGround()) {
            this.setYRot((float) (this.getYRot() - (isInWater() && !onGround() ? 2.5 : 6) * entityData.get(DELTA_ROT)));
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * 0.017453292F) * (!isInWater() && !onGround() ? 0.13f : (isInWater() && !onGround() ? 2f : 2.4)) * this.entityData.get(POWER), 0.0, Mth.cos(this.getYRot() * 0.017453292F) * (!isInWater() && !onGround() ? 0.13f : (isInWater() && !onGround() ? 2f : 2.4)) * this.entityData.get(POWER)));
        }
    }

    private void gunnerAngle() {
        Entity driver = this.getFirstPassenger();
        if (driver == null) return;

        float gunAngle = -Mth.wrapDegrees(driver.getYHeadRot() - this.getYRot());

        float diffY;
        float diffX;

        diffY = Mth.wrapDegrees(gunAngle - getTurretYRot() + 0.05f);
        diffX = Mth.wrapDegrees(driver.getXRot() - this.getTurretXRot());


        this.setTurretXRot(Mth.clamp(this.getTurretXRot() + Mth.clamp(0.95f * diffX, -5, 5), -74f, 7.5f));
        this.setTurretYRot(this.getTurretYRot() + Mth.clamp(0.95f * diffY, -15, 15));
    }

    @Override
    public float turretYRotO() {
        return turretYRotO;
    }

    @Override
    public float turretYRot() {
        return turretYRot;
    }

    @Override
    public float turretXRotO() {
        return turretXRotO;
    }

    @Override
    public float turretXRot() {
        return turretXRot;
    }

    @Override
    public Vec3 getBarrelVec(float ticks) {
        return getBarrelVector(ticks);
    }

    public float getTurretYRot() {
        return this.turretYRot;
    }

    public void setTurretYRot(float pTurretYRot) {
        this.turretYRot = pTurretYRot;
    }

    public float getTurretXRot() {
        return this.turretXRot;
    }

    public void setTurretXRot(float pTurretXRot) {
        this.turretXRot = pTurretXRot;
    }

    public float getLeftWheelRot() {
        return this.leftWheelRot;
    }

    public void setLeftWheelRot(float pLeftWheelRot) {
        this.leftWheelRot = pLeftWheelRot;
    }

    public float getRightWheelRot() {
        return this.rightWheelRot;
    }

    public void setRightWheelRot(float pRightWheelRot) {
        this.rightWheelRot = pRightWheelRot;
    }

    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.BMP_ENGINE.get();
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        // From Immersive_Aircraft
        if (!this.hasPassenger(passenger)) {
            return;
        }

        Matrix4f transform = getTurretTransform();
        Matrix4f transformV = getVehicleTransform();

        float x = 0.5f;
        float y = 0.1f;
        float z = 0.75f;
        y += (float) passenger.getMyRidingOffset();

        int i = this.getSeatIndex(passenger);

        Vector4f worldPosition;
        if (i == 0) {
            worldPosition = transformPosition(transform, x, y, z);
        } else {
            worldPosition = transformPosition(transformV, 0, 1, 0);
        }
        passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
        callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
    }

    public int getMaxPassengers() {
        return 5;
    }

    public Matrix4f getBarrelTransform() {
        Matrix4f transformT = getTurretTransform();
        float x = 0f;
        float y = 0.5541f;
        float z = 0.83004375f;
        Vector4f worldPosition = transformPosition(transformT, x, y, z);

        Matrix4f transform = new Matrix4f();
        transform.translate(worldPosition.x, worldPosition.y, worldPosition.z);
        transform.rotate(Axis.YP.rotationDegrees(getTurretYRot() - getYRot()));
        transform.rotate(Axis.XP.rotationDegrees(getTurretXRot()));
        transform.rotate(Axis.ZP.rotationDegrees(getRoll()));
        return transform;
    }

    public Matrix4f getTurretTransform() {
        Matrix4f transformT = getVehicleTransform();
        float x = 0f;
        float y = 2f;
        float z = -0.703125f;
        Vector4f worldPosition = transformPosition(transformT, x, y, z);

        Matrix4f transform = new Matrix4f();
        transform.translate(worldPosition.x, worldPosition.y, worldPosition.z);
        transform.rotate(Axis.YP.rotationDegrees(getTurretYRot() - getYRot()));
        transform.rotate(Axis.XP.rotationDegrees(getXRot()));
        transform.rotate(Axis.ZP.rotationDegrees(getRoll()));
        return transform;
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));

        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 80f,
                    this.getX(), this.getY(), this.getZ(), 5f, ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }


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

        this.discard();
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -74F, 7.5F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState firePredicate(AnimationState<Bmp2Entity> event) {
        if (this.entityData.get(FIRE_ANIM) > 1 && getWeaponType(0) == 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.lav.fire"));
        }

        if (this.entityData.get(FIRE_ANIM) > 0 && getWeaponType(0) == 1) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.lav.fire2"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.lav.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::firePredicate));
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
        if (getWeaponType(0) == 0) {
            return 250;
        } else if (getWeaponType(0) == 1) {
            return 750;
        }
        return 250;
    }

    @Override
    public boolean canShoot(Player player) {
        if (getWeaponType(0) == 0) {
            return (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) && !cannotFire;
        } else if (getWeaponType(0) == 1) {
            return (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) && !cannotFireCoax;
        } else if (getWeaponType(0) == 2) {
            return (this.entityData.get(LOADED_MISSILE) > 0);
        }
        return false;
    }

    @Override
    public int getAmmoCount(Player player) {
        return this.entityData.get(AMMO);
    }

    @Override
    public boolean banHand(Player player) {
        return true;
    }

    @Override
    public boolean hidePassenger() {
        return true;
    }

    @Override
    public int zoomFov() {
        return 3;
    }

    @Override
    public void changeWeapon(int index, int scroll) {
        if (index != 0) return;

        var type = (getWeaponType(0) + scroll + 3) % 3;
        setWeaponType(0, type);

        var sound = switch (type) {
            case 0, 2 -> ModSounds.INTO_MISSILE.get();
            case 1 -> ModSounds.INTO_CANNON.get();
            default -> throw new IllegalStateException("Unexpected type: " + type);
        };

        this.level().playSound(null, this, sound, this.getSoundSource(), 1, 1);
    }

    @Override
    public int getWeaponType(int index) {
        return index == 0 ? entityData.get(WEAPON_TYPE) : -1;
    }

    @Override
    public void setWeaponType(int index, int type) {
        if (index == 0) entityData.set(WEAPON_TYPE, type);
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/bmp2_icon.png");
    }
}
