package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.item.gun.Taser;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentCategoryTool {
    public static final EnchantmentCategory TASER = EnchantmentCategory.create("superbwarfare:taser", item -> item instanceof Taser);

    public static final EnchantmentCategory GUN = EnchantmentCategory.create("superbwarfare:gun", item -> item instanceof GunItem && !(item instanceof Taser));
}

