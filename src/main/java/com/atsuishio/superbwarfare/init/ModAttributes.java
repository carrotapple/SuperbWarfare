package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ModUtils.MODID);

    public static final RegistryObject<Attribute> BULLET_RESISTANCE = ATTRIBUTES.register("bullet_resistance", () -> (new RangedAttribute("attribute." + ModUtils.MODID + ".bullet_resistance", 0, 0, 1)).setSyncable(true));

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        List<EntityType<? extends LivingEntity>> entityTypes = event.getTypes();
        entityTypes.forEach((e) -> {
            Class<? extends Entity> baseClass = e.getBaseClass();
            if (baseClass.isAssignableFrom(LivingEntity.class)) {
                event.add(e, BULLET_RESISTANCE.get());
            }
        });

    }
}
