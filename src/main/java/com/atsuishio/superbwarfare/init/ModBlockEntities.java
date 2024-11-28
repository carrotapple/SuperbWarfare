package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.entity.ContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModUtils.MODID);

    public static final RegistryObject<BlockEntityType<ContainerBlockEntity>> CONTAINER = REGISTRY.register("container",
            () -> BlockEntityType.Builder.of(ContainerBlockEntity::new, ModBlocks.CONTAINER.get()).build(null));

}
