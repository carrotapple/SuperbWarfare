package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.item.gun.*;
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
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModUtils.MODID);

    public static final RegistryObject<CreativeModeTab> GUN_TAB = TABS.register("guns",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.guns"))
                    .icon(() -> new ItemStack(ModItems.TASER.get()))
                    .displayItems(
                            (param, output) -> {
                                output.accept(Taser.getGunInstance());
                                output.accept(Glock17Item.getGunInstance());
                                output.accept(Glock18Item.getGunInstance());
                                output.accept(M1911Item.getGunInstance());
                                output.accept(Abekiri.getGunInstance());
                                output.accept(Trachelium.getGunInstance());
                                output.accept(VectorItem.getGunInstance());
                                output.accept(SksItem.getGunInstance());
                                output.accept(AK47Item.getGunInstance());
                                output.accept(M4Item.getGunInstance());
                                output.accept(Hk416Item.getGunInstance());
                                output.accept(Qbz95Item.getGunInstance());
                                output.accept(Mk14Item.getGunInstance());
                                output.accept(MarlinItem.getGunInstance());
                                output.accept(SvdItem.getGunInstance());
                                output.accept(HuntingRifle.getGunInstance());
                                output.accept(M98bItem.getGunInstance());
                                output.accept(SentinelItem.getGunInstance());
                                output.accept(Ntw20.getGunInstance());
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
                    .title(Component.translatable("item_group.superbwarfare.ammo"))
                    .icon(() -> new ItemStack(ModItems.SHOTGUN_AMMO_BOX.get()))
                    .withTabsBefore(GUN_TAB.getKey())
                    .displayItems((param, output) -> ModItems.AMMO.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register("item",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.item"))
                    .icon(() -> new ItemStack(ModItems.TARGET_DEPLOYER.get()))
                    .withTabsBefore(AMMO_TAB.getKey())
                    .displayItems((param, output) -> ModItems.ITEMS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final RegistryObject<CreativeModeTab> BLOCK_TAB = TABS.register("block",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.block"))
                    .icon(() -> new ItemStack(ModItems.SANDBAG.get()))
                    .withTabsBefore(ITEM_TAB.getKey())
                    .displayItems((param, output) -> ModItems.BLOCKS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(ModItems.SENPAI_SPAWN_EGG.get());
        }
    }
}
