package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FuMO25Menu extends AbstractContainerMenu {

    protected final Container container;
    protected final ContainerLevelAccess access;

    public static final int X_OFFSET = 0;
    public static final int Y_OFFSET = 11;

    public FuMO25Menu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(1), ContainerLevelAccess.NULL);
    }

    public FuMO25Menu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(1), access);
    }

    public FuMO25Menu(int pContainerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(ModMenuTypes.FUMO_25_MENU.get(), pContainerId);

        checkContainerSize(container, 1);

        this.container = container;
        this.access = access;

        this.addSlot(new ParaSlot(container, 0, 8 + X_OFFSET, 11 + Y_OFFSET));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18 + X_OFFSET, 84 + i * 18 + Y_OFFSET));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18 + X_OFFSET, 142 + Y_OFFSET));
        }
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
        });
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
