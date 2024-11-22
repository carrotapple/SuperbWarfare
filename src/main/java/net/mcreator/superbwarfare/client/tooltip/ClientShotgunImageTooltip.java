package net.mcreator.superbwarfare.client.tooltip;

import net.mcreator.superbwarfare.client.tooltip.component.GunImageComponent;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class ClientShotgunImageTooltip extends ClientGunImageTooltip {

    public ClientShotgunImageTooltip(GunImageComponent tooltip) {
        super(tooltip);
    }

    @Override
    protected Component getDamageComponent() {
        boolean slug = false;

        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
            slug = true;
        }

        if (slug) {
            double damage = ItemNBTTool.getDouble(stack, "damage", 0) * ItemNBTTool.getDouble(stack, "projectile_amount", 0) * TooltipTool.perkDamage(stack);
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + (TooltipTool.heBullet(stack) ? " + " + new DecimalFormat("##.#")
                            .format(0.8 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN));
        } else {
            double damage = ItemNBTTool.getDouble(stack, "damage", 0) * TooltipTool.perkDamage(stack);
            return Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + " * " + new DecimalFormat("##").format(ItemNBTTool.getDouble(stack, "projectile_amount", 0))).withStyle(ChatFormatting.GREEN));
        }
    }
}
