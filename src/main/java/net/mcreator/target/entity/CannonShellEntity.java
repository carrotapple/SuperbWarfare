package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.CustomExplosion;
import net.mcreator.target.tools.ParticleTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class CannonShellEntity extends ThrowableItemProjectile {
    private float damage = 0;
    private float explosionRadius = 0;
    private float explosionDamage = 0;
    private float fireProbability = 0;
    private int fireTime = 0;

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

        if (this.level() instanceof ServerLevel) {
            causeExplode();
        }
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        int x = blockHitResult.getBlockPos().getX();
        int y = blockHitResult.getBlockPos().getY();
        int z = blockHitResult.getBlockPos().getZ();

        float hardness = this.level().getBlockState(BlockPos.containing(x, y, z)).getDestroySpeed(this.level(), BlockPos.containing(x, y, z));


        if (!this.level().isClientSide() && this.level() instanceof ServerLevel) {
            causeExplode();
        }
        this.discard();
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

        if (Math.random() > fireProbability){
            fireTime = 0;
        }

        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), explosionDamage,
                this.getX(), this.getY(), this.getZ(), explosionRadius, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1).setFireTime(fireTime);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnHugeExplosionParticles(this.level(), this.position());
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
