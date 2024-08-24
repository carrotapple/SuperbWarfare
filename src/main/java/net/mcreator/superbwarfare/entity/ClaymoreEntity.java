package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class ClaymoreEntity extends LivingEntity implements GeoEntity, AnimatedEntity, OwnableEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(ClaymoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationProcedure = "empty";

    public ClaymoreEntity(EntityType<ClaymoreEntity> type, Level world) {
        super(type, world);
    }

    public ClaymoreEntity(LivingEntity owner, Level level) {
        super(ModEntities.CLAYMORE.get(), level);
        this.setOwnerUUID(owner.getUUID());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public MobType getMobType() {
        return super.getMobType();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE))
            return false;
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
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (level() instanceof ServerLevel) {
            destroyExplode();
            this.discard();
        }
    }

    private void destroyExplode() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), 15f,
                this.getX(), this.getY(), this.getZ(), 7.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    public void setOwnerUUID(@Nullable UUID pUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(pUuid));
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public boolean isOwnedBy(LivingEntity pEntity) {
        return pEntity == this.getOwner();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");

            assert this.getServer() != null;
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.isOwnedBy(player) && player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) {
                this.discard();
            }

            if (!player.getAbilities().instabuild) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.CLAYMORE_MINE.get()));
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
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

        this.removeAllEffects();
        this.clearFire();

        if (this.tickCount >= 40) {
            final Vec3 center = new Vec3(x + 1.5 * this.getLookAngle().x, y + 1.5 * this.getLookAngle().y, z + 1.5 * this.getLookAngle().z);
            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2.5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
                var condition = this.getOwner() != target
                        && target instanceof LivingEntity
                        && !(target instanceof ClaymoreEntity)
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
                ModDamageTypes.causeMineDamage(this.level().registryAccess(), this.getOwner()), 40f,
                target.getX(), target.getY(), target.getZ(), 4f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 0.5);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    private PlayState movementPredicate(software.bernie.geckolib.core.animation.AnimationState<ClaymoreEntity> event) {
        if (this.animationProcedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.claymore.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<ClaymoreEntity> event) {
        if (!animationProcedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationProcedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationProcedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (animationProcedure.equals("empty")) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 1) {
            this.remove(ClaymoreEntity.RemovalReason.KILLED);
        }
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
        data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10);
    }
}