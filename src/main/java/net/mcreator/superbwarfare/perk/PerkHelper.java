package net.mcreator.superbwarfare.perk;

import net.mcreator.superbwarfare.init.ModPerks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class PerkHelper {
    private static final String TAG_PERK_ID = "id";
    private static final String TAG_PERK_LEVEL = "level";
    private static final String TAG_PERK = "Perks";

    /**
     * 把一个Perk封装成nbt进行存储
     */
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
                .filter(p -> p.get().descriptionId.equals(perk.descriptionId))
                .findFirst()
                .map(RegistryObject::getId)
                .orElse(null);
    }

    public static int getItemPerkLevel(Perk perk, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        var tag = stack.getTag();
        if (tag == null) {
            return 0;
        }

        var tagPerk = tag.getCompound(TAG_PERK);
        if (!tagPerk.contains(perk.type.getName())) {
            return 0;
        }

        var pt = tagPerk.getCompound(perk.type.getName());
        ResourceLocation id = getPerkId(perk);
        if (id == null) {
            return 0;
        }

        if (!pt.getString(TAG_PERK_ID).equals(makeId(id))) {
            return 0;
        }

        return getPerkLevel(getPerkTag(stack, perk.type));
    }

    public static CompoundTag getPerkTag(ItemStack stack, Perk.Type type) {
        var tag = stack.getTag();
        if (tag == null) return new CompoundTag();

        var tagPerk = tag.getCompound(TAG_PERK);
        if (!tagPerk.contains(type.getName())) return new CompoundTag();
        return tagPerk.getCompound(type.getName());
    }

    public static void setPerk(ItemStack stack, Perk perk, int level) {
        var tag = stack.getTag();
        if (tag == null) {
            tag = new CompoundTag();
        }

        var perkTag = tag.getCompound(TAG_PERK);
        if (perkTag.isEmpty()) {
            perkTag = new CompoundTag();
        }

        perkTag.put(perk.type.getName(), makePerk(getPerkId(perk), level));
        stack.addTagElement(TAG_PERK, perkTag);
    }

    public static void setPerk(ItemStack stack, Perk perk) {
        setPerk(stack, perk, 1);
    }

    @Nullable
    public static Perk getPerkByType(ItemStack stack, Perk.Type type) {
        var tag = stack.getTag();
        if (tag == null) {
            return null;
        }

        var tagPerk = tag.getCompound(TAG_PERK);
        if (!tagPerk.contains(type.getName())) {
            return null;
        }

        return ModPerks.PERKS.getEntries().stream()
                .filter(p -> makeId(p.getId()).equals(tagPerk.getCompound(type.getName()).getString(TAG_PERK_ID)))
                .findFirst()
                .map(RegistryObject::get)
                .orElse(null);
    }

    public static String makeId(ResourceLocation resourceLocation) {
        return resourceLocation.getNamespace() + ":" + resourceLocation.getPath();
    }

}
