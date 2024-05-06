package net.mcreator.target.item.common.material.pack;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import net.mcreator.target.rarity.RarityTool;

import java.util.List;

public class LegendaryMaterialPackItem extends Item {
    public LegendaryMaterialPackItem() {
        super(new Item.Properties().stacksTo(64).rarity(RarityTool.LEGENDARY));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }
}
