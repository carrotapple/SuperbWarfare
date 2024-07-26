package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappingHandler {
    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(Registries.ITEM)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                Item remappedResource = ForgeRegistries.ITEMS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<Block> mapping : event.getAllMappings(Registries.BLOCK)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                Block remappedResource = ForgeRegistries.BLOCKS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<Enchantment> mapping : event.getAllMappings(Registries.ENCHANTMENT)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                Enchantment remappedResource = ForgeRegistries.ENCHANTMENTS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<SoundEvent> mapping : event.getAllMappings(Registries.SOUND_EVENT)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                SoundEvent remappedResource = ForgeRegistries.SOUND_EVENTS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<BlockEntityType<?>> mapping : event.getAllMappings(Registries.BLOCK_ENTITY_TYPE)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                BlockEntityType<?> remappedResource = ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<EntityType<?>> mapping : event.getAllMappings(Registries.ENTITY_TYPE)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                EntityType<?> remappedResource = ForgeRegistries.ENTITY_TYPES.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<Attribute> mapping : event.getAllMappings(Registries.ATTRIBUTE)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                Attribute remappedResource = ForgeRegistries.ATTRIBUTES.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<MobEffect> mapping : event.getAllMappings(Registries.MOB_EFFECT)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                MobEffect remappedResource = ForgeRegistries.MOB_EFFECTS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<Potion> mapping : event.getAllMappings(Registries.POTION)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                Potion remappedResource = ForgeRegistries.POTIONS.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<MenuType<?>> mapping : event.getAllMappings(Registries.MENU)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                MenuType<?> remappedResource = ForgeRegistries.MENU_TYPES.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }

        for (MissingMappingsEvent.Mapping<ParticleType<?>> mapping : event.getAllMappings(Registries.PARTICLE_TYPE)) {
            if ("target".equals(mapping.getKey().getNamespace())) {
                ResourceLocation newResourceLocation = new ResourceLocation(ModUtils.MODID, mapping.getKey().getPath());
                ParticleType<?> remappedResource = ForgeRegistries.PARTICLE_TYPES.getValue(newResourceLocation);
                if (remappedResource != null) {
                    mapping.remap(remappedResource);
                }
            }
        }
    }
}
