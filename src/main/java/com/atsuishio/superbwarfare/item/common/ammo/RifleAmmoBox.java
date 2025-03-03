package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.tools.AmmoType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RifleAmmoBox extends AmmoSupplierItem {

    public RifleAmmoBox() {
        super(AmmoType.RIFLE, 30, new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.superbwarfare.rifle_ammo_box").withStyle(ChatFormatting.GRAY));
    }
}
