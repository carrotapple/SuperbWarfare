package com.atsuishio.superbwarfare.client.tooltip;

import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class ClientBocekImageTooltip extends ClientGunImageTooltip {

    public ClientBocekImageTooltip(GunImageComponent tooltip) {
        super(tooltip);
    }

    @Override
    protected Component getDamageComponent() {
        boolean slug = false;

        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
            slug = true;
        }

        double total = GunsTool.getGunDoubleTag(stack, "Damage", 0) * TooltipTool.perkDamage(stack);

        if (slug) {
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(total) + (TooltipTool.heBullet(stack) ? " + " + new DecimalFormat("##.#")
                            .format(0.8 * total * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN));
        } else {
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(total * 0.1) + " * 10").withStyle(ChatFormatting.GREEN))
                    .append(Component.literal(" / ").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(total)).withStyle(ChatFormatting.GREEN));
        }
    }
}
