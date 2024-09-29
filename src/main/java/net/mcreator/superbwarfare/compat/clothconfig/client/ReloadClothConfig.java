package net.mcreator.superbwarfare.compat.clothconfig.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.mcreator.superbwarfare.config.client.ReloadConfig;
import net.minecraft.network.chat.Component;

public class ReloadClothConfig {

    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.reload"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.reload.empty_auto_reload"), ReloadConfig.EMPTY_AUTO_RELOAD.get())
                .setDefaultValue(true)
                .setSaveConsumer(ReloadConfig.EMPTY_AUTO_RELOAD::set)
                .setTooltip(Component.translatable("config.superbwarfare.client.reload.empty_auto_reload.des")).build()
        );
    }
}
