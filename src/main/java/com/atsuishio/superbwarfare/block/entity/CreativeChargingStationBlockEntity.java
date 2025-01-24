package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.entity.vehicle.IChargeEntity;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

/**
 * Energy Data Slot Code based on @GoryMoon's Chargers
 */
public class CreativeChargingStationBlockEntity extends BlockEntity {

    public static final int CHARGE_RADIUS = 8;

    private LazyOptional<EnergyStorage> energyHandler;

    public CreativeChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_CHARGING_STATION.get(), pos, state);
        this.energyHandler = LazyOptional.of(() -> new EnergyStorage(2147483647));
    }

    public static void serverTick(CreativeChargingStationBlockEntity blockEntity) {
        if (blockEntity.level == null) return;

        blockEntity.energyHandler.ifPresent(handler -> {
            blockEntity.chargeEntity();
            blockEntity.chargeBlock();
        });
    }

    private void chargeEntity() {
        if (this.level == null) return;

        this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(CHARGE_RADIUS))
                .forEach(entity -> {
                    if (entity instanceof IChargeEntity chargeEntity && chargeEntity.canCharge()) {
                        chargeEntity.charge(10000000);
                    }
                });
    }

    private void chargeBlock() {
        if (this.level == null) return;

        for (Direction direction : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity == null
                    || !blockEntity.getCapability(ForgeCapabilities.ENERGY).isPresent()
                    || blockEntity instanceof CreativeChargingStationBlockEntity
            ) continue;

            blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                if (energy.canReceive() && energy.getEnergyStored() < energy.getMaxEnergyStored()) {
                    energy.receiveEnergy(10000000, false);
                    blockEntity.setChanged();
                }
            });
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.energyHandler = LazyOptional.of(() -> new EnergyStorage(2147483647));
    }
}
