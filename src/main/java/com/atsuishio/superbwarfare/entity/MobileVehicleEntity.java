package com.atsuishio.superbwarfare.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

public class MobileVehicleEntity extends Entity {

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;
    public boolean leftInputDown;
    public boolean rightInputDown;
    public boolean forwardInputDown;
    public boolean backInputDown;
    public boolean upInputDown;
    public boolean downInputDown;

    public MobileVehicleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void baseTick() {
        super.baseTick();

        tickLerp();

        this.refreshDimensions();
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
            this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
            this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = (double)pYaw;
        this.lerpXRot = (double)pPitch;
        this.lerpSteps = 10;
    }

    public static double calculateAngle(Vec3 move, Vec3 view) {
        move = move.multiply(1, 0, 1).normalize();
        view = view.multiply(1, 0, 1).normalize();

        double startLength = move.length();
        double endLength = view.length();
        if (startLength > 0.0D && endLength > 0.0D) {
            return Math.toDegrees(Math.acos(Mth.clamp(move.dot(view) / (startLength * endLength), -1, 1)));
        } else {
            return 0.0D;
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
    }
}
