package net.mcreator.superbwarfare.enchantment;

import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class KillClip extends Enchantment {

    public KillClip() {
        super(Rarity.UNCOMMON, EnchantmentCategoryTool.GUN, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + 3 * pLevel;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return getMinCost(pLevel) + 10;
    }
}
