package net.mcreator.target.event;

import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.*;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.ParticleTool;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class GunEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (event.phase == TickEvent.Phase.END && stack.is(TargetModTags.Items.GUN)) {
            handleGunFire(player);
            handleMiniGunFire(player);
        }
    }

    /**
     * 通用的武器开火流程
     */
    private static void handleGunFire(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModTags.Items.NORMAL_GUN)) {
            double mode = stack.getOrCreateTag().getInt("fire_mode");

            int interval = stack.getOrCreateTag().getInt("fire_interval");

            if (!player.getPersistentData().getBoolean("firing") && player.getMainHandItem().getItem() == TargetModItems.DEVOTION.get()) {
                stack.getOrCreateTag().putDouble("fire_increase", 0);
            }

            if (stack.getOrCreateTag().getInt("ammo") == 0) {
                stack.getOrCreateTag().putInt("burst_fire", 0);
            }

            if ((player.getPersistentData().getBoolean("firing") || stack.getOrCreateTag().getInt("burst_fire") > 0)
                    && !stack.getOrCreateTag().getBoolean("reloading")
                    && !stack.getOrCreateTag().getBoolean("charging")
                    && stack.getOrCreateTag().getInt("ammo") > 0
                    && !player.getCooldowns().isOnCooldown(stack.getItem())
                    && stack.getOrCreateTag().getDouble("need_bolt_action") == 0) {

                playGunSounds(player);

                if (mode == 0) {
                    player.getPersistentData().putBoolean("firing", false);
                }

                int burst_cooldown = 0;
                if (mode == 1) {
                    stack.getOrCreateTag().putInt("burst_fire", (stack.getOrCreateTag().getInt("burst_fire") - 1));
                    burst_cooldown = stack.getOrCreateTag().getInt("burst_fire") == 0 ? interval + 4 : 0;
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
                    stack.getOrCreateTag().putDouble("HoldOpen", 1);
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

                if (player.getMainHandItem().getItem() == TargetModItems.M_4.get() || player.getMainHandItem().getItem() == TargetModItems.HK_416.get()) {
                    if (stack.getOrCreateTag().getDouble("fire_sequence") == 1) {
                        stack.getOrCreateTag().putDouble("fire_sequence", 0);
                    } else {
                        stack.getOrCreateTag().putDouble("fire_sequence", 1);
                    }
                }

                if (player.getMainHandItem().getItem() == TargetModItems.DEVOTION.get()) {
                    stack.getOrCreateTag().putDouble("fire_increase", stack.getOrCreateTag().getDouble("fire_increase") + 0.334);
                }

                if (player.getMainHandItem().getItem() == TargetModItems.ABEKIRI.get()) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
                        ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x, player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                                player.getZ() + 1.8 * player.getLookAngle().z, 30, 0.4, 0.4, 0.4, 0.005, true, serverPlayer);
                    }
                }

                if (player.getMainHandItem().getItem() == TargetModItems.SENTINEL.get()) {
                    stack.getOrCreateTag().putBoolean("zoom_fire", (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming);
                    if (stack.getOrCreateTag().getDouble("power") > 20) {
                        stack.getOrCreateTag().putDouble("power", (stack.getOrCreateTag().getDouble("power") - 20));
                    } else {
                        stack.getOrCreateTag().putDouble("power", 0);
                    }
                    stack.getOrCreateTag().putDouble("crot", 20);
                }

                int zoom_add_cooldown = 0;
                if (player.getMainHandItem().getItem() == TargetModItems.MARLIN.get()) {
                    if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                        zoom_add_cooldown = 5;
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 15);
                        stack.getOrCreateTag().putBoolean("fastfiring", false);
                    } else {
                        stack.getOrCreateTag().putDouble("marlin_animation_time", 10);
                        stack.getOrCreateTag().putBoolean("fastfiring", true);
                    }
                }

                int cooldown = interval + (int) stack.getOrCreateTag().getDouble("fire_sequence") - (int) stack.getOrCreateTag().getDouble("fire_increase") + burst_cooldown + zoom_add_cooldown;
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);

                for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                    gunShoot(player);
                }
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
        var tag = stack.getOrCreateTag();

        if (stack.getItem() != TargetModItems.MINIGUN.get()) {
            return;
        }

        if ((player.getPersistentData().getBoolean("firing") || (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) && !player.isSprinting()) {
            if (tag.getDouble("minigun_rotation") < 10) {
                tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") + 1));
            }
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, TargetModSounds.MINIGUN_ROT.get(), 2f, 1f);
            }
        } else if (tag.getDouble("minigun_rotation") > 0) {
            tag.putDouble("minigun_rotation", (tag.getDouble("minigun_rotation") - 0.5));
        }

        if (tag.getDouble("overheat") == 0
                && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                && !(player.getCooldowns().isOnCooldown(stack.getItem())) && tag.getDouble("minigun_rotation") >= 10 && player.getPersistentData().getBoolean("firing")) {
            tag.putDouble("heat", (tag.getDouble("heat") + 0.5));
            if (tag.getDouble("heat") >= 50.5) {
                tag.putDouble("overheat", 40);
                player.getCooldowns().addCooldown(stack.getItem(), 40);
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.MINIGUN_OVERHEAT.get(), 2f, 1f);
                }
            }

            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                if (tag.getDouble("heat") <= 40) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.MINIGUN_FIRE_1P.get(), 2f, 1f);
                    player.playSound(TargetModSounds.MINIGUN_FIRE_3P.get(), 4f, 1f);
                    player.playSound(TargetModSounds.MINIGUN_FAR.get(), 12f, 1f);
                    player.playSound(TargetModSounds.MINIGUN_VERYFAR.get(), 24f, 1f);
                } else {
                    float pitch = (float) (1 - 0.025 * Math.abs(40 - tag.getDouble("heat")));

                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.MINIGUN_FIRE_1P.get(), 2f, pitch);
                    player.playSound(TargetModSounds.MINIGUN_FIRE_3P.get(), 4f, pitch);
                    player.playSound(TargetModSounds.MINIGUN_FAR.get(), 12f, pitch);
                    player.playSound(TargetModSounds.MINIGUN_VERYFAR.get(), 24f, pitch);
                }
            }

            for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                gunShoot(player);
            }

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.rifleAmmo = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables()).rifleAmmo - 1;
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
        if (!stack.is(TargetModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            if (player.getMainHandItem().getItem() == TargetModItems.SENTINEL.get() && stack.getOrCreateTag().getDouble("power") > 0) {
                SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, "sentinel_charge_fire_1p"));
                if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
                }

                SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, "sentinel_charge_fire_3p"));
                if (sound3p != null) {
                    player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, 4f, 1f);
                }

                SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, "sentinel_charge_far"));
                if (soundFar != null) {
                    player.playSound(soundFar, 12f, 1f);
                }

                SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, "sentinel_charge_veryfar"));
                if (soundVeryFar != null) {
                    player.playSound(soundVeryFar, 24f, 1f);
                }
            } else {
                SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_fire_1p"));
                if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
                }

                SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_fire_3p"));
                if (sound3p != null) {
                    player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, 4f, 1f);
                }

                SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_far"));
                if (soundFar != null) {
                    player.playSound(soundFar, 12f, 1f);
                }

                SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_veryfar"));
                if (soundVeryFar != null) {
                    player.playSound(soundVeryFar, 24f, 1f);
                }

            }
        }
    }

    public static void playGunBoltSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_bolt"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
            }
        }
    }

    public static void gunShoot(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

            capability.recoilHorizon = 2 * Math.random() - 1;
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (!player.level().isClientSide()) {
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            int monster_multiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), heldItem);
            float damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("add_damage")) * (float) heldItem.getOrCreateTag().getDouble("damageadd");

            ProjectileEntity projectile = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(damage)
                    .headShot(headshot);

            if (heldItem.getOrCreateTag().getBoolean("beast")) {
                projectile.beast();
            }

            projectile.monster_multiple(monster_multiple);

            projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y + 0.0005f, player.getLookAngle().z, 1 * (float) heldItem.getOrCreateTag().getDouble("velocity"),
                    (float) player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()));
            player.level().addFreshEntity(projectile);
        }
    }

}
