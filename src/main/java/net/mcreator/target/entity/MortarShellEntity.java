package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.tools.ParticleTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class MortarShellEntity extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);

    public MortarShellEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(TargetModEntities.MORTAR_SHELL.get(), world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, Level world) {
        super(type, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, LivingEntity entity, Level world) {
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
    public void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (this.level() instanceof ServerLevel level) {
            level.explode(this, (this.getX()), (this.getY()), (this.getZ()), 10, Level.ExplosionInteraction.NONE);
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                ParticleTool.spawnMediumExplosionParticles(level, entity.position());
            }
        }
        this.discard();
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.level() instanceof ServerLevel level) {
            level.explode(this, this.getX(), this.getY(), this.getZ(), 10, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(),
                    2, 0, 0, 0, 0.02, true);
        }
        if (this.inGround) {
            if (!this.level().isClientSide() && this.getServer() != null) {
                ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
            }
            this.discard();
        }
    }
}
