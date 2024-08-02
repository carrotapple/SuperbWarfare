package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ProjectileEntity;
import net.mcreator.superbwarfare.event.modevent.ReloadEvent;
import net.mcreator.superbwarfare.init.ModEnchantments;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.ZoomMessage;
import net.mcreator.superbwarfare.tools.GunInfo;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
            handleGunFire(player);
            handleMiniGunFire(player);
            handleGunReload(player);
            handleGunSingleReload(player);
            handleSentinelCharge(player);
        }
    }

    /**
     * 通用的武器开火流程
     */
    private static void handleGunFire(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {
            double mode = stack.getOrCreateTag().getInt("fire_mode");

            int interval = stack.getOrCreateTag().getInt("fire_interval");

            if (!player.getPersistentData().getBoolean("firing") && player.getMainHandItem().getItem() == ModItems.DEVOTION.get()) {
                stack.getOrCreateTag().putDouble("fire_increase", 0);
            }

            if (stack.getOrCreateTag().getInt("ammo") == 0) {
                stack.getOrCreateTag().putInt("burst_fire", 0);
            }

            if ((player.getPersistentData().getBoolean("firing") || stack.getOrCreateTag().getInt("burst_fire") > 0)
                    && !(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                    && !stack.getOrCreateTag().getBoolean("reloading")
                    && !stack.getOrCreateTag().getBoolean("charging")
                    && stack.getOrCreateTag().getInt("ammo") > 0
                    && !player.getCooldowns().isOnCooldown(stack.getItem())
                    && !stack.getOrCreateTag().getBoolean("need_bolt_action")) {

                playGunSounds(player);

                if (mode == 0) {
                    player.getPersistentData().putBoolean("firing", false);
                }

                int burstCooldown = 0;
                if (mode == 1) {
                    stack.getOrCreateTag().putInt("burst_fire", (stack.getOrCreateTag().getInt("burst_fire") - 1));
                    burstCooldown = stack.getOrCreateTag().getInt("burst_fire") == 0 ? interval + 4 : 0;
                }

                if (stack.getOrCreateTag().getDouble("animindex") == 1) {
                    stack.getOrCreateTag().putDouble("animindex", 0);
                } else {
                    stack.getOrCreateTag().putDouble("animindex", 1);
                }
                /*
                  空仓挂机
                 */
                if (stack.getOrCreateTag().getInt("ammo") == 1) {
                    stack.getOrCreateTag().putBoolean("HoldOpen", true);
                }

                /*
                  判断是否为栓动武器（bolt_action_time > 0），并在开火后给一个需要上膛的状态
                 */
                if (stack.getOrCreateTag().getDouble("bolt_action_time") > 0 && stack.getOrCreateTag().getInt("ammo") > 1) {
                    stack.getOrCreateTag().putBoolean("need_bolt_action", true);
                }

                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
                stack.getOrCreateTag().putInt("fire_animation", interval);
                player.getPersistentData().putInt("noRun_time", interval + 2);
                stack.getOrCreateTag().putDouble("flash_time", 2);

                stack.getOrCreateTag().putDouble("empty", 1);

                if (stack.getItem() == ModItems.M_60.get()) {
                    stack.getOrCreateTag().putBoolean("bullet_chain", true);
                }

                if (stack.getItem() == ModItems.M_4.get() || player.getMainHandItem().getItem() == ModItems.HK_416.get()) {
                    if (stack.getOrCreateTag().getDouble("fire_sequence") == 1) {
                        stack.getOrCreateTag().putDouble("fire_sequence", 0);
                    } else {
                        stack.getOrCreateTag().putDouble("fire_sequence", 1);
                    }
                }

                if (stack.getItem() == ModItems.DEVOTION.get()) {
                    stack.getOrCreateTag().putDouble("fire_increase", stack.getOrCreateTag().getDouble("fire_increase") + 0.334);
                }

                if (stack.getItem() == ModItems.ABEKIRI.get()) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
                        ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x, player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                                player.getZ() + 1.8 * player.getLookAngle().z, 30, 0.4, 0.4, 0.4, 0.005, true, serverPlayer);
                    }
                }

                if (stack.getItem() == ModItems.SENTINEL.get()) {
                    stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                            iEnergyStorage -> iEnergyStorage.extractEnergy(3000, false)
                    );
                    stack.getOrCreateTag().putDouble("chamber_rot", 20);
                }

                int zoomAddCooldown = 0;
                if (stack.getItem() == ModItems.MARLIN.get()) {
                    if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
                        zoomAddCooldown = 5;
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 15);
                        stack.getOrCreateTag().putBoolean("fastfiring", false);
                    } else {
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 10);
                        stack.getOrCreateTag().putBoolean("fastfiring", true);
                    }
                }

                int cooldown = interval + (int) stack.getOrCreateTag().getDouble("fire_sequence") - (int) stack.getOrCreateTag().getDouble("fire_increase") + burstCooldown + zoomAddCooldown;
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);

                for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                    gunShoot(player);
                }

                stack.getOrCreateTag().putBoolean("shoot", true);

            }

            /*
              在开火动画的最后1tick，设置需要拉栓上膛的武器拉栓动画的倒计时为data里的拉栓时间
             */
            if (stack.getOrCreateTag().getInt("fire_animation") == 1 && stack.getOrCreateTag().getBoolean("need_bolt_action")) {
                stack.getOrCreateTag().putInt("bolt_action_anim", stack.getOrCreateTag().getInt("bolt_action_time"));
                player.getCooldowns().addCooldown(stack.getItem(), stack.getOrCreateTag().getInt("bolt_action_time"));
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

        if ((player.getPersistentData().getBoolean("firing") || (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) && !player.isSprinting()) {
            if (tag.getDouble("minigun_rotation") < 10) {
                tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") + 1));
            }
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_ROT.get(), 2f, 1f);
            }
        } else if (tag.getDouble("minigun_rotation") > 0) {
            tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") - 0.5));
        }

        if (tag.getDouble("overheat") == 0
                && (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo > 0
                && !(player.getCooldowns().isOnCooldown(stack.getItem())) && tag.getDouble("minigun_rotation") >= 10 && player.getPersistentData().getBoolean("firing")) {
            tag.putDouble("heat", (tag.getDouble("heat") + 0.5));
            if (tag.getDouble("heat") >= 50.5) {
                tag.putDouble("overheat", 40);
                player.getCooldowns().addCooldown(stack.getItem(), 40);
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_OVERHEAT.get(), 2f, 1f);
                }
            }

            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                if (tag.getDouble("heat") <= 40) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_FIRE_1P.get(), 2f, 1f);
                    player.playSound(ModSounds.MINIGUN_FIRE_3P.get(), 4f, 1f);
                    player.playSound(ModSounds.MINIGUN_FAR.get(), 12f, 1f);
                    player.playSound(ModSounds.MINIGUN_VERYFAR.get(), 24f, 1f);
                } else {
                    float pitch = (float) (1 - 0.025 * Math.abs(40 - tag.getDouble("heat")));

                    SoundTool.playLocalSound(serverPlayer, ModSounds.MINIGUN_FIRE_1P.get(), 2f, pitch);
                    player.playSound(ModSounds.MINIGUN_FIRE_3P.get(), 4f, pitch);
                    player.playSound(ModSounds.MINIGUN_FAR.get(), 12f, pitch);
                    player.playSound(ModSounds.MINIGUN_VERYFAR.get(), 24f, pitch);
                }
            }

            stack.getOrCreateTag().putBoolean("shoot", true);

            for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                gunShoot(player);
            }

            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.rifleAmmo = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).rifleAmmo - 1;
                capability.syncPlayerVariables(player);
            });

            tag.putInt("fire_animation", 2);
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
                        player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, 4f, 1f);
                    }

                    SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_far"));
                    if (soundFar != null) {
                        player.playSound(soundFar, 12f, 1f);
                    }

                    SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_veryfar"));
                    if (soundVeryFar != null) {
                        player.playSound(soundVeryFar, 24f, 1f);
                    }

                    return;
                }
            }

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_fire_1p"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
            }

            SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_fire_3p"));
            if (sound3p != null) {
                player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, 4f, 1f);
            }

            SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_far"));
            if (soundFar != null) {
                player.playSound(soundFar, 12f, 1f);
            }

            SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, name + "_veryfar"));
            if (soundVeryFar != null) {
                player.playSound(soundVeryFar, 24f, 1f);
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

    public static void gunShoot(Player player) {
        ItemStack heldItem = player.getMainHandItem();

        if (!player.level().isClientSide()) {
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.MONSTER_HUNTER.get(), heldItem);
            float damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("add_damage")) * (float) heldItem.getOrCreateTag().getDouble("damageadd");

            boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;

            ProjectileEntity projectile = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(damage)
                    .headShot(headshot)
                    .zoom(zoom);

            if (heldItem.getOrCreateTag().getBoolean("beast")) {
                projectile.beast();
            }

            projectile.monsterMultiple(monsterMultiple);

            projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y + 0.0005f, player.getLookAngle().z, 1 * (float) heldItem.getOrCreateTag().getDouble("velocity"),
                    (float) (heldItem.getOrCreateTag().getDouble("dev") * ZoomMessage.zoom_spread));
            player.level().addFreshEntity(projectile);
        }
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
                    tag.putInt("gun_reloading_time", (int) tag.getDouble("empty_reload_time"));
                    stack.getOrCreateTag().putBoolean("is_empty_reloading", true);
                    playGunEmptyReloadSounds(player);
                } else {
                    tag.putInt("gun_reloading_time", (int) tag.getDouble("normal_reload_time"));
                    stack.getOrCreateTag().putBoolean("is_normal_reloading", true);
                    playGunNormalReloadSounds(player);
                }
            } else {
                tag.putInt("gun_reloading_time", (int) tag.getDouble("empty_reload_time"));
                stack.getOrCreateTag().putBoolean("is_empty_reloading", true);
                playGunEmptyReloadSounds(player);
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

        if (stack.getItem() == ModItems.GLOCK_17.get()) {
            if (tag.getInt("gun_reloading_time") == 5) {
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

        if (stack.is(ModTags.Items.SHOTGUN)) {
            if (stack.getItem() == ModItems.ABEKIRI.get()) {
                GunsTool.reload(player, GunInfo.Type.SHOTGUN);
            } else {
                GunsTool.reload(player, GunInfo.Type.SHOTGUN, true);
            }
        } else if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            GunsTool.reload(player, GunInfo.Type.SNIPER, true);
        } else if (stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) {
            GunsTool.reload(player, GunInfo.Type.HANDGUN, true);
        } else if (stack.is(ModTags.Items.RIFLE)) {
            if (stack.getItem() == ModItems.M_60.get()) {
                GunsTool.reload(player, GunInfo.Type.RIFLE);
            } else {
                GunsTool.reload(player, GunInfo.Type.RIFLE, true);
            }
        }
        stack.getOrCreateTag().putBoolean("is_normal_reloading", false);
        stack.getOrCreateTag().putBoolean("is_empty_reloading", false);

        MinecraftForge.EVENT_BUS.post(new ReloadEvent.Post(player, stack));
    }

    public static void playGunEmptyReload(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.SHOTGUN)) {
            GunsTool.reload(player, GunInfo.Type.SHOTGUN);
        } else if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            GunsTool.reload(player, GunInfo.Type.SNIPER);
        } else if (stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) {
            GunsTool.reload(player, GunInfo.Type.HANDGUN);
        } else if (stack.is(ModTags.Items.RIFLE)) {
            GunsTool.reload(player, GunInfo.Type.RIFLE);
        } else if (stack.getItem() == ModItems.TASER.get()) {
            stack.getOrCreateTag().putInt("ammo", 1);
            player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.TASER_ELECTRODE.get(), 1, player.inventoryMenu.getCraftSlots());
        } else if (stack.getItem() == ModItems.M_79.get()) {
            stack.getOrCreateTag().putInt("ammo", 1);
            player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.GRENADE_40MM.get(), 1, player.inventoryMenu.getCraftSlots());
        } else if (stack.getItem() == ModItems.RPG.get()) {
            stack.getOrCreateTag().putInt("ammo", 1);
            player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.ROCKET.get(), 1, player.inventoryMenu.getCraftSlots());
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
                tag.putInt("prepare_load", (int) tag.getDouble("prepare_load_time"));
                player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("prepare_load_time"));

            } else {
                playGunPrepareReloadSounds(player);
                tag.putInt("prepare", (int) tag.getDouble("prepare_time"));
                player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("prepare_time"));
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

            // 检查备弹
            var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
            if (stack.is(ModTags.Items.SHOTGUN) && capability.shotgunAmmo == 0) {
                tag.putBoolean("force_stage3_start", true);
            } else if (stack.is(ModTags.Items.SNIPER_RIFLE) && capability.sniperAmmo == 0) {
                tag.putBoolean("force_stage3_start", true);
            } else if ((stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                tag.putBoolean("force_stage3_start", true);
            } else if (stack.is(ModTags.Items.RIFLE) && capability.rifleAmmo == 0) {
                tag.putBoolean("force_stage3_start", true);
            } else {
                tag.putInt("reload_stage", 2);
            }
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
                && tag.getInt("ammo") < (int) tag.getDouble("mag")) {

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
        if (stack.getItem() == ModItems.M_870.get()) {
            if (tag.getInt("iterative") == 3) {
                singleLoad(player);
            }
        }

        if (stack.getItem() == ModItems.MARLIN.get()) {
            if (tag.getInt("iterative") == 3) {
                singleLoad(player);
            }
        }

        // 二阶段结束
        if (tag.getInt("iterative") == 1) {

            // 装满结束
            if (tag.getInt("ammo") >= (int) tag.getDouble("mag")) {
                tag.putInt("reload_stage", 3);
            }

            // 备弹耗尽结束
            var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
            if (stack.is(ModTags.Items.SHOTGUN) && capability.shotgunAmmo == 0) {
                tag.putInt("reload_stage", 3);
            } else if (stack.is(ModTags.Items.SNIPER_RIFLE) && capability.sniperAmmo == 0) {
                tag.putInt("reload_stage", 3);
            } else if ((stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                tag.putInt("reload_stage", 3);
            } else if (stack.is(ModTags.Items.RIFLE) && capability.rifleAmmo == 0) {
                tag.putInt("reload_stage", 3);
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
            tag.putDouble("finish", (int) tag.getDouble("finish_time"));
            player.getCooldowns().addCooldown(stack.getItem(), (int) tag.getDouble("finish_time"));
            playGunEndReloadSounds(player);
        }

        // 三阶段结束
        if (tag.getInt("finish") == 1) {
            tag.putInt("reload_stage", 0);
            tag.putBoolean("reloading", false);

            MinecraftForge.EVENT_BUS.post(new ReloadEvent.Post(player, stack));
        }
    }

    public static void singleLoad(Player player) {
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        tag.putInt("ammo", tag.getInt("ammo") + 1);

        if (stack.is(ModTags.Items.SHOTGUN)) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.shotgunAmmo = capability.shotgunAmmo - 1;
                capability.syncPlayerVariables(player);
            });
        } else if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.sniperAmmo = capability.sniperAmmo - 1;
                capability.syncPlayerVariables(player);
            });
        } else if ((stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG))) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.handgunAmmo = capability.handgunAmmo - 1;
                capability.syncPlayerVariables(player);
            });
        } else if (stack.is(ModTags.Items.RIFLE)) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.rifleAmmo = capability.rifleAmmo - 1;
                capability.syncPlayerVariables(player);
            });
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
        //启动换弹
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
