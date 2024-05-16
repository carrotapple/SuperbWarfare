package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.registries.ForgeRegistries;

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
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "target:mediumexp");
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
        if (this.getServer() != null) {
            // TODO 修改为正确的粒子效果播放方法
            this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                    this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 2 force");
        }
        if (this.inGround) {
            if (!this.level().isClientSide() && this.getServer() != null) {
                this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                        this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "target:mediumexp");
            }
            this.discard();
        }
    }
}
