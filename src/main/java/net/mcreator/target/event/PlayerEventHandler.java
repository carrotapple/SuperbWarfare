package net.mcreator.target.event;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        if (!TargetModVariables.MapVariables.get(player.level()).pvpMode) {
            return;
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(TargetModTags.Items.GUN)) {
                stack.getOrCreateTag().putDouble("ammo", stack.getOrCreateTag().getDouble("mag"));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            handlePlayerProne(player);
            handlePlayerSprint(player);
            handleWeaponLevel(player);
            handleWeaponSway(player);
            handleAmmoCount(player);
            handleFireTime(player);
            handleGround(player);
            handlePrepareZoom(player);
            handleSpecialWeaponAmmo(player);
            handleChangeFireRate(player);
            handleDistantRange(player);
            handleRenderDamageIndicator(player);
            handleBocekPulling(player);
            handleGunRecoil(player);
        }
    }

    /**
     * 判断玩家是否趴下
     */
    private static void handlePlayerProne(Player player) {
        Level level = player.level();

        if (player.getBbHeight() <= 1) {
            player.getPersistentData().putDouble("prone", 3);
        }

        if (player.isShiftKeyDown() && level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 0.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()
                && !level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 1.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()) {
            player.getPersistentData().putDouble("prone", 3);
        }

        if (player.getPersistentData().getDouble("prone") > 0) {
            player.getPersistentData().putDouble("prone", (player.getPersistentData().getDouble("prone") - 1));
        }

        boolean flag = !(player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).refresh;

        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.refresh = flag;
            capability.syncPlayerVariables(player);
        });
    }

    /**
     * 判断玩家是否在奔跑
     */
    private static void handlePlayerSprint(Player player) {
        if (player.getMainHandItem().getOrCreateTag().getDouble("fireanim") > 0) {
            player.getPersistentData().putDouble("noRun", 20);
        }

        if (player.isShiftKeyDown() || player.isPassenger() || player.isInWater() || (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            player.getPersistentData().putDouble("noRun", 1);
        }

        if (player.getPersistentData().getDouble("noRun") > 0) {
            player.getPersistentData().putDouble("noRun", (player.getPersistentData().getDouble("noRun") - 1));
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            player.setSprinting(false);
        }
    }

    /**
     * 处理武器等级
     */
    private static void handleWeaponLevel(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModTags.Items.GUN)) {
            if (stack.getOrCreateTag().getDouble("level") == 0) {
                stack.getOrCreateTag().putDouble("exp2", 20);
            } else {
                stack.getOrCreateTag().putDouble("exp2", (stack.getOrCreateTag().getDouble("exp1") + stack.getOrCreateTag().getDouble("level") * 500));
            }
            if (stack.getOrCreateTag().getDouble("damagetotal") >= stack.getOrCreateTag().getDouble("exp2")) {
                stack.getOrCreateTag().putDouble("exp1", (stack.getOrCreateTag().getDouble("exp2")));
                stack.getOrCreateTag().putDouble("level", (stack.getOrCreateTag().getDouble("level") + 1));
            }
            stack.getOrCreateTag().putDouble("damagenow", (stack.getOrCreateTag().getDouble("damagetotal") - stack.getOrCreateTag().getDouble("exp1")));
            stack.getOrCreateTag().putDouble("damageneed", (stack.getOrCreateTag().getDouble("exp2") - stack.getOrCreateTag().getDouble("exp1")));
        }
    }

    private static void handleWeaponSway(Player player) {
        double[] recoilTimer = {0};
        double totalTime = 10;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;

        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {
                if (player == null)
                    return;
                double pose;
                if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && player.getPersistentData().getDouble("prone") == 0) {
                    pose = 0.85;
                } else if (player.getPersistentData().getDouble("prone") > 0) {
                    if (player.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1) {
                        pose = 0;
                    } else {
                        pose = 0.25;
                    }
                } else {
                    pose = 1;
                }
                player.getPersistentData().putDouble("time", (player.getPersistentData().getDouble("time") + 0.015));
                player.getPersistentData().putDouble("x", (pose * (-0.008) * Math.sin(1 * player.getPersistentData().getDouble("time")) * (1 - 0.9 * player.getPersistentData().getDouble("zoomtime"))));
                player.getPersistentData().putDouble("y", (pose * 0.125 * Math.sin(player.getPersistentData().getDouble("time") - 1.585) * (1 - 0.9 * player.getPersistentData().getDouble("zoomtime"))));

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

    public static String handleAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        String firemode = "";
        if (stack.getOrCreateTag().getDouble("firemode") == 2) {
            firemode = "Auto";
        } else if (stack.getOrCreateTag().getDouble("firemode") == 1) {
            firemode = "Burst";
        } else if (stack.getOrCreateTag().getDouble("firemode") == 0) {
            firemode = "Semi";
        }
        if (stack.getItem() == TargetModItems.BOCEK.get()) {
            return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("maxammo"))) + " " + firemode;
        }
        if (stack.getItem() == TargetModItems.MINIGUN.get()) {
            return new java.text.DecimalFormat("##").format((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo) + " " + firemode;
        }
        if (stack.is(TargetModTags.Items.RIFLE)) {
            stack.getOrCreateTag().putDouble("maxammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo));
            return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("ammo"))) + "/"
                    + new java.text.DecimalFormat("##").format((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo) + " " + firemode;
        }
        if (stack.is(TargetModTags.Items.HANDGUN)
                || stack.is(ItemTags.create(new ResourceLocation("target:smg")))) {
            stack.getOrCreateTag().putDouble("maxammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo));
            return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("ammo"))) + "/"
                    + new java.text.DecimalFormat("##").format((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo) + " " + firemode;
        }
        if (stack.is(TargetModTags.Items.SHOTGUN)) {
            stack.getOrCreateTag().putDouble("maxammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo));
            return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("ammo"))) + "/"
                    + new java.text.DecimalFormat("##").format((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo) + " " + firemode;
        }
        if (stack.is(TargetModTags.Items.SNIPER_RIFLE)) {
            stack.getOrCreateTag().putDouble("maxammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo));
            return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("ammo"))) + "/"
                    + new java.text.DecimalFormat("##").format((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo) + " " + firemode;
        }
        return (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("ammo"))) + "/"
                + (new java.text.DecimalFormat("##").format(stack.getOrCreateTag().getDouble("maxammo"))) + " " + firemode;
    }

    private static void handleFireTime(Player player) {
        double[] recoilTimer = {0};
        double totalTime = 50;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;
        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {
                if (player == null) {
                    return;
                }
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).firing > 0) {
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).firing - 0.05;
                        capability.syncPlayerVariables(player);
                    });
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

    private static void handleGround(Player player) {
        if (player.onGround()) {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.playerDoubleJump = false;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handlePrepareZoom(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.is(TargetModTags.Items.GUN) && stack.getOrCreateTag().getDouble("reloading") != 1 && !player.isSpectator()) {
            if (player.getMainHandItem().getItem() != TargetModItems.MINIGUN.get()) {
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) {
                    player.setSprinting(false);
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zooming = true;
                        capability.syncPlayerVariables(player);
                    });

                    if (player.getPersistentData().getDouble("zoom_time") < 10) {
                        player.getPersistentData().putDouble("zoom_time", (player.getPersistentData().getDouble("zoom_time") + 1));
                    }
                } else {
                    player.getPersistentData().putDouble("zoom_time", 0);
                }
            }
        }
    }

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == TargetModItems.RPG.get() && stack.getOrCreateTag().getDouble("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
        if (stack.getItem() == TargetModItems.BOCEK.get() && stack.getOrCreateTag().getDouble("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
    }

    private static void handleChangeFireRate(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(TargetModTags.Items.GUN)) {
            if (stack.getOrCreateTag().getDouble("cg") > 0) {
                stack.getOrCreateTag().putDouble("cg", (stack.getOrCreateTag().getDouble("cg") - 1));
            }
        }
    }

    /**
     * 望远镜瞄准时显示距离
     */
    private static void handleDistantRange(Player player) {
        ItemStack stack = player.getUseItem();
        if (stack.getItem() == Items.SPYGLASS) {
            if (player.position().distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                    new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(1024)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos()))) <= 512) {
                if (!player.level().isClientSide())
                    player.displayClientMessage(Component.literal((new java.text.DecimalFormat("##.#")
                            .format(player.position().distanceTo((Vec3.atLowerCornerOf(
                                    player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(768)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos()))))
                            + "M")), true);
            } else {
                if (player.level().isClientSide())
                    player.displayClientMessage(Component.literal("---M"), true);
            }
        }
    }


    private static void handleRenderDamageIndicator(Player player) {
        double[] recoilTimer = {0};
        double totalTime = 10;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;
        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {
                if (player == null) return;

                player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    var headIndicator = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.headIndicator).orElse(0d);
                    var hitIndicator = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.hitIndicator).orElse(0d);
                    var killIndicator = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.killIndicator).orElse(0d);

                    capability.headIndicator = Math.max(0, headIndicator - 1);
                    capability.hitIndicator = Math.max(0, hitIndicator - 1);
                    capability.killIndicator = Math.max(0, killIndicator - 1);

                    capability.syncPlayerVariables(player);
                });

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

    private static void handleBocekPulling(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowPullHold) {
            if (mainHandItem.getItem() == TargetModItems.BOCEK.get()
                    && tag.getDouble("maxammo") > 0
                    && !player.getCooldowns().isOnCooldown(mainHandItem.getItem())
                    && tag.getDouble("power") < 12
            ) {
                tag.putDouble("power", tag.getDouble("power") + 1);

                player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.bowPull = true;
                    capability.syncPlayerVariables(player);
                });
            }
            if (tag.getDouble("power") == 1) {
                if (!player.level().isClientSide() && player.getServer() != null) {
                    // TODO 修改为正确的音效播放
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), player.level() instanceof ServerLevel ? (ServerLevel) player.level() : null, 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_pull_1p player @s ~ ~ ~ 2 1");

                    player.level().playSound(null, player.blockPosition(), TargetModSounds.BOCEK_PULL_3P.get(), SoundSource.PLAYERS, 0.5f, 1);
                }
            }
        } else {
            if (mainHandItem.getItem() == TargetModItems.BOCEK.get()) {
                tag.putDouble("power", 0);
            }
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPull = false;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handleGunRecoil(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (!stack.is(TargetModTags.Items.GUN)) {
            return;
        }

        float recoilX = (float) stack.getOrCreateTag().getDouble("recoilx");
        float recoilY = (float) stack.getOrCreateTag().getDouble("recoily");
        float recoilYaw = (float) (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon;

        double[] recoilTimer = {0};
        double totalTime = 100;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;

        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {
                float rx;
                float ry;
                if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && player.getPersistentData().getDouble("prone") == 0) {
                    rx = 0.7f;
                    ry = 0.8f;
                } else if (player.getPersistentData().getDouble("prone") > 0) {
                    if (stack.getOrCreateTag().getDouble("bipod") == 1) {
                        rx = 0.05f;
                        ry = 0.1f;
                    } else {
                        rx = 0.5f;
                        ry = 0.7f;
                    }
                } else {
                    rx = 1f;
                    ry = 1f;
                }

                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil >= 1) {
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.recoil = 0;
                        capability.syncPlayerVariables(player);
                    });
                }

                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil > 0) {
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.recoil = (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil + 0.0025;
                        capability.syncPlayerVariables(player);
                    });

                    double sinRes = Math.sin(2 * Math.PI * (1.03f * (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoil - 0.032047110911)) + 0.2;

                    float newPitch = ((float) (player.getXRot() - 1.5f * recoilY * ry * sinRes));
                    player.setXRot(newPitch);
                    player.xRotO = player.getXRot();

                    float newYaw = ((float) (player.getYRot() - 1.0f * recoilYaw * recoilX * rx * sinRes));
                    player.setYRot(newYaw);
                    player.yRotO = player.getYRot();
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
}
