package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.block.menu.ChargingStationMenu;
import com.atsuishio.superbwarfare.entity.IChargeEntity;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChargingStationBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    protected static final int SLOT_FUEL = 0;
    protected static final int SLOT_CHARGE = 1;

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0};

    public static final int MAX_ENERGY = 4000000;
    public static final int MAX_DATA_COUNT = 3;
    public static final int DEFAULT_FUEL_TIME = 1600;
    public static final int CHARGE_SPEED = 128;
    public static final int CHARGE_OTHER_SPEED = 100000;
    public static final int CHARGE_RADIUS = 8;

    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    private final LazyOptional<EnergyStorage> energyHandler;
    private LazyOptional<?>[] itemHandlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    public int fuelTick = 0;
    public int maxFuelTick = DEFAULT_FUEL_TIME;
    public int energy;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> ChargingStationBlockEntity.this.fuelTick;
                case 1 -> ChargingStationBlockEntity.this.maxFuelTick;
                case 2 -> ChargingStationBlockEntity.this.energy;
                default -> 0;
            };
        }

        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    ChargingStationBlockEntity.this.fuelTick = pValue;
                    break;
                case 1:
                    ChargingStationBlockEntity.this.maxFuelTick = pValue;
                    break;
                case 2:
                    ChargingStationBlockEntity.this.energy = pValue;
                    break;
            }
        }

        public int getCount() {
            return MAX_DATA_COUNT;
        }
    };

    public ChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHARGING_STATION.get(), pos, state);

        this.energyHandler = LazyOptional.of(() -> new EnergyStorage(MAX_ENERGY));
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, ChargingStationBlockEntity blockEntity) {
        if (blockEntity.fuelTick > 0) {
            blockEntity.fuelTick--;
            blockEntity.energyHandler.ifPresent(handler -> {
                int energy = handler.getEnergyStored();
                if (energy < handler.getMaxEnergyStored()) {
                    handler.receiveEnergy(CHARGE_SPEED, false);
                    blockEntity.setChanged();
                }
            });
        } else {
            if (blockEntity.getItem(SLOT_FUEL).isEmpty()) return;

            AtomicBoolean flag = new AtomicBoolean(false);
            blockEntity.energyHandler.ifPresent(handler -> {
                if (handler.getEnergyStored() >= handler.getMaxEnergyStored()) {
                    flag.set(true);
                }
            });
            if (flag.get()) return;

            ItemStack fuel = blockEntity.getItem(SLOT_FUEL);
            int burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
            if (burnTime > 0) {
                blockEntity.fuelTick = burnTime;
                blockEntity.maxFuelTick = burnTime;
                fuel.shrink(1);
                blockEntity.setChanged();
            } else if (fuel.getItem().isEdible()) {
                var properties = fuel.getFoodProperties(null);
                if (properties == null) return;

                int nutrition = properties.getNutrition();
                float saturation = properties.getSaturationModifier() * 2.0f * nutrition;
                int tick = nutrition * 80 + (int) (saturation * 200);

                if (fuel.hasCraftingRemainingItem()) {
                    tick += 400;
                }

                fuel.shrink(1);

                blockEntity.fuelTick = tick;
                blockEntity.maxFuelTick = tick;
                blockEntity.setChanged();
            }
        }

        blockEntity.energyHandler.ifPresent(handler -> {
            int energy = handler.getEnergyStored();
            blockEntity.energy = energy;
            if (energy > 0) {
                blockEntity.chargeEntity(handler);
            }
            if (handler.getEnergyStored() > 0) {
                blockEntity.chargeItemStack(handler);
            }
        });
    }

    private void chargeEntity(EnergyStorage handler) {
        if (this.level == null) return;

        List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(CHARGE_RADIUS));
        entities.forEach(entity -> {
            if (entity instanceof IChargeEntity chargeEntity) {
                chargeEntity.charge(Math.min(CHARGE_OTHER_SPEED, handler.getEnergyStored()));
                handler.extractEnergy(Math.min(CHARGE_OTHER_SPEED, handler.getEnergyStored()), false);
            }
        });
        this.setChanged();
    }

    private void chargeItemStack(EnergyStorage handler) {
        ItemStack stack = this.getItem(SLOT_CHARGE);
        if (stack.isEmpty()) return;

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(consumer -> {
            if (consumer.getEnergyStored() < consumer.getMaxEnergyStored()) {
                consumer.receiveEnergy(Math.min(CHARGE_OTHER_SPEED, handler.getEnergyStored()), false);
                handler.extractEnergy(Math.min(CHARGE_OTHER_SPEED, handler.getEnergyStored()), false);
                this.setChanged();
            }
        });
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("Energy")) {
            getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
                ((EnergyStorage) handler).deserializeNBT(pTag.get("Energy"));
            });
        }
        this.fuelTick = pTag.getInt("FuelTick");
        this.maxFuelTick = pTag.getInt("MaxFuelTick");
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> pTag.put("Energy", ((EnergyStorage) handler).serializeNBT()));
        pTag.putInt("FuelTick", this.fuelTick);
        pTag.putInt("MaxFuelTick", this.maxFuelTick);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return pIndex == SLOT_FUEL;
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
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
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        ItemStack itemstack = this.items.get(pSlot);
        boolean flag = !pStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack);
        this.items.set(pSlot, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        if (pSlot == 0 && !flag) {
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.superbwarfare.charging_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChargingStationMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.items, true);
        return compoundtag;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) {
                return itemHandlers[0].cast();
            } else if (side == Direction.DOWN) {
                return itemHandlers[1].cast();
            } else {
                return itemHandlers[2].cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<?> itemHandler : itemHandlers) itemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.itemHandlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
