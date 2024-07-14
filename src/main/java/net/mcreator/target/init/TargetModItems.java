package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.item.*;
import net.mcreator.target.item.common.ammo.*;
import net.mcreator.target.item.common.blueprint.*;
import net.mcreator.target.item.common.material.*;
import net.mcreator.target.item.common.material.component.*;
import net.mcreator.target.item.common.material.pack.*;
import net.mcreator.target.item.gun.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class TargetModItems {

    /**
     * guns
     */
    public static final DeferredRegister<Item> GUNS = DeferredRegister.create(ForgeRegistries.ITEMS, TargetMod.MODID);

    public static final RegistryObject<Item> TASER = GUNS.register("taser", Taser::new);
    public static final RegistryObject<Item> ABEKIRI = GUNS.register("abekiri", Abekiri::new);
    public static final RegistryObject<Item> TRACHELIUM = GUNS.register("trachelium", Trachelium::new);
    public static final RegistryObject<Item> VECTOR = GUNS.register("vector", VectorItem::new);
    public static final RegistryObject<Item> AK_47 = GUNS.register("ak_47", AK47Item::new);
    public static final RegistryObject<Item> SKS = GUNS.register("sks", SksItem::new);
    public static final RegistryObject<Item> M_4 = GUNS.register("m_4", M4Item::new);
    public static final RegistryObject<Item> HK_416 = GUNS.register("hk_416", Hk416Item::new);
    public static final RegistryObject<Item> MK_14 = GUNS.register("mk_14", Mk14Item::new);
    public static final RegistryObject<Item> MARLIN = GUNS.register("marlin", MarlinItem::new);
    public static final RegistryObject<Item> SVD = GUNS.register("svd", SvdItem::new);
    public static final RegistryObject<Item> M_98B = GUNS.register("m_98b", M98bItem::new);
    public static final RegistryObject<Item> SENTINEL = GUNS.register("sentinel", SentinelItem::new);
    public static final RegistryObject<Item> HUNTING_RIFLE = GUNS.register("hunting_rifle", HuntingRifle::new);
    public static final RegistryObject<Item> KRABER = GUNS.register("kraber", Kraber::new);
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
    public static final DeferredRegister<Item> AMMO = DeferredRegister.create(ForgeRegistries.ITEMS, TargetMod.MODID);

    public static final RegistryObject<Item> HANDGUN_AMMO = AMMO.register("handgun_ammo", HandgunAmmo::new);
    public static final RegistryObject<Item> RIFLE_AMMO = AMMO.register("rifle_ammo", RifleAmmo::new);
    public static final RegistryObject<Item> SNIPER_AMMO = AMMO.register("sniper_ammo", SniperAmmo::new);
    public static final RegistryObject<Item> SHOTGUN_AMMO = AMMO.register("shotgun_ammo", ShotgunAmmo::new);
    public static final RegistryObject<Item> HANDGUN_AMMO_BOX = AMMO.register("handgun_ammo_box", HandgunAmmoBox::new);
    public static final RegistryObject<Item> RIFLE_AMMO_BOX = AMMO.register("rifle_ammo_box", RifleAmmoBox::new);
    public static final RegistryObject<Item> SNIPER_AMMO_BOX = AMMO.register("sniper_ammo_box", SniperAmmoBox::new);
    public static final RegistryObject<Item> SHOTGUN_AMMO_BOX = AMMO.register("shotgun_ammo_box", ShotgunAmmoBox::new);
    public static final RegistryObject<Item> CREATIVE_AMMO_BOX = AMMO.register("creative_ammo_box", CreativeAmmoBox::new);
    public static final RegistryObject<Item> TASER_ELECTRODE = AMMO.register("taser_electrode", TaserElectrode::new);
    public static final RegistryObject<Item> GRENADE_40MM = AMMO.register("grenade_40mm", Grenade40mm::new);
    public static final RegistryObject<Item> MORTAR_SHELLS = AMMO.register("mortar_shells", MortarShells::new);
    public static final RegistryObject<Item> ROCKET = AMMO.register("rocket", Rocket::new);

    /**
     * items
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TargetMod.MODID);

    public static final RegistryObject<Item> SENPAI_SPAWN_EGG = ITEMS.register("senpai_spawn_egg", () -> new ForgeSpawnEggItem(TargetModEntities.SENPAI, -11584987, -14014413, new Item.Properties()));
    public static final RegistryObject<Item> TARGET_DEPLOYER = ITEMS.register("target_deployer", TargetDeployer::new);
    public static final RegistryObject<Item> SANDBAG = block(TargetModBlocks.SANDBAG);
    public static final RegistryObject<Item> BARBED_WIRE = block(TargetModBlocks.BARBED_WIRE);
    public static final RegistryObject<Item> CLAYMORE_MINE = ITEMS.register("claymore_mine", ClaymoreMine::new);
    public static final RegistryObject<Item> JUMP_PAD = block(TargetModBlocks.JUMP_PAD);
    public static final RegistryObject<Item> LIGHT_SABER = ITEMS.register("light_saber", LightSaber::new);
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> MORTAR_DEPLOYER = ITEMS.register("mortar_deployer", MortarDeployer::new);
    public static final RegistryObject<Item> MORTAR_BARREL = ITEMS.register("mortar_barrel", MortarBarrel::new);
    public static final RegistryObject<Item> MORTAR_BASE_PLATE = ITEMS.register("mortar_base_plate", MortarBasePlate::new);
    public static final RegistryObject<Item> MORTAR_BIPOD = ITEMS.register("mortar_bipod", MortarBipod::new);
    public static final RegistryObject<Item> FUSEE = ITEMS.register("fusee", Fusee::new);
    public static final RegistryObject<Item> PRIMER = ITEMS.register("primer", Primer::new);
    public static final RegistryObject<Item> SOUL_STEEL_NUGGET = ITEMS.register("soul_steel_nugget", SoulSteelNugget::new);
    public static final RegistryObject<Item> COPPERPLATE = ITEMS.register("copperplate", Copperplate::new);
    public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("ingot_steel", IngotSteel::new);
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", LeadIngot::new);
    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot", TungstenIngot::new);
    public static final RegistryObject<Item> CEMENTED_CARBIDE_INGOT = ITEMS.register("cemented_carbide_ingot", CementedCarbideIngot::new);
    public static final RegistryObject<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot", SoulSteelIngot::new);
    public static final RegistryObject<Item> IRON_POWDER = ITEMS.register("iron_powder", IronPowder::new);
    public static final RegistryObject<Item> TUNGSTEN_POWDER = ITEMS.register("tungsten_powder", TungstenPowder::new);
    public static final RegistryObject<Item> COAL_POWDER = ITEMS.register("coal_powder", CoalPowder::new);
    public static final RegistryObject<Item> COAL_IRON_POWDER = ITEMS.register("coal_iron_powder", CoalIronPowder::new);
    public static final RegistryObject<Item> RAW_CEMENTED_CARBIDE_POWDER = ITEMS.register("raw_cemented_carbide_powder", RawCementedCarbidePowder::new);
    public static final RegistryObject<Item> GALENA_ORE = block(TargetModBlocks.GALENA_ORE);
    public static final RegistryObject<Item> DEEPSLATE_GALENA_ORE = block(TargetModBlocks.DEEPSLATE_GALENA_ORE);
    public static final RegistryObject<Item> SCHEELITE_ORE = block(TargetModBlocks.SCHEELITE_ORE);
    public static final RegistryObject<Item> DEEPSLATE_SCHEELITE_ORE = block(TargetModBlocks.DEEPSLATE_SCHEELITE_ORE);
    public static final RegistryObject<Item> GALENA = ITEMS.register("galena", Galena::new);
    public static final RegistryObject<Item> SCHEELITE = ITEMS.register("scheelite", Scheelite::new);
    public static final RegistryObject<Item> DOG_TAG = ITEMS.register("dog_tag", DogTag::new);
    public static final RegistryObject<Item> SHIELD_CELL = ITEMS.register("shield_cell", ShieldCell::new);

    public static final RegistryObject<Item> IRON_BARREL = ITEMS.register("iron_barrel", IronBarrel::new);
    public static final RegistryObject<Item> IRON_ACTION = ITEMS.register("iron_action", IronAction::new);
    public static final RegistryObject<Item> IRON_TRIGGER = ITEMS.register("iron_trigger", IronTrigger::new);
    public static final RegistryObject<Item> IRON_SPRING = ITEMS.register("iron_spring", IronSpring::new);
    public static final RegistryObject<Item> STEEL_BARREL = ITEMS.register("steel_barrel", SteelBarrel::new);
    public static final RegistryObject<Item> STEEL_ACTION = ITEMS.register("steel_action", SteelAction::new);
    public static final RegistryObject<Item> STEEL_TRIGGER = ITEMS.register("steel_trigger", SteelTrigger::new);
    public static final RegistryObject<Item> STEEL_SPRING = ITEMS.register("steel_spring", SteelSpring::new);
    public static final RegistryObject<Item> CEMENTED_CARBIDE_BARREL = ITEMS.register("cemented_carbide_barrel", CementedCarbideBarrel::new);
    public static final RegistryObject<Item> CEMENTED_CARBIDE_ACTION = ITEMS.register("cemented_carbide_action", CementedCarbideAction::new);
    public static final RegistryObject<Item> CEMENTED_CARBIDE_TRIGGER = ITEMS.register("cemented_carbide_trigger", CementedCarbideTrigger::new);
    public static final RegistryObject<Item> CEMENTED_CARBIDE_SPRING = ITEMS.register("cemented_carbide_spring", CementedCarbideSpring::new);
    public static final RegistryObject<Item> NETHERITE_BARREL = ITEMS.register("netherite_barrel", NetheriteBarrel::new);
    public static final RegistryObject<Item> NETHERITE_ACTION = ITEMS.register("netherite_action", NetheriteAction::new);
    public static final RegistryObject<Item> NETHERITE_TRIGGER = ITEMS.register("netherite_trigger", NetheriteTrigger::new);
    public static final RegistryObject<Item> NETHERITE_SPRING = ITEMS.register("netherite_spring", NetheriteSpring::new);

    public static final RegistryObject<Item> COMMON_MATERIAL_PACK = ITEMS.register("common_material_pack", CommonMaterialPack::new);
    public static final RegistryObject<Item> RARE_MATERIAL_PACK = ITEMS.register("rare_material_pack", RareMaterialPack::new);
    public static final RegistryObject<Item> EPIC_MATERIAL_PACK = ITEMS.register("epic_material_pack", EpicMaterialPack::new);
    public static final RegistryObject<Item> LEGENDARY_MATERIAL_PACK = ITEMS.register("legendary_material_pack", LegendaryMaterialPack::new);
    public static final RegistryObject<Item> SPECIAL_MATERIAL_PACK = ITEMS.register("special_material_pack", SpecialMaterialPack::new);

    public static final RegistryObject<Item> TRACHELIUM_BLUEPRINT = ITEMS.register("trachelium_blueprint", TracheliumBlueprint::new);
    public static final RegistryObject<Item> HUNTING_RIFLE_BLUEPRINT = ITEMS.register("hunting_rifle_blueprint", HuntingRifleBlueprint::new);
    public static final RegistryObject<Item> M_79_BLUEPRINT = ITEMS.register("m_79_blueprint", M79Blueprint::new);
    public static final RegistryObject<Item> RPG_BLUEPRINT = ITEMS.register("rpg_blueprint", RpgBlueprint::new);
    public static final RegistryObject<Item> BOCEK_BLUEPRINT = ITEMS.register("bocek_blueprint", BocekBlueprint::new);
    public static final RegistryObject<Item> M_4_BLUEPRINT = ITEMS.register("m_4_blueprint", M4Blueprint::new);
    public static final RegistryObject<Item> AA_12_BLUEPRINT = ITEMS.register("aa_12_blueprint", Aa12Blueprint::new);
    public static final RegistryObject<Item> HK_416_BLUEPRINT = ITEMS.register("hk_416_blueprint", HK416Blueprint::new);
    public static final RegistryObject<Item> RPK_BLUEPRINT = ITEMS.register("rpk_blueprint", RPKBlueprint::new);
    public static final RegistryObject<Item> SKS_BLUEPRINT = ITEMS.register("sks_blueprint", SKSBlueprint::new);
    public static final RegistryObject<Item> KRABER_BLUEPRINT = ITEMS.register("kraber_blueprint", KRABERBlueprint::new);
    public static final RegistryObject<Item> VECTOR_BLUEPRINT = ITEMS.register("vector_blueprint", VectorBlueprint::new);
    public static final RegistryObject<Item> MINIGUN_BLUEPRINT = ITEMS.register("minigun_blueprint", MinigunBlueprint::new);
    public static final RegistryObject<Item> MK_14_BLUEPRINT = ITEMS.register("mk_14_blueprint", Mk14Blueprint::new);
    public static final RegistryObject<Item> SENTINEL_BLUEPRINT = ITEMS.register("sentinel_blueprint", SentinelBlueprint::new);
    public static final RegistryObject<Item> M_60_BLUEPRINT = ITEMS.register("m_60_blueprint", M60Blueprint::new);
    public static final RegistryObject<Item> SVD_BLUEPRINT = ITEMS.register("svd_blueprint", SvdBlueprint::new);
    public static final RegistryObject<Item> MARLIN_BLUEPRINT = ITEMS.register("marlin_blueprint", MarlinBlueprint::new);
    public static final RegistryObject<Item> M_870_BLUEPRINT = ITEMS.register("m_870_blueprint", M870Blueprint::new);
    public static final RegistryObject<Item> M_98B_BLUEPRINT = ITEMS.register("m_98b_blueprint", M98bBlueprint::new);
    public static final RegistryObject<Item> AK_47_BLUEPRINT = ITEMS.register("ak_47_blueprint", AK47Blueprint::new);
    public static final RegistryObject<Item> DEVOTION_BLUEPRINT = ITEMS.register("devotion_blueprint", DevotionBlueprint::new);
    public static final RegistryObject<Item> TASER_BLUEPRINT = ITEMS.register("taser_blueprint", TaserBlueprint::new);

    public static final RegistryObject<Item> GUN_RECYCLE = block(TargetModBlocks.GUN_RECYCLE);


    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        GUNS.register(bus);
        AMMO.register(bus);
    }
}
