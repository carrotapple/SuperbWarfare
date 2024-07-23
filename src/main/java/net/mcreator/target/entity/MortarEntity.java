package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

import javax.annotation.Nullable;

public class MortarEntity extends PathfinderMob implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> FIRE_TIME = SynchedEntityData.defineId(MortarEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationProcedure = "empty";

    public MortarEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(TargetModEntities.MORTAR.get(), world);
    }

    public MortarEntity(EntityType<MortarEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(true);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(FIRE_TIME, 0);

    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.2F;
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
        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.setYRot(this.getYRot());
        this.setXRot(-70);
        this.setYBodyRot(this.getYRot());
        this.setYHeadRot(this.getYRot());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.yBodyRotO = this.getYRot();
        this.yHeadRotO = this.getYRot();
        return retval;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("fire_time", this.entityData.get(FIRE_TIME));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("fire_time"))
            this.entityData.set(FIRE_TIME, compound.getInt("fire_time"));
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);

        ItemStack mainHandItem = player.getMainHandItem();
        if (player.isShiftKeyDown()) {
            this.setYRot(player.getYRot());
            this.setXRot(this.getXRot());
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            this.yBodyRotO = this.getYRot();
            this.yHeadRotO = this.getYRot();
        }
        if (mainHandItem.getItem() == TargetModItems.MORTAR_SHELLS.get() && !player.getCooldowns().isOnCooldown(TargetModItems.MORTAR_SHELLS.get()) && !player.isShiftKeyDown()) {

//            this.getPersistentData().putInt("fire_time",25);

            this.entityData.set(FIRE_TIME,25);

            player.getCooldowns().addCooldown(TargetModItems.MORTAR_SHELLS.get(), 30);
            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> TargetModItems.MORTAR_SHELLS.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
            if (!this.level().isClientSide()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TargetModSounds.MORTAR_LOAD.get(), SoundSource.PLAYERS, 1f, 1f);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TargetModSounds.MORTAR_FIRE.get(), SoundSource.PLAYERS, 8f, 1f);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TargetModSounds.MORTAR_DISTANT.get(), SoundSource.PLAYERS, 32f, 1f);
            }
            TargetMod.queueServerWork(20, () -> {
                Level level = this.level();
                if (level instanceof ServerLevel server) {
                    MortarShellEntity entityToSpawn = new MortarShellEntity(TargetModEntities.MORTAR_SHELL.get(), player, level);
                    entityToSpawn.setPos(this.getX(), this.getEyeY(), this.getZ());
                    entityToSpawn.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 8, (float) 0.5);
                    level.addFreshEntity(entityToSpawn);
                    server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (this.getX() + 3 * this.getLookAngle().x), (this.getY() + 0.1 + 3 * this.getLookAngle().y), (this.getZ() + 3 * this.getLookAngle().z), 8, 0.4, 0.4, 0.4,
                            0.007);
                    server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 50, 2, 0.02, 2, 0.0005);
                }
            });
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        double[] Timer = {0};
        double totalTime = 4;
        int sleepTime = 2;
        double Duration = totalTime / sleepTime;
        Runnable Runnable = () -> {
            while (Timer[0] < Duration) {

                this.setXRot((float) -this.getAttribute(TargetModAttributes.MORTAR_PITCH.get()).getBaseValue());

                Timer[0]++;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread Thread = new Thread(Runnable);
        Thread.start();

//        if (this.getPersistentData().getInt("fire_time") > 0) {
//            this.getPersistentData().putInt("fire_time",this.getPersistentData().getInt("fire_time") - 1);
//        }

        this.entityData.set(FIRE_TIME,this.entityData.get(FIRE_TIME) - 1);

        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 1);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
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
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationProcedure.equals("empty")) {
            if (this.entityData.get(FIRE_TIME) > 0) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mortar.fire"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mortar.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState event) {
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
        if (this.deathTime == 5) {
            this.remove(MortarEntity.RemovalReason.KILLED);
            this.dropExperience();
            if (this.level() instanceof ServerLevel level) {
                var x = this.getX();
                var y = this.getY();
                var z = this.getZ();
                level.explode(null, x, y, z, 0, Level.ExplosionInteraction.NONE);
                ItemEntity mortar = new ItemEntity(level, x, (y + 1), z, new ItemStack(TargetModItems.MORTAR_DEPLOYER.get()));
                mortar.setPickUpDelay(10);
                level.addFreshEntity(mortar);
            }
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
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 0, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
