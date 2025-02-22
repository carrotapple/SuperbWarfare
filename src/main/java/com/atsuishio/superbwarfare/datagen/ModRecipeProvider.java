package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.init.ModRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        SpecialRecipeBuilder.special(ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.get()).save(pWriter, "potion_mortar_shell");
    }
}
