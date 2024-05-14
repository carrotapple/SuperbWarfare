package net.mcreator.target.event;

import net.mcreator.target.entity.BocekarrowEntity;
import net.mcreator.target.entity.Target1Entity;
import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingEntityEventHandler {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) return;
        renderDamageIndicator(event);
        target1DamageImmune(event, event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event == null || event.getEntity() == null) return;
        arrowDamageImmuneForMine(event, event.getSource(), event.getSource().getEntity());
        arrowDamage(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(), event.getAmount());
        claymoreDamage(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event == null || event.getEntity() == null) return;
        killIndication(event.getSource().getEntity());
    }

    private static void target1DamageImmune(Event event, Entity entity) {
        if (entity == null) return;
        if (entity instanceof Target1Entity && entity.getPersistentData().getDouble("targetdown") > 0) {
            event.setCanceled(true);
        }
    }

    private static void claymoreDamage(LivingAttackEvent event, LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || sourceentity == null)
            return;
        if ((damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION)) && entity.getPersistentData().getDouble("claymore") > 0) {
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            }
            entity.hurt(TargetModDamageTypes.causeMineDamage(world.registryAccess(), sourceentity), (float) amount);
        }
    }

    // TODO 把伤害逻辑移植到箭类中
    private static void arrowDamage(LivingAttackEvent event, LevelAccessor world, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null) return;
        if (damagesource.is(DamageTypes.ARROW) && immediatesourceentity instanceof BocekarrowEntity) {
            event.setCanceled(true);
            entity.hurt(TargetModDamageTypes.causeArrowInBrainDamage(world.registryAccess(), sourceentity), (float) amount);
        }
    }

    private static void killIndication(Entity sourceEntity) {
        if (sourceEntity == null) return;
        if (sourceEntity instanceof Player player && player.getMainHandItem().is(TargetModTags.Items.GUN)) {
            if (!sourceEntity.level().isClientSide() && sourceEntity.getServer() != null) {

                // TODO 修改为正确音效播放方法
                sourceEntity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, sourceEntity.position(), sourceEntity.getRotationVector(), sourceEntity.level() instanceof ServerLevel ? (ServerLevel) sourceEntity.level() : null, 4,
                        sourceEntity.getName().getString(), sourceEntity.getDisplayName(), sourceEntity.level().getServer(), sourceEntity), "playsound target:targetdown player @s ~ ~ ~ 100 1");
            }
            sourceEntity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.killIndicator = 40;
                capability.syncPlayerVariables(sourceEntity);
            });
        }
    }

    private static void arrowDamageImmuneForMine(Event event, DamageSource damageSource, Entity sourceEntity) {
        if (damageSource == null || sourceEntity == null) return;
        if (sourceEntity instanceof Player player && (!sourceEntity.isAlive() || player.isSpectator())
                && (damageSource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:arrow_in_brain")))
                || damageSource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:mine")))
                || damageSource.is(DamageTypes.ARROW))
        ) event.setCanceled(true);
    }

    private static void renderDamageIndicator(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) return;
        var damagesource = event.getSource();
        var sourceEntity = event.getEntity();

        if (damagesource == null || sourceEntity == null) return;

        if (sourceEntity instanceof Player && (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION) || damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("target:mine"))))) {
            if (sourceEntity.getServer() != null) {
                // TODO 修改为正确的音效播放方法
                sourceEntity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, sourceEntity.position(), sourceEntity.getRotationVector(), sourceEntity.level() instanceof ServerLevel ? (ServerLevel) sourceEntity.level() : null, 4,
                        sourceEntity.getName().getString(), sourceEntity.getDisplayName(), sourceEntity.level().getServer(), sourceEntity), "playsound target:indication voice @a ~ ~ ~ 1 1");
            }
            sourceEntity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.hitIndicator = 25;
                capability.syncPlayerVariables(sourceEntity);
            });
        }
    }

    /**
     * 换弹时切换枪械，取消换弹音效播放
     */
    @SubscribeEvent
    public static void handleChangeSlot(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && event.getSlot() == EquipmentSlot.MAINHAND) {
            if (player.level().isClientSide || player.level().getServer() == null) {
                return;
            }

            ItemStack oldStack = event.getFrom();
            ItemStack newStack = event.getTo();

            if (oldStack.getItem() instanceof GunItem oldGun && player.level() instanceof ServerLevel serverLevel) {
                if (newStack.getItem() != oldStack.getItem()) {
                    stopGunReloadSound(serverLevel, oldGun);

                    newStack.getOrCreateTag().putDouble("drawtime", 0);
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zoom = false;
                        capability.syncPlayerVariables(player);
                    });
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zooming = false;
                        capability.syncPlayerVariables(player);
                    });
                    player.getPersistentData().putDouble("zoompos", 0);
                    player.getPersistentData().putDouble("zoom_time", 0);

                } else if (!newStack.getOrCreateTag().hasUUID("gun_uuid") || !oldStack.getOrCreateTag().hasUUID("gun_uuid") ||
                        !newStack.getOrCreateTag().getUUID("gun_uuid").equals(oldStack.getOrCreateTag().getUUID("gun_uuid"))) {
                    stopGunReloadSound(serverLevel, oldGun);

                    newStack.getOrCreateTag().putDouble("drawtime", 0);
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zoom = false;
                        capability.syncPlayerVariables(player);
                    });
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zooming = false;
                        capability.syncPlayerVariables(player);
                    });
                    player.getPersistentData().putDouble("zoompos", 0);
                    player.getPersistentData().putDouble("zoom_time", 0);
                }
            }
        }
    }

    private static void stopGunReloadSound(ServerLevel server, GunItem gun) {
        gun.getReloadSound().forEach(sound -> {
            var clientboundstopsoundpacket = new ClientboundStopSoundPacket(sound.getLocation(), SoundSource.PLAYERS);
            server.players().forEach(p -> p.connection.send(clientboundstopsoundpacket));
        });
    }
}
