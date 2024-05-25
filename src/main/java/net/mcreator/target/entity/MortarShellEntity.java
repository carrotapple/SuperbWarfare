package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.ParticleTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class MortarShellEntity extends ThrowableItemProjectile {
    private float damage = 100f;

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, Level world) {
        super(type, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, LivingEntity entity, Level world, float damage) {
        super(type, entity, world);
        this.damage = damage;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return TargetModItems.MORTAR_SHELLS.get();
    }


    @Override
    public void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        entity.hurt(this.level().damageSources().thrown(this, this.getOwner()), this.damage);

        if (this.level() instanceof ServerLevel level) {
            level.explode(this, (this.getX()), (this.getY()), (this.getZ()), 11, Level.ExplosionInteraction.NONE);
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                ParticleTool.spawnMediumExplosionParticles(level, entity.position());
            }
        }
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel level) {
            level.explode(this, this.getX(), this.getY(), this.getZ(), 11, Level.ExplosionInteraction.NONE);
            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(),
                    2, 0, 0, 0, 0.02, true);
        }
        if (this.tickCount > 600 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 11f, Level.ExplosionInteraction.NONE);
                ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            }
            this.discard();
        }
    }
}
