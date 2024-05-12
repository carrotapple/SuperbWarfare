package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TargetModDamageTypes {
    public static final ResourceKey<DamageType> GUNFIRE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TargetMod.MODID, "gunfire"));
    public static final ResourceKey<DamageType> ARROW_IN_BRAIN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TargetMod.MODID, "arrow_in_brain"));
    public static final ResourceKey<DamageType> MINE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TargetMod.MODID, "mine"));


    public static DamageSource causeGunFireDamage(RegistryAccess registryAccess, @Nullable Entity entity) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(GUNFIRE), entity);
    }

    public static DamageSource causeArrowInBrainDamage(RegistryAccess registryAccess, @Nullable Entity entity) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ARROW_IN_BRAIN), entity);
    }

    public static DamageSource causeMineDamage(RegistryAccess registryAccess, @Nullable Entity entity) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(MINE), entity);
    }

    private static class DamageMessages extends DamageSource {
        public DamageMessages(Holder.Reference<DamageType> typeReference) {
            super(typeReference);
        }

        public DamageMessages(Holder.Reference<DamageType> typeReference, Entity entity) {
            super(typeReference, entity);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
            Entity entity = this.getDirectEntity() == null ? this.getEntity() : this.getDirectEntity();
            if (entity == null) {
                return Component.translatable("death.attack." + this.getMsgId(), pLivingEntity.getDisplayName());
            } else {
                return Component.translatable("death.attack." + this.getMsgId() + ".entity", pLivingEntity.getDisplayName(), entity.getDisplayName());
            }
        }
    }
}
