package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.common.GameplayConfig;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.SimulationDistanceMessage;
import net.mcreator.superbwarfare.tools.GunInfo;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

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

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.zoom = false;
            capability.tacticalSprintExhaustion = false;
            capability.tacticalSprintTime = 600;
            capability.syncPlayerVariables(player);
        });

        handleRespawnReload(player);
        handleRespawnAutoArmor(player);
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
                handlePlayerSprint(player);
                handleSpecialWeaponAmmo(player);
                handleBocekPulling(player);
                isProne(player);
            }

            handleGround(player);
            handleSimulationDistance(player);
            handleTacticalSprint(player);
            handleBreath(player);
        }
    }

    public static boolean isProne(Player player) {
        Level level = player.level();
        if (player.getBbHeight() <= 1) return true;

        return player.isCrouching() && level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 0.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()
                && !level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 1.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude();
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

    /**
     * 判断玩家是否在奔跑
     */
    private static void handlePlayerSprint(Player player) {
        if (player.getMainHandItem().getOrCreateTag().getInt("flash_time") > 0 || player.getMainHandItem().getOrCreateTag().getInt("fire_animation") > 0) {
            player.getPersistentData().putDouble("noRun", 20);
        }

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).holdFire) {
            player.getPersistentData().putDouble("noRun", 10);
        }

        if (player.isShiftKeyDown()
                || player.isPassenger()
                || player.isInWater()
                || (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
            player.getPersistentData().putDouble("noRun", 3);
        }

        if (player.getPersistentData().getDouble("noRun") > 0) {
            player.getPersistentData().putDouble("noRun", (player.getPersistentData().getDouble("noRun") - 1));
        }

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom
                || (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).holdFire) {
            player.setSprinting(false);
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

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.RPG.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
        if (stack.getItem() == ModItems.BOCEK.get() && stack.getOrCreateTag().getInt("ammo") == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
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
                    capability.tacticalSprint = false;
                    capability.syncPlayerVariables(player);
                });
                player.setSprinting(false);
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

        if (tag.getDouble("power") > 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.tacticalSprint = false;
                capability.syncPlayerVariables(player);
            });
            player.setSprinting(false);
        }
    }

    private static void handleSimulationDistance(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            var distanceManager = serverLevel.getChunkSource().chunkMap.getDistanceManager();
            var playerTicketManager = distanceManager.playerTicketManager;
            int maxDistance = playerTicketManager.viewDistance;

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SimulationDistanceMessage(maxDistance));
        }
    }

    private static void handleRespawnReload(Player player) {
        if (!GameplayConfig.RESPAWN_RELOAD.get()) return;

        int count = 0;
        for (var inv : player.getInventory().items) {
            if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                count++;
            }
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModTags.Items.GUN)) {
                if (count == 0) {
                    var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

                    if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO) && cap.shotgunAmmo > 0) {
                        GunsTool.reload(player, stack, GunInfo.Type.SHOTGUN);
                    }
                    if (stack.is(ModTags.Items.USE_SNIPER_AMMO) && cap.sniperAmmo > 0) {
                        GunsTool.reload(player, stack, GunInfo.Type.SNIPER);
                    }
                    if (stack.is(ModTags.Items.USE_HANDGUN_AMMO) && cap.handgunAmmo > 0) {
                        GunsTool.reload(player, stack, GunInfo.Type.HANDGUN);
                    }
                    if (stack.is(ModTags.Items.USE_RIFLE_AMMO) && cap.rifleAmmo > 0) {
                        GunsTool.reload(player, stack, GunInfo.Type.RIFLE);
                    }
                    if (stack.getItem() == ModItems.TASER.get() && stack.getOrCreateTag().getInt("max_ammo") > 0) {
                        stack.getOrCreateTag().putInt("ammo", 1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.TASER_ELECTRODE.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.M_79.get() && stack.getOrCreateTag().getInt("max_ammo") > 0) {
                        stack.getOrCreateTag().putInt("ammo", 1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.GRENADE_40MM.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.RPG.get() && stack.getOrCreateTag().getInt("max_ammo") > 0) {
                        stack.getOrCreateTag().putInt("ammo", 1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.ROCKET.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.JAVELIN.get() && stack.getOrCreateTag().getInt("max_ammo") > 0) {
                        stack.getOrCreateTag().putInt("ammo", 1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.JAVELIN_MISSILE.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                } else {
                    stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag"));
                }
                stack.getOrCreateTag().putBoolean("HoldOpen", false);
            }
        }
    }

    private static void handleRespawnAutoArmor(Player player) {
        if (!GameplayConfig.RESPAWN_AUTO_ARMOR.get()) return;

        ItemStack armor = player.getItemBySlot(EquipmentSlot.CHEST);
        if (armor == ItemStack.EMPTY) return;

        double armorPlate = armor.getOrCreateTag().getDouble("ArmorPlate");

        int armorLevel = 1;
        if (armor.is(ModTags.Items.MILITARY_ARMOR)) {
            armorLevel = 2;
        } else if (armor.is(ModTags.Items.MILITARY_ARMOR_HEAVY)) {
            armorLevel = 3;
        }

        if (armorPlate < armorLevel * 30) {
            for (var stack : player.getInventory().items) {
                if (stack.is(ModItems.ARMOR_PLATE.get())) {
                    for (int index0 = 0; index0 < Math.ceil(((armorLevel * 30) - armorPlate) / 30); index0++) {
                        stack.finishUsingItem(player.level(), player);
                    }
                }
            }
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
