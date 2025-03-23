package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.MortarEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class WheelChairEntity extends MobileVehicleEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
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
    public void playerTouch(Player pPlayer) {
        if (this.position().distanceTo(pPlayer.position()) > 1.4 || pPlayer == this.getFirstPassenger()) return;
        if (!this.level().isClientSide) {
            double entitySize = pPlayer.getBbWidth() * pPlayer.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(pPlayer.position().vectorTo(this.position()).toVector3f()).scale(0.5 * f * pPlayer.getDeltaMovement().length())));
            this.setYRot(pPlayer.getYHeadRot());
        }
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .multiply(2, ModDamageTypes.VEHICLE_STRIKE);
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
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound(ModSounds.WHEEL_STEP.get(), (float) (getDeltaMovement().length() * 0.5), random.nextFloat() * 0.15f + 1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean sendFireStarParticleOnHurt() {
        return false;
    }

    @Override
    public void baseTick() {
        if (jumpCoolDown > 0 && onGround()) {
            jumpCoolDown--;
        }

        if (handBusyTime > 0) {
            handBusyTime--;
        }

        super.baseTick();
        if (this.onGround()) {
            float f = (float) Mth.clamp(0.85f + 0.05f * Mth.abs(90 - (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1))) / 90, 0.01, 0.99);
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.95, f));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.95, 0.99));
        }
        this.setSprinting(this.getDeltaMovement().horizontalDistance() > 0.15);
        attractEntity();

        this.terrainCompat();

        this.refreshDimensions();
    }

    public boolean inBlock(Vec3 vec3) {
        AABB aabb = new AABB(vec3, vec3);
        return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = this.level().getBlockState(p_201942_);
            return !blockstate.isAir() && blockstate.isSuffocating(this.level(), p_201942_) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), p_201942_).move((double) p_201942_.getX(), (double) p_201942_.getY(), (double) p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }

    // 地形适应测试
    public void terrainCompat() {
        Matrix4f transform = this.getWheelsTransform(1);

        // 点位
        // 左前
        Vector4f positionLF = transformPosition(transform, 0.45f, 0, 0.5f);
        // 右前
        Vector4f positionRF = transformPosition(transform, -0.45f, 0, 0.5f);
        // 左后
        Vector4f positionLB = transformPosition(transform, 0.45f, 0, -0.5f);
        // 右后
        Vector4f positionRB = transformPosition(transform, -0.45f, 0, -0.5f);

        Vec3 p1 = new Vec3(positionLF.x, positionLF.y, positionLF.z);
        Vec3 p2 = new Vec3(positionRF.x, positionRF.y, positionRF.z);
        Vec3 p3 = new Vec3(positionLB.x, positionLB.y, positionLB.z);
        Vec3 p4 = new Vec3(positionRB.x, positionRB.y, positionRB.z);

        // 测试用粒子效果，用于确定点位位置
//        var passenger = this.getFirstPassenger();
//
//        if (passenger != null) {
//            if (passenger.level() instanceof ServerLevel serverLevel) {
//                sendParticle(serverLevel, ParticleTypes.END_ROD, p1.x, p1.y, p1.z, 1, 0, 0, 0, 0, true);
//                sendParticle(serverLevel, ParticleTypes.END_ROD, p2.x, p2.y, p2.z, 1, 0, 0, 0, 0, true);
//                sendParticle(serverLevel, ParticleTypes.END_ROD, p3.x, p3.y, p3.z, 1, 0, 0, 0, 0, true);
//                sendParticle(serverLevel, ParticleTypes.END_ROD, p4.x, p4.y, p4.z, 1, 0, 0, 0, 0, true);
//            }
//        }

        // 确定点位是否在墙里来调整点位高度
        float p1y = (float) Mth.clamp(this.traceBlockY(p1, 2), -1, 1);
        float p2y = (float) Mth.clamp(this.traceBlockY(p2, 2), -1, 1);
        float p3y = (float) Mth.clamp(this.traceBlockY(p3, 2), -1, 1);
        float p4y = (float) Mth.clamp(this.traceBlockY(p4, 2), -1, 1);

        p1 = p1.add(new Vec3(0, p1y, 0));
        p2 = p2.add(new Vec3(0, p2y, 0));
        p3 = p3.add(new Vec3(0, p3y, 0));
        p4 = p4.add(new Vec3(0, p4y, 0));

        // 通过点位位置获取角度
        // 左后-左前
        Vec3 lbf = p3.vectorTo(p1);
        // 右后-右前
        Vec3 rbf = p4.vectorTo(p2);
        // 左前-右前
        Vec3 lrf = p1.vectorTo(p2);
        // 左后-右后
        Vec3 lrb = p3.vectorTo(p4);

        double x1 = getXRotFromVector(lbf);
        double x2 = getXRotFromVector(rbf);
        double z1 = getXRotFromVector(lrf);
        double z2 = getXRotFromVector(lrb);

        float diffX = Math.clamp(-90f, 90f, Mth.wrapDegrees((float) ((x1 + x2) / 2) - this.getXRot()));
        this.setXRot(Mth.clamp(this.getXRot() + 0.15f * diffX, -90f, 90f));

        float diffZ = Math.clamp(-90f, 90f, Mth.wrapDegrees((float) ((z1 + z2) / 2) - this.getRoll()));
        this.setZRot(Mth.clamp(this.getRoll() + 0.15f * diffZ, -90f, 90f));
    }

    public Matrix4f getWheelsTransform(float ticks) {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) Mth.lerp(ticks, xo, getX()), (float) Mth.lerp(ticks, yo, getY()), (float) Mth.lerp(ticks, zo, getZ()));
        transform.rotate(Axis.YP.rotationDegrees(-Mth.lerp(ticks, yRotO, getYRot())));
        return transform;
    }

    public double traceBlockY(Vec3 pos, double maxLength) {
        var res = this.level().clip(new ClipContext(pos.add(0, maxLength / 2d, 0), pos.add(0, -maxLength / 2d, 0),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (res.getType() == HitResult.Type.BLOCK) {
            return pos.y - res.getLocation().y;
        }
        if (!this.level().noCollision(new AABB(pos, pos))) {
            return pos.y;
        }
        return pos.y - maxLength / 2d;
    }

    public boolean hasEnoughSpaceFor(Entity pEntity) {
        return pEntity.getBbWidth() < this.getBbWidth();
    }

    public void attractEntity() {
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for (Entity entity : list) {
                if (!entity.hasPassenger(this) && flag && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && (entity instanceof LivingEntity || entity instanceof MortarEntity) && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                    entity.startRiding(this);
                }
            }
        }
    }

    @Override
    public void travel() {
        Entity passenger = this.getFirstPassenger();

        float diffY = 0;

        if (passenger == null) {
            this.leftInputDown = false;
            this.rightInputDown = false;
            this.forwardInputDown = false;
            this.backInputDown = false;
        } else if (passenger instanceof Player) {
            if (level().isClientSide && this.getEnergy() > 0) {
                level().playLocalSound(this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(), this.getEngineSound(), this.getSoundSource(), Math.min((this.forwardInputDown || this.backInputDown ? 7.5f : 5f) * 2 * Mth.abs(this.entityData.get(POWER)), 0.25f), (random.nextFloat() * 0.1f + 1f), false);
            }
            diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
            this.setYRot(this.getYRot() + Mth.clamp(0.4f * diffY, -5f, 5f));
        }

        if (this.forwardInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) + 0.01f);
            if (this.getEnergy() <= 0 && passenger instanceof Player player) {
                moveWithOutPower(player, true);
            }
        }

        if (this.backInputDown) {
            this.entityData.set(POWER, this.entityData.get(POWER) - 0.01f);
            if (this.getEnergy() <= 0 && passenger instanceof Player player) {
                moveWithOutPower(player, false);
            }
        }

        if (this.upInputDown && this.onGround() && this.getEnergy() > 400 && jumpCoolDown == 0) {
            if (passenger instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.WHEEL_CHAIR_JUMP.get(), SoundSource.PLAYERS, 1, 1);
            }
            this.consumeEnergy(VehicleConfig.WHEELCHAIR_JUMP_ENERGY_COST.get());
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.6, 0));
            jumpCoolDown = 3;
        }

        if (this.forwardInputDown || this.backInputDown) {
            this.consumeEnergy(VehicleConfig.WHEELCHAIR_MOVE_ENERGY_COST.get());
        }

        if (passenger instanceof Player player && player.level().isClientSide && this.handBusyTime > 0) {
            var localPlayer = Minecraft.getInstance().player;
            if (localPlayer != null && player.getUUID().equals(localPlayer.getUUID())) {
                localPlayer.handsBusy = true;
            }
        }

        this.entityData.set(POWER, this.entityData.get(POWER) * 0.87f);

        float angle = (float) calculateAngle(this.getDeltaMovement(), this.getViewVector(1));
        double s0;

        if (Mth.abs(angle) < 90) {
            s0 = this.getDeltaMovement().horizontalDistance();
        } else {
            s0 = -this.getDeltaMovement().horizontalDistance();
        }

        this.setLeftWheelRot((float) (this.getLeftWheelRot() - 1.25 * s0) - 0.015f * Mth.clamp(0.4f * diffY, -5f, 5f));
        this.setRightWheelRot((float) (this.getRightWheelRot() - 1.25 * s0) + 0.015f * Mth.clamp(0.4f * diffY, -5f, 5f));

        this.setDeltaMovement(this.getDeltaMovement().add(getViewVector(1).scale((this.onGround() ? 1 : 0.1) * this.entityData.get(POWER))));
    }

    public void moveWithOutPower(Player player, boolean forward) {
        this.entityData.set(POWER, this.entityData.get(POWER) + (forward ? 0.015f : -0.015f));
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.BOAT_PADDLE_LAND, SoundSource.PLAYERS, 1, 1);
        }
        player.causeFoodExhaustion(0.03F);

        this.handBusyTime = 4;
        this.forwardInputDown = false;
        this.backInputDown = false;
    }

    @Override
    public SoundEvent getEngineSound() {
        return ModSounds.WHEEL_CHAIR_ENGINE.get();
    }

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f2 = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f3 = Mth.clamp(f2, -90F, 90.0F);
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

        Matrix4f transform = getVehicleTransform(1);

        float x = 0f;
        float y = 0.7f;
        float z = 0f;
        y += (float) passenger.getMyRidingOffset();

        int i = this.getSeatIndex(passenger);

        if (i == 0) {
            Vector4f worldPosition = transformPosition(transform, x, y, z);
            passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
            callback.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);
        }

        if (passenger != this.getFirstPassenger()) {
            passenger.setXRot(passenger.getXRot() + (getXRot() - xRotO));
        }
    }

    @Override
    public void destroy() {
        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), getAttacker(), getAttacker()), 10f,
                    this.getX(), this.getY(), this.getZ(), 2f, ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnSmallExplosionParticles(this.level(), this.position());
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
        return VehicleConfig.WHEELCHAIR_HP.get();
    }

    @Override
    public int getMaxEnergy() {
        return VehicleConfig.WHEELCHAIR_MAX_ENERGY.get();
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/wheel_chair_icon.png");
    }
}
