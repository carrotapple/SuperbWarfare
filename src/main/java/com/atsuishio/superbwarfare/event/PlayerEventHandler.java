package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.SimulationDistanceMessage;
import com.atsuishio.superbwarfare.tools.AmmoType;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", false);
        }
        for (ItemStack pStack : player.getInventory().items) {
            if (pStack.is(ModTags.Items.GUN)) {
                pStack.getOrCreateTag().putBoolean("draw", true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        handleRespawnReload(player);
        handleRespawnAutoArmor(player);

        for (ItemStack pStack : player.getInventory().items) {
            if (pStack.is(ModTags.Items.GUN)) {
                pStack.getOrCreateTag().putBoolean("draw", true);
            }
        }
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
                handleSpecialWeaponAmmo(player);
            }

            handleSimulationDistance(player);
        }
    }

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();
        var data = GunData.from(stack);

        if ((stack.is(ModItems.RPG.get()) || stack.is(ModItems.BOCEK.get())) && data.getAmmo() == 1) {
            stack.getOrCreateTag().putDouble("empty", 0);
        }
    }

    private static void handleSimulationDistance(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            var distanceManager = serverLevel.getChunkSource().chunkMap.getDistanceManager();
            var playerTicketManager = distanceManager.playerTicketManager;
            int maxDistance = playerTicketManager.viewDistance;

            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SimulationDistanceMessage(maxDistance));
        }
    }

    private static void handleRespawnReload(Player player) {
        if (!GameplayConfig.RESPAWN_RELOAD.get()) return;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModTags.Items.GUN)) {
                var data = GunData.from(stack);
                if (!InventoryTool.hasCreativeAmmoBox(player)) {
                    var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

                    if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO) && cap.shotgunAmmo > 0) {
                        GunsTool.reload(player, stack, AmmoType.SHOTGUN);
                    }
                    if (stack.is(ModTags.Items.USE_SNIPER_AMMO) && cap.sniperAmmo > 0) {
                        GunsTool.reload(player, stack, AmmoType.SNIPER);
                    }
                    if (stack.is(ModTags.Items.USE_HANDGUN_AMMO) && cap.handgunAmmo > 0) {
                        GunsTool.reload(player, stack, AmmoType.HANDGUN);
                    }
                    if (stack.is(ModTags.Items.USE_RIFLE_AMMO) && cap.rifleAmmo > 0) {
                        GunsTool.reload(player, stack, AmmoType.RIFLE);
                    }
                    if (stack.is(ModTags.Items.USE_HEAVY_AMMO) && cap.heavyAmmo > 0) {
                        GunsTool.reload(player, stack, AmmoType.HEAVY);
                    }

                    if (stack.getItem() == ModItems.TASER.get() && GunsTool.getGunIntTag(stack, "MaxAmmo") > 0 && data.getAmmo() == 0) {
                        data.setAmmo(1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.TASER_ELECTRODE.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.M_79.get() && GunsTool.getGunIntTag(stack, "MaxAmmo") > 0 && data.getAmmo() == 0) {
                        data.setAmmo(1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.GRENADE_40MM.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.RPG.get() && GunsTool.getGunIntTag(stack, "MaxAmmo") > 0 && data.getAmmo() == 0) {
                        data.setAmmo(1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.ROCKET.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                    if (stack.getItem() == ModItems.JAVELIN.get() && GunsTool.getGunIntTag(stack, "MaxAmmo") > 0 && data.getAmmo() == 0) {
                        data.setAmmo(1);
                        player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == ModItems.JAVELIN_MISSILE.get(), 1, player.inventoryMenu.getCraftSlots());
                    }
                } else {
                    data.setAmmo(data.magazine());
                }
                GunsTool.setGunBooleanTag(stack, "HoldOpen", false);
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

        if (armorPlate < armorLevel * 15) {
            for (var stack : player.getInventory().items) {
                if (stack.is(ModItems.ARMOR_PLATE.get())) {
                    if (stack.getTag() != null && stack.getTag().getBoolean("Infinite")) {
                        armor.getOrCreateTag().putDouble("ArmorPlate", armorLevel * 15);

                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.5f, 1);
                        }
                    } else {
                        for (int index0 = 0; index0 < Math.ceil(((armorLevel * 15) - armorPlate) / 15); index0++) {
                            stack.finishUsingItem(player.level(), player);
                        }
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
            var data = GunData.from(output);

            data.setUpgradePoint(data.getUpgradePoint() + 1);

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(1);
        }
    }
}
