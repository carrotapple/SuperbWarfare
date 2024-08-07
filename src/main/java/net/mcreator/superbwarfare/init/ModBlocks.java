package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ModUtils.MODID);

    public static final RegistryObject<Block> SANDBAG = REGISTRY.register("sandbag", SandbagBlock::new);
    public static final RegistryObject<Block> BARBED_WIRE = REGISTRY.register("barbed_wire", BarbedWireBlock::new);
    public static final RegistryObject<Block> JUMP_PAD = REGISTRY.register("jump_pad", JumpPadBlock::new);
    public static final RegistryObject<Block> GALENA_ORE = REGISTRY.register("galena_ore", GalenaOreBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_GALENA_ORE = REGISTRY.register("deepslate_galena_ore", DeepslateGalenaOreBlock::new);
    public static final RegistryObject<Block> SCHEELITE_ORE = REGISTRY.register("scheelite_ore", ScheeliteOreBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_SCHEELITE_ORE = REGISTRY.register("deepslate_scheelite_ore", DeepslateScheeliteOreBlock::new);
    public static final RegistryObject<Block> DRAGON_TEETH = REGISTRY.register("dragon_teeth", DragonTeethBlock::new);

}
