package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TooltipTool {

    public static void addHideText(List<Component> tooltip, Component text) {
        if (Screen.hasShiftDown()) {
            tooltip.add(text);
        }
    }

    public static double perkDamage(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            return ammoPerk.damageRate;
        }
        return 1;
    }

    public static boolean heBullet(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        return perk == ModPerks.HE_BULLET.get();
    }

    public static int heBulletLevel(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk == ModPerks.HE_BULLET.get()) {
            return PerkHelper.getItemPerkLevel(perk, stack);
        }
        return 0;
    }

    private static void addRpmTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.translatable("des.superbwarfare.tips.rpm").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getDouble(stack, "rpm", 0) + ItemNBTTool.getDouble(stack, "customRpm", 0))).withStyle(ChatFormatting.GREEN)));
    }

    private static void addLevelTips(List<Component> tooltip, ItemStack stack) {
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

        tooltip.add(Component.translatable("des.superbwarfare.tips.level").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(level + " " + new DecimalFormat("##.##").format(rate * 100) + "%").withStyle(formatting).withStyle(ChatFormatting.BOLD)));

        int upgradePoint = Mth.floor(ItemNBTTool.getDouble(stack, "UpgradePoint", 0));

        tooltip.add(Component.translatable("des.superbwarfare.tips.upgrade_point").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(String.valueOf(upgradePoint)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD)));

    }

    private static void addBypassTips(List<Component> tooltip, ItemStack stack) {
        double perkBypassArmorRate = 0;
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        if (perk instanceof AmmoPerk ammoPerk) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            perkBypassArmorRate = ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
        }
        double byPassRate = Math.max(ItemNBTTool.getDouble(stack, "BypassesArmor", 0) + perkBypassArmorRate, 0);

        tooltip.add(Component.translatable("des.superbwarfare.tips.bypass").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.##").format(byPassRate * 100) + "%").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD)));
    }

    private static void addPerkTips(List<Component> tooltip, ItemStack stack) {
        CompoundTag ammoTag = PerkHelper.getPerkTag(stack, Perk.Type.AMMO);
        CompoundTag functionalTag = PerkHelper.getPerkTag(stack, Perk.Type.FUNCTIONAL);
        CompoundTag damageTag = PerkHelper.getPerkTag(stack, Perk.Type.DAMAGE);

        if (!ammoTag.isEmpty() || !functionalTag.isEmpty() || !damageTag.isEmpty()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.translatable("perk.superbwarfare.tips").withStyle(ChatFormatting.GOLD));
        }

        if (!ammoTag.isEmpty()) {
            var ids = ammoTag.getString("id").split(":");
            if (ids.length <= 1) return;
            String id = ids[1];
            var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
            tooltip.add(Component.translatable("perk.superbwarfare.slot_Ammo").withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(" >> "))
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.translatable("item.superbwarfare." + id).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" Lvl. " + ammoTag.getInt("level")).withStyle(ChatFormatting.WHITE)));
            addHideText(tooltip, Component.translatable("des.superbwarfare." + id).withStyle(ChatFormatting.GRAY));
            if (perk instanceof AmmoPerk ammoPerk) {
                if (ammoPerk.damageRate < 1) {
                    addHideText(tooltip, Component.translatable("des.superbwarfare.perk_damage_reduce").withStyle(ChatFormatting.RED));
                } else if (ammoPerk.damageRate > 1) {
                    addHideText(tooltip, Component.translatable("des.superbwarfare.perk_damage_plus").withStyle(ChatFormatting.GREEN));
                }

                if (ammoPerk.speedRate < 1) {
                    addHideText(tooltip, Component.translatable("des.superbwarfare.perk_speed_reduce").withStyle(ChatFormatting.RED));
                } else if (ammoPerk.speedRate > 1) {
                    addHideText(tooltip, Component.translatable("des.superbwarfare.perk_speed_plus").withStyle(ChatFormatting.GREEN));
                }

                if (ammoPerk.slug && stack.is(ModTags.Items.SHOTGUN)) {
                    addHideText(tooltip, Component.translatable("des.superbwarfare.perk_slug").withStyle(ChatFormatting.YELLOW));
                }
            }
        }

        if (!functionalTag.isEmpty()) {
            var ids = functionalTag.getString("id").split(":");
            if (ids.length <= 1) return;
            String id = ids[1];
            tooltip.add(Component.translatable("perk.superbwarfare.slot_Functional").withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(" >> "))
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.translatable("item.superbwarfare." + id).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" Lvl. " + functionalTag.getInt("level")).withStyle(ChatFormatting.WHITE)));
            addHideText(tooltip, Component.translatable("des.superbwarfare." + id).withStyle(ChatFormatting.GRAY));
        }

        if (!damageTag.isEmpty()) {
            var ids = damageTag.getString("id").split(":");
            if (ids.length <= 1) return;
            String id = ids[1];
            tooltip.add(Component.translatable("perk.superbwarfare.slot_Damage").withStyle(ChatFormatting.RED)
                    .append(Component.literal(" >> "))
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.translatable("item.superbwarfare." + id).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" Lvl. " + damageTag.getInt("level")).withStyle(ChatFormatting.WHITE)));
            addHideText(tooltip, Component.translatable("des.superbwarfare." + id).withStyle(ChatFormatting.GRAY));
        }
    }

    public static void addSentinelTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.literal(""));

        AtomicBoolean flag = new AtomicBoolean(false);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> flag.set(e.getEnergyStored() > 0)
        );

        if (flag.get()) {
            double damage = (ItemNBTTool.getDouble(stack, "damage", 0) +
                    ItemNBTTool.getDouble(stack, "sentinelChargeDamage", 0))
                    * perkDamage(stack);

            tooltip.add(Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + (heBullet(stack) ? " + " + new DecimalFormat("##.#").format(0.8 * damage * (1 + 0.1 * heBulletLevel(stack))) : "")).withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)));
        } else {
            double damage = ItemNBTTool.getDouble(stack, "damage", 0) * perkDamage(stack);

            tooltip.add(Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage) + (heBullet(stack) ? new DecimalFormat("##.#").format(0.4 * damage * (1 + 0.1 * heBulletLevel(stack))) : "")).withStyle(ChatFormatting.GREEN)));
        }

        if (stack.is(ModTags.Items.IS_AUTO_WEAPON)) {
            addRpmTips(tooltip, stack);
        }
        addLevelTips(tooltip, stack);
        addBypassTips(tooltip, stack);
        addPerkTips(tooltip, stack);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> tooltip.add(Component.literal(e.getEnergyStored() + " / " + e.getMaxEnergyStored() + " FE").withStyle(ChatFormatting.GRAY))
        );
    }

    public static void addTaserTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.literal(""));

        double damage = ItemNBTTool.getDouble(stack, "damage", 0);

        tooltip.add(Component.translatable("des.superbwarfare.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.GREEN)));

        addLevelTips(tooltip, stack);
        int upgradePoint = Mth.floor(ItemNBTTool.getDouble(stack, "UpgradePoint", 0));

        tooltip.add(Component.translatable("des.superbwarfare.tips.upgrade_point").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(String.valueOf(upgradePoint)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD)));
        addPerkTips(tooltip, stack);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> tooltip.add(Component.literal(e.getEnergyStored() + " / " + e.getMaxEnergyStored() + " FE").withStyle(ChatFormatting.GRAY))
        );
    }

}
