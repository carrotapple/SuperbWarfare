package net.mcreator.superbwarfare.compat.clothconfig.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.minecraft.network.chat.Component;

public class DisplayClothConfig {

    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.display"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.kill_indication"), DisplayConfig.KILL_INDICATION.get())
                .setDefaultValue(true)
                .setSaveConsumer(DisplayConfig.KILL_INDICATION::set)
                .setTooltip(Component.translatable("config.superbwarfare.client.display.kill_indication.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.global_indication"), DisplayConfig.GLOBAL_INDICATION.get())
                .setDefaultValue(false)
                .setSaveConsumer(DisplayConfig.GLOBAL_INDICATION::set)
                .setTooltip(Component.translatable("config.superbwarfare.client.display.global_indication.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.ammo_hud"), DisplayConfig.AMMO_HUD.get())
                .setDefaultValue(true)
                .setSaveConsumer(DisplayConfig.AMMO_HUD::set)
                .setTooltip(Component.translatable("config.superbwarfare.client.display.ammo_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.float_cross_hair"), DisplayConfig.FLOAT_CROSS_HAIR.get())
                .setDefaultValue(true)
                .setSaveConsumer(DisplayConfig.FLOAT_CROSS_HAIR::set)
                .setTooltip(Component.translatable("config.superbwarfare.client.display.float_cross_hair.des"))
                .build()
        );
    }
}
