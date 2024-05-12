package net.mcreator.target.entity;

import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.procedures.MedexpProcedure;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
        onHit();
    }

    private void onHit() {
        if (this.level() instanceof ServerLevel server) {
            MedexpProcedure.execute(this.level(), this.getX(), getY(), getZ());
            server.explode(this, (this.getX()), (this.getY()), (this.getZ()), 7.5f, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        onHit();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getServer() != null) {
            // TODO 修改为正确的粒子效果播放方法
            this.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, this.position(), this.getRotationVector(), this.level() instanceof ServerLevel ? (ServerLevel) this.level() : null, 4,
                    this.getName().getString(), this.getDisplayName(), this.level().getServer(), this), "particle minecraft:campfire_cosy_smoke ~ ~ ~ 0 0 0 0 2 force");
        }
        if (this.inGround) this.discard();
    }

    public static MortarShellEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1f, 5, 5);
    }

    public static MortarShellEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        MortarShellEntity entityarrow = new MortarShellEntity(TargetModEntities.MORTAR_SHELL.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityarrow;
    }

    public static MortarShellEntity shoot(LivingEntity entity, LivingEntity target) {
        MortarShellEntity entityarrow = new MortarShellEntity(TargetModEntities.MORTAR_SHELL.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(5);
        entityarrow.setKnockback(5);
        entityarrow.setCritArrow(false);
        entity.level().addFreshEntity(entityarrow);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return entityarrow;
    }
}
