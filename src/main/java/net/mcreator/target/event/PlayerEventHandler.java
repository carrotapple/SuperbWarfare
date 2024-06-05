package net.mcreator.target.event;

import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag"));
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
            handleAmmoCount(player);
            handleGround(player);
            handlePrepareZoom(player);
            handleSpecialWeaponAmmo(player);
            handleChangeFireRate(player);
            handleDistantRange(player);
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
        if (player.getMainHandItem().getOrCreateTag().getInt("flash_time") > 0) {
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
            var tag = stack.getOrCreateTag();
            if (tag.getInt("level") == 0) {
                tag.putDouble("exp2", 20);
            } else {
                tag.putDouble("exp2", (tag.getDouble("exp1") + tag.getInt("level") * 500));
            }
            if (tag.getDouble("damagetotal") >= tag.getDouble("exp2")) {
                tag.putDouble("exp1", (tag.getDouble("exp2")));
                tag.putInt("level", tag.getInt("level") + 1);
            }
            tag.putDouble("damagenow", (tag.getDouble("damagetotal") - tag.getDouble("exp1")));
            tag.putDouble("damageneed", (tag.getDouble("exp2") - tag.getDouble("exp1")));
        }
    }

    public static void handleAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.is(TargetModTags.Items.RIFLE)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo));
        }
        if (stack.is(TargetModTags.Items.HANDGUN) || stack.is(TargetModTags.Items.SMG)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo));
        }
        if (stack.is(TargetModTags.Items.SHOTGUN)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo));
        }
        if (stack.is(TargetModTags.Items.SNIPER_RIFLE)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo));
        }
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

        if (stack.is(TargetModTags.Items.GUN) && !stack.getOrCreateTag().getBoolean("reloading") && !player.isSpectator()) {
            if (player.getMainHandItem().getItem() != TargetModItems.MINIGUN.get()) {
                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom) {
                    player.setSprinting(false);
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zooming = true;
                        capability.syncPlayerVariables(player);
                    });

                    if (player.getPersistentData().getInt("zoom_animation_time") < 10) {
                        player.getPersistentData().putInt("zoom_animation_time", player.getPersistentData().getInt("zoom_animation_time") + 1);
                    }
                } else {
                    player.getPersistentData().putInt("zoom_animation_time", 0);
                }
            }
        }
    }

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == TargetModItems.RPG.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
        if (stack.getItem() == TargetModItems.BOCEK.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
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

    private static void handleBocekPulling(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).bowPullHold) {
            if (mainHandItem.getItem() == TargetModItems.BOCEK.get()
                    && tag.getInt("max_ammo") > 0
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
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.BOCEK_PULL_1P.get(), 2f, 1f);
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
        if (!player.getMainHandItem().is(TargetModTags.Items.GUN)) return;

        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        float recoilX = (float) tag.getDouble("recoil_x");
        float recoilY = (float) tag.getDouble("recoil_y");

        float recoilYaw = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.recoilHorizon).orElse(0d).floatValue();

        double[] recoilTimer = {0};
        double totalTime = 20;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;

        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {

                /**
                 * 开火动画计时器
                 */

                if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).firing > 0) {
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).firing - 0.1;
                        capability.syncPlayerVariables(player);
                    });
                } else {
                    player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = 0;
                        capability.syncPlayerVariables(player);
                    });
                }

                /**
                 * 计算后坐力
                 */

                float rx, ry;
                if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && player.getPersistentData().getDouble("prone") == 0) {
                    rx = 0.7f;
                    ry = 0.8f;
                } else if (player.getPersistentData().getDouble("prone") > 0) {
                    if (tag.getDouble("bipod") == 1) {
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

                double recoil = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.recoil).orElse(0d);

                if (recoil >= 2) recoil = 0d;

                if (0 < recoil && recoil < 2) {
                    recoil = recoil + 0.017 * (2.1 - recoil);

                    double sinRes = 0;
                    sinRes = 0.35 * Math.sin(Math.PI * (1.5 * recoil)) * (3 - Math.pow(recoil , 2)) + 0.065;

                    float newPitch = ((float) (player.getXRot() - 5f * recoilY * ry * (sinRes + Mth.clamp(0.8 - recoil,0,0.8))));
                    player.setXRot(newPitch);
                    player.xRotO = player.getXRot();

                    float newYaw = ((float) (player.getYRot() - 5f * recoilYaw * recoilX * rx * sinRes));
                    player.setYRot(newYaw);
                    player.yRotO = player.getYRot();
                }

                /**
                 * 计算散布
                 */

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
                            .setBaseValue(player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) + 0.125 * Math.pow(index - player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()), 2));
                } else {
                    player.getAttribute(TargetModAttributes.SPREAD.get())
                            .setBaseValue(player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) - 0.125 * Math.pow(index - player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()), 2));
                }

                if (player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) > 15) {
                    player.getAttribute(TargetModAttributes.SPREAD.get()).setBaseValue(15);
                }
                if (player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()) < 0) {
                    player.getAttribute(TargetModAttributes.SPREAD.get()).setBaseValue(0);
                }


                double finalRecoil = recoil;
                player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(c -> {
                    c.recoil = finalRecoil;
                    c.syncPlayerVariables(player);
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

}
