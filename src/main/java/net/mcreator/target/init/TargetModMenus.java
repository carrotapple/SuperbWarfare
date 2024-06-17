package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.world.inventory.GunRecycleGuiMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TargetModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TargetMod.MODID);

    public static final RegistryObject<MenuType<GunRecycleGuiMenu>> GUN_RECYCLE_GUI = REGISTRY.register("gun_recycle_gui", () -> IForgeMenuType.create(GunRecycleGuiMenu::new));
}
