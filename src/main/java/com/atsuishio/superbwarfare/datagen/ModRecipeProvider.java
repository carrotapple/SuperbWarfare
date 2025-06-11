package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        SpecialRecipeBuilder.special(ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.get()).save(pWriter, "potion_mortar_shell");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_ADD_AMMO_SERIALIZER.get()).save(pWriter, "ammo_box_add_ammo");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_EXTRACT_AMMO_SERIALIZER.get()).save(pWriter, "ammo_box_extract_ammo");

        copyBlueprint(pWriter, ModItems.TRACHELIUM_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.GLOCK_17_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MP_443_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.GLOCK_18_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.HUNTING_RIFLE_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_79_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.RPG_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.BOCEK_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_4_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.AA_12_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.HK_416_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.RPK_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.SKS_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.NTW_20_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MP_5_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.VECTOR_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MINIGUN_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MK_14_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.SENTINEL_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_60_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.SVD_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MARLIN_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_870_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_98B_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.AK_47_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.AK_12_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.DEVOTION_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.TASER_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_1911_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.QBZ_95_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.K_98_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MOSIN_NAGANT_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.JAVELIN_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.M_2_HB_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.SECONDARY_CATACLYSM_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.INSIDIOUS_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.AURELIA_SCEPTRE_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MK_42_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.MLE_1934_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.HPJ_11_BLUEPRINT.get());
        copyBlueprint(pWriter, ModItems.ANNIHILATOR_BLUEPRINT.get());
    }

    private static void copyBlueprint(Consumer<FinishedRecipe> pWriter, ItemLike result) {
        copySmithingTemplate(pWriter, result, Items.LAPIS_LAZULI);
    }
}
