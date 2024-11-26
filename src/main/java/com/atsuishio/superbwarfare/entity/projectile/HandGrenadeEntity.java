package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.network.message.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.ProjectileTool;
import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

public class HandGrenadeEntity extends ThrowableItemProjectile {
    private int fuse = 100;

    public HandGrenadeEntity(EntityType<? extends HandGrenadeEntity> type, Level world) {
        super(type, world);
    }

    public HandGrenadeEntity(LivingEntity entity, Level level, int fuse) {
        super(ModEntities.HAND_GRENADE_ENTITY.get(), entity, level);
        this.fuse = fuse;
    }

    public HandGrenadeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.HAND_GRENADE_ENTITY.get(), level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HAND_GRENADE.get();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    protected void onHit(HitResult result) {
        switch (result.getType()) {
            case BLOCK:
                BlockHitResult blockResult = (BlockHitResult) result;
                BlockPos resultPos = blockResult.getBlockPos();
                BlockState state = this.level().getBlockState(resultPos);
                SoundEvent event = state.getBlock().getSoundType(state, this.level(), resultPos, this).getBreakSound();
                double speed = this.getDeltaMovement().length();
                if (speed > 0.1) {
                    this.level().playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
                }
                this.bounce(blockResult.getDirection());

                if (state.getBlock() instanceof BellBlock bell) {
                    bell.attemptToRing(this.level(), resultPos, blockResult.getDirection());
                }

                break;
            case ENTITY:
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity entity = entityResult.getEntity();
                double speed_e = this.getDeltaMovement().length();
                if (speed_e > 0.1) {
                    if (this.getOwner() instanceof LivingEntity living) {
                        if (!living.level().isClientSide() && living instanceof ServerPlayer player) {
                            living.level().playSound(null, living.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);

                            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
                        }
                    }
                    entity.hurt(entity.damageSources().thrown(this, this.getOwner()), 1.0F);
                }
                this.bounce(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.25, 1.0, 0.25));
                break;
            default:
                break;
        }
    }

    private void bounce(Direction direction) {
        switch (direction.getAxis()) {
            case X:
                this.setDeltaMovement(this.getDeltaMovement().multiply(-0.5, 0.75, 0.75));
                break;
            case Y:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, -0.25, 0.75));
                if (this.getDeltaMovement().y() < this.getGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
                }
                break;
            case Z:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, 0.75, -0.5));
                break;
        }
    }

    @Override
    public void tick() {
        super.tick();
        --this.fuse;

        if (this.fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                ProjectileTool.causeCustomExplode(this, 125f, 5f, 1.2f);
            }
        }

        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.SMOKE, this.xo, this.yo, this.zo,
                    1, 0, 0, 0, 0.01, true);
        }
    }
    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * 0.017453292F) * Mth.cos(pX * 0.017453292F);
        float f1 = -Mth.sin((pX + pZ) * 0.017453292F);
        float f2 = Mth.cos(pY * 0.017453292F) * Mth.cos(pX * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0 : -vec3.y, vec3.z));
    }

    @Override
    protected float getGravity() {
        return 0.07F;
    }
}
