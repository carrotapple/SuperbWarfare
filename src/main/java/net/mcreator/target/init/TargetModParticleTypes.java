package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TargetModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TargetMod.MODID);
    public static final RegistryObject<SimpleParticleType> FIRESTAR = REGISTRY.register("firestar", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BULLTHOLE = REGISTRY.register("bullthole", () -> new SimpleParticleType(false));
}

