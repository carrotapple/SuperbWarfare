package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.capability.energy.ItemEnergyProvider;
import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class BatteryItem extends Item {

    private final Supplier<Integer> energyCapacity;
    public int maxEnergy;

    public BatteryItem(int maxEnergy, Properties properties) {
        super(properties.stacksTo(1));
        this.maxEnergy = maxEnergy;
        this.energyCapacity = () -> maxEnergy;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        if (!pStack.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            return false;
        }

        AtomicInteger energy = new AtomicInteger(0);
        pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> energy.set(e.getEnergyStored())
        );
        return energy.get() != maxEnergy;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        AtomicInteger energy = new AtomicInteger(0);
        pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> energy.set(e.getEnergyStored())
        );

        return Math.round((float) energy.get() * 13.0F / maxEnergy);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag tag) {
        return new ItemEnergyProvider(stack, energyCapacity.get());
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0xFFFF00;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new CellImageComponent(pStack));
    }
}
