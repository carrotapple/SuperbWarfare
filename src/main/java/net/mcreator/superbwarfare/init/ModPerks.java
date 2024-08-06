package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.perk.Perk;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPerks {

    @SubscribeEvent
    public static void registry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<Perk>().setName(new ResourceLocation(ModUtils.MODID, "perk")));
    }

    public static final DeferredRegister<Perk> PERKS = DeferredRegister.create(new ResourceLocation(ModUtils.MODID, "perk"), ModUtils.MODID);

    // TODO 完成各种Perk的注册
    public static final RegistryObject<Perk> KILL_CLIP = PERKS.register("kill_clip", Perk::new);

}
