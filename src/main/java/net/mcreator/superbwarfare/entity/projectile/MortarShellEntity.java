package net.mcreator.superbwarfare.entity.projectile;

import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModEntities;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.ProjectileTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class MortarShellEntity extends ThrowableItemProjectile {

    private float damage = 250f;
    private int life = 600;
    private float radius = 12.5f;

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, Level world) {
        super(type, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, LivingEntity entity, Level world, float damage) {
        super(type, entity, world);
        this.damage = damage;
    }

    public MortarShellEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.MORTAR_SHELL.get(), level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
        pCompound.putInt("Life", this.life);
        pCompound.putFloat("Radius", this.radius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Damage")) {
            this.damage = pCompound.getFloat("Damage");
        } else {
            this.damage = 250f;
        }

        if (pCompound.contains("Life")) {
            this.life = pCompound.getInt("Life");
        } else {
            this.life = 600;
        }

        if (pCompound.contains("Radius")) {
            this.radius = pCompound.getFloat("Radius");
        } else {
            this.radius = 12.5f;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MORTAR_SHELLS.get();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    public void onHitEntity(EntityHitResult entityHitResult) {
        if (this.tickCount > 1) {
            Entity entity = entityHitResult.getEntity();
            entity.hurt(ModDamageTypes.causeCannonFireDamage(this.level().registryAccess(), this, this.getOwner()), this.damage);
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this, this.damage, this.radius);
            }
            this.discard();
        }
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos resultPos = blockHitResult.getBlockPos();
        BlockState state = this.level().getBlockState(resultPos);
        if (state.getBlock() instanceof BellBlock bell) {
            bell.attemptToRing(this.level(), resultPos, blockHitResult.getDirection());
        }
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel) {
            ProjectileTool.causeCustomExplode(this, this.damage, this.radius);
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo,
                    1, 0, 0, 0, 0.001, true);
        }
        if (this.tickCount > this.life || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this, this.damage, this.radius);
            }
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
