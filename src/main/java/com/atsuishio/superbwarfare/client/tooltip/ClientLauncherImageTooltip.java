package com.atsuishio.superbwarfare.client.tooltip;

import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.atsuishio.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class ClientLauncherImageTooltip extends ClientGunImageTooltip {

    public ClientLauncherImageTooltip(GunImageComponent tooltip) {
        super(tooltip);
    }

    @Override
    protected Component getDamageComponent() {
        double damage = GunsTool.getGunDoubleTag(stack, "Damage", 0) * TooltipTool.perkDamage(stack);
        double explosionDamage = ItemNBTTool.getDouble(stack, "ExplosionDamage", 0);

        return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.GREEN)
                        .append(Component.literal("").withStyle(ChatFormatting.RESET))
                        .append(Component.literal(" + " + new DecimalFormat("##.#").format(explosionDamage)).withStyle(ChatFormatting.GOLD)));
    }
}
