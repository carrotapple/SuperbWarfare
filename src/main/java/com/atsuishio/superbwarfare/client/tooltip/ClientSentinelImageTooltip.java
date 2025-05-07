package com.atsuishio.superbwarfare.client.tooltip;

import com.atsuishio.superbwarfare.client.TooltipTool;
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import com.atsuishio.superbwarfare.tools.FormatTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

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
            double damage = data.damage();
            return Component.translatable("des.superbwarfare.guns.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(FormatTool.format1D(damage) + (TooltipTool.heBullet(stack) ? " + " +
                                    FormatTool.format1D(0.8 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : ""))
                            .withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
        } else {
            double damage = getGunData().damage();
            return Component.translatable("des.superbwarfare.guns.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(FormatTool.format1D(damage) + (TooltipTool.heBullet(stack) ?
                            FormatTool.format1D(0.4 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN));
        }
    }
}
