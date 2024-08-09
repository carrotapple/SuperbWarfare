package net.mcreator.superbwarfare.entity;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.headshot.BoundingBoxManager;
import net.mcreator.superbwarfare.headshot.IHeadshotBox;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
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
    private float bypassArmorRate = 0.0f;
    private float undeadMultiple = 1.0f;

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, Level world) {
        super(type, world);
    }

    public BocekArrowEntity(EntityType<? extends BocekArrowEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public BocekArrowEntity(LivingEntity entity, Level level, int monsterMultiplier) {
        super(ModEntities.BOCEK_ARROW.get(), entity, level);
        this.monsterMultiplier = monsterMultiplier;
    }

    public BocekArrowEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.BOCEK_ARROW.get(), world);
    }

    public BocekArrowEntity bypassArmorRate(float bypassArmorRate) {
        this.bypassArmorRate = bypassArmorRate;
        return this;
    }

    public BocekArrowEntity undeadMultiple(float undeadMultiple) {
        this.undeadMultiple = undeadMultiple;
        return this;
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
        BlockPos resultPos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);
        if(state.getBlock() instanceof BellBlock bell) {
            bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        float damageMultiplier = 1 + 0.4f * this.monsterMultiplier;

        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                living.playSound(ModSounds.INDICATION.get());

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
                                living.playSound(ModSounds.HEADSHOT.get());

                                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(1, 5));
                            }
                        }
                    }
                }
            }
        }

        boolean hurt;
        if (entity instanceof Monster) {
            hurt = performHurt(entity, i * damageMultiplier * (entity instanceof LivingEntity living && living.getMobType() == MobType.UNDEAD? this.undeadMultiple : 1), headshot);
        } else {
            hurt = performHurt(entity, i * (entity instanceof LivingEntity living && living.getMobType() == MobType.UNDEAD? this.undeadMultiple : 1), headshot);
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

    private boolean performHurt(Entity entity, float damage, boolean headshot) {
        float normalDamage = damage * Mth.clamp(1 - bypassArmorRate, 0, 1);
        float absoluteDamage = damage * Mth.clamp(bypassArmorRate, 0, 1);

        entity.invulnerableTime = 0;
        if (headshot) {
            entity.hurt(ModDamageTypes.causeArrowInBrainDamage(this.level().registryAccess(), this, this.getOwner()), normalDamage * 2);
            entity.invulnerableTime = 0;
            return entity.hurt(ModDamageTypes.causeArrowInBrainAbsoluteDamage(this.level().registryAccess(), this, this.getOwner()), absoluteDamage * 2);
        } else {
            entity.hurt(ModDamageTypes.causeArrowInKneeDamage(this.level().registryAccess(), this, this.getOwner()), normalDamage);
            entity.invulnerableTime = 0;
            return entity.hurt(ModDamageTypes.causeArrowInKneeAbsoluteDamage(this.level().registryAccess(), this, this.getOwner()), absoluteDamage);
        }
    }

}
