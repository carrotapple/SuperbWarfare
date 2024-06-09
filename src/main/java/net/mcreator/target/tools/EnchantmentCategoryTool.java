package net.mcreator.target.tools;

import net.mcreator.target.item.gun.Taser;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentCategoryTool {
    public static final EnchantmentCategory TASER = EnchantmentCategory.create("target:taser", item -> item instanceof Taser);

}
