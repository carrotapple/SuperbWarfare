package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModMenuTypes;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData;
import com.atsuishio.superbwarfare.network.dataslot.SimpleEnergyData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class FuMO25Menu extends EnergyMenu {

    protected final Container container;
    protected final ContainerLevelAccess access;
    protected final ContainerEnergyData containerData;

    private int posX = Integer.MIN_VALUE;
    private int posY = Integer.MIN_VALUE;
    private int posZ = Integer.MIN_VALUE;

    public static final int X_OFFSET = 164;
    public static final int Y_OFFSET = 0;

    public FuMO25Menu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(1), ContainerLevelAccess.NULL, new SimpleEnergyData(FuMO25BlockEntity.MAX_DATA_COUNT));
    }

    public FuMO25Menu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access, ContainerEnergyData containerData) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(1), access, containerData);
    }

    public FuMO25Menu(int pContainerId, Inventory inventory, Container container, ContainerLevelAccess access, ContainerEnergyData containerData) {
        super(ModMenuTypes.FUMO_25_MENU.get(), pContainerId, containerData);

        checkContainerSize(container, 1);

        this.container = container;
        this.access = access;
        this.containerData = containerData;

        this.addSlot(new ParaSlot(container, 0, 278, 60));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18 + X_OFFSET, 84 + i * 18 + Y_OFFSET));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18 + X_OFFSET, 142 + Y_OFFSET));
        }
    }

    public void setPos(int x, int y, int z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void resetPos() {
        this.posX = Integer.MIN_VALUE;
        this.posY = Integer.MIN_VALUE;
        this.posZ = Integer.MIN_VALUE;
    }

    public void setPosToParameters() {
        if (this.posX != Integer.MIN_VALUE && this.posY != Integer.MIN_VALUE) {
            ItemStack stack = this.container.getItem(0);
            if (stack.isEmpty()) return;

            stack.getOrCreateTag().putInt("TargetX", this.posX);
            stack.getOrCreateTag().putInt("TargetY", this.posY);
            stack.getOrCreateTag().putInt("TargetZ", this.posZ);

            this.resetPos();
            this.container.setChanged();
        }
    }

    @Nullable
    public BlockPos getCurrentPos() {
        if (this.posX != Integer.MIN_VALUE && this.posY != Integer.MIN_VALUE && this.posZ != Integer.MIN_VALUE) {
            return new BlockPos(this.posX, this.posY, this.posZ);
        }
        return null;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex != 0) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                } else if (pIndex >= 1 && pIndex < 28) {
                    if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 28 && pIndex < 37 && !this.moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.access.evaluate((level, pos) -> level.getBlockState(pos).is(ModBlocks.FUMO_25.get())
                && pPlayer.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    public void removed(Player pPlayer) {
        this.access.execute((level, pos) -> {
            ItemStack para = this.container.getItem(0);
            if (!para.isEmpty()) {
                pPlayer.getInventory().placeItemBackInInventory(para);
            }
            this.container.removeItemNoUpdate(0);
            resetPos();
        });
    }

    public long getEnergy() {
        return this.containerData.get(0);
    }

    public long getFuncType() {
        return this.containerData.get(1);
    }

    public void setFuncType(byte type) {
        this.containerData.set(1, type);
    }

    public long getTime() {
        return this.containerData.get(2);
    }

    static class ParaSlot extends Slot {

        public ParaSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(ModItems.FIRING_PARAMETERS.get());
        }
    }
}
