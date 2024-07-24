package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.item.Monitor;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
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

import java.util.Objects;
import java.util.UUID;

public class DroneEntity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> LINKED = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> CONTROLLER = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float moveX = 0;
    private float moveY = 0;
    private float moveZ = 0;
    private boolean move = false;

    public String animationprocedure = "empty";

    public DroneEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(TargetModEntities.DRONE.get(), world);
    }

    public DroneEntity(EntityType<DroneEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }

    public DroneEntity(EntityType<? extends DroneEntity> type, Level world, float moveX, float moveY, float moveZ) {
        super(type, world);
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(CONTROLLER, "undefined");
        this.entityData.define(LINKED, false);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.075F;
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("Linked", this.entityData.get(LINKED));
        compound.putString("Controller", this.entityData.get(CONTROLLER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Linked"))
            this.entityData.set(LINKED, compound.getBoolean("Linked"));
        if (compound.contains("Controller"))
            this.entityData.set(CONTROLLER, compound.getString("Controller"));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.getPersistentData().getBoolean("left")) {
            this.moveX = -1.5f;
        }
        if (this.getPersistentData().getBoolean("right")) {
            this.moveX = 1.5f;
        }

        if (!this.getPersistentData().getBoolean("left") && !this.getPersistentData().getBoolean("right")) {
            if (this.moveX >= 0) {
                this.moveX = Mth.clamp(this.moveX - 0.3f, 0, 1);
            } else {
                this.moveX = Mth.clamp(this.moveX + 0.3f, -1, 0);
            }
        }

        if (this.getPersistentData().getBoolean("forward")) {
            this.moveZ = -1.5f;
        }
        if (this.getPersistentData().getBoolean("backward")) {
            this.moveZ = 1.5f;
        }

        if (!this.getPersistentData().getBoolean("forward") && !this.getPersistentData().getBoolean("backward")) {
            if (this.moveZ >= 0) {
                this.moveZ = Mth.clamp(this.moveZ - 0.3f, 0, 1);
            } else {
                this.moveZ = Mth.clamp(this.moveZ + 0.3f, -1, 0);
            }
        }

        if (this.getPersistentData().getBoolean("up")) {
            this.moveY = -1.5f;
        }
        if (this.getPersistentData().getBoolean("down")) {
            this.moveY = 1.5f;
        }

        if (!this.getPersistentData().getBoolean("up") && !this.getPersistentData().getBoolean("down")) {
            if (this.moveY >= 0) {
                this.moveY = Mth.clamp(this.moveY - 0.3f, 0, 1);
            } else {
                this.moveY = Mth.clamp(this.moveY + 0.3f, -1, 0);
            }
        }

        LivingEntity control = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(this.entityData.get(CONTROLLER))).findFirst().orElse(null);

        this.move = this.getPersistentData().getBoolean("left")
                || this.getPersistentData().getBoolean("right")
                || this.getPersistentData().getBoolean("forward")
                || this.getPersistentData().getBoolean("backward")
                || this.getPersistentData().getBoolean("up")
                || this.getPersistentData().getBoolean("down");

        if (this.move || !this.onGround()) {
            this.level().playSound(null, this.getOnPos(), TargetModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
            if (control != null) {
                ItemStack stack = control.getMainHandItem();
                if (stack.getOrCreateTag().getBoolean("Using") && control instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.DRONE_SOUND.get(), 100, 1);
                }
            }
        }

        Vec3 vec = this.getDeltaMovement();
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1) {
            if (this.move) {
                this.setDeltaMovement(vec.multiply(1.04, 1, 1.04));
            }
        }

        if (this.getPersistentData().getBoolean("firing")) {
            if (control instanceof Player player) {
                droneDrop(player);
            }
            this.getPersistentData().putBoolean("firing", false);
        }

        this.refreshDimensions();
    }

    private void droneDrop(Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            DroneGrenadeEntity droneGrenadeEntity = new DroneGrenadeEntity(player, level);
            droneGrenadeEntity.setPos(this.getX(), this.getY(), this.getZ());
            droneGrenadeEntity.shoot(0, -1, 0, 0, 0.5f);
            level.addFreshEntity(droneGrenadeEntity);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);

        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == TargetModItems.MONITOR.get()) {
            if (!player.isCrouching()) {
                if (!this.entityData.get(LINKED)) {
                    if (stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(Component.translatable("des.target.monitor.monitor_already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(LINKED, true);
                    this.entityData.set(CONTROLLER, player.getStringUUID());

                    Monitor.link(stack, this.getStringUUID());
                    player.displayClientMessage(Component.translatable("des.target.monitor.linked").withStyle(ChatFormatting.GREEN), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.hit_player"))), SoundSource.PLAYERS, 0.5F, 1);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("des.target.monitor.already_linked").withStyle(ChatFormatting.RED), true);
                }
            } else {
                if (this.entityData.get(LINKED)) {
                    if (!stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(Component.translatable("des.target.monitor.already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(CONTROLLER, "none");
                    this.entityData.set(LINKED, false);

                    Monitor.disLink(stack);
                    player.displayClientMessage(Component.translatable("des.target.monitor.unlinked").withStyle(ChatFormatting.RED), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.hit_player"))), SoundSource.PLAYERS, 0.5F, 1);
                    }
                }
            }
        } else if (stack.isEmpty() && player.isCrouching()) {
            if (!this.level().isClientSide()) this.discard();
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TargetModItems.DRONE_SPAWN_EGG.get()));
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void travel(Vec3 dir) {
        LivingEntity control = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(this.entityData.get(CONTROLLER))).findFirst().orElse(null);

        if (control != null) {
            ItemStack stack = control.getMainHandItem();
            if (stack.getOrCreateTag().getBoolean("Using")) {
                this.setYRot(control.getYRot() + 180);
                this.yRotO = this.getYRot();
                this.setXRot(Mth.clamp(control.getXRot(), -25, 95));
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = control.getYRot() + 180;
                this.yHeadRot = control.getYRot() + 180;
                this.setMaxUpStep(1.0F);
                this.setSpeed(4 * (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float forward = -moveZ;
                float upDown = -moveY;
                float strafe = -moveX;
                super.travel(new Vec3(2 * strafe, 2 * upDown, 2 * forward));
                Vec3 vec3 = this.getDeltaMovement();
                if (!this.move) {
                    this.setDeltaMovement(vec3.multiply(0.9, 0.8, 0.9));
                } else {
                    this.setDeltaMovement(vec3.multiply(1.05, 0.99, 1.05));
                }
                return;
            }
        }

        this.setMaxUpStep(0.5F);
        super.travel(dir);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        String id = this.entityData.get(CONTROLLER);

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return;
        }

        Player player = this.level().getPlayerByUUID(uuid);
        if (player != null) {
            player.getInventory().items.stream().filter(stack -> stack.getItem() == TargetModItems.MONITOR.get())
                    .forEach(stack -> {
                        if (stack.getOrCreateTag().getString(Monitor.LINKED_DRONE).equals(this.getStringUUID())) {
                            Monitor.disLink(stack);
                        }
                    });
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 1);
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
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.1);
        builder = builder.add(Attributes.MAX_HEALTH, 20);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.FLYING_SPEED, 0.1);
        return builder;
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            if (this.entityData.get(LINKED) || !this.onGround()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.drone.fly"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.drone.idle"));
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
        if (this.deathTime >= 100 || this.onGround()) {
            this.remove(DroneEntity.RemovalReason.KILLED);
            if (level() instanceof ServerLevel) {
                level().explode(null, this.getX(), this.getY(), this.getZ(), 0.1F, Level.ExplosionInteraction.NONE);
            }
        }
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y() - 0.02, this.getDeltaMovement().z()));
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 1, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 1, this::procedurePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
