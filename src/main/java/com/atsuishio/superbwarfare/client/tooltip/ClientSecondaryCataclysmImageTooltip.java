package com.atsuishio.superbwarfare.client.tooltip;

import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class ClientSecondaryCataclysmImageTooltip extends ClientEnergyImageTooltip {

    public ClientSecondaryCataclysmImageTooltip(GunImageComponent tooltip) {
        super(tooltip);
    }

    @Override
    protected Component getDamageComponent() {
        double damage = GunsTool.getGunDoubleTag(stack, "Damage", 0) * TooltipTool.perkDamage(stack);
        double explosionDamage = GunsTool.getGunDoubleTag(stack, "ExplosionDamage", 0);

        return Component.translatable("des.superbwarfare.guns.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.GREEN)
                        .append(Component.literal("").withStyle(ChatFormatting.RESET))
                        .append(Component.literal(" + " + new DecimalFormat("##.#").format(explosionDamage)).withStyle(ChatFormatting.GOLD)));
    }
}
