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
public class TargetModGunTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TargetMod.MODID);

    public static final RegistryObject<CreativeModeTab> TARGET_GUNS = REGISTRY.register("target_guns",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.target.target_guns")).icon(() -> new ItemStack(ItemRegistry.TASER.get())).displayItems((parameters, tabData) -> {
                        tabData.accept(ItemRegistry.TASER.get());
                        tabData.accept(ItemRegistry.ABEKIRI.get());
                        tabData.accept(ItemRegistry.TRACHELIUM.get());
                        tabData.accept(ItemRegistry.VECTOR.get());
                        tabData.accept(ItemRegistry.AK_47.get());
                        tabData.accept(ItemRegistry.SKS.get());
                        tabData.accept(ItemRegistry.M_4.get());
                        tabData.accept(ItemRegistry.HK_416.get());
                        tabData.accept(ItemRegistry.MK_14.get());
                        tabData.accept(ItemRegistry.MARLIN.get());
                        tabData.accept(ItemRegistry.SVD.get());
                        tabData.accept(ItemRegistry.M_98B.get());
                        tabData.accept(ItemRegistry.SENTINEL.get());
                        tabData.accept(ItemRegistry.HUNTING_RIFLE.get());
                        tabData.accept(ItemRegistry.KRABER.get());
                        tabData.accept(ItemRegistry.M_870.get());
                        tabData.accept(ItemRegistry.AA_12.get());
                        tabData.accept(ItemRegistry.DEVOTION.get());
                        tabData.accept(ItemRegistry.RPK.get());
                        tabData.accept(ItemRegistry.M_60.get());
                        tabData.accept(ItemRegistry.MINIGUN.get());
                        tabData.accept(ItemRegistry.M_79.get());
                        tabData.accept(ItemRegistry.RPG.get());
                        tabData.accept(ItemRegistry.BOCEK.get());
                    })
                    .build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {

        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(ItemRegistry.SENPAI_SPAWN_EGG.get());
        }
    }
}
