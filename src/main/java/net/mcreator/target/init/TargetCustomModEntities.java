
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.target.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.mcreator.target.entity.ProjectileEntity;

import net.mcreator.target.TargetMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetCustomModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TargetMod.MODID);

	public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE =
            ENTITY_TYPES.register("projectile",
                    () -> EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC).sized(1.0f, 1.0f).build("projectile"));

}
