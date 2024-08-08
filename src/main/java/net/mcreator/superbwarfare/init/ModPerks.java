package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.perk.AmmoPerk;
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

    public static final RegistryObject<AmmoPerk> SILVER_BULLET = PERKS.register("silver_bullet", () -> new AmmoPerk("silver_bullet", Perk.Type.AMMO, 0.1f));

    public static final RegistryObject<Perk> FOURTH_TIMES_CHARM = PERKS.register("fourth_times_charm", () -> new Perk("fourth_times_charm", Perk.Type.FUNCTIONAL));
    public static final RegistryObject<Perk> HEAL_CLIP = PERKS.register("heal_clip", () -> new Perk("heal_clip", Perk.Type.FUNCTIONAL));

    public static final RegistryObject<Perk> KILL_CLIP = PERKS.register("kill_clip", () -> new Perk("kill_clip", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> GUTSHOT_STRAIGHT = PERKS.register("gutshot_straight", () -> new Perk("gutshot_straight", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> KILLING_TALLY = PERKS.register("killing_tally", () -> new Perk("killing_tally", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> MONSTER_HUNTER = PERKS.register("monster_hunter", () -> new Perk("monster_hunter", Perk.Type.DAMAGE));
}
