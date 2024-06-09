
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.target.enchantment.VoltOverloadEnchantment;
import net.mcreator.target.TargetMod;

public class TargetModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TargetMod.MODID);
	public static final RegistryObject<Enchantment> VOLT_OVERLOAD = REGISTRY.register("volt_overload", () -> new VoltOverloadEnchantment());
}
