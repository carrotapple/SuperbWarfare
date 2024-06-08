package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.*;
import net.mcreator.target.network.message.ClientIndicatorMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

import java.util.Optional;

public class TaserBulletProjectileEntity extends ThrowableItemProjectile {
    private float damage = 5f;

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, Level world) {
        super(type, world);
    }

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public TaserBulletProjectileEntity(LivingEntity entity, Level level, float damage) {
        super(TargetModEntities.TASER_BULLET_PROJECTILE.get(), entity, level);
        this.damage = damage;
    }

    public TaserBulletProjectileEntity(PlayMessages.SpawnEntity packet, Level level) {
        this(TargetModEntities.TASER_BULLET_PROJECTILE.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return TargetModItems.TASER_ELECTRODE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
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
                    if (headshot && this.getOwner() instanceof LivingEntity living) {
                        if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                            living.level().playSound(null, living.blockPosition(), TargetModSounds.HEADSHOT.get(), SoundSource.VOICE, 1, 1);

                            TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                        }
                    }
                }
            }
        }

        if (entity instanceof LivingEntity living) {
            if (living instanceof Player player && player.isCreative()){
                return;
            }
            if (!living.level().isClientSide()) {
                living.addEffect(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, 0));
            }
        }

        if (headshot) {
            entity.hurt(TargetModDamageTypes.causeGunFireHeadshotDamage(this.level().registryAccess(), this, this.getOwner()), this.damage * 1.5f);
        } else {
            entity.hurt(TargetModDamageTypes.causeGunFireDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount == 5) {
            this.setDeltaMovement(new Vec3(0, 0, 0));
        }

        if (this.tickCount > 200) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!level().isClientSide) {
        this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
        this.setNoGravity(true);
        }
    }
}
