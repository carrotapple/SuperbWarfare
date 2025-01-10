package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.item.ArmorPlate;
import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import com.atsuishio.superbwarfare.item.gun.handgun.*;
import com.atsuishio.superbwarfare.item.gun.launcher.JavelinItem;
import com.atsuishio.superbwarfare.item.gun.launcher.M79Item;
import com.atsuishio.superbwarfare.item.gun.launcher.RpgItem;
import com.atsuishio.superbwarfare.item.gun.launcher.SecondaryCataclysm;
import com.atsuishio.superbwarfare.item.gun.machinegun.DevotionItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.M60Item;
import com.atsuishio.superbwarfare.item.gun.machinegun.MinigunItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.RpkItem;
import com.atsuishio.superbwarfare.item.gun.rifle.*;
import com.atsuishio.superbwarfare.item.gun.shotgun.Aa12Item;
import com.atsuishio.superbwarfare.item.gun.shotgun.AbekiriItem;
import com.atsuishio.superbwarfare.item.gun.shotgun.M870Item;
import com.atsuishio.superbwarfare.item.gun.smg.VectorItem;
import com.atsuishio.superbwarfare.item.gun.sniper.*;
import com.atsuishio.superbwarfare.item.gun.special.BocekItem;
import com.atsuishio.superbwarfare.item.gun.special.TaserItem;
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
                                output.accept(TaserItem.getGunInstance());
                                output.accept(Glock17Item.getGunInstance());
                                output.accept(Glock18Item.getGunInstance());
                                output.accept(M1911Item.getGunInstance());
                                output.accept(Mp443Item.getGunInstance());
                                output.accept(AbekiriItem.getGunInstance());
                                output.accept(Trachelium.getGunInstance());
                                output.accept(VectorItem.getGunInstance());
                                output.accept(SksItem.getGunInstance());
                                output.accept(AK47Item.getGunInstance());
                                output.accept(AK12Item.getGunInstance());
                                output.accept(M4Item.getGunInstance());
                                output.accept(Hk416Item.getGunInstance());
                                output.accept(Qbz95Item.getGunInstance());
                                output.accept(Mk14Item.getGunInstance());
                                output.accept(MarlinItem.getGunInstance());
                                output.accept(K98Item.getGunInstance());
                                output.accept(MosinNagantItem.getGunInstance());
                                output.accept(SvdItem.getGunInstance());
                                output.accept(HuntingRifleItem.getGunInstance());
                                output.accept(M98bItem.getGunInstance());
                                output.accept(SentinelItem.getGunInstance());
                                output.accept(Ntw20Item.getGunInstance());
                                output.accept(M870Item.getGunInstance());
                                output.accept(Aa12Item.getGunInstance());
                                output.accept(DevotionItem.getGunInstance());
                                output.accept(RpkItem.getGunInstance());
                                output.accept(M60Item.getGunInstance());
                                output.accept(MinigunItem.getGunInstance());
                                output.accept(BocekItem.getGunInstance());
                                output.accept(M79Item.getGunInstance());
                                output.accept(SecondaryCataclysm.getGunInstance());
                                output.accept(RpgItem.getGunInstance());
                                output.accept(JavelinItem.getGunInstance());
                            }
                    )
                    .build());

    public static final RegistryObject<CreativeModeTab> PERK_TAB = TABS.register("perk",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.perk"))
                    .icon(() -> new ItemStack(ModItems.AP_BULLET.get()))
                    .withTabsBefore(GUN_TAB.getKey())
                    .displayItems((param, output) -> ModItems.PERKS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final RegistryObject<CreativeModeTab> AMMO_TAB = TABS.register("ammo",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.ammo"))
                    .icon(() -> new ItemStack(ModItems.SHOTGUN_AMMO_BOX.get()))
                    .withTabsBefore(PERK_TAB.getKey())
                    .displayItems((param, output) -> ModItems.AMMO.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register("item",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.superbwarfare.item"))
                    .icon(() -> new ItemStack(ModItems.TARGET_DEPLOYER.get()))
                    .withTabsBefore(AMMO_TAB.getKey())
                    .displayItems((param, output) -> ModItems.ITEMS.getEntries().forEach(registryObject -> {
                        if (registryObject.get() == ModItems.CONTAINER.get()) {
                            output.accept(ContainerBlockItem.createInstance(ModEntities.MK_42.get()));
                            output.accept(ContainerBlockItem.createInstance(ModEntities.MLE_1934.get()));
                            output.accept(ContainerBlockItem.createInstance(ModEntities.ANNIHILATOR.get()));
                            output.accept(ContainerBlockItem.createInstance(ModEntities.SPEEDBOAT.get(), true));
                            output.accept(ContainerBlockItem.createInstance(ModEntities.WHEEL_CHAIR.get()));
                            output.accept(ContainerBlockItem.createInstance(ModEntities.AH_6.get()));
                        } else {
                            output.accept(registryObject.get());
                            if (registryObject.get() == ModItems.ARMOR_PLATE.get()) {
                                output.accept(ArmorPlate.getInfiniteInstance());
                            }
                        }
                    }))
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
