
package net.mcreator.target.init;

import net.mcreator.target.enchantment.LongerWireEnchantment;
import net.mcreator.target.enchantment.MonsterHunterEnchantment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.target.enchantment.VoltOverloadEnchantment;
import net.mcreator.target.enchantment.SuperRechargeEnchantment;
import net.mcreator.target.TargetMod;

public class TargetModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TargetMod.MODID);
	public static final RegistryObject<Enchantment> VOLT_OVERLOAD = REGISTRY.register("volt_overload", () -> new VoltOverloadEnchantment());
	public static final RegistryObject<Enchantment> SUPER_RECHARGE = REGISTRY.register("super_recharge", () -> new SuperRechargeEnchantment());
	public static final RegistryObject<Enchantment> LONGER_WIRE = REGISTRY.register("longer_wire", () -> new LongerWireEnchantment());
	public static final RegistryObject<Enchantment> MONSTER_HUNTER = REGISTRY.register("monster_hunter", () -> new MonsterHunterEnchantment());
}
