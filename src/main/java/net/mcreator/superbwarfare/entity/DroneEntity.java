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
import net.minecraft.core.NonNullList;
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

public class DroneEntity extends LivingEntity implements GeoEntity {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> LINKED = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> CONTROLLER = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> KAMIKAZE = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Float> MOVE_X = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> MOVE_Y = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> MOVE_Z = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROT_X = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROT_Z = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean move = false;

    public String animationprocedure = "empty";

    public DroneEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.DRONE.get(), world);
    }

    public DroneEntity(EntityType<DroneEntity> type, Level world) {
        super(type, world);
    }

    public DroneEntity(EntityType<? extends DroneEntity> type, Level world, float moveX, float moveY, float moveZ) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(CONTROLLER, "undefined");
        this.entityData.define(LINKED, false);
        this.entityData.define(AMMO, 0);
        this.entityData.define(KAMIKAZE, false);
        this.entityData.define(MOVE_X, 0f);
        this.entityData.define(MOVE_Y, 0f);
        this.entityData.define(MOVE_Z, 0f);
        this.entityData.define(ROT_X, 0f);
        this.entityData.define(ROT_Z, 0f);
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
        compound.putFloat("moveX", this.entityData.get(MOVE_X));
        compound.putFloat("moveY", this.entityData.get(MOVE_Y));
        compound.putFloat("moveZ", this.entityData.get(MOVE_Z));
        compound.putFloat("rotX", this.entityData.get(ROT_X));
        compound.putFloat("rotZ", this.entityData.get(ROT_Z));
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
        if (compound.contains("moveX"))
            this.entityData.set(MOVE_X, compound.getFloat("moveX"));
        if (compound.contains("moveY"))
            this.entityData.set(MOVE_Y, compound.getFloat("moveY"));
        if (compound.contains("moveZ"))
            this.entityData.set(MOVE_Z, compound.getFloat("moveZ"));
        if (compound.contains("rotX"))
            this.entityData.set(ROT_X, compound.getFloat("rotX"));
        if (compound.contains("rotZ"))
            this.entityData.set(ROT_Z, compound.getFloat("rotZ"));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.getPersistentData().getBoolean("left")) {
            this.entityData.set(MOVE_X, -1.5f);
            this.entityData.set(ROT_X, Mth.clamp(this.entityData.get(ROT_X) + 0.05f, -0.5f, 0.5f));
        }
        if (this.getPersistentData().getBoolean("right")) {
            this.entityData.set(MOVE_X, 1.5f);
            this.entityData.set(ROT_X, Mth.clamp(this.entityData.get(ROT_X) - 0.05f, -0.5f, 0.5f));
        }

        if (this.entityData.get(ROT_X) > 0) {
            this.entityData.set(ROT_X, Mth.clamp(this.entityData.get(ROT_X) - 0.025f, 0, 0.5f));
        } else {
            this.entityData.set(ROT_X, Mth.clamp(this.entityData.get(ROT_X) + 0.025f, -0.5f, 0));
        }

        if (!this.getPersistentData().getBoolean("left") && !this.getPersistentData().getBoolean("right")) {
            if (this.entityData.get(MOVE_X) >= 0) {
                this.entityData.set(MOVE_X, Mth.clamp(this.entityData.get(MOVE_X) - 0.3f, 0, 1));
            } else {
                this.entityData.set(MOVE_X, Mth.clamp(this.entityData.get(MOVE_X) + 0.3f, -1, 0));
            }
        }

        if (this.getPersistentData().getBoolean("forward")) {
            this.entityData.set(MOVE_Z, this.entityData.get(MOVE_Z) - 0.15f);
            this.entityData.set(ROT_Z, Mth.clamp(this.entityData.get(ROT_Z) - 0.05f, -0.5f, 0.5f));
        }
        if (this.getPersistentData().getBoolean("backward")) {
            this.entityData.set(MOVE_Z, this.entityData.get(MOVE_Z) + 0.15f);
            this.entityData.set(ROT_Z, Mth.clamp(this.entityData.get(ROT_Z) + 0.05f, -0.5f, 0.5f));
        }

        if (this.entityData.get(ROT_Z) > 0) {
            this.entityData.set(ROT_Z, Mth.clamp(this.entityData.get(ROT_Z) - 0.025f, 0, 0.5f));
        } else {
            this.entityData.set(ROT_Z, Mth.clamp(this.entityData.get(ROT_Z) + 0.025f, -0.5f, 0));
        }

        if (this.entityData.get(MOVE_Z) >= 0) {
            this.entityData.set(MOVE_Z, Mth.clamp(this.entityData.get(MOVE_Z) - 0.1f, 0, 1));
        } else {
            this.entityData.set(MOVE_Z, Mth.clamp(this.entityData.get(MOVE_Z) + 0.1f, -1, 0));
        }

        if (this.getPersistentData().getBoolean("up")) {
            this.entityData.set(MOVE_Y, -1.5f);
        }
        if (this.getPersistentData().getBoolean("down")) {
            this.entityData.set(MOVE_Y, 1.5f);
        }
        if (this.entityData.get(MOVE_Y) >= 0) {
            this.entityData.set(MOVE_Y, Mth.clamp(this.entityData.get(MOVE_Y) - 0.3f, 0, 1));
        } else {
            this.entityData.set(MOVE_Y, Mth.clamp(this.entityData.get(MOVE_Y) + 0.3f, -1, 0));
        }

        this.setDeltaMovement(new Vec3(
                this.getDeltaMovement().x + -this.entityData.get(MOVE_Z) * 0.1f * this.getLookAngle().x,
                this.getDeltaMovement().y + -this.entityData.get(MOVE_Y) * 0.05f,
                this.getDeltaMovement().z + -this.entityData.get(MOVE_Z) * 0.1f * this.getLookAngle().z
        ));

        this.move = this.getPersistentData().getBoolean("left")
                || this.getPersistentData().getBoolean("right")
                || this.getPersistentData().getBoolean("forward")
                || this.getPersistentData().getBoolean("backward")
                || this.getPersistentData().getBoolean("up")
                || this.getPersistentData().getBoolean("down");

        Vec3 vec = this.getDeltaMovement();
        if (this.getDeltaMovement().horizontalDistanceSqr() < 0.75) {
            if (this.move) {
                this.setDeltaMovement(vec.multiply(1.04, 1, 1.04));
            }
        }

        LivingEntity control = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(this.entityData.get(CONTROLLER))).findFirst().orElse(null);

        if (!this.onGround()) {
            this.level().playSound(null, this.getOnPos(), ModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
            if (control != null) {
                ItemStack stack = control.getMainHandItem();
                if (stack.getOrCreateTag().getBoolean("Using") && control instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.DRONE_SOUND.get(), 100, 1);
                }
            }
        }

        if (this.getPersistentData().getBoolean("firing")) {
            if (control instanceof Player player) {
                if (this.entityData.get(AMMO) > 0) {
                    this.entityData.set(AMMO, this.entityData.get(AMMO) - 1);
                    droneDrop(player);
                }
                if (this.entityData.get(KAMIKAZE)) {
                    this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), player), 10000);
                }
            }
            this.getPersistentData().putBoolean("firing", false);
        }

        this.refreshDimensions();
    }

    private void droneDrop(Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            RgoGrenadeEntity rgoGrenadeEntity = new RgoGrenadeEntity(player, level, 160);
            rgoGrenadeEntity.setPos(this.getX(), this.getY(), this.getZ());
            rgoGrenadeEntity.shoot(0, -1, 0, 0, 0.5f);
            level.addFreshEntity(rgoGrenadeEntity);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        super.interact(player, hand);

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
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.RGO_GRENADE.get()));
            }

            if (this.entityData.get(KAMIKAZE)) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.MORTAR_SHELLS.get()));
            }

            player.getInventory().items.stream().filter(stack_ -> stack_.getItem() == ModItems.MONITOR.get())
                    .forEach(stack_ -> {
                        if (stack_.getOrCreateTag().getString(Monitor.LINKED_DRONE).equals(this.getStringUUID())) {
                            Monitor.disLink(stack_);
                        }
                    });

            if (!this.level().isClientSide()) this.discard();
        } else if (stack.getItem() == ModItems.RGO_GRENADE.get() && !this.entityData.get(KAMIKAZE)) {
            if (this.entityData.get(AMMO) < 6) {
                this.entityData.set(AMMO, this.entityData.get(AMMO) + 1);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
                }
            }
        } else if (stack.getItem() == ModItems.MORTAR_SHELLS.get() && this.entityData.get(AMMO) == 0 && !this.entityData.get(KAMIKAZE)) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.entityData.set(KAMIKAZE, true);
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
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float strafe = -this.entityData.get(MOVE_X);
                super.travel(new Vec3(2 * strafe, -this.entityData.get(MOVE_Y), -this.entityData.get(MOVE_Z)));
                Vec3 vec3 = this.getDeltaMovement();
                if (this.onGround()) {
                    this.setDeltaMovement(vec3.multiply(0.7, 0.98, 0.7));
                } else {
                    this.setDeltaMovement(vec3.multiply(1.04, 0.98, 1.04));
                }
                if (!this.move) {
                    this.setDeltaMovement(vec3.multiply(0.9, 0.8, 0.9));
                }
                return;
            }
        }
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
        }

        if (this.entityData.get(KAMIKAZE)) {
            kamikazeExplosion(source.getEntity());
        }

        if (level() instanceof ServerLevel) {
            level().explode(null, this.getX(), this.getY(), this.getZ(), 0, Level.ExplosionInteraction.NONE);
        }
        this.discard();

    }

    private void kamikazeExplosion(Entity source) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), source, source), 150,
                this.getX(), this.getY(), this.getZ(), 12.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
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
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
        builder = builder.add(Attributes.MAX_HEALTH, 4);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.FLYING_SPEED, 10);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<DroneEntity> event) {
        if (this.animationprocedure.equals("empty")) {
            if (!this.onGround()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.drone.fly"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.drone.idle"));
        }
        return PlayState.STOP;
    }

    private PlayState procedurePredicate(AnimationState<DroneEntity> event) {
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
            return super.getSlotLimit(slot);
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
