package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ModUtils.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.GALENA_ORE.get(), ModBlocks.SCHEELITE_ORE.get(),
                ModBlocks.DEEPSLATE_GALENA_ORE.get(), ModBlocks.DEEPSLATE_SCHEELITE_ORE.get(), ModBlocks.DRAGON_TEETH.get(),
                ModBlocks.SILVER_ORE.get(), ModBlocks.DEEPSLATE_SILVER_ORE.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.BARBED_WIRE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.GALENA_ORE.get(), ModBlocks.SCHEELITE_ORE.get(),
                ModBlocks.DEEPSLATE_GALENA_ORE.get(), ModBlocks.DEEPSLATE_SCHEELITE_ORE.get(), ModBlocks.DRAGON_TEETH.get(),
                ModBlocks.REFORGING_TABLE.get(), ModBlocks.LEAD_BLOCK.get(), ModBlocks.STEEL_BLOCK.get(), ModBlocks.TUNGSTEN_BLOCK.get(),
                ModBlocks.CEMENTED_CARBIDE_BLOCK.get(), ModBlocks.SILVER_ORE.get(), ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModBlocks.SILVER_BLOCK.get(), ModBlocks.JUMP_PAD.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SANDBAG.get());

    }
}
