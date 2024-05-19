package net.mcreator.target.event;

import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

        if (event.phase == TickEvent.Phase.END) {
            handleGunsDev(player);
            handleGunFire(player);
        }
    }

    private static void handleGunsDev(Player player) {
        double[] recoilTimer = {0};
        double totalTime = 20;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;

        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {
                if (player == null) {
                    return;
                }

                ItemStack stack = player.getMainHandItem();

                double basic = stack.getOrCreateTag().getDouble("dev");

                double sprint = player.isSprinting() ? 0.5 * basic : 0;
                double sneaking = player.isShiftKeyDown() ? (-0.25) * basic : 0;
                double prone = player.getPersistentData().getDouble("prone") > 0 ? (-0.5) * basic : 0;
                double jump = player.onGround() ? 0 : 1.5 * basic;
                double fire = stack.getOrCreateTag().getInt("fire_animation") > 0 ? 0.5 * basic : 0;
                double ride = player.isPassenger() ? (-0.5) * basic : 0;

                double walk;
                if (player.getPersistentData().getDouble("move_forward") == 1 || player.getPersistentData().getDouble("move_backward") == 1 ||
                        player.getPersistentData().getDouble("move_left") == 1 || player.getPersistentData().getDouble("move_right") == 1) {
                    walk = 0.2 * basic;
                } else {
                    walk = 0;
                }

                double zoom;
                if (player.getPersistentData().getDouble("zoom_animation_time") > 4) {
                    if (stack.is(TargetModTags.Items.SNIPER_RIFLE)) {
                        zoom = 0.0001;
                    } else if (stack.is(TargetModTags.Items.SHOTGUN)) {
                        zoom = 0.9;
                    } else {
                        zoom = 0.0001;
                    }
                } else {
                    zoom = 1;
                }

                double index = zoom * (basic + walk + sprint + sneaking + prone + jump + fire + ride);

                if (player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) < index) {
                    player.getAttribute(TargetModAttributes.SPREAD.get())
                            .setBaseValue(player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) + 0.0125 * Math.pow(index - player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()), 2));
                } else {
                    player.getAttribute(TargetModAttributes.SPREAD.get())
                            .setBaseValue(player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) - 0.0125 * Math.pow(index - player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()), 2));
                }

                recoilTimer[0]++;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread recoilThread = new Thread(recoilRunnable);
        recoilThread.start();
    }

    private static void handleGunFire(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModTags.Items.NORMAL_GUN)) {
            double mode = stack.getOrCreateTag().getInt("fire_mode");
            if (!player.getPersistentData().getBoolean("firing") && player.getMainHandItem().getItem() == TargetModItems.DEVOTION.get()) {
                stack.getOrCreateTag().putDouble("fire_increase", 0);
            }

            if (player.getPersistentData().getBoolean("firing")
                    && !stack.getOrCreateTag().getBoolean("reloading")
                    && stack.getOrCreateTag().getInt("ammo") > 0
                    && !player.getCooldowns().isOnCooldown(stack.getItem())
                    && mode != 1
                    && stack.getOrCreateTag().getDouble("need_bolt_action") == 0) {

                playGunSounds(player);

                if (stack.getOrCreateTag().getInt("fire_mode") == 0) {
                    player.getPersistentData().putBoolean("firing", false);
                }

                if (stack.getOrCreateTag().getDouble("animindex") == 1) {
                    stack.getOrCreateTag().putDouble("animindex", 0);
                } else {
                    stack.getOrCreateTag().putDouble("animindex", 1);
                }

                if (stack.getOrCreateTag().getInt("ammo") == 1) {
                    stack.getOrCreateTag().putDouble("gj", 1);
                }
                /*
                  判断是否为栓动武器（bolt_action_time > 0），并在开火后给一个需要上膛的状态
                 */
                if (stack.getOrCreateTag().getDouble("bolt_action_time") > 0 && stack.getOrCreateTag().getInt("ammo") > 1) {
                    stack.getOrCreateTag().putDouble("need_bolt_action", 1);
                }

                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
                stack.getOrCreateTag().putInt("fire_animation", stack.getOrCreateTag().getInt("fire_interval"));
                player.getPersistentData().putInt("noRun_time", stack.getOrCreateTag().getInt("fire_interval") + 2);
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
                    if (!player.level().isClientSide() && player.getServer() != null) {
                        player.getServer().getCommands().performPrefixedCommand(
                                new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), player.level() instanceof ServerLevel ? (ServerLevel) player.level() : null, 4, player.getName().getString(), player.getDisplayName(),
                                        player.level().getServer(), player),
                                ("particle minecraft:cloud" + (" " + (player.getX() + 1.8 * player.getLookAngle().x)) + (" " + (player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y))
                                        + (" " + (player.getZ() + 1.8 * player.getLookAngle().z)) + " 0.4 0.4 0.4 0.005 30 force @s"));
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

                int cooldown = (int) stack.getOrCreateTag().getDouble("fire_interval") + (int) stack.getOrCreateTag().getDouble("fire_sequence") - (int) stack.getOrCreateTag().getDouble("fire_increase");
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);

                for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectile_amount"); index0++) {
                    gunShoot(player);
                }

            }
            /*
              在开火动画的最后1tick，设置需要拉栓上膛的武器拉栓动画的倒计时为data里的拉栓时间
             */
            if (stack.getOrCreateTag().getInt("fire_animation") == 1 && stack.getOrCreateTag().getDouble("need_bolt_action") == 1) {
                stack.getOrCreateTag().putDouble("bolt_action_anim", stack.getOrCreateTag().getDouble("bolt_action_time"));
                player.getCooldowns().addCooldown(stack.getItem(), (int) stack.getOrCreateTag().getDouble("bolt_action_time"));
                playGunBoltSounds(player);
            }
            if (stack.getOrCreateTag().getDouble("bolt_action_anim") > 0) {
                stack.getOrCreateTag().putDouble("bolt_action_anim", stack.getOrCreateTag().getDouble("bolt_action_anim") - 1);
            }
            if (stack.getOrCreateTag().getDouble("bolt_action_anim") == 1) {
                stack.getOrCreateTag().putDouble("need_bolt_action", 0);
            }
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
                SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_charge_fire_1p"));
                if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, sound1p, 2f, 1f);
                }

                SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "v_fire_3p"));
                if (sound3p != null) {
                    player.level().playSound(null, player.getOnPos(), sound3p, SoundSource.PLAYERS, 4f, 1f);
                }

                SoundEvent soundFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_charge_far"));
                if (soundFar != null) {
                    player.playSound(soundFar, 12f, 1f);
                }

                SoundEvent soundVeryFar = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_charge_veryfar"));
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
        if (Math.random() < 0.5) {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = -1;
                capability.syncPlayerVariables(player);
            });
        } else {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = 1;
                capability.syncPlayerVariables(player);
            });
        }

        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (!player.level().isClientSide()) {
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            float damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("add_damage")) * (float) heldItem.getOrCreateTag().getDouble("damageadd");

            ProjectileEntity projectile = new ProjectileEntity(player.level())
                    .shooter(player)
                    .damage(damage)
                    .headShot(headshot);

            if (heldItem.getOrCreateTag().getBoolean("beast")) {
                projectile.beast();
            }

            projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 1 * (float) heldItem.getOrCreateTag().getDouble("velocity"),
                    (float) player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()));
            player.level().addFreshEntity(projectile);
        }
    }

}
