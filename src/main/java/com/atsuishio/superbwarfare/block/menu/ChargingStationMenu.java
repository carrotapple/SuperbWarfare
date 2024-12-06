package com.atsuishio.superbwarfare.block.menu;

import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.init.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ChargingStationMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;

    public static final int X_OFFSET = 0;
    public static final int Y_OFFSET = 11;

    public ChargingStationMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(2), new SimpleContainerData(ChargingStationBlockEntity.MAX_DATA_COUNT));
    }

    public ChargingStationMenu(int id, Inventory inventory, Container container, ContainerData containerData) {
        super(ModMenuTypes.CHARGING_STATION_MENU.get(), id);

        checkContainerSize(container, 2);
        checkContainerDataCount(containerData, ChargingStationBlockEntity.MAX_DATA_COUNT);

        this.container = container;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 0, 51, 43));
        this.addSlot(new ChargingStationMenu.ChargingSlot(container, 1, 110, 43));

        this.addDataSlots(containerData);

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
            if (pIndex == 1) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex != 0) {
                if (itemstack1.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getBurnTime(null) > 0 || itemstack1.getFoodProperties(null) != null) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 2 && pIndex < 29) {
                    if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 29 && pIndex < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
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
        return this.container.stillValid(pPlayer);
    }

    public int getFuelTick() {
        return this.containerData.get(0);
    }

    public int getMaxFuelTick() {
        return this.containerData.get(1);
    }

    public int getEnergy() {
        return this.containerData.get(2);
    }

    class ChargingSlot extends Slot {

        public ChargingSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return super.mayPlace(pStack);
        }
    }

}
