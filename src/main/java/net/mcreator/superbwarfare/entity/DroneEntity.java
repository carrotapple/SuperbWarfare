package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.item.Monitor;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class DroneEntity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> LINKED = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> CONTROLLER = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> KAMIKAZE = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float moveX = 0;
    private float moveY = 0;
    private float moveZ = 0;
    private boolean move = false;

    public String animationprocedure = "empty";

    public DroneEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.DRONE.get(), world);
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
        this.entityData.define(AMMO, 0);
        this.entityData.define(KAMIKAZE, false);
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
        compound.putInt("ammo", this.entityData.get(AMMO));
        compound.putBoolean("Kamikaze", this.entityData.get(KAMIKAZE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Linked"))
            this.entityData.set(LINKED, compound.getBoolean("Linked"));
        if (compound.contains("Controller"))
            this.entityData.set(CONTROLLER, compound.getString("Controller"));
        if (compound.contains("ammo"))
            this.entityData.set(AMMO, compound.getInt("ammo"));
        if (compound.contains("Kamikaze"))
            this.entityData.set(KAMIKAZE, compound.getBoolean("Kamikaze"));
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

        if (!this.onGround()) {
            this.level().playSound(null, this.getOnPos(), ModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
            if (control != null) {
                ItemStack stack = control.getMainHandItem();
                if (stack.getOrCreateTag().getBoolean("Using") && control instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.DRONE_SOUND.get(), 100, 1);
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
                if (this.entityData.get(AMMO) > 0) {
                    this.entityData.set(AMMO,this.entityData.get(AMMO) - 1);
                    droneDrop(player);
                }
                if (this.entityData.get(KAMIKAZE)) {
                    kamikazeExplosion(player);
                }
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

    private void kamikazeExplosion(Player player) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), player, player), 150,
                this.getX(), this.getY(), this.getZ(), 12.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION)), 10000);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);

        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == ModItems.MONITOR.get()) {
            if (!player.isCrouching()) {
                if (!this.entityData.get(LINKED)) {
                    if (stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(Component.translatable("des.superbwarfare.monitor.monitor_already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(LINKED, true);
                    this.entityData.set(CONTROLLER, player.getStringUUID());

                    Monitor.link(stack, this.getStringUUID());
                    player.displayClientMessage(Component.translatable("des.superbwarfare.monitor.linked").withStyle(ChatFormatting.GREEN), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.hit_player"))), SoundSource.PLAYERS, 0.5F, 1);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("des.superbwarfare.monitor.already_linked").withStyle(ChatFormatting.RED), true);
                }
            } else {
                if (this.entityData.get(LINKED)) {
                    if (!stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(Component.translatable("des.superbwarfare.monitor.already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(CONTROLLER, "none");
                    this.entityData.set(LINKED, false);

                    Monitor.disLink(stack);
                    player.displayClientMessage(Component.translatable("des.superbwarfare.monitor.unlinked").withStyle(ChatFormatting.RED), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.hit_player"))), SoundSource.PLAYERS, 0.5F, 1);
                    }
                }
            }
        } else if (stack.isEmpty() && player.isCrouching()) {
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.DRONE.get()));
            for (int index0 = 0; index0 < this.entityData.get(AMMO); index0++) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.GRENADE_40MM.get()));
            }
            if (!this.level().isClientSide()) this.discard();
        } else if (stack.getItem() == ModItems.GRENADE_40MM.get() && !this.entityData.get(KAMIKAZE)) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (this.entityData.get(AMMO) < 6) {
                this.entityData.set(AMMO,this.entityData.get(AMMO) + 1);
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
                }
            }
        } else if (stack.getItem() == ModItems.MORTAR_SHELLS.get() && this.entityData.get(AMMO) == 0 && !this.entityData.get(KAMIKAZE)) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.entityData.set(KAMIKAZE,true);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
            }
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
                this.setYRot(control.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(Mth.clamp(control.getXRot(), -25, 90));
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = control.getYRot();
                this.yHeadRot = control.getYRot();
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
            player.getInventory().items.stream().filter(stack -> stack.getItem() == ModItems.MONITOR.get())
                    .forEach(stack -> {
                        if (stack.getOrCreateTag().getString(Monitor.LINKED_DRONE).equals(this.getStringUUID())) {
                            Monitor.disLink(stack);
                        }
                    });
            if (this.entityData.get(KAMIKAZE)){
                destroyExplosion(player);
            }
        } else {
            if (this.entityData.get(KAMIKAZE)){
                destroyExplosion2();
            }
        }

        if (level() instanceof ServerLevel) {
            level().explode(null, this.getX(), this.getY(), this.getZ(), 0, Level.ExplosionInteraction.NONE);
        }
        this.discard();

    }

    private void destroyExplosion(Player player) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), player, player), 40,
                this.getX(), this.getY(), this.getZ(), 10f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    private void destroyExplosion2() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this), 40,
                this.getX(), this.getY(), this.getZ(), 10f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
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
        builder = builder.add(Attributes.MAX_HEALTH, 4);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.FLYING_SPEED, 0.1);
        return builder;
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            if (!this.onGround()) {
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

    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };
    private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER && side == null)
            return LazyOptional.of(() -> combined).cast();
        return super.getCapability(capability, side);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
            }
        }
    }
}
