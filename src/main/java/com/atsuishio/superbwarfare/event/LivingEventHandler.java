package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.LaserCapability;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.LaserTowerEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArmedVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.modevent.PreKillEvent;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.item.gun.data.value.ReloadState;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.network.message.receive.DrawClientMessage;
import com.atsuishio.superbwarfare.network.message.receive.PlayerGunKillMessage;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber
public class LivingEventHandler {

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (!event.getSource().is(ModDamageTypes.VEHICLE_EXPLOSION) && event.getEntity().getVehicle() instanceof VehicleEntity vehicle) {
            if (event.getEntity().getVehicle() instanceof ArmedVehicleEntity iArmedVehicle && iArmedVehicle.hidePassenger(event.getEntity())) {
                if (!(event.getSource().is(DamageTypes.EXPLOSION)
                        || event.getSource().is(DamageTypes.PLAYER_EXPLOSION)
                        || event.getSource().is(ModDamageTypes.CUSTOM_EXPLOSION)
                        || event.getSource().is(ModDamageTypes.MINE)
                        || event.getSource().is(ModDamageTypes.PROJECTILE_BOOM))) {
                    vehicle.hurt(event.getSource(), event.getAmount());
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) {
            return;
        }

        handleVehicleHurt(event);
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

        killIndication(event);
        handleGunPerksWhenDeath(event);
        handlePlayerKillEntity(event);
        giveKillExpToWeapon(event);

        if (event.getEntity() instanceof Player player) {
            handlePlayerBeamReset(player);
        }
    }

    private static void handleVehicleHurt(LivingHurtEvent event) {
        var vehicle = event.getEntity().getVehicle();
        if (vehicle instanceof VehicleEntity) {
            if (vehicle instanceof ArmedVehicleEntity iArmedVehicle) {
                if (iArmedVehicle.hidePassenger(event.getEntity())) {
                    if (!event.getSource().is(ModDamageTypes.VEHICLE_EXPLOSION)) {
                        event.setCanceled(true);
                    }
                } else {
                    if (!(event.getSource().is(DamageTypes.EXPLOSION)
                            || event.getSource().is(DamageTypes.PLAYER_EXPLOSION)
                            || event.getSource().is(ModDamageTypes.CUSTOM_EXPLOSION)
                            || event.getSource().is(ModDamageTypes.MINE)
                            || event.getSource().is(ModDamageTypes.PROJECTILE_BOOM))) {
                        vehicle.hurt(event.getSource(), 0.7f * event.getAmount());
                    }

                    event.setAmount(0.3f * event.getAmount());
                }
            }
        }
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

        if (!(stack.getItem() instanceof GunItem)) return;

        var data = GunData.from(stack);
        var perk = data.perk.get(Perk.Type.AMMO);

        // 距离衰减
        if (DamageTypeTool.isGunDamage(source)) {
            double distance = entity.position().distanceTo(sourceEntity.position());

            var ammoType = data.ammoTypeInfo().playerAmmoType();
            if (ammoType != null) {
                switch (ammoType) {
                    case SHOTGUN -> {
                        if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
                            damage = reduceDamageByDistance(amount, distance, 0.015, 30);
                        } else {
                            damage = reduceDamageByDistance(amount, distance, 0.05, 15);
                        }
                    }

                    case SNIPER -> damage = reduceDamageByDistance(amount, distance, 0.001, 150);
                    case HEAVY -> damage = reduceDamageByDistance(amount, distance, 0.0007, 250);
                    case HANDGUN -> damage = reduceDamageByDistance(amount, distance, 0.03, 40);
                    case RIFLE -> damage = reduceDamageByDistance(amount, distance, 0.007, 100);
                }
            }

            // TODO 正确计算距离衰减
            if (stack.is(ModTags.Items.SMG)) {
                damage = reduceDamageByDistance(amount, distance, 0.02, 50);
            } else if (stack.getItem() == ModItems.BOCEK.get()) {
                damage = reduceDamageByDistance(amount, distance, 0.007, 100);
            }
        }

        // 计算防弹插板减伤
        ItemStack armor = entity.getItemBySlot(EquipmentSlot.CHEST);

        if (armor != ItemStack.EMPTY && armor.getTag() != null && armor.getTag().contains("ArmorPlate")) {
            double armorValue;
            armorValue = armor.getOrCreateTag().getDouble("ArmorPlate");
            armor.getOrCreateTag().putDouble("ArmorPlate", Math.max(armor.getOrCreateTag().getDouble("ArmorPlate") - damage, 0));
            damage = Math.max(damage - armorValue, 0);
        }

        // 计算防弹护具减伤
        if (source.is(ModTags.DamageTypes.PROJECTILE) || source.is(DamageTypes.MOB_PROJECTILE)) {
            damage *= 1 - 0.8 * Mth.clamp(entity.getAttributeValue(ModAttributes.BULLET_RESISTANCE.get()), 0, 1);
        }

        if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            damage *= 1 - 0.2 * Mth.clamp(entity.getAttributeValue(ModAttributes.BULLET_RESISTANCE.get()), 0, 1);
        }

