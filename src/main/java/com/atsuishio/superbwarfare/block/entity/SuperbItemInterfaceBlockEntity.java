package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.block.SuperbItemInterfaceBlock;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.menu.SuperbItemInterfaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuperbItemInterfaceBlockEntity extends BaseContainerBlockEntity {

    public static final int TRANSFER_COOLDOWN = 20;
    public static final int CONTAINER_SIZE = 5;
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int cooldownTime = -1;

    public SuperbItemInterfaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SUPERB_ITEM_INTERFACE.get(), pPos, pBlockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SuperbItemInterfaceBlockEntity blockEntity) {
        --blockEntity.cooldownTime;
        if (blockEntity.isOnCooldown()) return;
        blockEntity.setCooldown(TRANSFER_COOLDOWN);

        if (blockEntity.isEmpty()) return;

        var facing = state.getValue(SuperbItemInterfaceBlock.FACING);

        // find entities
        var x = pos.getX() + facing.getStepX();
        var y = pos.getY() + facing.getStepY();
        var z = pos.getZ() + facing.getStepZ();

        var list = level.getEntities(
                (Entity) null,
                new AABB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5),
                entity -> entity.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()
        );
        if (list.isEmpty()) return;
        var target = list.get(level.random.nextInt(list.size()));

        // item transfer

        var index = -1;
        for (int i = 0; i < blockEntity.items.size(); i++) {
            var stack = blockEntity.items.get(i);
            if (!stack.isEmpty()) {
                index = i;
                break;
            }
        }
        if (index == -1) return;

        var stack = blockEntity.items.get(index);
        var itemHandler = target.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (stack.isEmpty()) break;

            stack = itemHandler.insertItem(i, stack, false);
        }

        blockEntity.items.set(index, stack);
        blockEntity.setChanged();
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.cooldownTime = pTag.getInt("TransferCooldown");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items);
        pTag.putInt("TransferCooldown", this.cooldownTime);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.superbwarfare.superb_item_interface");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory) {
        return new SuperbItemInterfaceMenu(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return this.items.get(pSlot);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.items, pSlot, pAmount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        this.items.set(pSlot, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public void setCooldown(int pCooldownTime) {
        this.cooldownTime = pCooldownTime;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> new InvWrapper(this)));
    }
}
