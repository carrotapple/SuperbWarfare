package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.headshot.BoundingBoxManager;
import net.mcreator.superbwarfare.headshot.IHeadshotBox;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
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

import java.util.Optional;

public class RpgRocketEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity{

    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(CannonShellEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationprocedure = "empty";

    private int monsterMultiplier = 0;
    private float damage = 150f;

    public RpgRocketEntity(EntityType<? extends RpgRocketEntity> type, Level world) {
        super(type, world);
    }

    public RpgRocketEntity(EntityType<? extends RpgRocketEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public RpgRocketEntity(LivingEntity entity, Level level, float damage, int monsterMultiplier) {
        super(ModEntities.RPG_ROCKET.get(), entity, level);
        this.damage = damage;
        this.monsterMultiplier = monsterMultiplier;
    }

    public RpgRocketEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.RPG_ROCKET.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ROCKET.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        float damageMultiplier = 1 + 0.2f * this.monsterMultiplier;
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        AABB boundingBox = entity.getBoundingBox();
        Vec3 startVec = this.position();
        Vec3 endVec = startVec.add(this.getDeltaMovement());
        Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);
        /* Check for headshot */
        boolean headshot = false;
        if (entity instanceof LivingEntity) {
            IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
            if (headshotBox != null) {
                AABB box = headshotBox.getHeadshotBox((LivingEntity) entity);
                if (box != null) {
                    box = box.move(boundingBox.getCenter().x, boundingBox.minY, boundingBox.getCenter().z);
                    Optional<Vec3> headshotHitPos = box.clip(startVec, endVec);
                    if (headshotHitPos.isEmpty()) {
                        box = box.inflate(0.2, 0.2, 0.2);
                        headshotHitPos = box.clip(startVec, endVec);
                    }
                    if (headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55)) {
                        headshot = true;
                    }
                    if (headshot) {
                        if (this.getOwner() instanceof LivingEntity living) {
                            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                                living.playSound(ModSounds.HEADSHOT.get(), 1, 1);

                                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                            }
                        }
                    }
                }
            }
        }

        if (headshot) {
            if (entity instanceof Monster monster) {
                monster.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * 5f * damageMultiplier);
            } else {
                entity.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * 5f);
            }
        } else {
            if (entity instanceof Monster monster) {
                monster.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * damageMultiplier);
            } else {
                entity.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
            }
        }

        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
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
        if (this.tickCount > 1) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount == 3) {
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, this.xo, this.yo, this.zo, 15, 0.8, 0.8, 0.8, 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo, 10, 0.8, 0.8, 0.8, 0.01, true);
            }
        }
        if (this.tickCount > 2) {
            this.setDeltaMovement(new Vec3((1.03 * this.getDeltaMovement().x()), (1.03 * this.getDeltaMovement().y() - 0.02), (1.03 * this.getDeltaMovement().z())));

            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.xo, this.yo, this.zo, 1, 0, 0, 0, 0, true);
            }
        }

        if (this.tickCount > 100 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
            this.discard();
        }
    }

    private void causeExplode() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), (float) 2 / 3 * this.damage,
                this.getX(), this.getY(), this.getZ(), 10f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(this.monsterMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.rpg.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
        if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (animationprocedure.equals("empty")) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationprocedure = procedure;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 0, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
