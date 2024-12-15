package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.init.ModMenuTypes;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyDataSlot;
import com.atsuishio.superbwarfare.network.dataslot.SimpleEnergyData;
import com.atsuishio.superbwarfare.network.message.ContainerDataMessage;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChargingStationMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerEnergyData containerData;
    protected final Level level;
    private final List<ContainerEnergyDataSlot> containerEnergyDataSlots = Lists.newArrayList();
    private final List<ServerPlayer> usingPlayers = new ArrayList<>();

    public static final int X_OFFSET = 0;
    public static final int Y_OFFSET = 0;

    public ChargingStationMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(2), new SimpleEnergyData(ChargingStationBlockEntity.MAX_DATA_COUNT));
    }

    public ChargingStationMenu(int id, Inventory inventory, Container container, ContainerEnergyData containerData) {
        super(ModMenuTypes.CHARGING_STATION_MENU.get(), id);

        checkContainerSize(container, 2);

        this.container = container;
        this.containerData = containerData;
        this.level = inventory.player.level();

        this.addSlot(new Slot(container, 0, 44, 54));
        this.addSlot(new ChargingSlot(container, 1, 116, 54));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18 + X_OFFSET, 84 + i * 18 + Y_OFFSET));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18 + X_OFFSET, 142 + Y_OFFSET));
        }

        for (int i = 0; i < containerData.getCount(); ++i) {
            this.containerEnergyDataSlots.add(ContainerEnergyDataSlot.forContainer(containerData, i));
        }
    }

    @Override
    public void broadcastChanges() {
        List<ContainerDataMessage.Pair> pairs = new ArrayList<>();
        for (int i = 0; i < containerEnergyDataSlots.size(); ++i) {
            ContainerEnergyDataSlot dataSlot = containerEnergyDataSlots.get(i);
            if (dataSlot.checkAndClearUpdateFlag())
                pairs.add(new ContainerDataMessage.Pair(i, dataSlot.get()));
        }

        if (!pairs.isEmpty()) {
            PacketDistributor.PacketTarget target = PacketDistributor.NMLIST.with(usingPlayers.stream().map(serverPlayer -> serverPlayer.connection.connection)::toList);
            ModUtils.PACKET_HANDLER.send(target, new ContainerDataMessage(this.containerId, pairs));
        }

        super.broadcastChanges();
    }

    public void setData(int id, int data) {
        containerEnergyDataSlots.get(id).set(data);
    }

    public void setData(int id, long data) {
        containerEnergyDataSlots.get(id).set(data);
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
                } else if (ForgeHooks.getBurnTime(itemstack1, RecipeType.SMELTING) > 0 || itemstack1.getFoodProperties(null) != null) {
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

    public long getFuelTick() {
        return this.containerData.get(0);
    }

    public long getMaxFuelTick() {
        return this.containerData.get(1);
    }

    public long getEnergy() {
        return this.containerData.get(2);
    }

    static class ChargingSlot extends Slot {

        public ChargingSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return super.mayPlace(pStack);
        }
    }

    @SubscribeEvent
    public static void onContainerOpened(PlayerContainerEvent.Open event) {
        if (event.getContainer() instanceof ChargingStationMenu menu && event.getEntity() instanceof ServerPlayer serverPlayer) {
            menu.usingPlayers.add(serverPlayer);

            List<ContainerDataMessage.Pair> toSync = new ArrayList<>();
            for (int i = 0; i < menu.containerEnergyDataSlots.size(); ++i) {
                toSync.add(new ContainerDataMessage.Pair(i, menu.containerEnergyDataSlots.get(i).get()));
            }

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ContainerDataMessage(menu.containerId, toSync));
        }
    }

    @SubscribeEvent
    public static void onContainerClosed(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof ChargingStationMenu menu && event.getEntity() instanceof ServerPlayer serverPlayer) {
            menu.usingPlayers.remove(serverPlayer);
        }
    }
}
