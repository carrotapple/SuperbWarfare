package com.atsuishio.superbwarfare.entity.vehicle.base;

import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.C4Entity;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.entity.projectile.FlareDecoyEntity;
import com.atsuishio.superbwarfare.entity.projectile.LaserEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Vector3f;

public abstract class MobileVehicleEntity extends EnergyVehicleEntity {

    public static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(MobileVehicleEntity.class, EntityDataSerializers.FLOAT);

    public boolean leftInputDown;
    public boolean rightInputDown;
    public boolean forwardInputDown;
    public boolean backInputDown;
    public boolean upInputDown;
    public boolean downInputDown;
    public boolean decoyInputDown;
    public double lastTickSpeed;
    public double lastTickVerticalSpeed;
    public int collisionCoolDown;

    public MobileVehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void playerTouch(Player pPlayer) {
        if (pPlayer.isCrouching() && !this.level().isClientSide) {
            double entitySize = pPlayer.getBbWidth() * pPlayer.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            double f1 = Math.min(thisSize / entitySize, 4);
            this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(pPlayer.position().vectorTo(this.position()).toVector3f()).scale(0.15 * f * pPlayer.getDeltaMovement().length())));
            pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(new Vec3(this.position().vectorTo(pPlayer.position()).toVector3f()).scale(0.1 * f1 * pPlayer.getDeltaMovement().length())));
        }
    }

    @Override
    public void baseTick() {
        lastTickSpeed = new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.06, this.getDeltaMovement().z).length();
        lastTickVerticalSpeed = this.getDeltaMovement().y + 0.06;
        if (collisionCoolDown > 0) {
            collisionCoolDown--;
        }
        super.baseTick();
        preventStacking();
        crushEntities(this.getDeltaMovement());
        if (!(this instanceof DroneEntity)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.06, 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        collideLilyPadBlock();
        this.refreshDimensions();
    }

    public void collideLilyPadBlock() {
        if (level() instanceof ServerLevel) {
            AABB aabb = getBoundingBox().inflate(0.05).move(this.getDeltaMovement().scale(0.6));
            BlockPos.betweenClosedStream(aabb).forEach((pos) -> {
                BlockState blockstate = this.level().getBlockState(pos);
                if (blockstate.is(Blocks.LILY_PAD)) {
                    this.level().destroyBlock(pos, true);
                }
            });
        }
    }

    public void collideBlock() {
        if (level() instanceof ServerLevel) {
            if (!VehicleConfig.COLLISION_DESTROY_BLOCKS.get()) return;

            AABB aabb = getBoundingBox().move(this.getDeltaMovement().scale(0.6));
            BlockPos.betweenClosedStream(aabb).forEach((pos) -> {
                BlockState blockstate = this.level().getBlockState(pos);
                if (blockstate.is(ModTags.Blocks.SOFT_COLLISION)) {
                    this.level().destroyBlock(pos, true);
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.96));
                }
            });
        }
    }

    public void collideHardBlock() {
        if (level() instanceof ServerLevel) {
            if (!VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS.get()) return;

            AABB aabb = getBoundingBox().move(this.getDeltaMovement().scale(0.6));
            BlockPos.betweenClosedStream(aabb).forEach((pos) -> {
                BlockState blockstate = this.level().getBlockState(pos);
                if (blockstate.is(ModTags.Blocks.HARD_COLLISION)) {
                    this.level().destroyBlock(pos, true);
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.6));
                }
            });
        }
    }

    @Override
    public void move(@NotNull MoverType movementType, @NotNull Vec3 movement) {
        super.move(movementType, movement);
        if (level() instanceof ServerLevel) {
            if (lastTickSpeed < 0.3 || collisionCoolDown > 0 || this instanceof DroneEntity) return;
            Entity driver = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_DRIVER_UUID));

            if ((verticalCollision)) {
                if (this instanceof HelicopterEntity) {
                    this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, driver == null ? this : driver), (float) (20 * ((lastTickSpeed - 0.3) * (lastTickSpeed - 0.3))));
                    this.bounceVertical(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                } else if (Mth.abs((float) lastTickVerticalSpeed) > 0.4) {
                    this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, driver == null ? this : driver), (float) (96 * ((Mth.abs((float) lastTickVerticalSpeed) - 0.4) * (lastTickSpeed - 0.3) * (lastTickSpeed - 0.3))));
                    if (!this.level().isClientSide) {
                        this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                    }
                    this.bounceVertical(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                }
            }

            if (this.horizontalCollision) {
                this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, driver == null ? this : driver), (float) (126 * ((lastTickSpeed - 0.4) * (lastTickSpeed - 0.4))));
                this.bounceHorizontal(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                }
                collisionCoolDown = 4;
                crash = true;
                this.entityData.set(POWER, 0.4f * entityData.get(POWER));
            }
        }
    }

    public void bounceHorizontal(Direction direction) {
        switch (direction.getAxis()) {
            case X:
                this.setDeltaMovement(this.getDeltaMovement().multiply(-0.4, 0.99, 0.99));
                break;
            case Z:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.99, 0.99, -0.4));
                break;
        }
    }

    public void bounceVertical(Direction direction) {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
        }
        collisionCoolDown = 4;
        crash = true;
        if (direction.getAxis() == Direction.Axis.Y) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, -0.8, 0.9));
        }
    }

    /**
     * 防止载具堆叠
     */
    public void preventStacking() {
        var Box = getBoundingBox();

        var entities = level().getEntities(EntityTypeTest.forClass(Entity.class), Box, entity -> entity != this && entity != getFirstPassenger() && entity.getVehicle() == null)
                .stream().filter(entity -> entity instanceof VehicleEntity)
                .toList();

        for (var entity : entities) {
            Vec3 toVec = this.position().add(new Vec3(1, 1, 1).scale(random.nextFloat() * 0.01f + 1f)).vectorTo(entity.position());
            Vec3 velAdd = toVec.normalize().scale(Math.max((this.getBbWidth() + 2) - position().distanceTo(entity.position()), 0) * 0.002);
            double entitySize = entity.getBbWidth() * entity.getBbHeight();
            double thisSize = this.getBbWidth() * this.getBbHeight();
            double f = Math.min(entitySize / thisSize, 2);
            double f1 = Math.min(thisSize / entitySize, 2);

            this.pushNew(-f * velAdd.x, -f * velAdd.y, -f * velAdd.z);
            entity.push(f1 * velAdd.x, f1 * velAdd.y, f1 * velAdd.z);
        }
    }

    public void pushNew(double pX, double pY, double pZ) {
        this.setDeltaMovement(this.getDeltaMovement().add(pX, pY, pZ));
    }

    /**
     * 撞击实体并造成伤害
     *
     * @param velocity 动量
     */
    public void crushEntities(Vec3 velocity) {
        if (level() instanceof ServerLevel) {
            if (!this.canCrushEntities()) return;
            if (velocity.horizontalDistance() < 0.25) return;
            if (isRemoved()) return;
            var frontBox = getBoundingBox().move(velocity.scale(0.6));
            var velAdd = velocity.add(0, 0, 0).scale(0.9);

            var entities = level().getEntities(EntityTypeTest.forClass(Entity.class), frontBox,
                            entity -> entity != this && entity != getFirstPassenger() && entity.getVehicle() == null)
                    .stream().filter(entity -> {
                                if (entity.isAlive()
                                        && !(entity instanceof ItemEntity || entity instanceof Projectile || entity instanceof ProjectileEntity || entity instanceof LaserEntity || entity instanceof FlareDecoyEntity || entity instanceof AreaEffectCloud || entity instanceof C4Entity)
                                        && !(entity instanceof Player player && (player.isSpectator() || player.isCreative()))) {
                                    var type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                                    if (type == null) return false;
                                    return !VehicleConfig.COLLISION_ENTITY_BLACKLIST.get().contains(type.toString());
                                }
                                return false;
                            }
                    )
                    .toList();

            for (var entity : entities) {
                double entitySize = entity.getBbWidth() * entity.getBbHeight();
                double thisSize = this.getBbWidth() * this.getBbHeight();
                double f = Math.min(entitySize / thisSize, 2);
                double f1 = Math.min(thisSize / entitySize, 4);

                if (velocity.length() > 0.3 && getBoundingBox().distanceToSqr(entity.getBoundingBox().getCenter()) < 1) {
                    if (!this.level().isClientSide) {
                        this.level().playSound(null, this, ModSounds.VEHICLE_STRIKE.get(), this.getSoundSource(), 1, 1);
                    }
                    if (!(entity instanceof TargetEntity)) {
                        this.pushNew(-f * velAdd.x, -f * velAdd.y, -f * velAdd.z);
                    }
                    entity.push(f1 * velAdd.x, f1 * velAdd.y, f1 * velAdd.z);
                    entity.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), (float) (thisSize * 20 * ((velocity.length() - 0.3) * (velocity.length() - 0.3))));
                    if (entities instanceof VehicleEntity) {
                        this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), entity, entity.getFirstPassenger() == null ? entity : entity.getFirstPassenger()), (float) (entitySize * 10 * ((velocity.length() - 0.3) * (velocity.length() - 0.3))));
                    }
                } else {
                    entity.push(0.3 * f1 * velAdd.x, 0.3 * f1 * velAdd.y, 0.3 * f1 * velAdd.z);
                }
            }
        }
    }

    public Vector3f getForwardDirection() {
        return new Vector3f(
                Mth.sin(-getYRot() * ((float) Math.PI / 180)),
                0.0f,
                Mth.cos(getYRot() * ((float) Math.PI / 180))
        ).normalize();
    }

    public Vector3f getRightDirection() {
        return new Vector3f(
                Mth.cos(-getYRot() * ((float) Math.PI / 180)),
                0.0f,
                Mth.sin(getYRot() * ((float) Math.PI / 180))
        ).normalize();
    }

    public SoundEvent getEngineSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(POWER, 0f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(POWER, compound.getFloat("Power"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Power", this.entityData.get(POWER));
    }

    public boolean canCrushEntities() {
        return true;
    }
}
