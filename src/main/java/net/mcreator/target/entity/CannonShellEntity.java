package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.*;
import net.mcreator.target.network.message.ClientIndicatorMessage;
import net.mcreator.target.tools.CustomExplosion;
import net.mcreator.target.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

public class CannonShellEntity extends ThrowableItemProjectile {
    private float damage = 0;
    private float explosionRadius = 0;
    private float explosionDamage = 0;
    private float fireProbability = 0;
    private int fireTime = 0;
    private int durability = 40;

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
        this(TargetModEntities.CANNON_SHELL.get(), level);
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
        return TargetModItems.HE_5_INCHES.get();
    }

    @Override
    public void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        entity.hurt(this.level().damageSources().thrown(this, this.getOwner()), this.damage);
        entity.invulnerableTime = 0;

        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
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
        causeSmallExplode();

        if (blockState.is(TargetModBlocks.BARBED_WIRE.get()) || blockState.is(Blocks.NETHERITE_BLOCK)) {
            this.durability -= 10;
        }

        if (blockState.is(Blocks.IRON_BLOCK) || blockState.is(Blocks.COPPER_BLOCK)) {
            this.durability -= 5;
        }

        if (blockState.is(Blocks.GOLD_BLOCK)) {
            this.durability -= 3;
        }

        Vec3 vec = this.getDeltaMovement();
        double vec_x = vec.x;
        double vec_y = vec.y;
        double vec_z = vec.z;

        this.setDeltaMovement(vec_x - 0.02 * vec_x * hardness, vec_y - 0.02 * vec_y * hardness, vec_z - 0.02 * vec_z * hardness);

        if (this.durability <= 0) {
            if (!this.level().isClientSide()) {
                causeExplode();
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(),
                    1, 0, 0, 0, 0.001, true);
        }
        if (this.tickCount > 600 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
            this.discard();
        }
    }

    private void causeExplode() {
        if (Math.random() > fireProbability) {
            fireTime = 0;
        }

        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), explosionDamage,
                this.getX(), this.getY(), this.getZ(), explosionRadius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1).setFireTime(fireTime);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        if (explosionRadius > 7) {
            ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());
        } else {
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }
    }

    private void causeSmallExplode() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), 10,
                this.getX(), this.getY(), this.getZ(), 3, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
