
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.mcreator.target.potion.ShockMobEffect;
import net.mcreator.target.TargetMod;

public class TargetModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TargetMod.MODID);
	public static final RegistryObject<MobEffect> SHOCK = REGISTRY.register("shock", () -> new ShockMobEffect());
}
