package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

// TODO 图标和简介等
public class VehicleDeployerBlockItem extends BlockItem {
    public VehicleDeployerBlockItem() {
        super(ModBlocks.VEHICLE_DEPLOYER.get(), new Properties().stacksTo(1));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("test"));
    }
}
