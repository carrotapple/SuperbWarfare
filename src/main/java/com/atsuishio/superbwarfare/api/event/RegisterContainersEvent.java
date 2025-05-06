package com.atsuishio.superbwarfare.api.event;

import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Register Entities as a container
 */
@ApiStatus.AvailableSince("0.7.7")
public class RegisterContainersEvent extends Event implements IModBusEvent {
    public static final List<ItemStack> containers = new ArrayList<>();

    public <T extends Entity> void add(RegistryObject<EntityType<T>> type) {
        add(type.get(), false);
    }

    public <T extends Entity> void add(RegistryObject<EntityType<T>> type, boolean canBePlacedAboveWater) {
        add(type.get(), canBePlacedAboveWater);
    }

    public <T extends Entity> void add(EntityType<T> type) {
        add(type, false);
    }

    public <T extends Entity> void add(EntityType<T> type, boolean canBePlacedAboveWater) {
        ItemStack stack = new ItemStack(ModBlocks.CONTAINER.get());
        CompoundTag tag = new CompoundTag();

        tag.putString("EntityType", EntityType.getKey(type).toString());
        BlockItem.setBlockEntityData(stack, ModBlockEntities.CONTAINER.get(), tag);

        if (canBePlacedAboveWater) {
            stack.getOrCreateTag().putBoolean("CanPlacedAboveWater", true);
        }

        containers.add(stack);
    }

    public void add(Entity entity) {
        add(entity, false);
    }

    public void add(Entity entity, boolean canBePlacedAboveWater) {
        ItemStack stack = new ItemStack(ModBlocks.CONTAINER.get());
        CompoundTag tag = new CompoundTag();

        tag.putString("EntityType", EntityType.getKey(entity.getType()).toString());
        BlockItem.setBlockEntityData(stack, ModBlockEntities.CONTAINER.get(), tag);
        tag.put("Entity", entity.serializeNBT());

        if (canBePlacedAboveWater) {
            stack.getOrCreateTag().putBoolean("CanPlacedAboveWater", true);
        }

        containers.add(stack);
    }
}
