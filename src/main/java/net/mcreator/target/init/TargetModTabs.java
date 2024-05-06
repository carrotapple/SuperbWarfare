
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetModTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TargetMod.MODID);
    public static final RegistryObject<CreativeModeTab> TARGET_ITEM = REGISTRY.register("target_item",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.target.target_item")).icon(() -> new ItemStack(ItemRegistry.TARGET_DEPLOYER.get())).displayItems((parameters, tabData) -> {
                        tabData.accept(ItemRegistry.TARGET_DEPLOYER.get());
                        tabData.accept(TargetModBlocks.SANDBAG.get().asItem());
                        tabData.accept(TargetModBlocks.BARBED_WIRE.get().asItem());
                        tabData.accept(ItemRegistry.CLAYMORE_MINE.get());
                        tabData.accept(TargetModBlocks.JUMPPAD_BLOCK.get().asItem());
                        tabData.accept(ItemRegistry.LIGHT_SABER.get());
                        tabData.accept(ItemRegistry.HAMMER.get());
                        tabData.accept(ItemRegistry.MORTAR_DEPLOYER.get());
                        tabData.accept(ItemRegistry.MORTAR_BARREL.get());
                        tabData.accept(ItemRegistry.MORTAR_BASE_PLATE.get());
                        tabData.accept(ItemRegistry.MORTAR_BIPOD.get());
                        tabData.accept(ItemRegistry.FUSEE.get());
                        tabData.accept(ItemRegistry.SOUL_STEEL_NUGGET.get());
                        tabData.accept(ItemRegistry.COPPERPLATE.get());
                        tabData.accept(ItemRegistry.INGOT_STEEL.get());
                        tabData.accept(ItemRegistry.LEAD_INGOT.get());
                        tabData.accept(ItemRegistry.TUNGSTEN_INGOT.get());
                        tabData.accept(ItemRegistry.CEMENTED_CARBIDE_INGOT.get());
                        tabData.accept(ItemRegistry.SOUL_STEEL_INGOT.get());
                        tabData.accept(ItemRegistry.IRON_POWDER.get());
                        tabData.accept(ItemRegistry.TUNGSTEN_POWDER.get());
                        tabData.accept(ItemRegistry.COAL_POWDER.get());
                        tabData.accept(ItemRegistry.COAL_IRON_POWDER.get());
                        tabData.accept(ItemRegistry.RAW_CEMENTED_CARBIDE_POWDER.get());
                        tabData.accept(TargetModBlocks.GALENA_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.DEEPSLATE_GALENA_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.SCHEELITE_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.DEEPSLATE_SCHEELITE_ORE.get().asItem());
                        tabData.accept(ItemRegistry.GALENA.get());
                        tabData.accept(ItemRegistry.SCHEELITE.get());
                        tabData.accept(ItemRegistry.BUCKSHOT.get());
                        tabData.accept(ItemRegistry.IRON_BARREL.get());
                        tabData.accept(ItemRegistry.IRON_ACTION.get());
                        tabData.accept(ItemRegistry.IRON_TRIGGER.get());
                        tabData.accept(ItemRegistry.IRON_SPRING.get());
                        tabData.accept(ItemRegistry.STEEL_BARREL.get());
                        tabData.accept(ItemRegistry.STEEL_ACTION.get());
                        tabData.accept(ItemRegistry.STEEL_TRIGGER.get());
                        tabData.accept(ItemRegistry.STEEL_SPRING.get());
                        tabData.accept(ItemRegistry.CEMENTED_CARBIDE_BARREL.get());
                        tabData.accept(ItemRegistry.CEMENTED_CARBIDE_ACTION.get());
                        tabData.accept(ItemRegistry.CEMENTED_CARBIDE_TRIGGER.get());
                        tabData.accept(ItemRegistry.CEMENTED_CARBIDE_SPRING.get());
                        tabData.accept(ItemRegistry.NETHERITE_BARREL.get());
                        tabData.accept(ItemRegistry.NETHERITE_ACTION.get());
                        tabData.accept(ItemRegistry.NETHERITE_TRIGGER.get());
                        tabData.accept(ItemRegistry.NETHERITE_SPRING.get());
                        tabData.accept(ItemRegistry.COMMON_MATERIAL_PACK.get());
                        tabData.accept(ItemRegistry.RARE_MATERIAL_PACK.get());
                        tabData.accept(ItemRegistry.EPIC_MATERIAL_PACK.get());
                        tabData.accept(ItemRegistry.LEGENDARY_MATERIAL_PACK.get());
                        tabData.accept(ItemRegistry.SPECIAL_MATERIAL_PACK.get());
                        tabData.accept(ItemRegistry.TRACHELIUM_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.HUNTING_RIFLE_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.M_79_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.RPG_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.BOCEK_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.M_4_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.AA_12_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.HK_416_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.RPK_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.SKS_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.KRABER_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.VECTOR_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.MINIGUN_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.MK_14_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.SENTINEL_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.M_60_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.SVD_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.MARLIN_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.M_870_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.M_98B_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.AK_47_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.DEVOTION_BLUEPRINT.get());
                        tabData.accept(ItemRegistry.TASER_BLUEPRINT.get());
                        tabData.accept(TargetModBlocks.GUN_RECYCLE.get().asItem());
                    })

                    .build());
    public static final RegistryObject<CreativeModeTab> AMMO = REGISTRY.register("ammo",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.target.ammo")).icon(() -> new ItemStack(ItemRegistry.SHOTGUN_AMMO_BOX.get())).displayItems((parameters, tabData) -> {
                        tabData.accept(ItemRegistry.HANDGUN_AMMO.get());
                        tabData.accept(ItemRegistry.RIFLE_AMMO.get());
                        tabData.accept(ItemRegistry.SNIPER_AMMO.get());
                        tabData.accept(ItemRegistry.SHOTGUN_AMMO.get());
                        tabData.accept(ItemRegistry.HANDGUN_AMMO_BOX.get());
                        tabData.accept(ItemRegistry.RIFLE_AMMO_BOX.get());
                        tabData.accept(ItemRegistry.SNIPER_AMMO_BOX.get());
                        tabData.accept(ItemRegistry.SHOTGUN_AMMO_BOX.get());
                        tabData.accept(ItemRegistry.CREATIVE_AMMO_BOX.get());
                        tabData.accept(ItemRegistry.TASER_ELECTRODE.get());
                        tabData.accept(ItemRegistry.GRENADE_40MM.get());
                        tabData.accept(ItemRegistry.MORTAR_SHELLS.get());
                        tabData.accept(ItemRegistry.ROCKET.get());
                    })

                    .build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {

        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(ItemRegistry.SENPAI_SPAWN_EGG.get());
        }
    }
}
