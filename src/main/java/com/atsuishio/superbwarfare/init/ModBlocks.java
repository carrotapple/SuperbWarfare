package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.*;
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
    public static final RegistryObject<Block> SILVER_ORE = REGISTRY.register("silver_ore", SilverOreBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = REGISTRY.register("deepslate_silver_ore", DeepslateSilverOreBlock::new);
    public static final RegistryObject<Block> DRAGON_TEETH = REGISTRY.register("dragon_teeth", DragonTeethBlock::new);
    public static final RegistryObject<Block> REFORGING_TABLE = REGISTRY.register("reforging_table", ReforgingTableBlock::new);
    public static final RegistryObject<Block> LEAD_BLOCK = REGISTRY.register("lead_block", LeadBlock::new);
    public static final RegistryObject<Block> STEEL_BLOCK = REGISTRY.register("steel_block", SteelBlock::new);
    public static final RegistryObject<Block> TUNGSTEN_BLOCK = REGISTRY.register("tungsten_block", TungstenBlock::new);
    public static final RegistryObject<Block> SILVER_BLOCK = REGISTRY.register("silver_block", SilverBlock::new);
    public static final RegistryObject<Block> CEMENTED_CARBIDE_BLOCK = REGISTRY.register("cemented_carbide_block", CementedCarbideBlock::new);
    public static final RegistryObject<Block> CONTAINER = REGISTRY.register("container", ContainerBlock::new);
    public static final RegistryObject<Block> CHARGING_STATION = REGISTRY.register("charging_station", ChargingStationBlock::new);
    public static final RegistryObject<Block> FUMO_25 = REGISTRY.register("fumo_25", FuMO25Block::new);
}
