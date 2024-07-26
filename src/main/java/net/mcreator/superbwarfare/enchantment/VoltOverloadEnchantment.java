
package net.mcreator.superbwarfare.enchantment;

import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

import net.mcreator.superbwarfare.init.TargetModItems;

public class VoltOverloadEnchantment extends Enchantment {
	public VoltOverloadEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentCategoryTool.TASER, slots);
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

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack itemstack) {
		return Ingredient.of(new ItemStack(TargetModItems.TASER.get())).test(itemstack);
	}
}
