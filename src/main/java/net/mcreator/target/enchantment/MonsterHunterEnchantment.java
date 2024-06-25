
package net.mcreator.target.enchantment;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

public class MonsterHunterEnchantment extends Enchantment {
	public MonsterHunterEnchantment(EquipmentSlot... slots) {
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
