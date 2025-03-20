package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class C4Entity extends ThrowableItemProjectile implements GeoEntity {
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<String> TARGET_UUID = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(C4Entity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected boolean inGround;
    protected boolean onEntity;

    public C4Entity(EntityType<C4Entity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public C4Entity(LivingEntity owner, Level level) {
        super(ModEntities.C_4.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
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

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();

        if (this.tickCount >= ExplosionConfig.C4_EXPLOSION_COUNTDOWN.get()) {
            this.explode();
        }

        if (inGround && checkNoClip()) {
            inGround = false;
        }

        if (!inGround && !onEntity) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05, 0.0));
        }

        Entity target = EntityFindUtil.findEntity(level(), entityData.get(TARGET_UUID));

        if (onEntity) {
            if (target != null) {
                setPosRaw(target.getX(), target.getY() + target.getBbHeight(), target.getZ());
            } else {
                onEntity = false;
            }
        }

        this.refreshDimensions();
    }

    public boolean checkNoClip() {
        return level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(getViewVector(1).scale(-0.25)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK;
    }

    public void look(Vec3 pTarget) {
        double d0 = pTarget.x;
        double d1 = pTarget.y;
        double d2 = pTarget.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
        setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
        setYHeadRot(getYRot());
        xRotO = getXRot();
        yRotO = getYRot();
    }

    @Override
    protected void updateRotation() {
        if (getDeltaMovement().length() > 0.05 && !inGround && !onEntity) {
            super.updateRotation();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        switch (result.getType()) {
            case BLOCK:
                BlockHitResult blockResult = (BlockHitResult) result;
                BlockPos resultPos = blockResult.getBlockPos();
                BlockState state = this.level().getBlockState(resultPos);
                SoundEvent event = state.getBlock().getSoundType(state, this.level(), resultPos, this).getBreakSound();
                double speed = this.getDeltaMovement().length();
                if (speed > 0.1) {
                    this.level().playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
                }
                this.bounce(blockResult.getDirection());

                break;
            case ENTITY:
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity entity = entityResult.getEntity();
                if (entity == this.getOwner() || entity == this.getVehicle()) return;
                entityData.set(TARGET_UUID, entity.getStringUUID());
                onEntity = true;
                this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
                setXRot(-90);
                xRotO = getXRot();
                break;
            default:
                break;
        }
    }

    private void bounce(Direction direction) {
        Vec3 vec3 = Vec3.atLowerCornerOf(direction.getNormal());
        look(vec3);
        inGround = true;
        this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
    }

    public void explode() {
        if (!this.level().isClientSide()) {
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            this.discard();
        }

        ModUtils.queueServerWork(1, () -> {
            if (this.level().isClientSide()) return;

            this.triggerExplode(this);
        });
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
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.5);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected float getGravity() {
        return 0;
    }
}