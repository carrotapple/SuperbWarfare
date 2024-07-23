package net.mcreator.target.item;

import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Monitor extends Item {
    public static final String LINKED = "Linked";
    public static final String LINKED_DRONE = "LinkedDrone";

    public Monitor() {
        super(new Properties().stacksTo(1));
    }

    public static void link(ItemStack itemstack, int id) {
        ItemNBTTool.setBoolean(itemstack, LINKED, true);
        ItemNBTTool.setInt(itemstack, LINKED_DRONE, id);
    }

    public static void disLink(ItemStack itemstack) {
        ItemNBTTool.setBoolean(itemstack, LINKED, false);
        ItemNBTTool.setInt(itemstack, LINKED_DRONE, -1);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addMonitorTips(list, ItemNBTTool.getInt(stack, LINKED_DRONE, -1));
    }
}
