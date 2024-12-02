package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModUtils.MODID);

    public static final RegistryObject<SoundEvent> TASER_FIRE_1P = REGISTRY.register("taser_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "taser_fire_1p")));
    public static final RegistryObject<SoundEvent> TASER_FIRE_3P = REGISTRY.register("taser_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "taser_fire_3p")));
    public static final RegistryObject<SoundEvent> TASER_RELOAD_EMPTY = REGISTRY.register("taser_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "taser_reload_empty")));

    public static final RegistryObject<SoundEvent> SHOCK = REGISTRY.register("shock", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "shock")));
    public static final RegistryObject<SoundEvent> ELECTRIC = REGISTRY.register("electric", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "electric")));

    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_1P = REGISTRY.register("trachelium_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_fire_1p")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_3P = REGISTRY.register("trachelium_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_fire_3p")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FAR = REGISTRY.register("trachelium_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_far")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_VERYFAR = REGISTRY.register("trachelium_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_veryfar")));

    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_1P_S = REGISTRY.register("trachelium_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_fire_1p_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FIRE_3P_S = REGISTRY.register("trachelium_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_fire_3p_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_FAR_S = REGISTRY.register("trachelium_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_far_s")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_RELOAD_EMPTY = REGISTRY.register("trachelium_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_reload_empty")));
    public static final RegistryObject<SoundEvent> TRACHELIUM_BOLT = REGISTRY.register("trachelium_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "trachelium_bolt")));

    public static final RegistryObject<SoundEvent> TRIGGER_CLICK = REGISTRY.register("triggerclick", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "triggerclick")));
    public static final RegistryObject<SoundEvent> HIT = REGISTRY.register("hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hit")));
    public static final RegistryObject<SoundEvent> TARGET_DOWN = REGISTRY.register("targetdown", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "targetdown")));
    public static final RegistryObject<SoundEvent> INDICATION = REGISTRY.register("indication", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "indication")));
    public static final RegistryObject<SoundEvent> JUMP = REGISTRY.register("jump", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "jump")));
    public static final RegistryObject<SoundEvent> DOUBLE_JUMP = REGISTRY.register("doublejump", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "doublejump")));

    public static final RegistryObject<SoundEvent> EXPLOSION_CLOSE = REGISTRY.register("explosion_close", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "explosion_close")));
    public static final RegistryObject<SoundEvent> EXPLOSION_FAR = REGISTRY.register("explosion_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "explosion_far")));
    public static final RegistryObject<SoundEvent> EXPLOSION_VERY_FAR = REGISTRY.register("explosion_very_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "explosion_very_far")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_CLOSE = REGISTRY.register("huge_explosion_close", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "huge_explosion_close")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_FAR = REGISTRY.register("huge_explosion_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "huge_explosion_far")));
    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION_VERY_FAR = REGISTRY.register("huge_explosion_very_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "huge_explosion_very_far")));
    public static final RegistryObject<SoundEvent> EXPLOSION_WATER = REGISTRY.register("explosion_water", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "explosion_water")));

    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FIRE_1P = REGISTRY.register("hunting_rifle_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hunting_rifle_fire_1p")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FIRE_3P = REGISTRY.register("hunting_rifle_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hunting_rifle_fire_3p")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_FAR = REGISTRY.register("hunting_rifle_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hunting_rifle_far")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_VERYFAR = REGISTRY.register("hunting_rifle_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hunting_rifle_veryfar")));
    public static final RegistryObject<SoundEvent> HUNTING_RIFLE_RELOAD_EMPTY = REGISTRY.register("hunting_rifle_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hunting_rifle_reload_empty")));

    public static final RegistryObject<SoundEvent> OUCH = REGISTRY.register("ouch", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ouch")));
    public static final RegistryObject<SoundEvent> STEP = REGISTRY.register("step", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "step")));
    public static final RegistryObject<SoundEvent> GROWL = REGISTRY.register("growl", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "growl")));
    public static final RegistryObject<SoundEvent> IDLE = REGISTRY.register("idle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "idle")));
    public static final RegistryObject<SoundEvent> HENG = REGISTRY.register("heng", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "heng")));

    public static final RegistryObject<SoundEvent> M_79_FIRE_1P = REGISTRY.register("m_79_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_79_fire_1p")));
    public static final RegistryObject<SoundEvent> M_79_FIRE_3P = REGISTRY.register("m_79_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_79_fire_3p")));
    public static final RegistryObject<SoundEvent> M_79_FAR = REGISTRY.register("m_79_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_79_far")));
    public static final RegistryObject<SoundEvent> M_79_VERYFAR = REGISTRY.register("m_79_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_79_veryfar")));
    public static final RegistryObject<SoundEvent> M_79_RELOAD_EMPTY = REGISTRY.register("m_79_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_79_reload_empty")));

    public static final RegistryObject<SoundEvent> SKS_FIRE_1P = REGISTRY.register("sks_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_fire_1p")));
    public static final RegistryObject<SoundEvent> SKS_FIRE_3P = REGISTRY.register("sks_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_fire_3p")));
    public static final RegistryObject<SoundEvent> SKS_RELOAD_NORMAL = REGISTRY.register("sks_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_reload_normal")));
    public static final RegistryObject<SoundEvent> SKS_RELOAD_EMPTY = REGISTRY.register("sks_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_reload_empty")));
    public static final RegistryObject<SoundEvent> SKS_FAR = REGISTRY.register("sks_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_far")));
    public static final RegistryObject<SoundEvent> SKS_VERYFAR = REGISTRY.register("sks_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sks_veryfar")));

    public static final RegistryObject<SoundEvent> ABEKIRI_FIRE_1P = REGISTRY.register("abekiri_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_fire_1p")));
    public static final RegistryObject<SoundEvent> ABEKIRI_FIRE_3P = REGISTRY.register("abekiri_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_fire_3p")));
    public static final RegistryObject<SoundEvent> ABEKIRI_FAR = REGISTRY.register("abekiri_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_far")));
    public static final RegistryObject<SoundEvent> ABEKIRI_VERYFAR = REGISTRY.register("abekiri_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_veryfar")));
    public static final RegistryObject<SoundEvent> ABEKIRI_RELOAD_NORMAL = REGISTRY.register("abekiri_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_reload_normal")));
    public static final RegistryObject<SoundEvent> ABEKIRI_RELOAD_EMPTY = REGISTRY.register("abekiri_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "abekiri_reload_empty")));

    public static final RegistryObject<SoundEvent> AK_47_FIRE_1P = REGISTRY.register("ak_47_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_fire_1p")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_3P = REGISTRY.register("ak_47_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_fire_3p")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_1P_S = REGISTRY.register("ak_47_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_fire_1p_s")));
    public static final RegistryObject<SoundEvent> AK_47_FIRE_3P_S = REGISTRY.register("ak_47_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_fire_3p_s")));
    public static final RegistryObject<SoundEvent> AK_47_FAR = REGISTRY.register("ak_47_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_far")));
    public static final RegistryObject<SoundEvent> AK_47_VERYFAR = REGISTRY.register("ak_47_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_veryfar")));
    public static final RegistryObject<SoundEvent> AK_47_FAR_S = REGISTRY.register("ak_47_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far_s")));
    public static final RegistryObject<SoundEvent> AK_47_VERYFAR_S = REGISTRY.register("ak_47_veryfar_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar_s")));
    public static final RegistryObject<SoundEvent> AK_47_RELOAD_NORMAL = REGISTRY.register("ak_47_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_reload_normal")));
    public static final RegistryObject<SoundEvent> AK_47_RELOAD_EMPTY = REGISTRY.register("ak_47_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_47_reload_empty")));

    public static final RegistryObject<SoundEvent> AK_12_FIRE_1P = REGISTRY.register("ak_12_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_fire_1p")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_3P = REGISTRY.register("ak_12_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_fire_3p")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_1P_S = REGISTRY.register("ak_12_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_fire_1p_s")));
    public static final RegistryObject<SoundEvent> AK_12_FIRE_3P_S = REGISTRY.register("ak_12_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_fire_3p_s")));
    public static final RegistryObject<SoundEvent> AK_12_FAR = REGISTRY.register("ak_12_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far")));
    public static final RegistryObject<SoundEvent> AK_12_VERYFAR = REGISTRY.register("ak_12_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar")));
    public static final RegistryObject<SoundEvent> AK_12_FAR_S = REGISTRY.register("ak_12_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far_s")));
    public static final RegistryObject<SoundEvent> AK_12_VERYFAR_S = REGISTRY.register("ak_12_veryfar_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar_s")));
    public static final RegistryObject<SoundEvent> AK_12_RELOAD_NORMAL = REGISTRY.register("ak_12_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_reload_normal")));
    public static final RegistryObject<SoundEvent> AK_12_RELOAD_EMPTY = REGISTRY.register("ak_12_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_reload_empty")));

    public static final RegistryObject<SoundEvent> LAND = REGISTRY.register("land", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "land")));
    public static final RegistryObject<SoundEvent> HEADSHOT = REGISTRY.register("headshot", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "headshot")));

    public static final RegistryObject<SoundEvent> DEVOTION_FIRE_1P = REGISTRY.register("devotion_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_fire_1p")));
    public static final RegistryObject<SoundEvent> DEVOTION_FIRE_3P = REGISTRY.register("devotion_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_fire_3p")));
    public static final RegistryObject<SoundEvent> DEVOTION_FAR = REGISTRY.register("devotion_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_far")));
    public static final RegistryObject<SoundEvent> DEVOTION_VERYFAR = REGISTRY.register("devotion_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_veryfar")));
    public static final RegistryObject<SoundEvent> DEVOTION_RELOAD_NORMAL = REGISTRY.register("devotion_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_reload_normal")));
    public static final RegistryObject<SoundEvent> DEVOTION_RELOAD_EMPTY = REGISTRY.register("devotion_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "devotion_reload_empty")));

    public static final RegistryObject<SoundEvent> RPG_FIRE_1P = REGISTRY.register("rpg_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpg_fire_1p")));
    public static final RegistryObject<SoundEvent> RPG_FIRE_3P = REGISTRY.register("rpg_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpg_fire_3p")));
    public static final RegistryObject<SoundEvent> RPG_FAR = REGISTRY.register("rpg_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpg_far")));
    public static final RegistryObject<SoundEvent> RPG_VERYFAR = REGISTRY.register("rpg_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpg_veryfar")));
    public static final RegistryObject<SoundEvent> RPG_RELOAD_EMPTY = REGISTRY.register("rpg_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpg_reload_empty")));

    public static final RegistryObject<SoundEvent> MORTAR_FIRE = REGISTRY.register("mortar_fire", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mortar_fire")));
    public static final RegistryObject<SoundEvent> MORTAR_LOAD = REGISTRY.register("mortar_load", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mortar_load")));
    public static final RegistryObject<SoundEvent> MORTAR_DISTANT = REGISTRY.register("mortar_distant", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mortar_distant")));

    public static final RegistryObject<SoundEvent> FIRE_RATE = REGISTRY.register("firerate", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "firerate")));

    public static final RegistryObject<SoundEvent> M_4_FIRE_1P = REGISTRY.register("m_4_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_fire_1p")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_3P = REGISTRY.register("m_4_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_fire_3p")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_1P_S = REGISTRY.register("m_4_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_fire_1p_s")));
    public static final RegistryObject<SoundEvent> M_4_FIRE_3P_S = REGISTRY.register("m_4_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_fire_3p_s")));
    public static final RegistryObject<SoundEvent> M_4_FAR = REGISTRY.register("m_4_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_far")));
    public static final RegistryObject<SoundEvent> M_4_VERYFAR = REGISTRY.register("m_4_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_veryfar")));
    public static final RegistryObject<SoundEvent> M_4_RELOAD_NORMAL = REGISTRY.register("m_4_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_reload_normal")));
    public static final RegistryObject<SoundEvent> M_4_RELOAD_EMPTY = REGISTRY.register("m_4_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_4_reload_empty")));
    public static final RegistryObject<SoundEvent> M_4_FAR_S = REGISTRY.register("m_4_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far_s")));
    public static final RegistryObject<SoundEvent> M_4_VERYFAR_S = REGISTRY.register("m_4_veryfar_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> AA_12_FIRE_1P = REGISTRY.register("aa_12_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_fire_1p")));
    public static final RegistryObject<SoundEvent> AA_12_FIRE_3P = REGISTRY.register("aa_12_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_fire_3p")));
    public static final RegistryObject<SoundEvent> AA_12_FAR = REGISTRY.register("aa_12_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_far")));
    public static final RegistryObject<SoundEvent> AA_12_VERYFAR = REGISTRY.register("aa_12_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_veryfar")));
    public static final RegistryObject<SoundEvent> AA_12_RELOAD_NORMAL = REGISTRY.register("aa_12_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_reload_normal")));
    public static final RegistryObject<SoundEvent> AA_12_RELOAD_EMPTY = REGISTRY.register("aa_12_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "aa_12_reload_empty")));

    public static final RegistryObject<SoundEvent> BOCEK_ZOOM_FIRE_1P = REGISTRY.register("bocek_zoom_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_zoom_fire_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_ZOOM_FIRE_3P = REGISTRY.register("bocek_zoom_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_zoom_fire_3p")));
    public static final RegistryObject<SoundEvent> BOCEK_SHATTER_CAP_FIRE_1P = REGISTRY.register("bocek_shatter_cap_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_shatter_cap_fire_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_SHATTER_CAP_FIRE_3P = REGISTRY.register("bocek_shatter_cap_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_shatter_cap_fire_3p")));
    public static final RegistryObject<SoundEvent> BOCEK_PULL_1P = REGISTRY.register("bocek_pull_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_pull_1p")));
    public static final RegistryObject<SoundEvent> BOCEK_PULL_3P = REGISTRY.register("bocek_pull_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bocek_pull_3p")));

    public static final RegistryObject<SoundEvent> HK_416_FIRE_1P = REGISTRY.register("hk_416_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_fire_1p")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_3P = REGISTRY.register("hk_416_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_fire_3p")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_1P_S = REGISTRY.register("hk_416_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_fire_1p_s")));
    public static final RegistryObject<SoundEvent> HK_416_FIRE_3P_S = REGISTRY.register("hk_416_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_fire_3p_s")));
    public static final RegistryObject<SoundEvent> HK_416_FAR = REGISTRY.register("hk_416_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_far")));
    public static final RegistryObject<SoundEvent> HK_416_VERYFAR = REGISTRY.register("hk_416_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_veryfar")));
    public static final RegistryObject<SoundEvent> HK_416_RELOAD_NORMAL = REGISTRY.register("hk_416_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_reload_normal")));
    public static final RegistryObject<SoundEvent> HK_416_RELOAD_EMPTY = REGISTRY.register("hk_416_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "hk_416_reload_empty")));
    public static final RegistryObject<SoundEvent> HK_416_FAR_S = REGISTRY.register("hk_416_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far_s")));
    public static final RegistryObject<SoundEvent> HK_416_VERYFAR_S = REGISTRY.register("hk_416_veryfar_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> RPK_FIRE_1P = REGISTRY.register("rpk_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_fire_1p")));
    public static final RegistryObject<SoundEvent> RPK_FIRE_3P = REGISTRY.register("rpk_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_fire_3p")));
    public static final RegistryObject<SoundEvent> RPK_FAR = REGISTRY.register("rpk_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_far")));
    public static final RegistryObject<SoundEvent> RPK_VERYFAR = REGISTRY.register("rpk_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_veryfar")));
    public static final RegistryObject<SoundEvent> RPK_RELOAD_NORMAL = REGISTRY.register("rpk_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_reload_normal")));
    public static final RegistryObject<SoundEvent> RPK_RELOAD_EMPTY = REGISTRY.register("rpk_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "rpk_reload_empty")));

    public static final RegistryObject<SoundEvent> NTW_20_FIRE_1P = REGISTRY.register("ntw_20_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_fire_1p")));
    public static final RegistryObject<SoundEvent> NTW_20_FIRE_3P = REGISTRY.register("ntw_20_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_fire_3p")));
    public static final RegistryObject<SoundEvent> NTW_20_FAR = REGISTRY.register("ntw_20_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_far")));
    public static final RegistryObject<SoundEvent> NTW_20_VERYFAR = REGISTRY.register("ntw_20_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_veryfar")));
    public static final RegistryObject<SoundEvent> NTW_20_RELOAD_NORMAL = REGISTRY.register("ntw_20_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_reload_normal")));
    public static final RegistryObject<SoundEvent> NTW_20_RELOAD_EMPTY = REGISTRY.register("ntw_20_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_reload_empty")));
    public static final RegistryObject<SoundEvent> NTW_20_BOLT = REGISTRY.register("ntw_20_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ntw_20_bolt")));

    public static final RegistryObject<SoundEvent> VECTOR_FIRE_1P = REGISTRY.register("vector_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_fire_1p")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_3P = REGISTRY.register("vector_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_fire_3p")));
    public static final RegistryObject<SoundEvent> VECTOR_FAR = REGISTRY.register("vector_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_far")));
    public static final RegistryObject<SoundEvent> VECTOR_VERYFAR = REGISTRY.register("vector_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_veryfar")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_1P_S = REGISTRY.register("vector_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_fire_1p_s")));
    public static final RegistryObject<SoundEvent> VECTOR_FIRE_3P_S = REGISTRY.register("vector_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_fire_3p_s")));
    public static final RegistryObject<SoundEvent> VECTOR_FAR_S = REGISTRY.register("vector_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_far_s")));
    public static final RegistryObject<SoundEvent> VECTOR_RELOAD_NORMAL = REGISTRY.register("vector_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_reload_normal")));
    public static final RegistryObject<SoundEvent> VECTOR_RELOAD_EMPTY = REGISTRY.register("vector_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "vector_reload_empty")));

    public static final RegistryObject<SoundEvent> MINIGUN_FIRE_1P = REGISTRY.register("minigun_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_fire_1p")));
    public static final RegistryObject<SoundEvent> MINIGUN_FIRE_3P = REGISTRY.register("minigun_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_fire_3p")));
    public static final RegistryObject<SoundEvent> MINIGUN_FAR = REGISTRY.register("minigun_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_far")));
    public static final RegistryObject<SoundEvent> MINIGUN_VERYFAR = REGISTRY.register("minigun_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_veryfar")));
    public static final RegistryObject<SoundEvent> MINIGUN_ROT = REGISTRY.register("minigun_rot", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_rot")));
    public static final RegistryObject<SoundEvent> MINIGUN_OVERHEAT = REGISTRY.register("minigun_overheat", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "minigun_overheat")));

    public static final RegistryObject<SoundEvent> MK_14_FIRE_1P = REGISTRY.register("mk_14_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_fire_1p")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_3P = REGISTRY.register("mk_14_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_fire_3p")));
    public static final RegistryObject<SoundEvent> MK_14_FAR = REGISTRY.register("mk_14_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_far")));
    public static final RegistryObject<SoundEvent> MK_14_VERYFAR = REGISTRY.register("mk_14_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_veryfar")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_1P_S = REGISTRY.register("mk_14_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_fire_1p_s")));
    public static final RegistryObject<SoundEvent> MK_14_FIRE_3P_S  = REGISTRY.register("mk_14_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_fire_3p_s")));
    public static final RegistryObject<SoundEvent> MK_14_FAR_S  = REGISTRY.register("mk_14_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_far_s")));
    public static final RegistryObject<SoundEvent> MK_14_RELOAD_NORMAL = REGISTRY.register("mk_14_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_reload_normal")));
    public static final RegistryObject<SoundEvent> MK_14_RELOAD_EMPTY = REGISTRY.register("mk_14_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_14_reload_empty")));

    public static final RegistryObject<SoundEvent> SENTINEL_FIRE_1P = REGISTRY.register("sentinel_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_fire_1p")));
    public static final RegistryObject<SoundEvent> SENTINEL_FIRE_3P = REGISTRY.register("sentinel_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_fire_3p")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FIRE_1P = REGISTRY.register("sentinel_charge_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_charge_fire_1p")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FIRE_3P = REGISTRY.register("sentinel_charge_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_charge_fire_3p")));
    public static final RegistryObject<SoundEvent> SENTINEL_FAR = REGISTRY.register("sentinel_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_far")));
    public static final RegistryObject<SoundEvent> SENTINEL_VERYFAR = REGISTRY.register("sentinel_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_veryfar")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_FAR = REGISTRY.register("sentinel_charge_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_charge_far")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE_VERYFAR = REGISTRY.register("sentinel_charge_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_charge_veryfar")));
    public static final RegistryObject<SoundEvent> SENTINEL_RELOAD_NORMAL = REGISTRY.register("sentinel_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_reload_normal")));
    public static final RegistryObject<SoundEvent> SENTINEL_RELOAD_EMPTY = REGISTRY.register("sentinel_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_reload_empty")));
    public static final RegistryObject<SoundEvent> SENTINEL_CHARGE = REGISTRY.register("sentinel_charge", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_charge")));
    public static final RegistryObject<SoundEvent> SENTINEL_BOLT = REGISTRY.register("sentinel_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "sentinel_bolt")));

    public static final RegistryObject<SoundEvent> M_60_FIRE_1P = REGISTRY.register("m_60_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_fire_1p")));
    public static final RegistryObject<SoundEvent> M_60_FIRE_3P = REGISTRY.register("m_60_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_fire_3p")));
    public static final RegistryObject<SoundEvent> M_60_FAR = REGISTRY.register("m_60_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_far")));
    public static final RegistryObject<SoundEvent> M_60_VERYFAR = REGISTRY.register("m_60_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_veryfar")));
    public static final RegistryObject<SoundEvent> M_60_RELOAD_NORMAL = REGISTRY.register("m_60_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_reload_normal")));
    public static final RegistryObject<SoundEvent> M_60_RELOAD_EMPTY = REGISTRY.register("m_60_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_60_reload_empty")));

    public static final RegistryObject<SoundEvent> SVD_FIRE_1P = REGISTRY.register("svd_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_fire_1p")));
    public static final RegistryObject<SoundEvent> SVD_FIRE_3P = REGISTRY.register("svd_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_fire_3p")));
    public static final RegistryObject<SoundEvent> SVD_FAR = REGISTRY.register("svd_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_far")));
    public static final RegistryObject<SoundEvent> SVD_VERYFAR = REGISTRY.register("svd_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_veryfar")));
    public static final RegistryObject<SoundEvent> SVD_RELOAD_NORMAL = REGISTRY.register("svd_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_reload_normal")));
    public static final RegistryObject<SoundEvent> SVD_RELOAD_EMPTY = REGISTRY.register("svd_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "svd_reload_empty")));

    public static final RegistryObject<SoundEvent> M_98B_FIRE_1P = REGISTRY.register("m_98b_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_fire_1p")));
    public static final RegistryObject<SoundEvent> M_98B_FIRE_3P = REGISTRY.register("m_98b_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_fire_3p")));
    public static final RegistryObject<SoundEvent> M_98B_FAR = REGISTRY.register("m_98b_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_far")));
    public static final RegistryObject<SoundEvent> M_98B_VERYFAR = REGISTRY.register("m_98b_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_veryfar")));
    public static final RegistryObject<SoundEvent> M_98B_RELOAD_NORMAL = REGISTRY.register("m_98b_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_reload_normal")));
    public static final RegistryObject<SoundEvent> M_98B_RELOAD_EMPTY = REGISTRY.register("m_98b_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_reload_empty")));
    public static final RegistryObject<SoundEvent> M_98B_BOLT = REGISTRY.register("m_98b_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_98b_bolt")));

    public static final RegistryObject<SoundEvent> MARLIN_FIRE_1P = REGISTRY.register("marlin_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_fire_1p")));
    public static final RegistryObject<SoundEvent> MARLIN_FIRE_3P = REGISTRY.register("marlin_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_fire_3p")));
    public static final RegistryObject<SoundEvent> MARLIN_FAR = REGISTRY.register("marlin_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_far")));
    public static final RegistryObject<SoundEvent> MARLIN_VERYFAR = REGISTRY.register("marlin_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_veryfar")));
    public static final RegistryObject<SoundEvent> MARLIN_PREPARE = REGISTRY.register("marlin_prepare", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_prepare")));
    public static final RegistryObject<SoundEvent> MARLIN_LOOP = REGISTRY.register("marlin_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_loop")));
    public static final RegistryObject<SoundEvent> MARLIN_END = REGISTRY.register("marlin_end", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_end")));
    public static final RegistryObject<SoundEvent> MARLIN_BOLT = REGISTRY.register("marlin_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "marlin_bolt")));

    public static final RegistryObject<SoundEvent> M_870_FIRE_1P = REGISTRY.register("m_870_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_fire_1p")));
    public static final RegistryObject<SoundEvent> M_870_FIRE_3P = REGISTRY.register("m_870_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_fire_3p")));
    public static final RegistryObject<SoundEvent> M_870_FAR = REGISTRY.register("m_870_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_far")));
    public static final RegistryObject<SoundEvent> M_870_VERYFAR = REGISTRY.register("m_870_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_veryfar")));
    public static final RegistryObject<SoundEvent> M_870_PREPARE_LOAD = REGISTRY.register("m_870_prepare_load", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_prepare_load")));
    public static final RegistryObject<SoundEvent> M_870_LOOP = REGISTRY.register("m_870_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_loop")));
    public static final RegistryObject<SoundEvent> M_870_BOLT = REGISTRY.register("m_870_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_870_bolt")));

    public static final RegistryObject<SoundEvent> GLOCK_17_FIRE_1P = REGISTRY.register("glock_17_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_fire_1p")));
    public static final RegistryObject<SoundEvent> GLOCK_17_FIRE_3P = REGISTRY.register("glock_17_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_fire_3p")));
    public static final RegistryObject<SoundEvent> GLOCK_17_FAR = REGISTRY.register("glock_17_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_far")));
    public static final RegistryObject<SoundEvent> GLOCK_17_VERYFAR = REGISTRY.register("glock_17_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_veryfar")));
    public static final RegistryObject<SoundEvent> GLOCK_17_RELOAD_NORMAL = REGISTRY.register("glock_17_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_reload_normal")));
    public static final RegistryObject<SoundEvent> GLOCK_17_RELOAD_EMPTY = REGISTRY.register("glock_17_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_17_reload_empty")));

    public static final RegistryObject<SoundEvent> GLOCK_18_FIRE_1P = REGISTRY.register("glock_18_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_fire_1p")));
    public static final RegistryObject<SoundEvent> GLOCK_18_FIRE_3P = REGISTRY.register("glock_18_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_fire_3p")));
    public static final RegistryObject<SoundEvent> GLOCK_18_FAR = REGISTRY.register("glock_18_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_far")));
    public static final RegistryObject<SoundEvent> GLOCK_18_VERYFAR = REGISTRY.register("glock_18_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_veryfar")));
    public static final RegistryObject<SoundEvent> GLOCK_18_RELOAD_NORMAL = REGISTRY.register("glock_18_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_reload_normal")));
    public static final RegistryObject<SoundEvent> GLOCK_18_RELOAD_EMPTY = REGISTRY.register("glock_18_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "glock_18_reload_empty")));

    public static final RegistryObject<SoundEvent> M_1911_FIRE_1P = REGISTRY.register("m_1911_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_fire_1p")));
    public static final RegistryObject<SoundEvent> M_1911_FIRE_3P = REGISTRY.register("m_1911_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_fire_3p")));
    public static final RegistryObject<SoundEvent> M_1911_FAR = REGISTRY.register("m_1911_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_far")));
    public static final RegistryObject<SoundEvent> M_1911_VERYFAR = REGISTRY.register("m_1911_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_veryfar")));
    public static final RegistryObject<SoundEvent> M_1911_RELOAD_NORMAL = REGISTRY.register("m_1911_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_reload_normal")));
    public static final RegistryObject<SoundEvent> M_1911_RELOAD_EMPTY = REGISTRY.register("m_1911_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "m_1911_reload_empty")));

    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_1P = REGISTRY.register("qbz_95_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_fire_1p")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_3P = REGISTRY.register("qbz_95_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_fire_3p")));
    public static final RegistryObject<SoundEvent> QBZ_95_FAR = REGISTRY.register("qbz_95_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_far")));
    public static final RegistryObject<SoundEvent> QBZ_95_VERYFAR = REGISTRY.register("qbz_95_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_veryfar")));
    public static final RegistryObject<SoundEvent> QBZ_95_RELOAD_NORMAL = REGISTRY.register("qbz_95_reload_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_reload_normal")));
    public static final RegistryObject<SoundEvent> QBZ_95_RELOAD_EMPTY = REGISTRY.register("qbz_95_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_reload_empty")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_1P_S = REGISTRY.register("qbz_95_fire_1p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_fire_1p_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_FIRE_3P_S = REGISTRY.register("qbz_95_fire_3p_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "qbz_95_fire_3p_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_FAR_S = REGISTRY.register("qbz_95_far_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_far_s")));
    public static final RegistryObject<SoundEvent> QBZ_95_VERYFAR_S = REGISTRY.register("qbz_95_veryfar_s", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "ak_12_veryfar_s")));

    public static final RegistryObject<SoundEvent> K_98_FIRE_1P = REGISTRY.register("k_98_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_fire_1p")));
    public static final RegistryObject<SoundEvent> K_98_FIRE_3P = REGISTRY.register("k_98_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_fire_3p")));
    public static final RegistryObject<SoundEvent> K_98_FAR = REGISTRY.register("k_98_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_far")));
    public static final RegistryObject<SoundEvent> K_98_VERYFAR = REGISTRY.register("k_98_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_veryfar")));
    public static final RegistryObject<SoundEvent> K_98_RELOAD_EMPTY = REGISTRY.register("k_98_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_reload_empty")));
    public static final RegistryObject<SoundEvent> K_98_BOLT = REGISTRY.register("k_98_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_bolt")));
    public static final RegistryObject<SoundEvent> K_98_PREPARE = REGISTRY.register("k_98_prepare", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_prepare")));
    public static final RegistryObject<SoundEvent> K_98_LOOP = REGISTRY.register("k_98_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_loop")));
    public static final RegistryObject<SoundEvent> K_98_END = REGISTRY.register("k_98_end", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "k_98_end")));

    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FIRE_1P = REGISTRY.register("mosin_nagant_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_fire_1p")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FIRE_3P = REGISTRY.register("mosin_nagant_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_fire_3p")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FAR = REGISTRY.register("mosin_nagant_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_far")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_VERYFAR = REGISTRY.register("mosin_nagant_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_veryfar")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_BOLT = REGISTRY.register("mosin_nagant_bolt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_bolt")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_PREPARE = REGISTRY.register("mosin_nagant_prepare", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_prepare")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_PREPARE_EMPTY = REGISTRY.register("mosin_nagant_prepare_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_prepare_empty")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_LOOP = REGISTRY.register("mosin_nagant_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_loop")));
    public static final RegistryObject<SoundEvent> MOSIN_NAGANT_END = REGISTRY.register("mosin_nagant_end", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mosin_nagant_end")));

    public static final RegistryObject<SoundEvent> JAVELIN_FIRE_1P = REGISTRY.register("javelin_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_fire_1p")));
    public static final RegistryObject<SoundEvent> JAVELIN_FIRE_3P = REGISTRY.register("javelin_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_fire_3p")));
    public static final RegistryObject<SoundEvent> JAVELIN_FAR = REGISTRY.register("javelin_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_far")));
    public static final RegistryObject<SoundEvent> JAVELIN_RELOAD_EMPTY = REGISTRY.register("javelin_reload_empty", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_reload_empty")));

    public static final RegistryObject<SoundEvent> JAVELIN_LOCK = REGISTRY.register("javelin_lock", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_lock")));
    public static final RegistryObject<SoundEvent> JAVELIN_LOCKON = REGISTRY.register("javelin_lockon", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "javelin_lockon")));

    public static final RegistryObject<SoundEvent> MK_42_FIRE_1P = REGISTRY.register("mk_42_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_42_fire_1p")));
    public static final RegistryObject<SoundEvent> MK_42_FIRE_3P = REGISTRY.register("mk_42_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_42_fire_3p")));
    public static final RegistryObject<SoundEvent> MK_42_FAR = REGISTRY.register("mk_42_far", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_42_far")));
    public static final RegistryObject<SoundEvent> MK_42_VERYFAR = REGISTRY.register("mk_42_veryfar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_42_veryfar")));
    public static final RegistryObject<SoundEvent> MK_42_RELOAD = REGISTRY.register("mk_42_reload", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "mk_42_reload")));
    public static final RegistryObject<SoundEvent> CANNON_ZOOM_IN = REGISTRY.register("cannon_zoom_in", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "cannon_zoom_in")));
    public static final RegistryObject<SoundEvent> CANNON_ZOOM_OUT = REGISTRY.register("cannon_zoom_out", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "cannon_zoom_out")));

    public static final RegistryObject<SoundEvent> BULLET_SUPPLY = REGISTRY.register("bullet_supply", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "bullet_supply")));
    public static final RegistryObject<SoundEvent> ADJUST_FOV = REGISTRY.register("adjust_fov", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "adjust_fov")));
    public static final RegistryObject<SoundEvent> DRONE_SOUND = REGISTRY.register("drone_sound", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "drone_sound")));
    public static final RegistryObject<SoundEvent> GRENADE_PULL = REGISTRY.register("grenade_pull", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "grenade_pull")));
    public static final RegistryObject<SoundEvent> GRENADE_THROW = REGISTRY.register("grenade_throw", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "grenade_throw")));

    public static final RegistryObject<SoundEvent> EDIT_MODE = REGISTRY.register("edit_mode", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "edit_mode")));
    public static final RegistryObject<SoundEvent> EDIT = REGISTRY.register("edit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "edit")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_NORMAL = REGISTRY.register("shell_casing_normal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "shell_casing_normal")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_SHOTGUN = REGISTRY.register("shell_casing_shotgun", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "shell_casing_shotgun")));
    public static final RegistryObject<SoundEvent> SHELL_CASING_50CAL = REGISTRY.register("shell_casing_50cal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "shell_casing_50cal")));

    public static final RegistryObject<SoundEvent> OPEN = REGISTRY.register("open", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "open")));

    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_1P = REGISTRY.register("charge_rifle_fire_1p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "charge_rifle_fire_1p")));
    public static final RegistryObject<SoundEvent> CHARGE_RIFLE_FIRE_3P = REGISTRY.register("charge_rifle_fire_3p", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ModUtils.MODID, "charge_rifle_fire_3p")));
}
