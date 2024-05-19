package net.mcreator.target.event;

import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.Target1Entity;
import net.mcreator.target.init.TargetModDamageTypes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.network.message.PlayerKillMessage;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
public class LivingEntityEventHandler {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }
        renderDamageIndicator(event);
        target1DamageImmune(event, event.getEntity());
        reduceBulletDamage(event, event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }
        claymoreDamage(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }
        killIndication(event.getSource().getEntity());
    }

    private static void reduceBulletDamage(LivingHurtEvent event, DamageSource damagesource, LivingEntity entity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || sourceentity == null) return;

        double damage = amount;
        ItemStack stack = sourceentity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
        if (damagesource.is(TargetModDamageTypes.ARROW_IN_KNEE) || damagesource.is(TargetModDamageTypes.ARROW_IN_BRAIN)) {
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }
        if ((damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION) || damagesource.is(DamageTypes.ARROW))
                && (stack.getItem() == TargetModItems.M_79.get() || stack.getItem() == TargetModItems.RPG.get())
        ) {
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }

        if (damagesource.is(TargetModDamageTypes.GUN_FIRE) || damagesource.is(TargetModDamageTypes.GUN_FIRE_HEADSHOT)) {
            double distance = entity.position().distanceTo(sourceentity.position());

            if (stack.is(TargetModTags.Items.SHOTGUN) || stack.getItem() == TargetModItems.BOCEK.get()) {
                damage = reduceDamageByDistance(amount, distance, 0.05, 20);
            } else if (stack.is(TargetModTags.Items.SNIPER_RIFLE)) {
                damage = reduceDamageByDistance(amount, distance, 0.001, 200);
            } else if (stack.is(TargetModTags.Items.HANDGUN)) {
                damage = reduceDamageByDistance(amount, distance, 0.04, 40);
            } else if (stack.is(TargetModTags.Items.SMG)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 50);
            } else if (stack.is(TargetModTags.Items.RIFLE)) {
                damage = reduceDamageByDistance(amount, distance, 0.005, 100);
            }
            event.setAmount((float) damage);
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }
    }

    private static double reduceDamageByDistance(double amount, double distance, double rate, double minDistance) {
        return amount / (1 + rate * Math.max(0, distance - minDistance));
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

    private static void killIndication(Entity sourceEntity) {
        if (sourceEntity == null) return;
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

            if (player.level() instanceof ServerLevel serverLevel) {
                var newTag = newStack.getTag();
                var oldTag = oldStack.getTag();

                if (newStack.getItem() != oldStack.getItem()
                        || newTag == null || oldTag == null
                        || !newTag.hasUUID("gun_uuid") || !oldTag.hasUUID("gun_uuid")
                        || !newTag.getUUID("gun_uuid").equals(oldTag.getUUID("gun_uuid"))
                ) {
                    if (newStack.getItem() instanceof GunItem) {
                        newStack.getOrCreateTag().putBoolean("draw", true);
                    }

                    if (oldStack.getItem() instanceof GunItem oldGun) {
                        stopGunReloadSound(serverLevel, oldGun);
                    }

                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zoom = false;
                        capability.zooming = false;
                        capability.syncPlayerVariables(player);
                    });

                    player.getPersistentData().putDouble("zoom_pos", 0);
                    player.getPersistentData().putDouble("zoom_animation_time", 0);
                    if (newStack.getOrCreateTag().getDouble("bolt_action_time") > 0) {
                        newStack.getOrCreateTag().putDouble("bolt_action_anim", 0);
                    }
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

    @SubscribeEvent
    public static void handlePlayerKillEntity(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!TargetModVariables.MapVariables.get(entity.level()).pvpMode) {
            return;
        }

        if (source.getDirectEntity() instanceof ServerPlayer player) {
            if (source.is(TargetModDamageTypes.GUN_FIRE) || source.is(TargetModDamageTypes.ARROW_IN_KNEE)) {
                TargetMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerKillMessage(player.getId(), entity.getId(), false));
            } else if (source.is(TargetModDamageTypes.GUN_FIRE_HEADSHOT) || source.is(TargetModDamageTypes.ARROW_IN_BRAIN)) {
                TargetMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerKillMessage(player.getId(), entity.getId(), true));
            }
        }
    }
}
