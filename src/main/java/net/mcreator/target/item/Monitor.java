package net.mcreator.target.item;

import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
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
    public static final String LINKED = "linked";


    public static void link(ItemStack itemstack, Boolean link) {
        ItemNBTTool.setBoolean(itemstack, LINKED, link);
    }
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        TooltipTool.addMonitorTips(list, stack, player);
    }
}
