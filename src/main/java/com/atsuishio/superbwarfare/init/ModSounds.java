package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModSounds {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModUtils.MODID);

    public static final RegistryObject<SoundEvent> TASER_FIRE_1P = REGISTRY.register("taser_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("taser_fire_1p")));
    public static final RegistryObject<SoundEvent> TASER_FIRE_3P = REGISTRY.register("taser_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("taser_fire_3p")));
    public static final RegistryObject<SoundEvent> TASER_RELOAD_EMPTY = REGISTRY.register("taser_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("taser_reload_empty")));

    public static final RegistryObject<SoundEvent> SHOCK = REGISTRY.register("shock", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("shock")));
    public static final RegistryObject<SoundEvent> ELECTRIC = REGISTRY.register("electric", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("electric")));

    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_1P = REGISTRY.register("trachelium_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_fire_1p")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_3P = REGISTRY.register("trachelium_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_fire_3p")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FAR = REGISTRY.register("trachelium_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_far")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_VERYFAR = REGISTRY.register("trachelium_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_veryfar")));

    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_1P_S = REGISTRY.register("trachelium_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_fire_1p_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_3P_S = REGISTRY.register("trachelium_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_fire_3p_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FAR_S = REGISTRY.register("trachelium_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_far_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_RELOAD_EMPTY = REGISTRY.register("trachelium_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_reload_empty")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_BOLT = REGISTRY.register("trachelium_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("trachelium_bolt")));

    public static final RegistryObject<SoundEvent> TRIGGER_CLICK = REGISTRY.register("triggerclick", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("triggerclick")));
    public static final RegistryObject<SoundEvent> HIT = REGISTRY.register("hit", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hit")));
    public static final RegistryObject<SoundEvent> TARGET_DOWN = REGISTRY.register("targetdown", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("targetdown")));
    public static final RegistryObject<SoundEvent> INDICATION = REGISTRY.register("indication", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("indication")));
    public static final RegistryObject<SoundEvent> JUMP = REGISTRY.register("jump", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("jump")));
    public static final RegistryObject<SoundEvent> DOUBLE_JUMP = REGISTRY.register("doublejump", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("doublejump")));

    public static final RegistryObject<SoundEvent> EXPLOSION_CLOSE = REGISTRY.register("explosion_close", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("explosion_close")));
    public static final RegistryObject<SoundEvent> EXPLOSION_FAR = REGISTRY.register("explosion_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("explosion_far")));
    public static final RegistryObject<SoundEvent> EXPLOSION_VERY_FAR = REGISTRY.register("explosion_very_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("explosion_very_far")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_CLOSE = REGISTRY.register("huge_explosion_close", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("huge_explosion_close")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_FAR = REGISTRY.register("huge_explosion_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("huge_explosion_far")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_VERY_FAR = REGISTRY.register("huge_explosion_very_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("huge_explosion_very_far")));
    public static final RegistryObject<SoundEvent> EXPLOSION_WATER = REGISTRY.register("explosion_water", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("explosion_water")));

    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FIRE_1P = REGISTRY.register("hunting_rifle_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hunting_rifle_fire_1p")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FIRE_3P = REGISTRY.register("hunting_rifle_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hunting_rifle_fire_3p")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FAR = REGISTRY.register("hunting_rifle_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hunting_rifle_far")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_VERYFAR = REGISTRY.register("hunting_rifle_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hunting_rifle_veryfar")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_RELOAD_EMPTY = REGISTRY.register("hunting_rifle_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hunting_rifle_reload_empty")));

    public static final RegistryObject<SoundEvent> OUCH = REGISTRY.register("ouch", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ouch")));
    public static final RegistryObject<SoundEvent> STEP = REGISTRY.register("step", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("step")));
    public static final RegistryObject<SoundEvent> GROWL = REGISTRY.register("growl", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("growl")));
    public static final RegistryObject<SoundEvent> IDLE = REGISTRY.register("idle", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("idle")));
    public static final RegistryObject<SoundEvent> HENG = REGISTRY.register("heng", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heng")));

    public static final RegistryObject<SoundEvent> M_79_FIRE_1P = REGISTRY.register("m_79_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_79_fire_1p")));
    public static final RegistryObject<SoundEvent> M_79_FIRE_3P = REGISTRY.register("m_79_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_79_fire_3p")));
    public static final RegistryObject<SoundEvent> M_79_FAR = REGISTRY.register("m_79_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_79_far")));
    public static final RegistryObject<SoundEvent> M_79_VERYFAR = REGISTRY.register("m_79_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_79_veryfar")));
    public static final RegistryObject<SoundEvent> M_79_RELOAD_EMPTY = REGISTRY.register("m_79_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_79_reload_empty")));

    public static final RegistryObject<SoundEvent> SKS_FIRE_1P = REGISTRY.register("sks_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_fire_1p")));
    public static final RegistryObject<SoundEvent> SKS_FIRE_3P = REGISTRY.register("sks_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_fire_3p")));
    public static final RegistryObject<SoundEvent> SKS_RELOAD_NORMAL = REGISTRY.register("sks_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_reload_normal")));
    public static final RegistryObject<SoundEvent> SKS_RELOAD_EMPTY = REGISTRY.register("sks_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_reload_empty")));
    public static final RegistryObject<SoundEvent> SKS_FAR = REGISTRY.register("sks_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_far")));
    public static final RegistryObject<SoundEvent> SKS_VERYFAR = REGISTRY.register("sks_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sks_veryfar")));

    public static final RegistryObject<SoundEvent> ABEKIRI_FIRE_1P = REGISTRY.register("abekiri_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_fire_1p")));
    public static final RegistryObject<SoundEvent> ABEKIRI_FIRE_3P = REGISTRY.register("abekiri_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_fire_3p")));
    public static final RegistryObject<SoundEvent> ABEKIRI_FAR = REGISTRY.register("abekiri_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_far")));
    public static final RegistryObject<SoundEvent> ABEKIRI_VERYFAR = REGISTRY.register("abekiri_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_veryfar")));
    public static final RegistryObject<SoundEvent> ABEKIRI_RELOAD_NORMAL = REGISTRY.register("abekiri_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_reload_normal")));
    public static final RegistryObject<SoundEvent> ABEKIRI_RELOAD_EMPTY = REGISTRY.register("abekiri_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("abekiri_reload_empty")));

    public static final RegistryObject<SoundEvent> AK_47_FIRE_1P = REGISTRY.register("ak_47_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_fire_1p")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_3P = REGISTRY.register("ak_47_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_fire_3p")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_1P_S = REGISTRY.register("ak_47_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_fire_1p_s")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_3P_S = REGISTRY.register("ak_47_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_fire_3p_s")));
    public static final RegistryObject<SoundEvent> AK_47_FAR = REGISTRY.register("ak_47_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_far")));
    public static final RegistryObject<SoundEvent> AK_47_VERYFAR = REGISTRY.register("ak_47_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_veryfar")));
    public static final RegistryObject<SoundEvent> AK_47_FAR_S = REGISTRY.register("ak_47_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far_s")));
    public static final RegistryObject<SoundEvent> AK_47_VERYFAR_S = REGISTRY.register("ak_47_veryfar_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar_s")));
    public static final RegistryObject<SoundEvent> AK_47_RELOAD_NORMAL = REGISTRY.register("ak_47_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_reload_normal")));
    public static final RegistryObject<SoundEvent> AK_47_RELOAD_EMPTY = REGISTRY.register("ak_47_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_47_reload_empty")));

    public static final RegistryObject<SoundEvent> AK_12_FIRE_1P = REGISTRY.register("ak_12_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_fire_1p")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_3P = REGISTRY.register("ak_12_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_fire_3p")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_1P_S = REGISTRY.register("ak_12_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_fire_1p_s")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_3P_S = REGISTRY.register("ak_12_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_fire_3p_s")));
    public static final RegistryObject<SoundEvent> AK_12_FAR = REGISTRY.register("ak_12_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far")));
    public static final RegistryObject<SoundEvent> AK_12_VERYFAR = REGISTRY.register("ak_12_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar")));
    public static final RegistryObject<SoundEvent> AK_12_FAR_S = REGISTRY.register("ak_12_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far_s")));
    public static final RegistryObject<SoundEvent> AK_12_VERYFAR_S = REGISTRY.register("ak_12_veryfar_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar_s")));
    public static final RegistryObject<SoundEvent> AK_12_RELOAD_NORMAL = REGISTRY.register("ak_12_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_reload_normal")));
    public static final RegistryObject<SoundEvent> AK_12_RELOAD_EMPTY = REGISTRY.register("ak_12_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_reload_empty")));

    public static final RegistryObject<SoundEvent> LAND = REGISTRY.register("land", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("land")));
    public static final RegistryObject<SoundEvent> HEADSHOT = REGISTRY.register("headshot", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("headshot")));

    public static final RegistryObject<SoundEvent> DEVOTION_FIRE_1P = REGISTRY.register("devotion_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_fire_1p")));
    public static final RegistryObject<SoundEvent> DEVOTION_FIRE_3P = REGISTRY.register("devotion_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_fire_3p")));
    public static final RegistryObject<SoundEvent> DEVOTION_FAR = REGISTRY.register("devotion_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_far")));
    public static final RegistryObject<SoundEvent> DEVOTION_VERYFAR = REGISTRY.register("devotion_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_veryfar")));
    public static final RegistryObject<SoundEvent> DEVOTION_RELOAD_NORMAL = REGISTRY.register("devotion_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_reload_normal")));
    public static final RegistryObject<SoundEvent> DEVOTION_RELOAD_EMPTY = REGISTRY.register("devotion_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("devotion_reload_empty")));

    public static final RegistryObject<SoundEvent> RPG_FIRE_1P = REGISTRY.register("rpg_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpg_fire_1p")));
    public static final RegistryObject<SoundEvent> RPG_FIRE_3P = REGISTRY.register("rpg_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpg_fire_3p")));
    public static final RegistryObject<SoundEvent> RPG_FAR = REGISTRY.register("rpg_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpg_far")));
    public static final RegistryObject<SoundEvent> RPG_VERYFAR = REGISTRY.register("rpg_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpg_veryfar")));
    public static final RegistryObject<SoundEvent> RPG_RELOAD_EMPTY = REGISTRY.register("rpg_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpg_reload_empty")));

    public static final RegistryObject<SoundEvent> MORTAR_FIRE = REGISTRY.register("mortar_fire", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mortar_fire")));
    public static final RegistryObject<SoundEvent> MORTAR_LOAD = REGISTRY.register("mortar_load", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mortar_load")));
    public static final RegistryObject<SoundEvent> MORTAR_DISTANT = REGISTRY.register("mortar_distant", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mortar_distant")));

    public static final RegistryObject<SoundEvent> FIRE_RATE = REGISTRY.register("firerate", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("firerate")));

    public static final RegistryObject<SoundEvent> M_4_FIRE_1P = REGISTRY.register("m_4_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_fire_1p")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_3P = REGISTRY.register("m_4_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_fire_3p")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_1P_S = REGISTRY.register("m_4_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_fire_1p_s")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_3P_S = REGISTRY.register("m_4_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_fire_3p_s")));
    public static final RegistryObject<SoundEvent> M_4_FAR = REGISTRY.register("m_4_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_far")));
    public static final RegistryObject<SoundEvent> M_4_VERYFAR = REGISTRY.register("m_4_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_veryfar")));
    public static final RegistryObject<SoundEvent> M_4_RELOAD_NORMAL = REGISTRY.register("m_4_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_reload_normal")));
    public static final RegistryObject<SoundEvent> M_4_RELOAD_EMPTY = REGISTRY.register("m_4_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_4_reload_empty")));
    public static final RegistryObject<SoundEvent> M_4_FAR_S = REGISTRY.register("m_4_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far_s")));
    public static final RegistryObject<SoundEvent> M_4_VERYFAR_S = REGISTRY.register("m_4_veryfar_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> AA_12_FIRE_1P = REGISTRY.register("aa_12_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_fire_1p")));
    public static final RegistryObject<SoundEvent> AA_12_FIRE_3P = REGISTRY.register("aa_12_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_fire_3p")));
    public static final RegistryObject<SoundEvent> AA_12_FAR = REGISTRY.register("aa_12_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_far")));
    public static final RegistryObject<SoundEvent> AA_12_VERYFAR = REGISTRY.register("aa_12_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_veryfar")));
    public static final RegistryObject<SoundEvent> AA_12_RELOAD_NORMAL = REGISTRY.register("aa_12_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_reload_normal")));
    public static final RegistryObject<SoundEvent> AA_12_RELOAD_EMPTY = REGISTRY.register("aa_12_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("aa_12_reload_empty")));

    public static final RegistryObject<SoundEvent> BOCEK_ZOOM_FIRE_1P = REGISTRY.register("bocek_zoom_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_zoom_fire_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_ZOOM_FIRE_3P = REGISTRY.register("bocek_zoom_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_zoom_fire_3p")));
    public static final RegistryObject<SoundEvent> BOCEK_SHATTER_CAP_FIRE_1P = REGISTRY.register("bocek_shatter_cap_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_shatter_cap_fire_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_SHATTER_CAP_FIRE_3P = REGISTRY.register("bocek_shatter_cap_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_shatter_cap_fire_3p")));
    public static final RegistryObject<SoundEvent> BOCEK_PULL_1P = REGISTRY.register("bocek_pull_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_pull_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_PULL_3P = REGISTRY.register("bocek_pull_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bocek_pull_3p")));

    public static final RegistryObject<SoundEvent> HK_416_FIRE_1P = REGISTRY.register("hk_416_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_fire_1p")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_3P = REGISTRY.register("hk_416_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_fire_3p")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_1P_S = REGISTRY.register("hk_416_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_fire_1p_s")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_3P_S = REGISTRY.register("hk_416_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_fire_3p_s")));
    public static final RegistryObject<SoundEvent> HK_416_FAR = REGISTRY.register("hk_416_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_far")));
    public static final RegistryObject<SoundEvent> HK_416_VERYFAR = REGISTRY.register("hk_416_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_veryfar")));
    public static final RegistryObject<SoundEvent> HK_416_RELOAD_NORMAL = REGISTRY.register("hk_416_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_reload_normal")));
    public static final RegistryObject<SoundEvent> HK_416_RELOAD_EMPTY = REGISTRY.register("hk_416_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("hk_416_reload_empty")));
    public static final RegistryObject<SoundEvent> HK_416_FAR_S = REGISTRY.register("hk_416_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far_s")));
    public static final RegistryObject<SoundEvent> HK_416_VERYFAR_S = REGISTRY.register("hk_416_veryfar_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> RPK_FIRE_1P = REGISTRY.register("rpk_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_fire_1p")));
    public static final RegistryObject<SoundEvent> RPK_FIRE_3P = REGISTRY.register("rpk_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_fire_3p")));
    public static final RegistryObject<SoundEvent> RPK_FAR = REGISTRY.register("rpk_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_far")));
    public static final RegistryObject<SoundEvent> RPK_VERYFAR = REGISTRY.register("rpk_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_veryfar")));
    public static final RegistryObject<SoundEvent> RPK_RELOAD_NORMAL = REGISTRY.register("rpk_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_reload_normal")));
    public static final RegistryObject<SoundEvent> RPK_RELOAD_EMPTY = REGISTRY.register("rpk_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("rpk_reload_empty")));

    public static final RegistryObject<SoundEvent> NTW_20_FIRE_1P = REGISTRY.register("ntw_20_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_fire_1p")));
    public static final RegistryObject<SoundEvent> NTW_20_FIRE_3P = REGISTRY.register("ntw_20_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_fire_3p")));
    public static final RegistryObject<SoundEvent> NTW_20_FAR = REGISTRY.register("ntw_20_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_far")));
    public static final RegistryObject<SoundEvent> NTW_20_VERYFAR = REGISTRY.register("ntw_20_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_veryfar")));
    public static final RegistryObject<SoundEvent> NTW_20_RELOAD_NORMAL = REGISTRY.register("ntw_20_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_reload_normal")));
    public static final RegistryObject<SoundEvent> NTW_20_RELOAD_EMPTY = REGISTRY.register("ntw_20_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_reload_empty")));
    public static final RegistryObject<SoundEvent> NTW_20_BOLT = REGISTRY.register("ntw_20_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ntw_20_bolt")));

    public static final RegistryObject<SoundEvent> VECTOR_FIRE_1P = REGISTRY.register("vector_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_fire_1p")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_3P = REGISTRY.register("vector_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_fire_3p")));
    public static final RegistryObject<SoundEvent> VECTOR_FAR = REGISTRY.register("vector_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_far")));
    public static final RegistryObject<SoundEvent> VECTOR_VERYFAR = REGISTRY.register("vector_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_veryfar")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_1P_S = REGISTRY.register("vector_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_fire_1p_s")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_3P_S = REGISTRY.register("vector_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_fire_3p_s")));
    public static final RegistryObject<SoundEvent> VECTOR_FAR_S = REGISTRY.register("vector_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_far_s")));
    public static final RegistryObject<SoundEvent> VECTOR_RELOAD_NORMAL = REGISTRY.register("vector_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_reload_normal")));
    public static final RegistryObject<SoundEvent> VECTOR_RELOAD_EMPTY = REGISTRY.register("vector_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vector_reload_empty")));

    public static final RegistryObject<SoundEvent> MINIGUN_FIRE_1P = REGISTRY.register("minigun_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_fire_1p")));
    public static final RegistryObject<SoundEvent> MINIGUN_FIRE_3P = REGISTRY.register("minigun_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_fire_3p")));
    public static final RegistryObject<SoundEvent> MINIGUN_FAR = REGISTRY.register("minigun_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_far")));
    public static final RegistryObject<SoundEvent> MINIGUN_VERYFAR = REGISTRY.register("minigun_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_veryfar")));
    public static final RegistryObject<SoundEvent> MINIGUN_ROT = REGISTRY.register("minigun_rot", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_rot")));
    public static final RegistryObject<SoundEvent> MINIGUN_OVERHEAT = REGISTRY.register("minigun_overheat", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("minigun_overheat")));

    public static final RegistryObject<SoundEvent> MK_14_FIRE_1P = REGISTRY.register("mk_14_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_fire_1p")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_3P = REGISTRY.register("mk_14_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_fire_3p")));
    public static final RegistryObject<SoundEvent> MK_14_FAR = REGISTRY.register("mk_14_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_far")));
    public static final RegistryObject<SoundEvent> MK_14_VERYFAR = REGISTRY.register("mk_14_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_veryfar")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_1P_S = REGISTRY.register("mk_14_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_fire_1p_s")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_3P_S = REGISTRY.register("mk_14_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_fire_3p_s")));
    public static final RegistryObject<SoundEvent> MK_14_FAR_S = REGISTRY.register("mk_14_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_far_s")));
    public static final RegistryObject<SoundEvent> MK_14_RELOAD_NORMAL = REGISTRY.register("mk_14_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_reload_normal")));
    public static final RegistryObject<SoundEvent> MK_14_RELOAD_EMPTY = REGISTRY.register("mk_14_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_14_reload_empty")));

    public static final RegistryObject<SoundEvent> SENTINEL_FIRE_1P = REGISTRY.register("sentinel_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_fire_1p")));
    public static final RegistryObject<SoundEvent> SENTINEL_FIRE_3P = REGISTRY.register("sentinel_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_fire_3p")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FIRE_1P = REGISTRY.register("sentinel_charge_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_charge_fire_1p")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FIRE_3P = REGISTRY.register("sentinel_charge_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_charge_fire_3p")));
    public static final RegistryObject<SoundEvent> SENTINEL_FAR = REGISTRY.register("sentinel_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_far")));
    public static final RegistryObject<SoundEvent> SENTINEL_VERYFAR = REGISTRY.register("sentinel_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_veryfar")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FAR = REGISTRY.register("sentinel_charge_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_charge_far")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_VERYFAR = REGISTRY.register("sentinel_charge_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_charge_veryfar")));
    public static final RegistryObject<SoundEvent> SENTINEL_RELOAD_NORMAL = REGISTRY.register("sentinel_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_reload_normal")));
    public static final RegistryObject<SoundEvent> SENTINEL_RELOAD_EMPTY = REGISTRY.register("sentinel_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_reload_empty")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE = REGISTRY.register("sentinel_charge", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_charge")));
    public static final RegistryObject<SoundEvent> SENTINEL_BOLT = REGISTRY.register("sentinel_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("sentinel_bolt")));

    public static final RegistryObject<SoundEvent> M_60_FIRE_1P = REGISTRY.register("m_60_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_fire_1p")));
    public static final RegistryObject<SoundEvent> M_60_FIRE_3P = REGISTRY.register("m_60_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_fire_3p")));
    public static final RegistryObject<SoundEvent> M_60_FAR = REGISTRY.register("m_60_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_far")));
    public static final RegistryObject<SoundEvent> M_60_VERYFAR = REGISTRY.register("m_60_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_veryfar")));
    public static final RegistryObject<SoundEvent> M_60_RELOAD_NORMAL = REGISTRY.register("m_60_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_reload_normal")));
    public static final RegistryObject<SoundEvent> M_60_RELOAD_EMPTY = REGISTRY.register("m_60_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_60_reload_empty")));

    public static final RegistryObject<SoundEvent> SVD_FIRE_1P = REGISTRY.register("svd_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_fire_1p")));
    public static final RegistryObject<SoundEvent> SVD_FIRE_3P = REGISTRY.register("svd_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_fire_3p")));
    public static final RegistryObject<SoundEvent> SVD_FAR = REGISTRY.register("svd_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_far")));
    public static final RegistryObject<SoundEvent> SVD_VERYFAR = REGISTRY.register("svd_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_veryfar")));
    public static final RegistryObject<SoundEvent> SVD_RELOAD_NORMAL = REGISTRY.register("svd_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_reload_normal")));
    public static final RegistryObject<SoundEvent> SVD_RELOAD_EMPTY = REGISTRY.register("svd_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("svd_reload_empty")));

    public static final RegistryObject<SoundEvent> M_98B_FIRE_1P = REGISTRY.register("m_98b_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_fire_1p")));
    public static final RegistryObject<SoundEvent> M_98B_FIRE_3P = REGISTRY.register("m_98b_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_fire_3p")));
    public static final RegistryObject<SoundEvent> M_98B_FAR = REGISTRY.register("m_98b_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_far")));
    public static final RegistryObject<SoundEvent> M_98B_VERYFAR = REGISTRY.register("m_98b_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_veryfar")));
    public static final RegistryObject<SoundEvent> M_98B_RELOAD_NORMAL = REGISTRY.register("m_98b_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_reload_normal")));
    public static final RegistryObject<SoundEvent> M_98B_RELOAD_EMPTY = REGISTRY.register("m_98b_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_reload_empty")));
    public static final RegistryObject<SoundEvent> M_98B_BOLT = REGISTRY.register("m_98b_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_98b_bolt")));

    public static final RegistryObject<SoundEvent> MARLIN_FIRE_1P = REGISTRY.register("marlin_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_fire_1p")));
    public static final RegistryObject<SoundEvent> MARLIN_FIRE_3P = REGISTRY.register("marlin_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_fire_3p")));
    public static final RegistryObject<SoundEvent> MARLIN_FAR = REGISTRY.register("marlin_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_far")));
    public static final RegistryObject<SoundEvent> MARLIN_VERYFAR = REGISTRY.register("marlin_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_veryfar")));
    public static final RegistryObject<SoundEvent> MARLIN_PREPARE = REGISTRY.register("marlin_prepare", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_prepare")));
    public static final RegistryObject<SoundEvent> MARLIN_LOOP = REGISTRY.register("marlin_loop", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_loop")));
    public static final RegistryObject<SoundEvent> MARLIN_END = REGISTRY.register("marlin_end", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_end")));
    public static final RegistryObject<SoundEvent> MARLIN_BOLT = REGISTRY.register("marlin_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("marlin_bolt")));

    public static final RegistryObject<SoundEvent> M_870_FIRE_1P = REGISTRY.register("m_870_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_fire_1p")));
    public static final RegistryObject<SoundEvent> M_870_FIRE_3P = REGISTRY.register("m_870_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_fire_3p")));
    public static final RegistryObject<SoundEvent> M_870_FAR = REGISTRY.register("m_870_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_far")));
    public static final RegistryObject<SoundEvent> M_870_VERYFAR = REGISTRY.register("m_870_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_veryfar")));
    public static final RegistryObject<SoundEvent> M_870_PREPARE_LOAD = REGISTRY.register("m_870_prepare_load", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_prepare_load")));
    public static final RegistryObject<SoundEvent> M_870_LOOP = REGISTRY.register("m_870_loop", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_loop")));
    public static final RegistryObject<SoundEvent> M_870_BOLT = REGISTRY.register("m_870_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_870_bolt")));

    public static final RegistryObject<SoundEvent> GLOCK_17_FIRE_1P = REGISTRY.register("glock_17_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_fire_1p")));
    public static final RegistryObject<SoundEvent> GLOCK_17_FIRE_3P = REGISTRY.register("glock_17_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_fire_3p")));
    public static final RegistryObject<SoundEvent> GLOCK_17_FAR = REGISTRY.register("glock_17_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_far")));
    public static final RegistryObject<SoundEvent> GLOCK_17_VERYFAR = REGISTRY.register("glock_17_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_veryfar")));
    public static final RegistryObject<SoundEvent> GLOCK_17_RELOAD_NORMAL = REGISTRY.register("glock_17_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_normal")));
    public static final RegistryObject<SoundEvent> GLOCK_17_RELOAD_EMPTY = REGISTRY.register("glock_17_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_empty")));

    public static final RegistryObject<SoundEvent> GLOCK_18_FIRE_1P = REGISTRY.register("glock_18_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_fire_1p")));
    public static final RegistryObject<SoundEvent> GLOCK_18_FIRE_3P = REGISTRY.register("glock_18_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_fire_3p")));
    public static final RegistryObject<SoundEvent> GLOCK_18_FAR = REGISTRY.register("glock_18_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_far")));
    public static final RegistryObject<SoundEvent> GLOCK_18_VERYFAR = REGISTRY.register("glock_18_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_veryfar")));
    public static final RegistryObject<SoundEvent> GLOCK_18_RELOAD_NORMAL = REGISTRY.register("glock_18_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_normal")));
    public static final RegistryObject<SoundEvent> GLOCK_18_RELOAD_EMPTY = REGISTRY.register("glock_18_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_empty")));

    public static final RegistryObject<SoundEvent> MP_443_FIRE_1P = REGISTRY.register("mp_443_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mp_443_fire_1p")));
    public static final RegistryObject<SoundEvent> MP_443_FIRE_3P = REGISTRY.register("mp_443_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mp_443_fire_3p")));
    public static final RegistryObject<SoundEvent> MP_443_FAR = REGISTRY.register("mp_443_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_far")));
    public static final RegistryObject<SoundEvent> MP_443_VERYFAR = REGISTRY.register("mp_443_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_veryfar")));
    public static final RegistryObject<SoundEvent> MP_443_RELOAD_NORMAL = REGISTRY.register("mp_443_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_normal")));
    public static final RegistryObject<SoundEvent> MP_443_RELOAD_EMPTY = REGISTRY.register("mp_443_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_empty")));

    public static final RegistryObject<SoundEvent> M_1911_FIRE_1P = REGISTRY.register("m_1911_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_1911_fire_1p")));
    public static final RegistryObject<SoundEvent> M_1911_FIRE_3P = REGISTRY.register("m_1911_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_1911_fire_3p")));
    public static final RegistryObject<SoundEvent> M_1911_FAR = REGISTRY.register("m_1911_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_1911_far")));
    public static final RegistryObject<SoundEvent> M_1911_VERYFAR = REGISTRY.register("m_1911_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m_1911_veryfar")));
    public static final RegistryObject<SoundEvent> M_1911_RELOAD_NORMAL = REGISTRY.register("m_1911_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_normal")));
    public static final RegistryObject<SoundEvent> M_1911_RELOAD_EMPTY = REGISTRY.register("m_1911_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("glock_17_reload_empty")));

    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_1P = REGISTRY.register("qbz_95_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_fire_1p")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_3P = REGISTRY.register("qbz_95_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_fire_3p")));
    public static final RegistryObject<SoundEvent> QBZ_95_FAR = REGISTRY.register("qbz_95_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_far")));
    public static final RegistryObject<SoundEvent> QBZ_95_VERYFAR = REGISTRY.register("qbz_95_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_veryfar")));
    public static final RegistryObject<SoundEvent> QBZ_95_RELOAD_NORMAL = REGISTRY.register("qbz_95_reload_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_reload_normal")));
    public static final RegistryObject<SoundEvent> QBZ_95_RELOAD_EMPTY = REGISTRY.register("qbz_95_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_reload_empty")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_1P_S = REGISTRY.register("qbz_95_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_fire_1p_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_3P_S = REGISTRY.register("qbz_95_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("qbz_95_fire_3p_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_FAR_S = REGISTRY.register("qbz_95_far_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_far_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_VERYFAR_S = REGISTRY.register("qbz_95_veryfar_s", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> K_98_FIRE_1P = REGISTRY.register("k_98_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_fire_1p")));
    public static final RegistryObject<SoundEvent> K_98_FIRE_3P = REGISTRY.register("k_98_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_fire_3p")));
    public static final RegistryObject<SoundEvent> K_98_FAR = REGISTRY.register("k_98_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_far")));
    public static final RegistryObject<SoundEvent> K_98_VERYFAR = REGISTRY.register("k_98_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_veryfar")));
    public static final RegistryObject<SoundEvent> K_98_RELOAD_EMPTY = REGISTRY.register("k_98_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_reload_empty")));
    public static final RegistryObject<SoundEvent> K_98_BOLT = REGISTRY.register("k_98_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_bolt")));
    public static final RegistryObject<SoundEvent> K_98_PREPARE = REGISTRY.register("k_98_prepare", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_prepare")));
    public static final RegistryObject<SoundEvent> K_98_LOOP = REGISTRY.register("k_98_loop", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_loop")));
    public static final RegistryObject<SoundEvent> K_98_END = REGISTRY.register("k_98_end", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("k_98_end")));

    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FIRE_1P = REGISTRY.register("mosin_nagant_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_fire_1p")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FIRE_3P = REGISTRY.register("mosin_nagant_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_fire_3p")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FAR = REGISTRY.register("mosin_nagant_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_far")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_VERYFAR = REGISTRY.register("mosin_nagant_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_veryfar")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_BOLT = REGISTRY.register("mosin_nagant_bolt", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_bolt")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_PREPARE = REGISTRY.register("mosin_nagant_prepare", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_prepare")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_PREPARE_EMPTY = REGISTRY.register("mosin_nagant_prepare_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_prepare_empty")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_LOOP = REGISTRY.register("mosin_nagant_loop", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_loop")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_END = REGISTRY.register("mosin_nagant_end", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mosin_nagant_end")));

    public static final RegistryObject<SoundEvent> JAVELIN_FIRE_1P = REGISTRY.register("javelin_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_fire_1p")));
    public static final RegistryObject<SoundEvent> JAVELIN_FIRE_3P = REGISTRY.register("javelin_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_fire_3p")));
    public static final RegistryObject<SoundEvent> JAVELIN_FAR = REGISTRY.register("javelin_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_far")));
    public static final RegistryObject<SoundEvent> JAVELIN_RELOAD_EMPTY = REGISTRY.register("javelin_reload_empty", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_reload_empty")));

    public static final RegistryObject<SoundEvent> JAVELIN_LOCK = REGISTRY.register("javelin_lock", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_lock")));
    public static final RegistryObject<SoundEvent> JAVELIN_LOCKON = REGISTRY.register("javelin_lockon", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("javelin_lockon")));

    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FIRE_1P = REGISTRY.register("secondary_cataclysm_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_fire_1p")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FIRE_3P = REGISTRY.register("secondary_cataclysm_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_fire_3p")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FAR = REGISTRY.register("secondary_cataclysm_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_far")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_VERYFAR = REGISTRY.register("secondary_cataclysm_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_veryfar")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FIRE_1P_CHARGE = REGISTRY.register("secondary_cataclysm_fire_1p_charge", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_fire_1p_charge")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FIRE_3P_CHARGE = REGISTRY.register("secondary_cataclysm_fire_3p_charge", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_fire_3p_charge")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_FAR_CHARGE = REGISTRY.register("secondary_cataclysm_far_charge", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_far_charge")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_VERYFAR_CHARGE = REGISTRY.register("secondary_cataclysm_veryfar_charge", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_veryfar_charge")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_PREPARE_LOAD = REGISTRY.register("secondary_cataclysm_prepare_load", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_prepare_load")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_LOOP = REGISTRY.register("secondary_cataclysm_loop", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_loop")));
    public static final RegistryObject<SoundEvent> SECONDARY_CATACLYSM_END = REGISTRY.register("secondary_cataclysm_end", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("secondary_cataclysm_end")));

    public static final RegistryObject<SoundEvent> M_2_FIRE_1P = REGISTRY.register("m2_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m2_fire_1p")));
    public static final RegistryObject<SoundEvent> M_2_FIRE_3P = REGISTRY.register("m2_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m2_fire_3p")));
    public static final RegistryObject<SoundEvent> M_2_FAR = REGISTRY.register("m2_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m2_far")));
    public static final RegistryObject<SoundEvent> M_2_VERYFAR = REGISTRY.register("m2_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("m2_veryfar")));

    public static final RegistryObject<SoundEvent> MK_42_FIRE_1P = REGISTRY.register("mk_42_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_42_fire_1p")));
    public static final RegistryObject<SoundEvent> MK_42_FIRE_3P = REGISTRY.register("mk_42_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_42_fire_3p")));
    public static final RegistryObject<SoundEvent> MK_42_FAR = REGISTRY.register("mk_42_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_42_far")));
    public static final RegistryObject<SoundEvent> MK_42_VERYFAR = REGISTRY.register("mk_42_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_42_veryfar")));
    public static final RegistryObject<SoundEvent> MK_42_RELOAD = REGISTRY.register("mk_42_reload", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("mk_42_reload")));
    public static final RegistryObject<SoundEvent> CANNON_ZOOM_IN = REGISTRY.register("cannon_zoom_in", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("cannon_zoom_in")));
    public static final RegistryObject<SoundEvent> CANNON_ZOOM_OUT = REGISTRY.register("cannon_zoom_out", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("cannon_zoom_out")));

    public static final RegistryObject<SoundEvent> BULLET_SUPPLY = REGISTRY.register("bullet_supply", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("bullet_supply")));
    public static final RegistryObject<SoundEvent> ADJUST_FOV = REGISTRY.register("adjust_fov", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("adjust_fov")));
    public static final RegistryObject<SoundEvent> DRONE_SOUND = REGISTRY.register("drone_sound", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("drone_sound")));
    public static final RegistryObject<SoundEvent> GRENADE_PULL = REGISTRY.register("grenade_pull", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("grenade_pull")));
    public static final RegistryObject<SoundEvent> GRENADE_THROW = REGISTRY.register("grenade_throw", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("grenade_throw")));

    public static final RegistryObject<SoundEvent> EDIT_MODE = REGISTRY.register("edit_mode", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("edit_mode")));
    public static final RegistryObject<SoundEvent> EDIT = REGISTRY.register("edit", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("edit")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_NORMAL = REGISTRY.register("shell_casing_normal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("shell_casing_normal")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_SHOTGUN = REGISTRY.register("shell_casing_shotgun", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("shell_casing_shotgun")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_50CAL = REGISTRY.register("shell_casing_50cal", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("shell_casing_50cal")));

    public static final RegistryObject<SoundEvent> OPEN = REGISTRY.register("open", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("open")));

    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_1P = REGISTRY.register("charge_rifle_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("charge_rifle_fire_1p")));
    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_3P = REGISTRY.register("charge_rifle_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("charge_rifle_fire_3p")));
    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_BOOM_1P = REGISTRY.register("charge_rifle_fire_boom_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("charge_rifle_fire_boom_1p")));
    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_BOOM_3P = REGISTRY.register("charge_rifle_fire_boom_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("charge_rifle_fire_boom_3p")));

    public static final RegistryObject<SoundEvent> ANNIHILATOR_FIRE_1P = REGISTRY.register("annihilator_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("annihilator_fire_1p")));
    public static final RegistryObject<SoundEvent> ANNIHILATOR_FIRE_3P = REGISTRY.register("annihilator_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("annihilator_fire_3p")));
    public static final RegistryObject<SoundEvent> ANNIHILATOR_FAR = REGISTRY.register("annihilator_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("annihilator_far")));
    public static final RegistryObject<SoundEvent> ANNIHILATOR_VERYFAR = REGISTRY.register("annihilator_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("annihilator_veryfar")));
    public static final RegistryObject<SoundEvent> ANNIHILATOR_RELOAD = REGISTRY.register("annihilator_reload", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("annihilator_reload")));

    public static final RegistryObject<SoundEvent> BOAT_ENGINE = REGISTRY.register("boat_engine", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("boat_engine")));
    public static final RegistryObject<SoundEvent> VEHICLE_STRIKE = REGISTRY.register("vehicle_strike", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("vehicle_strike")));
    public static final RegistryObject<SoundEvent> WHEEL_CHAIR_ENGINE = REGISTRY.register("wheel_chair_engine", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("wheel_chair_engine")));
    public static final RegistryObject<SoundEvent> WHEEL_CHAIR_JUMP = REGISTRY.register("wheel_chair_jump", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("wheel_chair_jump")));

    public static final RegistryObject<SoundEvent> RADAR_SEARCH_START = REGISTRY.register("radar_search_start", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("radar_search_start")));
    public static final RegistryObject<SoundEvent> RADAR_SEARCH_IDLE = REGISTRY.register("radar_search_idle", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("radar_search_idle")));
    public static final RegistryObject<SoundEvent> RADAR_SEARCH_END = REGISTRY.register("radar_search_end", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("radar_search_end")));

    public static final RegistryObject<SoundEvent> HELICOPTER_ENGINE_START = REGISTRY.register("helicopter_engine_start", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("helicopter_engine_start")));
    public static final RegistryObject<SoundEvent> HELICOPTER_ENGINE = REGISTRY.register("helicopter_engine", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("helicopter_engine")));
    public static final RegistryObject<SoundEvent> HELICOPTER_ENGINE_1P = REGISTRY.register("helicopter_engine_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("helicopter_engine_1p")));
    public static final RegistryObject<SoundEvent> HELICOPTER_CANNON_FIRE_1P = REGISTRY.register("heli_cannon_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_cannon_fire_1p")));
    public static final RegistryObject<SoundEvent> HELICOPTER_CANNON_FIRE_3P = REGISTRY.register("heli_cannon_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_cannon_fire_3p")));
    public static final RegistryObject<SoundEvent> HELICOPTER_CANNON_FAR = REGISTRY.register("heli_cannon_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_cannon_far")));
    public static final RegistryObject<SoundEvent> HELICOPTER_CANNON_VERYFAR = REGISTRY.register("heli_cannon_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_cannon_veryfar")));
    public static final RegistryObject<SoundEvent> HELICOPTER_ROCKET_FIRE_1P = REGISTRY.register("heli_rocket_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_rocket_fire_1p")));
    public static final RegistryObject<SoundEvent> HELICOPTER_ROCKET_FIRE_3P = REGISTRY.register("heli_rocket_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("heli_rocket_fire_3p")));
    public static final RegistryObject<SoundEvent> INTO_CANNON = REGISTRY.register("into_cannon", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("into_cannon")));
    public static final RegistryObject<SoundEvent> INTO_MISSILE = REGISTRY.register("into_missile", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("into_missile")));
    public static final RegistryObject<SoundEvent> MISSILE_RELOAD = REGISTRY.register("missile_reload", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("missile_reload")));
    public static final RegistryObject<SoundEvent> LOW_HEALTH = REGISTRY.register("low_health", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("low_health")));
    public static final RegistryObject<SoundEvent> NO_HEALTH = REGISTRY.register("no_health", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("no_health")));
    public static final RegistryObject<SoundEvent> LOCKING_WARNING = REGISTRY.register("locking_warning", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("locking_warning")));
    public static final RegistryObject<SoundEvent> LOCKED_WARNING = REGISTRY.register("locked_warning", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("locked_warning")));
    public static final RegistryObject<SoundEvent> MISSILE_WARNING = REGISTRY.register("missile_warning", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("missile_warning")));
    public static final RegistryObject<SoundEvent> DECOY_FIRE = REGISTRY.register("decoy_fire", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("decoy_fire")));
    public static final RegistryObject<SoundEvent> DECOY_RELOAD = REGISTRY.register("decoy_reload", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("decoy_reload")));
    public static final RegistryObject<SoundEvent> LUNGE_MINE_GROWL = REGISTRY.register("lunge_mine_growl", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lunge_mine_growl")));
    public static final RegistryObject<SoundEvent> LAV_CANNON_FIRE_1P = REGISTRY.register("lav_cannon_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_fire_1p")));
    public static final RegistryObject<SoundEvent> LAV_CANNON_FIRE_3P = REGISTRY.register("lav_cannon_fire_3p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_fire_3p")));
    public static final RegistryObject<SoundEvent> LAV_CANNON_FAR = REGISTRY.register("lav_cannon_far", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_far")));
    public static final RegistryObject<SoundEvent> LAV_CANNON_VERYFAR = REGISTRY.register("lav_cannon_veryfar", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_veryfar")));
    public static final RegistryObject<SoundEvent> LAV_ENGINE = REGISTRY.register("lav_engine", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_engine")));
    public static final RegistryObject<SoundEvent> LAV_ENGINE_1P = REGISTRY.register("lav_engine_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("lav_engine_1p")));
    public static final RegistryObject<SoundEvent> COAX_FIRE_1P = REGISTRY.register("coax_fire_1p", () -> SoundEvent.createVariableRangeEvent(ModUtils.loc("coax_fire_1p")));
}

