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
        gunSmithing(writer, ModItems.M_79_BLUEPRINT.get(), GunRarity.RARE, Items.DISPENSER, ModItems.M_79.get());
        gunSmithing(writer, ModItems.RPG_BLUEPRINT.get(), GunRarity.RARE, Items.DISPENSER, ModItems.RPG.get());
        gunSmithing(writer, ModItems.BOCEK_BLUEPRINT.get(), GunRarity.EPIC, Items.BOW, ModItems.BOCEK.get());
        gunSmithing(writer, ModItems.M_4_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.M_4.get());
        gunSmithing(writer, ModItems.AA_12_BLUEPRINT.get(), GunRarity.LEGENDARY, Items.NETHERITE_INGOT, ModItems.AA_12.get());
        gunSmithing(writer, ModItems.HK_416_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.HK_416.get());
        gunSmithing(writer, ModItems.RPK_BLUEPRINT.get(), GunRarity.EPIC, ItemTags.LOGS, ModItems.RPK.get());
        gunSmithing(writer, ModItems.SKS_BLUEPRINT.get(), GunRarity.RARE, ItemTags.LOGS, ModItems.SKS.get());
        gunSmithing(writer, ModItems.NTW_20_BLUEPRINT.get(), GunRarity.LEGENDARY, Items.SPYGLASS, ModItems.NTW_20.get());
        gunSmithing(writer, ModItems.MP_5_BLUEPRINT.get(), GunRarity.RARE, Items.IRON_INGOT, ModItems.MP_5.get());
        gunSmithing(writer, ModItems.VECTOR_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.VECTOR.get());
        gunSmithing(writer, ModItems.MINIGUN_BLUEPRINT.get(), GunRarity.LEGENDARY, ModItems.MOTOR.get(), ModItems.MINIGUN.get());
        gunSmithing(writer, ModItems.MK_14_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.MK_14.get());
        gunSmithing(writer, ModItems.SENTINEL_BLUEPRINT.get(), GunRarity.EPIC, ModItems.CELL.get(), ModItems.SENTINEL.get());
        gunSmithing(writer, ModItems.M_60_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.M_60.get());
        gunSmithing(writer, ModItems.SVD_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.SVD.get());
        gunSmithing(writer, ModItems.MARLIN_BLUEPRINT.get(), GunRarity.COMMON, ItemTags.LOGS, ModItems.MARLIN.get());
        gunSmithing(writer, ModItems.M_870_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.M_870.get());
        gunSmithing(writer, ModItems.M_98B_BLUEPRINT.get(), GunRarity.EPIC, Items.SPYGLASS, ModItems.M_98B.get());
        gunSmithing(writer, ModItems.AK_47_BLUEPRINT.get(), GunRarity.RARE, ItemTags.LOGS, ModItems.AK_47.get());
        gunSmithing(writer, ModItems.AK_12_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.AK_12.get());
        gunSmithing(writer, ModItems.DEVOTION_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.DEVOTION.get());
        gunSmithing(writer, ModItems.TASER_BLUEPRINT.get(), GunRarity.COMMON, Items.YELLOW_CONCRETE, ModItems.TASER.get());
        gunSmithing(writer, ModItems.M_1911_BLUEPRINT.get(), GunRarity.COMMON, ModTags.Items.INGOTS_STEEL, ModItems.M_1911.get());
        gunSmithing(writer, ModItems.QBZ_95_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.QBZ_95.get());
        gunSmithing(writer, ModItems.K_98_BLUEPRINT.get(), GunRarity.RARE, ItemTags.LOGS, ModItems.K_98.get());
        gunSmithing(writer, ModItems.MOSIN_NAGANT_BLUEPRINT.get(), GunRarity.RARE, ItemTags.LOGS, ModItems.MOSIN_NAGANT.get());
        gunSmithing(writer, ModItems.JAVELIN_BLUEPRINT.get(), GunRarity.LEGENDARY, ModItems.ANCIENT_CPU.get(), ModItems.JAVELIN.get());
//        gunSmithing(writer, ModItems.M_2_HB_BLUEPRINT.get(), GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.M_2_HB.get());
        gunSmithing(writer, ModItems.SECONDARY_CATACLYSM_BLUEPRINT.get(), GunRarity.LEGENDARY, ModItems.KNIFE.get(), ModItems.SECONDARY_CATACLYSM.get());
        gunSmithing(writer, ModItems.INSIDIOUS_BLUEPRINT.get(), GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.INSIDIOUS.get());
        gunSmithing(writer, ModItems.AURELIA_SCEPTRE_BLUEPRINT.get(), GunRarity.LEGENDARY, Items.END_CRYSTAL, ModItems.AURELIA_SCEPTRE.get());

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
