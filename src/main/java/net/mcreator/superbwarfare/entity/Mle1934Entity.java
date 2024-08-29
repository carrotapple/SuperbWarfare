package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.item.common.ammo.CannonShellItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Mle1934Entity extends PathfinderMob implements GeoEntity, ICannonEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(Mle1934Entity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationprocedure = "empty";

    public Mle1934Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MLE_1934.get(), world);
    }

    public Mle1934Entity(EntityType<Mle1934Entity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(true);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "undefined");
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 2.16F;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public MobType getMobType() {
        return super.getMobType();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.075;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ModSounds.HIT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.HIT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (amount < 34) {
            return false;
        }
        return super.hurt(source, amount - 34);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide());
        super.mobInteract(player, hand);
        player.setXRot(this.getXRot());
        player.setYRot(this.getYRot());
        player.startRiding(this);
        return result;
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
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 30f,
                this.getX(), this.getY(), this.getZ(), 7.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
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
    public void baseTick() {
        super.baseTick();

        if (this.getFirstPassenger() == null) return;

        Entity gunner = this.getFirstPassenger();

        if (this.getPersistentData().getInt("FireCooldown") > 0) {
            this.getPersistentData().putInt("FireCooldown", this.getPersistentData().getInt("FireCooldown") - 1);
        }

        if (this.getPersistentData().getInt("FireCooldown") > 72) {
            gunner.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.cannonRecoil = 10;
                capability.syncPlayerVariables(gunner);
            });
        }

        this.refreshDimensions();
    }

    @Override
    public void cannonShoot(Player player) {
        if (this.getPersistentData().getInt("FireCooldown") > 0) {
            return;
        }

        Level level = player.level();
        if (level instanceof ServerLevel server) {
            ItemStack stack = player.getMainHandItem();

            if (!(stack.getItem() instanceof CannonShellItem)) {
                return;
            }

            float hitDamage = 0;
            float explosionRadius = 0;
            float explosionDamage = 0;
            float fireProbability = 0;
            int fireTime = 0;
            int durability = 0;
            boolean salvoShoot = false;

            if (stack.is(ModItems.HE_5_INCHES.get())) {
                hitDamage = 130;
                explosionRadius = 13;
                explosionDamage = 250;
                fireProbability = 0.24F;
                fireTime = 5;
                salvoShoot = stack.getCount() > 1 || player.isCreative();
            }

            if (stack.is(ModItems.AP_5_INCHES.get())) {
                hitDamage = 550;
                explosionRadius = 3.8f;
                explosionDamage = 300;
                fireProbability = 0;
                fireTime = 0;
                durability = 35;
                salvoShoot = stack.getCount() > 1 || player.isCreative();
            }

            if (!player.isCreative()) {
                stack.shrink(salvoShoot ? 2 : 1);
            }

            float yRot = this.getYRot();
            if (yRot < 0) {
                yRot += 360;
            }
            yRot = yRot + 90 % 360;

            var leftPos = new Vector3d(7.2, 0, -0.45);
            leftPos.rotateZ(-this.getXRot() * Mth.DEG_TO_RAD);
            leftPos.rotateY(-yRot * Mth.DEG_TO_RAD);

            //左炮管
            CannonShellEntity entityToSpawnLeft = new CannonShellEntity(ModEntities.CANNON_SHELL.get(),
                    player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime).durability(durability);

            entityToSpawnLeft.setPos(this.getX() + leftPos.x,
                    this.getEyeY() - 0.2 + leftPos.y,
                    this.getZ() + leftPos.z);
            entityToSpawnLeft.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 18, 0.05f);
            level.addFreshEntity(entityToSpawnLeft);

            //右炮管
            if (salvoShoot) {
                var rightPos = new Vector3d(7.2, 0, 0.45);
                rightPos.rotateZ(-this.getXRot() * Mth.DEG_TO_RAD);
                rightPos.rotateY(-yRot * Mth.DEG_TO_RAD);

                CannonShellEntity entityToSpawnRight = new CannonShellEntity(ModEntities.CANNON_SHELL.get(),
                        player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime).durability(durability);

                entityToSpawnRight.setPos(this.getX() + rightPos.x,
                        this.getEyeY() - 0.2 + rightPos.y,
                        this.getZ() + rightPos.z);
                entityToSpawnRight.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 18, 0.05f);
                level.addFreshEntity(entityToSpawnRight);

                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.recoilHorizon = 1);
            } else {
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.recoilHorizon = -1);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_FIRE_1P.get(), 2, 1);
                ModUtils.queueServerWork(44, () -> SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_RELOAD.get(), 2, 1));
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.getPersistentData().putInt("FireCooldown", 74);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 5 * this.getLookAngle().x,
                    this.getY(),
                    this.getZ() + 5 * this.getLookAngle().z,
                    200, 5, 0.02, 5, 0.005);

            double x = this.getX() + 9 * this.getLookAngle().x;
            double y = this.getEyeY() + 9 * this.getLookAngle().y;
            double z = this.getZ() + 9 * this.getLookAngle().z;

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);

            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 9.5 * this.getLookAngle().x,
                    this.getEyeY() + 9.5 * this.getLookAngle().y,
                    this.getZ() + 9.5 * this.getLookAngle().z,
                    5, 0.15, 0.15, 0.15, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 10 * this.getLookAngle().x,
                    this.getEyeY() + 10 * this.getLookAngle().y,
                    this.getZ() + 10 * this.getLookAngle().z,
                    4, 0.15, 0.15, 0.15, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 11.5 * this.getLookAngle().x,
                    this.getEyeY() + 11.5 * this.getLookAngle().y,
                    this.getZ() + 11.5 * this.getLookAngle().z,
                    3, 0.15, 0.15, 0.15, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 12 * this.getLookAngle().x,
                    this.getEyeY() + 12 * this.getLookAngle().y,
                    this.getZ() + 12 * this.getLookAngle().z,
                    2, 0.15, 0.15, 0.15, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 12.5 * this.getLookAngle().x,
                    this.getEyeY() + 12.5 * this.getLookAngle().y,
                    this.getZ() + 12.5 * this.getLookAngle().z,
                    2, 0.15, 0.15, 0.15, 0.0075);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 13 * this.getLookAngle().x,
                    this.getEyeY() + 13 * this.getLookAngle().y,
                    this.getZ() + 13 * this.getLookAngle().z,
                    1, 0.15, 0.15, 0.15, 0.0075);
        }
    }

    @Override
    public void travel(Vec3 dir) {
        Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (this.isVehicle() && entity != null) {
            this.setYRot(entity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(Mth.clamp(entity.getXRot() - 1f, -30, 4));
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = entity.getYRot();
            this.yHeadRot = entity.getYRot();
            this.setMaxUpStep(1.0F);
            if (entity instanceof LivingEntity passenger) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float forward = passenger.zza;
                float strafe = passenger.xxa;
                super.travel(new Vec3(strafe, 0, forward));
            }
            double d1 = this.getX() - this.xo;
            double d0 = this.getZ() - this.zo;
            float f1 = (float) Math.sqrt(d1 * d1 + d0 * d0) * 4;
            if (f1 > 1.0F)
                f1 = 1.0F;
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4F);
            this.walkAnimation.position(this.walkAnimation.position() + this.walkAnimation.speed());
            this.calculateEntityAnimation(true);
            return;
        }
        this.setMaxUpStep(0.5F);
        super.travel(dir);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 1);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    public static void init() {
    }
    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -30.0F, 4.0F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }
    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 600)
                .add(Attributes.ARMOR, 30)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    private PlayState movementPredicate(AnimationState<Mle1934Entity> event) {
        if (this.animationprocedure.equals("empty")) {

            if (this.getFirstPassenger() != null) {
                Entity gunner = this.getFirstPassenger();
                var capability = gunner.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);
                if (capability.orElse(new ModVariables.PlayerVariables()).cannonRecoil > 0) {
                    if (capability.orElse(new ModVariables.PlayerVariables()).recoilHorizon == 1) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mle1934.salvo_fire"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mle1934.fire"));
                    }
                }
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mle1934.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Mle1934Entity> event) {
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
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 1) {
            this.remove(RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
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

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        var damagesource = event.getSource();
        var entity = event.getEntity();
        if (damagesource == null || entity == null) return;

        var sourceentity = damagesource.getEntity();
        if (sourceentity == null) return;

        if (entity instanceof Mle1934Entity mle1934) {
            if (mle1934.getFirstPassenger() == null) return;
            Entity gunner = mle1934.getFirstPassenger();
            if (event.getSource().getDirectEntity() == gunner) {
                event.setCanceled(true);
            }
        }
    }
}
