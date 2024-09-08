package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.item.gun.special.TaserItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentCategoryTool {
    public static final EnchantmentCategory TASER = EnchantmentCategory.create("superbwarfare:taser",
            item -> item instanceof TaserItem);
    public static final EnchantmentCategory GUN = EnchantmentCategory.create("superbwarfare:gun",
            item -> item instanceof GunItem && !(item instanceof TaserItem));
    public static final EnchantmentCategory CAN_RELOAD = EnchantmentCategory.create("superbwarfare:can_reload",
            item -> item.getDefaultInstance().is(ModTags.Items.CAN_RELOAD));
}
