
package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.mcreator.superbwarfare.init.TargetModEntities;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TaserBulletProjectileEntity extends AbstractArrow implements ItemSupplier {
    private float damage = 1f;
    private int volt = 0;
    private int wire_length = 0;
    private boolean stop = false;
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);

    public TaserBulletProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.TASER_BULLET_PROJECTILE.get(), world);
    }

    public TaserBulletProjectileEntity(LivingEntity entity, Level level, float damage, int volt, int wire_length) {
        super(TargetModEntities.TASER_BULLET_PROJECTILE.get(), entity, level);
        this.damage = damage;
        this.volt = volt;
        this.wire_length = wire_length;
    }

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return PROJECTILE_ITEM;
    }

    @Override
    protected ItemStack getPickupItem() {
        return PROJECTILE_ITEM;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }
        if (entity instanceof LivingEntity living) {
            entity.invulnerableTime = 0;
            entity.hurt(TargetModDamageTypes.causeShockDamage(this.level().registryAccess(), this.getOwner()), this.damage);
            if (living instanceof Player player && player.isCreative()) {
                return;
            }
            if (!living.level().isClientSide()) {
                living.addEffect(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, volt), this.getOwner());
            }
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() != null && this.position().distanceTo(this.getOwner().position()) > 10 + 4 * wire_length && !stop) {
            stop = true;
            this.setDeltaMovement(new Vec3(0, 0, 0));
        }

        if (this.tickCount > 200) {
            this.discard();
        }
    }
}

