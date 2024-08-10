package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.compat.CompatHolder;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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

    /**
     * Ammo Perks
     */
    public static final DeferredRegister<Perk> AMMO_PERKS = DeferredRegister.create(new ResourceLocation(ModUtils.MODID, "perk"), ModUtils.MODID);

    public static final RegistryObject<Perk> AP_BULLET = AMMO_PERKS.register("ap_bullet",
            () -> new AmmoPerk(new AmmoPerk.Builder("ap_bullet", Perk.Type.AMMO).bypassArmorRate(0.4f).rgb(230, 0, 0)));
    public static final RegistryObject<Perk> SILVER_BULLET = AMMO_PERKS.register("silver_bullet",
            () -> new AmmoPerk(new AmmoPerk.Builder("silver_bullet", Perk.Type.AMMO).bypassArmorRate(0.05f).rgb(87, 166, 219)));
    public static final RegistryObject<Perk> POISONOUS_BULLET = AMMO_PERKS.register("poisonous_bullet",
            () -> new AmmoPerk(new AmmoPerk.Builder("poisonous_bullet", Perk.Type.AMMO).bypassArmorRate(0.0f).rgb(48, 131, 6)
                    .mobEffect(() -> MobEffects.POISON)));
    public static final RegistryObject<Perk> BEAST_BULLET = AMMO_PERKS.register("beast_bullet",
            () -> new AmmoPerk(new AmmoPerk.Builder("beast_bullet", Perk.Type.AMMO).bypassArmorRate(0.0f).rgb(134, 65, 14)));

    /**
     * Functional Perks
     */
    public static final DeferredRegister<Perk> FUNC_PERKS = DeferredRegister.create(new ResourceLocation(ModUtils.MODID, "perk"), ModUtils.MODID);

    public static final RegistryObject<Perk> HEAL_CLIP = FUNC_PERKS.register("heal_clip", () -> new Perk("heal_clip", Perk.Type.FUNCTIONAL));
    public static final RegistryObject<Perk> FOURTH_TIMES_CHARM = FUNC_PERKS.register("fourth_times_charm", () -> new Perk("fourth_times_charm", Perk.Type.FUNCTIONAL));
    public static final RegistryObject<Perk> SUBSISTENCE = FUNC_PERKS.register("subsistence", () -> new Perk("subsistence", Perk.Type.FUNCTIONAL));
    public static final RegistryObject<Perk> FIELD_DOCTOR = FUNC_PERKS.register("field_doctor", () -> new Perk("field_doctor", Perk.Type.FUNCTIONAL));

    /**
     * Damage Perks
     */
    public static final DeferredRegister<Perk> DAMAGE_PERKS = DeferredRegister.create(new ResourceLocation(ModUtils.MODID, "perk"), ModUtils.MODID);

    public static final RegistryObject<Perk> KILL_CLIP = DAMAGE_PERKS.register("kill_clip", () -> new Perk("kill_clip", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> GUTSHOT_STRAIGHT = DAMAGE_PERKS.register("gutshot_straight", () -> new Perk("gutshot_straight", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> KILLING_TALLY = DAMAGE_PERKS.register("killing_tally", () -> new Perk("killing_tally", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> HEAD_SEEKER = DAMAGE_PERKS.register("head_seeker", () -> new Perk("head_seeker", Perk.Type.DAMAGE));
    public static final RegistryObject<Perk> MONSTER_HUNTER = DAMAGE_PERKS.register("monster_hunter", () -> new Perk("monster_hunter", Perk.Type.DAMAGE));


    public static void registerCompatPerks() {
        if (ModList.get().isLoaded(CompatHolder.DMV)) {
            AMMO_PERKS.register("blade_bullet", () -> new AmmoPerk(new AmmoPerk.Builder("blade_bullet", Perk.Type.AMMO)
                    .bypassArmorRate(-0.2f).rgb(0xB4, 0x4B, 0x88).mobEffect(() -> CompatHolder.DMV_BLEEDING)));
        }
        if (ModList.get().isLoaded(CompatHolder.VRC)) {
            AMMO_PERKS.register("curse_flame_bullet", () -> new AmmoPerk(new AmmoPerk.Builder("curse_flame_bullet", Perk.Type.AMMO)
                    .bypassArmorRate(0.0f).rgb(0xB1, 0xC1, 0xF2).mobEffect(() -> CompatHolder.VRC_CURSE_FLAME)));
        }
    }

    public static void register(IEventBus bus) {
        registerCompatPerks();
        AMMO_PERKS.register(bus);
        FUNC_PERKS.register(bus);
        DAMAGE_PERKS.register(bus);
    }
}
