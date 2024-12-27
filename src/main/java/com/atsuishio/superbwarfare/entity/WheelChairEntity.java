package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WheelChairEntity extends MobileVehicleEntity implements GeoEntity, IVehicleEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 50;
    public static final int MAX_ENERGY = 24000;
    public float leftWheelRot;
    public float rightWheelRot;
    public float leftWheelRotO;
    public float rightWheelRotO;
    public int jumpCoolDown;
    public int handBusyTime;

    public WheelChairEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.WHEEL_CHAIR.get(), world);
    }

    public WheelChairEntity(EntityType<WheelChairEntity> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.1f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
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
        super.hurt(source, amount);
        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(amount);
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (jumpCoolDown > 0 && onGround()) {
            jumpCoolDown--;
        }

        if (handBusyTime > 0) {
            handBusyTime--;
        }

        leftWheelRotO = this.getLeftWheelRot();
        rightWheelRotO = this.getRightWheelRot();

        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.048, 0.0));
        if (this.onGround()) {
            float f = 0.7f + 0.2f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90;
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.95, f));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.95, 0.99));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (level().isClientSide && this.getEnergy() > 0) {
            level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), ModSounds.WHEEL_CHAIR_ENGINE.get(), this.getSoundSource(), (float) (0.2 * this.getDeltaMovement().length()), (random.nextFloat() * 0.1f + 0.7f), false);
        }

        this.setSprinting(this.getDeltaMovement().horizontalDistance() > 0.15);

        this.refreshDimensions();
    }

    @Override
    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

        float diffY = 0;

        if (passenger == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
        } else {
            diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
            this.setYRot(this.getYRot() + Mth.clamp(0.4f * diffY, -5f, 5f));
        }

        if (this.forwardInputDown) {
            power += 0.01f;
            if (this.getEnergy() <= 0 && passenger instanceof Player player) {
                moveWithOutPower(player, true);
            }
        }

        if (this.backInputDown) {
            power -= 0.01f;
            if (this.getEnergy() <= 0 && passenger instanceof Player player) {
                moveWithOutPower(player, false);
            }
        }

        if (this.upInputDown && this.onGround() && this.getEnergy() > 400 && jumpCoolDown == 0) {
            if (passenger instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.WHEEL_CHAIR_JUMP.get(), SoundSource.PLAYERS, 1, 1);
            }
            this.extraEnergy(400);
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.48, 0));
            jumpCoolDown = 5;
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.extraEnergy(1);
        }

        if (passenger instanceof Player player && handBusyTime > 0) {
            var localPlayer = Minecraft.getInstance().player;
            if (localPlayer != null && player.getUUID().equals(localPlayer.getUUID())) {
                localPlayer.handsBusy = true;
            }
        }

        power *= 0.87f;

        float angle = (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1));
        double s0;

        if (Mth.abs(angle) < 90) {
            s0 = this.getDeltaMovement().horizontalDistance();
        } else {
            s0 = -this.getDeltaMovement().horizontalDistance();
        }

        this.setLeftWheelRot((float) (this.getLeftWheelRot() - 1 * s0) - 0.015f * Mth.clamp(0.4f * diffY, -5f, 5f));
        this.setRightWheelRot((float) (this.getRightWheelRot() - 1 * s0) + 0.015f * Mth.clamp(0.4f * diffY, -5f, 5f));

//        if (entity instanceof Player player) {
//            player.displayClientMessage(Component.literal("Angle:" + new java.text.DecimalFormat("##.##").format(this.getRightWheelRot())), true);
//        }

        this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * 0.017453292F) * (this.onGround() ? 1 : 0.1) * power, 0.0, Mth.cos(this.getYRot() * 0.017453292F) * (this.onGround() ? 1 : 0.1) * power));

    }

    public void moveWithOutPower(Player player, boolean forward) {
        power += (forward ? 0.015f : -0.015f);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.BOAT_PADDLE_LAND, SoundSource.PLAYERS, 1, 1);
        }
        player.causeFoodExhaustion(0.03F);

        handBusyTime = 4;
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

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
            CustomExplosion explosion = new CustomExplosion(this.level(), attacker == null ? this : attacker,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), attacker == null ? this : attacker, attacker == null ? this : attacker), 25.0f,
                    this.getX(), this.getY(), this.getZ(), 5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            this.discard();
        }
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
    public float getMaxHealth() {
        return MAX_HEALTH;
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
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
}
