package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.entity.projectile.RgoGrenadeEntity;
import com.atsuishio.superbwarfare.entity.vehicle.MobileVehicleEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.Monitor;
import com.atsuishio.superbwarfare.tools.ChunkLoadTool;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class DroneEntity extends MobileVehicleEntity implements GeoEntity {

    public static final EntityDataAccessor<Boolean> LINKED = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> CONTROLLER = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> KAMIKAZE_MODE = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DELTA_ROT = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DELTA_X_ROT = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> PROPELLER_ROT = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = 5;

    public float propellerRot;
    public float propellerRotO;

    public boolean fire;
    public int collisionCoolDown;
    public double lastTickSpeed;
    public double lastTickVerticalSpeed;
    public float pitch;
    public float pitchO;
    public Set<Long> loadedChunks = new HashSet<>();

    public DroneEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.DRONE.get(), world);
    }

    public DroneEntity(EntityType<DroneEntity> type, Level world) {
        super(type, world);
    }

    public float getBodyPitch() {
        return pitch;
    }

    public void setBodyXRot(float rot) {
        pitch = rot;
    }

    public float getBodyPitch(float tickDelta) {
        return Mth.lerp(0.6f * tickDelta, pitchO, getBodyPitch());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        this.hurt(amount);
        return true;
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    public DroneEntity(EntityType<? extends DroneEntity> type, Level world, float moveX, float moveY, float moveZ) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PROPELLER_ROT, 0f);
        this.entityData.define(DELTA_ROT, 0f);
        this.entityData.define(DELTA_X_ROT, 0f);
        this.entityData.define(CONTROLLER, "undefined");
        this.entityData.define(LINKED, false);
        this.entityData.define(AMMO, 0);
        this.entityData.define(KAMIKAZE_MODE, 0);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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
        compound.putInt("KamikazeMode", this.entityData.get(KAMIKAZE_MODE));
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
        if (compound.contains("KamikazeMode"))
            this.entityData.set(KAMIKAZE_MODE, compound.getInt("KamikazeMode"));
    }

    @Override
    public void baseTick() {
        pitchO = this.getBodyPitch();
        setBodyXRot(pitch * 0.9f);
        propellerRotO = this.getPropellerRot();

        super.baseTick();

        if (this.level() instanceof ServerLevel serverLevel) {
            // 更新需要加载的区块
            ChunkLoadTool.updateLoadedChunks(serverLevel, this, this.loadedChunks);
        }

        lastTickSpeed = this.getDeltaMovement().length();
        lastTickVerticalSpeed = this.getDeltaMovement().y;

        if (collisionCoolDown > 0) {
            collisionCoolDown--;
        }

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (!this.onGround()) {
            if (controller != null) {
                handleSimulationDistance(controller);
                ItemStack stack = controller.getMainHandItem();
                if (stack.is(ModItems.MONITOR.get())) {
                    if (stack.getOrCreateTag().getBoolean("Using") && controller.level().isClientSide) {
                        controller.playSound(ModSounds.DRONE_SOUND.get(), 32, 1);
                    }
                } else {
                    upInputDown = false;
                    downInputDown = false;
                    forwardInputDown = false;
                    backInputDown = false;
                    leftInputDown = false;
                    rightInputDown = false;
                }
                if (!controller.level().isClientSide) {
                    this.level().playSound(null, this.getOnPos(), ModSounds.DRONE_SOUND.get(), SoundSource.AMBIENT, 3, 1);
                }

                if (tickCount % 5 == 0) {
                    controller.getInventory().items.stream().filter(pStack -> pStack.getItem() == ModItems.MONITOR.get())
                            .forEach(pStack -> {
                                if (pStack.getOrCreateTag().getString(Monitor.LINKED_DRONE).equals(this.getStringUUID())) {
                                    Monitor.getDronePos(pStack, this.position());
                                }
                            });
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
            if (this.entityData.get(KAMIKAZE_MODE) != 0) {
                if (controller != null) {
                    if (controller.getMainHandItem().is(ModItems.MONITOR.get())) {
                        Monitor.disLink(controller.getMainHandItem(), controller);
                    }
                    this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), controller), 10000);
                }

            }
            this.fire = false;
        }

        this.refreshDimensions();
    }

    public void handleSimulationDistance(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer) {
            var distanceManager = serverLevel.getChunkSource().chunkMap.getDistanceManager();
            var playerTicketManager = distanceManager.playerTicketManager;
            int maxDistance = playerTicketManager.viewDistance;

            if (this.position().vectorTo(player.position()).horizontalDistance() > maxDistance * 16) {
                upInputDown = false;
                downInputDown = false;
                forwardInputDown = false;
                backInputDown = false;
                leftInputDown = false;
                rightInputDown = false;
                Vec3 toVec = position().vectorTo(player.position()).normalize();
                setDeltaMovement(getDeltaMovement().add(new Vec3(toVec.x, 0, toVec.z).scale(0.2)));
            }
        }
    }

    public float getPropellerRot() {
        return this.propellerRot;
    }

    public void setPropellerRot(float pPropellerRot) {
        this.propellerRot = pPropellerRot;
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

                    Monitor.disLink(stack, player);
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.monitor.unlinked").withStyle(ChatFormatting.RED), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.5F, 1);
                    }
                }
            }
        } else if (stack.isEmpty() && player.isCrouching()) {
            // 返还物品
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.DRONE.get()));

            // 返还普通弹药
            for (int index0 = 0; index0 < this.entityData.get(AMMO); index0++) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.RGO_GRENADE.get()));
            }

            // 返还神风弹药
            if (this.entityData.get(KAMIKAZE_MODE) == 1) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.MORTAR_SHELLS.get()));
            } else if (this.entityData.get(KAMIKAZE_MODE) == 2) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.EXPLOSIVE_MINE.get()));
            }

            player.getInventory().items.stream().filter(stack_ -> stack_.getItem() == ModItems.MONITOR.get())
                    .forEach(stack_ -> {
                        if (stack_.getOrCreateTag().getString(Monitor.LINKED_DRONE).equals(this.getStringUUID())) {
                            Monitor.disLink(stack_, player);
                        }
                    });

            if (!this.level().isClientSide()) this.discard();
        } else if (stack.getItem() == ModItems.RGO_GRENADE.get() && this.entityData.get(KAMIKAZE_MODE) == 0) {
            // 装载普通弹药
            if (this.entityData.get(AMMO) < 6) {
                this.entityData.set(AMMO, this.entityData.get(AMMO) + 1);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
                }
            }
        } else if (stack.getItem() == ModItems.MORTAR_SHELLS.get() && this.entityData.get(AMMO) == 0 && this.entityData.get(KAMIKAZE_MODE) == 0) {
            // 迫击炮神风
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.entityData.set(KAMIKAZE_MODE, 1);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
            }
        } else if (stack.getItem() == ModItems.EXPLOSIVE_MINE.get() && this.entityData.get(AMMO) == 0 && this.entityData.get(KAMIKAZE_MODE) == 0) {
            // C4神风
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.entityData.set(KAMIKAZE_MODE, 2);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 0.5F, 1);
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void travel() {
        float diffX;
        float diffY;
        if (!this.onGround()) {
            // left and right
            if (rightInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) - 0.3f);
            } else if (this.leftInputDown) {
                this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) + 0.3f);
            }

            // forward and backward
            if (forwardInputDown) {
                this.entityData.set(DELTA_X_ROT, this.entityData.get(DELTA_X_ROT) - 0.3f);
            } else if (backInputDown) {
                this.entityData.set(DELTA_X_ROT, this.entityData.get(DELTA_X_ROT) + 0.3f);
            }

            float f = (float) (0.97f - 0.02f * lastTickSpeed);
            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.9, f));

        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 1, 0.8));
            this.setZRot(this.roll * 0.9f);
            this.setXRot(this.getXRot() * 0.9f);
            this.setBodyXRot(this.getBodyPitch() * 0.9f);
        }

        if (this.isInWater() && this.tickCount % 4 == 0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            this.hurt(ModDamageTypes.causeVehicleStrikeDamage(this.level().registryAccess(), this, this.getFirstPassenger() == null ? this : this.getFirstPassenger()), 26 + (float) (60 * ((lastTickSpeed - 0.4) * (lastTickSpeed - 0.4))));
        }

        boolean up = this.upInputDown;
        boolean down = this.downInputDown;

        if (up) {
            this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.01f, 0.15f));
        }

        if (down) {
            this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.01f, 0));
        }

        if (!(up || down)) {
            if (this.getDeltaMovement().y() < 0) {
                this.entityData.set(POWER, Math.min(this.entityData.get(POWER) + 0.001f, 0.15f));
            } else {
                this.entityData.set(POWER, Math.max(this.entityData.get(POWER) - 0.001f, 0));
            }
        }

        this.entityData.set(DELTA_ROT, this.entityData.get(DELTA_ROT) * 0.8f);
        this.entityData.set(DELTA_X_ROT, this.entityData.get(DELTA_X_ROT) * 0.8f);
        this.entityData.set(PROPELLER_ROT, Mth.lerp(0.08f, this.entityData.get(PROPELLER_ROT), this.entityData.get(POWER)));
        this.setPropellerRot(this.getPropellerRot() + 30 * this.entityData.get(PROPELLER_ROT));
        this.entityData.set(PROPELLER_ROT, this.entityData.get(PROPELLER_ROT) * 0.9995f);

        this.setZRot(Mth.clamp(this.getRoll() - this.entityData.get(DELTA_ROT), -30, 30));
        this.setBodyXRot(Mth.clamp(this.getBodyPitch() - this.entityData.get(DELTA_X_ROT), -30, 30));

        setDeltaMovement(getDeltaMovement().add(0.0f, Math.min(Math.sin((90 - this.getBodyPitch()) * Mth.DEG_TO_RAD), Math.sin((90 + this.getRoll()) * Mth.DEG_TO_RAD)) * this.entityData.get(POWER), 0.0f));

        Vector3f direction = getRightDirection().mul(Math.cos((this.getRoll() + 90) * Mth.DEG_TO_RAD) * 0.1f);
        setDeltaMovement(getDeltaMovement().add(new Vec3(direction.x, direction.y, direction.z).scale(4)));

        Vector3f directionZ = getForwardDirection().mul(-Math.cos((this.getBodyPitch() + 90) * Mth.DEG_TO_RAD) * 0.1f);
        setDeltaMovement(getDeltaMovement().add(new Vec3(directionZ.x, directionZ.y, directionZ.z).scale(4)));

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));
        if (controller != null) {
            ItemStack stack = controller.getMainHandItem();
            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
                diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(controller.getYHeadRot() - this.getYRot()));
                diffX = Math.clamp(-60f, 60f, Mth.wrapDegrees(controller.getXRot() - this.getXRot()));
                this.setYRot(this.getYRot() + 0.5f * diffY);
                this.setXRot(Mth.clamp(this.getXRot() + 0.5f * diffX, -10, 90));
            }
        }

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
        if (lastTickSpeed > 0.12) {
            if (this.entityData.get(KAMIKAZE_MODE) != 0 && 20 * lastTickSpeed > this.getHealth()) {
                target.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller), ExplosionConfig.DRONE_KAMIKAZE_HIT_DAMAGE.get());
                if (controller != null && controller.getMainHandItem().is(ModItems.MONITOR.get())) {
                    Monitor.disLink(controller.getMainHandItem(), controller);
                }
            }
            target.hurt(ModDamageTypes.causeDroneHitDamage(this.level().registryAccess(), this, controller), (float) (5 * lastTickSpeed));

            this.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), Objects.requireNonNullElse(controller, this)), (float) (((this.entityData.get(KAMIKAZE_MODE) != 0) ? 20 : 4) * lastTickSpeed));
        }
    }

    @Override
    public void move(@NotNull MoverType movementType, @NotNull Vec3 movement) {
        super.move(movementType, movement);
        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));

        if (lastTickSpeed < 0.2 || collisionCoolDown > 0) return;

        if ((verticalCollision) && Mth.abs((float) lastTickVerticalSpeed) > 0.3) {
            this.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller == null ? this : controller), (float) (20 * ((Mth.abs((float) lastTickVerticalSpeed) - 0.3) * (lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))));
            collisionCoolDown = 4;
        }

        if (this.horizontalCollision) {
            this.hurt(ModDamageTypes.causeCustomExplosionDamage(this.level().registryAccess(), this, controller == null ? this : controller), (float) (10 * ((lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))));
            collisionCoolDown = 4;
        }
    }

    @Override
    public void destroy() {
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
                            Monitor.disLink(stack, player);
                        }
                    });
        }

        if (this.entityData.get(KAMIKAZE_MODE) != 0) {
            kamikazeExplosion(this.entityData.get(KAMIKAZE_MODE));
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

        Player controller = EntityFindUtil.findPlayer(this.level(), this.entityData.get(CONTROLLER));
        if (controller != null) {
            if (controller.getMainHandItem().is(ModItems.MONITOR.get())) {
                Monitor.disLink(controller.getMainHandItem(), controller);
                this.discard();
            }
        }

        this.discard();
    }


    private void kamikazeExplosion(int mode) {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
        CustomExplosion explosion = switch (mode) {
            case 1 -> new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_DAMAGE.get(),
                    this.getX(), this.getY(), this.getZ(), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_RADIUS.get(), ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);

            case 2 -> new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), ExplosionConfig.C4_EXPLOSION_DAMAGE.get(),
                    this.getX(), this.getY(), this.getZ(), ExplosionConfig.C4_EXPLOSION_RADIUS.get(), ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);

            default -> null;
        };

        if (explosion == null) return;

        explosion.explode();
        ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float ignoreExplosionHorizontalKnockBack() {
        return 0;
    }

    @Override
    public float ignoreExplosionVerticalKnockBack() {
        return 0;
    }

    @Override
    public void onRemovedFromWorld() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ChunkLoadTool.unloadAllChunks(serverLevel, this, this.loadedChunks);
        }
        super.onRemovedFromWorld();
    }
}
