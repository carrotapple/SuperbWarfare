package net.mcreator.superbwarfare.enchantment;

import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class HealClip extends Enchantment {

    public HealClip() {
        super(Rarity.RARE, EnchantmentCategoryTool.CAN_RELOAD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 15 + 6 * pLevel;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return getMinCost(pLevel) + 10;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemstack) {
        return itemstack.is(ModTags.Items.CAN_RELOAD);
    }

}
