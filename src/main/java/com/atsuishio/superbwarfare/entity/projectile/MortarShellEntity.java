package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.ProjectileTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashSet;
import java.util.Set;

public class MortarShellEntity extends ThrowableItemProjectile implements GeoEntity {

    private float damage = ExplosionConfig.MORTAR_SHELL_EXPLOSION_DAMAGE.get();
    private int life = 600;
    private float radius = ExplosionConfig.MORTAR_SHELL_EXPLOSION_RADIUS.get();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Set<Long> loadedChunks = new HashSet<>();

    public MortarShellEntity(EntityType<? extends MortarShellEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
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

        ListTag listTag = new ListTag();
        for (long chunkPos : this.loadedChunks) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Pos", chunkPos);
            listTag.add(tag);
        }
        pCompound.put("Chunks", listTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Damage")) {
            this.damage = pCompound.getFloat("Damage");
        } else {
            this.damage = ExplosionConfig.MORTAR_SHELL_EXPLOSION_DAMAGE.get();
        }

        if (pCompound.contains("Life")) {
            this.life = pCompound.getInt("Life");
        } else {
            this.life = 600;
        }

        if (pCompound.contains("Radius")) {
            this.radius = pCompound.getFloat("Radius");
        } else {
            this.radius = ExplosionConfig.MORTAR_SHELL_EXPLOSION_RADIUS.get();
        }

        if (pCompound.contains("Chunks")) {
            ListTag listTag = pCompound.getList("Chunks", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                this.loadedChunks.add(tag.getLong("Pos"));
            }
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
            if (this.tickCount > 1) {
                ProjectileTool.causeCustomExplode(this, this.damage, this.radius);
            }
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.xo, this.yo, this.zo,
                    1, 0, 0, 0, 0.001, true);

            var movement = this.getDeltaMovement();
            var currentX = this.chunkPosition().x;
            var currentZ = this.chunkPosition().z;
            var nextX = movement.x > 0 ? currentX + 1 : currentX - 1;
            var nextZ = movement.z > 0 ? currentZ + 1 : currentZ - 1;

            ForgeChunkManager.forceChunk(serverLevel, ModUtils.MODID, this, currentX, currentZ, true, false);
            ForgeChunkManager.forceChunk(serverLevel, ModUtils.MODID, this, currentX, nextZ, true, false);
            ForgeChunkManager.forceChunk(serverLevel, ModUtils.MODID, this, nextX, currentZ, true, false);
            ForgeChunkManager.forceChunk(serverLevel, ModUtils.MODID, this, nextX, nextZ, true, false);

            this.loadedChunks.add(ChunkPos.asLong(currentX, currentZ));
            this.loadedChunks.add(ChunkPos.asLong(currentX, nextZ));
            this.loadedChunks.add(ChunkPos.asLong(nextX, currentZ));
            this.loadedChunks.add(ChunkPos.asLong(nextX, nextZ));
        }
        if (this.tickCount > this.life || this.isInWater()) {
            if (this.level() instanceof ServerLevel) {
                ProjectileTool.causeCustomExplode(this, this.damage, this.radius);
            }
            this.discard();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    public void onRemovedFromWorld() {
        if (this.level() instanceof ServerLevel serverLevel) {
            for (long chunkPos : this.loadedChunks) {
                ForgeChunkManager.forceChunk(serverLevel, ModUtils.MODID, this, ChunkPos.getX(chunkPos), ChunkPos.getZ(chunkPos), false, false);
            }
        }
        super.onRemovedFromWorld();
    }
}
