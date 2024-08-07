package net.mcreator.superbwarfare.perk;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class PerkHelper {
    private static final String TAG_PERK_ID = "id";
    private static final String TAG_PERK_LEVEL = "level";

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
        return ResourceLocation.tryParse(pCompoundTag.getString("id"));
    }

    // TODO 实现通过注册表找到对应perk并返回对应的resourcelocation
//    @Nullable
//    public static ResourceLocation getPerkId(Perk perk) {
//        return
//    }

    public static int getItemPerkLevel(Perk perk, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
//            ResourceLocation resourcelocation = getEnchantmentId(pEnchantment);
//            ListTag listtag = getPerkTags(stack);
//
//            for(int i = 0; i < listtag.size(); ++i) {
//                CompoundTag compoundtag = listtag.getCompound(i);
//                ResourceLocation resourcelocation1 = getEnchantmentId(compoundtag);
//                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
//                    return getEnchantmentLevel(compoundtag);
//                }
//            }

            return 0;
        }
    }

    public static ListTag getPerkTags(ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().getList("Perks", 10) : new ListTag();
    }


}
