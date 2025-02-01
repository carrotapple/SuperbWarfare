package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.AnimatedEntity;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class JavelinMissileEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<String> TARGET_UUID = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> TOP = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> TARGET_X = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TARGET_Y = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TARGET_Z = SynchedEntityData.defineId(JavelinMissileEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float monsterMultiplier = 0.0f;
    private float damage = 500.0f;
    private float explosion_damage = 140f;
    private float explosion_radius = 6f;
    private boolean distracted = false;

    public JavelinMissileEntity(EntityType<? extends JavelinMissileEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public JavelinMissileEntity(LivingEntity entity, Level level, float damage, float explosion_damage, float explosion_radius) {
        super(ModEntities.JAVELIN_MISSILE.get(), entity, level);
        this.damage = damage;
        this.explosion_damage = explosion_damage;
        this.explosion_radius = explosion_radius;
    }

    public JavelinMissileEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.JAVELIN_MISSILE.get(), level);
    }

    public void setMonsterMultiplier(float monsterMultiplier) {
        this.monsterMultiplier = monsterMultiplier;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.JAVELIN_MISSILE.get();
    }

    public void setTargetUuid(String uuid) {
        this.entityData.set(TARGET_UUID, uuid);
    }

    public void setTargetPosition(Float targetX, Float targetY, Float targetZ) {
        this.entityData.set(TARGET_X, targetX);
        this.entityData.set(TARGET_Y, targetY);
        this.entityData.set(TARGET_Z, targetZ);
    }

    public void setAttackMode(boolean mode) {
        this.entityData.set(TOP, mode);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TARGET_UUID, "none");
        this.entityData.define(TOP, false);
        this.entityData.define(TARGET_X, 0f);
        this.entityData.define(TARGET_Y, 0f);
        this.entityData.define(TARGET_Z, 0f);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        float damageMultiplier = 1 + this.monsterMultiplier;
        Entity entity = result.getEntity();
        if (entity == this.getOwner() || entity == this.getVehicle()) return;
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        if (entity instanceof Monster monster) {
            monster.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), (entityData.get(TOP) ? 1.3f : 1f) * this.damage * damageMultiplier);
        } else {
            entity.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), (entityData.get(TOP) ? 1.3f : 1f) * this.damage);
        }

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                causeExplode(result);
            }
        }

        this.discard();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos resultPos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);

        if (state.getBlock() instanceof BellBlock bell) {
            bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
        }

        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                causeExplode(blockHitResult);
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = EntityFindUtil.findEntity(this.level(), entityData.get(TARGET_UUID));
        List<Entity> decoy = SeekTool.seekLivingEntities(this, this.level(), 48 , 160);

        for (var e : decoy) {
            if (e instanceof FlareDecoyEntity flareDecoy && !distracted) {
                this.entityData.set(TARGET_UUID, flareDecoy.getStringUUID());
                distracted = true;
            }
        }

        if (entity != null) {
            if (entity.level() instanceof ServerLevel) {
                this.entityData.set(TARGET_X, (float) entity.getX());
                this.entityData.set(TARGET_Y, (float) entity.getY() + 0.5f * entity.getBbHeight());
                this.entityData.set(TARGET_Z, (float) entity.getZ());
                if ((!entity.getPassengers().isEmpty() || entity instanceof VehicleEntity) && entity.tickCount %((int)Math.max(0.04 * this.distanceTo(entity),2)) == 0) {
                    entity.level().playSound(null, entity.getOnPos(), entity instanceof Pig ? SoundEvents.PIG_HURT : ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 2, 1f);
                }
            }
        }


        double px = this.getX();
        double ex = this.entityData.get(TARGET_X);
        double pz = this.getZ();
        double ez = this.entityData.get(TARGET_Z);
        boolean dir = Math.sqrt(Math.pow(px - ex, 2) + Math.pow(pz - ez, 2)) < 30;
        Vec3 targetPos = new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y) + (entity instanceof EnderDragon ? -3 : 0), this.entityData.get(TARGET_Z));
        if (entity != null) {
            Vec3 toVec = getEyePosition().vectorTo(targetPos.add(entity.getDeltaMovement().scale(0.5))).normalize();
            if (this.tickCount > 3) {
                if (entityData.get(TOP)) {
                    if (!dir) {
                        Vec3 targetTopPos = new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y) + Mth.clamp(5 * this.tickCount, 0, 90), this.entityData.get(TARGET_Z));
                        Vec3 toTopVec = getEyePosition().vectorTo(targetTopPos).normalize();
                        setDeltaMovement(getDeltaMovement().add(toTopVec.scale(0.5)));
                    } else {
                        boolean lostTarget = this.getY() < entity.getY();
                        if (!lostTarget) {
                            setDeltaMovement(getDeltaMovement().add(toVec.scale(1)).scale(0.87));
                        }
                    }
                } else {
                    boolean lostTarget = (VectorTool.calculateAngle(getDeltaMovement(), toVec) > 80);
                    if (!lostTarget) {
                        setDeltaMovement(getDeltaMovement().add(toVec.scale(1)).scale(0.87));
                    }
                }
            }
        }


        if (this.tickCount == 4) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, this.xo, this.yo, this.zo, 15, 0.8, 0.8, 0.8, 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 10, 0.8, 0.8, 0.8, 0.01, true);
            }
        }

        if (this.tickCount > 4) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 1, 0, 0, 0, 0, true);
            }
        }

        if (this.tickCount > 200 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        this, this.explosion_damage, this.explosion_radius, this.monsterMultiplier);
            }
            this.discard();
        }

        // 控制速度
        if (this.getDeltaMovement().length() < 2.6) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.06, 1.06, 1.06));
        }

        if (this.getDeltaMovement().length() > 2.9) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.9, 0.9));
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(0.96, 0.96, 0.96));
    }

    private void causeExplode(HitResult result) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(),
                        this,
                        this.getOwner()),
                explosion_damage,
                this.getX(),
                this.getEyeY(),
                this.getZ(),
                explosion_radius,
                ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).
                setDamageMultiplier(this.monsterMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnSmallExplosionParticles(this.level(), result.getLocation());
    }

    private PlayState movementPredicate(AnimationState<JavelinMissileEntity> event) {
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.jvm.idle"));
    }

    @Override
    protected float getGravity() {
        return 0F;
    }

    public String getSyncedAnimation() {
        return null;
    }

    public void setAnimation(String animation) {
    }

    @Override
    public void setAnimationProcedure(String procedure) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
