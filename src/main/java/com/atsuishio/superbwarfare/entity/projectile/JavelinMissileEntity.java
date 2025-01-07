package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.AnimatedEntity;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.ProjectileTool;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.commands.arguments.EntityAnchorArgument;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
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
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        entity, this.explosion_damage, this.explosion_radius, this.monsterMultiplier);
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
                ProjectileTool.causeCustomExplode(this,
                        ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        this, this.explosion_damage, this.explosion_radius, this.monsterMultiplier);
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
                this.entityData.set(TARGET_Y, (float) entity.getEyeY());
                this.entityData.set(TARGET_Z, (float) entity.getZ());
                if ((!entity.getPassengers().isEmpty() || entity instanceof VehicleEntity) && entity.tickCount %((int)Math.max(0.04 * this.distanceTo(entity),2)) == 0) {
                    entity.level().playSound(null, entity.getOnPos(), entity instanceof Pig ? SoundEvents.PIG_HURT : ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 2, 1f);
                }
            }
        } else  {
            this.entityData.set(TARGET_X, (float)(this.getX() + this.getDeltaMovement().scale(10).x));
            this.entityData.set(TARGET_Y, (float)(this.getY() + this.getDeltaMovement().scale(10).y));
            this.entityData.set(TARGET_Z, (float)(this.getZ() + this.getDeltaMovement().scale(10).z));
        }


        double px = this.getX();
        double ex = this.entityData.get(TARGET_X);
        double pz = this.getZ();
        double ez = this.entityData.get(TARGET_Z);
        boolean dir = Math.sqrt(Math.pow(px - ex, 2) + Math.pow(pz - ez, 2)) < 10;

        if (this.tickCount == 4) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, this.xo, this.yo, this.zo, 15, 0.8, 0.8, 0.8, 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 10, 0.8, 0.8, 0.8, 0.01, true);
            }
        }

        if (this.tickCount > 3) {
            if (entityData.get(TOP)) {
                if (!dir) {
                    this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y) + Mth.clamp(4 * this.tickCount, 0, 90), this.entityData.get(TARGET_Z)));
                } else {
                    this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y) + (entity instanceof EnderDragon ? -3 : 0), this.entityData.get(TARGET_Z)));
                    this.setDeltaMovement(this.getDeltaMovement().scale(1.1));
                }

            } else {
                this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y) + (entity instanceof EnderDragon ? -3 : 0), this.entityData.get(TARGET_Z)));
            }
        }

        if (this.tickCount > 4) {
            this.setDeltaMovement(new Vec3(
                    0.7f * this.getDeltaMovement().x + (dir ? 3 : 1.3f) * this.getLookAngle().x,
                    0.7f * this.getDeltaMovement().y + (dir ? 3 : 1.3f) * this.getLookAngle().y,
                    0.7f * this.getDeltaMovement().z + (dir ? 3 : 1.3f) * this.getLookAngle().z
            ));
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
    }

    private void look(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget) {
        Vec3 vec3 = pAnchor.apply(this);
        double d0 = (pTarget.x - vec3.x) * 0.2;
        double d1 = (pTarget.y - vec3.y) * 0.2;
        double d2 = (pTarget.z - vec3.z) * 0.2;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        this.setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
        this.setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
        this.setYHeadRot(this.getYRot());
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
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
