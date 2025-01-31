package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.gui.RangeHelper;
import com.atsuishio.superbwarfare.config.server.ExplosionDestroyConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.projectile.CannonShellEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.common.ammo.CannonShellItem;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class Mk42Entity extends VehicleEntity implements GeoEntity, ICannonEntity {

    public static final EntityDataAccessor<Integer> COOL_DOWN = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final float MAX_HEALTH = VehicleConfig.MK42_HP.get();
    public static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(Mk42Entity.class, EntityDataSerializers.FLOAT);

    public Mk42Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.MK_42.get(), world);
    }

    public Mk42Entity(EntityType<Mk42Entity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COOL_DOWN, 0);
        this.entityData.define(PITCH, 0f);
        this.entityData.define(YAW, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("CoolDown", this.entityData.get(COOL_DOWN));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COOL_DOWN, compound.getInt("CoolDown"));
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 2.16F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        if (this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), this.getX(), this.getY() + 2.5, this.getZ(), 4, 0.2, 0.2, 0.2, 0.2, false);
        }

        if (source.is(DamageTypes.ARROW)) {
            amount *= 0.1f;
        }
        if (source.is(DamageTypes.TRIDENT)) {
            amount *= 0.1f;
        }
        if (source.is(DamageTypes.MOB_ATTACK)) {
            amount *= 0.3f;
        }
        if (source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
            amount *= 0.15f;
        }
        if (source.is(DamageTypes.MOB_PROJECTILE)) {
            amount *= 0.15f;
        }
        if (source.is(DamageTypes.PLAYER_ATTACK)) {
            amount *= 0.1f;

        }
        if (source.is(DamageTypes.LAVA)) {
            amount *= 2.5f;
        }
        if (source.is(DamageTypes.EXPLOSION)) {
            amount *= 1f;
        }
        if (source.is(DamageTypes.PLAYER_EXPLOSION)) {
            amount *= 1f;
        }

        if (source.is(ModDamageTypes.CUSTOM_EXPLOSION)) {
            amount *= 0.4f;
        }
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            amount *= 0.4f;
        }
        if (source.is(ModDamageTypes.MINE)) {
            amount *= 0.14f;
        }
        if (source.is(ModDamageTypes.LUNGE_MINE)) {
            amount *= 0.14f;
        }
        if (source.is(ModDamageTypes.CANNON_FIRE)) {
            amount *= 0.3f;
        }
        if (source.is(ModTags.DamageTypes.PROJECTILE)) {
            amount *= 0.02f;
        }
        if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            amount *= 0.14f;
        }
        if (source.is(ModDamageTypes.VEHICLE_STRIKE)) {
            amount *= 1.7f;
        }

        this.level().playSound(null, this.getOnPos(), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
        this.hurt(Math.max(amount - 8, 0));
        return true;
    }

    @Override
    public Vec3 getDeltaMovement() {
        return new Vec3(0, Math.min(super.getDeltaMovement().y, 0), 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.entityData.get(COOL_DOWN) > 0) {
            this.entityData.set(COOL_DOWN, this.entityData.get(COOL_DOWN) - 1);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        lowHealthWarning();

        this.refreshDimensions();
    }

    @Override
    public void destroy() {
        Entity attacker = EntityFindUtil.findEntity(this.level(), this.entityData.get(LAST_ATTACKER_UUID));

        if (level() instanceof ServerLevel) {
            CustomExplosion explosion = new CustomExplosion(this.level(), this,
                    ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), attacker, attacker), 100f,
                    this.getX(), this.getY(), this.getZ(), 7f, ExplosionDestroyConfig.EXPLOSION_DESTROY.get() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
            explosion.explode();
            net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
            explosion.finalizeExplosion(false);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }


        List<Entity> passengers = this.getPassengers();
        for (var entity : passengers) {
            if (entity instanceof LivingEntity living) {
                var tempAttacker = living == attacker ? null : attacker;

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

        this.discard();

    }

    @Override
    public void vehicleShoot(Player player) {
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
                hitDamage = VehicleConfig.MK42_HE_DAMAGE.get();
                explosionRadius = VehicleConfig.MK42_HE_EXPLOSION_RADIUS.get();
                explosionDamage = VehicleConfig.MK42_HE_EXPLOSION_DAMAGE.get();
                fireProbability = 0.18F;
                fireTime = 2;
                durability = 1;
            }

            if (stack.is(ModItems.AP_5_INCHES.get())) {
                hitDamage = VehicleConfig.MK42_AP_DAMAGE.get();
                explosionRadius = VehicleConfig.MK42_AP_EXPLOSION_RADIUS.get();
                explosionDamage = VehicleConfig.MK42_AP_EXPLOSION_DAMAGE.get();
                fireProbability = 0;
                fireTime = 0;
                durability = 60;
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            CannonShellEntity entityToSpawn = new CannonShellEntity(player, level, hitDamage, explosionRadius, explosionDamage, fireProbability, fireTime)
                    .durability(durability);

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
                        Mth.clamp(count--, 1, 5), 0.15, 0.15, 0.15, 0.0025);
            }

            final Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for (Entity target : level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(20), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {

                if (target instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(15, 15, 45, this.getX(), this.getEyeY(), this.getZ()));
                }
            }
        }
    }

    @Override
    public void travel() {
        Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        float diffY = 0;
        float diffX = 0;

        if (passenger instanceof Player player) {
            if (player.getOffhandItem().getItem() == ModItems.FIRING_PARAMETERS.get()) {
                if (setTarget(player.getOffhandItem())) {
                    diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(entityData.get(YAW) - this.getYRot()));
                    diffX = Mth.wrapDegrees(entityData.get(PITCH) - this.getXRot());
                    this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -1.75f, 1.75f));
                    this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(0.95f * diffX, -5, 5), -85, 15));
                }
            } else {
                diffY = Math.clamp(-90f, 90f, Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot()));
                diffX = passenger.getXRot() - 1.3f - this.getXRot();
                diffX = diffX * 0.15f;
                this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -1.75f, 1.75f));
                this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(diffX, -3f, 3f), -85, 16.3f));
            }
        }
    }

    public boolean setTarget(ItemStack stack) {
        int targetX = stack.getOrCreateTag().getInt("TargetX");
        int targetY = stack.getOrCreateTag().getInt("TargetY");
        int targetZ = stack.getOrCreateTag().getInt("TargetZ");

        this.look(EntityAnchorArgument.Anchor.EYES, new Vec3(targetX, targetY, targetZ));

        double[] angles = new double[2];
        boolean flag = RangeHelper.canReachTarget(15, 0.05, 0.99,
                new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ()),
                new BlockPos(targetX, targetY, targetZ),
                angles);

        if (flag) {
            this.entityData.set(PITCH, -(float) angles[1]);
        }

        return flag;
    }

    private void look(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget) {
        Vec3 vec3 = pAnchor.apply(this);
        double d0 = (pTarget.x - vec3.x) * 0.2;
        double d2 = (pTarget.z - vec3.z) * 0.2;
        this.entityData.set(YAW, Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -85.0F, 16.3F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    private PlayState movementPredicate(AnimationState<Mk42Entity> event) {
        if (this.entityData.get(COOL_DOWN) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.mk42.fire"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.mk42.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public boolean isDriver(Player player) {
        return player == this.getFirstPassenger();
    }

    @Override
    public int mainGunRpm() {
        return 0;
    }

    @Override
    public boolean canShoot(Player player) {
        return true;
    }

    @Override
    public int getAmmoCount(Player player) {
        if (player.getMainHandItem().getItem() instanceof CannonShellItem) {
            return player.getMainHandItem().getCount();
        } else {
            return -1;
        }
    }

    @Override
    public boolean banHand() {
        return true;
    }

    @Override
    public boolean hidePassenger() {
        return true;
    }

    @Override
    public int zoomFov() {
        return 5;
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/sherman_icon.png");
    }
}
