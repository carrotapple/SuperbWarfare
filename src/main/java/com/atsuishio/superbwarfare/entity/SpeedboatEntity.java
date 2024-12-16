package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.menu.SpeedboatMenu;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
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

import java.util.Comparator;

public class SpeedboatEntity extends Entity implements GeoEntity, IChargeEntity, IVehicleEntity, HasCustomInventoryScreen, ContainerEntity {

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROT_Y = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROTOR = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.STRING);

    public static final float MAX_HEALTH = CannonConfig.SPEEDBOAT_HP.get();
    public static final float MAX_ENERGY = CannonConfig.SPEEDBOAT_MAX_ENERGY.get().floatValue();
    public static final int CONTAINER_SIZE = 102;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

    public float turretYRot;
    public float turretXRot;
    public float turretYRotO;
    public float turretXRotO;

    public float heat;
    public boolean cannotFire;

    public SpeedboatEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.SPEEDBOAT.get(), world);
    }

    public SpeedboatEntity(EntityType<SpeedboatEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, MAX_HEALTH);
        this.entityData.define(ENERGY, 0f);
        this.entityData.define(ROT_Y, 0f);
        this.entityData.define(DELTA_ROT, 0f);
        this.entityData.define(POWER, 0f);
        this.entityData.define(ROTOR, 0f);
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(LAST_ATTACKER_UUID, "undefined");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putFloat("Energy", this.entityData.get(ENERGY));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));
        ContainerHelper.saveAllItems(compound, this.getItemStacks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(ENERGY, compound.getFloat("Energy"));
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        } else {
            this.entityData.set(HEALTH, MAX_HEALTH);
        }
        if (compound.contains("LastAttacker")) {
            this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        }
        ContainerHelper.loadAllItems(compound, this.getItemStacks());
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.getDeltaMovement().length() > 0.2) {
            return false;
        } else {
            return canVehicleCollide(this, pEntity);
        }
    }

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    @Override
    public boolean isPushable() {
        return true;
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
            amount *= 2f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 3f;
        }
        if (source.is(ModDamageTypes.GUN_FIRE_ABSOLUTE)) {
            amount *= 1.2f;
        }

        if (source.getEntity() != null) {
            this.entityData.set(LAST_ATTACKER_UUID, source.getEntity().getStringUUID());
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.5f * java.lang.Math.max(amount - 3, 0));

        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;
        if (player.isShiftKeyDown()) {
            if (player.getMainHandItem().is(ModItems.CROWBAR.get()) && this.getFirstPassenger() == null) {
                ItemStack stack = ContainerBlockItem.createInstance(this);
                if (!player.addItem(stack)) {
                    player.drop(stack, false);
                }
                this.remove(RemovalReason.DISCARDED);
                this.discard();
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
                player.openMenu(this);
                return !player.level().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
            }
        } else {
            if (player.getMainHandItem().is(Items.IRON_INGOT)) {
                if (this.entityData.get(HEALTH) < MAX_HEALTH) {
                    this.entityData.set(HEALTH, Math.min(this.entityData.get(HEALTH) + 0.1f * MAX_HEALTH, MAX_HEALTH));
                    player.getMainHandItem().shrink(1);
                    if (!this.level().isClientSide) {
                        this.level().playSound(null, this, SoundEvents.IRON_GOLEM_REPAIR, this.getSoundSource(), 1, 1);
                    }
                } else {
                    player.startRiding(this);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            player.startRiding(this);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
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

        if (this.entityData.get(COOL_DOWN) > 0) {
            this.entityData.set(COOL_DOWN, this.entityData.get(COOL_DOWN) - 1);
        }

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

        double fluidFloat;
        fluidFloat = -0.05 + 0.1 * getSubmergedHeight(this);
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, fluidFloat, 0.0));

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.85, 0.2));
        } else {
            float f = 0.74f + 0.09f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.04 * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.85, f));
        }

        if (this.level() instanceof ServerLevel) {
            this.entityData.set(ROT_Y, this.getYRot());
        }

        handleClientSync();
        this.tickLerp();
        this.controlBoat();

        if (this.entityData.get(HEALTH) <= 0) {
            this.ejectPassengers();
            destroy();
        }

        if (this.isVehicle() && this.getDeltaMovement().length() > 0.05) {
            crushEntities(this.getDeltaMovement());
        }

        collideBlock();
        gunnerAngle();
        gunnerFire();

