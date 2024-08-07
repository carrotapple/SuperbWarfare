package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.enchantment.*;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModUtils.MODID);

	public static final RegistryObject<Enchantment> VOLT_OVERLOAD = REGISTRY.register("volt_overload", VoltOverload::new);
	public static final RegistryObject<Enchantment> SUPER_RECHARGE = REGISTRY.register("super_recharge", SuperRecharge::new);
	public static final RegistryObject<Enchantment> LONGER_WIRE = REGISTRY.register("longer_wire", LongerWire::new);
	public static final RegistryObject<Enchantment> MONSTER_HUNTER = REGISTRY.register("monster_hunter", MonsterHunter::new);
	public static final RegistryObject<Enchantment> HEAL_CLIP = REGISTRY.register("heal_clip", HealClip::new);
	public static final RegistryObject<Enchantment> GUTSHOT_STRAIGHT = REGISTRY.register("gutshot_straight", GutshotStraight::new);
	public static final RegistryObject<Enchantment> KILLING_TALLY = REGISTRY.register("killing_tally", KillingTally::new);
	public static final RegistryObject<Enchantment> FOURTH_TIMES_CHARM = REGISTRY.register("fourth_times_charm", FourthTimesCharm::new);
}
