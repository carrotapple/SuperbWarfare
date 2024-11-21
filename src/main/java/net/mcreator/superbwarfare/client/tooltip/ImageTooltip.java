package net.mcreator.superbwarfare.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ImageTooltip(int width, int height, ItemStack stack) implements TooltipComponent {
}