package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.menu.VehicleMenu;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;

public class ContainerMobileEntity extends MobileVehicleEntity implements HasCustomInventoryScreen, ContainerEntity {

    public static final int CONTAINER_SIZE = 102;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    public ContainerMobileEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ContainerHelper.saveAllItems(compound, this.getItemStacks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ContainerHelper.loadAllItems(compound, this.getItemStacks());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getVehicle() == this) return InteractionResult.PASS;

        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.EXPLOSIVE_MINE.get())) {
            stack.use(player.level(), player, hand);
            return InteractionResult.SUCCESS;
        }
        if (player.isShiftKeyDown() && stack.is(ModItems.CROWBAR.get())) {
            ItemStack container = ContainerBlockItem.createInstance(this);
            if (!player.addItem(container)) {
                player.drop(container, false);
            }
            this.remove(RemovalReason.DISCARDED);
            this.discard();
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown()) {
            player.openMenu(this);
            return !player.level().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
        } else if (this.getHealth() < this.getMaxHealth() && stack.is(Items.IRON_INGOT)) {
            this.heal(Math.min(50, this.getMaxHealth()));
            stack.shrink(1);
            if (!this.level().isClientSide) {
                this.level().playSound(null, this, SoundEvents.IRON_GOLEM_REPAIR, this.getSoundSource(), 0.5f, 1);
            }
            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown()) {
            if (this.getFirstPassenger() == null) {
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else if (!(this.getFirstPassenger() instanceof Player)) {
                this.getFirstPassenger().stopRiding();
                player.setXRot(this.getXRot());
                player.setYRot(this.getYRot());
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            if (this.canAddPassenger(player)) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void remove(RemovalReason pReason) {
        if (!this.level().isClientSide && pReason != RemovalReason.DISCARDED) {
            Containers.dropContents(this.level(), this, this);
        }
        super.remove(pReason);
    }


    @Override
    public void baseTick() {
        super.baseTick();
        pickUpItem();

        this.getItemStacks().stream().filter(stack -> stack.is(ModItems.CELL.get()) && stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0) > 0)
                .forEach(stack ->
                        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> {
                            if (this.getEnergy() < this.getMaxEnergy()) {
                                int energy = this.getMaxEnergy() - this.getEnergy();
                                int stackEnergyNeed = Math.min(energyStorage.getEnergyStored(), energy);
                                energyStorage.extractEnergy(stackEnergyNeed, false);
                                this.setEnergy(this.getEnergy() + stackEnergyNeed);
                            }
                        })
                );

//        Player player = (Player) this.getFirstPassenger();

//        if (player != null) {
//            player.displayClientMessage(Component.literal( new DecimalFormat("##.#").format(this.getEnergy())), true);
//        }

        this.refreshDimensions();
    }

    public void pickUpItem() {
        List<ItemEntity> list = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.2F, 0.1, 0.2F));
        if (!list.isEmpty()) {
            for (ItemEntity entity : list) {
                if (!this.level().isClientSide) {
                    HopperBlockEntity.addItem(this, entity);
                }
            }
        }
    }

    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
        pPlayer.openMenu(this);
        if (!pPlayer.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, pPlayer);
        }
    }

    @Nullable
    @Override
    public ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public void setLootTable(@Nullable ResourceLocation pLootTable) {
    }

    @Override
    public long getLootTableSeed() {
        return 0;
    }

    @Override
    public void setLootTableSeed(long pLootTableSeed) {
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.items;
    }

    @Override
    public void clearItemStacks() {
        this.items.clear();
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack itemstack = this.getItemStacks().get(pSlot);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.getItemStacks().set(pSlot, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.getItemStacks().set(pSlot, pStack);
        if (!pStack.isEmpty() && pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return !this.isRemoved() && this.position().closerThan(pPlayer.position(), 8.0D);
    }

    @Override
    public void clearContent() {
        this.getItemStacks().clear();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        if (pPlayer.isSpectator()) {
            return null;
        } else {
            return new VehicleMenu(pContainerId, pPlayerInventory, this);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    }

    @Override
    public void stopOpen(Player pPlayer) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(pPlayer));
    }
}
