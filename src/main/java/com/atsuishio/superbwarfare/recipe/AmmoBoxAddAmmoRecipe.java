package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModRecipes;
import com.atsuishio.superbwarfare.item.common.ammo.AmmoBox;
import com.atsuishio.superbwarfare.item.common.ammo.AmmoSupplierItem;
import com.atsuishio.superbwarfare.tools.AmmoType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AmmoBoxAddAmmoRecipe extends CustomRecipe {

    public AmmoBoxAddAmmoRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, @NotNull Level pLevel) {
        var hasAmmoBox = false;
        var hasAmmo = false;

        for (var item : pContainer.getItems()) {
            if (item.getItem() instanceof AmmoBox) {
                if (hasAmmoBox) return false;
                hasAmmoBox = true;
            } else if (item.getItem() instanceof AmmoSupplierItem) {
                hasAmmo = true;
            } else if (!item.isEmpty()) {
                return false;
            }
        }

        return hasAmmoBox && hasAmmo;
    }


    private void addAmmo(HashMap<AmmoType, Integer> map, AmmoType type, int count) {
        map.put(type, map.getOrDefault(type, 0) + count);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        var map = new HashMap<AmmoType, Integer>();
        var ammoBox = ItemStack.EMPTY;

        for (var item : pContainer.getItems()) {
            if (item.getItem() instanceof AmmoSupplierItem ammoSupplier) {
                addAmmo(map, ammoSupplier.type, ammoSupplier.ammoToAdd);
            } else if (item.getItem() instanceof AmmoBox) {
                ammoBox = item.copy();
                for (var type : AmmoType.values()) {
                    addAmmo(map, type, type.get(item));
                }
            }
        }

        for (var type : AmmoType.values()) {
            type.set(ammoBox, map.getOrDefault(type, 0));
        }

        return ammoBox;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.AMMO_BOX_ADD_AMMO_SERIALIZER.get();
    }
}
