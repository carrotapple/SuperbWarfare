package com.atsuishio.superbwarfare.capability;

import com.atsuishio.superbwarfare.entity.projectile.AbstractLaserEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class LaserHandler {

    public final int coolingTick;
    public boolean isUsing;
    public final LivingEntity entity;
    public final AbstractLaserEntity laserEntity;
    private int tick;

    public LaserHandler(LivingEntity entity, AbstractLaserEntity laserEntity, int coolingTick) {
        this.coolingTick = coolingTick;
        this.entity = entity;
        this.laserEntity = laserEntity;
    }

    public void start() {
        this.tick = 0;
        this.isUsing = true;

        if (this.entity.level() instanceof ServerLevel level) {
            level.addFreshEntity(this.laserEntity);
        }
        this.entity.swing(InteractionHand.MAIN_HAND, true);
    }

    public void tick() {
        if (this.isUsing) {
            this.tick++;

            if (this.tick > laserEntity.getDuration()) {
                this.stop();
            }
        }
    }

    public void stop() {
        if (!this.isUsing) return;

        this.isUsing = false;
        this.tick = 0;
        if (this.laserEntity != null) {
            this.laserEntity.discard();
        }
    }

    public int getTick() {
        return this.tick;
    }

    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        if (this.isUsing) {
            compoundTag.putInt("Tick", this.tick);
        }
        return compoundTag;
    }

    public void readNBT(Tag nbt) {
        CompoundTag compoundTag = (CompoundTag) nbt;
        this.isUsing = compoundTag.contains("Tick");
        if (this.isUsing) {
            this.tick = compoundTag.getInt("Tick");
        }
    }

    public boolean isUsable() {
        return !this.isUsing;
    }

}
