package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WheelChairEntity extends MobileVehicleEntity implements GeoEntity, IVehicleEntity, IChargeEntity {

    public static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(WheelChairEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(WheelChairEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(WheelChairEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 50;
    public static final float MAX_ENERGY = 24000;

    public float leftWheelRot;
    public float rightWheelRot;
    public float leftWheelRotO;
    public float rightWheelRotO;

    public WheelChairEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.WHEEL_CHAIR.get(), world);
    }

    public WheelChairEntity(EntityType<WheelChairEntity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.1f);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, MAX_HEALTH);
        this.entityData.define(POWER, 0f);
        this.entityData.define(ENERGY, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        } else {
            this.entityData.set(HEALTH, MAX_HEALTH);
        }
        compound.putFloat("Energy", this.entityData.get(ENERGY));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(ENERGY, compound.getFloat("Energy"));
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.75F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.05;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
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

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.entityData.set(HEALTH, this.entityData.get(HEALTH) - amount);
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;
        if (player.isShiftKeyDown() && player.getMainHandItem().is(ModItems.CROWBAR.get())) {
            ItemStack stack = ContainerBlockItem.createInstance(this);
            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }
            this.remove(RemovalReason.DISCARDED);
            this.discard();
        } else {
            if (player.getMainHandItem().is(Items.IRON_INGOT)) {
                if (this.entityData.get(HEALTH) < MAX_HEALTH) {
                    this.entityData.set(HEALTH, Math.min(this.entityData.get(HEALTH) + 0.5f * MAX_HEALTH, MAX_HEALTH));
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
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void baseTick() {
        super.baseTick();

        leftWheelRotO = this.getLeftWheelRot();
        rightWheelRotO = this.getRightWheelRot();

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.078, 0.0));
        if (this.onGround()) {
            float f = 0.7f + 0.2f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.99, f));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.99, 0.99));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.entityData.get(HEALTH) <= 0) {
            this.ejectPassengers();
            destroy();
        }

        if (level().isClientSide && this.entityData.get(ENERGY) > 0) {
            level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), ModSounds.WHEEL_CHAIR_ENGINE.get(), this.getSoundSource(), (float) (0.2 * this.getDeltaMovement().length()), (random.nextFloat() * 0.1f + 0.7f), false);
        }

        this.setSprinting(this.getDeltaMovement().length() > 0.15);

        travel();
        this.refreshDimensions();
    }

    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (!(passenger instanceof LivingEntity entity)) return;

        float diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(entity.getYHeadRot() - this.getYRot()));

        this.setYRot(this.getYRot() + Mth.clamp(0.4f * diffY,-5f, 5f));

        if (this.forwardInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.01f);
            if (this.entityData.get(ENERGY) <= 0 && entity instanceof Player player) {
                moveWithOutPower(player, true);
            }
        }

        if (this.backInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.01f);
            if (this.entityData.get(ENERGY) <= 0 && entity instanceof Player player) {
                moveWithOutPower(player, false);
            }
        }

        if (this.upInputDown && this.onGround() && this.entityData.get(ENERGY) > 800) {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.WHEEL_CHAIR_JUMP.get(), SoundSource.PLAYERS, 1, 1);
            }
            this.entityData.set(ENERGY, this.entityData.get(ENERGY) - 800);
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.58, 0));
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.entityData.set(ENERGY, Math.max(this.entityData.get(ENERGY) - 1, 0));
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.87f);

        float angle = (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1));
        double s0;

        if (Mth.abs(angle) < 90) {
            s0 = this.getDeltaMovement().length();
        } else {
            s0 = -this.getDeltaMovement().length();
        }

        this.setLeftWheelRot((float) (this.getLeftWheelRot() - 0.75 * s0) - 0.015f * Mth.clamp(0.4f * diffY,-5f, 5f));
        this.setRightWheelRot((float) (this.getRightWheelRot() - 0.75 * s0) + 0.015f * Mth.clamp(0.4f * diffY,-5f, 5f));

//        if (entity instanceof Player player) {
//            player.displayClientMessage(Component.literal("Angle:" + new java.text.DecimalFormat("##.##").format(this.getRightWheelRot())), true);
//        }

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * 0.017453292F) * this.entityData.get(POWER), 0.0, Mth.cos(this.getYRot() * 0.017453292F) * this.entityData.get(POWER)));
        }
    }

    public void moveWithOutPower(Player player, boolean forward) {
        this.entityData.set(POWER, this.entityData.get(POWER) + (forward ? 0.015f : -0.015f));
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.BOAT_PADDLE_LAND, SoundSource.PLAYERS, 1, 1);
        }
        player.causeFoodExhaustion(0.03F);
        // TODO 手动摇轮椅时像划船一样收起物品
        this.forwardInputDown = false;
        this.backInputDown = false;
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

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -90F, 90.0F);
        entity.yRotO += f3 - f2;
        entity.setYRot(entity.getYRot() + f3 - f2);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private void destroy() {
        if (level() instanceof ServerLevel) {
            level().explode(null, this.getX(), this.getY(), this.getZ(), 0, Level.ExplosionInteraction.NONE);
        }
        this.discard();
    }

    @Override
    public void vehicleShoot(Player player) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getHealth() {
        return this.entityData.get(HEALTH).intValue();
    }

    @Override
    public float getMaxHealth() {
        return (int) MAX_HEALTH;
    }

    @Override
    public boolean isDriver(Player player) {
        return player == this.getFirstPassenger();
    }

    @Override
    public int mainGunRpm() {
        return 0;
    }

    @Override
    public boolean canShoot(Player player) {
        return true;
    }

    @Override
    public int getAmmoCount(Player player) {
        return -1;
    }

    @Override
    public int getEnergy() {
        return this.entityData.get(ENERGY).intValue();
    }

    @Override
    public int getMaxEnergy() {
        return (int) MAX_ENERGY;
    }

    @Override
    public void charge(int amount) {
        this.entityData.set(ENERGY, Math.min(this.entityData.get(ENERGY) + amount, MAX_ENERGY));
    }

    @Override
    public boolean canCharge() {
        return this.entityData.get(ENERGY) < MAX_ENERGY;
    }
}
