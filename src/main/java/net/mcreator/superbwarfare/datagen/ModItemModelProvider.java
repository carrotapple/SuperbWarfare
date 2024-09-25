package net.mcreator.superbwarfare.datagen;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModBlocks;
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

        simpleItem(ModItems.MORTAR_SHELLS);

        // misc
        simpleItem(ModItems.ANCIENT_CPU);
        simpleItem(ModItems.PROPELLER);
        simpleItem(ModItems.MOTOR);
        simpleItem(ModItems.DRONE);

        simpleItem(ModItems.TARGET_DEPLOYER);
        simpleItem(ModItems.MORTAR_DEPLOYER);
        simpleItem(ModItems.MORTAR_BARREL);
        simpleItem(ModItems.MORTAR_BASE_PLATE);
        simpleItem(ModItems.MORTAR_BIPOD);
        simpleItem(ModItems.SEEKER);
        simpleItem(ModItems.MISSILE_ENGINE);
        simpleItem(ModItems.FUSEE);
        simpleItem(ModItems.PRIMER);
        simpleItem(ModItems.AP_HEAD);
        simpleItem(ModItems.HE_HEAD);
        simpleItem(ModItems.COPPER_PLATE);
        simpleItem(ModItems.STEEL_INGOT);
        simpleItem(ModItems.LEAD_INGOT);
        simpleItem(ModItems.TUNGSTEN_INGOT);
        simpleItem(ModItems.CEMENTED_CARBIDE_INGOT);
        simpleItem(ModItems.HIGH_ENERGY_EXPLOSIVES);
        simpleItem(ModItems.GRAIN);
        simpleItem(ModItems.IRON_POWDER);
        simpleItem(ModItems.TUNGSTEN_POWDER);
        simpleItem(ModItems.COAL_POWDER);
        simpleItem(ModItems.COAL_IRON_POWDER);
        simpleItem(ModItems.RAW_CEMENTED_CARBIDE_POWDER);
        simpleItem(ModItems.GALENA);
        simpleItem(ModItems.SCHEELITE);
        simpleItem(ModItems.DOG_TAG);
        simpleItem(ModItems.TRANSCRIPT);
        simpleItem(ModItems.RAW_SILVER);
        simpleItem(ModItems.SILVER_INGOT);
        simpleItem(ModItems.CROWBAR);

        simpleItem(ModItems.TUNGSTEN_ROD);
        simpleItem(ModItems.IRON_BARREL);
        simpleItem(ModItems.IRON_ACTION);
        simpleItem(ModItems.IRON_TRIGGER);
        simpleItem(ModItems.IRON_SPRING);
        simpleItem(ModItems.STEEL_BARREL);
        simpleItem(ModItems.STEEL_ACTION);
        simpleItem(ModItems.STEEL_TRIGGER);
        simpleItem(ModItems.STEEL_SPRING);
        simpleItem(ModItems.CEMENTED_CARBIDE_BARREL);
        simpleItem(ModItems.CEMENTED_CARBIDE_ACTION);
        simpleItem(ModItems.CEMENTED_CARBIDE_TRIGGER);
        simpleItem(ModItems.CEMENTED_CARBIDE_SPRING);
        simpleItem(ModItems.NETHERITE_BARREL);
        simpleItem(ModItems.NETHERITE_ACTION);
        simpleItem(ModItems.NETHERITE_TRIGGER);
        simpleItem(ModItems.NETHERITE_SPRING);

        simpleItem(ModItems.COMMON_MATERIAL_PACK);
        simpleItem(ModItems.RARE_MATERIAL_PACK);
        simpleItem(ModItems.EPIC_MATERIAL_PACK);
        simpleItem(ModItems.LEGENDARY_MATERIAL_PACK);

        // armor

        simpleItem(ModItems.RU_HELMET_6B47);
        simpleItem(ModItems.RU_CHEST_6B43);

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

        // blocks
        evenSimplerBlockItem(ModBlocks.BARBED_WIRE);
        evenSimplerBlockItem(ModBlocks.JUMP_PAD);
        evenSimplerBlockItem(ModBlocks.REFORGING_TABLE);
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
