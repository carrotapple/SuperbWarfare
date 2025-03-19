package com.atsuishio.superbwarfare.entity.projectile;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.network.message.ClientMotionSyncMessage;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public abstract class FastThrowableProjectile extends ThrowableItemProjectile implements CustomSyncMotionEntity {

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public FastThrowableProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        this.syncMotion();
    }

    @Override
    public void syncMotion() {
        if (this.level().isClientSide) return;

        var motion = this.getDeltaMovement();
        double length = motion.length();
        int tickNeeded = 5;
        if (length > 0) {
            tickNeeded = (int) Mth.clamp(10D / length, 1, 5);
        }
        if (this.tickCount % tickNeeded == 0) {
            ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ClientMotionSyncMessage(this));
        }
    }
}
