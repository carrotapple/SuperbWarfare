package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
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
public class ModDamageTypes {
    public static final ResourceKey<DamageType> GUN_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "gunfire"));
    public static final ResourceKey<DamageType> GUN_FIRE_ABSOLUTE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "gunfire_absolute"));
    public static final ResourceKey<DamageType> GUN_FIRE_HEADSHOT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "gunfire_headshot"));
    public static final ResourceKey<DamageType> GUN_FIRE_HEADSHOT_ABSOLUTE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "gunfire_headshot_absolute"));
    public static final ResourceKey<DamageType> ARROW_IN_KNEE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "arrow_in_knee"));
    public static final ResourceKey<DamageType> ARROW_IN_KNEE_ABSOLUTE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "arrow_in_knee_absolute"));
    public static final ResourceKey<DamageType> ARROW_IN_BRAIN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "arrow_in_brain"));
    public static final ResourceKey<DamageType> ARROW_IN_BRAIN_ABSOLUTE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "arrow_in_brain_absolute"));
    public static final ResourceKey<DamageType> MINE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "mine"));
    public static final ResourceKey<DamageType> BEAST = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "beast"));
    public static final ResourceKey<DamageType> SHOCK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "shock"));
    public static final ResourceKey<DamageType> PROJECTILE_BOOM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "projectile_boom"));
    public static final ResourceKey<DamageType> CANNON_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModUtils.MODID, "cannon_fire"));

    public static DamageSource causeGunFireDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(GUN_FIRE), directEntity, attacker);
    }

    public static DamageSource causeGunFireHeadshotDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(GUN_FIRE_HEADSHOT), directEntity, attacker);
    }

    public static DamageSource causeArrowInKneeDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ARROW_IN_KNEE), directEntity, attacker);
    }

    public static DamageSource causeArrowInBrainDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ARROW_IN_BRAIN), directEntity, attacker);
    }

    public static DamageSource causeMineDamage(RegistryAccess registryAccess, @Nullable Entity entity) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(MINE), entity);
    }

    public static DamageSource causeShockDamage(RegistryAccess registryAccess, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SHOCK), attacker);
    }

    public static DamageSource causeProjectileBoomDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(PROJECTILE_BOOM), directEntity, attacker);
    }

    public static DamageSource causeCannonFireDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(CANNON_FIRE), directEntity, attacker);
    }

    public static DamageSource causeGunFireAbsoluteDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(GUN_FIRE_ABSOLUTE), directEntity, attacker);
    }

    public static DamageSource causeGunFireHeadshotAbsoluteDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(GUN_FIRE_HEADSHOT_ABSOLUTE), directEntity, attacker);
    }

    public static DamageSource causeArrowInKneeAbsoluteDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ARROW_IN_KNEE_ABSOLUTE), directEntity, attacker);
    }

    public static DamageSource causeArrowInBrainAbsoluteDamage(RegistryAccess registryAccess, @Nullable Entity directEntity, @Nullable Entity attacker) {
        return new DamageMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ARROW_IN_BRAIN_ABSOLUTE), directEntity, attacker);
    }

    private static class DamageMessages extends DamageSource {
        public DamageMessages(Holder.Reference<DamageType> typeReference) {
            super(typeReference);
        }

        public DamageMessages(Holder.Reference<DamageType> typeReference, Entity entity) {
            super(typeReference, entity);
        }

        public DamageMessages(Holder.Reference<DamageType> typeReference, Entity directEntity, Entity attacker) {
            super(typeReference, directEntity, attacker);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
            Entity entity = this.getEntity() == null ? this.getDirectEntity() : this.getEntity();
            if (entity == null) {
                return Component.translatable("death.attack." + this.getMsgId(), pLivingEntity.getDisplayName());
            } else if (entity instanceof LivingEntity living && living.getMainHandItem().hasCustomHoverName()) {
                return Component.translatable("death.attack." + this.getMsgId() + ".item", pLivingEntity.getDisplayName(), entity.getDisplayName(), living.getMainHandItem().getDisplayName());
            } else {
                return Component.translatable("death.attack." + this.getMsgId() + ".entity", pLivingEntity.getDisplayName(), entity.getDisplayName());
            }
        }
    }
}
