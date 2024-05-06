
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
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.target.target_item")).icon(() -> new ItemStack(TargetModItems.TARGET_DEPLOYER.get())).displayItems((parameters, tabData) -> {
                        tabData.accept(TargetModItems.TARGET_DEPLOYER.get());
                        tabData.accept(TargetModBlocks.SANDBAG.get().asItem());
                        tabData.accept(TargetModBlocks.BARBED_WIRE.get().asItem());
                        tabData.accept(TargetModItems.CLAYMORE_MINE.get());
                        tabData.accept(TargetModBlocks.JUMPPAD_BLOCK.get().asItem());
                        tabData.accept(TargetModItems.LIGHT_SABER.get());
                        tabData.accept(TargetModItems.HAMMER.get());
                        tabData.accept(TargetModItems.MORTAR_DEPLOYER.get());
                        tabData.accept(TargetModItems.MORTAR_BARREL.get());
                        tabData.accept(TargetModItems.MORTAR_BASE_PLATE.get());
                        tabData.accept(TargetModItems.MORTAR_BIPOD.get());
                        tabData.accept(TargetModItems.FUSEE.get());
                        tabData.accept(TargetModItems.SOUL_STEEL_NUGGET.get());
                        tabData.accept(TargetModItems.COPPERPLATE.get());
                        tabData.accept(TargetModItems.INGOT_STEEL.get());
                        tabData.accept(TargetModItems.LEAD_INGOT.get());
                        tabData.accept(TargetModItems.TUNGSTEN_INGOT.get());
                        tabData.accept(TargetModItems.CEMENTED_CARBIDE_INGOT.get());
                        tabData.accept(TargetModItems.SOUL_STEEL_INGOT.get());
                        tabData.accept(TargetModItems.IRON_POWDER.get());
                        tabData.accept(TargetModItems.TUNGSTEN_POWDER.get());
                        tabData.accept(TargetModItems.COAL_POWDER.get());
                        tabData.accept(TargetModItems.COAL_IRON_POWDER.get());
                        tabData.accept(TargetModItems.RAW_CEMENTED_CARBIDE_POWDER.get());
                        tabData.accept(TargetModBlocks.GALENA_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.DEEPSLATE_GALENA_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.SCHEELITE_ORE.get().asItem());
                        tabData.accept(TargetModBlocks.DEEPSLATE_SCHEELITE_ORE.get().asItem());
                        tabData.accept(TargetModItems.GALENA.get());
                        tabData.accept(TargetModItems.SCHEELITE.get());
                        tabData.accept(TargetModItems.BUCKSHOT.get());
                        tabData.accept(TargetModItems.IRON_BARREL.get());
                        tabData.accept(TargetModItems.IRON_ACTION.get());
                        tabData.accept(TargetModItems.IRON_TRIGGER.get());
                        tabData.accept(TargetModItems.IRON_SPRING.get());
                        tabData.accept(TargetModItems.STEEL_BARREL.get());
                        tabData.accept(TargetModItems.STEEL_ACTION.get());
                        tabData.accept(TargetModItems.STEEL_TRIGGER.get());
                        tabData.accept(TargetModItems.STEEL_SPRING.get());
                        tabData.accept(TargetModItems.CEMENTED_CARBIDE_BARREL.get());
                        tabData.accept(TargetModItems.CEMENTED_CARBIDE_ACTION.get());
                        tabData.accept(TargetModItems.CEMENTED_CARBIDE_TRIGGER.get());
                        tabData.accept(TargetModItems.CEMENTED_CARBIDE_SPRING.get());
                        tabData.accept(TargetModItems.NETHERITE_BARREL.get());
                        tabData.accept(TargetModItems.NETHERITE_ACTION.get());
                        tabData.accept(TargetModItems.NETHERITE_TRIGGER.get());
                        tabData.accept(TargetModItems.NETHERITE_SPRING.get());
                        tabData.accept(TargetModItems.COMMON_MATERIAL_PACK.get());
                        tabData.accept(TargetModItems.RARE_MATERIAL_PACK.get());
                        tabData.accept(TargetModItems.EPIC_MATERIAL_PACK.get());
                        tabData.accept(TargetModItems.LEGENDARY_MATERIAL_PACK.get());
                        tabData.accept(TargetModItems.SPECIAL_MATERIAL_PACK.get());
                        tabData.accept(TargetModItems.TRACHELIUM_BLUEPRINT.get());
                        tabData.accept(TargetModItems.HUNTING_RIFLE_BLUEPRINT.get());
                        tabData.accept(TargetModItems.M_79_BLUEPRINT.get());
                        tabData.accept(TargetModItems.RPG_BLUEPRINT.get());
                        tabData.accept(TargetModItems.BOCEK_BLUEPRINT.get());
                        tabData.accept(TargetModItems.M_4_BLUEPRINT.get());
                        tabData.accept(TargetModItems.AA_12_BLUEPRINT.get());
                        tabData.accept(TargetModItems.HK_416_BLUEPRINT.get());
                        tabData.accept(TargetModItems.RPK_BLUEPRINT.get());
                        tabData.accept(TargetModItems.SKS_BLUEPRINT.get());
                        tabData.accept(TargetModItems.KRABER_BLUEPRINT.get());
                        tabData.accept(TargetModItems.VECTOR_BLUEPRINT.get());
                        tabData.accept(TargetModItems.MINIGUN_BLUEPRINT.get());
                        tabData.accept(TargetModItems.MK_14_BLUEPRINT.get());
                        tabData.accept(TargetModItems.SENTINEL_BLUEPRINT.get());
                        tabData.accept(TargetModItems.M_60_BLUEPRINT.get());
                        tabData.accept(TargetModItems.SVD_BLUEPRINT.get());
                        tabData.accept(TargetModItems.MARLIN_BLUEPRINT.get());
                        tabData.accept(TargetModItems.M_870_BLUEPRINT.get());
                        tabData.accept(TargetModItems.M_98B_BLUEPRINT.get());
                        tabData.accept(TargetModItems.AK_47_BLUEPRINT.get());
                        tabData.accept(TargetModItems.DEVOTION_BLUEPRINT.get());
                        tabData.accept(TargetModItems.TASER_BLUEPRINT.get());
                        tabData.accept(TargetModBlocks.GUN_RECYCLE.get().asItem());
                    })

                    .build());
    public static final RegistryObject<CreativeModeTab> AMMO = REGISTRY.register("ammo",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.target.ammo")).icon(() -> new ItemStack(TargetModItems.SHOTGUN_AMMO_BOX.get())).displayItems((parameters, tabData) -> {
                        tabData.accept(TargetModItems.HANDGUN_AMMO.get());
                        tabData.accept(TargetModItems.RIFLE_AMMO.get());
                        tabData.accept(TargetModItems.SNIPER_AMMO.get());
                        tabData.accept(TargetModItems.SHOTGUN_AMMO.get());
                        tabData.accept(TargetModItems.HANDGUN_AMMO_BOX.get());
                        tabData.accept(TargetModItems.RIFLE_AMMO_BOX.get());
                        tabData.accept(TargetModItems.SNIPER_AMMO_BOX.get());
                        tabData.accept(TargetModItems.SHOTGUN_AMMO_BOX.get());
                        tabData.accept(TargetModItems.CREATIVE_AMMO_BOX.get());
                        tabData.accept(TargetModItems.TASER_ELECTRODE.get());
                        tabData.accept(TargetModItems.GRENADE_40MM.get());
                        tabData.accept(TargetModItems.MORTAR_SHELLS.get());
                        tabData.accept(TargetModItems.ROCKET.get());
                    })

                    .build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {

        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(TargetModItems.SENPAI_SPAWN_EGG.get());
        }
    }
}
