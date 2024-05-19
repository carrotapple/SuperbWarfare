package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.mobeffect.ShockMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TargetModMobEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TargetMod.MODID);

    public static final RegistryObject<MobEffect> SHOCK = REGISTRY.register("shock", ShockMobEffect::new);
}
