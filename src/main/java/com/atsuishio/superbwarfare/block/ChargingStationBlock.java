package com.atsuishio.superbwarfare.block;

import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ChargingStationBlock extends BaseEntityBlock {

    public ChargingStationBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChargingStationBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide) {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.CHARGING_STATION.get(), ChargingStationBlockEntity::serverTick);
        }
        return null;
    }

}
