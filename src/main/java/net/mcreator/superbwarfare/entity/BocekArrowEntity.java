package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.headshot.BoundingBoxManager;
import net.mcreator.superbwarfare.headshot.IHeadshotBox;
import net.mcreator.superbwarfare.init.TargetModDamageTypes;
import net.mcreator.superbwarfare.init.TargetModEntities;
import net.mcreator.superbwarfare.init.TargetModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
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

    private int monsterMultiplier = 0;

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, Level world) {
        super(type, world);
    }

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public BocekArrowEntity(LivingEntity entity, Level level, int monsterMultiplier) {
        super(TargetModEntities.BOCEK_ARROW.get(), entity, level);
        this.monsterMultiplier = monsterMultiplier;
    }

    public BocekArrowEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.BOCEK_ARROW.get(), world);
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
        float damageMultiplier = 1 + 0.4f * this.monsterMultiplier;

        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.playSound(TargetModSounds.INDICATION.get());

                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
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

                                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                            }
                        }
                    }
                }
            }
        }

        boolean hurt;
        if (headshot) {
            if (entity instanceof Monster monster) {
                hurt = monster.hurt(TargetModDamageTypes.causeArrowInBrainDamage(this.level().registryAccess(), this, this.getOwner()), (float) i * 2 * damageMultiplier);
            } else {
                hurt = entity.hurt(TargetModDamageTypes.causeArrowInBrainDamage(this.level().registryAccess(), this, this.getOwner()), (float) i * 2);
            }
        } else {
            if (entity instanceof Monster monster) {
                hurt = monster.hurt(TargetModDamageTypes.causeArrowInKneeDamage(this.level().registryAccess(), this, this.getOwner()), (float) i * damageMultiplier);
            } else {
                hurt = entity.hurt(TargetModDamageTypes.causeArrowInKneeDamage(this.level().registryAccess(), this, this.getOwner()), (float) i);
            }
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

}
