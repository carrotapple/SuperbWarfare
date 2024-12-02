package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.RgoGrenadeEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.Monitor;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public class DroneEntity extends LivingEntity implements GeoEntity {
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
    public static double lastTickSpeed = 0;


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

        if (!this.onGround()) {
            if (this.getPersistentData().getBoolean("left")) {
                this.entityData.set(MOVE_X, -1.5f);
                this.entityData.set(ROT_X, Mth.lerp(0.1f, this.entityData.get(ROT_X), 0.3f));
            }
            if (this.getPersistentData().getBoolean("right")) {
                this.entityData.set(MOVE_X, 1.5f);
                this.entityData.set(ROT_X, Mth.lerp(0.1f, this.entityData.get(ROT_X), -0.3f));
            }

            if (this.getPersistentData().getBoolean("forward")) {
                this.entityData.set(MOVE_Z, this.entityData.get(MOVE_Z) - 0.1f);
                this.entityData.set(ROT_Z, Mth.lerp(0.05f, this.entityData.get(ROT_Z), -0.2f));
            }
            if (this.getPersistentData().getBoolean("backward")) {
                this.entityData.set(MOVE_Z, this.entityData.get(MOVE_Z) + 0.1f);
                this.entityData.set(ROT_Z, Mth.lerp(0.05f, this.entityData.get(ROT_Z), 0.2f));
            }
        } else {
            this.entityData.set(ROT_X, 0f);
            this.entityData.set(ROT_Z, 0f);
        }

        this.entityData.set(ROT_X, Mth.lerp(0.05f, this.entityData.get(ROT_X), 0));

        if (!this.getPersistentData().getBoolean("left") && !this.getPersistentData().getBoolean("right")) {
            this.entityData.set(MOVE_X, Mth.lerp(0.1f, this.entityData.get(MOVE_X), 0));
        }

        this.entityData.set(ROT_Z, Mth.lerp(0.05f, this.entityData.get(ROT_Z), 0));

        this.entityData.set(MOVE_Z, Mth.lerp(0.05f, this.entityData.get(MOVE_Z), 0));

        if (this.getPersistentData().getBoolean("up")) {
            this.entityData.set(MOVE_Y, -1.5f);
        }
        if (this.getPersistentData().getBoolean("down")) {
            this.entityData.set(MOVE_Y, 1.5f);
        }

        this.entityData.set(MOVE_Y, Mth.lerp(0.5f, this.entityData.get(MOVE_Y), 0));

        this.setDeltaMovement(new Vec3(
                this.getDeltaMovement().x + -this.entityData.get(MOVE_Z) * 0.07f * this.getLookAngle().x,
                this.getDeltaMovement().y + (this.onGround() ? 0.059 : 0) + -this.entityData.get(MOVE_Y) * 0.05f,
                this.getDeltaMovement().z + -this.entityData.get(MOVE_Z) * 0.07f * this.getLookAngle().z
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
                this.setDeltaMovement(vec.multiply(1.035, 0.99, 1.035));
            }
        }

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (!this.onGround()) {
            this.level().playSound(null, this.getOnPos(), ModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
            if (controller != null) {
                ItemStack stack = controller.getMainHandItem();
                if (stack.getOrCreateTag().getBoolean("Using") && controller instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.DRONE_SOUND.get(), 4, 1);
                }
                controller.setYRot(controller.getYRot() - 5 * this.entityData.get(ROT_X) * Mth.abs(this.entityData.get(MOVE_Z)));
            }
        }

        if (this.isInWater()) {
            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), Objects.requireNonNullElse(controller, this)), 0.25f + (float) (2 * lastTickSpeed));
        }

        if (this.getPersistentData().getBoolean("firing")) {
            if (this.entityData.get(AMMO) > 0) {
                this.entityData.set(AMMO, this.entityData.get(AMMO) - 1);
                if (controller != null) {
                    droneDrop(controller);
                }
            }
            if (this.entityData.get(KAMIKAZE)) {
                this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), controller), 10000);
            }
            this.getPersistentData().putBoolean("firing", false);
        }

        this.refreshDimensions();
    }

    private void droneDrop(Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            RgoGrenadeEntity rgoGrenadeEntity = new RgoGrenadeEntity(player, level, 160);
            rgoGrenadeEntity.setPos(this.getX(), this.getEyeY() - 0.09, this.getZ());
            rgoGrenadeEntity.droneShoot(this);
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
        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (controller != null) {
            ItemStack stack = controller.getMainHandItem();
            if (stack.getOrCreateTag().getBoolean("Using")) {
                this.setYRot(controller.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(Mth.clamp(controller.getXRot(), -25, 90));
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = controller.getYRot();
                this.yHeadRot = controller.getYRot();
            }
        }

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

        lastTickSpeed = this.getDeltaMovement().length();
        crash(controller);
        float f = 0.7f;
        AABB aabb = AABB.ofSize(this.getEyePosition(), f, 0.3, f);
        var level = this.level();
        final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        for (Entity target : level.getEntitiesOfClass(Entity.class, aabb, e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
            if (this != target && target != null) {
                hitEntityCrash(controller, target);
            }
        }

    }

    public void crash(Player controller) {
        if (isHit() && lastTickSpeed > 0.3) {
            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), Objects.requireNonNullElse(controller, this)), (float) ((this.entityData.get(KAMIKAZE) ? 6 : 2) * lastTickSpeed));
        }
    }

    public void hitEntityCrash(Player controller, Entity target) {
        if (lastTickSpeed > 0.2) {
            if (this.entityData.get(KAMIKAZE) && 6 * lastTickSpeed > this.getHealth()) {
                target.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller), 600);
            }
            target.hurt(ModDamageTypes.causeDroneHitDamage(this.level().registryAccess(), this, controller), (float) (5 * lastTickSpeed));
            if (target instanceof Mob mobEntity) {
                mobEntity.setTarget(this);
            }
            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), Objects.requireNonNullElse(controller, this)), (float) ((this.entityData.get(KAMIKAZE) ? 6 : 0.5) * lastTickSpeed));
        }
    }

    public boolean isHit() {
        float f = 0.7f;
        AABB aabb = AABB.ofSize(this.getEyePosition(), f, 0.3, f);
        return BlockPos.betweenClosedStream(aabb).anyMatch((block) -> {
            BlockState blockstate = this.level().getBlockState(block);
            return !blockstate.isAir() && blockstate.isSuffocating(this.level(), block) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), block).move(block.getX(), block.getY(), block.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
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

        ItemStack stack = new ItemStack(ModItems.RGO_GRENADE.get(),this.entityData.get(AMMO));

        if (this.level() instanceof ServerLevel level) {
            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), stack);
            itemEntity.setPickUpDelay(10);
            level.addFreshEntity(itemEntity);
        }

        if (level() instanceof ServerLevel) {
            level().explode(null, this.getX(), this.getY(), this.getZ(), 0, Level.ExplosionInteraction.NONE);
        }
        this.discard();
    }


    private void kamikazeExplosion(Entity source) {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), source, source), 125,
                this.getX(), this.getY(), this.getZ(), 7.5f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isNoGravity() {
        return !this.onGround();
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
