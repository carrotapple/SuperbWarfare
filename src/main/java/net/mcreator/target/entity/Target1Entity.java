package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

// TODO 重置靶子
@Mod.EventBusSubscriber
public class Target1Entity extends PathfinderMob implements GeoEntity, AnimatedEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(Target1Entity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(Target1Entity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationProcedure = "empty";

    public Target1Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(TargetModEntities.TARGET_1.get(), world);
    }

    public Target1Entity(EntityType<Target1Entity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(true);
        setPersistenceRequired();
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
        this.entityData.define(ANIMATION, "undefined");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
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
    public boolean causeFallDamage(float l, float d, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide()) {
            this.level().playSound(null, BlockPos.containing(this.getX(), this.getY(), this.getZ()), TargetModSounds.HIT.get(), SoundSource.BLOCKS, 8, 1);
        } else {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), TargetModSounds.HIT.get(), SoundSource.BLOCKS, 8, 1, false);
        }
        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);

        this.setYRot(0);
        this.setXRot(0);
        this.setYBodyRot(this.getYRot());
        this.setYHeadRot(this.getYRot());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        return data;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    public static void onEntityAttacked(LivingAttackEvent event, DamageSource source) {
        var entity = event.getEntity();
        if (entity == null) return;
        if (entity instanceof Target1Entity target1) {

            if (source.is(DamageTypes.IN_FIRE)
                || source.getDirectEntity() instanceof ThrownPotion
                || source.getDirectEntity() instanceof AreaEffectCloud
                || source.is(DamageTypes.FALL)
                || source.is(DamageTypes.CACTUS)
                || source.is(DamageTypes.DROWN)
                || source.is(DamageTypes.LIGHTNING_BOLT)
                || source.is(DamageTypes.FALLING_ANVIL)
                || source.is(DamageTypes.DRAGON_BREATH)
                || source.is(DamageTypes.WITHER)
                || source.is(DamageTypes.WITHER_SKULL)
                || source.is(DamageTypes.MAGIC)) {
                event.setCanceled(true);
            }

            if (entity.getPersistentData().getDouble("target_down") > 0) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onTarget1Down(LivingDeathEvent event) {
        var entity = event.getEntity();
        var sourceEntity = event.getSource().getEntity();

        if (entity == null) return;

        if (entity instanceof Target1Entity target1) {

            event.setCanceled(true);
            target1.setHealth(target1.getMaxHealth());

            if (sourceEntity == null) return;

            if (sourceEntity instanceof Player player) {
                player.displayClientMessage(Component.literal(("Target Down " + new java.text.DecimalFormat("##.#").format((entity.position()).distanceTo((sourceEntity.position()))) + "M")), true);
                SoundTool.playLocalSound(player, TargetModSounds.TARGET_DOWN.get(), 100, 1);
                entity.getPersistentData().putDouble("target_down", 100);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide());
        super.mobInteract(player, hand);

        if (player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) {
                this.discard();
            }

            player.addItem(new ItemStack(TargetModItems.TARGET_DEPLOYER.get()));
        } else {
            if (!(player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                this.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((player.getX()), this.getY(), (player.getZ())));

                this.setYRot(this.getYRot());
                this.setXRot(0);
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.getYRot());
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();

                this.getPersistentData().putDouble("target_down", 0);
            }
        }

        return result;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getPersistentData().getDouble("target_down") > 0) {
            this.getPersistentData().putDouble("target_down", this.getPersistentData().getDouble("target_down") - 1);
        }
        if (this.getPersistentData().getDouble("target_down") >= 98) {
            this.setYRot(this.getYRot());
            this.setXRot((float) (100 - this.getPersistentData().getDouble("target_down")) * -45f);
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            this.yBodyRotO = this.getYRot();
            this.yHeadRotO = this.getYRot();
        }
        if (this.getPersistentData().getDouble("target_down") <= 5 && this.getPersistentData().getDouble("target_down") > 0) {
            this.setYRot(this.getYRot());
            this.setXRot((float) (-90 + (5 - this.getPersistentData().getDouble("target_down")) * 18f * 1.25f));
            this.setYBodyRot(this.getYRot());
            this.setYHeadRot(this.getYRot());
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            this.yBodyRotO = this.getYRot();
            this.yHeadRotO = this.getYRot();
        }
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
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
        this.setNoGravity(true);
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.FLYING_SPEED, 0);
    }

    private PlayState movementPredicate(AnimationState<Target1Entity> event) {
        if (this.animationProcedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.target.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<Target1Entity> event) {
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
        if (this.deathTime >= 100) {
            this.spawnAtLocation(new ItemStack(TargetModItems.TARGET_DEPLOYER.get()));
            this.remove(Target1Entity.RemovalReason.KILLED);
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

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        float num;
        if (this.getPersistentData().getDouble("target_down") > 0) {
            num = 0.1f;
        } else {
            num = 1f;
        }

        return super.getDimensions(p_33597_).scale(num);
    }
}
