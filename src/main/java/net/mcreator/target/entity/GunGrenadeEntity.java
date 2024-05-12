package net.mcreator.target.entity;

import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.procedures.MedexpProcedure;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import java.util.Optional;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class GunGrenadeEntity extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);

    public GunGrenadeEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.GUN_GRENADE.get(), world);
    }

    public GunGrenadeEntity(EntityType<? extends GunGrenadeEntity> type, Level world) {
        super(type, world);
    }

    public GunGrenadeEntity(EntityType<? extends GunGrenadeEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public GunGrenadeEntity(EntityType<? extends GunGrenadeEntity> type, LivingEntity entity, Level world) {
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
    protected void onHitEntity(EntityHitResult result) {
        final Vec3 position = this.position();
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            double _setval = 25;
            living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.hitIndicator = _setval;
                capability.syncPlayerVariables(living);
            });
            if (!living.level().isClientSide() && living.getServer() != null) {
                living.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, living.position(), living.getRotationVector(), living.level() instanceof ServerLevel ? (ServerLevel) living.level() : null, 4,
                        living.getName().getString(), living.getDisplayName(), living.level().getServer(), living), "playsound target:indication voice @a ~ ~ ~ 1 1");
            }
        }

        if (this.getPersistentData().getDouble("baoxian") > 0) {
            if (this.level() instanceof ServerLevel level) {
                level.explode(this, (this.getX()), (this.getY()), (this.getZ()), 5.5f, Level.ExplosionInteraction.NONE);
                MedexpProcedure.execute(this.level(), (this.getX()), (this.getY()), (this.getZ()));
                this.discard();
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
                    if (!headshotHitPos.isPresent()) {
                        box = box.inflate(0.2, 0.2, 0.2);
                        headshotHitPos = box.clip(startVec, endVec);
                    }
                    if (headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55)) {
                        headshot = true;
                    }
                    if (headshot) {
                        if (this.getOwner() instanceof LivingEntity living) {
                            setBaseDamage(getBaseDamage() * 2);
                            double _setval = 25;
                            living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                                capability.headIndicator = _setval;
                                capability.syncPlayerVariables(living);
                            });
                            if (!living.level().isClientSide() && living.getServer() != null) {
                                living.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, living.position(), living.getRotationVector(), living.level() instanceof ServerLevel ? (ServerLevel) living.level() : null, 4,
                                        living.getName().getString(), living.getDisplayName(), living.level().getServer(), living), "playsound target:headshot voice @a ~ ~ ~ 1 1");
                            }
                        }
                    }
                }
            }
        }
        super.onHitEntity(result);
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.getPersistentData().getDouble("baoxian") > 0) {
            if (this.level() instanceof ServerLevel level) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5.5f, Level.ExplosionInteraction.NONE);
                MedexpProcedure.execute(level, this.getX(), this.getY(), this.getZ());
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.getPersistentData().putDouble("baoxian", (this.getPersistentData().getDouble("baoxian") + 1));

        // TODO 修改为正确的粒子效果添加
        if (!this.level().isClientSide() && this.getServer() != null) {
            this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                    this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 1 force");
        }

        if (this.tickCount > 200) {
            this.discard();
        }
    }

    public static GunGrenadeEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1f, 5, 5);
    }

    public static GunGrenadeEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        GunGrenadeEntity entityarrow = new GunGrenadeEntity(TargetModEntities.GUN_GRENADE.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        return entityarrow;
    }

    public static GunGrenadeEntity shoot(LivingEntity entity, LivingEntity target) {
        GunGrenadeEntity entityarrow = new GunGrenadeEntity(TargetModEntities.GUN_GRENADE.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(5);
        entityarrow.setKnockback(5);
        entityarrow.setCritArrow(false);
        entity.level().addFreshEntity(entityarrow);
        return entityarrow;
    }
}
