package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.entity.projectile.CannonShellEntity;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.item.common.ammo.CannonShellItem;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Mk42Entity extends PathfinderMob implements GeoEntity, ICannonEntity {
    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationprocedure = "empty";

    protected int interpolationSteps;

    protected double serverYRot;
    protected double serverXRot;

    public Mk42Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MK_42.get(), world);
    }

    public Mk42Entity(EntityType<Mk42Entity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(true);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(TYPE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("cool_down", this.entityData.get(COOL_DOWN));
        compound.putInt("type", this.entityData.get(TYPE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COOL_DOWN, compound.getInt("cool_down"));
        this.entityData.set(TYPE, compound.getInt("type"));
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
        return super.getPassengersRidingOffset() - 0.25;
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
        if (amount < 32) {
            return false;
        }
        return super.hurt(source, 0.3f * amount);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.CROWBAR.get() && this.getFirstPassenger() == null) {
            this.discard();
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.MK_42_SPAWN_EGG.get()));
        } else {
            player.setXRot(this.getXRot());
            player.setYRot(this.getYRot());
            player.startRiding(this);

        }
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide());
        super.mobInteract(player, hand);
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
    public void baseTick() {
        super.baseTick();
        if (this.entityData.get(COOL_DOWN) > 0) {
            this.entityData.set(COOL_DOWN, this.entityData.get(COOL_DOWN) - 1);
        }
        if (this.entityData.get(COOL_DOWN) > 28) {
            if (Math.random() < 0.5) {
                this.entityData.set(TYPE, -1);
            } else {
                this.entityData.set(TYPE, 1);
            }
        }
        this.refreshDimensions();
    }

    @Override
    public void cannonShoot(Player player) {
        if (this.entityData.get(COOL_DOWN) > 0) {
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

            if (stack.is(ModItems.HE_5_INCHES.get())) {
                hitDamage = 600;
                explosionRadius = 10;
                explosionDamage = 200;
                fireProbability = 0.18F;
                fireTime = 5;
            }

            if (stack.is(ModItems.AP_5_INCHES.get())) {
                hitDamage = 850;
                explosionRadius = 3;
                explosionDamage = 250;
                fireProbability = 0;
                fireTime = 0;
                durability = 25;
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            CannonShellEntity entityToSpawn = new CannonShellEntity(ModEntities.CANNON_SHELL.get(),
                    player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime).durability(durability);

            entityToSpawn.setPos(this.getX(), this.getEyeY(), this.getZ());
            entityToSpawn.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 15, 0.05f);
            level.addFreshEntity(entityToSpawn);

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_FIRE_1P.get(), 2, 1);
                SoundTool.playLocalSound(serverPlayer, ModSounds.MK_42_RELOAD.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

            this.entityData.set(COOL_DOWN, 30);

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + 5 * this.getLookAngle().x,
                    this.getY(),
                    this.getZ() + 5 * this.getLookAngle().z,
                    100, 7, 0.02, 7, 0.005);

            double x = this.getX() + 9 * this.getLookAngle().x;
            double y = this.getEyeY() + 9 * this.getLookAngle().y;
            double z = this.getZ() + 9 * this.getLookAngle().z;

            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);

            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);

            int count = 6;

            for (float i = 9.5f; i < 16; i += .5f) {
                server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        this.getX() + i * this.getLookAngle().x,
                        this.getEyeY() + i * this.getLookAngle().y,
                        this.getZ() + i * this.getLookAngle().z,
                        Mth.clamp(count--,1,5), 0.15, 0.15, 0.15, 0.0025);
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        serverYRot = yaw;
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    @Override
    public void travel(@NotNull Vec3 dir) {
        Player entity = this.getPassengers().isEmpty() ? null : (Player) this.getPassengers().get(0);
        ItemStack stack = null;
        if (entity != null) {
            stack = entity.getMainHandItem();
        }
        if (stack != null && this.isVehicle() && !stack.is(ModTags.Items.GUN)) {
            float diffY = entity.getYHeadRot() - this.getYRot();
            float diffX = entity.getXRot() - 1.3f - this.getXRot();
            if (diffY > 180.0f) {
                diffY -= 360.0f;
            } else if (diffY < -180.0f) {
                diffY += 360.0f;
            }
            diffY = diffY * 0.15f;
            diffX = diffX * 0.15f;

            this.setYRot(this.getYRot() + Mth.clamp(diffY, -1.75f, 1.75f));
            this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -3f, 3f), -85, 15));
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            return;
        }
        super.travel(dir);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    public static void init() {
    }

    protected void clampRotation(Entity entity) {
        ItemStack stack = null;
        if (entity instanceof Player player) {
            stack = player.getMainHandItem();
        }

        if (!stack.is(ModTags.Items.GUN)) {
            float f = Mth.wrapDegrees(entity.getXRot());
            float f1 = Mth.clamp(f, -85.0F, 15.0F);
            entity.xRotO += f1 - f;
            entity.setXRot(entity.getXRot() + f1 - f);
        }
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
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

    private PlayState movementPredicate(AnimationState<Mk42Entity> event) {
        if (this.entityData.get(COOL_DOWN) > 0) {
            if (this.entityData.get(TYPE) == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire2"));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mk42.idle"));
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
        return null;
    }

    public void setAnimation(String animation) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
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
            if (event.getSource().getDirectEntity() == gunner) {
                event.setCanceled(true);
            }
        }
    }
}
