package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CannonShellEntity extends ThrowableItemProjectile implements GeoEntity, AnimatedEntity{

    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(CannonShellEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public String animationprocedure = "empty";
    private float damage = 0;
    private float explosionRadius = 0;
    private float explosionDamage = 0;
    private float fireProbability = 0;
    private int fireTime = 0;
    private int durability = 40;
    private boolean firstHit = true;

    public CannonShellEntity(EntityType<? extends CannonShellEntity> type, Level world) {
        super(type, world);
    }

    public CannonShellEntity(EntityType<? extends CannonShellEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public CannonShellEntity(EntityType<? extends CannonShellEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public CannonShellEntity(EntityType<? extends CannonShellEntity> type, LivingEntity entity, Level world, float damage, float explosionRadius, float explosionDamage, float fireProbability, int fireTime) {
        super(type, entity, world);
        this.damage = damage;
        this.explosionRadius = explosionRadius;
        this.explosionDamage = explosionDamage;
        this.fireProbability = fireProbability;
        this.fireTime = fireTime;
    }

    public CannonShellEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.CANNON_SHELL.get(), level);
    }

    public CannonShellEntity durability(int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HE_5_INCHES.get();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    public void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        entity.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
        entity.invulnerableTime = 0;

        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        if (this.level() instanceof ServerLevel) {
            causeExplode();
        }

        Vec3 vec = this.getDeltaMovement();
        double vec_x = vec.x;
        double vec_y = vec.y;
        double vec_z = vec.z;

        this.setDeltaMovement(vec_x - 0.02 * vec_x, vec_y - 0.02 * vec_y, vec_z - 0.02 * vec_z);

        this.durability -= 2;
        if (this.durability <= 0) {
            if (!this.level().isClientSide()) {
                causeExplode();
            }
            this.discard();
        }
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        int x = blockHitResult.getBlockPos().getX();
        int y = blockHitResult.getBlockPos().getY();
        int z = blockHitResult.getBlockPos().getZ();

        if (this.firstHit) {
            ParticleTool.cannonHitParticles(this.level(), this.position());
            this.firstHit = false;
        }

        BlockState blockState = this.level().getBlockState(BlockPos.containing(x, y, z));
        if (blockState.is(Blocks.BEDROCK) || blockState.is(Blocks.BARRIER)) {
            if (!this.level().isClientSide()) {
                causeExplode();
            }
            this.discard();
            return;
        }

        float hardness = this.level().getBlockState(BlockPos.containing(x, y, z)).getBlock().defaultDestroyTime();
        this.durability -= (int) hardness;

        Vec3 vec = this.getDeltaMovement();
        this.setDeltaMovement(vec.multiply(0.9, 0.9, 0.9));

        if (blockState.is(ModBlocks.BARBED_WIRE.get()) || blockState.is(Blocks.NETHERITE_BLOCK)) {
            this.durability -= 10;
        }

        if (blockState.is(Blocks.IRON_BLOCK) || blockState.is(Blocks.COPPER_BLOCK)) {
            this.durability -= 5;
        }

        if (blockState.is(Blocks.GOLD_BLOCK)) {
            this.durability -= 3;
        }

        if (this.durability <= 0) {
            if (!this.level().isClientSide()) {
                causeExplode();
            }
        }

        if (this.durability > 0) {
            ModUtils.queueServerWork(1, () -> {
                this.setDeltaMovement(vec.multiply(0.1, 0.1, 0.1));
                if (!this.level().isClientSide()) {
                    causeExplode();
                }
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.xo, this.yo, this.zo,
                    1, 0, 0, 0, 0.001, true);
        }
        if (this.tickCount > 600 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
        }
    }

    private void causeExplode() {
        if (Math.random() > fireProbability) {
            fireTime = 0;
        }

        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), explosionDamage,
                this.getX(), this.getY(), this.getZ(), explosionRadius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1).setFireTime(fireTime);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        if (explosionRadius > 7) {
            ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());
        } else {
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }
        this.discard();
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cannon_shell.idle"));
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
    protected float getGravity() {
        return 0.05F;
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void setAnimationProcedure(String procedure) {
        this.animationprocedure = procedure;
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
