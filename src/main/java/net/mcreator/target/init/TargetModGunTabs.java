
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.target.TargetMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetModGunTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TargetMod.MODID);

	public static final RegistryObject<CreativeModeTab> TARGET_GUNS = REGISTRY.register("target_guns",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.target.target_guns")).icon(() -> new ItemStack(TargetModItems.TASER.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TargetModItems.TASER.get());
				tabData.accept(TargetModItems.ABEKIRI.get());
				tabData.accept(TargetModItems.TRACHELIUM.get());
				tabData.accept(TargetModItems.VECTOR.get());
				tabData.accept(TargetModItems.AK_47.get());
				tabData.accept(TargetModItems.SKS.get());
				tabData.accept(TargetModItems.M_4.get());
				tabData.accept(TargetModItems.HK_416.get());
				tabData.accept(TargetModItems.MK_14.get());
				tabData.accept(TargetModItems.MARLIN.get());
				tabData.accept(TargetModItems.SVD.get());
				tabData.accept(TargetModItems.M_98B.get());
				tabData.accept(TargetModItems.SENTINEL.get());
				tabData.accept(TargetModItems.HUNTING_RIFLE.get());
				tabData.accept(TargetModItems.KRABER.get());
				tabData.accept(TargetModItems.M_870.get());
				tabData.accept(TargetModItems.AA_12.get());
				tabData.accept(TargetModItems.DEVOTION.get());
				tabData.accept(TargetModItems.RPK.get());
				tabData.accept(TargetModItems.M_60.get());
				tabData.accept(TargetModItems.MINIGUN.get());
				tabData.accept(TargetModItems.M_79.get());
				tabData.accept(TargetModItems.RPG.get());
				tabData.accept(TargetModItems.BOCEK.get());
			})
					.build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {

		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(TargetModItems.SENPAI_SPAWN_EGG.get());
		}
	}
}
