package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.compat.CompatHolder;
import com.atsuishio.superbwarfare.compat.clothconfig.ClothConfigHelper;
import com.atsuishio.superbwarfare.config.client.ReloadConfig;
import com.atsuishio.superbwarfare.entity.ICannonEntity;
import com.atsuishio.superbwarfare.entity.IVehicleEntity;
import com.atsuishio.superbwarfare.entity.MortarEntity;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.*;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.cantFireTime;
import static com.atsuishio.superbwarfare.event.ClientEventHandler.drawTime;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClickHandler {

    public static boolean switchZoom = false;

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    @SubscribeEvent
    public static void onButtonReleased(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        int button = event.getButton();
        if (button == ModKeyMappings.FIRE.getKey().getValue()) {
            handleWeaponFireRelease();
        }
        if (button == ModKeyMappings.HOLD_ZOOM.getKey().getValue()) {
            handleWeaponZoomRelease();
            return;
        }

        if (button == ModKeyMappings.SWITCH_ZOOM.getKey().getValue() && !switchZoom) {
            handleWeaponZoomRelease();
        }
    }

    @SubscribeEvent
    public static void onButtonPressed(InputEvent.MouseButton.Pre event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();


        int button = event.getButton();

        if (player.getMainHandItem().is(ModTags.Items.GUN)
                || stack.is(ModItems.MONITOR.get())
                || player.hasEffect(ModMobEffects.SHOCK.get())
                || (player.getVehicle() != null && player.getVehicle() instanceof IVehicleEntity)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                event.setCanceled(true);
            }
        }

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (stack.is(ModTags.Items.GUN)
                    || (player.isPassenger() && player.getVehicle() instanceof ICannonEntity)
                    || (player.getVehicle() != null && player.getVehicle() instanceof SpeedboatEntity boat && boat.getFirstPassenger() == player && stack.is(ItemStack.EMPTY.getItem()))) {
                event.setCanceled(true);
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
            }
        }

        if ((player.getMainHandItem().is(ModTags.Items.GUN) && !(player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity))
                || stack.is(ModItems.MONITOR.get())
                || (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity)
                || (player.getVehicle() != null && player.getVehicle() instanceof SpeedboatEntity boat && boat.getFirstPassenger() == player && stack.is(ItemStack.EMPTY.getItem()))
                || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get()))) {
            if (button == ModKeyMappings.FIRE.getKey().getValue()) {
                handleWeaponFirePress(player, stack);
            }

            if (button == ModKeyMappings.HOLD_ZOOM.getKey().getValue()) {
                handleWeaponZoomPress();
                switchZoom = false;
                return;
            }

            if (button == ModKeyMappings.SWITCH_ZOOM.getKey().getValue()) {
                handleWeaponZoomPress();
                switchZoom = !switchZoom;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;

        if (notInGame()) return;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        double scroll = event.getScrollDelta();

        if (stack.is(ModTags.Items.GUN) && ClientEventHandler.zoom) {
            var tag = stack.getOrCreateTag();
            if (tag.getBoolean("CanSwitchScope")) {
                ModUtils.PACKET_HANDLER.sendToServer(new SwitchScopeMessage(scroll));
            } else if (tag.getBoolean("CanAdjustZoomFov") || stack.is(ModItems.MINIGUN.get())) {
                ModUtils.PACKET_HANDLER.sendToServer(new AdjustZoomFovMessage(scroll));
            }
            event.setCanceled(true);
        }

        if (player.getVehicle() instanceof IVehicleEntity iVehicle && iVehicle.isDriver(player) && ClientEventHandler.zoom) {
            ClientEventHandler.vehicleFov = Mth.clamp(ClientEventHandler.vehicleFov + 0.4 * scroll, 1, 6);
            event.setCanceled(true);
        }

        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            ClientEventHandler.droneFov = Mth.clamp(ClientEventHandler.droneFov + 0.4 * scroll, 1, 6);
            event.setCanceled(true);
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking == null) return;
        if (looking instanceof MortarEntity && player.isShiftKeyDown()) {
            ModUtils.PACKET_HANDLER.sendToServer(new AdjustMortarAngleMessage(scroll));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (notInGame()) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        setKeyState(event);

        int key = event.getKey();
        if (event.getAction() == GLFW.GLFW_PRESS) {

            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                return;
            }

            if (key == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
                handleDoubleJump(player);
            }
            if (key == ModKeyMappings.CONFIG.getKey().getValue()) {
                handleConfigScreen(player);
            }
            if (key == ModKeyMappings.RELOAD.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new ReloadMessage(0));
            }
            if (key == ModKeyMappings.EDIT_MODE.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new EditModeMessage(0));
            }

            if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
                if (ModKeyMappings.EDIT_GRIP.getKeyModifier().isActive(KeyConflictContext.IN_GAME)) {
                    if (key == ModKeyMappings.EDIT_GRIP.getKey().getValue() && stack.is(ModTags.Items.CAN_APPLY_GRIP)) {
                        ModUtils.PACKET_HANDLER.sendToServer(new EditMessage(4));
                        editModelShake();
                    }
                } else {
                    if (key == ModKeyMappings.EDIT_SCOPE.getKey().getValue() && stack.is(ModTags.Items.CAN_APPLY_SCOPE)) {
                        ModUtils.PACKET_HANDLER.sendToServer(new EditMessage(0));
                        editModelShake();
                    } else if (key == ModKeyMappings.EDIT_BARREL.getKey().getValue() && stack.is(ModTags.Items.CAN_APPLY_BARREL)) {
                        ModUtils.PACKET_HANDLER.sendToServer(new EditMessage(1));
                        editModelShake();
                    } else if (key == ModKeyMappings.EDIT_MAGAZINE.getKey().getValue() && stack.is(ModTags.Items.CAN_APPLY_MAGAZINE)) {
                        ModUtils.PACKET_HANDLER.sendToServer(new EditMessage(2));
                        editModelShake();
                    } else if (key == ModKeyMappings.EDIT_STOCK.getKey().getValue() && stack.is(ModTags.Items.CAN_APPLY_STOCK)) {
                        ModUtils.PACKET_HANDLER.sendToServer(new EditMessage(3));
                        editModelShake();
                    }
                }
            }
            if (key == ModKeyMappings.SENSITIVITY_INCREASE.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new SensitivityMessage(true));
            }
            if (key == ModKeyMappings.SENSITIVITY_REDUCE.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new SensitivityMessage(false));
            }

            if ((player.getMainHandItem().is(ModTags.Items.GUN) && !(player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity))
                    || stack.is(ModItems.MONITOR.get())
                    || (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity)
                    || (player.getVehicle() != null && player.getVehicle() instanceof SpeedboatEntity boat && boat.getFirstPassenger() == player && stack.is(ItemStack.EMPTY.getItem()))
                    || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get()))) {
                if (key == ModKeyMappings.FIRE.getKey().getValue()) {
                    handleWeaponFirePress(player, stack);
                }

                if (key == ModKeyMappings.HOLD_ZOOM.getKey().getValue()) {
                    handleWeaponZoomPress();
                    switchZoom = false;
                    return;
                }

                if (key == ModKeyMappings.SWITCH_ZOOM.getKey().getValue()) {
                    handleWeaponZoomPress();
                    switchZoom = !switchZoom;
                }
            }
        } else {

            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                return;
            }

            if (key == ModKeyMappings.FIRE.getKey().getValue()) {
                handleWeaponFireRelease();
            }
            if (key == ModKeyMappings.HOLD_ZOOM.getKey().getValue()) {
                handleWeaponZoomRelease();
                return;
            }

            if (key == ModKeyMappings.SWITCH_ZOOM.getKey().getValue() && !switchZoom) {
                handleWeaponZoomRelease();
            }
        }
    }

    public static void handleWeaponFirePress(Player player, ItemStack stack) {

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        if (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get())) {
            ModUtils.PACKET_HANDLER.sendToServer(new SetFiringParametersMessage(0));
        }

        if (stack.is(ModItems.MONITOR.get())) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneFireMessage(0));
        }

        if (player.getVehicle() instanceof ICannonEntity) {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleFireMessage(0));
            return;
        }

        if (player.getVehicle() instanceof IVehicleEntity iVehicle && iVehicle.isDriver(player)) {
            ClientEventHandler.holdFire = true;
        }

        if (stack.is(ModTags.Items.GUN) && !(player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity)) {
            if ((!(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                    && !stack.getOrCreateTag().getBoolean("reloading")
                    && !stack.getOrCreateTag().getBoolean("charging")
                    && !stack.getOrCreateTag().getBoolean("need_bolt_action"))
                    && cantFireTime == 0
                    && drawTime < 0.01
                    && !notInGame()) {
                player.playSound(ModSounds.TRIGGER_CLICK.get(), 1, 1);
            }

            if (!stack.is(ModTags.Items.CANNOT_RELOAD) && stack.getOrCreateTag().getInt("ammo") <= 0) {
                if (ReloadConfig.LEFT_CLICK_RELOAD.get()) {
                    ModUtils.PACKET_HANDLER.sendToServer(new ReloadMessage(0));
                }
            } else {
                ModUtils.PACKET_HANDLER.sendToServer(new FireMessage(0));
                if (!stack.is(ModItems.BOCEK.get())) {
                    ClientEventHandler.holdFire = true;
                }
                if (GunsTool.getGunIntTag(stack, "FireMode") == 1 && ClientEventHandler.burstFireSize == 0) {
                    ClientEventHandler.burstFireSize = (int) stack.getOrCreateTag().getDouble("burst_size");
                }
            }
        }
    }

    public static void handleWeaponFireRelease() {
        ModUtils.PACKET_HANDLER.sendToServer(new FireMessage(1));
        ClientEventHandler.holdFire = false;
        ClientEventHandler.customRpm = 0;
    }

    public static void handleWeaponZoomPress() {
        ModUtils.PACKET_HANDLER.sendToServer(new ZoomMessage(0));
        ClientEventHandler.zoom = true;
    }

    public static void handleWeaponZoomRelease() {
        ModUtils.PACKET_HANDLER.sendToServer(new ZoomMessage(1));
        ClientEventHandler.zoom = false;
    }

    private static void editModelShake() {
        ClientEventHandler.movePosY = -0.8;
        ClientEventHandler.fireRotTimer = 0.4;
    }

    private static void setKeyState(InputEvent.Key event) {
        int key = event.getKey();
        int state;
        if (event.getAction() == InputConstants.PRESS) {
            state = 1;
        } else if (event.getAction() == InputConstants.RELEASE) {
            state = 0;
        } else {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        handleDroneMove(key, state, player);
        handleVehicleMove(key, state, player);
    }

    private static void handleDroneMove(int key, int state, Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.MONITOR.get())) return;
        if (!stack.getOrCreateTag().getBoolean("Using")) return;
        if (!stack.getOrCreateTag().getBoolean("Linked")) return;

        var options = Minecraft.getInstance().options;

        if (key == options.keyLeft.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(0, state == 1));
        } else if (key == options.keyRight.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(1, state == 1));
        } else if (key == options.keyUp.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(2, state == 1));
        } else if (key == options.keyDown.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(3, state == 1));
        } else if (key == options.keyJump.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(4, state == 1));
        } else if (key == options.keyShift.getKey().getValue()) {
            ModUtils.PACKET_HANDLER.sendToServer(new DroneMovementMessage(5, state == 1));
        }
    }

    private static void handleVehicleMove(int key, int state, Player player) {
        if (player.getVehicle() != null && player.getVehicle() instanceof IVehicleEntity && player.getVehicle().getFirstPassenger() == player) {

            var options = Minecraft.getInstance().options;

            if (key == options.keyLeft.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(0, state == 1));
            } else if (key == options.keyRight.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(1, state == 1));
            } else if (key == options.keyUp.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(2, state == 1));
            } else if (key == options.keyDown.getKey().getValue()) {
                ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(3, state == 1));
            }
        }
    }

    private static void handleDoubleJump(Player player) {
        Level level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).playerDoubleJump) {
            player.setDeltaMovement(new Vec3(player.getLookAngle().x, 0.8, player.getLookAngle().z));
            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1, false);

            ModUtils.PACKET_HANDLER.sendToServer(new DoubleJumpMessage(false));
        }
    }

    private static void handleConfigScreen(Player player) {
        if (ModList.get().isLoaded(CompatHolder.CLOTH_CONFIG)) {
            CompatHolder.hasMod(CompatHolder.CLOTH_CONFIG, () -> Minecraft.getInstance().setScreen(ClothConfigHelper.getConfigScreen(null)));
        } else {
            player.displayClientMessage(Component.translatable("des.superbwarfare.no_cloth_config").withStyle(ChatFormatting.RED), true);
        }
    }

}