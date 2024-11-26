package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TaserBulletProjectileEntity extends AbstractArrow implements ItemSupplier {

    private float damage = 1f;
    private int volt = 0;
    private int wireLength = 0;
    private boolean stop = false;
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(ModItems.TASER_ELECTRODE.get());

    public TaserBulletProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.TASER_BULLET_PROJECTILE.get(), world);
    }

    public TaserBulletProjectileEntity(LivingEntity entity, Level level, float damage, int volt, int wireLength) {
        super(ModEntities.TASER_BULLET_PROJECTILE.get(), entity, level);
        this.damage = damage;
        this.volt = volt;
        this.wireLength = wireLength;
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
                living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }
        if (entity instanceof LivingEntity living) {
            entity.invulnerableTime = 0;
            entity.hurt(ModDamageTypes.causeShockDamage(this.level().registryAccess(), this.getOwner()), this.damage);
            if (living instanceof Player player && player.isCreative()) {
                return;
            }
            if (!living.level().isClientSide()) {
                if (living instanceof Creeper creeper && living.level() instanceof ServerLevel serverLevel) {
                    creeper.thunderHit(serverLevel, new LightningBolt(EntityType.LIGHTNING_BOLT, serverLevel));
                } else {
                    living.addEffect(new MobEffectInstance(ModMobEffects.SHOCK.get(), 100 + volt * 30, volt), this.getOwner());
                }
            }
        }
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos resultPos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);
        if (state.getBlock() instanceof BellBlock bell) {
            bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() != null && this.position().distanceTo(this.getOwner().position()) > 10 + 4 * wireLength && !stop) {
            stop = true;
            this.setDeltaMovement(new Vec3(0, 0, 0));
        }

        if (this.tickCount > 200) {
            this.discard();
        }
    }
}