//        attractEntity();

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
    private void gunnerFire() {
        if (this.entityData.get(COOL_DOWN) != 0 || this.cannotFire) return;
        if (this.getItemStacks().stream().noneMatch(stack -> stack.is(ModItems.HEAVY_AMMO.get()))) return;

        Entity driver = this.getFirstPassenger();
        if (driver == null) return;

        if (driver instanceof Player player && !(player.getMainHandItem().is(ModTags.Items.GUN))) {
            if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).holdFire) {
                ProjectileEntity projectile = new ProjectileEntity(driver.level())
                        .shooter(player)
                        .damage(CannonConfig.SPEEDBOAT_GUN_DAMAGE.get())
                        .headShot(2f)
                        .zoom(false);

                projectile.bypassArmorRate(0.9f);
                projectile.setPos(this.xo - this.getViewVector(1).scale(0.54).x - this.getDeltaMovement().x, this.yo + 3.0, this.zo - this.getViewVector(1).scale(0.54).z - this.getDeltaMovement().z);
                projectile.shoot(player, player.getLookAngle().x, player.getLookAngle().y + (zooming() ? 0.002f : -0.009f), player.getLookAngle().z, 20,
                        (float) 0.4);
                this.level().addFreshEntity(projectile);

                float pitch = heat <= 60 ? 1 : (float) (1 - 0.015 * java.lang.Math.abs(60 - heat));

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.M_2_FIRE_1P.get(), 2, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_2_FIRE_3P.get(), SoundSource.PLAYERS, 4, pitch);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_2_FAR.get(), SoundSource.PLAYERS, 12, pitch);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_2_VERYFAR.get(), SoundSource.PLAYERS, 24, pitch);
                }

                Level level = player.level();

                final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer serverPlayer) {
                        ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(6, 5, 7, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }

                if (level instanceof ServerLevel) {
                    this.entityData.set(COOL_DOWN, 3);
                    heat += 4;
                }

                this.getItemStacks().stream().filter(stack -> stack.is(ModItems.HEAVY_AMMO.get())).findFirst().ifPresent(stack -> stack.shrink(1));
            }
        }
    }

    /**
     * 撞击实体并造成伤害
     * @param velocity 动量
     */
    public void crushEntities(Vec3 velocity) {
        var frontBox = getBoundingBox().move(velocity.scale(0.5));
        var velAdd = velocity.add(0, 0, 0).scale(1.5);
        for (var entity : level().getEntities(EntityTypeTest.forClass(Entity.class), frontBox, entity -> entity != this && entity != getFirstPassenger() && entity.getVehicle() == null)) {

            double entitySize = entity.getBbWidth() * entity.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            double f1 = Math.min(thisSize / entitySize, 4);

            if (!(entity instanceof TargetEntity)) {
                this.push(-f * velAdd.x, -f * velAdd.y, -f * velAdd.z);
            }

            if (velocity.length() > 0.2 && entity.isAlive() && !(entity instanceof ItemEntity || entity instanceof Projectile || entity instanceof ProjectileEntity)) {
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                }
                entity.push(f1 * velAdd.x, f1 * velAdd.y, f1 * velAdd.z);
                entity.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), (float) (25 * velocity.length()));
            }
        }
    }

    /**
     * 撞掉莲叶
     */
    public void collideBlock() {
        AABB aabb = AABB.ofSize(new Vec3(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ()), 3.6, 2.6, 3.6);
        BlockPos.betweenClosedStream(aabb).forEach((pos) -> {
            BlockState blockstate = this.level().getBlockState(pos);
            if (blockstate.is(Blocks.LILY_PAD)) {
                this.level().destroyBlock(pos, true);
            }
        });
    }

    private void controlBoat() {
        Entity passenger0 = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        if (this.entityData.get(ENERGY) <= 0) return;

        if (passenger0 == null) {
            this.getPersistentData().putBoolean("left", false);
            this.getPersistentData().putBoolean("right", false);
            this.getPersistentData().putBoolean("forward", false);
            this.getPersistentData().putBoolean("backward", false);
        }

        float diffY = 0;
        if (this.getPersistentData().getBoolean("forward")) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.02f);
        }

        if (this.getPersistentData().getBoolean("backward")) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.02f);
            if (this.getPersistentData().getBoolean("right")) {
                diffY = Mth.clamp(diffY + 1f, 0, 10);
                handleSetDiffY(diffY);
            } else if (this.getPersistentData().getBoolean("left")) {
                diffY = Mth.clamp(diffY - 1f, -10, 0);
                handleSetDiffY(diffY);
            }
        } else {
            if (this.getPersistentData().getBoolean("right")) {
                diffY = Mth.clamp(diffY - 1f, -10, 0);
                handleSetDiffY(diffY);
            } else if (this.getPersistentData().getBoolean("left")) {
                diffY = Mth.clamp(diffY + 1f, 0, 10);
                handleSetDiffY(diffY);
            }
        }

        if (this.getPersistentData().getBoolean("forward") || this.getPersistentData().getBoolean("backward")) {
            this.entityData.set(ENERGY, Math.max(this.entityData.get(ENERGY) - CannonConfig.SPEEDBOAT_ENERGY_COST.get().floatValue(), 0));
        }

        if (level().isClientSide) {
            level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.getPersistentData().getBoolean("forward") || this.getPersistentData().getBoolean("backward") ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.9f);
        this.entityData.set(ROTOR, this.entityData.get(ROTOR) + this.entityData.get(POWER));
        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.8f);

        if (this.isInWater() || this.isUnderWater()) {
            this.setYRot(this.entityData.get(ROT_Y) - this.entityData.get(DELTA_ROT));
            this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(this.getLookAngle().x, 0, this.getLookAngle().z).scale(this.entityData.get(POWER))));
        }
    }

    private void gunnerAngle() {
        Entity driver = this.getFirstPassenger();
        if (driver == null) return;

        float gunAngle = -Math.clamp(-140f, 140f, Mth.wrapDegrees(driver.getYHeadRot() - this.getYRot()));

        turretYRotO = this.getTurretYRot();
        turretXRotO = this.getTurretXRot();

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

    private void handleSetDiffY(float diffY) {
        this.entityData.set(DELTA_ROT, (float) Mth.clamp(diffY * 1.3 * Math.max(8 * this.getDeltaMovement().length(), 0.5), -2, 2));
    }

    private void handleClientSync() {
        if (isControlledByLocalInstance()) {
            lerpSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }
        if (lerpSteps <= 0) {
            return;
        }
        double interpolatedX = getX() + (lerpX - getX()) / (double) lerpSteps;
        double interpolatedY = getY() + (lerpY - getY()) / (double) lerpSteps;
        double interpolatedZ = getZ() + (lerpZ - getZ()) / (double) lerpSteps;
        double interpolatedYaw = Mth.wrapDegrees(lerpYRot - (double) getYRot());
        setYRot(getYRot() + (float) interpolatedYaw / (float) lerpSteps);
        setXRot(getXRot() + (float) (lerpXRot - (double) getXRot()) / (float) lerpSteps);

        setPos(interpolatedX, interpolatedY, interpolatedZ);
        setRot(getYRot(), getXRot());

        --lerpSteps;
    }

    protected SoundEvent getEngineSound() {
        return ModSounds.BOAT_ENGINE.get();
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            double posY = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();

            if (!zooming() && (this.isInWater() || this.isUnderWater())) {
                pPassenger.setYRot(pPassenger.getYRot() - 1.27f * this.entityData.get(DELTA_ROT));
                pPassenger.setYHeadRot(pPassenger.getYHeadRot() - 1.27f * this.entityData.get(DELTA_ROT));
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

//    public boolean hasEnoughSpaceFor(Entity pEntity) {
//        return pEntity.getBbWidth() < this.getBbWidth();
//    }
//
//    public void attractEntity() {
//        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
//        if (!list.isEmpty()) {
//            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);
//
//            for (Entity entity : list) {
//                if (!entity.hasPassenger(this)) {
//                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
//                        entity.startRiding(this);
//                    } else {
//                        this.push(entity);
//                    }
//                }
//            }
//        }
//    }

    public static double calculateAngle(Vec3 move, Vec3 view) {
        move = move.multiply(1, 0, 1).normalize();
        view = view.multiply(1, 0, 1).normalize();

        double startLength = move.length();
        double endLength = view.length();
        if (startLength > 0.0D && endLength > 0.0D) {
            return Math.toDegrees(Math.acos(Mth.clamp(move.dot(view) / (startLength * endLength), -1, 1)));
        } else {
            return 0.0D;
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
            this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
            this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYaw;
        this.lerpXRot = pPitch;
        this.lerpSteps = 10;
    }

    private void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        CustomExplosion explosion = new CustomExplosion(this.level(), attacker,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 45f,
                this.getX(), this.getY(), this.getZ(), 3f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
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
        if (this.entityData.get(COOL_DOWN) > 1 && !cannotFire) {
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
    public void charge(int amount) {
        this.entityData.set(ENERGY, Math.min(this.entityData.get(ENERGY) + amount, MAX_ENERGY));
    }

    @Override
    public boolean canCharge() {
        return this.entityData.get(ENERGY) < MAX_ENERGY;
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
            return new SpeedboatMenu(pContainerId, pPlayerInventory, this);
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
}
