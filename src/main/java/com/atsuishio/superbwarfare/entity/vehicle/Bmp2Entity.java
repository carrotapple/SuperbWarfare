package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.SmallCannonShellWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.VehicleWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.WgMissileWeapon;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.AmmoType;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
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

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class Bmp2Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity {
    public static final EntityDataAccessor<Integer> LOADED_MISSILE = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> MISSILE_COUNT = SynchedEntityData.defineId(Bmp2Entity.class, EntityDataSerializers.INT);

    public static final float MAX_HEALTH = VehicleConfig.BMP_2_HP.get();
    public static final int MAX_ENERGY = VehicleConfig.BMP_2_MAX_ENERGY.get();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int reloadCoolDown;

    public Bmp2Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.BMP_2.get(), world);
    }

    public Bmp2Entity(EntityType<Bmp2Entity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.5f);
    }

    @Override
    public VehicleWeapon[][] initWeapons() {
        return new VehicleWeapon[][]{
                new VehicleWeapon[]{
                        new SmallCannonShellWeapon()
                                .damage(VehicleConfig.BMP_2_CANNON_DAMAGE.get())
                                .explosionDamage(VehicleConfig.BMP_2_CANNON_EXPLOSION_DAMAGE.get())
                                .explosionRadius(VehicleConfig.BMP_2_CANNON_EXPLOSION_RADIUS.get().floatValue())
                                .sound(ModSounds.INTO_MISSILE.get())
                                .icon(ModUtils.loc("textures/screens/vehicle_weapon/cannon_30mm.png")),
                        new ProjectileWeapon()
                                .damage(9.5f)
                                .headShot(2)
                                .zoom(false)
                                .sound(ModSounds.INTO_CANNON.get())
                                .icon(ModUtils.loc("textures/screens/vehicle_weapon/gun_7_62mm.png")),
                        new WgMissileWeapon()
                                .damage(ExplosionConfig.WIRE_GUIDE_MISSILE_DAMAGE.get())
                                .explosionDamage(ExplosionConfig.WIRE_GUIDE_MISSILE_EXPLOSION_DAMAGE.get())
                                .explosionRadius(ExplosionConfig.WIRE_GUIDE_MISSILE_EXPLOSION_RADIUS.get())
                                .sound(ModSounds.INTO_MISSILE.get()),
                }
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOADED_MISSILE, 0);
        this.entityData.define(MISSILE_COUNT, 0);
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
                .multiply(0.2f)
                .multiply(1.5f, DamageTypes.ARROW)
                .multiply(1.5f, DamageTypes.TRIDENT)
                .multiply(2.5f, DamageTypes.MOB_ATTACK)
                .multiply(2f, DamageTypes.MOB_ATTACK_NO_AGGRO)
                .multiply(1.5f, DamageTypes.MOB_PROJECTILE)
                .multiply(12.5f, DamageTypes.LAVA)
                .multiply(6f, DamageTypes.EXPLOSION)
                .multiply(6f, DamageTypes.PLAYER_EXPLOSION)
                .multiply(2f, ModDamageTypes.CUSTOM_EXPLOSION)
                .multiply(2f, ModDamageTypes.PROJECTILE_BOOM)
                .multiply(0.7f, ModDamageTypes.MINE)
                .multiply(0.9f, ModDamageTypes.LUNGE_MINE)
                .multiply(1.5f, ModDamageTypes.CANNON_FIRE)
                .multiply(0.1f, ModTags.DamageTypes.PROJECTILE)
                .multiply(0.7f, ModTags.DamageTypes.PROJECTILE_ABSOLUTE)
                .multiply(8.5f, ModDamageTypes.VEHICLE_STRIKE)
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
        super.baseTick();

        if (getLeftTrack() < 0) {
            setLeftTrack(100);
        }

        if (getLeftTrack() > 100) {
            setLeftTrack(0);
        }

        if (getRightTrack() < 0) {
            setRightTrack(100);
        }

        if (getRightTrack() > 100) {
            setRightTrack(0);
        }


        if (this.level() instanceof ServerLevel) {
            if (reloadCoolDown > 0) {
                reloadCoolDown--;
            }
            this.handleAmmo();
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

        collideBlock();
        if (this.getDeltaMovement().length() > 0.1) {
            collideHardBlock();
        }

        turretAngle(15, 10);
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

        if ((hasItem(ModItems.WIRE_GUIDE_MISSILE.get())
                || InventoryTool.hasCreativeAmmoBox(player))
                && this.reloadCoolDown <= 0 && this.getEntityData().get(LOADED_MISSILE) < 1) {
            this.entityData.set(LOADED_MISSILE, this.getEntityData().get(LOADED_MISSILE) + 1);
            this.reloadCoolDown = 160;
            if (!InventoryTool.hasCreativeAmmoBox(player)) {
                this.getItemStacks().stream().filter(stack -> stack.is(ModItems.WIRE_GUIDE_MISSILE.get())).findFirst().ifPresent(stack -> stack.shrink(1));
            }
            this.level().playSound(null, this, ModSounds.BMP_MISSILE_RELOAD.get(), this.getSoundSource(), 1, 1);
        }

        if (getWeaponIndex(0) == 0) {
            this.entityData.set(AMMO, countItem(ModItems.SMALL_SHELL.get()));
        } else if (getWeaponIndex(0) == 1) {
            this.entityData.set(AMMO, ammoCount);
        } else {
            this.entityData.set(AMMO, this.getEntityData().get(LOADED_MISSILE));
        }

        this.entityData.set(MISSILE_COUNT, countItem(ModItems.WIRE_GUIDE_MISSILE.get()));
    }

    @Override
    public void move(@NotNull MoverType movementType, @NotNull Vec3 movement) {
        super.move(movementType, movement);
        if (this.isInWater() && horizontalCollision) {
            setDeltaMovement(this.getDeltaMovement().add(0, 0.07, 0));
        }
    }

    @Override
    public void vehicleShoot(Player player, int type) {
        boolean hasCreativeAmmo = false;
        for (int i = 0; i < getMaxPassengers() - 1; i++) {
            if (getNthEntity(i) instanceof Player pPlayer && InventoryTool.hasCreativeAmmoBox(pPlayer)) {
                hasCreativeAmmo = true;
            }
        }

        Matrix4f transform = getBarrelTransform(1);
        if (getWeaponIndex(0) == 0) {
            if (this.cannotFire) return;
            float x = -0.1125f;
            float y = 0.174025f;
            float z = 4.2f;

            Vector4f worldPosition = transformPosition(transform, x, y, z);
            var smallCannonShell = ((SmallCannonShellWeapon) getWeapon(0)).create(player);

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

            if (hasCreativeAmmo) return;

            this.getItemStacks().stream().filter(stack -> stack.is(ModItems.SMALL_SHELL.get())).findFirst().ifPresent(stack -> stack.shrink(1));
        } else if (getWeaponIndex(0) == 1) {
            if (this.cannotFireCoax) return;
            float x = 0.1125f;
            float y = 0.174025f;
            float z = 2f;

            Vector4f worldPosition = transformPosition(transform, x, y, z);

            if (this.entityData.get(AMMO) > 0 || hasCreativeAmmo) {
                var projectileRight = ((ProjectileWeapon) getWeapon(0)).create(player);

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
        } else if (getWeaponIndex(0) == 2 && this.getEntityData().get(LOADED_MISSILE) > 0) {
            Matrix4f transformT = getBarrelTransform(1);
            Vector4f worldPosition = transformPosition(transformT, 0, 1, 0);

            var wgMissileEntity = ((WgMissileWeapon) getWeapon(0)).create(player);

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

        setLeftTrack((float) ((getLeftTrack() - 1.9 * Math.PI * s0) + Mth.clamp(0.4f * Math.PI * this.entityData.get(DELTA_ROT), -5f, 5f)));
        setRightTrack((float) ((getRightTrack() - 1.9 * Math.PI * s0) - Mth.clamp(0.4f * Math.PI * this.entityData.get(DELTA_ROT), -5f, 5f)));

        if (this.isInWater() || onGround()) {
            this.setYRot((float) (this.getYRot() - (isInWater() && !onGround() ? 2.5 : 6) * entityData.get(DELTA_ROT)));
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * 0.017453292F) * (!isInWater() && !onGround() ? 0.13f : (isInWater() && !onGround() ? 2f : 2.4)) * this.entityData.get(POWER), 0.0, Mth.cos(this.getYRot() * 0.017453292F) * (!isInWater() && !onGround() ? 0.13f : (isInWater() && !onGround() ? 2f : 2.4)) * this.entityData.get(POWER)));
        }
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

        Matrix4f transform = getTurretTransform(1);
        Matrix4f transformV = getVehicleTransform(1);

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

    public Vec3 driverZoomPos(float ticks) {
        Matrix4f transform = getTurretTransform(ticks);
        Vector4f worldPosition = transformPosition(transform, 0, 0, 0.75f);
        return new Vec3(worldPosition.x, worldPosition.y, worldPosition.z);
    }

    public Matrix4f getBarrelTransform(float ticks) {
        Matrix4f transformT = getTurretTransform(ticks);
        float x = 0f;
        float y = 0.5541f;
        float z = 0.83004375f;
        Vector4f worldPosition = transformPosition(transformT, x, y, z);

        Matrix4f transform = new Matrix4f();
        transform.translate(worldPosition.x, worldPosition.y, worldPosition.z);
        transform.rotate(Axis.YP.rotationDegrees(Mth.lerp(ticks, turretYRotO - yRotO, getTurretYRot() - getYRot())));
        transform.rotate(Axis.XP.rotationDegrees(Mth.lerp(ticks, turretXRotO, getTurretXRot())));
        transform.rotate(Axis.ZP.rotationDegrees(Mth.lerp(ticks, prevRoll, getRoll())));
        return transform;
    }

    public Matrix4f getTurretTransform(float ticks) {
        Matrix4f transformT = getVehicleTransform(ticks);
        float x = 0f;
        float y = 2f;
        float z = -0.703125f;
        Vector4f worldPosition = transformPosition(transformT, x, y, z);

        Matrix4f transform = new Matrix4f();
        transform.translate(worldPosition.x, worldPosition.y, worldPosition.z);
        transform.rotate(Axis.YP.rotationDegrees(Mth.lerp(ticks, turretYRotO - yRotO, getTurretYRot() - getYRot())));
        transform.rotate(Axis.XP.rotationDegrees(Mth.lerp(ticks, xRotO, getXRot())));
        transform.rotate(Axis.ZP.rotationDegrees(Mth.lerp(ticks, prevRoll, getRoll())));
        return transform;
    }

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), getAttacker(), getAttacker()), 80f,
                    this.getX(), this.getY(), this.getZ(), 5f, ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }

        explodePassengers();
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
        if (this.entityData.get(FIRE_ANIM) > 1 && getWeaponIndex(0) == 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.lav.fire"));
        }

        if (this.entityData.get(FIRE_ANIM) > 0 && getWeaponIndex(0) == 1) {
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
    public int mainGunRpm(Player player) {
        if (getWeaponIndex(0) == 0) {
            return 250;
        } else if (getWeaponIndex(0) == 1) {
            return 750;
        }
        return 250;
    }

    @Override
    public boolean canShoot(Player player) {
        if (getWeaponIndex(0) == 0) {
            return (this.entityData.get(AMMO) > 0 || InventoryTool.hasCreativeAmmoBox(player)) && !cannotFire;
        } else if (getWeaponIndex(0) == 1) {
            return (this.entityData.get(AMMO) > 0 || InventoryTool.hasCreativeAmmoBox(player)) && !cannotFireCoax;
        } else if (getWeaponIndex(0) == 2) {
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
    public boolean hidePassenger(Entity entity) {
        return true;
    }

    @Override
    public int zoomFov() {
        return 3;
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/bmp2_icon.png");
    }
}
