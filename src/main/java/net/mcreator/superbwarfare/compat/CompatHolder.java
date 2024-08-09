package net.mcreator.superbwarfare.compat;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ObjectHolder;

public class CompatHolder {

    public static final String DMV = "dreamaticvoyage";
    public static final String VRC = "virtuarealcraft";

    @ObjectHolder(registryName = "minecraft:mob_effect", value = DMV + ":bleeding")
    public static final MobEffect DMV_BLEEDING = null;

    @ObjectHolder(registryName = "minecraft:mob_effect", value = VRC + ":curse_flame")
    public static final MobEffect VRC_CURSE_FLAME = null;

}
