package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TargetModBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TargetMod.MODID);

    public static final RegistryObject<Block> SANDBAG = REGISTRY.register("sandbag", SandbagBlock::new);
    public static final RegistryObject<Block> BARBED_WIRE = REGISTRY.register("barbed_wire", BarbedWireBlock::new);
    public static final RegistryObject<Block> JUMP_PAD = REGISTRY.register("jump_pad", JumpPadBlock::new);
    public static final RegistryObject<Block> GALENA_ORE = REGISTRY.register("galena_ore", GalenaOreBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_GALENA_ORE = REGISTRY.register("deepslate_galena_ore", DeepslateGalenaOreBlock::new);
    public static final RegistryObject<Block> SCHEELITE_ORE = REGISTRY.register("scheelite_ore", ScheeliteOreBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_SCHEELITE_ORE = REGISTRY.register("deepslate_scheelite_ore", DeepslateScheeliteOreBlock::new);
    public static final RegistryObject<Block> GUN_RECYCLE = REGISTRY.register("gun_recycle", GunRecycleBlock::new);
}
