package com.atsuishio.superbwarfare.client.tooltip;

import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.atsuishio.superbwarfare.tools.TooltipTool;
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSentinelImageTooltip extends ClientEnergyImageTooltip {

    public ClientSentinelImageTooltip(GunImageComponent tooltip) {
        super(tooltip);
    }

    @Override
    protected Component getDamageComponent() {
        AtomicBoolean flag = new AtomicBoolean(false);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> flag.set(e.getEnergyStored() > 0)
        );

        if (flag.get()) {
            double damage = (ItemNBTTool.getDouble(stack, "damage", 0) +
                    ItemNBTTool.getDouble(stack, "sentinelChargeDamage", 0))
                    * TooltipTool.perkDamage(stack);
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + (TooltipTool.heBullet(stack) ? " + " + new DecimalFormat("##.#")
                                    .format(0.8 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : ""))
                            .withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
        } else {
            double damage = ItemNBTTool.getDouble(stack, "damage", 0) * TooltipTool.perkDamage(stack);
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + (TooltipTool.heBullet(stack) ? new DecimalFormat("##.#")
                            .format(0.4 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN));
        }
    }
}
