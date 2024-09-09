package net.mcreator.superbwarfare.datagen;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"ConstantConditions", "UnusedReturnValue", "SameParameterValue", "unused"})
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModUtils.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.EMPTY_PERK, "perk/");

        // blueprints
        blueprintItem(ModItems.TRACHELIUM_BLUEPRINT);
        blueprintItem(ModItems.GLOCK_17_BLUEPRINT);
        blueprintItem(ModItems.GLOCK_18_BLUEPRINT);
        blueprintItem(ModItems.HUNTING_RIFLE_BLUEPRINT);
        blueprintItem(ModItems.M_79_BLUEPRINT);
        blueprintItem(ModItems.RPG_BLUEPRINT);
        blueprintItem(ModItems.BOCEK_BLUEPRINT);
        blueprintItem(ModItems.M_4_BLUEPRINT);
        blueprintItem(ModItems.AA_12_BLUEPRINT);
        blueprintItem(ModItems.HK_416_BLUEPRINT);
        blueprintItem(ModItems.RPK_BLUEPRINT);
        blueprintItem(ModItems.SKS_BLUEPRINT);
        blueprintItem(ModItems.NTW_20_BLUEPRINT);
        blueprintItem(ModItems.VECTOR_BLUEPRINT);
        blueprintItem(ModItems.MINIGUN_BLUEPRINT);
        blueprintItem(ModItems.MK_14_BLUEPRINT);
        blueprintItem(ModItems.SENTINEL_BLUEPRINT);
        blueprintItem(ModItems.M_60_BLUEPRINT);
        blueprintItem(ModItems.SVD_BLUEPRINT);
        blueprintItem(ModItems.MARLIN_BLUEPRINT);
        blueprintItem(ModItems.M_870_BLUEPRINT);
        blueprintItem(ModItems.M_98B_BLUEPRINT);
        blueprintItem(ModItems.AK_47_BLUEPRINT);
        blueprintItem(ModItems.DEVOTION_BLUEPRINT);
        blueprintItem(ModItems.TASER_BLUEPRINT);
        blueprintItem(ModItems.M_1911_BLUEPRINT);
        blueprintItem(ModItems.QBZ_95_BLUEPRINT);
        blueprintItem(ModItems.K_98_BLUEPRINT);
        blueprintItem(ModItems.MOSIN_NAGANT_BLUEPRINT);
        blueprintItem(ModItems.JAVELIN_BLUEPRINT);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return simpleItem(item, "");
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String location) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(ModUtils.MODID, "item/" + location + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String location, String renderType) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(ModUtils.MODID, "item/" + location + item.getId().getPath())).renderType(renderType);
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(ModUtils.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    private ItemModelBuilder blueprintItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(ModUtils.MODID, "item/gun_blueprint"));
    }

}
