package net.mcreator.target.entity;

import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
        Entity entity = result.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.hitIndicator = 25;
                capability.syncPlayerVariables(living);
            });
            if (!living.level().isClientSide() && living.getServer() != null) {
                living.level().playSound(null, living.blockPosition(), TargetModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);
            }
        }

        if (this.getPersistentData().getInt("fuse") > 0) {

            if (this.level() instanceof ServerLevel level) {
                level.explode(this, (this.getX()), (this.getY()), (this.getZ()), 5.5f, Level.ExplosionInteraction.NONE);
                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.getServer(), entity), "target:mediumexp");
                }
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
                    if (headshotHitPos.isEmpty()) {
                        box = box.inflate(0.2, 0.2, 0.2);
                        headshotHitPos = box.clip(startVec, endVec);
                    }
                    if (headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55)) {
                        headshot = true;
                    }
                    if (headshot) {
                        if (this.getOwner() instanceof LivingEntity living) {
                            setBaseDamage(getBaseDamage() * 2);
                            living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                                capability.headIndicator = 25;
                                capability.syncPlayerVariables(living);
                            });
                            if (!living.level().isClientSide()) {
                                living.level().playSound(null, living.getX(), living.getY(), living.getZ(), TargetModSounds.HEADSHOT.get(), SoundSource.VOICE, 1f, 1f);
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
        if (this.getPersistentData().getInt("fuse") > 0) {
            if (this.level() instanceof ServerLevel) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5.5f, Level.ExplosionInteraction.NONE);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.getPersistentData().putInt("fuse", this.getPersistentData().getInt("fuse") + 1);

        // TODO 修改为正确的粒子效果添加
        if (!this.level().isClientSide() && this.getServer() != null) {
            this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                    this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 1 force");
        }
        if (this.inGround) {
            if (!this.level().isClientSide() && this.getServer() != null) {
                this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                        this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "target:mediumexp");
            }
            this.discard();
        }
        if (this.tickCount > 200) {
            this.discard();
        }
    }
}
