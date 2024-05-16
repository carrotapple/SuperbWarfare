
package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.world.entity.EntityType;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TargetModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TargetMod.MODID);
    public static final RegistryObject<Attribute> MORTAR_PITCH = ATTRIBUTES.register("mortar_pitch", () -> (new RangedAttribute("attribute." + TargetMod.MODID + ".mortar_pitch", 70, 20, 89)).setSyncable(true));
    public static final RegistryObject<Attribute> SPREAD = ATTRIBUTES.register("spread", () -> (new RangedAttribute("attribute." + TargetMod.MODID + ".spread", 0, 0, 1024)).setSyncable(true));

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(TargetModEntities.MORTAR.get(), MORTAR_PITCH.get());
        event.add(EntityType.PLAYER, SPREAD.get());
    }
}
