package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.projectile.ProjectileEntity;
import net.mcreator.superbwarfare.event.modevent.ReloadEvent;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.GunInfo;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber
public class GunEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (event.phase == TickEvent.Phase.END && stack.is(ModTags.Items.GUN)) {
            handleGunBolt(player);
            handleMiniGunFire(player);
            handleGunReload(player);
            handleGunSingleReload(player);
            handleSentinelCharge(player);
        }
    }

    /**
     * 通用的武器开火流程
     */
    private static void handleGunBolt(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {
            if (stack.getOrCreateTag().getInt("fire_animation") == 1 && stack.getOrCreateTag().getBoolean("need_bolt_action")) {
                stack.getOrCreateTag().putInt("bolt_action_anim", stack.getOrCreateTag().getInt("bolt_action_time") + 1);
                player.getCooldowns().addCooldown(stack.getItem(), stack.getOrCreateTag().getInt("bolt_action_time") + 1);
                playGunBoltSounds(player);
            }
            if (stack.getOrCreateTag().getInt("bolt_action_anim") > 0) {
                stack.getOrCreateTag().putInt("bolt_action_anim", stack.getOrCreateTag().getInt("bolt_action_anim") - 1);
            }
            if (stack.getOrCreateTag().getInt("bolt_action_anim") == 1) {
                stack.getOrCreateTag().putBoolean("need_bolt_action", false);
            }
        }
    }

    /**
     * 加特林开火流程
     */
    private static void handleMiniGunFire(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() != ModItems.MINIGUN.get()) {
            return;
        }

        var tag = stack.getOrCreateTag();

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).holdFire || (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
            if (tag.getDouble("minigun_rotation") < 10) {
                tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") + 1));
            }
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_ROT.get(), 2f, 1f);
            }
        } else if (tag.getDouble("minigun_rotation") > 0) {
            tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") - 0.5));
        }
    }

    /**
     * 根据武器的注册名来寻找音效并播放
     */
    public static void playGunSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            if (stack.getItem() == ModItems.SENTINEL.get()) {
                AtomicBoolean charged = new AtomicBoolean(false);

                stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                        e -> charged.set(e.getEnergyStored() > 0)
                );

                if (charged.get()) {
                    SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_fire_1p"));
                    if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
                    }

                    SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_fire_3p"));
                    if (sound3p != null) {
                        player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.2f, 1f);
                    }

                    SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_far"));
                    if (soundFar != null) {
                        player.level().playSound(null, player.getOnPos(), soundFar, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.5f, 1f);
                    }

                    SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_veryfar"));
                    if (soundVeryFar != null) {
                        player.level().playSound(null, player.getOnPos(), soundVeryFar, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius"), 1f);
                    }

                    return;
                }
            }

            var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

            if (perk == ModPerks.BEAST_BULLET.get()) {
                player.playSound(ModSounds.HENG.get(), 4f, 1f);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.HENG.get(), 4f, 1f);
                }
            }

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_fire_1p"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
            }

            SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_fire_3p"));
            if (sound3p != null) {
                player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.2f, 1f);
            }

            SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_far"));
            if (soundFar != null) {
                player.level().playSound(null, player.getOnPos(), soundFar, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius") * 0.5f, 1f);
            }

            SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_veryfar"));
            if (soundVeryFar != null) {
                player.level().playSound(null, player.getOnPos(), soundVeryFar, SoundSource.PLAYERS, (float) stack.getOrCreateTag().getDouble("SoundRadius"), 1f);
            }
        }
    }

    public static void playGunBoltSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_bolt"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
            }
        }
    }

    public static void gunShoot(Player player, double spared) {
        ItemStack heldItem = player.getMainHandItem();

        if (!player.level().isClientSide()) {
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            float damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("sentinelChargeDamage")) * (float) perkDamage(heldItem);
            float velocity = (float) heldItem.getOrCreateTag().getDouble("velocity") * (float) perkSpeed(heldItem);
            int projectileAmount = (int) heldItem.getOrCreateTag().getDouble("projectile_amount");
            float bypassArmorRate = (float) heldItem.getOrCreateTag().getDouble("BypassesArmor");
            boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
            var perk = PerkHelper.getPerkByType(heldItem, Perk.Type.AMMO);

            ProjectileEntity projectile = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? projectileAmount * damage : damage)
                    .headShot(headshot)
                    .zoom(zoom);

            if (perk instanceof AmmoPerk ammoPerk) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);

                bypassArmorRate += ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
                projectile.setRGB(ammoPerk.rgb);

                if (ammoPerk.mobEffect.get() != null) {
                    int amplifier;
                    if (perk.descriptionId.equals("blade_bullet")) {
                        amplifier = level / 3;
                    } else {
                        amplifier = level - 1;
                    }

                    projectile.effect(() -> new MobEffectInstance(ammoPerk.mobEffect.get(), 70 + 30 * level, amplifier));
                }
            }

            bypassArmorRate = Math.max(bypassArmorRate, 0);
            projectile.bypassArmorRate(bypassArmorRate);

            if (perk == ModPerks.SILVER_BULLET.get()) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);
                projectile.undeadMultiple(1.0f + 0.5f * level);
            } else if (perk == ModPerks.BEAST_BULLET.get()) {
                projectile.beast();
            } else if (perk == ModPerks.JHP_BULLET.get()) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);
                projectile.jhpBullet(true, level);
            } else if (perk == ModPerks.HE_BULLET.get()) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);
                projectile.heBullet(true, level);
            }

            var dmgPerk = PerkHelper.getPerkByType(heldItem, Perk.Type.DAMAGE);
            if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);
                projectile.monsterMultiple(0.1f + 0.1f * level);
            }

            projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y + 0.0005f, player.getLookAngle().z, velocity,
                    (float) spared);
            player.level().addFreshEntity(projectile);
        }
    }

    public static double perkDamage(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            return ammoPerk.damageRate;
        }
        return 1;
    }

    public static double perkSpeed(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            return ammoPerk.speedRate;
        }
        return 1;
    }

    /**
     * 通用的武器换弹流程
     */
    private static void handleGunReload(Player player) {
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();
        //启动换弹
        if (tag.getBoolean("start_reload")) {
            MinecraftForge.EVENT_BUS.post(new ReloadEvent.Pre(player, stack));
            if (stack.is(ModTags.Items.OPEN_BOLT)) {
                if (tag.getInt("ammo") == 0) {
                    tag.putInt("gun_reloading_time", (int) tag.getDouble("empty_reload_time") + 2);
                    stack.getOrCreateTag().putBoolean("is_empty_reloading", true);
                    playGunEmptyReloadSounds(player);
                } else {
                    tag.putInt("gun_reloading_time", (int) tag.getDouble("normal_reload_time") + 2);
                    stack.getOrCreateTag().putBoolean("is_normal_reloading", true);
                    playGunNormalReloadSounds(player);
                }
            } else {
                tag.putInt("gun_reloading_time", (int) tag.getDouble("empty_reload_time") + 2);
                stack.getOrCreateTag().putBoolean("is_empty_reloading", true);
                playGunEmptyReloadSounds(player);
            }
            if (stack.getItem() == ModItems.DEVOTION.get()) {
                tag.putInt("customRpm", 0);
            }
            tag.putBoolean("start_reload", false);
        }

        if (tag.getInt("gun_reloading_time") > 0) {
            tag.putInt("gun_reloading_time", tag.getInt("gun_reloading_time") - 1);
        }

        if (stack.getItem() == ModItems.RPG.get()) {
            if (tag.getInt("gun_reloading_time") == 84) {
                tag.putBoolean("empty", false);
            }
            if (tag.getInt("gun_reloading_time") == 7) {
                tag.putBoolean("close_hammer", false);
            }
        }

        if (stack.getItem() == ModItems.MK_14.get()) {
            if (tag.getInt("gun_reloading_time") == 18) {
                tag.putBoolean("HoldOpen", false);
            }
        }

        if (stack.getItem() == ModItems.SKS.get()) {
            if (tag.getInt("gun_reloading_time") == 14) {
                tag.putBoolean("HoldOpen", false);
            }
        }

        if (stack.getItem() == ModItems.M_60.get()) {
            if (tag.getInt("gun_reloading_time") == 55) {
                tag.putBoolean("bullet_chain", false);
            }
        }

        if (stack.getItem() == ModItems.GLOCK_17.get() || stack.getItem() == ModItems.GLOCK_18.get() || stack.getItem() == ModItems.M_1911.get()) {
            if (tag.getInt("gun_reloading_time") == 5) {
                tag.putBoolean("HoldOpen", false);
            }
        }

        if (stack.getItem() == ModItems.QBZ_95.get()) {
            if (tag.getInt("gun_reloading_time") == 14) {
                tag.putBoolean("HoldOpen", false);
            }
        }

        if (tag.getInt("gun_reloading_time") == 1) {
            if (stack.is(ModTags.Items.OPEN_BOLT)) {
                if (tag.getInt("ammo") == 0) {
                    playGunEmptyReload(player);
                } else {
                    playGunNormalReload(player);
                }
            } else {
                playGunEmptyReload(player);
            }
        }
    }

    public static void playGunNormalReload(Player player) {
        ItemStack stack = player.getMainHandItem();

        int count = 0;
        for (var inv : player.getInventory().items) {
            if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                count++;
            }
        }

        if (count == 0) {
            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                if (stack.is(ModTags.Items.EXTRA_ONE_AMMO)) {
                    GunsTool.reload(player, stack, GunInfo.Type.SHOTGUN, true);
                } else {
                    GunsTool.reload(player, stack, GunInfo.Type.SHOTGUN);
                }
            } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.SNIPER, true);
            } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.HANDGUN, true);
            } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                if (stack.is(ModTags.Items.EXTRA_ONE_AMMO)) {
                    GunsTool.reload(player, stack, GunInfo.Type.RIFLE, true);
                } else {
                    GunsTool.reload(player, stack, GunInfo.Type.RIFLE);
                }
            }
        } else {
            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag") + (stack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0));
            } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag") + (stack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0));
            } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag") + (stack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0));
            } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag") + (stack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0));
            }
        }

        stack.getOrCreateTag().putBoolean("is_normal_reloading", false);
        stack.getOrCreateTag().putBoolean("is_empty_reloading", false);

        MinecraftForge.EVENT_BUS.post(new ReloadEvent.Post(player, stack));
    }

    public static void playGunEmptyReload(Player player) {
        ItemStack stack = player.getMainHandItem();

        int count = 0;
        for (var inv : player.getInventory().items) {
            if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                count++;
            }
        }

        if (count == 0) {
            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.SHOTGUN);
            } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.SNIPER);
            } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.HANDGUN);
            } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                GunsTool.reload(player, stack, GunInfo.Type.RIFLE);
            } else if (stack.getItem() == ModItems.TASER.get()) {
                stack.getOrCreateTag().putInt("ammo", 1);
                player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.TASER_ELECTRODE.get(), 1, player.inventoryMenu.getCraftSlots());
            } else if (stack.getItem() == ModItems.M_79.get()) {
                stack.getOrCreateTag().putInt("ammo", 1);
                player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.GRENADE_40MM.get(), 1, player.inventoryMenu.getCraftSlots());
            } else if (stack.getItem() == ModItems.RPG.get()) {
                stack.getOrCreateTag().putInt("ammo", 1);
                player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.ROCKET.get(), 1, player.inventoryMenu.getCraftSlots());
            } else if (stack.getItem() == ModItems.JAVELIN.get()) {
                stack.getOrCreateTag().putInt("ammo", 1);
                player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.JAVELIN_MISSILE.get(), 1, player.inventoryMenu.getCraftSlots());
            }
        } else {
            stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag"));
        }

        stack.getOrCreateTag().putBoolean("is_normal_reloading", false);
        stack.getOrCreateTag().putBoolean("is_empty_reloading", false);

        MinecraftForge.EVENT_BUS.post(new ReloadEvent.Post(player, stack));
    }

    public static void playGunEmptyReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_reload_empty"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    public static void playGunNormalReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_reload_normal"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    /**
     * 单发装填类的武器换弹流程
     */
    private static void handleGunSingleReload(Player player) {
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        // 换弹流程计时器
        if (tag.getDouble("prepare") > 0) {
            tag.putDouble("prepare", tag.getDouble("prepare") - 1);
        }
        if (tag.getDouble("prepare_load") > 0) {
            tag.putDouble("prepare_load", tag.getDouble("prepare_load") - 1);
        }
        if (tag.getDouble("iterative") > 0) {
            tag.putDouble("iterative", tag.getDouble("iterative") - 1);
        }
        if (tag.getDouble("finish") > 0) {
            tag.putDouble("finish", tag.getDouble("finish") - 1);
        }

        // 一阶段
        if (tag.getBoolean("start_single_reload")) {
            MinecraftForge.EVENT_BUS.post(new ReloadEvent.Pre(player, stack));

            // 此处判断空仓换弹的时候，是否在准备阶段就需要装填一发，如M870
            if (tag.getDouble("prepare_load_time") != 0 && tag.getInt("ammo") == 0) {
                playGunPrepareLoadReloadSounds(player);
                tag.putInt("prepare_load", (int) tag.getDouble("prepare_load_time") + 1);
                player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("prepare_load_time") + 1);
            } else if (tag.getDouble("prepare_empty") != 0 && tag.getInt("ammo") == 0) {
                // 此处判断空仓换弹，如莫辛纳甘
                playGunEmptyPrepareSounds(player);
                tag.putInt("prepare", (int) tag.getDouble("prepare_empty") + 1);
                player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("prepare_empty") + 1);
            } else {
                playGunPrepareReloadSounds(player);
                tag.putInt("prepare", (int) tag.getDouble("prepare_time") + 1);
                player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("prepare_time") + 1);
            }

            tag.putBoolean("force_stop", false);
            tag.putBoolean("stop", false);
            tag.putInt("reload_stage", 1);
            tag.putBoolean("reloading", true);
            tag.putBoolean("start_single_reload", false);
        }

        if (stack.getItem() == ModItems.M_870.get()) {
            if (tag.getInt("prepare_load") == 10) {
                singleLoad(player);
            }
        }

        // 一阶段结束，检查备弹，如果有则二阶段启动，无则直接跳到三阶段
        if ((tag.getDouble("prepare") == 1 || tag.getDouble("prepare_load") == 1)) {
            int count = 0;
            for (var inv : player.getInventory().items) {
                if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                    count++;
                }
            }

            if (count == 0) {
                var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
                if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO) && capability.shotgunAmmo == 0) {
                    tag.putBoolean("force_stage3_start", true);
                } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO) && capability.sniperAmmo == 0) {
                    tag.putBoolean("force_stage3_start", true);
                } else if ((stack.is(ModTags.Items.USE_HANDGUN_AMMO) || stack.is(ModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                    tag.putBoolean("force_stage3_start", true);
                } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO) && capability.rifleAmmo == 0) {
                    tag.putBoolean("force_stage3_start", true);
                } else {
                    tag.putInt("reload_stage", 2);
                }
            } else {
                tag.putInt("reload_stage", 2);
            }
            // 检查备弹

        }

        // 强制停止换弹，进入三阶段
        if (tag.getBoolean("force_stop")) {
            tag.putBoolean("stop", true);
        }

        // 二阶段
        if ((tag.getDouble("prepare") == 0 || tag.getDouble("prepare_load") == 0)
                && tag.getInt("reload_stage") == 2
                && tag.getInt("iterative") == 0
                && !tag.getBoolean("stop")
                && tag.getInt("ammo") < (int) tag.getDouble("mag") + tag.getInt("customMag")) {

            playGunLoopReloadSounds(player);
            tag.putDouble("iterative", (int) tag.getDouble("iterative_time"));
            player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("iterative_time"));
            // 动画播放nbt
            if (tag.getDouble("load_index") == 1) {
                tag.putDouble("load_index", 0);
            } else {
                tag.putDouble("load_index", 1);
            }
        }

        // 装填
        if (stack.getItem() == ModItems.M_870.get() || stack.getItem() == ModItems.MARLIN.get()) {
            if (tag.getInt("iterative") == 3) {
                singleLoad(player);
            }
        }

        if (stack.getItem() == ModItems.K_98.get() || stack.getItem() == ModItems.MOSIN_NAGANT.get()) {
            if (tag.getInt("iterative") == 1) {
                singleLoad(player);
            }
        }

        // 二阶段结束
        if (tag.getInt("iterative") == 1) {
            // 装满结束
            if (tag.getInt("ammo") >= (int) tag.getDouble("mag") + tag.getInt("customMag")) {
                tag.putInt("reload_stage", 3);
            }

            // 备弹耗尽结束
            int count = 0;
            for (var inv : player.getInventory().items) {
                if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                    count++;
                }
            }

            if (count == 0) {
                var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
                if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO) && capability.shotgunAmmo == 0) {
                    tag.putInt("reload_stage", 3);
                } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO) && capability.sniperAmmo == 0) {
                    tag.putInt("reload_stage", 3);
                } else if ((stack.is(ModTags.Items.USE_HANDGUN_AMMO) || stack.is(ModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                    tag.putInt("reload_stage", 3);
                } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO) && capability.rifleAmmo == 0) {
                    tag.putInt("reload_stage", 3);
                }
            }

            // 强制结束
            if (tag.getBoolean("stop")) {
                tag.putInt("reload_stage", 3);
                tag.putBoolean("force_stop", false);
                tag.putBoolean("stop", false);
            }
        }

        // 三阶段
        if ((tag.getInt("iterative") == 1 && tag.getInt("reload_stage") == 3) || tag.getBoolean("force_stage3_start")) {
            tag.putBoolean("force_stage3_start", false);
            tag.putDouble("finish", (int) tag.getDouble("finish_time") + 2);
            player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("finish_time") + 2);
            playGunEndReloadSounds(player);
        }

        // 三阶段结束
        if (tag.getInt("finish") == 1) {
            tag.putInt("reload_stage", 0);
            if (tag.getDouble("bolt_action_time") > 0) {
                stack.getOrCreateTag().putBoolean("need_bolt_action", false);
            }
            tag.putBoolean("reloading", false);

            MinecraftForge.EVENT_BUS.post(new ReloadEvent.Post(player, stack));
        }
    }

    public static void singleLoad(Player player) {
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        tag.putInt("ammo", tag.getInt("ammo") + 1);

        int count = 0;
        for (var inv : player.getInventory().items) {
            if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                count++;
            }
        }

        if (count == 0) {
            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.shotgunAmmo -= 1;
                    capability.syncPlayerVariables(player);
                });
            } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.sniperAmmo -= 1;
                    capability.syncPlayerVariables(player);
                });
            } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.handgunAmmo -= 1;
                    capability.syncPlayerVariables(player);
                });
            } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.rifleAmmo -= 1;
                    capability.syncPlayerVariables(player);
                });
            }
        }
    }

    public static void playGunPrepareReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_prepare"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    public static void playGunEmptyPrepareSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_prepare_empty"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    public static void playGunPrepareLoadReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_prepare_load"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    public static void playGunLoopReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_loop"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    public static void playGunEndReloadSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_end"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 10f, 1f);
            }
        }
    }

    /**
     * 哨兵充能
     */
    private static void handleSentinelCharge(Player player) {
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();
        // 启动换弹
        if (tag.getBoolean("start_sentinel_charge")) {
            tag.putInt("sentinel_charge_time", 127);
            stack.getOrCreateTag().putBoolean("sentinel_is_charging", true);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
            }

            tag.putBoolean("start_sentinel_charge", false);
        }

        if (tag.getInt("sentinel_charge_time") > 0) {
            tag.putInt("sentinel_charge_time", tag.getInt("sentinel_charge_time") - 1);
        }

        if (tag.getInt("sentinel_charge_time") == 17) {
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                    iEnergyStorage -> iEnergyStorage.receiveEnergy(24000, false)
            );

            player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.SHIELD_CELL.get(), 1, player.inventoryMenu.getCraftSlots());
        }

        if (tag.getInt("sentinel_charge_time") == 1) {
            tag.putBoolean("sentinel_is_charging", false);
        }
    }

}