        if (source.is(ModDamageTypes.PROJECTILE_BOOM) || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.CANNON_FIRE) || source.is(ModDamageTypes.CUSTOM_EXPLOSION)
                || source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            damage *= 1 - 0.3 * Mth.clamp(entity.getAttributeValue(ModAttributes.BULLET_RESISTANCE.get()), 0, 1);
        }

        event.setAmount((float) damage);

        if (entity instanceof TargetEntity && sourceEntity instanceof Player player) {
            player.displayClientMessage(Component.translatable("tips.superbwarfare.target.damage",
                    FormatTool.format2D(damage),
                    FormatTool.format1D(entity.position().distanceTo(sourceEntity.position())), "m"), false);
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
        if (!(stack.getItem() instanceof GunItem)) return;
        if (event.getEntity() instanceof TargetEntity) return;

        var data = GunData.from(stack);
        double amount = Math.min(0.125 * event.getAmount(), event.getEntity().getMaxHealth());

        // 先处理发射器类武器或高爆弹的爆炸伤害
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            if (stack.is(ModTags.Items.LAUNCHER) || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.set(data.exp.get() + amount);
            }
        }

        // 再判断是不是枪械能造成的伤害
        if (!DamageTypeTool.isGunDamage(source)) return;

        data.exp.set(data.exp.get() + amount);
    }

    private static void giveKillExpToWeapon(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        if (event.getEntity() instanceof TargetEntity) return;

        var data = GunData.from(stack);
        double amount = 20 + 2 * event.getEntity().getMaxHealth();

        // 先处理发射器类武器或高爆弹的爆炸伤害
        if (source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            if (stack.is(ModTags.Items.LAUNCHER) || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.set(data.exp.get() + amount);
            }
        }

        // 再判断是不是枪械能造成的伤害
        if (DamageTypeTool.isGunDamage(source)) {
            data.exp.set(data.exp.get() + amount);
        }

        // 提升武器等级
        int level = data.level.get();
        double exp = data.exp.get();
        double upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded;
            level = data.level.get() + 1;
            upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;
            data.exp.set(exp);
            data.level.set(level);
            data.upgradePoint.set(data.upgradePoint.get() + 0.5);
        }
    }

    private static void handleGunLevels(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        if (event.getEntity() instanceof TargetEntity) return;

        var data = GunData.from(stack);
        int level = data.level.get();
        double exp = data.exp.get();
        double upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded;
            level = data.level.get() + 1;
            upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;
            data.exp.set(exp);
            data.level.set(level);
            data.upgradePoint.set(data.upgradePoint.get() + 0.5);
        }
    }

    private static void killIndication(LivingDeathEvent event) {
        DamageSource source = event.getSource();

        var sourceEntity = source.getEntity();
        if (sourceEntity == null) {
            return;
        }

        // 如果配置不选择全局伤害提示，则只在伤害类型为mod添加的时显示指示器
        if (!GameplayConfig.GLOBAL_INDICATION.get() && !DamageTypeTool.isModDamage(source)) {
            return;
        }

        if (!sourceEntity.level().isClientSide() && sourceEntity instanceof ServerPlayer player) {
            if (MinecraftForge.EVENT_BUS.post(new PreKillEvent.Indicator(player, source, event.getEntity()))) {
                return;
            }

            SoundTool.playLocalSound(player, ModSounds.TARGET_DOWN.get(), 3f, 1f);

            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(2, 8));
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

            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientIndicatorMessage(0, 5));
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

            player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::stop);

            if (player instanceof ServerPlayer serverPlayer) {
                if (newStack.getItem() != oldStack.getItem()
                        || newStack.getTag() == null || oldStack.getTag() == null
                        || (newStack.getItem() instanceof GunItem && !GunData.from(newStack).initialized())
                        || (oldStack.getItem() instanceof GunItem && !GunData.from(newStack).initialized())
                        || (newStack.getItem() instanceof GunItem && oldStack.getItem() instanceof GunItem && !Objects.equals(GunsTool.getGunUUID(newStack), GunsTool.getGunUUID(oldStack)))
                ) {
                    if (oldStack.getItem() instanceof GunItem oldGun) {
                        stopGunReloadSound(serverPlayer, oldGun);

                        var oldData = GunData.from(oldStack);

                        if (oldData.defaultActionTime() > 0) {
                            oldData.bolt.actionTimer.reset();
                        }

                        oldData.reload.setTime(0);

                        oldData.reload.setState(ReloadState.NOT_RELOADING);

                        if (oldData.defaultIterativeTime() != 0) {
                            oldData.stopped.set(false);
                            oldData.forceStop.set(false);
                            oldData.reload.setStage(0);
                            oldData.reload.prepareTimer.reset();
                            oldData.reload.prepareLoadTimer.reset();
                            oldData.reload.iterativeLoadTimer.reset();
                            oldData.reload.finishTimer.reset();
                        }

                        if (oldStack.is(ModItems.SENTINEL.get())) {
                            oldData.charge.timer.reset();
                        }

                        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(cap -> {
                            cap.edit = false;
                            cap.sync(player);
                        });
                    }

                    if (newStack.getItem() instanceof GunItem) {
                        var newData = GunData.from(newStack);
                        newData.draw.set(true);

                        if (newData.defaultActionTime() > 0) {
                            newData.bolt.actionTimer.reset();
                        }

                        newData.reload.setState(ReloadState.NOT_RELOADING);
                        newData.reload.reloadTimer.reset();

                        if (newData.defaultIterativeTime() != 0) {
                            newData.forceStop.set(false);
                            newData.stopped.set(false);
                            newData.reload.setStage(0);
                            newData.reload.prepareTimer.reset();
                            newData.reload.prepareLoadTimer.reset();
                            newData.reload.iterativeLoadTimer.reset();
                            newData.reload.finishTimer.reset();
                        }

                        if (newStack.is(ModItems.SENTINEL.get())) {
                            newData.charge.timer.reset();
                        }

                        int level = newData.perk.getLevel(ModPerks.KILLING_TALLY);
                        if (level != 0) {
                            GunsTool.setPerkIntTag(newStack, "KillingTally", 0);
                        }

                        if (player.level() instanceof ServerLevel) {
                            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new DrawClientMessage(true));
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

        if (MinecraftForge.EVENT_BUS.post(new PreKillEvent.SendKillMessage(attacker, source, entity))) {
            return;
        }

        if (attacker != null && MiscConfig.SEND_KILL_FEEDBACK.get()) {
            if (DamageTypeTool.isHeadshotDamage(source)) {
                Mod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), true, damageTypeResourceKey));
            } else {
                Mod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new PlayerGunKillMessage(attacker.getId(), entity.getId(), false, damageTypeResourceKey));
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
        if (!(stack.getItem() instanceof GunItem)) {
            return;
        }

        if (DamageTypeTool.isGunDamage(source) || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleKillClipDamage(stack, event);
            handleVorpalWeaponDamage(stack, event);
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
            if (GunData.from(stack).perk.getLevel(ModPerks.FOURTH_TIMES_CHARM) > 0) {
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
        if (!(stack.getItem() instanceof GunItem)) {
            return;
        }

        if (DamageTypeTool.isGunDamage(source) || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            handleClipPerks(stack);
        }

        if (DamageTypeTool.isGunDamage(source)) {
            handleKillingTallyAddCount(stack);
            handleSubsistence(stack, attacker);
        }

        if (DamageTypeTool.isHeadshotDamage(source)) {
            handleDesperado(stack);
        }
    }

    private static void handleClipPerks(ItemStack stack) {
        int healClipLevel = GunData.from(stack).perk.getLevel(ModPerks.HEAL_CLIP);
        if (healClipLevel != 0) {
            GunsTool.setPerkIntTag(stack, "HealClipTime", 80 + healClipLevel * 20);
        }

        int killClipLevel = GunData.from(stack).perk.getLevel(ModPerks.KILL_CLIP);
        if (killClipLevel != 0) {
            GunsTool.setPerkIntTag(stack, "KillClipReloadTime", 80);
        }
    }

    private static void handleKillClipDamage(ItemStack stack, LivingHurtEvent event) {
        if (GunsTool.getPerkIntTag(stack, "KillClipTime") > 0) {
            int level = GunData.from(stack).perk.getLevel(ModPerks.KILL_CLIP);
            if (level == 0) {
                return;
            }

            event.setAmount(event.getAmount() * (1.2f + 0.05f * level));
        }
    }

    private static void handleGutshotStraightDamage(ItemStack stack, LivingHurtEvent event) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.GUTSHOT_STRAIGHT);
        if (level == 0) {
            return;
        }

        event.setAmount(event.getAmount() * (1.15f + 0.05f * level));
    }

    private static void handleKillingTallyDamage(ItemStack stack, LivingHurtEvent event) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.KILLING_TALLY);
        if (level == 0) {
            return;
        }

        int killTally = GunsTool.getPerkIntTag(stack, "KillingTally");
        if (killTally == 0) {
            return;
        }

        event.setAmount(event.getAmount() * (1.0f + (0.1f * level) * killTally));
    }

    private static void handleKillingTallyAddCount(ItemStack stack) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.KILLING_TALLY);
        if (level != 0) {
            GunsTool.setPerkIntTag(stack, "KillingTally", Math.min(3, GunsTool.getPerkIntTag(stack, "KillingTally") + 1));
        }
    }

    private static void handleFourthTimesCharm(ItemStack stack) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.FOURTH_TIMES_CHARM);
        if (level == 0) {
            return;
        }

        int fourthTimesCharmTick = GunsTool.getPerkIntTag(stack, "FourthTimesCharmTick");
        if (fourthTimesCharmTick <= 0) {
            GunsTool.setPerkIntTag(stack, "FourthTimesCharmTick", 40 + 10 * level);
            GunsTool.setPerkIntTag(stack, "FourthTimesCharmCount", 1);
        } else {
            int count = GunsTool.getPerkIntTag(stack, "FourthTimesCharmCount");
            if (count < 4) {
                GunsTool.setPerkIntTag(stack, "FourthTimesCharmCount", Math.min(4, count + 1));
            }
        }
    }

    private static void handleSubsistence(ItemStack stack, Player player) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.SUBSISTENCE);
        if (level == 0) {
            return;
        }

        float rate = level * 0.1f + (stack.is(ModTags.Items.SMG) || stack.is(ModTags.Items.RIFLE) ? 0.07f : 0f);

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(cap -> {
            var data = GunData.from(stack);
            int mag = data.magazine();
            int ammo = data.ammo.get();
            int ammoReload = (int) Math.min(mag, mag * rate);
            int ammoNeed = Math.min(mag - ammo, ammoReload);

            boolean flag = InventoryTool.hasCreativeAmmoBox(player);

            var ammoType = data.ammoTypeInfo().playerAmmoType();
            if (ammoType != null) {
                int ammoFinal = Math.min(ammoType.get(cap), ammoNeed);
                if (flag) {
                    ammoFinal = ammoNeed;
                } else {
                    ammoType.add(cap, -ammoFinal);
                }

                data.ammo.set(Math.min(mag, ammo + ammoFinal));
            }

            cap.sync(player);
        });
    }

    private static void handleFieldDoctor(ItemStack stack, LivingHurtEvent event, Player player) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.FIELD_DOCTOR);
        if (level == 0) {
            return;
        }

        if (event.getEntity().isAlliedTo(player)) {
            event.getEntity().heal(event.getAmount() * Math.min(1.0f, 0.25f + 0.05f * level));
            event.setCanceled(true);
        }
    }

    private static void handleHeadSeekerTime(ItemStack stack) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.HEAD_SEEKER);
        if (level == 0) {
            return;
        }

        GunsTool.setPerkIntTag(stack, "HeadSeeker", 11 + level * 2);
    }

    private static void handleHeadSeekerDamage(ItemStack stack, LivingHurtEvent event) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.HEAD_SEEKER);
        if (level == 0) {
            return;
        }

        if (GunsTool.getPerkIntTag(stack, "HeadSeeker") > 0) {
            event.setAmount(event.getAmount() * (1.095f + 0.0225f * level));
        }
    }

    private static void handleDesperado(ItemStack stack) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.DESPERADO);
        if (level == 0) {
            return;
        }

        GunsTool.setPerkIntTag(stack, "DesperadoTime", 90 + level * 10);
    }

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        if (!VehicleConfig.VEHICLE_ITEM_PICKUP.get()) return;
        if (event.getEntity().getVehicle() instanceof ContainerMobileVehicleEntity containerMobileVehicleEntity) {
            var pickUp = event.getItem();
            if (!containerMobileVehicleEntity.level().isClientSide) {
                HopperBlockEntity.addItem(containerMobileVehicleEntity, pickUp);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        // 死亡掉落弹药盒
        if (event.getEntity() instanceof Player player && !player.level().getLevelData().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY).orElse(new ModVariables.PlayerVariables());

            boolean drop = Stream.of(Ammo.values())
                    .mapToInt(type -> type.get(cap))
                    .sum() > 0;

            if (drop) {
                var stack = new ItemStack(ModItems.AMMO_BOX.get());

                for (var type : Ammo.values()) {
                    type.set(stack, type.get(cap));
                    type.set(cap, 0);
                }

                stack.getOrCreateTag().putBoolean("All", true);
                stack.getOrCreateTag().putBoolean("IsDrop", true);

                cap.sync(player);
                event.getDrops().add(new ItemEntity(player.level(), player.getX(), player.getY() + 1, player.getZ(), stack));
            }
        }

        DamageSource source = event.getSource();
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack mainHandItem = player.getMainHandItem();

        // 创生物收集掉落物
        if (player.getVehicle() instanceof ContainerMobileVehicleEntity containerMobileVehicleEntity && source.is(ModDamageTypes.VEHICLE_STRIKE)) {
            var drops = event.getDrops();
            var removed = new ArrayList<ItemEntity>();

            drops.forEach(itemEntity -> {
                ItemStack stack = itemEntity.getItem();

                InventoryTool.insertItem(containerMobileVehicleEntity.getItemStacks(), stack);

                if (stack.getCount() <= 0) {
                    player.drop(stack, false);
                    removed.add(itemEntity);
                }
            });

            drops.removeAll(removed);
            return;
        }

        if (mainHandItem.getItem() instanceof GunItem && GunData.from(mainHandItem).perk.getLevel(ModPerks.POWERFUL_ATTRACTION) > 0 && (DamageTypeTool.isGunDamage(source) || DamageTypeTool.isExplosionDamage(source))) {
            var drops = event.getDrops();
            drops.forEach(itemEntity -> {
                ItemStack item = itemEntity.getItem();
                if (!player.addItem(item)) {
                    player.drop(item, false);
                }
            });
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null) return;

        if (player.getVehicle() instanceof ArmedVehicleEntity) {
            player.giveExperiencePoints(event.getDroppedExperience());
            event.setCanceled(true);
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;

        int level = GunData.from(stack).perk.getLevel(ModPerks.POWERFUL_ATTRACTION);
        if (level > 0) {
            player.giveExperiencePoints((int) (event.getDroppedExperience() * (0.8f + 0.2f * level)));

            event.setCanceled(true);
        }
    }

    public static void handlePlayerBeamReset(Player player) {
        player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::end);
    }

    private static void handleVorpalWeaponDamage(ItemStack stack, LivingHurtEvent event) {
        var entity = event.getEntity();
        int level = GunData.from(stack).perk.getLevel(ModPerks.VORPAL_WEAPON);
        if (level <= 0) return;
        if (entity.getHealth() < 100.0f) return;

        event.setAmount((float) (event.getAmount() + entity.getHealth() * 0.00002f * Math.pow(level, 2)));
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        ICustomKnockback knockback = ICustomKnockback.getInstance(event.getEntity());
        if (knockback.superbWarfare$getKnockbackStrength() >= 0) {
            event.setStrength((float) knockback.superbWarfare$getKnockbackStrength());
        }
    }

    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if (living.getVehicle() instanceof VehicleEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPreSendKillMessage(PreKillEvent.SendKillMessage event) {
        if (event.getSource().getDirectEntity() instanceof LaserTowerEntity && !(event.getTarget() instanceof Player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPreIndicator(PreKillEvent.Indicator event) {
        if (event.getSource().getDirectEntity() instanceof LaserTowerEntity && !(event.getTarget() instanceof Player)) {
            event.setCanceled(true);
        }
    }
}
