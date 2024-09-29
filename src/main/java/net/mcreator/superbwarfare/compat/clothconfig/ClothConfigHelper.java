package net.mcreator.superbwarfare.compat.clothconfig;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.mcreator.superbwarfare.compat.clothconfig.client.DisplayClothConfig;
import net.mcreator.superbwarfare.compat.clothconfig.client.KillMessageClothConfig;
import net.mcreator.superbwarfare.compat.clothconfig.client.ReloadClothConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import org.jetbrains.annotations.Nullable;

public class ClothConfigHelper {

    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(Component.translatable("config.superbwarfare.title"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = root.entryBuilder();

        ReloadClothConfig.init(root, entryBuilder);
        KillMessageClothConfig.init(root, entryBuilder);
        DisplayClothConfig.init(root, entryBuilder);

        return root;
    }

    public static void registerScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigScreen(parent)));
    }

    public static Screen getConfigScreen(@Nullable Screen parent) {
        return ClothConfigHelper.getConfigBuilder().setParentScreen(parent).build();
    }
}
