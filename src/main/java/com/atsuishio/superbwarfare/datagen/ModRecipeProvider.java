package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModRecipes;
import com.atsuishio.superbwarfare.init.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        SpecialRecipeBuilder.special(ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.get()).save(writer, "potion_mortar_shell");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_ADD_AMMO_SERIALIZER.get()).save(writer, "ammo_box_add_ammo");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_EXTRACT_AMMO_SERIALIZER.get()).save(writer, "ammo_box_extract_ammo");

        gunSmithing(writer, ModItems.TRACHELIUM_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.TRACHELIUM.get());
        gunSmithing(writer, ModItems.GLOCK_17_BLUEPRINT.get(), GunRarity.COMMON, Items.IRON_INGOT, ModItems.GLOCK_17.get());
        gunSmithing(writer, ModItems.MP_443_BLUEPRINT.get(), GunRarity.COMMON, Items.IRON_INGOT, ModItems.MP_443.get());
        gunSmithing(writer, ModItems.GLOCK_18_BLUEPRINT.get(), GunRarity.RARE, Items.GOLD_INGOT, ModItems.GLOCK_18.get());
        gunSmithing(writer, ModItems.HUNTING_RIFLE_BLUEPRINT.get(), GunRarity.RARE, ItemTags.LOGS, ModItems.HUNTING_RIFLE.get());

        gunSmithing(writer, ModItems.SENTINEL_BLUEPRINT.get(), GunRarity.EPIC, ModItems.CELL.get(), ModItems.SENTINEL.get());

        copyBlueprint(writer, ModItems.TRACHELIUM_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.GLOCK_17_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MP_443_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.GLOCK_18_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.HUNTING_RIFLE_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_79_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.RPG_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.BOCEK_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_4_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.AA_12_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.HK_416_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.RPK_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.SKS_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.NTW_20_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MP_5_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.VECTOR_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MINIGUN_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MK_14_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.SENTINEL_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_60_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.SVD_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MARLIN_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_870_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_98B_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.AK_47_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.AK_12_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.DEVOTION_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.TASER_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_1911_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.QBZ_95_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.K_98_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MOSIN_NAGANT_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.JAVELIN_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.M_2_HB_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.SECONDARY_CATACLYSM_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.INSIDIOUS_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.AURELIA_SCEPTRE_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MK_42_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.MLE_1934_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.HPJ_11_BLUEPRINT.get());
        copyBlueprint(writer, ModItems.ANNIHILATOR_BLUEPRINT.get());
    }

    public static void copyBlueprint(Consumer<FinishedRecipe> writer, ItemLike result) {
        copySmithingTemplate(writer, result, Items.LAPIS_LAZULI);
    }

    public static void gunSmithing(Consumer<FinishedRecipe> writer, ItemLike blueprint, GunRarity rarity, TagKey<Item> tagKey, Item pResultItem) {
        gunSmithing(writer, blueprint, rarity, Ingredient.of(tagKey), pResultItem);
    }

    public static void gunSmithing(Consumer<FinishedRecipe> writer, ItemLike blueprint, GunRarity rarity, ItemLike ingredient, Item pResultItem) {
        gunSmithing(writer, blueprint, rarity, Ingredient.of(ingredient), pResultItem);
    }

    public static void gunSmithing(Consumer<FinishedRecipe> writer, ItemLike blueprint, GunRarity rarity, Ingredient ingredient, Item pResultItem) {
        ItemLike pack = switch (rarity) {
            case COMMON -> ModItems.COMMON_MATERIAL_PACK.get();
            case RARE -> ModItems.RARE_MATERIAL_PACK.get();
            case EPIC -> ModItems.EPIC_MATERIAL_PACK.get();
            case LEGENDARY -> ModItems.LEGENDARY_MATERIAL_PACK.get();
        };

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(blueprint),
                        Ingredient.of(pack),
                        ingredient,
                        RecipeCategory.COMBAT,
                        pResultItem)
                .unlocks(getHasName(blueprint), has(blueprint))
                .save(writer, Mod.loc(getItemName(pResultItem) + "_smithing"));
    }

    public enum GunRarity {
        COMMON,
        RARE,
        EPIC,
        LEGENDARY
    }
}
