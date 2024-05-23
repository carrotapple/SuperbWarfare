package net.mcreator.target.entity;

import net.mcreator.target.TargetMod;
import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.message.ClientIndicatorMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

import java.util.Optional;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class BocekArrowEntity extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Items.ARROW);

    public BocekArrowEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.BOCEK_ARROW.get(), world);
    }

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, Level world) {
        super(type, world);
    }

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
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
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.playSound(TargetModSounds.INDICATION.get());

                TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
            }
        }

        float f = (float) this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double) f * this.getBaseDamage(), 0.0D, Integer.MAX_VALUE));

        if (this.isCritArrow()) {
            long j = this.random.nextInt(i / 2 + 2);
            i = (int) Math.min(j + (long) i, 2147483647L);
        }

        boolean headshot = false;

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.invulnerableTime = 0;

            AABB boundingBox = entity.getBoundingBox();
            Vec3 startVec = this.position();
            Vec3 endVec = startVec.add(this.getDeltaMovement());
            Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);

            /* Check for headshot */
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
                                living.playSound(TargetModSounds.HEADSHOT.get());

                                TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                            }
                        }
                    }
                }
            }
        }

        boolean hurt;
        if (headshot) {
            hurt = entity.hurt(TargetModDamageTypes.causeArrowInBrainDamage(this.level().registryAccess(), this.getOwner()), (float) i * 2);
        } else {
            hurt = entity.hurt(TargetModDamageTypes.causeArrowInKneeDamage(this.level().registryAccess(), this.getOwner()), (float) i);
        }

        if (!hurt) {
            int k = entity.getRemainingFireTicks();
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            this.discard();
        }
    }

    public static BocekArrowEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1f, 5, 0);
    }

    public static BocekArrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        BocekArrowEntity bocekArrowEntity = new BocekArrowEntity(TargetModEntities.BOCEK_ARROW.get(), entity, world);
        bocekArrowEntity.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        bocekArrowEntity.setSilent(true);
        bocekArrowEntity.setCritArrow(false);
        bocekArrowEntity.setBaseDamage(damage);
        bocekArrowEntity.setKnockback(knockback);
        world.addFreshEntity(bocekArrowEntity);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return bocekArrowEntity;
    }

    public static BocekArrowEntity shoot(LivingEntity entity, LivingEntity target) {
        BocekArrowEntity bocekArrowEntity = new BocekArrowEntity(TargetModEntities.BOCEK_ARROW.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        bocekArrowEntity.shoot(dx, dy - bocekArrowEntity.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        bocekArrowEntity.setSilent(true);
        bocekArrowEntity.setBaseDamage(5);
        bocekArrowEntity.setKnockback(5);
        bocekArrowEntity.setCritArrow(false);
        entity.level().addFreshEntity(bocekArrowEntity);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return bocekArrowEntity;
    }
}
