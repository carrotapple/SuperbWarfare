package net.mcreator.superbwarfare.perk;

import net.mcreator.superbwarfare.init.ModPerks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class PerkHelper {
    private static final String TAG_PERK_ID = "id";
    private static final String TAG_PERK_LEVEL = "level";
    private static final String TAG_PERK = "Perks";

    public static CompoundTag makePerk(@Nullable ResourceLocation pId, int pLevel) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString(TAG_PERK_ID, String.valueOf(pId));
        compoundtag.putShort(TAG_PERK_LEVEL, (short) pLevel);
        return compoundtag;
    }

    public static CompoundTag setPerkLevel(CompoundTag pCompound, int pLevel) {
        pCompound.putShort(TAG_PERK_LEVEL, (short) pLevel);
        return pCompound;
    }

    public static int getPerkLevel(CompoundTag pCompound) {
        return Mth.clamp(pCompound.getInt(TAG_PERK_LEVEL), 0, 255);
    }

    @Nullable
    public static ResourceLocation getPerkId(CompoundTag pCompoundTag) {
        return ResourceLocation.tryParse(pCompoundTag.getString(TAG_PERK_ID));
    }

    @Nullable
    public static ResourceLocation getPerkId(Perk perk) {
        return ModPerks.PERKS.getEntries().stream()
                .filter(p -> p.get().type == perk.type)
                .findFirst()
                .map(RegistryObject::getId)
                .orElse(null);
    }

    public static int getItemPerkLevel(Perk perk, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        ResourceLocation perkId = getPerkId(perk);
        ListTag perkTags = getPerkTags(stack);

        for (int i = 0; i < perkTags.size(); ++i) {
            CompoundTag compoundtag = perkTags.getCompound(i);
            ResourceLocation tagPerkId = getPerkId(compoundtag);
            if (tagPerkId != null && tagPerkId.equals(perkId)) {
                return getPerkLevel(compoundtag);
            }
        }

        return 0;
    }

    public static ListTag getPerkTags(ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().getList(TAG_PERK, 10) : new ListTag();
    }

}
