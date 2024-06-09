package net.mcreator.target.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mcreator.target.TargetMod;

public class TargetModPotion {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, TargetMod.MODID);

    public static final RegistryObject<Potion> SHOCK= POTIONS.register("target_shock",
            () -> new Potion(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, 0)));
    public static final RegistryObject<Potion> STRONG_SHOCK = POTIONS.register("target_strong_shock",
            () -> new Potion(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 100, 1)));
    public static final RegistryObject<Potion> LONG_SHOCK = POTIONS.register("target_long_shock",
            () -> new Potion(new MobEffectInstance(TargetModMobEffects.SHOCK.get(), 400, 0)));
}
