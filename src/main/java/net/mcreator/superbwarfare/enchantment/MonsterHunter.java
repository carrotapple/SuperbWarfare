package net.mcreator.superbwarfare.enchantment;

import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class MonsterHunter extends Enchantment {
	public MonsterHunter(EquipmentSlot... slots) {
		super(Rarity.UNCOMMON, EnchantmentCategoryTool.GUN, slots);
	}

	@Override
	public int getMinCost(int pLevel) {
		return 8 + 6 * pLevel;
	}

	@Override
	public int getMaxCost(int pLevel) {
		return super.getMaxCost(pLevel) + 20;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
