package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.*;
import net.mcreator.superbwarfare.item.common.BlueprintItem;
import net.mcreator.superbwarfare.item.common.MaterialPack;
import net.mcreator.superbwarfare.item.common.ammo.*;
import net.mcreator.superbwarfare.item.gun.*;
import net.mcreator.superbwarfare.tools.RarityTool;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModItems {

    /**
     * guns
     */
    public static final DeferredRegister<Item> GUNS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    public static final RegistryObject<Item> TASER = GUNS.register("taser", Taser::new);
    public static final RegistryObject<Item> GLOCK_17 = GUNS.register("glock_17", Glock17Item::new);
    public static final RegistryObject<Item> GLOCK_18 = GUNS.register("glock_18", Glock18Item::new);
    public static final RegistryObject<Item> M_1911 = GUNS.register("m_1911", M1911Item::new);
    public static final RegistryObject<Item> ABEKIRI = GUNS.register("abekiri", Abekiri::new);
    public static final RegistryObject<Item> TRACHELIUM = GUNS.register("trachelium", Trachelium::new);
    public static final RegistryObject<Item> VECTOR = GUNS.register("vector", VectorItem::new);
    public static final RegistryObject<Item> AK_47 = GUNS.register("ak_47", AK47Item::new);
    public static final RegistryObject<Item> SKS = GUNS.register("sks", SksItem::new);
    public static final RegistryObject<Item> M_4 = GUNS.register("m_4", M4Item::new);
    public static final RegistryObject<Item> HK_416 = GUNS.register("hk_416", Hk416Item::new);
    public static final RegistryObject<Item> QBZ_95 = GUNS.register("qbz_95", Qbz95Item::new);
    public static final RegistryObject<Item> MK_14 = GUNS.register("mk_14", Mk14Item::new);
    public static final RegistryObject<Item> MARLIN = GUNS.register("marlin", MarlinItem::new);
    public static final RegistryObject<Item> SVD = GUNS.register("svd", SvdItem::new);
    public static final RegistryObject<Item> M_98B = GUNS.register("m_98b", M98bItem::new);
    public static final RegistryObject<Item> SENTINEL = GUNS.register("sentinel", SentinelItem::new);
    public static final RegistryObject<Item> HUNTING_RIFLE = GUNS.register("hunting_rifle", HuntingRifle::new);
    public static final RegistryObject<Item> NTW_20 = GUNS.register("ntw_20", Ntw20::new);
    public static final RegistryObject<Item> M_870 = GUNS.register("m_870", M870Item::new);
    public static final RegistryObject<Item> AA_12 = GUNS.register("aa_12", Aa12Item::new);
    public static final RegistryObject<Item> DEVOTION = GUNS.register("devotion", Devotion::new);
    public static final RegistryObject<Item> RPK = GUNS.register("rpk", RpkItem::new);
    public static final RegistryObject<Item> M_60 = GUNS.register("m_60", M60Item::new);
    public static final RegistryObject<Item> MINIGUN = GUNS.register("minigun", Minigun::new);
    public static final RegistryObject<Item> M_79 = GUNS.register("m_79", M79Item::new);
    public static final RegistryObject<Item> RPG = GUNS.register("rpg", RpgItem::new);
    public static final RegistryObject<Item> BOCEK = GUNS.register("bocek", BocekItem::new);

    /**
     * Ammo
     */
    public static final DeferredRegister<Item> AMMO = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    public static final RegistryObject<Item> HANDGUN_AMMO = AMMO.register("handgun_ammo", HandgunAmmo::new);
    public static final RegistryObject<Item> RIFLE_AMMO = AMMO.register("rifle_ammo", RifleAmmo::new);
    public static final RegistryObject<Item> SNIPER_AMMO = AMMO.register("sniper_ammo", SniperAmmo::new);
    public static final RegistryObject<Item> SHOTGUN_AMMO = AMMO.register("shotgun_ammo", ShotgunAmmo::new);
    public static final RegistryObject<Item> HANDGUN_AMMO_BOX = AMMO.register("handgun_ammo_box", HandgunAmmoBox::new);
    public static final RegistryObject<Item> RIFLE_AMMO_BOX = AMMO.register("rifle_ammo_box", RifleAmmoBox::new);
    public static final RegistryObject<Item> SNIPER_AMMO_BOX = AMMO.register("sniper_ammo_box", SniperAmmoBox::new);
    public static final RegistryObject<Item> SHOTGUN_AMMO_BOX = AMMO.register("shotgun_ammo_box", ShotgunAmmoBox::new);
    public static final RegistryObject<Item> CREATIVE_AMMO_BOX = AMMO.register("creative_ammo_box", CreativeAmmoBox::new);
    public static final RegistryObject<Item> TASER_ELECTRODE = AMMO.register("taser_electrode", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRENADE_40MM = AMMO.register("grenade_40mm", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MORTAR_SHELLS = AMMO.register("mortar_shells", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROCKET = AMMO.register("rocket", Rocket::new);
    public static final RegistryObject<Item> HE_5_INCHES = AMMO.register("he_5_inches", He5Inches::new);
    public static final RegistryObject<Item> AP_5_INCHES = AMMO.register("ap_5_inches", Ap5Inches::new);
    public static final RegistryObject<Item> HAND_GRENADE = AMMO.register("hand_grenade", HandGrenade::new);
    public static final RegistryObject<Item> RGO_GRENADE = AMMO.register("rgo_grenade", RgoGrenade::new);
    public static final RegistryObject<Item> CLAYMORE_MINE = AMMO.register("claymore_mine", ClaymoreMine::new);

    /**
     * items
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    public static final RegistryObject<Item> SENPAI_SPAWN_EGG = ITEMS.register("senpai_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SENPAI, -11584987, -14014413, new Item.Properties()));
    public static final RegistryObject<Item> MK_42_SPAWN_EGG = ITEMS.register("mk42_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.MK_42, -8348258, -2630437, new Item.Properties()));
    public static final RegistryObject<Item> MLE_1934_SPAWN_EGG = ITEMS.register("mle1934_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.MLE_1934, -3355444, -14146005, new Item.Properties()));
    public static final RegistryObject<Item> DRONE = ITEMS.register("drone", Drone::new);

    public static final RegistryObject<Item> MONITOR = ITEMS.register("monitor", Monitor::new);
    public static final RegistryObject<Item> TARGET_DEPLOYER = ITEMS.register("target_deployer", TargetDeployer::new);
    public static final RegistryObject<Item> LIGHT_SABER = ITEMS.register("light_saber", LightSaber::new);
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> MORTAR_DEPLOYER = ITEMS.register("mortar_deployer", MortarDeployer::new);
    public static final RegistryObject<Item> MORTAR_BARREL = ITEMS.register("mortar_barrel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MORTAR_BASE_PLATE = ITEMS.register("mortar_base_plate", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MORTAR_BIPOD = ITEMS.register("mortar_bipod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FUSEE = ITEMS.register("fusee", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRIMER = ITEMS.register("primer", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AP_HEAD = ITEMS.register("ap_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HE_HEAD = ITEMS.register("he_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPERPLATE = ITEMS.register("copperplate", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("ingot_steel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CEMENTED_CARBIDE_INGOT = ITEMS.register("cemented_carbide_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_ENERGY_EXPLOSIVES = ITEMS.register("high_energy_explosives", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRAIN = ITEMS.register("grain", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_POWDER = ITEMS.register("iron_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_POWDER = ITEMS.register("tungsten_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COAL_POWDER = ITEMS.register("coal_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COAL_IRON_POWDER = ITEMS.register("coal_iron_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_CEMENTED_CARBIDE_POWDER = ITEMS.register("raw_cemented_carbide_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GALENA = ITEMS.register("galena", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCHEELITE = ITEMS.register("scheelite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DOG_TAG = ITEMS.register("dog_tag", DogTag::new);
    public static final RegistryObject<Item> SHIELD_CELL = ITEMS.register("shield_cell", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> TUNGSTEN_ROD = ITEMS.register("tungsten_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_BARREL = ITEMS.register("iron_barrel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_ACTION = ITEMS.register("iron_action", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_TRIGGER = ITEMS.register("iron_trigger", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_SPRING = ITEMS.register("iron_spring", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_BARREL = ITEMS.register("steel_barrel", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> STEEL_ACTION = ITEMS.register("steel_action", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> STEEL_TRIGGER = ITEMS.register("steel_trigger", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> STEEL_SPRING = ITEMS.register("steel_spring", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> CEMENTED_CARBIDE_BARREL = ITEMS.register("cemented_carbide_barrel", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CEMENTED_CARBIDE_ACTION = ITEMS.register("cemented_carbide_action", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CEMENTED_CARBIDE_TRIGGER = ITEMS.register("cemented_carbide_trigger", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CEMENTED_CARBIDE_SPRING = ITEMS.register("cemented_carbide_spring", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> NETHERITE_BARREL = ITEMS.register("netherite_barrel", () -> new Item(new Item.Properties().rarity(RarityTool.LEGENDARY)));
    public static final RegistryObject<Item> NETHERITE_ACTION = ITEMS.register("netherite_action", () -> new Item(new Item.Properties().rarity(RarityTool.LEGENDARY)));
    public static final RegistryObject<Item> NETHERITE_TRIGGER = ITEMS.register("netherite_trigger", () -> new Item(new Item.Properties().rarity(RarityTool.LEGENDARY)));
    public static final RegistryObject<Item> NETHERITE_SPRING = ITEMS.register("netherite_spring", () -> new Item(new Item.Properties().rarity(RarityTool.LEGENDARY)));

    public static final RegistryObject<Item> COMMON_MATERIAL_PACK = ITEMS.register("common_material_pack", () -> new MaterialPack(Rarity.COMMON));
    public static final RegistryObject<Item> RARE_MATERIAL_PACK = ITEMS.register("rare_material_pack", () -> new MaterialPack(Rarity.RARE));
    public static final RegistryObject<Item> EPIC_MATERIAL_PACK = ITEMS.register("epic_material_pack", () -> new MaterialPack(Rarity.EPIC));
    public static final RegistryObject<Item> LEGENDARY_MATERIAL_PACK = ITEMS.register("legendary_material_pack", () -> new MaterialPack(RarityTool.LEGENDARY));

    public static final RegistryObject<Item> TRACHELIUM_BLUEPRINT = ITEMS.register("trachelium_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> GLOCK_17_BLUEPRINT = ITEMS.register("glock_17_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final RegistryObject<Item> GLOCK_18_BLUEPRINT = ITEMS.register("glock_18_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final RegistryObject<Item> HUNTING_RIFLE_BLUEPRINT = ITEMS.register("hunting_rifle_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> M_79_BLUEPRINT = ITEMS.register("m_79_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> RPG_BLUEPRINT = ITEMS.register("rpg_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> BOCEK_BLUEPRINT = ITEMS.register("bocek_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> M_4_BLUEPRINT = ITEMS.register("m_4_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> AA_12_BLUEPRINT = ITEMS.register("aa_12_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> HK_416_BLUEPRINT = ITEMS.register("hk_416_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> RPK_BLUEPRINT = ITEMS.register("rpk_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> SKS_BLUEPRINT = ITEMS.register("sks_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> NTW_20_BLUEPRINT = ITEMS.register("ntw_20_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> VECTOR_BLUEPRINT = ITEMS.register("vector_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> MINIGUN_BLUEPRINT = ITEMS.register("minigun_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> MK_14_BLUEPRINT = ITEMS.register("mk_14_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> SENTINEL_BLUEPRINT = ITEMS.register("sentinel_blueprint", () -> new BlueprintItem(RarityTool.LEGENDARY));
    public static final RegistryObject<Item> M_60_BLUEPRINT = ITEMS.register("m_60_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> SVD_BLUEPRINT = ITEMS.register("svd_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> MARLIN_BLUEPRINT = ITEMS.register("marlin_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> M_870_BLUEPRINT = ITEMS.register("m_870_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> M_98B_BLUEPRINT = ITEMS.register("m_98b_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> AK_47_BLUEPRINT = ITEMS.register("ak_47_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final RegistryObject<Item> DEVOTION_BLUEPRINT = ITEMS.register("devotion_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final RegistryObject<Item> TASER_BLUEPRINT = ITEMS.register("taser_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final RegistryObject<Item> M_1911_BLUEPRINT = ITEMS.register("m_1911_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final RegistryObject<Item> QBZ_95_BLUEPRINT = ITEMS.register("qbz_95_blueprint", () -> new BlueprintItem(Rarity.EPIC));

    /**
     * Block
     */
    public static final DeferredRegister<Item> BLOCKS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    public static final RegistryObject<Item> GALENA_ORE = block(ModBlocks.GALENA_ORE);
    public static final RegistryObject<Item> DEEPSLATE_GALENA_ORE = block(ModBlocks.DEEPSLATE_GALENA_ORE);
    public static final RegistryObject<Item> SCHEELITE_ORE = block(ModBlocks.SCHEELITE_ORE);
    public static final RegistryObject<Item> DEEPSLATE_SCHEELITE_ORE = block(ModBlocks.DEEPSLATE_SCHEELITE_ORE);
    public static final RegistryObject<Item> JUMP_PAD = block(ModBlocks.JUMP_PAD);
    public static final RegistryObject<Item> SANDBAG = block(ModBlocks.SANDBAG);
    public static final RegistryObject<Item> BARBED_WIRE = block(ModBlocks.BARBED_WIRE);
    public static final RegistryObject<Item> DRAGON_TEETH = block(ModBlocks.DRAGON_TEETH);

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return BLOCKS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * Perk Items
     */
    public static final DeferredRegister<Item> PERKS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    public static void registerPerkItems() {
        ModPerks.PERKS.getEntries().forEach(registryObject -> PERKS.register(registryObject.getId().getPath(), () -> new PerkItem(registryObject)));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        GUNS.register(bus);
        AMMO.register(bus);
        BLOCKS.register(bus);
        registerPerkItems();
        PERKS.register(bus);
    }
}
