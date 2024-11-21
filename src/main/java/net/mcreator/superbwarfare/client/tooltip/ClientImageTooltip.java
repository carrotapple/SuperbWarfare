package net.mcreator.superbwarfare.client.tooltip;

import net.mcreator.superbwarfare.init.ModKeyMappings;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class ClientImageTooltip implements ClientTooltipComponent {

    private final int width;
    private final int height;
    private final ItemStack stack;

    public ClientImageTooltip(ImageTooltip tooltip) {
        this.width = tooltip.width();
        this.height = tooltip.height();
        this.stack = tooltip.stack();
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();

        int yo = 0;
        if (renderWeaponDamageTooltip(font, guiGraphics, x, y + yo)) yo += 10;
        if (renderWeaponLevelTooltip(font, guiGraphics, x, y + yo)) yo += 10;
        if (renderWeaponBypassTooltip(font, guiGraphics, x, y + yo)) yo += 10;
        if (renderWeaponEditTooltip(font, guiGraphics, x, y + yo)) yo += 20;

        if (!Screen.hasShiftDown()) {
            renderPerksShortcut(guiGraphics, x, y + yo);
            yo += 20;
        }

        guiGraphics.pose().popPose();
    }

    // TODO 等nbt重置后，修改nbt位置
    private boolean renderWeaponDamageTooltip(Font font, GuiGraphics guiGraphics, int x, int y) {
        double damage = ItemNBTTool.getDouble(stack, "damage", 0) * TooltipTool.perkDamage(stack);
        var damageComponent = Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage) + (TooltipTool.heBullet(stack) ? " + "
                        + new DecimalFormat("##.#").format(0.8 * damage * (1 + 0.1 * TooltipTool.heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN));

        guiGraphics.drawString(font, damageComponent, x, y, 0xFFFFFF);

        if (stack.is(ModTags.Items.IS_AUTO_WEAPON)) {
            var rpmComponent = Component.translatable("des.superbwarfare.tips.rpm").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getDouble(stack, "rpm", 0) + ItemNBTTool.getDouble(stack, "customRpm", 0))).withStyle(ChatFormatting.GREEN));

            int xo = font.width(damageComponent.getVisualOrderText());
            guiGraphics.drawString(font, rpmComponent, x + xo + 16, y, 0xFFFFFF);
        }

        return true;
    }

    private boolean renderWeaponLevelTooltip(Font font, GuiGraphics guiGraphics, int x, int y) {
        int level = ItemNBTTool.getInt(stack, "Level", 0);
        double rate = ItemNBTTool.getDouble(stack, "Exp", 0) / (20 * Math.pow(level, 2) + 160 * level + 20);

        ChatFormatting formatting;
        if (level < 10) {
            formatting = ChatFormatting.WHITE;
        } else if (level < 20) {
            formatting = ChatFormatting.AQUA;
        } else if (level < 30) {
            formatting = ChatFormatting.LIGHT_PURPLE;
        } else if (level < 40) {
            formatting = ChatFormatting.GOLD;
        } else {
            formatting = ChatFormatting.RED;
        }

        var levelComponent = Component.translatable("des.superbwarfare.tips.level").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(level + "").withStyle(formatting).withStyle(ChatFormatting.BOLD))
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(" (" + new DecimalFormat("#0.00").format(rate * 100) + "%)").withStyle(ChatFormatting.GRAY));

        guiGraphics.drawString(font, levelComponent, x, y, 0xFFFFFF);

        int upgradePoint = Mth.floor(ItemNBTTool.getDouble(stack, "UpgradePoint", 0));
        var upgradeComponent = Component.translatable("des.superbwarfare.tips.upgrade_point").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(String.valueOf(upgradePoint)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD));

        int xo = font.width(levelComponent.getVisualOrderText());
        guiGraphics.drawString(font, upgradeComponent, x + xo + 16, y, 0xFFFFFF);

        return true;
    }

    private boolean renderWeaponBypassTooltip(Font font, GuiGraphics guiGraphics, int x, int y) {
        double perkBypassArmorRate = 0;
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        if (perk instanceof AmmoPerk ammoPerk) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            perkBypassArmorRate = ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
        }
        double bypassRate = Math.max(ItemNBTTool.getDouble(stack, "BypassesArmor", 0) + perkBypassArmorRate, 0);
        var bypassComponent = Component.translatable("des.superbwarfare.tips.bypass").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.##").format(bypassRate * 100) + "%").withStyle(ChatFormatting.GOLD));

        if (bypassRate > 0) {
            guiGraphics.drawString(font, bypassComponent, x, y, 0xFFFFFF);
        }

        double headshot = ItemNBTTool.getDouble(stack, "headshot", 0);
        var headshotComponent = Component.translatable("des.superbwarfare.tips.headshot").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(headshot) + "x").withStyle(ChatFormatting.AQUA));

        int xo = bypassRate > 0 ? font.width(bypassComponent.getVisualOrderText()) : 0;
        if (headshot > 0) {
            guiGraphics.drawString(font, headshotComponent, x + xo + 16, y, 0xFFFFFF);
        }

        return bypassRate > 0 || headshot > 0;
    }

    private boolean renderWeaponEditTooltip(Font font, GuiGraphics guiGraphics, int x, int y) {
        if (stack.is(ModTags.Items.CAN_CUSTOM_GUN)) {
            var editComponent = Component.translatable("des.superbwarfare.tips.edit",
                    "[" + ModKeyMappings.EDIT_MODE.getKey().getDisplayName().getString() + "]").withStyle(ChatFormatting.LIGHT_PURPLE);
            guiGraphics.drawString(font, editComponent, x, y + 10, 0xFFFFFF);

            return true;
        }
        return false;
    }

    private void renderPerksShortcut(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.pose().pushPose();

        int xOffset = -20;

        Perk ammoPerk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (ammoPerk != null && PerkHelper.getPerkItem(ammoPerk).isPresent()) {
            xOffset += 20;

            var ammoItem = PerkHelper.getPerkItem(ammoPerk).get().get();
            guiGraphics.renderItem(ammoItem.getDefaultInstance(), x + xOffset, y + 2);
        }

        Perk funcPerk = PerkHelper.getPerkByType(stack, Perk.Type.FUNCTIONAL);
        if (funcPerk != null && PerkHelper.getPerkItem(funcPerk).isPresent()) {
            xOffset += 20;

            var funcItem = PerkHelper.getPerkItem(funcPerk).get().get();
            guiGraphics.renderItem(funcItem.getDefaultInstance(), x + xOffset, y + 2);
        }

        Perk damagePerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
        if (damagePerk != null && PerkHelper.getPerkItem(damagePerk).isPresent()) {
            xOffset += 20;

            var damageItem = PerkHelper.getPerkItem(damagePerk).get().get();
            guiGraphics.renderItem(damageItem.getDefaultInstance(), x + xOffset, y + 2);
        }

        guiGraphics.pose().popPose();
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return width;
    }

}
