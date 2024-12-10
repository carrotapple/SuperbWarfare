package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.CannonConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpeedboatEntity extends Entity implements GeoEntity, IChargeEntity, IVehicleEntity{
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SpeedboatEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = CannonConfig.MK42_HP.get();


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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putFloat("Energy", this.entityData.get(ENERGY));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(ENERGY, compound.getFloat("Energy"));
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

//    @Override
//    public Vec3 getDeltaMovement() {
//        return new Vec3(0, Math.min(super.getDeltaMovement().y, 0), 0);
//    }

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
//            player.displayClientMessage(Component.literal("SubmergedHeight" + new java.text.DecimalFormat("##.##").format(getSubmergedHeight(this))), true);
//        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        double fluidFloat;

        if (this.isInWater()) {
            fluidFloat = -0.025 + 0.05 * getSubmergedHeight(this);
        } else {
            fluidFloat = -0.04;
        }

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, fluidFloat, 0.0));
        this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0.85, 1));
        
        if (this.entityData.get(HEALTH) <= 0) {
            this.ejectPassengers();
            destroy();
        }
        this.refreshDimensions();
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
