package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.entity.TargetEntity;
import net.mcreator.superbwarfare.entity.projectile.ProjectileEntity;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.ClientIndicatorMessage;
import net.mcreator.superbwarfare.network.message.PlayerGunKillMessage;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.DamageTypeTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.text.DecimalFormat;

@Mod.EventBusSubscriber
public class LivingEventHandler {

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        handleGunPerksWhenHurt(event);
        renderDamageIndicator(event);
        reduceBulletDamage(event);
        giveExpToWeapon(event);
        handleGunLevels(event);
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        killIndication(event.getSource());
        handleGunPerksWhenDeath(event);
        handlePlayerKillEntity(event);
    }

    /**
     * 计算子弹伤害衰减
     */
    private static void reduceBulletDamage(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        Entity sourceEntity = source.getEntity();
        if (sourceEntity == null) return;

        double amount = event.getAmount();
        double damage = amount;

        ItemStack stack = sourceEntity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;

        //距离衰减
        if (DamageTypeTool.isGunDamage(source)) {
            double distance = entity.position().distanceTo(sourceEntity.position());

            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 25);
            } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                damage = reduceDamageByDistance(amount, distance, 0.001, 200);
            } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 50);
            } else if (stack.is(ModTags.Items.SMG)) {
                damage = reduceDamageByDistance(amount, distance, 0.03, 50);
            } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO) || stack.getItem() == ModItems.BOCEK.get()) {
                damage = reduceDamageByDistance(amount, distance, 0.0025, 150);
            }
        }

        //计算防弹插板减伤
        if (source.is(ModTags.DamageTypes.PROJECTILE) || source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            ItemStack armor = entity.getItemBySlot(EquipmentSlot.CHEST);

            if (armor != ItemStack.EMPTY && armor.getTag() != null && armor.getTag().contains("ArmorPlate")) {
                double armorValue;
                armorValue = armor.getOrCreateTag().getDouble("ArmorPlate");
                armor.getOrCreateTag().putDouble("ArmorPlate", Math.max(armor.getOrCreateTag().getDouble("ArmorPlate") - damage, 0));
                damage = Math.max(damage - armorValue, 0);
            }

            //计算防弹护具减伤
            if (source.is(ModTags.DamageTypes.PROJECTILE)) {
                damage *= 1 - 0.8 * Mth.clamp(entity.getAttributeValue(ModAttributes.BULLET_RESISTANCE.get()), 0, 1);
            }

            if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
                damage *= 1 - 0.2 * Mth.clamp(entity.getAttributeValue(ModAttributes.BULLET_RESISTANCE.get()), 0, 1);
            }
        }

        event.setAmount((float) damage);

        if (entity instanceof TargetEntity && sourceEntity instanceof Player player) {
            player.displayClientMessage(Component.literal("Damage:" + new DecimalFormat("##.#").format(damage) +
                    " Distance:" + new DecimalFormat("##.#").format(entity.position().distanceTo(sourceEntity.position())) + "M"), false);
        }
    }

    private static double reduceDamageByDistance(double amount, double distance, double rate, double minDistance) {
        return amount / (1 + rate * Math.max(0, distance - minDistance));
    }

    /**
     * 根据造成的伤害，提供武器经验
     */
    private static void giveExpToWeapon(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double amount = event.getAmount();

        // 先处理发射器类武器或高爆弹的爆炸伤害
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            if (stack.is(ModTags.Items.LAUNCHER) || PerkHelper.getItemPerkLevel(ModPerks.HE_BULLET.get(), stack) > 0) {
                stack.getOrCreateTag().putDouble("Exp", stack.getOrCreateTag().getDouble("Exp") + amount);
            }
        }

        // 再判断是不是枪械能造成的伤害
        if (!DamageTypeTool.isGunDamage(source)) return;

        stack.getOrCreateTag().putDouble("Exp", stack.getOrCreateTag().getDouble("Exp") + amount);
    }

    private static void handleGunLevels(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        var tag = stack.getOrCreateTag();
        int level = stack.getOrCreateTag().getInt("Level");
        double exp = stack.getOrCreateTag().getDouble("Exp");
        double upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;

        if (exp >= upgradeExpNeeded) {
            tag.putDouble("Exp", exp - upgradeExpNeeded);
            tag.putInt("Level", level + 1);
            tag.putDouble("UpgradePoint", tag.getDouble("UpgradePoint") + 0.25);
        }
    }

    private static void killIndication(DamageSource source) {
        var sourceEntity = source.getEntity();
        if (sourceEntity == null) {
            return;
        }

        // 如果配置不选择全局伤害提示，则只在伤害类型为mod添加的时显示指示器
        if (!DisplayConfig.GLOBAL_INDICATION.get() && !DamageTypeTool.isModDamage(source)) {
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

                        int level = PerkHelper.getItemPerkLevel(ModPerks.KILLING_TALLY.get(), newStack);
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

    /**
     * 发送击杀消息
     */
    private static void handlePlayerKillEntity(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        ResourceKey<DamageType> damageTypeResourceKey = source.typeHolder().unwrapKey().isPresent() ? source.typeHolder().unwrapKey().get() : DamageTypes.GENERIC;

        ServerPlayer attacker = null;
        if (source.getEntity() instanceof ServerPlayer player) {
            attacker = player;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
            attacker = player;
        }

        if (attacker != null) {
            if (DamageTypeTool.isHeadshotDamage(source)) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), true, damageTypeResourceKey));
            } else {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), false, damageTypeResourceKey));
            }
        }
    }

    private static void handleGunPerksWhenHurt(LivingHurtEvent event) {
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

        if (DamageTypeTool.isGunDamage(source) || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleKillClipDamage(stack, event);
        }

        if (DamageTypeTool.isGunFireDamage(source) && source.getDirectEntity() instanceof ProjectileEntity projectile && projectile.isZoom()) {
            handleGutshotStraightDamage(stack, event);
        }

        if (DamageTypeTool.isGunDamage(source)) {
            handleKillingTallyDamage(stack, event);
        }

        if (DamageTypeTool.isGunFireDamage(source)) {
            handleHeadSeekerTime(stack);
        }

        if (source.getDirectEntity() instanceof ProjectileEntity projectile) {
            if (PerkHelper.getItemPerkLevel(ModPerks.FOURTH_TIMES_CHARM.get(), stack) > 0) {
                float bypassArmorRate = projectile.getBypassArmorRate();
                if (bypassArmorRate >= 1.0f && source.is(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE)) {
                    handleFourthTimesCharm(stack);
                } else if (source.is(ModDamageTypes.GUN_FIRE_HEADSHOT)) {
                    handleFourthTimesCharm(stack);
                }
            }

            if (!projectile.isZoom()) {
                handleFieldDoctor(stack, event, attacker);
            }
        }

        if (DamageTypeTool.isHeadshotDamage(source)) {
            handleHeadSeekerDamage(stack, event);
        }
    }

    private static void handleGunPerksWhenDeath(LivingDeathEvent event) {
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

        if (DamageTypeTool.isGunDamage(source) || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleClipPerks(stack);
        }

        if (DamageTypeTool.isGunDamage(source)) {
            handleKillingTallyAddCount(stack);
            handleSubsistence(stack, attacker);
        }
    }

    private static void handleClipPerks(ItemStack stack) {
        int healClipLevel = PerkHelper.getItemPerkLevel(ModPerks.HEAL_CLIP.get(), stack);
        if (healClipLevel != 0) {
            stack.getOrCreateTag().putInt("HealClipTime", 80 + healClipLevel * 20);
        }

        int killClipLevel = PerkHelper.getItemPerkLevel(ModPerks.KILL_CLIP.get(), stack);
        if (killClipLevel != 0) {
            stack.getOrCreateTag().putInt("KillClipReloadTime", 80);
        }
    }

    private static void handleKillClipDamage(ItemStack stack, LivingHurtEvent event) {
        if (stack.getOrCreateTag().getInt("KillClipTime") > 0) {
            int level = PerkHelper.getItemPerkLevel(ModPerks.KILL_CLIP.get(), stack);
            if (level == 0) {
                return;
            }

            event.setAmount(event.getAmount() * (1.2f + 0.05f * level));
        }
    }

    private static void handleGutshotStraightDamage(ItemStack stack, LivingHurtEvent event) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.GUTSHOT_STRAIGHT.get(), stack);
        if (level == 0) {
            return;
        }

        event.setAmount(event.getAmount() * (1.15f + 0.05f * level));
    }

    private static void handleKillingTallyDamage(ItemStack stack, LivingHurtEvent event) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.KILLING_TALLY.get(), stack);
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
        int level = PerkHelper.getItemPerkLevel(ModPerks.KILLING_TALLY.get(), stack);
        if (level != 0) {
            stack.getOrCreateTag().putInt("KillingTally", Math.min(3, stack.getOrCreateTag().getInt("KillingTally") + 1));
        }
    }

    private static void handleFourthTimesCharm(ItemStack stack) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.FOURTH_TIMES_CHARM.get(), stack);
        if (level == 0) {
            return;
        }

        int fourthTimesCharmTick = stack.getOrCreateTag().getInt("FourthTimesCharmTick");
        if (fourthTimesCharmTick <= 0) {
            stack.getOrCreateTag().putInt("FourthTimesCharmTick", 40 + 10 * level);
            stack.getOrCreateTag().putInt("FourthTimesCharmCount", 1);
        } else {
            int count = stack.getOrCreateTag().getInt("FourthTimesCharmCount");
            if (count < 4) {
                stack.getOrCreateTag().putInt("FourthTimesCharmCount", Math.min(4, count + 1));
            }
        }
    }

    private static void handleSubsistence(ItemStack stack, Player player) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.SUBSISTENCE.get(), stack);
        if (level == 0) {
            return;
        }

        float rate = level * 0.1f + (stack.is(ModTags.Items.SMG) || stack.is(ModTags.Items.RIFLE) ? 0.07f : 0f);

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    int mag = stack.getOrCreateTag().getInt("mag");
                    int ammo = stack.getOrCreateTag().getInt("ammo");
                    int ammoReload = (int) Math.min(mag, mag * rate);
                    int ammoNeed = Math.min(mag - ammo, ammoReload);

                    if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                        int ammoFinal = Math.min(capability.rifleAmmo, ammoNeed);
                        capability.rifleAmmo -= ammoFinal;
                        stack.getOrCreateTag().putInt("ammo", Math.min(mag, ammo + ammoFinal));
                    } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                        int ammoFinal = Math.min(capability.handgunAmmo, ammoNeed);
                        capability.handgunAmmo -= ammoFinal;
                        stack.getOrCreateTag().putInt("ammo", Math.min(mag, ammo + ammoFinal));
                    }
                    capability.syncPlayerVariables(player);
                }
        );
    }

    private static void handleFieldDoctor(ItemStack stack, LivingHurtEvent event, Player player) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.FIELD_DOCTOR.get(), stack);
        if (level == 0) {
            return;
        }

        if (event.getEntity().isAlliedTo(player)) {
            event.getEntity().heal(event.getAmount() * Math.min(1.0f, 0.25f + 0.05f * level));
            event.setCanceled(true);
        }
    }

    private static void handleHeadSeekerTime(ItemStack stack) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.HEAD_SEEKER.get(), stack);
        if (level == 0) {
            return;
        }

        stack.getOrCreateTag().putInt("HeadSeeker", 11 + level * 2);
    }

    private static void handleHeadSeekerDamage(ItemStack stack, LivingHurtEvent event) {
        int level = PerkHelper.getItemPerkLevel(ModPerks.HEAD_SEEKER.get(), stack);
        if (level == 0) {
            return;
        }

        if (stack.getOrCreateTag().getInt("HeadSeeker") > 0) {
            event.setAmount(event.getAmount() * (1.095f + 0.0225f * level));
        }
    }
}
