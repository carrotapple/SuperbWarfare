package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.SANDBAG.get());
        this.dropSelf(ModBlocks.BARBED_WIRE.get());
        this.dropSelf(ModBlocks.JUMP_PAD.get());
        this.dropSelf(ModBlocks.DRAGON_TEETH.get());
        this.dropSelf(ModBlocks.REFORGING_TABLE.get());
        this.dropSelf(ModBlocks.LEAD_BLOCK.get());
        this.dropSelf(ModBlocks.STEEL_BLOCK.get());
        this.dropSelf(ModBlocks.TUNGSTEN_BLOCK.get());
        this.dropSelf(ModBlocks.CEMENTED_CARBIDE_BLOCK.get());
        this.dropSelf(ModBlocks.SILVER_BLOCK.get());

        this.add(ModBlocks.GALENA_ORE.get(), this.createOreDrop(ModBlocks.GALENA_ORE.get(), ModItems.GALENA.get()));
        this.add(ModBlocks.SCHEELITE_ORE.get(), this.createOreDrop(ModBlocks.SCHEELITE_ORE.get(), ModItems.SCHEELITE.get()));
        this.add(ModBlocks.SILVER_ORE.get(), this.createOreDrop(ModBlocks.SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
        this.add(ModBlocks.DEEPSLATE_GALENA_ORE.get(), this.createOreDrop(ModBlocks.DEEPSLATE_GALENA_ORE.get(), ModItems.GALENA.get()));
        this.add(ModBlocks.DEEPSLATE_SCHEELITE_ORE.get(), this.createOreDrop(ModBlocks.DEEPSLATE_SCHEELITE_ORE.get(), ModItems.SCHEELITE.get()));
        this.add(ModBlocks.DEEPSLATE_SILVER_ORE.get(), this.createOreDrop(ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRY.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
