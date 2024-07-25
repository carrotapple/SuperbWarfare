package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.message.ClientIndicatorMessage;
import net.mcreator.target.tools.CustomExplosion;
import net.mcreator.target.tools.ParticleTool;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

public class DroneGrenadeEntity extends ThrowableItemProjectile {


    public DroneGrenadeEntity(EntityType<? extends DroneGrenadeEntity> type, Level world) {
        super(type, world);
    }

    public DroneGrenadeEntity(EntityType<? extends DroneGrenadeEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public DroneGrenadeEntity(LivingEntity entity, Level level) {
        super(TargetModEntities.DRONE_GRENADE.get(), entity, level);
    }

    public DroneGrenadeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(TargetModEntities.DRONE_GRENADE.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return TargetModItems.GRENADE_40MM.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level() instanceof ServerLevel) {
            causeExplode();
        }
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.level() instanceof ServerLevel) {
            causeExplode();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(),
                    1, 0, 0, 0, 0.01, true);
        }
        if (this.tickCount > 200 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
        }
    }

    private void causeExplode() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), 55,
                this.getX(), this.getY(), this.getZ(), 6.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(1);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);
        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        this.discard();
    }

    @Override
    protected float getGravity() {
        return 0.07F;
    }
}
