package net.mcreator.superbwarfare.datagen;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"ConstantConditions", "SameParameterValue"})
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModUtils.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.BARBED_WIRE.get(), new ModelFile.UncheckedModelFile(modLoc("block/barbed_wire")));
        horizontalBlock(ModBlocks.JUMP_PAD.get(), new ModelFile.UncheckedModelFile(modLoc("block/jump_pad")));
        horizontalBlock(ModBlocks.REFORGING_TABLE.get(), new ModelFile.UncheckedModelFile(modLoc("block/reforging_table")));

        blockWithItem(ModBlocks.GALENA_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_GALENA_ORE);
        blockWithItem(ModBlocks.SCHEELITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SCHEELITE_ORE);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ModUtils.MODID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
