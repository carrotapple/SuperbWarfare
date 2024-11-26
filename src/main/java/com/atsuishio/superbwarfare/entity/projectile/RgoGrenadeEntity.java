package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.ProjectileTool;
import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.AnimatedEntity;
import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
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
import software.bernie.geckolib.util.GeckoLibUtil;

public class RgoGrenadeEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(RgoGrenadeEntity.class, EntityDataSerializers.STRING);
    private int fuse = 80;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";

    public RgoGrenadeEntity(EntityType<? extends RgoGrenadeEntity> type, Level world) {
        super(type, world);
    }

    public RgoGrenadeEntity(LivingEntity entity, Level level, int fuse) {
        super(ModEntities.RGO_GRENADE.get(), entity, level);
        this.fuse = fuse;
    }

    public RgoGrenadeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.RGO_GRENADE.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.RGO_GRENADE.get();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void onHit(HitResult result) {
        switch (result.getType()) {
            case BLOCK:
                BlockHitResult blockResult = (BlockHitResult) result;
                BlockPos resultPos = blockResult.getBlockPos();
                BlockState state = this.level().getBlockState(resultPos);
                if (state.getBlock() instanceof BellBlock bell) {
                    bell.attemptToRing(this.level(), resultPos, blockResult.getDirection());
                }
                if (this.tickCount > 2) {
                    ProjectileTool.causeCustomExplode(this, 100f, 4f, 1.2f);
                }

                break;
            case ENTITY:
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity entity = entityResult.getEntity();
                if (this.getOwner() instanceof LivingEntity living) {
                    if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                        living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                        ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
                    }
                }
                if (this.tickCount > 2 && !(entity instanceof DroneEntity)) {
                    ProjectileTool.causeCustomExplode(this, 100f, 4f, 1.2f);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void tick() {
        super.tick();
        --this.fuse;

        if (this.fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                ProjectileTool.causeCustomExplode(this, 100f, 4f, 1.2f);
            }
        }

        if (this.tickCount > 2) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.xo, this.yo, this.zo,
                        1, 0, 0, 0, 0.01, true);
            }
        }
    }

    @Override
    protected float getGravity() {
        return 0.07F;
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationProcedure = procedure;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void droneShoot(Entity drone) {
        Vec3 vec3 = (new Vec3(0.2 * drone.getDeltaMovement().x, 0.2 * drone.getDeltaMovement().y, 0.2 * drone.getDeltaMovement().z));
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * 0.017453292F) * Mth.cos(pX * 0.017453292F);
        float f1 = -Mth.sin((pX + pZ) * 0.017453292F);
        float f2 = Mth.cos(pY * 0.017453292F) * Mth.cos(pX * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0 : -vec3.y, vec3.z));
    }
}
