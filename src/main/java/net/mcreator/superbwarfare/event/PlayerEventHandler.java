package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.SimulationDistanceMessage;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.text.DecimalFormat;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", false);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        if (!ModVariables.MapVariables.get(player.level()).pvpMode) {
            return;
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModTags.Items.GUN)) {
                stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag"));
            }
        }

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.zoom = false;
            capability.zooming = false;
            capability.tacticalSprintExhaustion = false;
            capability.tacticalSprintTime = 600;
            capability.syncPlayerVariables(player);
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (event.phase == TickEvent.Phase.END) {
            if (stack.is(ModTags.Items.GUN)) {
                handleWeaponSway(player);
                handlePlayerProne(player);
                handlePlayerSprint(player);
                handleWeaponLevel(player);
                handleAmmoCount(player);
                handlePrepareZoom(player);
                handleSpecialWeaponAmmo(player);
                handleChangeFireRate(player);
                handleBocekPulling(player);
                handleGunRecoil(player);
            }

            handleGround(player);
            handleDistantRange(player);
            handleSimulationDistance(player);
            handleCannonTime(player);
            handleTacticalSprint(player);
            handleBreath(player);
        }
    }

    private static void handleWeaponSway(Player player) {
        if (player.getMainHandItem().is(ModTags.Items.GUN)) {
            float pose;
            var data = player.getPersistentData();

            if (player.isCrouching() && player.getBbHeight() >= 1 && data.getDouble("prone") == 0) {
                pose = 0.85f;
            } else if (player.getPersistentData().getDouble("prone") > 0) {
                pose = player.getMainHandItem().getOrCreateTag().getDouble("bipod") == 1 ? 0 : 0.25f;
            } else {
                pose = 1;
            }

            if (!player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breath &&
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zooming) {
                float newPitch = (float) (player.getXRot() - 0.03f * Mth.sin((float) (0.08 * player.tickCount)) * pose * Mth.nextDouble(RandomSource.create(), 0.1, 1));
                player.setXRot(newPitch);
                player.xRotO = player.getXRot();

                float newYaw = (float) (player.getYRot() - 0.015f * Mth.cos((float) (0.07 * (player.tickCount + 2 * Math.PI))) * pose * Mth.nextDouble(RandomSource.create(), 0.05, 1.25));
                player.setYRot(newYaw);
                player.yRotO = player.getYRot();
            }
        }
    }

    private static void handleBreath(Player player) {

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breath) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.breathTime = Mth.clamp(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                        .orElse(new ModVariables.PlayerVariables()).breathTime - 1, 0, 100);
                capability.syncPlayerVariables(player);
            });
        } else {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.breathTime = Mth.clamp(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                        .orElse(new ModVariables.PlayerVariables()).breathTime + 1, 0, 100);
                capability.syncPlayerVariables(player);
            });
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breathTime == 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.breathExhaustion = true;
                capability.breath = false;
                capability.syncPlayerVariables(player);
            });
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breathTime == 100) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.breathExhaustion = false;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handleTacticalSprint(Player player) {
        ItemStack stack = player.getMainHandItem();

        int sprint_cost;

        if (stack.is(ModTags.Items.GUN)) {
            double weight = stack.getOrCreateTag().getDouble("weight");
            if (weight == 0) {
                sprint_cost = 3;
            } else if (weight == 1) {
                sprint_cost = 4;
            } else if (weight == 2) {
                sprint_cost = 5;
            } else {
                sprint_cost = 2;
            }
        } else {
            sprint_cost = 2;
        }

        if (!player.isSprinting()) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprint = false;
                capability.syncPlayerVariables(player);
            });
            player.getPersistentData().putBoolean("canTacticalSprint", true);
        }

        if (player.isSprinting()
                && !(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).tacticalSprintExhaustion
                && player.getPersistentData().getBoolean("canTacticalSprint")) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprint = true;
                capability.syncPlayerVariables(player);
            });
            player.getPersistentData().putBoolean("canTacticalSprint", false);
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).tacticalSprint) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprintTime = Mth.clamp(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).tacticalSprintTime - sprint_cost, 0, 1000);
                capability.syncPlayerVariables(player);
            });
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 0, false, false));

        } else {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprintTime = Mth.clamp(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).tacticalSprintTime + 7, 0, 1000);
                capability.syncPlayerVariables(player);
            });
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).tacticalSprintTime == 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprintExhaustion = true;
                capability.tacticalSprint = false;
                capability.syncPlayerVariables(player);
            });
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).tacticalSprintTime == 1000) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprintExhaustion = false;
                capability.syncPlayerVariables(player);
            });
            player.getPersistentData().putBoolean("canTacticalSprint", true);
        }
    }

    private static void handleCannonTime(Player player) {
        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).cannonFiring > 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.cannonFiring = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).cannonFiring - 1;
                capability.syncPlayerVariables(player);
            });
        }
        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).cannonRecoil > 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.cannonRecoil = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).cannonRecoil - 1;
                capability.syncPlayerVariables(player);
            });
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

        if (player.isCrouching() && level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 0.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()
                && !level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 1.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()) {
            player.getPersistentData().putDouble("prone", 3);
        }

        if (player.getPersistentData().getDouble("prone") > 0) {
            player.getPersistentData().putDouble("prone", (player.getPersistentData().getDouble("prone") - 1));
        }
    }

    /**
     * 判断玩家是否在奔跑
     */
    private static void handlePlayerSprint(Player player) {
        if (player.getMainHandItem().getOrCreateTag().getInt("flash_time") > 0) {
            player.getPersistentData().putDouble("noRun", 20);
        }

        if (player.isShiftKeyDown() || player.isPassenger() || player.isInWater() || (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            player.getPersistentData().putDouble("noRun", 1);
        }

        if (player.getPersistentData().getDouble("noRun") > 0) {
            player.getPersistentData().putDouble("noRun", (player.getPersistentData().getDouble("noRun") - 1));
        }

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            player.setSprinting(false);
        }
    }

    /**
     * 处理武器等级
     */
    private static void handleWeaponLevel(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.GUN)) {
            var tag = stack.getOrCreateTag();
            if (tag.getInt("level") == 0) {
                tag.putDouble("exp2", 20);
            } else {
                tag.putDouble("exp2", (tag.getDouble("exp1") + tag.getInt("level") * 200 * (1 + 0.1 * tag.getInt("level"))));
            }
            if (tag.getDouble("damagetotal") >= tag.getDouble("exp2")) {
                tag.putDouble("exp1", (tag.getDouble("exp2")));
                tag.putInt("level", tag.getInt("level") + 1);
                tag.putDouble("UpgradePoint", tag.getDouble("UpgradePoint") + 0.25);
            }
            tag.putDouble("damagenow", (tag.getDouble("damagetotal") - tag.getDouble("exp1")));
            tag.putDouble("damageneed", (tag.getDouble("exp2") - tag.getDouble("exp1")));
        }
    }

    public static void handleAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo));
        }
        if (stack.is(ModTags.Items.USE_HANDGUN_AMMO) || stack.is(ModTags.Items.SMG)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).handgunAmmo));
        }
        if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).shotgunAmmo));
        }
        if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
            stack.getOrCreateTag().putInt("max_ammo",
                    ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).sniperAmmo));
        }
    }

    private static void handleGround(Player player) {
        if (player.onGround()) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.playerDoubleJump = false;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handlePrepareZoom(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.GUN) && !player.isSpectator()) {
            if (player.getMainHandItem().getItem() != ModItems.MINIGUN.get()) {
                if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
                    player.setSprinting(false);
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zooming = true;
                        capability.syncPlayerVariables(player);
                    });
                }
            }
        }
    }

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.RPG.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
        if (stack.getItem() == ModItems.BOCEK.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
    }

    private static void handleChangeFireRate(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.GUN)) {
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
                    player.displayClientMessage(Component.literal((new DecimalFormat("##.#")
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

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).bowPullHold) {
            if (mainHandItem.getItem() == ModItems.BOCEK.get()
                    && tag.getInt("max_ammo") > 0
                    && !player.getCooldowns().isOnCooldown(mainHandItem.getItem())
                    && tag.getDouble("power") < 12
            ) {
                tag.putDouble("power", tag.getDouble("power") + 1);

                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.bowPull = true;
                    capability.syncPlayerVariables(player);
                });
            }
            if (tag.getDouble("power") == 1) {
                if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.BOCEK_PULL_1P.get(), 2f, 1f);
                    player.level().playSound(null, player.blockPosition(), ModSounds.BOCEK_PULL_3P.get(), SoundSource.PLAYERS, 0.5f, 1);
                }
            }
        } else {
            if (mainHandItem.getItem() == ModItems.BOCEK.get()) {
                tag.putDouble("power", 0);
            }
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPull = false;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handleGunRecoil(Player player) {
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        float recoilX = (float) tag.getDouble("recoil_x");
        float recoilY = (float) tag.getDouble("recoil_y");
        float recoilPitch = 3f;
        float recoilYaw = 2f;

        float horizonRecoil = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.recoilHorizon).orElse(0d).floatValue();

        if (tag.getBoolean("shoot")) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = 2 * Math.random() - 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });
            tag.putBoolean("shoot", false);
        }

        double[] recoilTimer = {0};
        double totalTime = 20;
        int sleepTime = 2;
        double recoilDuration = totalTime / sleepTime;

        Runnable recoilRunnable = () -> {
            while (recoilTimer[0] < recoilDuration) {

                if (tag.getBoolean("shoot")) {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.recoilHorizon = 2 * Math.random() - 1;
                        capability.recoil = 0.1;
                        capability.firing = 1;
                        capability.syncPlayerVariables(player);
                    });
                    tag.putBoolean("shoot", false);
                }

                /*
                  开火动画计时器
                 */
                if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).firing > 0) {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).firing - 0.1;
                        capability.syncPlayerVariables(player);
                    });
                } else {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.firing = 0;
                        capability.syncPlayerVariables(player);
                    });
                }

                /*
                  计算后坐力
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

                double recoil = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.recoil).orElse(0d);

                if (recoil >= 2.5) recoil = 0d;

                double sinRes = 0;

                if (0 < recoil && recoil < 0.5) {
                    float newPitch = player.getXRot() - 0.05f * ry;
                    player.setXRot(newPitch);
                    player.xRotO = player.getXRot();
                }

                if (0 < recoil && recoil < 2) {
                    recoil = recoil + 0.025;
                    sinRes = Math.sin(Math.PI * recoil);
                }

                if (2 <= recoil && recoil < 2.5) {
                    recoil = recoil + 0.013;
                    sinRes = 0.4 * Math.sin(2 * Math.PI * recoil);
                }

                if (0 < recoil && recoil < 2.5) {
                    float newPitch = (float) (player.getXRot() - recoilPitch * recoilY * ry * (sinRes + Mth.clamp(0.8 - recoil, 0, 0.8)));
                    player.setXRot(newPitch);
                    player.xRotO = player.getXRot();

                    float newYaw = (float) (player.getYRot() - recoilYaw * horizonRecoil * recoilX * rx * sinRes);
                    player.setYRot(newYaw);
                    player.yRotO = player.getYRot();
                }

                double finalRecoil = recoil;
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(c -> {
                    c.recoil = finalRecoil;
                    c.syncPlayerVariables(player);
                });

                recoilTimer[0]++;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    ModUtils.LOGGER.error(e.getLocalizedMessage());
                }
            }
        };
        Thread recoilThread = new Thread(recoilRunnable);
        recoilThread.start();
    }

    private static void handleSimulationDistance(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            var distanceManager = serverLevel.getChunkSource().chunkMap.getDistanceManager();
            var playerTicketManager = distanceManager.playerTicketManager;
            int maxDistance = playerTicketManager.viewDistance;

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SimulationDistanceMessage(maxDistance));
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.is(ModTags.Items.GUN) && right.getItem() == ModItems.SHORTCUT_PACK.get()) {
            ItemStack output = left.copy();

            output.getOrCreateTag().putDouble("UpgradePoint", output.getOrCreateTag().getDouble("UpgradePoint") + 1);

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(1);
        }
    }
}
