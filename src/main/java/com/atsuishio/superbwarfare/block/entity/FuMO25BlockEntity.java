package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

public class FuMO25BlockEntity extends BlockEntity implements MenuProvider {

    public static final int MAX_ENERGY = 1000000;

    // 固定距离，以后有人改动这个需要自行解决GUI渲染问题
    public static final int DEFAULT_RANGE = 96;
    public static final int MAX_RANGE = 128;

    private LazyOptional<EnergyStorage> energyHandler;

    public FuncType type = FuncType.NORMAL;
    public int time = 0;

    public FuMO25BlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FUMO_25.get(), pPos, pBlockState);
        this.energyHandler = LazyOptional.of(() -> new EnergyStorage(MAX_ENERGY));
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, FuMO25BlockEntity blockEntity) {

    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, FuMO25BlockEntity blockEntity) {

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("Energy")) {
            getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> ((EnergyStorage) handler).deserializeNBT(pTag.get("Energy")));
        }
        this.type = FuncType.values()[Mth.clamp(pTag.getInt("Type"), 0, 3)];
        this.time = pTag.getInt("Time");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> pTag.put("Energy", ((EnergyStorage) handler).serializeNBT()));
        pTag.putInt("Type", this.type.ordinal());
        pTag.putInt("Time", this.time);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.superbwarfare.fumo_25");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        if (this.level == null) return null;
        return new FuMO25Menu(pContainerId, pPlayerInventory, ContainerLevelAccess.create(this.level, this.getBlockPos()));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.energyHandler = LazyOptional.of(() -> new EnergyStorage(MAX_ENERGY));
    }

    public enum FuncType {
        NORMAL,
        WIDER,
        GLOW,
        GUIDE
    }
}
