package net.mcreator.target.event;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.procedures.BulletFireNormalProcedure;
import net.mcreator.target.tools.ItemNBTTool;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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
                double fire = stack.getOrCreateTag().getDouble("fireanim") > 0 ? 0.5 * basic : 0;
                double ride = player.isPassenger() ? (-0.5) * basic : 0;

                double walk;
                if (player.getPersistentData().getDouble("qian") == 1 || player.getPersistentData().getDouble("tui") == 1 ||
                        player.getPersistentData().getDouble("mover") == 1 || player.getPersistentData().getDouble("movel") == 1) {
                    walk = 0.2 * basic;
                } else {
                    walk = 0;
                }

                double zoom;
                if (player.getPersistentData().getDouble("zoom_time") > 4) {
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
        if (stack.is(TargetModTags.Items.NORMAL_MAG_GUN)
                && player.getPersistentData().getDouble("firing") == 1
                && stack.getOrCreateTag().getDouble("reloading") == 0
                && stack.getOrCreateTag().getDouble("ammo") > 0
                && !player.getCooldowns().isOnCooldown(stack.getItem())
                && stack.getOrCreateTag().getDouble("firemode") != 1) {

            if (stack.getOrCreateTag().getDouble("firemode") == 0) {
                player.getPersistentData().putDouble("firing", 0);
            }

            if (stack.getOrCreateTag().getDouble("animindex") == 1) {
                stack.getOrCreateTag().putDouble("animindex", 0);
            } else {
                stack.getOrCreateTag().putDouble("animindex", 1);
            }

            if (stack.getOrCreateTag().getDouble("ammo") == 1) {
                stack.getOrCreateTag().putDouble("gj", 1);
            }

            stack.getOrCreateTag().putDouble("ammo", (stack.getOrCreateTag().getDouble("ammo") - 1));
            stack.getOrCreateTag().putDouble("firecooldown", 7);
            stack.getOrCreateTag().putDouble("fireanim", 2);
            stack.getOrCreateTag().putDouble("empty", 1);

            // TODO 补齐rpm数据
            int cooldown = (int) Math.ceil(20 * 60 / ItemNBTTool.getDouble(stack, "rpm", 60));
            player.getCooldowns().addCooldown(stack.getItem(), cooldown);

            for (int index0 = 0; index0 < (int) stack.getOrCreateTag().getDouble("projectileamount"); index0++) {
                BulletFireNormalProcedure.execute(player);
            }

            playGunSounds(player);
        }
    }


    public static void playGunSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(TargetModTags.Items.GUN)) {
            return;
        }

        if (!player.level().isClientSide) {
            String origin = stack.getItem().getDescriptionId();
            String name = origin.substring(origin.lastIndexOf(".") + 1);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_fire_1p"));
            if (sound1p != null && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(sound1p),
                        SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 2f, 1f, serverPlayer.level().random.nextLong()));
            }

            SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(TargetMod.MODID, name + "_fire_3p"));
            if (sound3p != null) {
                player.playSound(sound3p, 4f, 1f);
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
