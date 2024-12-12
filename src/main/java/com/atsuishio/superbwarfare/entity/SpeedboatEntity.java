package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpeedboatEntity extends Entity implements GeoEntity, IChargeEntity, IVehicleEntity {

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROT_Y = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);

    public static final float MAX_HEALTH = CannonConfig.MK42_HP.get();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putFloat("Energy", this.entityData.get(ENERGY));
        compound.putFloat("Power", this.entityData.get(POWER));
        compound.putFloat("DeltaRot", this.entityData.get(DELTA_ROT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(ENERGY, compound.getFloat("Energy"));
        this.entityData.set(POWER, compound.getFloat("Power"));
        this.entityData.set(DELTA_ROT, compound.getFloat("DeltaRot"));
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        } else {
            this.entityData.set(HEALTH, MAX_HEALTH);
        }
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.getDeltaMovement().length() > 0.2) {
            return false;
        } else {
            return canVehicleCollide(this, pEntity);
        }
    }

    //TODO 创飞碰到的碰撞箱小于该船的实体，且本体速度不会减少太多

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
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
        return super.getPassengersRidingOffset() - 1.3;
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
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - 0.75f * amount);

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

//        if (this.getFirstPassenger() instanceof Player player) {
//            player.displayClientMessage(Component.literal("Angle" + new java.text.DecimalFormat("##.##").format(Mth.abs(90 - (float)calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90)), true);
//        }



        double fluidFloat = -0.04;

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.isInWater()) {
            fluidFloat = -0.025 + 0.05 * getSubmergedHeight(this);
            float f = 0.87f + 0.09f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.04 * this.getDeltaMovement().length())));
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.85, f));
        } else if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.85, 0.2));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.85, 0.99));
        }

        float f = 0.85f + 0.09f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
        this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).normalize().scale(0.04 * this.getDeltaMovement().length())));
        this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.85, f));

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, fluidFloat, 0.0));

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

        this.refreshDimensions();
    }

    public void crushEntities(Vec3 velocity) {
        var frontBox = getBoundingBox().move(velocity.scale(0.5));
        var velAdd = velocity.add(0, 0, 0).scale(1.5);
        for (var entity : level().getEntities(EntityTypeTest.forClass(Entity.class), frontBox, entity -> entity != this && entity != getFirstPassenger() && entity.getVehicle() == null)) {

            double entitySize = entity.getBbWidth() * entity.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            double f1 = Math.min(thisSize / entitySize, 4);

            entity.push(f1 * velAdd.x, f1 * velAdd.y, f1 * velAdd.z);
            if (!(entity instanceof TargetEntity)) {
                this.push(-f * velAdd.x, -f * velAdd.y, -f * velAdd.z);
            }

            if (velocity.length() > 0.2 && entity.isAlive()) {
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                }
                if (!(entity instanceof ItemEntity)) {
                    entity.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger() ), (float) (25 * velocity.length()));
                }
            }
        }
    }

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

        float diffY = 0;

        diffY = (float) Mth.lerp(0.1 * diffY, diffY, 0);

        if (this.getPersistentData().getBoolean("forward")) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.08f);
        }

        if (this.getPersistentData().getBoolean("backward")) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.12f);
            if (this.getPersistentData().getBoolean("left")) {
                diffY = Mth.clamp(diffY + 1f, 0, 5);
                handleSetDiffY(diffY);
            } else if (this.getPersistentData().getBoolean("right")) {
                diffY = Mth.clamp(diffY - 1f, -5, 0);
                handleSetDiffY(diffY);
            }
        } else {
            if (this.getPersistentData().getBoolean("left")) {
                diffY = Mth.clamp(diffY - 1f, -5, 0);
                handleSetDiffY(diffY);
            } else if (this.getPersistentData().getBoolean("right")) {
                diffY = Mth.clamp(diffY + 1f, 0, 5);
                handleSetDiffY(diffY);
            }
        }

        if (level().isClientSide) {
            level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.inputUp || this.inputDown ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.3f);

        this.flyDist = this.entityData.get(POWER);

        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.8f);

        if (this.isInWater() || this.isUnderWater()) {
            this.setYRot(this.entityData.get(ROT_Y) + this.entityData.get(DELTA_ROT));
            this.setDeltaMovement(this.getDeltaMovement().add(this.getViewVector(1).scale(this.entityData.get(POWER))));
        }
    }

    private void handleSetDiffY(float diffY) {
        this.entityData.set(DELTA_ROT, (float) Mth.clamp(diffY * 1.3 * Math.max(4 * this.getDeltaMovement().length(), 0.5), -2 ,2));
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
        super.positionRider(pPassenger, pCallback);
        if (this.hasPassenger(pPassenger) && (this.isInWater() || this.isUnderWater())) {
            pPassenger.setYRot(pPassenger.getYRot() + 1.27f * this.entityData.get(DELTA_ROT));
            pPassenger.setYHeadRot(pPassenger.getYHeadRot() + 1.27f * this.entityData.get(DELTA_ROT));
        }
    }

    public static double calculateAngle(Vec3 move, Vec3 view) {
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
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 20f,
                this.getX(), this.getY(), this.getZ(), 4.5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());

        this.discard();
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -85.0F, 16.3F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);

        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -105.0F, 105.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void charge(int amount) {
        this.entityData.set(ENERGY, Math.min(this.entityData.get(ENERGY) + amount, CannonConfig.ANNIHILATOR_MAX_ENERGY.get().floatValue()));
    }
}
