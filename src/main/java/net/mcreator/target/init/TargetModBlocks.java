
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.mcreator.target.block.ScheeliteOreBlock;
import net.mcreator.target.block.SandbagBlock;
import net.mcreator.target.block.JumppadBlockBlock;
import net.mcreator.target.block.GunRecycleBlock;
import net.mcreator.target.block.GalenaOreBlock;
import net.mcreator.target.block.DeepslateScheeliteOreBlock;
import net.mcreator.target.block.DeepslateGalenaOreBlock;
import net.mcreator.target.block.BarbedWireBlock;
import net.mcreator.target.TargetMod;

public class TargetModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TargetMod.MODID);
	public static final RegistryObject<Block> SANDBAG = REGISTRY.register("sandbag", () -> new SandbagBlock());
	public static final RegistryObject<Block> BARBED_WIRE = REGISTRY.register("barbed_wire", () -> new BarbedWireBlock());
	public static final RegistryObject<Block> JUMPPAD_BLOCK = REGISTRY.register("jumppad_block", () -> new JumppadBlockBlock());
	public static final RegistryObject<Block> GALENA_ORE = REGISTRY.register("galena_ore", () -> new GalenaOreBlock());
	public static final RegistryObject<Block> DEEPSLATE_GALENA_ORE = REGISTRY.register("deepslate_galena_ore", () -> new DeepslateGalenaOreBlock());
	public static final RegistryObject<Block> SCHEELITE_ORE = REGISTRY.register("scheelite_ore", () -> new ScheeliteOreBlock());
	public static final RegistryObject<Block> DEEPSLATE_SCHEELITE_ORE = REGISTRY.register("deepslate_scheelite_ore", () -> new DeepslateScheeliteOreBlock());
	public static final RegistryObject<Block> GUN_RECYCLE = REGISTRY.register("gun_recycle", () -> new GunRecycleBlock());
}
