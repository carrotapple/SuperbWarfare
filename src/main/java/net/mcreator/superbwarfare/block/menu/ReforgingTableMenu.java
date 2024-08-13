package net.mcreator.superbwarfare.block.menu;

import net.mcreator.superbwarfare.init.ModBlocks;
import net.mcreator.superbwarfare.init.ModMenuTypes;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.PerkItem;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ReforgingTableMenu extends AbstractContainerMenu {
    protected final Container container;
    protected final ContainerLevelAccess access;

    public static final int INPUT_SLOT = 0;
    public static final int AMMO_PERK_SLOT = 1;
    public static final int FUNC_PERK_SLOT = 2;
    public static final int DAMAGE_PERK_SLOT = 3;
    public static final int RESULT_SLOT = 4;

    public final DataSlot ammoPerkLevel = DataSlot.standalone();
    public final DataSlot funcPerkLevel = DataSlot.standalone();
    public final DataSlot damagePerkLevel = DataSlot.standalone();

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

        this.ammoPerkLevel.set(1);
        this.funcPerkLevel.set(1);
        this.damagePerkLevel.set(1);

        this.addDataSlot(ammoPerkLevel);
        this.addDataSlot(funcPerkLevel);
        this.addDataSlot(damagePerkLevel);

        this.addSlot(new InputSlot(container, INPUT_SLOT, 20, 22));
        this.addSlot(new PerkSlot(container, AMMO_PERK_SLOT, Perk.Type.AMMO, 80, 25));
        this.addSlot(new PerkSlot(container, FUNC_PERK_SLOT, Perk.Type.FUNCTIONAL, 80, 45));
        this.addSlot(new PerkSlot(container, DAMAGE_PERK_SLOT, Perk.Type.DAMAGE, 80, 65));
        this.addSlot(new ResultSlot(container, RESULT_SLOT, 142, 45));

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

            if (pIndex == INPUT_SLOT) {
                onTakeGun(stack);
                if (!this.moveItemStackTo(stack, RESULT_SLOT + 1, RESULT_SLOT + 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= AMMO_PERK_SLOT && pIndex <= DAMAGE_PERK_SLOT) {
                onTakePerk(stack);
                if (!this.moveItemStackTo(stack, RESULT_SLOT + 1, RESULT_SLOT + 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex == RESULT_SLOT) {
                if (!this.moveItemStackTo(stack, RESULT_SLOT, RESULT_SLOT + 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
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

    public void setPerkLevel(Perk.Type type, boolean add) {
        switch (type) {
            case AMMO ->
                    this.ammoPerkLevel.set(add ? Math.min(10, this.ammoPerkLevel.get() + 1) : Math.max(1, this.ammoPerkLevel.get() - 1));
            case FUNCTIONAL ->
                    this.funcPerkLevel.set(add ? Math.min(10, this.funcPerkLevel.get() + 1) : Math.max(1, this.funcPerkLevel.get() - 1));
            case DAMAGE ->
                    this.damagePerkLevel.set(add ? Math.min(10, this.damagePerkLevel.get() + 1) : Math.max(1, this.damagePerkLevel.get() - 1));
        }
    }

    /**
     * 根据输入槽的枪械和Perk槽中的物品与等级，生成重铸后的武器，并放入输出槽中
     */
    public void generateResult() {
        ItemStack gun = this.container.getItem(INPUT_SLOT);
        if (!(gun.getItem() instanceof GunItem gunItem)) {
            return;
        }

        ItemStack ammo = this.container.getItem(AMMO_PERK_SLOT);
        ItemStack func = this.container.getItem(FUNC_PERK_SLOT);
        ItemStack damage = this.container.getItem(DAMAGE_PERK_SLOT);
        if (ammo.isEmpty() && func.isEmpty() && damage.isEmpty()) {
            return;
        }

        ItemStack result = gun.copy();

        if (!ammo.isEmpty() && ammo.getItem() instanceof PerkItem perkItem) {
            if (gunItem.canApplyPerk(result, perkItem.getPerk())) {
                PerkHelper.setPerk(result, perkItem.getPerk(), this.ammoPerkLevel.get());
                this.container.setItem(AMMO_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        if (!func.isEmpty() && func.getItem() instanceof PerkItem perkItem) {
            if (gunItem.canApplyPerk(result, perkItem.getPerk())) {
                PerkHelper.setPerk(result, perkItem.getPerk(), this.funcPerkLevel.get());
                this.container.setItem(FUNC_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        if (!damage.isEmpty() && damage.getItem() instanceof PerkItem perkItem) {
            if (gunItem.canApplyPerk(result, perkItem.getPerk())) {
                PerkHelper.setPerk(result, perkItem.getPerk(), this.damagePerkLevel.get());
                this.container.setItem(DAMAGE_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        this.container.setItem(INPUT_SLOT, ItemStack.EMPTY);
        this.container.setItem(RESULT_SLOT, result);
        this.container.setChanged();
    }

    /**
     * 从Perk槽中取出对应的Perk物品时，根据其类型移除输入槽中枪械的Perk
     *
     * @param perk Perk物品
     */
    private void onTakePerk(ItemStack perk) {
        ItemStack gun = this.container.getItem(INPUT_SLOT);
        if (!(gun.getItem() instanceof GunItem)) {
            return;
        }

        if (perk.getItem() instanceof PerkItem perkItem) {
            if (PerkHelper.getItemPerkLevel(perkItem.getPerk(), gun) <= 0) {
                return;
            }

            switch (perkItem.getPerk().type) {
                case AMMO -> this.ammoPerkLevel.set(1);
                case FUNCTIONAL -> this.funcPerkLevel.set(1);
                case DAMAGE -> this.damagePerkLevel.set(1);
            }

            ItemStack output = gun.copy();
            PerkHelper.removePerkByType(output, perkItem.getPerk().type);

            this.container.setItem(INPUT_SLOT, output);
            this.container.setChanged();
        }
    }

    /**
     * 将枪械放入输入槽中时，根据枪械上已有的Perk生成对应的Perk物品，并将等级调整为当前的等级
     *
     * @param pStack 输入的枪械
     */
    private void onPlaceGun(ItemStack pStack) {
        if (!(pStack.getItem() instanceof GunItem)) {
            return;
        }

        this.container.setChanged();

        var ammoPerk = PerkHelper.getPerkByType(pStack, Perk.Type.AMMO);
        if (ammoPerk != null) {
            this.ammoPerkLevel.set(PerkHelper.getItemPerkLevel(ammoPerk, pStack));
            var ammoPerkItem = PerkHelper.getPerkItem(ammoPerk);
            ammoPerkItem.ifPresent(registryObject -> this.container.setItem(AMMO_PERK_SLOT, registryObject.get().getDefaultInstance()));
        }

        var funcPerk = PerkHelper.getPerkByType(pStack, Perk.Type.FUNCTIONAL);
        if (funcPerk != null) {
            this.funcPerkLevel.set(PerkHelper.getItemPerkLevel(funcPerk, pStack));
            var funcPerkItem = PerkHelper.getPerkItem(funcPerk);
            funcPerkItem.ifPresent(registryObject -> this.container.setItem(FUNC_PERK_SLOT, registryObject.get().getDefaultInstance()));
        }

        var damagePerk = PerkHelper.getPerkByType(pStack, Perk.Type.DAMAGE);
        if (damagePerk != null) {
            this.damagePerkLevel.set(PerkHelper.getItemPerkLevel(damagePerk, pStack));
            var damagePerkItem = PerkHelper.getPerkItem(damagePerk);
            damagePerkItem.ifPresent(registryObject -> this.container.setItem(DAMAGE_PERK_SLOT, registryObject.get().getDefaultInstance()));
        }

        this.broadcastChanges();
    }

    /**
     * 拿走输入槽中的枪械时，如果Perk槽中存在放入枪械时生成的Perk物品，则将其移除，如果是没有的Perk则无视
     *
     * @param pStack 输入的枪械
     */
    private void onTakeGun(ItemStack pStack) {
        if (!(pStack.getItem() instanceof GunItem)) {
            return;
        }

        var ammoPerk = PerkHelper.getPerkByType(pStack, Perk.Type.AMMO);
        if (ammoPerk != null) {
            if (this.container.getItem(AMMO_PERK_SLOT).getItem() instanceof PerkItem perkItem && perkItem.getPerk() == ammoPerk) {
                this.container.setItem(AMMO_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        var funcPerk = PerkHelper.getPerkByType(pStack, Perk.Type.FUNCTIONAL);
        if (funcPerk != null) {
            if (this.container.getItem(FUNC_PERK_SLOT).getItem() instanceof PerkItem perkItem && perkItem.getPerk() == funcPerk) {
                this.container.setItem(FUNC_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        var damagePerk = PerkHelper.getPerkByType(pStack, Perk.Type.DAMAGE);
        if (damagePerk != null) {
            if (this.container.getItem(DAMAGE_PERK_SLOT).getItem() instanceof PerkItem perkItem && perkItem.getPerk() == damagePerk) {
                this.container.setItem(DAMAGE_PERK_SLOT, ItemStack.EMPTY);
            }
        }

        this.container.setChanged();
    }

    class InputSlot extends Slot {
        public InputSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        public boolean mayPlace(ItemStack pStack) {
            if (pStack.getItem() instanceof GunItem gunItem) {
                ItemStack ammoPerk = this.container.getItem(AMMO_PERK_SLOT);
                ItemStack funcPerk = this.container.getItem(FUNC_PERK_SLOT);
                ItemStack damagePerk = this.container.getItem(DAMAGE_PERK_SLOT);

                boolean flag1 = ammoPerk.isEmpty() || (ammoPerk.getItem() instanceof PerkItem perkItem && gunItem.canApplyPerk(pStack, perkItem.getPerk()));
                boolean flag2 = funcPerk.isEmpty() || (funcPerk.getItem() instanceof PerkItem perkItem && gunItem.canApplyPerk(pStack, perkItem.getPerk()));
                boolean flag3 = damagePerk.isEmpty() || (damagePerk.getItem() instanceof PerkItem perkItem && gunItem.canApplyPerk(pStack, perkItem.getPerk()));

                return flag1 && flag2 && flag3 && this.container.getItem(RESULT_SLOT).isEmpty();
            }
            return false;
        }

        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void onTake(Player pPlayer, ItemStack pStack) {
            super.onTake(pPlayer, pStack);
            onTakeGun(pStack);
        }

        @Override
        public void setByPlayer(ItemStack pStack) {
            onPlaceGun(pStack);
            super.setByPlayer(pStack);
        }
    }

    class PerkSlot extends Slot {
        public Perk.Type type;

        public PerkSlot(Container pContainer, int pSlot, Perk.Type type, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
            this.type = type;
        }

        public boolean mayPlace(ItemStack pStack) {
            return pStack.getItem() instanceof PerkItem perkItem && perkItem.getPerk().type == type && !container.getItem(INPUT_SLOT).isEmpty();
        }

        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void onTake(Player pPlayer, ItemStack pStack) {
            onTakePerk(pStack);
            super.onTake(pPlayer, pStack);
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
