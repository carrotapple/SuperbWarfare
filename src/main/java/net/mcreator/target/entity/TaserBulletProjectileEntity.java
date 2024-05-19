package net.mcreator.target.entity;

import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import java.util.Optional;

// TODO 父类改为Projectile
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TaserBulletProjectileEntity extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);

    public TaserBulletProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.TASER_BULLET_PROJECTILE.get(), world);
    }

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, Level world) {
        super(type, world);
    }

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public TaserBulletProjectileEntity(EntityType<? extends TaserBulletProjectileEntity> type, LivingEntity entity, Level world) {
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

    public static TaserBulletProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        TaserBulletProjectileEntity taserBullet = new TaserBulletProjectileEntity(TargetModEntities.TASER_BULLET_PROJECTILE.get(), entity, world);
        taserBullet.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        taserBullet.setSilent(true);
        taserBullet.setCritArrow(false);
        taserBullet.setBaseDamage(damage);
        taserBullet.setKnockback(knockback);
        world.addFreshEntity(taserBullet);
        return taserBullet;
    }

    public static TaserBulletProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
        TaserBulletProjectileEntity taserBullet = new TaserBulletProjectileEntity(TargetModEntities.TASER_BULLET_PROJECTILE.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        taserBullet.shoot(dx, dy - taserBullet.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        taserBullet.setSilent(true);
        taserBullet.setBaseDamage(5);
        taserBullet.setKnockback(5);
        taserBullet.setCritArrow(false);
        entity.level().addFreshEntity(taserBullet);
        return taserBullet;
    }

    public static TaserBulletProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1f, 5, 5);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.hitIndicator = 25;
                capability.syncPlayerVariables(living);
            });

            if (!living.level().isClientSide()) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);
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
                        setBaseDamage(getBaseDamage() * 1.5f);
                        living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.headIndicator = 25;
                            capability.syncPlayerVariables(living);
                        });
                        if (!living.level().isClientSide()) {
                            living.level().playSound(null, living.blockPosition(), TargetModSounds.HEADSHOT.get(), SoundSource.VOICE, 1, 1);
                        }
                    }
                }
            }
        }
        super.onHitEntity(result);

        if (this.getOwner() instanceof LivingEntity source) {
            CompoundTag tag = source.getMainHandItem().getOrCreateTag();
            tag.putInt("hit_count", tag.getInt("hit_count") + 1);
        }

        if (entity instanceof Player player && !player.isCreative()) {
            if (!player.level().isClientSide())
                player.addEffect(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, 0));
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        this.getPersistentData().putInt("live", this.getPersistentData().getInt("live") + 1);
        if (this.getPersistentData().getInt("live") == 5) {
            this.setDeltaMovement(new Vec3(0, 0, 0));
        }

        if (this.tickCount > 200) {
            this.discard();
        }
    }
}
