package net.mcreator.superbwarfare.enchantment;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class FourthTimesCharm extends Enchantment {

    public FourthTimesCharm() {
        super(Rarity.VERY_RARE, EnchantmentCategoryTool.GUN, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + 8 * pLevel;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return getMinCost(pLevel) + 20;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemstack) {
        return itemstack.is(ModTags.Items.CAN_SHOOT_BULLET) && !itemstack.is(ModItems.MINIGUN.get()) && !itemstack.is(ModTags.Items.SHOTGUN);
    }
}
