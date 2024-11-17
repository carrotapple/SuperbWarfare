package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.mobeffect.BurnMobEffect;
import net.mcreator.superbwarfare.mobeffect.ShockMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ModUtils.MODID);

    public static final RegistryObject<MobEffect> SHOCK = REGISTRY.register("shock", ShockMobEffect::new);
    public static final RegistryObject<MobEffect> BURN = REGISTRY.register("burn", BurnMobEffect::new);
}
