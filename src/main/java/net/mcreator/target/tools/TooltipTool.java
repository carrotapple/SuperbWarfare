package net.mcreator.target.tools;

import net.mcreator.target.entity.DroneEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
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

    public static void addGunTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.literal(""));

        double damage = (ItemNBTTool.getDouble(stack, "damage", 0) +
                ItemNBTTool.getDouble(stack, "add_damage", 0))
                * ItemNBTTool.getDouble(stack, "damageadd", 1);

        tooltip.add(Component.translatable("des.target.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.GREEN)));

        addLevelTips(tooltip, stack);
    }

    public static void addShotgunTips(List<Component> tooltip, ItemStack stack, int count) {
        tooltip.add(Component.literal(""));

        double damage = (ItemNBTTool.getDouble(stack, "damage", 0) +
                ItemNBTTool.getDouble(stack, "add_damage", 0))
                * ItemNBTTool.getDouble(stack, "damageadd", 1);

        tooltip.add(Component.translatable("des.target.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(damage) + " * " + count).withStyle(ChatFormatting.GREEN)));

        addLevelTips(tooltip, stack);
    }

    private static void addLevelTips(List<Component> tooltip, ItemStack stack) {
        int level = (int) ItemNBTTool.getDouble(stack, "level", 0);
        double rate = ItemNBTTool.getDouble(stack, "damagenow", 0) / ItemNBTTool.getDouble(stack, "damageneed", 1);

        ChatFormatting formatting;
        if (level < 4) {
            formatting = ChatFormatting.WHITE;
        } else if (level < 6) {
            formatting = ChatFormatting.AQUA;
        } else if (level < 8) {
            formatting = ChatFormatting.LIGHT_PURPLE;
        } else if (level < 10) {
            formatting = ChatFormatting.GOLD;
        } else {
            formatting = ChatFormatting.RED;
        }

        tooltip.add(Component.translatable("des.target.tips.level").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(level + " " + new DecimalFormat("##.##").format(rate * 100) + "%").withStyle(formatting).withStyle(ChatFormatting.BOLD)));

    }

    public static void addBocekTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.literal(""));

        double total = ItemNBTTool.getDouble(stack, "damage", 0) * ItemNBTTool.getDouble(stack, "damageadd", 1);

        tooltip.add(Component.translatable("des.target.tips.damage").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(total * 0.1) + " * 10").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" / ").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##.#").format(total)).withStyle(ChatFormatting.GREEN)));

        addLevelTips(tooltip, stack);
    }

    public static void addSentinelTips(List<Component> tooltip, ItemStack stack) {
        tooltip.add(Component.literal(""));

        AtomicBoolean flag = new AtomicBoolean(false);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> flag.set(e.getEnergyStored() > 0)
        );

        if (flag.get()) {
            double damage = (ItemNBTTool.getDouble(stack, "damage", 0) +
                    ItemNBTTool.getDouble(stack, "add_damage", 0))
                    * ItemNBTTool.getDouble(stack, "damageadd", 1);

            tooltip.add(Component.translatable("des.target.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)));
        } else {
            double damage = ItemNBTTool.getDouble(stack, "damage", 0) * ItemNBTTool.getDouble(stack, "damageadd", 1);

            tooltip.add(Component.translatable("des.target.tips.damage").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(new DecimalFormat("##.#").format(damage)).withStyle(ChatFormatting.GREEN)));
        }

        addLevelTips(tooltip, stack);

        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                e -> tooltip.add(Component.literal(e.getEnergyStored() + " / " + e.getMaxEnergyStored() + " FE").withStyle(ChatFormatting.GRAY))
        );
    }

    public static void addMonitorTips(List<Component> tooltip, String id) {
        if (id.equals("none")) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        DroneEntity entity = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(256))
                .stream().filter(e -> e.getStringUUID().equals(id)).findFirst().orElse(null);

        if (entity == null) return;

        tooltip.add(Component.literal(""));

        tooltip.add(Component.literal(player.distanceTo(entity) + " M").withStyle(ChatFormatting.GRAY));
    }
}
