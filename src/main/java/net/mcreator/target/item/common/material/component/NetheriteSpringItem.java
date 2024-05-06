package net.mcreator.target.item.common.material.component;

import net.mcreator.target.rarity.RarityTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class NetheriteSpringItem extends Item {
    public NetheriteSpringItem() {
        super(new Item.Properties().stacksTo(64).rarity(RarityTool.LEGENDARY));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }
}
