package com.atsuishio.superbwarfare.compat.jei;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class SbwJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ModUtils.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addItemStackInfo(new ItemStack(ModItems.ANCIENT_CPU.get()), Component.translatable("jei.superbwarfare.ancient_cpu"));
        registration.addItemStackInfo(new ItemStack(ModItems.CHARGING_STATION.get()), Component.translatable("jei.superbwarfare.charging_station"));
    }
}
