package net.mcreator.target.entity;

import net.mcreator.target.init.*;
import net.mcreator.target.item.common.ammo.CannonShellItem;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.CustomExplosion;
import net.mcreator.target.tools.ParticleTool;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Mk42Entity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float RotX = 0;
    private float RotY = 0;

    public String animationprocedure = "empty";

    public Mk42Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(TargetModEntities.MK_42.get(), world);
    }

    public Mk42Entity(EntityType<Mk42Entity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(TEXTURE, "sherman");
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 2.16F;
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(TEXTURE);
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
        return super.getPassengersRidingOffset() - 0.25;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:hit"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("target:hit"));
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
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        if (source.getDirectEntity() instanceof Player player && this.getFirstPassenger() != null && player == this.getFirstPassenger()) {
            return false;
        }
        if (source.getDirectEntity() instanceof Player player && this.getFirstPassenger() != null && player == this.getFirstPassenger()) {
            return false;
        }
        return super.hurt(source, amount);
    }


    @Override
    public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
        InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
        super.mobInteract(sourceentity, hand);
        sourceentity.setXRot(this.getXRot());
        sourceentity.setYRot(this.getYRot());
        sourceentity.startRiding(this);
        return retval;
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
                TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 30f,
                this.getX(), this.getY(), this.getZ(), 7.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Texture", this.getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Texture"))
            this.setTexture(compound.getString("Texture"));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.getFirstPassenger() == null) return;

        Entity gunner = this.getFirstPassenger();

//        float adjust_rateX = (float) Mth.clamp(Math.pow(gunner.getXRot() - this.getXRot(), 2),0,5f);
//        float adjust_rateY = (float) Mth.clamp(Math.pow(gunner.getYRot() - this.getYRot(), 2),0,3f);
//
//        if (RotY < gunner.getYRot()) {
//            RotY = (float) Mth.clamp(this.yHeadRot + adjust_rateY,Double.NEGATIVE_INFINITY, gunner.getYRot());
//        } else {
//            RotY = (float) Mth.clamp(this.yHeadRot- adjust_rateY,gunner.getYRot(),Double.POSITIVE_INFINITY);
//        }
//
//        if (RotX < gunner.getXRot()) {
//            RotX = Mth.clamp((Mth.clamp(this.getXRot() + adjust_rateX,-85 ,15)),-85, gunner.getXRot());
//        } else {
//            RotX = Mth.clamp((Mth.clamp(this.getXRot() - adjust_rateX,-85 ,15)),gunner.getXRot(),15);
//        }

        if (this.getPersistentData().getInt("fire_cooldown") > 0) {
            this.getPersistentData().putInt("fire_cooldown", this.getPersistentData().getInt("fire_cooldown") - 1);
        }

        if (this.getPersistentData().getInt("fire_cooldown") > 28) {
            gunner.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

                if (Math.random() < 0.5) {
                    capability.recoilHorizon = -1;
                } else {
                    capability.recoilHorizon = 1;
                }

                capability.cannonRecoil = 10;
                capability.syncPlayerVariables(gunner);
            });
        }

        if (this.getPersistentData().getBoolean("firing") && gunner instanceof Player player && this.getPersistentData().getInt("fire_cooldown") == 0) {
            cannonShoot(player);
        }

        this.refreshDimensions();
    }

    public void cannonShoot(Player player) {
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

            if (stack.is(TargetModItems.HE_5_INCHES.get())) {
                hitDamage = 100;
                explosionRadius = 10;
                explosionDamage = 200;
                fireProbability = 0.18F;
                fireTime = 5;
            }

            if (stack.is(TargetModItems.AP_5_INCHES.get())) {
                hitDamage = 450;
                explosionRadius = 3;
                explosionDamage = 250;
                fireProbability = 0;
                fireTime = 0;
                durability = 25;
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            CannonShellEntity entityToSpawn = new CannonShellEntity(TargetModEntities.CANNON_SHELL.get(),
                    player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime).durability(durability);

            entityToSpawn.setPos(this.getX(), this.getEyeY(), this.getZ());
            entityToSpawn.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 15, 0.1f);
            level.addFreshEntity(entityToSpawn);


            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, TargetModSounds.MK_42_FIRE_1P.get(), 2, 1);
                SoundTool.playLocalSound(serverPlayer, TargetModSounds.MK_42_RELOAD.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.getPersistentData().putInt("fire_cooldown", 30);

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

//    @Override
//    public void travel(Vec3 dir) {
//        if (this.getFirstPassenger() == null) return;
//        Entity gunner = this.getFirstPassenger();
//        if (this.isVehicle()) {
//            this.setYRot(gunner.getYRot());
//            this.yRotO = this.getYRot();
//            this.setXRot(Mth.clamp(gunner.getXRot() - 1.35f, -85, 15));
//            this.setRot(this.getYRot(), this.getXRot());
//            this.yBodyRot = gunner.getYRot();
//            this.yHeadRot = gunner.getYRot();
//        }
//    }

    @Override
    public void travel(Vec3 dir) {
        Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
        if (this.isVehicle()) {
            this.setYRot(entity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(Mth.clamp(entity.getXRot() - 1.35f, -85, 15));
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 500)
                .add(Attributes.ARMOR, 30)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {

            if (this.getFirstPassenger() != null) {
                Entity gunner = this.getFirstPassenger();
                var capability = gunner.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null);
                if (capability.orElse(new TargetModVariables.PlayerVariables()).cannonRecoil > 0) {
                    if (capability.orElse(new TargetModVariables.PlayerVariables()).recoilHorizon == 1) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire2"));
                    }
                }
            }

            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mk42.idle"));
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
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 1) {
            this.remove(Mk42Entity.RemovalReason.KILLED);
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

        if (entity instanceof Mk42Entity mk42) {
            if (mk42.getFirstPassenger() == null) return;
            Entity gunner = mk42.getFirstPassenger();
            if (event.getSource().getDirectEntity() == gunner){
                event.setCanceled(true);
            }
        }
    }
}
