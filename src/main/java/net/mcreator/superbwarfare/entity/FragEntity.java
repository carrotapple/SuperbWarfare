package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.TargetModDamageTypes;
import net.mcreator.superbwarfare.init.TargetModEntities;
import net.mcreator.superbwarfare.init.TargetModItems;
import net.mcreator.superbwarfare.init.TargetModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

public class FragEntity extends ThrowableItemProjectile {
    private Vec3 position0;

    public FragEntity(EntityType<? extends FragEntity> type, Level world) {
        super(type, world);
    }

    public FragEntity(EntityType<? extends FragEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public FragEntity(LivingEntity entity, Level level) {
        super(TargetModEntities.FRAG.get(), entity, level);
    }

    public FragEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(TargetModEntities.FRAG.get(), level);
    }

    public FragEntity setPosition0(Vec3 position0) {
        this.position0 = position0;
        return this;
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
        Entity entity = result.getEntity();

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));

                entity.hurt(TargetModDamageTypes.causeProjectileBoomDamage(this.level().registryAccess(), this, this.getOwner()),
                        5 - (float) Mth.clamp(0.1 * this.position0.distanceTo(entity.position())
                                * (entity instanceof LivingEntity livingEntity ? livingEntity.getMaxHealth() / 100 + 1 : 1), 0, 4.5));
            }
        }

        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 4 || this.isInWater()) {
            this.discard();
        }
    }
}
