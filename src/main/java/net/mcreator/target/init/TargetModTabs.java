package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.item.gun.*;
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
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TargetMod.MODID);

    public static final RegistryObject<CreativeModeTab> GUN_TAB = TABS.register("target_guns",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.target.target_guns"))
                    .icon(() -> new ItemStack(TargetModItems.TASER.get()))
                    .displayItems(
                            (param, output) -> {
                                output.accept(Taser.getGunInstance());
                                output.accept(Abekiri.getGunInstance());
                                output.accept(Trachelium.getGunInstance());
                                output.accept(VectorItem.getGunInstance());
                                output.accept(SksItem.getGunInstance());
                                output.accept(AK47Item.getGunInstance());
                                output.accept(M4Item.getGunInstance());
                                output.accept(Hk416Item.getGunInstance());
                                output.accept(Mk14Item.getGunInstance());
                                output.accept(MarlinItem.getGunInstance());
                                output.accept(SvdItem.getGunInstance());
                                output.accept(HuntingRifle.getGunInstance());
                                output.accept(M98bItem.getGunInstance());
                                output.accept(SentinelItem.getGunInstance());
                                output.accept(Kraber.getGunInstance());
                                output.accept(M870Item.getGunInstance());
                                output.accept(Aa12Item.getGunInstance());
                                output.accept(Devotion.getGunInstance());
                                output.accept(RpkItem.getGunInstance());
                                output.accept(M60Item.getGunInstance());
                                output.accept(Minigun.getGunInstance());
                                output.accept(BocekItem.getGunInstance());
                                output.accept(M79Item.getGunInstance());
                                output.accept(RpgItem.getGunInstance());
                            }
                    )
                    .build());

    public static final RegistryObject<CreativeModeTab> AMMO_TAB = TABS.register("ammo",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.target.ammo"))
                    .icon(() -> new ItemStack(TargetModItems.SHOTGUN_AMMO_BOX.get()))
                    .withTabsBefore(GUN_TAB.getKey())
                    .displayItems((param, output) -> TargetModItems.AMMO.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register("target_item",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.target.target_item"))
                    .icon(() -> new ItemStack(TargetModItems.TARGET_DEPLOYER.get()))
                    .withTabsBefore(AMMO_TAB.getKey())
                    .displayItems((param, output) -> TargetModItems.ITEMS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(TargetModItems.SENPAI_SPAWN_EGG.get());
        }
    }
}
