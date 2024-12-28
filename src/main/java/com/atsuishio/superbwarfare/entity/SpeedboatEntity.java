package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.PerkItem;
import com.atsuishio.superbwarfare.menu.VehicleMenu;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class SpeedboatEntity extends MobileVehicleEntity implements GeoEntity, IChargeEntity, IVehicleEntity, HasCustomInventoryScreen, ContainerEntity {
    public static final EntityDataAccessor<Integer> FIRE_ANIM = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> HEAT = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.INT);

    public static final float MAX_HEALTH = CannonConfig.SPEEDBOAT_HP.get();
    public static final int MAX_ENERGY = CannonConfig.SPEEDBOAT_MAX_ENERGY.get();
    public static final int CONTAINER_SIZE = 105;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public float turretYRot;
    public float turretXRot;
    public float turretYRotO;
    public float turretXRotO;
    public float rotorRot;
    public float rudderRot;
    public float rotorRotO;
    public float rudderRotO;

    public boolean cannotFire;

    public SpeedboatEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.SPEEDBOAT.get(), world);
    }

    public SpeedboatEntity(EntityType<SpeedboatEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AMMO, 0);
        this.entityData.define(FIRE_ANIM, 0);
        this.entityData.define(DELTA_ROT, 0f);
        this.entityData.define(HEAT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Energy", this.entityData.get(ENERGY));
        ContainerHelper.saveAllItems(compound, this.getItemStacks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ContainerHelper.loadAllItems(compound, this.getItemStacks());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.8;
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        if (!this.level().isClientSide && pReason != RemovalReason.DISCARDED) {
            Containers.dropContents(this.level(), this, this);
        }
        super.remove(pReason);
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
        this.hurt(0.5f * Math.max(amount - 3, 0));

        return true;
    }
    public double getSubmergedHeight(Entity entity) {
        for (FluidType fluidType : ForgeRegistries.FLUID_TYPES.get().getValues()) {
            if (entity.level().getFluidState(entity.blockPosition()).getFluidType() == fluidType)
                return entity.getFluidTypeHeight(fluidType);
        }
        return 0;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.entityData.get(HEAT) > 0) {
            this.entityData.set(HEAT, this.entityData.get(HEAT) - 1);
        }

        if (this.entityData.get(FIRE_ANIM) > 0) {
            this.entityData.set(FIRE_ANIM, this.entityData.get(FIRE_ANIM) - 1);
        }

        if (this.entityData.get(HEAT) < 40) {
            cannotFire = false;
        }

        if (this.level() instanceof ServerLevel) {
            this.entityData.set(AMMO, this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).mapToInt(ItemStack::getCount).sum());
        }

        turretYRotO = this.getTurretYRot();
        turretXRotO = this.getTurretXRot();
        rotorRotO = this.getRotorRot();
        rudderRotO = this.getRudderRot();

        Entity driver = this.getFirstPassenger();
        if (driver instanceof Player player) {
            if (this.entityData.get(HEAT) > 100) {
                cannotFire = true;
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 1f, 1f);
                }
            }
        }

        double fluidFloat;
        fluidFloat = -0.05 + 0.1 * getSubmergedHeight(this);
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, fluidFloat, 0.0));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.85, 0.2));
        } else {
            float f = 0.74f + 0.09f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.04 * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.85, f));
        }

        this.heal(0.05f);

        if (this.level() instanceof ServerLevel serverLevel && this.isInWater() && this.getDeltaMovement().length() > 0.1) {
            sendParticle(serverLevel, ParticleTypes.CLOUD, this.getX() + 0.5 * this.getDeltaMovement().x, this.getY() + getSubmergedHeight(this) - 0.2, this.getZ() + 0.5 * this.getDeltaMovement().z, (int)(2 + 4 * this.getDeltaMovement().length()), 0.65, 0, 0.65, 0, true);
            sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP, this.getX() + 0.5 * this.getDeltaMovement().x, this.getY() + getSubmergedHeight(this) - 0.2, this.getZ() + 0.5 * this.getDeltaMovement().z, (int)(2 + 10 * this.getDeltaMovement().length()), 0.65, 0, 0.65, 0, true);
            sendParticle(serverLevel, ParticleTypes.BUBBLE_COLUMN_UP, this.getX() - 4.5 * this.getLookAngle().x, this.getY() - 0.25, this.getZ() - 4.5 * this.getLookAngle().z, (int)(40 * Mth.abs(this.entityData.get(POWER))), 0.15, 0.15, 0.15, 0.02, true);
        }

        collideBlock();
        gunnerAngle();
        pickUpItem();

        this.refreshDimensions();
    }

    public boolean zooming() {
        Entity driver = this.getFirstPassenger();
        if (driver == null) return false;
        if (driver instanceof Player player) {
            return player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
        }
        return false;
    }

    /**
     * 机枪塔开火
     */
    @Override
    public void vehicleShoot(Player player) {
        if (this.cannotFire) return;

        ProjectileEntity projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .damage(CannonConfig.SPEEDBOAT_GUN_DAMAGE.get())
                .headShot(2f)
                .zoom(false);

        if (this.getItemStacks().size() > 102) {
            ItemStack perkItem = this.getItemStacks().get(102);
            if (perkItem.getItem() instanceof PerkItem perk) {
                if (perk.getPerk() == ModPerks.SILVER_BULLET.get()) {
                    projectile.undeadMultiple(2.5f);
                } else if (perk.getPerk() == ModPerks.BEAST_BULLET.get()) {
                    projectile.beast();
                } else if (perk.getPerk() == ModPerks.JHP_BULLET.get()) {
                    projectile.jhpBullet(true, 3);
                } else if (perk.getPerk() == ModPerks.HE_BULLET.get()) {
                    projectile.heBullet(true, 3);
                } else if (perk.getPerk() == ModPerks.INCENDIARY_BULLET.get()) {
                    projectile.fireBullet(true, 3, false);
                }

                if (perk.getPerk() instanceof AmmoPerk ammoPerk) {
                    projectile.setRGB(ammoPerk.rgb);
                    if (!ammoPerk.mobEffects.get().isEmpty()) {
                        ArrayList<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                        for (MobEffect effect : ammoPerk.mobEffects.get()) {
                            mobEffectInstances.add(new MobEffectInstance(effect, 160, 2));
                        }
                        projectile.effect(mobEffectInstances);
                    }
                }
            }
        }

        if (this.getItemStacks().size() > 104) {
            ItemStack perkItem = this.getItemStacks().get(104);
            if (perkItem.getItem() instanceof PerkItem perk) {
                if (perk.getPerk() == ModPerks.MONSTER_HUNTER.get()) {
                    projectile.monsterMultiple(0.5f);
                }
            }
        }

        projectile.bypassArmorRate(0.9f);
        projectile.setPos(this.xo - this.getViewVector(1).scale(0.54).x - this.getDeltaMovement().x, this.yo + 3.0, this.zo - this.getViewVector(1).scale(0.54).z - this.getDeltaMovement().z);
        projectile.shoot(player, player.getLookAngle().x, player.getLookAngle().y + (zooming() ? 0.002f : -0.009f), player.getLookAngle().z, 20,
                (float) 0.4);
        this.level().addFreshEntity(projectile);

        float pitch = this.entityData.get(HEAT) <= 60 ? 1 : (float) (1 - 0.011 * java.lang.Math.abs(60 - this.entityData.get(HEAT)));

        if (!player.level().isClientSide) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.playSound(ModSounds.M_2_FIRE_3P.get(), 4, pitch);
                serverPlayer.playSound(ModSounds.M_2_FAR.get(), 12, pitch);
                serverPlayer.playSound(ModSounds.M_2_VERYFAR.get(), 24, pitch);
            }
        }


        Level level = player.level();
        final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

        for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
            if (target instanceof ServerPlayer serverPlayer) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 5, this.getX(), this.getEyeY(), this.getZ()));
            }
        }

        this.entityData.set(HEAT, this.entityData.get(HEAT) + 3);
        this.entityData.set(FIRE_ANIM, 3);
        this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
    }


    /**
     * 撞掉莲叶和冰块
     */
    public void collideBlock() {
        AABB aabb = AABB.ofSize(new Vec3(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ()), 3.6, 2.6, 3.6);
        BlockPos.betweenClosedStream(aabb).forEach((pos) -> {
            BlockState blockstate = this.level().getBlockState(pos);
            if (blockstate.is(Blocks.LILY_PAD) || blockstate.is(Blocks.ICE) || blockstate.is(Blocks.FROSTED_ICE)) {
                this.level().destroyBlock(pos, true);
            }
        });
    }

    @Override
    public void travel() {
        Entity passenger0 = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        if (this.getEnergy() <= 0) return;

        if (passenger0 == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
        }

        if (forwardInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.02f);
        }

        if (backInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.02f);
            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.2f);
            } else if (leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.2f);
            }
        } else {
            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.1f);
            } else if (this.leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.1f);
            }
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.extraEnergy(CannonConfig.SPEEDBOAT_ENERGY_COST.get());
        }

        if (level().isClientSide) {
            level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.forwardInputDown || this.backInputDown ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.87f);
        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.8f);

        this.setRotorRot(this.getRotorRot() + 10 * this.entityData.get(POWER));
        this.setRudderRot(Mth.clamp(this.getRudderRot() - this.entityData.get(DELTA_ROT), -1.25f, 1.25f) * 0.7f * (this.entityData.get(POWER) > 0 ? 1 : -1));

        if (this.isInWater() || this.isUnderWater()) {
            this.setYRot((float) (this.getYRot() - Math.max(5 * this.getDeltaMovement().length(), 0.3) * this.entityData.get(DELTA_ROT)));
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * 0.017453292F) * this.entityData.get(POWER), 0.0, Mth.cos(this.getYRot() * 0.017453292F) * this.entityData.get(POWER)));
        }
    }

    private void gunnerAngle() {
        Entity driver = this.getFirstPassenger();
        if (driver == null) return;

        float gunAngle = -Math.clamp(-140f, 140f, Mth.wrapDegrees(driver.getYHeadRot() - this.getYRot()));

        this.setTurretYRot(gunAngle);
        this.setTurretXRot(driver.getXRot() - this.getXRot());
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

    public float getRotorRot() {
        return this.rotorRot;
    }

    public void setRotorRot(float pRotorRot) {
        this.rotorRot = pRotorRot;
    }

    public float getRudderRot() {
        return this.rudderRot;
    }

    public void setRudderRot(float pRudderRot) {
        this.rudderRot = pRudderRot;
    }
    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.BOAT_ENGINE.get();
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            double posY = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();

            if (!zooming() && (this.isInWater() || this.isUnderWater())) {
                pPassenger.setYRot((float) (pPassenger.getYRot() - Math.max(5 * this.getDeltaMovement().length(), 0.3) * this.entityData.get(DELTA_ROT)));
                pPassenger.setYHeadRot((float) (pPassenger.getYHeadRot() - Math.max(5 * this.getDeltaMovement().length(), 0.3) * this.entityData.get(DELTA_ROT)));
            }

            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(pPassenger);
                if (i == 0) {
                    pCallback.accept(pPassenger, this.getX(), posY, this.getZ());
                    return;
                }

                double zOffset = -0.8;
                if (i % 2 == 0) {
                    zOffset = 0.8;
                }

                double xOffset = (int) -((i - 1) / 2.0 + 1) * 0.95;
                Vec3 vec3 = (new Vec3(xOffset, 0.0D, zOffset)).yRot(-this.getYRot() * ((float) java.lang.Math.PI / 180F) - ((float) java.lang.Math.PI / 2F));
                pCallback.accept(pPassenger, this.getX() + vec3.x, posY, this.getZ() + vec3.z);
            } else {
                pCallback.accept(pPassenger, this.getX(), posY, this.getZ());
            }
        }
    }

    public void pickUpItem() {
        List<ItemEntity> list = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.2F, 0.1, 0.2F));
        if (!list.isEmpty()) {
            for (ItemEntity entity : list) {
                if (!this.level().isClientSide) {
                    HopperBlockEntity.addItem(this, entity);
                }
            }
        }
    }
    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        CustomExplosion explosion = new CustomExplosion(this.level(), attacker == null ? this : attacker,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker == null ? this : attacker, attacker == null ? this : attacker), 75f,
                this.getX(), this.getY(), this.getZ(), 5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        this.discard();
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -40.0F, 20F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);

        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -140.0F, 140.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState firePredicate(AnimationState<SpeedboatEntity> event) {
        if (this.entityData.get(FIRE_ANIM) > 1) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.speedboat.fire"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.speedboat.idle"));
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
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    public int getMaxPassengers() {
        return 5;
    }

    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
        pPlayer.openMenu(this);
        if (!pPlayer.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, pPlayer);
        }
    }

    @Nullable
    @Override
    public ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public void setLootTable(@Nullable ResourceLocation pLootTable) {
    }

    @Override
    public long getLootTableSeed() {
        return 0;
    }

    @Override
    public void setLootTableSeed(long pLootTableSeed) {
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.items;
    }

    @Override
    public void clearItemStacks() {
        this.items.clear();
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.items, pSlot, pAmount);
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return switch (pIndex) {
            case VehicleMenu.DEFAULT_AMMO_PERK_SLOT ->
                    pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == Perk.Type.AMMO;
            case VehicleMenu.DEFAULT_FUNC_PERK_SLOT ->
                    pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == Perk.Type.FUNCTIONAL && perkItem.getPerk() == ModPerks.POWERFUL_ATTRACTION.get();
            case VehicleMenu.DEFAULT_DAMAGE_PERK_SLOT ->
                    pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == Perk.Type.DAMAGE && perkItem.getPerk() == ModPerks.MONSTER_HUNTER.get();
            default -> true;
        };
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack itemstack = this.getItemStacks().get(pSlot);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.getItemStacks().set(pSlot, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.getItemStacks().set(pSlot, pStack);
        if (!pStack.isEmpty() && pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return !this.isRemoved() && this.position().closerThan(pPlayer.position(), 8.0D);
    }

    @Override
    public void clearContent() {
        this.getItemStacks().clear();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        if (pPlayer.isSpectator()) {
            return null;
        } else {
            return new VehicleMenu(pContainerId, pPlayerInventory, this);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    }

    @Override
    public void stopOpen(Player pPlayer) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(pPlayer));
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
        return 500;
    }

    @Override
    public boolean canShoot(Player player) {
        return (this.entityData.get(AMMO) > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())))
                && !player.getMainHandItem().is(ModTags.Items.GUN)
                && !cannotFire;
    }

    @Override
    public int getAmmoCount(Player player) {
        return this.entityData.get(AMMO);
    }
}
