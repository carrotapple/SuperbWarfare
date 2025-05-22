package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.capability.energy.ItemEnergyProvider;
import com.atsuishio.superbwarfare.tiers.ModItemTier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.function.Supplier;

public class ElectricBaton extends SwordItem {

    private final Supplier<Integer> energyCapacity;

    public ElectricBaton() {
        super(ModItemTier.STEEL, 3, -2.5f, new Properties().durability(1114));

        this.energyCapacity = () -> 6000;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag tag) {
        return new ItemEnergyProvider(stack, energyCapacity.get());
    }
}
