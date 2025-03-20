package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

// TODO 修复朝北吸附时角度不正确的问题
public class C4Entity extends Projectile implements GeoEntity {

    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<String> TARGET_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected boolean inGround;
    protected boolean onEntity;
    @Nullable
    private BlockState lastState;

    public C4Entity(EntityType<C4Entity> type, Level level) {
        super(type, level);
    }

    public C4Entity(LivingEntity owner, Level level) {
        super(ModEntities.C_4.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LAST_ATTACKER_UUID, "undefined");
        this.entityData.define(HEALTH, 10f);
        this.entityData.define(TARGET_UUID, "undefined");
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putString("Target", this.entityData.get(TARGET_UUID));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));

        if (this.lastState != null) {
            compound.put("InBlockState", NbtUtils.writeBlockState(this.lastState));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Health")) {
            this.entityData.set(HEALTH, compound.getFloat("Health"));
        }

        if (compound.contains("LastAttacker")) {
            this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        }

        if (compound.contains("Target")) {
            this.entityData.set(TARGET_UUID, compound.getString("Target"));
        }

        if (compound.contains("InBlockState", 10)) {
            this.lastState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("InBlockState"));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.getOwner() == player && player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) {
                this.discard();
            }

            if (!player.getAbilities().instabuild) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.C4_BOMB.get()));
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

//    @Override
//    public void tick() {
//        super.tick();
//        Level level = this.level();
//
//        if (this.tickCount >= ExplosionConfig.C4_EXPLOSION_COUNTDOWN.get()) {
//            this.explode();
//        }
//
//        if (inGround && checkNoClip()) {
//            inGround = false;
//        }
//
//        if (!inGround && !onEntity) {
//            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05, 0.0));
//        }
//
//        Entity target = EntityFindUtil.findEntity(level(), entityData.get(TARGET_UUID));
//
//        if (onEntity) {
//            if (target != null) {
//                setPosRaw(target.getX(), target.getY() + target.getBbHeight(), target.getZ());
//            } else {
//                onEntity = false;
//            }
//        }
//
//        this.refreshDimensions();
//    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= ExplosionConfig.C4_EXPLOSION_COUNTDOWN.get()) {
            this.explode();
        }

        Vec3 motion = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = motion.horizontalDistance();
            this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(motion.y, d0) * (double) (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir()) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.inGround) {
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            }
        } else {
            Vec3 position = this.position();
            Vec3 nextPosition = position.add(motion);
            HitResult hitresult = this.level().clip(new ClipContext(position, nextPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                nextPosition = hitresult.getLocation();
            }

            while (!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(position, nextPosition);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                    this.onHit(hitresult);
                    this.hasImpulse = true;
                    break;
                }

                if (entityhitresult == null) {
                    break;
                }

                hitresult = null;
            }

            if (this.isRemoved()) {
                return;
            }

            motion = this.getDeltaMovement();
            double pX = motion.x;
            double pY = motion.y;
            double pZ = motion.z;

            double nX = this.getX() + pX;
            double nY = this.getY() + pY;
            double nZ = this.getZ() + pZ;

            this.updateRotation();

            float f = 0.99F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    this.level().addParticle(ParticleTypes.BUBBLE, nX - pX * 0.25D, nY - pY * 0.25D, nZ - pZ * 0.25D, pX, pY, pZ);
                }

                f = this.getWaterInertia();
            }

            this.setDeltaMovement(motion.scale(f));
            if (!this.isNoGravity()) {
                Vec3 vec34 = this.getDeltaMovement();
                this.setDeltaMovement(vec34.x, vec34.y - (double) 0.05F, vec34.z);
            }

            this.setPos(nX, nY, nZ);
            this.checkInsideBlocks();
        }
    }

    private boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.multiply(this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F));
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        if (pType != MoverType.SELF && this.shouldFall()) {
            this.startFalling();
        }
    }

//    public boolean checkNoClip() {
//        return level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(getViewVector(1).normalize().scale(-0.25)),
//                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK;
//    }

    public void look(Vec3 pTarget) {
        double d0 = pTarget.x;
        double d1 = pTarget.y;
        double d2 = pTarget.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
//        setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
        setYHeadRot(getYRot());
        this.xRotO = getXRot();
        this.yRotO = getYRot();
    }

    @Override
    protected void updateRotation() {
        if (getDeltaMovement().length() > 0.05 && !inGround && !onEntity) {
            super.updateRotation();
        }
    }

//    @Override
//    protected void onHit(HitResult result) {
//        switch (result.getType()) {
//            case BLOCK:
//                BlockHitResult blockResult = (BlockHitResult) result;
//                BlockPos resultPos = blockResult.getBlockPos();
//                BlockState state = this.level().getBlockState(resultPos);
//                SoundEvent event = state.getBlock().getSoundType(state, this.level(), resultPos, this).getBreakSound();
//                double speed = this.getDeltaMovement().length();
//                if (speed > 0.1) {
//                    this.level().playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
//                }
//                this.bounce(blockResult.getDirection());
//
//                break;
//            case ENTITY:
//                EntityHitResult entityResult = (EntityHitResult) result;
//                Entity entity = entityResult.getEntity();
//                if (entity == this.getOwner() || entity == this.getVehicle()) return;
//                entityData.set(TARGET_UUID, entity.getStringUUID());
//                onEntity = true;
//                this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
//                setXRot(-90);
//                xRotO = getXRot();
//                break;
//            default:
//                break;
//        }
//    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected void onHit(HitResult pResult) {
        switch (pResult.getType()) {
            case BLOCK:
                this.onHitBlock((BlockHitResult) pResult);
                break;
            case ENTITY:
                this.onHitEntity((EntityHitResult) pResult);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
    }

    //    private void bounce(Direction direction) {
//        Vec3 vec3 = Vec3.atLowerCornerOf(direction.getNormal());
//        this.setYRot((float) (direction.get2DDataValue() * 90));
//        this.look(vec3);
//        this.inGround = true;
//        this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
//    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.lastState = this.level().getBlockState(pResult.getBlockPos());
        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);

        this.look(Vec3.atLowerCornerOf(pResult.getDirection().getNormal()));
        this.setYRot((float) (pResult.getDirection().get2DDataValue() * 90));

        BlockPos resultPos = pResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);
        SoundEvent event = state.getBlock().getSoundType(state, this.level(), resultPos, this).getBreakSound();
        double speed = this.getDeltaMovement().length();
        if (speed > 0.1) {
            this.level().playSound(null, pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
        }
        this.inGround = true;
    }

    public void explode() {
        if (!this.level().isClientSide()) {
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            ModUtils.queueServerWork(1, () -> this.triggerExplode(this));
        }
        this.discard();
    }

    private void triggerExplode(Entity target) {
        CustomExplosion explosion = new CustomExplosion(level(), this,
                ModDamageTypes.causeProjectileBoomDamage(level().registryAccess(), this, this.getOwner()), ExplosionConfig.C4_EXPLOSION_DAMAGE.get(),
                target.getX(), target.getY(), target.getZ(), ExplosionConfig.C4_EXPLOSION_RADIUS.get(), ExplosionConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion);
        ParticleTool.spawnHugeExplosionParticles(level(), position());
        explosion.finalizeExplosion(false);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return super.getDimensions(pPose).scale((float) 0.5);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected float getWaterInertia() {
        return 0.6F;
    }
}