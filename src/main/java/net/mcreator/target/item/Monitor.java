package net.mcreator.target.item;

import net.mcreator.target.tools.ItemNBTTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Monitor extends Item {
    public Monitor() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }
    public static final String NBT_LINKED = "LINKED";


    public static void link(ItemStack itemstack, Boolean link) {
        if (link) {
            ItemNBTTool.setBoolean(itemstack, NBT_LINKED, true);
        }
    }
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }
}
