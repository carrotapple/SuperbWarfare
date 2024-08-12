package net.mcreator.superbwarfare.block.menu;

import net.mcreator.superbwarfare.init.ModBlocks;
import net.mcreator.superbwarfare.init.ModMenuTypes;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.PerkItem;
import net.mcreator.superbwarfare.perk.Perk;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class ReforgingTableMenu extends AbstractContainerMenu {
    protected final Container container;
    protected final ContainerLevelAccess access;

    public static final int INPUT_SLOT = 0;
    public static final int AMMO_PERK_SLOT = 1;
    public static final int FUNC_PERK_SLOT = 2;
    public static final int DAMAGE_PERK_SLOT = 3;
    public static final int RESULT_SLOT = 4;

    private final DataSlot AMMO_PERK_LEVEL = DataSlot.standalone();
    private final DataSlot FUNC_PERK_LEVEL = DataSlot.standalone();
    private final DataSlot DAMAGE_PERK_LEVEL = DataSlot.standalone();

    public static final int X_OFFSET = 0;
    public static final int Y_OFFSET = 11;

    public ReforgingTableMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(5), ContainerLevelAccess.NULL);
    }

    public ReforgingTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(5), access);
    }

    public ReforgingTableMenu(int pContainerId, Inventory inventory, Container container, ContainerLevelAccess pContainerLevelAccess) {
        super(ModMenuTypes.REFORGING_TABLE_MENU.get(), pContainerId);

        checkContainerSize(container, 5);

        this.container = container;
        this.access = pContainerLevelAccess;

        this.addDataSlot(AMMO_PERK_LEVEL);
        this.addDataSlot(FUNC_PERK_LEVEL);
        this.addDataSlot(DAMAGE_PERK_LEVEL);

        this.addSlot(new InputSlot(container, INPUT_SLOT, 20, 20));
        this.addSlot(new PerkSlot(container, AMMO_PERK_SLOT, Perk.Type.AMMO, 60, 30));
        this.addSlot(new PerkSlot(container, FUNC_PERK_SLOT, Perk.Type.FUNCTIONAL, 60, 50));
        this.addSlot(new PerkSlot(container, DAMAGE_PERK_SLOT, Perk.Type.DAMAGE, 60, 70));
        this.addSlot(new ResultSlot(container, RESULT_SLOT, 130, 50));

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
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (pIndex == RESULT_SLOT) {
                if (!this.moveItemStackTo(stack, RESULT_SLOT, RESULT_SLOT + 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= AMMO_PERK_SLOT && pIndex <= DAMAGE_PERK_SLOT) {
                if (!this.moveItemStackTo(stack, RESULT_SLOT + 1, RESULT_SLOT + 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex != INPUT_SLOT) {
                if (stack.is(ModTags.Items.GUN)) {
                    if (!this.moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() instanceof PerkItem perkItem) {
                    Perk.Type type = perkItem.getPerk().type;
                    if (type == Perk.Type.AMMO) {
                        if (!this.moveItemStackTo(stack, AMMO_PERK_SLOT, AMMO_PERK_SLOT + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (type == Perk.Type.FUNCTIONAL) {
                        if (!this.moveItemStackTo(stack, FUNC_PERK_SLOT, FUNC_PERK_SLOT + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (type == Perk.Type.DAMAGE) {
                        if (!this.moveItemStackTo(stack, DAMAGE_PERK_SLOT, DAMAGE_PERK_SLOT + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(stack, RESULT_SLOT + 1, RESULT_SLOT + 37, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.access.evaluate((level, pos) -> level.getBlockState(pos).is(ModBlocks.REFORGING_TABLE.get())
                && pPlayer.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39796_, p_39797_) -> {
            for (int i = 0; i < this.container.getContainerSize(); ++i) {
                ItemStack itemstack = this.container.getItem(i);
                if (!itemstack.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(itemstack);
                }
            }
            this.clearContainer(pPlayer, this.container);
        });
    }

    static class InputSlot extends Slot {
        public InputSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(ModTags.Items.GUN);
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class PerkSlot extends Slot {
        public Perk.Type type;

        public PerkSlot(Container pContainer, int pSlot, Perk.Type type, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
            this.type = type;
        }

        public boolean mayPlace(ItemStack pStack) {
            return pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == type;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class ResultSlot extends Slot {
        public ResultSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        public boolean mayPlace(ItemStack pStack) {
            return false;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
