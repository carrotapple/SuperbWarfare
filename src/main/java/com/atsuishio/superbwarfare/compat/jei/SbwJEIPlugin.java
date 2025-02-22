package com.atsuishio.superbwarfare.compat.jei;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class SbwJEIPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ModUtils.loc("jei_plugin");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addItemStackInfo(new ItemStack(ModItems.ANCIENT_CPU.get()), Component.translatable("jei.superbwarfare.ancient_cpu"));
        registration.addItemStackInfo(new ItemStack(ModItems.CHARGING_STATION.get()), Component.translatable("jei.superbwarfare.charging_station"));

        List<CraftingRecipe> specialCraftingRecipes = PotionMortarShellRecipeMaker.createRecipes();
        registration.addRecipes(RecipeTypes.CRAFTING, specialCraftingRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.POTION_MORTAR_SHELL.get(), (stack, context) -> {
            if (!stack.hasTag()) {
                return IIngredientSubtypeInterpreter.NONE;
            }
            Potion potionType = PotionUtils.getPotion(stack);
            String potionTypeString = potionType.getName("");
            StringBuilder stringBuilder = new StringBuilder(potionTypeString);
            List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);
            for (MobEffectInstance effect : effects) {
                stringBuilder.append(";").append(effect);
            }

            return stringBuilder.toString();
        });
    }
}
