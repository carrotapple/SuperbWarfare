package net.mcreator.superbwarfare.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ImageTooltip implements TooltipComponent {

    public int width;
    public int height;
    public ItemStack stack;

    public ImageTooltip(int width, int height, ItemStack stack) {
        this.width = width;
        this.height = height;
        this.stack = stack;
    }

    public ImageTooltip(ItemStack stack) {
        this(32, 16, stack);
    }

}