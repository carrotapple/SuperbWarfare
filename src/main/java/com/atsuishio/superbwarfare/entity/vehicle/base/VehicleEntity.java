package com.atsuishio.superbwarfare.entity.vehicle.base;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.VectorTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public abstract class VehicleEntity extends Entity {

    public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> LAST_ATTACKER_UUID = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<String> LAST_DRIVER_UUID = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.STRING);

    protected int interpolationSteps;
    protected double x;
    protected double y;
    protected double z;
    protected double serverYRot;
    protected double serverXRot;

    public float roll;
    public float prevRoll;
    public int lastHurtTick;
    public int repairCoolDown = maxRepairCoolDown();
    public boolean crash;

    // 自定义骑乘
    private final List<Entity> orderedPassengers = generatePassengersList();

    private ArrayList<Entity> generatePassengersList() {
        var list = new ArrayList<Entity>(this.getMaxPassengers());
        for (int i = 0; i < this.getMaxPassengers(); i++) {
            list.add(null);
        }
        return list;
    }

    /**
     * 获取按顺序排列的成员列表
     *
     * @return 按顺序排列的成员列表
     */
    public List<Entity> getOrderedPassengers() {
        return orderedPassengers;
    }

    // 仅在客户端存在的实体顺序获取，用于在客户端正确同步实体座位顺序
    public Function<Entity, Integer> entityIndexOverride = null;

    @Override
    protected void addPassenger(@NotNull Entity newPassenger) {
        if (newPassenger.getVehicle() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        }

        int index;

        if (entityIndexOverride != null && entityIndexOverride.apply(newPassenger) != -1) {
            index = entityIndexOverride.apply(newPassenger);
        } else {
            index = 0;
            for (Entity passenger : orderedPassengers) {
                if (passenger == null) {
                    break;
                }
                index++;
            }
        }
        if (index >= getMaxPassengers() || index < 0) return;

        orderedPassengers.set(index, newPassenger);
        this.passengers = ImmutableList.copyOf(orderedPassengers.stream().filter(Objects::nonNull).toList());
        this.gameEvent(GameEvent.ENTITY_MOUNT, newPassenger);
    }

    @Override
    protected void removePassenger(@NotNull Entity pPassenger) {
        if (pPassenger.getVehicle() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        }

        var index = getSeatIndex(pPassenger);
        if (index == -1) return;

        orderedPassengers.set(index, null);
        this.passengers = ImmutableList.copyOf(orderedPassengers.stream().filter(Objects::nonNull).toList());

        pPassenger.boardingCooldown = 60;
        this.gameEvent(GameEvent.ENTITY_DISMOUNT, pPassenger);
    }

    @Nullable
    @Override
    public Entity getFirstPassenger() {
        return orderedPassengers.get(0);
    }

    /**
     * 获取第index个乘客
     *
     * @param index 目标座位
     * @return 目标座位的乘客
     */
    public Entity getNthEntity(int index) {
        return orderedPassengers.get(index);
    }

    /**
     * 尝试切换座位
     *
     * @param entity 乘客
     * @param index  目标座位
     * @return 是否切换成功
     */
    public boolean changeSeat(Entity entity, int index) {
        if (index < 0 || index >= getMaxPassengers()) return false;
        if (orderedPassengers.get(index) != null) return false;
        if (!orderedPassengers.contains(entity)) return false;

        orderedPassengers.set(orderedPassengers.indexOf(entity), null);
        orderedPassengers.set(index, entity);

        // 在服务端运行时，向所有玩家同步载具座位信息
        if (!this.level().isClientSide) {
            ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ClientboundSetPassengersPacket(this));
        }

        return true;
    }

    /**
     * 获取乘客所在座位索引
     *
     * @param entity 乘客
     * @return 座位索引
     */
    public int getSeatIndex(Entity entity) {
        return orderedPassengers.indexOf(entity);
    }

    public float getRoll() {
        return roll;
    }

    public float getRoll(float tickDelta) {
        return Mth.lerp(tickDelta, prevRoll, getRoll());
    }

    public float getYaw(float tickDelta) {
        return Mth.lerp(tickDelta, yRotO, getYRot());
    }

    public float getPitch(float tickDelta) {
        return Mth.lerp(tickDelta, xRotO, getXRot());
    }

    public void setZRot(float rot) {
        roll = rot;
    }

    public VehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, this.getMaxHealth());
        this.entityData.define(LAST_ATTACKER_UUID, "undefined");
        this.entityData.define(LAST_DRIVER_UUID, "undefined");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(LAST_ATTACKER_UUID, compound.getString("LastAttacker"));
        this.entityData.set(LAST_DRIVER_UUID, compound.getString("LastDriver"));
        this.entityData.set(HEALTH, compound.getFloat("Health"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.entityData.get(HEALTH));
        compound.putString("LastAttacker", this.entityData.get(LAST_ATTACKER_UUID));
        compound.putString("LastDriver", this.entityData.get(LAST_DRIVER_UUID));
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;

        ItemStack stack = player.getMainHandItem();
        if (player.isShiftKeyDown() && stack.is(ModItems.CROWBAR.get()) && this.getFirstPassenger() == null) {
            ItemStack container = ContainerBlockItem.createInstance(this);
            if (!player.addItem(container)) {
                player.drop(container, false);
            }
            this.remove(RemovalReason.DISCARDED);
            this.discard();
            return InteractionResult.SUCCESS;
        } else if (this.getHealth() < this.getMaxHealth() && stack.is(Items.IRON_INGOT)) {
            this.heal(Math.min(50, this.getMaxHealth()));
            stack.shrink(1);
            if (!this.level().isClientSide) {
                this.level().playSound(null, this, SoundEvents.IRON_GOLEM_REPAIR, this.getSoundSource(), 0.5f, 1);
            }
            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown()) {
            if (this.getFirstPassenger() == null) {
                if (player instanceof FakePlayer) return InteractionResult.PASS;
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else if (!(this.getFirstPassenger() instanceof Player)) {
                if (player instanceof FakePlayer) return InteractionResult.PASS;
                this.getFirstPassenger().stopRiding();
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            if (this.canAddPassenger(player)) {
                if (player instanceof FakePlayer) return InteractionResult.PASS;
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    public double getYRotFromVector(Vec3 vec3) {
        this.setDeltaMovement(vec3);
        return Mth.atan2(vec3.x, vec3.z) * (180F / Math.PI);
    }

    public double getXRotFromVector(Vec3 vec3) {
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        return Mth.atan2(vec3.y, d0) * (180F / Math.PI);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        // 计算减伤后的伤害
        float computedAmount = damageModifier.compute(source, amount);
        this.crash = source.is(ModDamageTypes.VEHICLE_STRIKE);

        if (source.getEntity() != null) {
            this.entityData.set(LAST_ATTACKER_UUID, source.getEntity().getStringUUID());
        }

        // 受伤打断呼吸回血
        if (computedAmount > 0) {
            lastHurtTick = 0;
            repairCoolDown = maxRepairCoolDown();
        }

        this.onHurt(computedAmount, source.getEntity(), true);

        // 显示火花粒子效果
        if (this.sendFireStarParticleOnHurt() && this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 0.5 * getBbHeight(), this.getZ(), 2, 0.4, 0.4, 0.4, 0.2, false);
        }
        // 播放受击音效
        if (this.playHitSoundOnHurt()) {
            this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        }

        return super.hurt(source, computedAmount);
    }

    /**
     * 受击时是否显示火花粒子效果
     */
    public boolean sendFireStarParticleOnHurt() {
        return true;
    }

    /**
     * 受击时是否播放受击音效
     */
    public boolean playHitSoundOnHurt() {
        return true;
    }

    protected final DamageModifier damageModifier = this.getDamageModifier();

    /**
     * 控制载具伤害免疫
     *
     * @return DamageModifier
     */
    public DamageModifier getDamageModifier() {
        return new DamageModifier()
                .immuneTo(source -> source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
                .immuneTo(DamageTypes.FALL)
                .immuneTo(DamageTypes.CACTUS)
                .immuneTo(DamageTypes.DROWN)
                .immuneTo(DamageTypes.DRAGON_BREATH)
                .immuneTo(DamageTypes.WITHER)
                .immuneTo(DamageTypes.WITHER_SKULL)
                .reduce(8, ModDamageTypes.VEHICLE_STRIKE);
    }

    public void heal(float pHealAmount) {
        if (this.level() instanceof ServerLevel) {
            this.setHealth(this.getHealth() + pHealAmount);
        }

    }

    public void onHurt(float pHealAmount, Entity attacker, boolean send) {
        if (this.level() instanceof ServerLevel) {
            var holder = Holder.direct(ModSounds.INDICATION_VEHICLE.get());
            if (attacker instanceof ServerPlayer player && pHealAmount > 0 && this.getHealth() > 0 && send && !(this instanceof DroneEntity)) {

                player.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player.getX(), player.getEyeY(), player.getZ(), 0.25f + (2.75f * pHealAmount / getMaxHealth()), random.nextFloat() * 0.1f + 0.9f, player.level().random.nextLong()));
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(3, 5));
            }

            if (pHealAmount > 0 && this.getHealth() > 0 && send) {
                List<Entity> passengers = this.getPassengers();
                for (var entity : passengers) {
                    if (entity instanceof ServerPlayer player1) {
                        player1.connection.send(new ClientboundSoundPacket(holder, SoundSource.PLAYERS, player1.getX(), player1.getEyeY(), player1.getZ(), 0.25f + (4.75f * pHealAmount / getMaxHealth()), random.nextFloat() * 0.1f + 0.6f, player1.level().random.nextLong()));
                    }
                }
            }

            this.setHealth(this.getHealth() - pHealAmount);
        }
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }

    public void setHealth(float pHealth) {
        this.entityData.set(HEALTH, Mth.clamp(pHealth, 0.0F, this.getMaxHealth()));
    }

    public float getMaxHealth() {
        return 50;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return super.isPushable();
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity attacker) {
        return hasPassenger(attacker) || super.skipAttackInteraction(attacker);
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    public int getMaxPassengers() {
        return 1;
    }

    public double getSubmergedHeight(Entity entity) {
        for (FluidType fluidType : ForgeRegistries.FLUID_TYPES.get().getValues()) {
            if (entity.level().getFluidState(entity.blockPosition()).getFluidType() == fluidType)
                return entity.getFluidTypeHeight(fluidType);
        }
        return 0;
    }

    /**
     * 呼吸回血冷却时长(单位:tick)，设为小于0的值以禁用呼吸回血
     */
    public int maxRepairCoolDown() {
        return VehicleConfig.REPAIR_COOLDOWN.get();
    }

    /**
     * 呼吸回血回血量
     */
    public float repairAmount() {
        return VehicleConfig.REPAIR_AMOUNT.get().floatValue();
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.lastHurtTick++;

        if (repairCoolDown > 0) {
            repairCoolDown--;
        }

        this.prevRoll = this.getRoll();

        float delta = Math.abs(getYRot() - yRotO);
        while (getYRot() > 180F) {
            setYRot(getYRot() - 360F);
            yRotO = getYRot() - delta;
        }
        while (getYRot() <= -180F) {
            setYRot(getYRot() + 360F);
            yRotO = delta + getYRot();
        }

        float deltaZ = Math.abs(getRoll() - prevRoll);
        while (getRoll() > 180F) {
            setZRot(getRoll() - 360F);
            prevRoll = getRoll() - deltaZ;
        }
        while (getRoll() <= -180F) {
            setZRot(getRoll() + 360F);
            prevRoll = deltaZ + getRoll();
        }

        handleClientSync();

        if (this.level() instanceof ServerLevel && this.getHealth() <= 0) {
            destroy();
        }

        travel();

        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));

        if (this.getHealth() <= 0.1 * this.getMaxHealth()) {
            // 血量过低时自动扣血
            this.onHurt(0.1f, attacker, false);
        } else {
            // 呼吸回血
            if (repairCoolDown == 0) {
                this.heal(repairAmount());
            }
        }

        if (getFirstPassenger() != null) {
            this.entityData.set(LAST_DRIVER_UUID, getFirstPassenger().getStringUUID());
        }

        this.refreshDimensions();
    }

    public void lowHealthWarning() {
        if (this.getHealth() <= 0.4 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
            }
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getHealth() <= 0.25 * this.getMaxHealth()) {
                playLowHealthParticle(serverLevel);
            }
            if (this.getHealth() <= 0.15 * this.getMaxHealth()) {
                playLowHealthParticle(serverLevel);
            }
        }

        if (this.getHealth() <= 0.1 * this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 2, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
                ParticleTool.sendParticle(serverLevel, ParticleTypes.FLAME, this.getX(), this.getY() + 0.85f * getBbHeight(), this.getZ(), 4, 0.35 * this.getBbWidth(), 0.12 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.05, true);
                ParticleTool.sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 0.85f * getBbHeight(), this.getZ(), 4, 0.1 * this.getBbWidth(), 0.05 * this.getBbHeight(), 0.1 * this.getBbWidth(), 0.4, true);
            }
            if (this.tickCount % 15 == 0) {
                this.level().playSound(null, this.getOnPos(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1, 1);
            }
        }

        if (this.getHealth() < 0.1f * this.getMaxHealth() && tickCount % 13 == 0) {
            this.level().playSound(null, this.getOnPos(), ModSounds.NO_HEALTH.get(), SoundSource.PLAYERS, 1, 1);
        } else if (this.getHealth() >= 0.1f && this.getHealth() < 0.4f * this.getMaxHealth() && tickCount % 10 == 0) {
            this.level().playSound(null, this.getOnPos(), ModSounds.LOW_HEALTH.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    private void playLowHealthParticle(ServerLevel serverLevel) {
        ParticleTool.sendParticle(serverLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
        ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.7f * getBbHeight(), this.getZ(), 1, 0.35 * this.getBbWidth(), 0.15 * this.getBbHeight(), 0.35 * this.getBbWidth(), 0.01, true);
    }

    public void destroy() {
    }

    protected Entity getAttacker() {
        return EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));
    }

    protected void crashPassengers() {
        for (var entity : this.getPassengers()) {
            if (entity instanceof LivingEntity living) {
                var tempAttacker = living == getAttacker() ? null : getAttacker();

                living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeAirCrashDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
            }
        }
    }

    protected void explodePassengers() {
        for (var entity : this.getPassengers()) {
            if (entity instanceof LivingEntity living) {
                var tempAttacker = living == getAttacker() ? null : getAttacker();

                living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
                living.invulnerableTime = 0;
                living.hurt(ModDamageTypes.causeVehicleExplosionDamage(this.level().registryAccess(), null, tempAttacker), Integer.MAX_VALUE);
            }
        }
    }

    public void travel() {
    }

    // From Immersive_Aircraft
    public Matrix4f getVehicleTransform() {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) getX(), (float) getY(), (float) getZ());
        transform.rotate(Axis.YP.rotationDegrees(-getYRot()));
        transform.rotate(Axis.XP.rotationDegrees(getXRot()));
        transform.rotate(Axis.ZP.rotationDegrees(getRoll()));
        return transform;
    }

    public Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }

    public void handleClientSync() {
        if (isControlledByLocalInstance()) {
            interpolationSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }
        if (interpolationSteps <= 0) {
            return;
        }
        double interpolatedX = getX() + (x - getX()) / (double) interpolationSteps;
        double interpolatedY = getY() + (y - getY()) / (double) interpolationSteps;
        double interpolatedZ = getZ() + (z - getZ()) / (double) interpolationSteps;
        double interpolatedYaw = Mth.wrapDegrees(serverYRot - (double) getYRot());
        setYRot(getYRot() + (float) interpolatedYaw / (float) interpolationSteps);
        setXRot(getXRot() + (float) (serverXRot - (double) getXRot()) / (float) interpolationSteps);

        setPos(interpolatedX, interpolatedY, interpolatedZ);
        setRot(getYRot(), getXRot());

        --interpolationSteps;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.x = x;
        this.y = y;
        this.z = z;
        serverYRot = yaw;
        serverXRot = pitch;
        this.interpolationSteps = 10;
    }

    public static double calculateAngle(Vec3 move, Vec3 view) {
        move = move.multiply(1, 0, 1).normalize();
        view = view.multiply(1, 0, 1).normalize();

        return VectorTool.calculateAngle(move, view);
    }

    protected Vec3 getDismountOffset(double vehicleWidth, double passengerWidth) {
        double offset = (vehicleWidth + passengerWidth + (double) 1.0E-5f) / 1.75;
        float yaw = getYRot() + 90.0f;
        float x = -Mth.sin(yaw * ((float) Math.PI / 180));
        float z = Mth.cos(yaw * ((float) Math.PI / 180));
        float n = Math.max(Math.abs(x), Math.abs(z));
        return new Vec3((double) x * offset / (double) n, 0.0, (double) z * offset / (double) n);
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3d = getDismountOffset(getBbWidth() * Mth.SQRT_OF_TWO, passenger.getBbWidth() * Mth.SQRT_OF_TWO);
        double ox = getX() - vec3d.x;
        double oz = getZ() + vec3d.z;
        BlockPos exitPos = new BlockPos((int) ox, (int) getY(), (int) oz);
        BlockPos floorPos = exitPos.below();
        if (!level().isWaterAt(floorPos)) {
            ArrayList<Vec3> list = Lists.newArrayList();
            double exitHeight = level().getBlockFloorHeight(exitPos);
            if (DismountHelper.isBlockFloorValid(exitHeight)) {
                list.add(new Vec3(ox, (double) exitPos.getY() + exitHeight, oz));
            }
            double floorHeight = level().getBlockFloorHeight(floorPos);
            if (DismountHelper.isBlockFloorValid(floorHeight)) {
                list.add(new Vec3(ox, (double) floorPos.getY() + floorHeight, oz));
            }
            for (Pose entityPose : passenger.getDismountPoses()) {
                for (Vec3 vec3d2 : list) {
                    if (!DismountHelper.canDismountTo(level(), vec3d2, passenger, entityPose)) continue;
                    passenger.setPose(entityPose);
                    return vec3d2;
                }
            }
        }

        return super.getDismountLocationForPassenger(passenger);
    }

    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/gun_icon/default_icon.png");
    }

    public boolean allowFreeCam() {
        return false;
    }

    // 本方法留空
    @Override
    public void push(double pX, double pY, double pZ) {
    }
}
