
package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.enchantment.LongerWireEnchantment;
import net.mcreator.superbwarfare.enchantment.MonsterHunterEnchantment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.superbwarfare.enchantment.VoltOverloadEnchantment;
import net.mcreator.superbwarfare.enchantment.SuperRechargeEnchantment;

public class TargetModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModUtils.MODID);
	public static final RegistryObject<Enchantment> VOLT_OVERLOAD = REGISTRY.register("volt_overload", () -> new VoltOverloadEnchantment());
	public static final RegistryObject<Enchantment> SUPER_RECHARGE = REGISTRY.register("super_recharge", () -> new SuperRechargeEnchantment());
	public static final RegistryObject<Enchantment> LONGER_WIRE = REGISTRY.register("longer_wire", () -> new LongerWireEnchantment());
	public static final RegistryObject<Enchantment> MONSTER_HUNTER = REGISTRY.register("monster_hunter", () -> new MonsterHunterEnchantment());
}
