package net.mcreator.superbwarfare.datagen;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                              CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ModUtils.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(Tags.Items.DUSTS).addTags(forgeTag("dusts/coal_coke"), forgeTag("dusts/tungsten"));
        this.tag(forgeTag("dusts/coal_coke")).add(ModItems.COAL_POWDER.get());
        this.tag(forgeTag("dusts/iron")).add(ModItems.IRON_POWDER.get());
        this.tag(forgeTag("dusts/tungsten")).add(ModItems.TUNGSTEN_POWDER.get());

        this.tag(Tags.Items.INGOTS).addTags(forgeTag("ingots/lead"), forgeTag("ingots/steel"), forgeTag("ingots/tungsten"));
        this.tag(forgeTag("ingots/lead")).add(ModItems.LEAD_INGOT.get());
        this.tag(forgeTag("ingots/steel")).add(ModItems.STEEL_INGOT.get());
        this.tag(forgeTag("ingots/tungsten")).add(ModItems.TUNGSTEN_INGOT.get());

        this.tag(Tags.Items.ORES).addTags(forgeTag("ores/lead"), forgeTag("ores/tungsten"));
        this.tag(forgeTag("ores/lead")).add(ModItems.GALENA_ORE.get(), ModItems.DEEPSLATE_GALENA_ORE.get());
        this.tag(forgeTag("ores/tungsten")).add(ModItems.SCHEELITE_ORE.get(), ModItems.DEEPSLATE_SCHEELITE_ORE.get());

        this.tag(Tags.Items.RAW_MATERIALS).addTags(forgeTag("raw_materials/lead"), forgeTag("raw_materials/tungsten"));
        this.tag(forgeTag("raw_materials/lead")).add(ModItems.GALENA.get());
        this.tag(forgeTag("raw_materials/tungsten")).add(ModItems.SCHEELITE.get());

        this.tag(Tags.Items.ORE_RATES_SINGULAR).add(ModItems.GALENA_ORE.get(), ModItems.DEEPSLATE_GALENA_ORE.get(),
                ModItems.SCHEELITE_ORE.get(), ModItems.DEEPSLATE_SCHEELITE_ORE.get());

        this.tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(ModItems.GALENA_ORE.get(), ModItems.SCHEELITE_ORE.get());
        this.tag(Tags.Items.ORES_IN_GROUND_STONE).add(ModItems.DEEPSLATE_GALENA_ORE.get(), ModItems.DEEPSLATE_SCHEELITE_ORE.get());

        this.tag(forgeTag("plates")).addTags(forgeTag("plates/copper"));
        this.tag(forgeTag("plates/copper")).add(ModItems.COPPER_PLATE.get());

        ModItems.GUNS.getEntries().forEach(registryObject -> this.tag(ModTags.Items.GUN).add(registryObject.get()));

    }

    private static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}
