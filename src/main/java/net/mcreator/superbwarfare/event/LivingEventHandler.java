package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.mcreator.superbwarfare.entity.Target1Entity;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.network.message.PlayerGunKillMessage;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
public class LivingEventHandler {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        handleGunEnchantmentsWhenHurt(event);
        renderDamageIndicator(event);
        reduceBulletDamage(event, event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        killIndication(event.getSource().getEntity());
        handleGunEnchantmentsWhenDeath(event);
        handlePlayerKillEntity(event);
    }

    private static void reduceBulletDamage(LivingHurtEvent event, DamageSource damagesource, LivingEntity entity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || sourceentity == null) {
            return;
        }

        double damage = amount;
        ItemStack stack = sourceentity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
        if ((damagesource.is(ModDamageTypes.ARROW_IN_KNEE) || damagesource.is(ModDamageTypes.ARROW_IN_BRAIN)
                && stack.getItem() == ModItems.BOCEK.get())) {
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }

        if ((damagesource.is(ModDamageTypes.PROJECTILE_BOOM) || damagesource.is(DamageTypes.ARROW))
                && (stack.getItem() == ModItems.M_79.get() || stack.getItem() == ModItems.RPG.get())
        ) {
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }

        if (damagesource.is(ModDamageTypes.GUN_FIRE) || damagesource.is(ModDamageTypes.GUN_FIRE_HEADSHOT)) {
            double distance = entity.position().distanceTo(sourceentity.position());

            if (stack.is(ModTags.Items.SHOTGUN) || stack.getItem() == ModItems.BOCEK.get()) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 25);
            } else if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
                damage = reduceDamageByDistance(amount, distance, 0.001, 200);
            } else if (stack.is(ModTags.Items.HANDGUN)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 50);
            } else if (stack.is(ModTags.Items.SMG)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 50);
            } else if (stack.is(ModTags.Items.RIFLE)) {
                damage = reduceDamageByDistance(amount, distance, 0.0025, 150);
            }
            event.setAmount((float) damage);
            stack.getOrCreateTag().putDouble("damagetotal", stack.getOrCreateTag().getDouble("damagetotal") + damage);
        }

        if (entity instanceof Target1Entity && sourceentity instanceof Player player) {
            player.displayClientMessage(Component.literal("Damage:" + new java.text.DecimalFormat("##.#").format(damage) + " Distance:" + new java.text.DecimalFormat("##.#").format((entity.position()).distanceTo((sourceentity.position()))) + "M"), false);
        }
    }

    private static double reduceDamageByDistance(double amount, double distance, double rate, double minDistance) {
        return amount / (1 + rate * Math.max(0, distance - minDistance));
    }

    private static void killIndication(Entity sourceEntity) {
        if (sourceEntity == null) {
            return;
        }

        if (!sourceEntity.level().isClientSide() && sourceEntity instanceof ServerPlayer player) {
            SoundTool.playLocalSound(player, ModSounds.TARGET_DOWN.get(), 100f, 1f);

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(2, 8));
        }
    }

    private static void renderDamageIndicator(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        var damagesource = event.getSource();
        var sourceEntity = damagesource.getEntity();

        if (sourceEntity == null) {
            return;
        }

        if (sourceEntity instanceof ServerPlayer player && (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION)
                || damagesource.is(ModDamageTypes.MINE) || damagesource.is(ModDamageTypes.PROJECTILE_BOOM))) {
            SoundTool.playLocalSound(player, ModSounds.INDICATION.get(), 1f, 1f);

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
        }
    }

    /**
     * 换弹时切换枪械，取消换弹音效播放
     */
    @SubscribeEvent
    public static void handleChangeSlot(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && event.getSlot() == EquipmentSlot.MAINHAND) {
            if (player.level().isClientSide) {
                return;
            }

            ItemStack oldStack = event.getFrom();
            ItemStack newStack = event.getTo();

            if (player instanceof ServerPlayer serverPlayer) {
                if (newStack.getItem() != oldStack.getItem()
                        || newStack.getTag() == null || oldStack.getTag() == null
                        || !newStack.getTag().hasUUID("gun_uuid") || !oldStack.getTag().hasUUID("gun_uuid")
                        || !newStack.getTag().getUUID("gun_uuid").equals(oldStack.getTag().getUUID("gun_uuid"))
                ) {
                    if (oldStack.getItem() instanceof GunItem oldGun) {
                        stopGunReloadSound(serverPlayer, oldGun);

                        if (oldStack.getTag() == null) {
                            return;
                        }
                        var oldTags = oldStack.getTag();

                        if (oldTags.getInt("bolt_action_time") > 0) {
                            oldTags.putInt("bolt_action_anim", 0);
                        }
                        oldTags.putBoolean("is_normal_reloading", false);
                        oldTags.putBoolean("is_empty_reloading", false);
                        oldTags.putInt("gun_reloading_time", 0);

                        oldTags.putBoolean("force_stop", false);
                        oldTags.putBoolean("stop", false);
                        oldTags.putInt("reload_stage", 0);
                        oldTags.putBoolean("reloading", false);
                        oldTags.putDouble("prepare", 0);
                        oldTags.putDouble("prepare_load", 0);
                        oldTags.putDouble("iterative", 0);
                        oldTags.putDouble("finish", 0);

                        oldTags.putBoolean("sentinel_is_charging", false);
                        oldTags.putInt("sentinel_charge_time", 0);
                    }

                    if (newStack.getItem() instanceof GunItem) {
                        newStack.getOrCreateTag().putBoolean("draw", true);
                        if (newStack.getOrCreateTag().getInt("bolt_action_time") > 0) {
                            newStack.getOrCreateTag().putInt("bolt_action_anim", 0);
                        }
                        newStack.getOrCreateTag().putBoolean("is_normal_reloading", false);
                        newStack.getOrCreateTag().putBoolean("is_empty_reloading", false);
                        newStack.getOrCreateTag().putInt("gun_reloading_time", 0);

                        newStack.getOrCreateTag().putBoolean("force_stop", false);
                        newStack.getOrCreateTag().putBoolean("stop", false);
                        newStack.getOrCreateTag().putInt("reload_stage", 0);
                        newStack.getOrCreateTag().putBoolean("reloading", false);
                        newStack.getOrCreateTag().putDouble("prepare", 0);
                        newStack.getOrCreateTag().putDouble("prepare_load", 0);
                        newStack.getOrCreateTag().putDouble("iterative", 0);
                        newStack.getOrCreateTag().putDouble("finish", 0);

                        newStack.getOrCreateTag().putBoolean("sentinel_is_charging", false);
                        newStack.getOrCreateTag().putInt("sentinel_charge_time", 0);

                        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILLING_TALLY.get(), newStack);
                        if (level != 0) {
                            newStack.getOrCreateTag().putInt("KillingTally", 0);
                        }

                        double weight = newStack.getOrCreateTag().getDouble("weight");

                        if (weight == 0) {
                            player.getCooldowns().addCooldown(newStack.getItem(), 12);
                        } else if (weight == 1) {
                            player.getCooldowns().addCooldown(newStack.getItem(), 17);
                        } else if (weight == 2) {
                            player.getCooldowns().addCooldown(newStack.getItem(), 30);
                        }
                    }
                }
            }
        }
    }

    private static void stopGunReloadSound(ServerPlayer player, GunItem gun) {
        gun.getReloadSound().forEach(sound -> {
            var clientboundstopsoundpacket = new ClientboundStopSoundPacket(sound.getLocation(), SoundSource.PLAYERS);
            player.connection.send(clientboundstopsoundpacket);
        });
    }


    private static void handlePlayerKillEntity(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!ModVariables.MapVariables.get(entity.level()).pvpMode) {
            return;
        }

        ResourceKey<DamageType> damageTypeResourceKey = source.typeHolder().unwrapKey().isPresent() ? source.typeHolder().unwrapKey().get() : DamageTypes.GENERIC;

        ServerPlayer attacker = null;
        if (source.getEntity() instanceof ServerPlayer player) {
            attacker = player;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
            attacker = player;
        }

        if (attacker != null) {
            if (source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) || source.is(ModDamageTypes.ARROW_IN_BRAIN)) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), true, damageTypeResourceKey));
            } else {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), false, damageTypeResourceKey));
            }
        }
    }

    private static void handleGunEnchantmentsWhenHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        Player attacker = null;
        if (source.getEntity() instanceof Player player) {
            attacker = player;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof Player player) {
            attacker = player;
        }

        if (attacker == null) {
            return;
        }

        ItemStack stack = attacker.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) ||
                source.is(ModDamageTypes.ARROW_IN_BRAIN) || source.is(ModDamageTypes.ARROW_IN_KNEE)
                || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleKillClipDamage(stack, event);
        }

        if (source.is(ModDamageTypes.GUN_FIRE) && source.getDirectEntity() instanceof ProjectileEntity projectile && projectile.isZoom()) {
            handleGutshotStraightDamage(stack, event);
        }

        if (source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)) {
            handleKillingTallyDamage(stack, event);
        }

        if (source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)) {
            handleFourthTimesCharm(stack);
        }
    }

    private static void handleGunEnchantmentsWhenDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();

        Player attacker = null;
        if (source.getEntity() instanceof Player player) {
            attacker = player;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof Player player) {
            attacker = player;
        }

        if (attacker == null) {
            return;
        }

        ItemStack stack = attacker.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT) ||
                source.is(ModDamageTypes.ARROW_IN_BRAIN) || source.is(ModDamageTypes.ARROW_IN_KNEE)
                || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleClipEnchantments(stack);
        }

        if (source.is(ModDamageTypes.GUN_FIRE) || source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)) {
            handleKillingTallyAddCount(stack);
        }
    }

    private static void handleClipEnchantments(ItemStack stack) {
        int healClipLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.HEAL_CLIP.get(), stack);
        if (healClipLevel != 0) {
            stack.getOrCreateTag().putInt("HealClipTime", 80 + healClipLevel * 20);
        }

        int killClipLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILL_CLIP.get(), stack);
        if (killClipLevel != 0) {
            stack.getOrCreateTag().putInt("KillClipReloadTime", 80);
        }
    }

    private static void handleKillClipDamage(ItemStack stack, LivingHurtEvent event) {
        if (stack.getOrCreateTag().getInt("KillClipTime") > 0) {
            int enchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GUTSHOT_STRAIGHT.get(), stack);
            if (enchantmentLevel == 0) {
                return;
            }

            event.setAmount(event.getAmount() * (1.2f + 0.05f * enchantmentLevel));
        }
    }

    private static void handleGutshotStraightDamage(ItemStack stack, LivingHurtEvent event) {
        int enchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GUTSHOT_STRAIGHT.get(), stack);
        if (enchantmentLevel == 0) {
            return;
        }

        event.setAmount(event.getAmount() * (1.15f + 0.05f * enchantmentLevel));
    }

    private static void handleKillingTallyDamage(ItemStack stack, LivingHurtEvent event) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILLING_TALLY.get(), stack);
        if (level == 0) {
            return;
        }

        int killTally = stack.getOrCreateTag().getInt("KillingTally");
        if (killTally == 0) {
            return;
        }

        event.setAmount(event.getAmount() * (1.0f + (0.1f * level) * killTally));
    }

    private static void handleKillingTallyAddCount(ItemStack stack) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILLING_TALLY.get(), stack);
        if (level != 0) {
            stack.getOrCreateTag().putInt("KillingTally", Math.min(3, stack.getOrCreateTag().getInt("KillingTally") + 1));
        }
    }

    private static void handleFourthTimesCharm(ItemStack stack) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.FOURTH_TIMES_CHARM.get(), stack);
        if (level == 0) {
            return;
        }

        int fourthTimesCharmTick = stack.getOrCreateTag().getInt("FourthTimesCharmTick");
        if (fourthTimesCharmTick <= 0) {
            stack.getOrCreateTag().putInt("FourthTimesCharmTick", 60);
            stack.getOrCreateTag().putInt("FourthTimesCharmCount", 1);
        } else {
            int count = stack.getOrCreateTag().getInt("FourthTimesCharmCount");
            if (count < 4) {
                stack.getOrCreateTag().putInt("FourthTimesCharmCount", Math.min(4, count + 1));
            }
        }
    }

}
