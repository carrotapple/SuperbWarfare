package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public double moveX = 0;
    public double moveY = 0;
    public double moveZ = 0;
    public boolean leftInputDown;
    public boolean rightInputDown;
    public boolean forwardInputDown;
    public boolean backInputDown;
    public boolean upInputDown;
    public boolean downInputDown;
    public boolean fire;
    public int collisionCoolDown;

    public double lastTickSpeed;
    public double lastTickVerticalSpeed;

    public DroneEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.DRONE.get(), world);
    }

    public DroneEntity(EntityType<DroneEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
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
        compound.putInt("Ammo", this.entityData.get(AMMO));
        compound.putBoolean("Kamikaze", this.entityData.get(KAMIKAZE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Linked"))
            this.entityData.set(LINKED, compound.getBoolean("Linked"));
        if (compound.contains("Controller"))
            this.entityData.set(CONTROLLER, compound.getString("Controller"));
        if (compound.contains("Ammo"))
            this.entityData.set(AMMO, compound.getInt("Ammo"));
        if (compound.contains("Kamikaze"))
            this.entityData.set(KAMIKAZE, compound.getBoolean("Kamikaze"));
    }

    public Vector3f getForwardDirection() {
        return new Vector3f(
                Mth.sin(-getYRot() * ((float) Math.PI / 180)),
                0.0f,
                Mth.cos(getYRot() * ((float) Math.PI / 180))
        ).normalize();
    }

    public Vector3f getRightDirection() {
        return new Vector3f(
                Mth.cos(-getYRot() * ((float) Math.PI / 180)),
                0.0f,
                Mth.sin(getYRot() * ((float) Math.PI / 180))
        ).normalize();
    }


    @Override
    public void baseTick() {
        super.baseTick();

        lastTickSpeed = this.getDeltaMovement().length();
        lastTickVerticalSpeed = this.getDeltaMovement().y;

        if (collisionCoolDown > 0) {
            collisionCoolDown--;
        }

        if (!this.onGround()) {
            // left and right
            if (leftInputDown) {
                moveX = Mth.clamp(moveX + 0.3f, 0, 3);
            } else if (rightInputDown) {
                moveX = Mth.clamp(moveX - 0.3f, -3, 0);
            }

            // forward and backward
            if (forwardInputDown) {
                moveZ = Mth.clamp(moveZ + 0.3f, 0, 3);
            } else if (backInputDown) {
                moveZ = Mth.clamp(moveZ - 0.3f, -3, 0);
            }

            moveX *= 0.25;
            moveZ *= 0.25;

        } else {
            moveX = 0;
            moveZ = 0;
        }

        // up and down
        if (upInputDown) {
            moveY = Mth.clamp(moveY + 0.3f, 0, 3);
        } else if (downInputDown) {
            moveY = Mth.clamp(moveY - 0.15f, -2, 0);
        }

        moveY *= 0.3;

        setDeltaMovement(getDeltaMovement().add(0.0f, (this.onGround() ? 0.059 : 0) + moveY, 0.0f));

        Vector3f direction = getRightDirection().mul((float) moveX);
        setDeltaMovement(getDeltaMovement().add(new Vec3(direction.x, direction.y, direction.z).scale(0.8)));

        Vector3f directionZ = getForwardDirection().mul((float) moveZ);
        setDeltaMovement(getDeltaMovement().add(new Vec3(directionZ.x, directionZ.y, directionZ.z).scale(0.8)));

        Vec3 vec = this.getDeltaMovement();
        this.setDeltaMovement(vec.multiply(1.055, 0.9, 1.055));

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (!this.onGround()) {
            if (controller != null) {
                ItemStack stack = controller.getMainHandItem();
                if (stack.getOrCreateTag().getBoolean("Using") && controller.level().isClientSide) {
                    controller.playSound(ModSounds.DRONE_SOUND.get(), 32, 1);
                }
                if (!controller.level().isClientSide) {
                    this.level().playSound(null, this.getOnPos(), ModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
                }
            }
        }

        if (this.isInWater()) {
            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), controller), 0.25f + (float) (2 * lastTickSpeed));
        }

        if (this.fire) {
            if (this.entityData.get(AMMO) > 0) {
                this.entityData.set(AMMO, this.entityData.get(AMMO) - 1);
                if (controller != null) {
                    droneDrop(controller);
                }
            }
            if (this.entityData.get(KAMIKAZE)) {
                this.discard();
                if (controller != null) {
                    if (controller.getMainHandItem().is(ModItems.MONITOR.get())) {
                        Monitor.disLink(controller.getMainHandItem());
                    }
                    kamikazeExplosion(controller);
                }

            }
            this.fire = false;
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
                        player.displayClientMessage(Component.translatable("tips.superbwarfare.monitor.already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(LINKED, true);
                    this.entityData.set(CONTROLLER, player.getStringUUID());

                    Monitor.link(stack, this.getStringUUID());
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.monitor.linked").withStyle(ChatFormatting.GREEN), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.5F, 1);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.drone.already_linked").withStyle(ChatFormatting.RED), true);
                }
            } else {
                if (this.entityData.get(LINKED)) {
                    if (!stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(Component.translatable("tips.superbwarfare.drone.already_linked").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.sidedSuccess(this.level().isClientSide());
                    }

                    this.entityData.set(CONTROLLER, "none");
                    this.entityData.set(LINKED, false);

                    Monitor.disLink(stack);
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.monitor.unlinked").withStyle(ChatFormatting.RED), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.5F, 1);
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
            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
                this.setYRot(controller.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(Mth.clamp(controller.getXRot(), -25, 90));
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = controller.getYRot();
                this.yHeadRot = controller.getYRot();
            }
        }

        super.travel(dir);
        float f = 0.7f;
        AABB aabb = AABB.ofSize(this.getEyePosition(), f, 0.3, f);
        var level = this.level();
        final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        for (Entity target : level.getEntitiesOfClass(Entity.class, aabb, e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {
            if (this != target && target != null && !(target instanceof RgoGrenadeEntity)) {
                hitEntityCrash(controller, target);
            }
        }

    }

    public void hitEntityCrash(Player controller, Entity target) {
        if (lastTickSpeed > 0.2) {
            if (this.entityData.get(KAMIKAZE) && 6 * lastTickSpeed > this.getHealth()) {
                target.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller), ExplosionConfig.DRONE_KAMIKAZE_HIT_DAMAGE.get());
            }
            target.hurt(ModDamageTypes.causeDroneHitDamage(this.level().registryAccess(), this, controller), (float) (5 * lastTickSpeed));
            if (target instanceof Mob mobEntity) {
                mobEntity.setTarget(this);
            }
            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), Objects.requireNonNullElse(controller, this)), (float) ((this.entityData.get(KAMIKAZE) ? 6 : 0.5) * lastTickSpeed));
        }
    }

    @Override
    public void move(@NotNull MoverType movementType, @NotNull Vec3 movement) {
        super.move(movementType, movement);
        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (lastTickSpeed < 0.2 || collisionCoolDown > 0) return;

        if ((verticalCollision) && Mth.abs((float) lastTickVerticalSpeed) > 0.2) {
            this.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller == null ? this : controller), (float) (20 * ((Mth.abs((float) lastTickVerticalSpeed) - 0.3) * (lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))));
        }

        if (this.horizontalCollision) {
            this.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller == null ? this : controller), (float) (10 * ((lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))));
            collisionCoolDown = 4;
        }
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

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (this.entityData.get(KAMIKAZE)) {
            this.discard();
            if (controller != null) {
                if (controller.getMainHandItem().is(ModItems.MONITOR.get())) {
                    Monitor.disLink(controller.getMainHandItem());
                }
                kamikazeExplosion(controller);
            }

        }

        ItemStack stack = new ItemStack(ModItems.RGO_GRENADE.get(), this.entityData.get(AMMO));

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
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), source, source), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_DAMAGE.get(),
                this.getX(), this.getY(), this.getZ(), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_RADIUS.get(), ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        ForgeEventFactory.onExplosionStart(this.level(), explosion);
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
