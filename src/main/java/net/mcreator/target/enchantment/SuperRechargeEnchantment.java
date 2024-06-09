
package net.mcreator.target.enchantment;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

public class SuperRechargeEnchantment extends Enchantment {
	public SuperRechargeEnchantment(EquipmentSlot... slots) {
		super(Rarity.UNCOMMON, EnchantmentCategoryTool.TASER, slots);
	}

	@Override
	public int getMinCost(int pLevel) {
		return 10 + 5 * pLevel;
	}

	@Override
	public int getMaxCost(int pLevel) {
		return super.getMaxCost(pLevel) + 25;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack itemstack) {
		return Ingredient.of(new ItemStack(TargetModItems.TASER.get())).test(itemstack);
	}
}
