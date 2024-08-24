package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;

public class ClaymoreEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Float> ROT_Y = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.FLOAT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";

    public ClaymoreEntity(EntityType<? extends ClaymoreEntity> type, Level world) {
        super(type, world);
    }

    public ClaymoreEntity(EntityType<? extends ClaymoreEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public ClaymoreEntity(LivingEntity entity, Level level) {
        super(ModEntities.CLAYMORE.get(), entity, level);
    }

    public ClaymoreEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.CLAYMORE.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ROT_Y, 0f);
    }

    public void setRotY(float rotY) {
        this.entityData.set(ROT_Y, rotY);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CLAYMORE_MINE.get();
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
                this.bounce(blockResult.getDirection());
                break;
            case ENTITY:
                this.bounce(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.25, 1.0, 0.25));
                break;
            default:
                break;
        }
    }

    private void bounce(Direction direction) {
        switch (direction.getAxis()) {
            case X:
                this.setDeltaMovement(this.getDeltaMovement().multiply(-0.5, 0.75, 0.75));
                break;
            case Y:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, -0.01, 0.75));
                if (this.getDeltaMovement().y() < this.getGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
                }
                break;
            case Z:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, 0.75, -0.5));
                break;
        }
    }

    @Override
    public void tick() {
        super.tick();
        var level = this.level();
        var x = this.getX();
        var y = this.getY();
        var z = this.getZ();

        if (this.tickCount >= 12000) {
            if (!this.level().isClientSide()) this.discard();
        }

        if (this.tickCount >= 20) {
            final Vec3 center = new Vec3(x + 1.5 * this.getLookAngle().x, y + 1.5 * this.getLookAngle().y, z + 1.5 * this.getLookAngle().z);
            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2.5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                var condition = this.getOwner() != target
                        && target instanceof LivingEntity
                        && !(target instanceof TargetEntity)
                        && !(target instanceof Player player && (player.isCreative() || player.isSpectator()))
                        && (!this.isAlliedTo(target) || target.getTeam() == null || target.getTeam().getName().equals("TDM"))
                        && !target.isShiftKeyDown();
                if (!condition) continue;

                if (!level.isClientSide()) {
                    if (!this.level().isClientSide()) {
                        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
                    }
                    this.discard();
                }

                ModUtils.queueServerWork(1, () -> {
                    if (!level.isClientSide())
                        triggerExplode(target);
                });
            }
        }

        this.refreshDimensions();
    }

    private void triggerExplode(Entity target) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeMineDamage(this.level().registryAccess(), this.getOwner()), 80f,
                target.getX(), target.getY(), target.getZ(), 6f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
    }

    @Override
    protected void updateRotation() {
        this.setXRot(0);
        this.setYRot(this.entityData.get(ROT_Y));
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.5);
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
}
