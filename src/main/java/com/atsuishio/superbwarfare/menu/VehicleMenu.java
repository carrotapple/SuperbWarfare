package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.init.ModMenuTypes;
import com.atsuishio.superbwarfare.item.PerkItem;
import com.atsuishio.superbwarfare.perk.Perk;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VehicleMenu extends AbstractContainerMenu {

    private final Container container;
    private final int containerRows;

    public static final int DEFAULT_SIZE = 105;

    public static final int X_OFFSET = 97;
    public static final int Y_OFFSET = 20;

    public VehicleMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(DEFAULT_SIZE));
    }

    public VehicleMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer) {
        super(ModMenuTypes.VEHICLE_MENU.get(), pContainerId);

        checkContainerSize(pContainer, DEFAULT_SIZE);
        this.container = pContainer;
        this.containerRows = 6;
        pContainer.startOpen(pPlayerInventory.player);
        int i = (this.containerRows - 4) * 18;

        for (int j = 0; j < this.containerRows; ++j) {
            for (int k = 0; k < 17; ++k) {
                this.addSlot(new Slot(pContainer, k + j * 17, 8 + k * 18 + 25, 18 + j * 18));
            }
        }

        this.addSlot(new PerkSlot(pContainer, this.containerRows * 17, Perk.Type.AMMO, 8, 36));
        this.addSlot(new PerkSlot(pContainer, this.containerRows * 17 + 1, Perk.Type.FUNCTIONAL, 8, 54));
        this.addSlot(new PerkSlot(pContainer, this.containerRows * 17 + 2, Perk.Type.DAMAGE, 8, 72));

        for (int l = 0; l < 3; ++l) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + l * 9 + 9, 8 + j * 18 + X_OFFSET, 84 + l * 18 + Y_OFFSET + i));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18 + X_OFFSET, 142 + Y_OFFSET + i));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.containerRows * 17 + 2) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 17 + 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() instanceof PerkItem) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 17, this.containerRows * 17 + 2, false)) {
                    if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 17, false)) {
                        return ItemStack.EMPTY;
                    }
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 17, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }

    static class PerkSlot extends Slot {

        public Perk.Type type;

        public PerkSlot(Container pContainer, int pSlot, Perk.Type type, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
            this.type = type;
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == type;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
