/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;

import net.mcreator.target.TargetMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TargetMod.MODID);
	public static final RegistryObject<Attribute> MOTARPITCH = ATTRIBUTES.register("motar_pitch", () -> (new RangedAttribute("attribute." + TargetMod.MODID + ".motar_pitch", 70, 20, 90)).setSyncable(true));
	public static final RegistryObject<Attribute> SPREAD = ATTRIBUTES.register("spread", () -> (new RangedAttribute("attribute." + TargetMod.MODID + ".spread", 0, 0, 1024)).setSyncable(true));

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
		});
	}

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(TargetModEntities.MORTAR.get(), MOTARPITCH.get());
		event.add(EntityType.PLAYER, SPREAD.get());
	}
}
