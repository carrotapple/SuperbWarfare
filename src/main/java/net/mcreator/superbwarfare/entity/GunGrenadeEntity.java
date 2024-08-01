package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.headshot.BoundingBoxManager;
import net.mcreator.superbwarfare.headshot.IHeadshotBox;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.tools.CustomExplosion;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

import java.util.Optional;

public class GunGrenadeEntity extends ThrowableItemProjectile {

    private int monsterMultiplier = 0;
    private float damage = 5f;

    public GunGrenadeEntity(EntityType<? extends GunGrenadeEntity> type, Level world) {
        super(type, world);
    }

    public GunGrenadeEntity(EntityType<? extends GunGrenadeEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public GunGrenadeEntity(LivingEntity entity, Level level, float damage, int monsterMultiplier) {
        super(ModEntities.GUN_GRENADE.get(), entity, level);
        this.damage = damage;
        this.monsterMultiplier = monsterMultiplier;
    }

    public GunGrenadeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.GUN_GRENADE.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GRENADE_40MM.get();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        float damageMultiplier = 1 + 0.2f * this.monsterMultiplier;
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        AABB boundingBox = entity.getBoundingBox();
        Vec3 startVec = this.position();
        Vec3 endVec = startVec.add(this.getDeltaMovement());
        Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);
        /* Check for headshot */
        boolean headshot = false;
        if (entity instanceof LivingEntity) {
            IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
            if (headshotBox != null) {
                AABB box = headshotBox.getHeadshotBox((LivingEntity) entity);
                if (box != null) {
                    box = box.move(boundingBox.getCenter().x, boundingBox.minY, boundingBox.getCenter().z);
                    Optional<Vec3> headshotHitPos = box.clip(startVec, endVec);
                    if (headshotHitPos.isEmpty()) {
                        box = box.inflate(0.2, 0.2, 0.2);
                        headshotHitPos = box.clip(startVec, endVec);
                    }
                    if (headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55)) {
                        headshot = true;
                    }
                    if (headshot) {
                        if (this.getOwner() instanceof LivingEntity living) {
                            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                                living.level().playSound(null, living.getX(), living.getY(), living.getZ(), ModSounds.HEADSHOT.get(), SoundSource.VOICE, 1f, 1f);

                                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                            }
                        }
                    }
                }
            }
        }

        if (headshot) {
            if (entity instanceof Monster monster) {
                monster.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * 2f * damageMultiplier);
            } else {
                entity.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * 2f);
            }
        } else {
            if (entity instanceof Monster monster) {
                monster.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * damageMultiplier);
            } else {
                entity.hurt(ModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
            }
        }

        if (this.tickCount > 0) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
        }

        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.tickCount > 0) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.xo, this.yo, this.zo,
                    1, 0, 0, 0, 0.02, true);
        }

        if (this.tickCount > 200 || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                causeExplode();
            }
            this.discard();
        }
    }

    private void causeExplode() {
        CustomExplosion explosion = new CustomExplosion(this.level(), this,
                ModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()), 1.8f * this.damage,
                this.getX(), this.getY(), this.getZ(), 7.5f, Explosion.BlockInteraction.KEEP).setDamageMultiplier(this.monsterMultiplier);
        explosion.explode();
        net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.level(), explosion);
        explosion.finalizeExplosion(false);

        ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
        for (int index0 = 0; index0 < 100; index0++) {
            fragShoot();
        }
    }

    public void fragShoot() {
        if (!this.level().isClientSide()) {
            FragEntity frag = new FragEntity((LivingEntity) this.getOwner(), level()).setPosition0(this.position());

            frag.setPos(this.getX(), this.getEyeY() + 0.1, this.getZ());
            frag.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 5,
                    360);
            this.level().addFreshEntity(frag);
        }
    }
}
