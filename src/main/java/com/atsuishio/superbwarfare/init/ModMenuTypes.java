package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.block.menu.ChargingStationMenu;
import com.atsuishio.superbwarfare.block.menu.ReforgingTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModUtils.MODID);

    public static final RegistryObject<MenuType<ReforgingTableMenu>> REFORGING_TABLE_MENU =
            REGISTRY.register("reforging_table_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new ReforgingTableMenu(windowId, inv)));
    public static final RegistryObject<MenuType<ChargingStationMenu>> CHARGING_STATION_MENU =
            REGISTRY.register("charging_station_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new ChargingStationMenu(windowId, inv)));
}
