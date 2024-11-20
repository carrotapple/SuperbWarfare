package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.network.message.ShootMessage;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.MillisTimer;
import net.mcreator.superbwarfare.tools.SeekTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_X;
import static net.mcreator.superbwarfare.entity.DroneEntity.ROT_Z;
import static net.mcreator.superbwarfare.event.PlayerEventHandler.isProne;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    public static double zoomTime = 0;
    public static double zoomPos = 0;
    public static double zoomPosZ = 0;
    public static double swayTime = 0;
    public static double swayX = 0;
    public static double swayY = 0;
    public static double moveXTime = 0;
    public static double moveYTime = 0;
    public static double movePosX = 0;
    public static double movePosY = 0;
    public static double moveRotZ = 0;
    public static double movePosHorizon = 0;
    public static double velocityY = 0;

    public static double[] turnRot = {0, 0, 0};
    public static double[] cameraRot = {0, 0, 0};

    public static double fireRecoilTime = 0;
    public static double firePosTimer = 0;
    public static double fireRotTimer = 0;
    public static double firePos = 0;
    public static double firePosZ = 0;
    public static double fireRot = 0;

    public static double recoilTime = 0;

    public static double recoilHorizon = 0;

    public static double recoilY = 0;
    public static double droneCameraRotX = 0;
    public static double droneCameraRotY = 0;
    public static double droneRotX = 0;
    public static double droneRotZ = 0;
    public static double breathTime = 0;

    public static double fov = 0;

    public static double pullTimer = 0;
    public static double bowTimer = 0;
    public static double handTimer = 0;
    public static double pullPos = 0;
    public static double bowPos = 0;
    public static double handPos = 0;
    public static double gunSpread = 0;
    public static double fireSpread = 0;
    public static double cantFireTime = 0;
    public static double lookDistance = 0;
    public static double cameraLocation = 0.6;

    public static double drawTime = 1;

    public static int shellIndex = 0;
    public static double[] shellIndexTime = {0, 0, 0, 0, 0, 0};
    public static double[] randomShell = {0, 0, 0};

    public static double customZoom = 0;
    public static MillisTimer clientTimer = new MillisTimer();

    public static boolean holdFire = false;
    public static int burstFireSize = 0;

    public static int customRpm = 0;

    public static double chamberRot = 0;
    public static double actionMove = 0;

    public static int miniGunRot = 0;

    public static double revolverPreTime = 0;

    public static double revolverWheelPreTime = 0;


    @SubscribeEvent
    public static void handleWeaponTurn(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        float xRotOffset = Mth.lerp(event.getPartialTick(), player.xBobO, player.xBob);
        float yRotOffset = Mth.lerp(event.getPartialTick(), player.yBobO, player.yBob);
        float xRot = player.getViewXRot(event.getPartialTick()) - xRotOffset;
        float yRot = player.getViewYRot(event.getPartialTick()) - yRotOffset;
        turnRot[0] = Mth.clamp(0.05 * xRot, -5, 5) * (1 - 0.75 * zoomTime);
        turnRot[1] = Mth.clamp(0.05 * yRot, -10, 10) * (1 - 0.75 * zoomTime) + 1.5f * (Mth.DEG_TO_RAD * recoilHorizon) * (0.5 + 0.4 * fireSpread);
        turnRot[2] = Mth.clamp(0.1 * yRot, -10, 10) * (1 - zoomTime);

        droneCameraRotX = Mth.clamp(0.25f * xRot, -10, 10);
        droneCameraRotY = Mth.clamp(0.25f * yRot, -20, 10);
    }

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    private static boolean revolverPre() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.REVOLVER)) {
            return true;
        } else if (stack.is(ModTags.Items.REVOLVER) && (stack.getOrCreateTag().getBoolean("DA") || stack.getOrCreateTag().getBoolean("canImmediatelyShoot"))){
            return true;
        }else {
            return revolverPreTime >= 1;
        }
    }

    private static boolean isMoving() {
        Player player = Minecraft.getInstance().player;
        return Minecraft.getInstance().options.keyLeft.isDown()
                || Minecraft.getInstance().options.keyRight.isDown()
                || Minecraft.getInstance().options.keyUp.isDown()
                || Minecraft.getInstance().options.keyDown.isDown()
                || (player != null && player.isSprinting());
    }

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == ModItems.MINIGUN.get()) {
            if (holdFire || GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
                miniGunRot = Math.min(miniGunRot + 5, 21);
                float rpm = 1;

                if (player.getMainHandItem().is(ModItems.MINIGUN.get())) {
                    rpm = (float) player.getMainHandItem().getOrCreateTag().getInt("rpm") / 3600;
                }
                player.playSound(ModSounds.MINIGUN_ROT.get(), 1, 0.7f + rpm);
            }
        }

        if (miniGunRot > 0) {
            miniGunRot -= 1;
        }
    }

    @SubscribeEvent
    public static void handleWeaponFire(TickEvent.RenderTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (player == null) return;
        if (level == null) return;

        ItemStack stack = player.getMainHandItem();
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        int mode = GunsTool.getGunIntTag(stack, "FireMode");

        // 精准度
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        double basicDev = stack.getOrCreateTag().getDouble("spread");
        double walk = isMoving() ? 0.3 * basicDev : 0;
        double sprint = player.isSprinting() ? 0.25 * basicDev : 0;
        double crouching = player.isCrouching() ? -0.15 * basicDev : 0;
        double prone = isProne(player) ? -0.3 * basicDev : 0;
        double jump = player.onGround() ? 0 * basicDev : 0.35 * basicDev;
        double ride = player.onGround() ? -0.25 * basicDev : 0;

        double zoomSpread;

        if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            zoomSpread = 1 - (0.995 * zoomTime);
        } else if (stack.is(ModTags.Items.SHOTGUN)) {
            if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
                zoomSpread = 1 - (0.85 * zoomTime);
            } else {
                zoomSpread = 1 - (0.25 * zoomTime);
            }
        } else if (stack.is(ModItems.MINIGUN.get())) {
            zoomSpread = 1 - (0.25 * zoomTime);
        } else {
            zoomSpread = 1 - (0.9 * zoomTime);
        }

        double spread = stack.is(ModTags.Items.SHOTGUN) || stack.is(ModItems.MINIGUN.get()) ? 1.2 * zoomSpread * (basicDev + 0.2 * (walk + sprint + crouching + prone + jump + ride) + fireSpread) : zoomSpread * (0.7 * basicDev + walk + sprint + crouching + prone + jump + ride + 0.8 * fireSpread);

        gunSpread = Mth.lerp(0.07 * times, gunSpread, spread);

        // 开火部分

        double weight = stack.getOrCreateTag().getDouble("weight") + stack.getOrCreateTag().getDouble("CustomWeight");
        double speed = 1 - (0.04 * weight);

        if (player.getPersistentData().getDouble("noRun") == 0 && player.isSprinting() && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) {
            cantFireTime = Mth.clamp(cantFireTime + 3 * times, 0, 24);
        } else {
            cantFireTime = Mth.clamp(cantFireTime - 6 * speed * times, 0, 40);
        }

        int rpm = (int) (stack.getOrCreateTag().getDouble("rpm") + customRpm);
        if (rpm == 0) {
            rpm = 600;
        }

        if (GunsTool.getPerkIntTag(stack, "DesperadoTimePost") > 0) {
            int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.DESPERADO.get(), stack);
            rpm *= (int) (1.25 + 0.05 * perkLevel);
        }

        double rps = (double) rpm / 60;

        // cooldown in ms
        int cooldown = (int) (1000 / rps);

        //左轮类

        if (clientTimer.getProgress() == 0 && stack.is(ModTags.Items.REVOLVER) && ((holdFire && !stack.getOrCreateTag().getBoolean("DA")) || (stack.getOrCreateTag().getInt("bolt_action_anim") < 7 && stack.getOrCreateTag().getInt("bolt_action_anim") > 2) || stack.getOrCreateTag().getBoolean("canImmediatelyShoot"))) {
            revolverPreTime = Mth.clamp(revolverPreTime + 0.21 * times, 0 , 1);
            revolverWheelPreTime = Mth.clamp(revolverWheelPreTime + 0.23 * times, 0 , revolverPreTime > 0.7 ? 1 : 0.55);
        } else if (!stack.getOrCreateTag().getBoolean("DA") && !stack.getOrCreateTag().getBoolean("canImmediatelyShoot")) {
            revolverPreTime = Mth.clamp(revolverPreTime - 1.2 * times, 0 ,  1);
        }


        if ((holdFire || burstFireSize > 0)
                && (player.getMainHandItem().is(ModTags.Items.NORMAL_GUN)
                && cantFireTime == 0
                && drawTime < 0.01
                && !notInGame()
                && !player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit
                && (!(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                && !stack.getOrCreateTag().getBoolean("reloading")
                && !stack.getOrCreateTag().getBoolean("charging")
                && stack.getOrCreateTag().getInt("ammo") > 0
                && !player.getCooldowns().isOnCooldown(stack.getItem())
                && !stack.getOrCreateTag().getBoolean("need_bolt_action")
                && revolverPre()
        )
                || (stack.is(ModItems.MINIGUN.get())
                && !player.isSprinting()
                && stack.getOrCreateTag().getDouble("overheat") == 0
                && !player.getCooldowns().isOnCooldown(stack.getItem()) && miniGunRot >= 20
                && ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo > 0 || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())))
        ))) {

            if (mode == 0) {
                if (clientTimer.getProgress() == 0) {
                    clientTimer.start();
                    shootClient(player);
                }
            } else {
                if (!clientTimer.started()) {
                    clientTimer.start();
                    // 首发瞬间发射
                    clientTimer.setProgress((cooldown + 1));
                }

                if (clientTimer.getProgress() >= cooldown) {
                    shootClient(player);
                    clientTimer.setProgress((clientTimer.getProgress() - cooldown));
                }
            }


            if (notInGame()) {
                clientTimer.stop();
            }

        } else {
            if (mode != 0) {
                clientTimer.stop();
            }
            fireSpread = 0;
        }

        gunPartMove(times);

        if (mode == 0  && clientTimer.getProgress() >= cooldown) {
            clientTimer.stop();
        }

        if (stack.getItem() == ModItems.DEVOTION.get() && (stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))) {
            customRpm = 0;
        }
    }

    public static void shootClient(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {
            if (stack.getOrCreateTag().getInt("ammo") > 0) {
                int mode = GunsTool.getGunIntTag(stack, "FireMode");
                if (mode != 2) {
                    holdFire = false;
                }

                if (mode == 1) {
                    if (stack.getOrCreateTag().getInt("ammo") == 1) {
                         burstFireSize = 1;
                    }
                    if (burstFireSize == 1) {
                        cantFireTime = 40;
                    }
                    burstFireSize--;
                }

                if (stack.is(ModItems.DEVOTION.get())) {
                    int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.TURBO_CHARGER.get(), stack);
                    customRpm = Math.min(customRpm + 15 + ((perkLevel > 0 ? 5 : 0) + 3 * perkLevel), 500);
                }

                if (stack.getItem() == ModItems.SENTINEL.get()) {
                    chamberRot = 1;
                }

                if (stack.getItem() == ModItems.NTW_20.get()) {
                    actionMove = 1;
                }

                revolverPreTime = 0;
                revolverWheelPreTime = 0;

                playGunClientSounds(player);
                handleClientShoot();
            }
        } else if (stack.is(ModItems.MINIGUN.get())) {
            var tag = stack.getOrCreateTag();

            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo > 0
                    || player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))) {

                var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
                float pitch = tag.getDouble("heat") <= 40 ? 1 : (float) (1 - 0.025 * Math.abs(40 - tag.getDouble("heat")));

                player.playSound(ModSounds.MINIGUN_FIRE_1P.get(), 1f, pitch);

                if (perk == ModPerks.BEAST_BULLET.get()) {
                    player.playSound(ModSounds.HENG.get(), 1f, 1f);
                }

                double shooterHeight = player.getEyePosition().distanceTo((Vec3.atLowerCornerOf(player.level().clip( new ClipContext(player.getEyePosition(), player.getEyePosition().add(new Vec3(0, -1 , 0).scale(10)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

                ModUtils.queueClientWork((int) (1 + 1.5 * shooterHeight), () -> {
                    player.playSound(ModSounds.SHELL_CASING_NORMAL.get(), (float) Math.max(1.5 - 0.2 * shooterHeight, 0), 1);
                });
            }

            handleClientShoot();
        }
    }

    public static void gunPartMove(float times) {
        chamberRot = Mth.lerp(0.07 * times, chamberRot, 0);
        actionMove = Mth.lerp(0.2 * times, actionMove, 0);
    }

    public static void handleClientShoot() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        ModUtils.PACKET_HANDLER.sendToServer(new ShootMessage(gunSpread));
        fireRecoilTime = 10;

        float gunRecoilY = (float) tag.getDouble("recoil_y") * 10;
        recoilY = (float) (2 * Math.random() - 1) * gunRecoilY;

        if (shellIndex < 5) {
            shellIndex++;
        }

        shellIndexTime[shellIndex] = 0.001;

        randomShell[0] = (1 + 0.2 * (Math.random() - 0.5));
        randomShell[1] = (0.2 + (Math.random() - 0.5));
        randomShell[2] = (0.7 + (Math.random() - 0.5));
    }

    public static void playGunClientSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            return;
        }

        String origin = stack.getItem().getDescriptionId();
        String name = origin.substring(origin.lastIndexOf(".") + 1);

        if (stack.getItem() == ModItems.SENTINEL.get()) {
            AtomicBoolean charged = new AtomicBoolean(false);

            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                    e -> charged.set(e.getEnergyStored() > 0)
            );

            if (charged.get()) {
                SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "sentinel_charge_fire_1p"));
                if (sound1p != null) {
                    player.playSound(sound1p, 1f, 1);
                }
                return;
            }
        }

        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        if (perk == ModPerks.BEAST_BULLET.get()) {
            player.playSound(ModSounds.HENG.get(), 1f, 1f);
        }

        int barrelType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.BARREL);

        SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(ModUtils.loc(name + (barrelType == 2 ? "_fire_1p_s" : "_fire_1p")));

        if (sound1p != null) {
            player.playSound(sound1p, 1f, 1);
        }

        double shooterHeight = player.getEyePosition().distanceTo((Vec3.atLowerCornerOf(player.level().clip( new ClipContext(player.getEyePosition(), player.getEyePosition().add(new Vec3(0, -1 , 0).scale(10)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

        ModUtils.queueClientWork((int) (1 + 1.5 * shooterHeight), () -> {
            if (stack.is(ModTags.Items.HAS_SHELL_EFFECT)) {
                if (stack.is(ModTags.Items.SHOTGUN)) {
                    player.playSound(ModSounds.SHELL_CASING_SHOTGUN.get(), (float) Math.max(0.75 - 0.12 * shooterHeight, 0), 1);
                } else if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
                    player.playSound(ModSounds.SHELL_CASING_50CAL.get(), (float) Math.max(1 - 0.15 * shooterHeight, 0), 1);
                } else {
                    player.playSound(ModSounds.SHELL_CASING_NORMAL.get(), (float) Math.max(1.5 - 0.2 * shooterHeight, 0), 1);
                }

            }
        });
    }

    @SubscribeEvent
    public static void handleWeaponBreathSway(TickEvent.RenderTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        float pose;
        float times = 2 * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        if (player.isCrouching() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.85f;
        } else if (isProne(player)) {
            pose = GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.GRIP) == 3 ? 0 : 0.25f;
        } else {
            pose = 1;
        }

        int stockType = GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.STOCK);

        double sway = switch (stockType) {
            case 1 -> 1;
            case 2 -> 0.55;
            default -> 0.8;
        };

        double cusWeight = player.getMainHandItem().getOrCreateTag().getDouble("CustomWeight");


        if (!player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breath &&
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom) {
            float newPitch = (float) (player.getXRot() - 0.01f * Mth.sin((float) (0.03 * player.tickCount)) * pose * Mth.nextDouble(RandomSource.create(), 0.1, 1) * times * sway * (1 - 0.03 * cusWeight));
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();

            float newYaw = (float) (player.getYRot() - 0.005f * Mth.cos((float) (0.025 * (player.tickCount + 2 * Math.PI))) * pose * Mth.nextDouble(RandomSource.create(), 0.05, 1.25) * times * sway * (1 - 0.03 * cusWeight));
            player.setYRot(newYaw);
            player.yRotO = player.getYRot();
        }
    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        handlePlayerCamera(event);
        if (level != null && entity instanceof LivingEntity living
                && living.getMainHandItem().is(ModItems.MONITOR.get())
                && living.getMainHandItem().getOrCreateTag().getBoolean("Using")
                && living.getMainHandItem().getOrCreateTag().getBoolean("Linked")) {
            handleDroneCamera(event, living);
        } else {
            var effect = Minecraft.getInstance().gameRenderer.currentEffect();
            if (effect != null && effect.getName().equals(ModUtils.MODID + ":shaders/post/scan_pincushion.json")) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
        if (level != null && entity instanceof LivingEntity living && living.getMainHandItem().is(ModTags.Items.GUN)) {
            handleWeaponSway(living);
            handleWeaponMove(living);
            handleWeaponZoom(living);
            handlePlayerBreath(living);
            handleWeaponFire(event, living);
            handleWeaponShell();
            handleGunRecoil();
            handleShockCamera(event, living);
            handleBowPullAnimation(living);
            handleWeaponDraw(living);
        }
    }

    private static void handleDroneCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 1.6);
        double pitch = event.getPitch();
        double roll = event.getRoll();

        DroneEntity drone = entity.level().getEntitiesOfClass(DroneEntity.class, entity.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

        if (drone != null) {
            droneRotZ = Mth.lerp(0.1 * times, droneRotZ, drone.getEntityData().get(ROT_Z));

            droneRotX = Mth.lerp(0.1 * times, droneRotX, drone.getEntityData().get(ROT_X));

            event.setPitch((float) (pitch + droneCameraRotX - 0.15f * Mth.RAD_TO_DEG * droneRotZ));
            event.setRoll((float) (roll + droneCameraRotY - 0.5f * Mth.RAD_TO_DEG * droneRotX));
        }

        if (drone != null && stack.getOrCreateTag().getBoolean("Using")) {
            if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
                Minecraft.getInstance().gameRenderer.loadEffect(ModUtils.loc("shaders/post/scan_pincushion.json"));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        InteractionHand leftHand = Minecraft.getInstance().options.mainHand().get() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        InteractionHand rightHand = Minecraft.getInstance().options.mainHand().get() == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        ItemStack rightHandItem = player.getItemInHand(rightHand);

        if (event.getHand() == leftHand) {
            if (rightHandItem.is(ModTags.Items.GUN)) {
                event.setCanceled(true);
            }
        }

        if (event.getHand() == rightHand) {
            if (rightHandItem.is(ModTags.Items.GUN) && drawTime > 0.15) {
                event.setCanceled(true);
            }
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                    .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().ifPresent(drone -> event.setCanceled(true));
        }
    }

    private static void handleWeaponSway(LivingEntity entity) {
        if (entity.getMainHandItem().is(ModTags.Items.GUN) && entity instanceof Player player) {
            float times = 2 * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
            double pose;

            if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && isProne(player)) {
                pose = 0.85;
            } else if (isProne(player)) {
                pose = GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.GRIP) == 3 ? 0 : 0.25f;
            } else {
                pose = 1;
            }

            swayTime += 0.05 * times;

            swayX = pose * -0.008 * Math.sin(swayTime) * (1 - 0.95 * zoomTime);
            swayY = pose * 0.125 * Math.sin(swayTime - 1.585) * (1 - 0.95 * zoomTime) - 3 * moveRotZ;
        }
    }

    private static void handleWeaponMove(LivingEntity entity) {
        if (entity.getMainHandItem().is(ModTags.Items.GUN)) {
            float times = 3.7f * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
            double moveSpeed = (float) Mth.clamp(entity.getDeltaMovement().horizontalDistanceSqr(), 0, 0.02);
            double onGround;

            if (entity.onGround()) {
                if (entity.isSprinting()) {
                    onGround = 1.35;
                } else {
                    onGround = 2.0;
                }
            } else {
                onGround = 0.001;
            }

            if (!entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
                if (Minecraft.getInstance().options.keyUp.isDown() && firePosTimer == 0) {
                    moveRotZ = Mth.lerp(0.2f * times, moveRotZ, 0.14) * (1 - zoomTime);
                } else {
                    moveRotZ = Mth.lerp(0.2f * times, moveRotZ, 0) * (1 - zoomTime);
                }
            }

            if (isMoving() && firePosTimer == 0) {
                if (moveYTime < 1.25) {
                    moveYTime += 1.2 * onGround * times * moveSpeed;
                } else {
                    moveYTime = 0.25;
                }

                if (moveXTime < 2) {
                    moveXTime += 1.2 * onGround * times * moveSpeed;
                } else {
                    moveXTime = 0;
                }

                movePosX = 0.2 * Math.sin(1 * Math.PI * moveXTime) * (1 - 0.95 * zoomTime);
                movePosY = -0.135 * Math.sin(2 * Math.PI * (moveYTime - 0.25)) * (1 - 0.95 * zoomTime);
            } else {
                moveXTime = Math.max(moveXTime - 0.5 * times, 0);
                moveYTime = Math.max(moveYTime - 0.5 * times, 0.25);

                movePosX = Mth.lerp(0.1f * times, movePosX, 0);
                movePosY = Mth.lerp(0.1f * times, movePosY, 0);

            }

            if (Minecraft.getInstance().options.keyRight.isDown()) {
                movePosHorizon = Mth.lerp(0.05f * times, movePosHorizon, 0.04) * (1 - zoomTime);
            } else if (Minecraft.getInstance().options.keyLeft.isDown()) {
                movePosHorizon = Mth.lerp(0.05f * times, movePosHorizon, -0.04) * (1 - zoomTime);
            } else {
                movePosHorizon = Mth.lerp(0.1f * times, movePosHorizon, 0);
            }

            double velocity = entity.getDeltaMovement().y() + 0.078;

            if (-0.8 < velocity && velocity < 0.8) {
                velocityY = Mth.lerp(0.5f * times, velocityY, velocity) * (1 - 0.8 * zoomTime);
            }
        }
    }

    private static void handleWeaponZoom(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        float times = 5 * Minecraft.getInstance().getDeltaFrameTime();

        double weight = stack.getOrCreateTag().getDouble("weight") + stack.getOrCreateTag().getDouble("CustomWeight");
        double speed = 1.5 - (0.07 * weight);

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS
                && !notInGame()
                && drawTime < 0.01
                && !player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getPersistentData().putDouble("noRun", 5);
            }
            if (cantFireTime <= 10) {
                zoomTime = Mth.clamp(zoomTime + 0.03 * speed * times, 0, 1);
            }
        } else {
            zoomTime = Mth.clamp(zoomTime - 0.04 * speed * times, 0, 1);
        }
        zoomPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(zoomTime, 2) - 1, 2)) + 0.5;
        zoomPosZ = -Math.pow(2 * zoomTime - 1, 2) + 1;
    }

    private static void handleWeaponFire(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        float times = 2f * Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.48f);
        float yaw = event.getYaw();
        float pitch = event.getPitch();
        float roll = event.getRoll();
        ItemStack stack = entity.getMainHandItem();
        double amplitude = 15000 * stack.getOrCreateTag().getDouble("recoil_y") * stack.getOrCreateTag().getDouble("recoil_x");

        if (fireRecoilTime > 0) {
            firePosTimer = 0.001;
            fireRotTimer = 0.001;
            fireRecoilTime -= 7 * times;
            fireSpread += 0.1 * times;
            firePosZ += (0.8 * firePosZ + 0.25) * (4 * Math.random() + 0.85) * times;
            recoilTime = 0.01;
        }

        fireSpread = Mth.clamp(fireSpread - 0.1 * (Math.pow(fireSpread, 2) * times), 0, 2);
        firePosZ = Mth.clamp(firePosZ - 1.2 * (Math.pow(firePosZ, 2) * times), 0, 1.5);

        if (0 < firePosTimer) {
            firePosTimer += 0.35 * (1.1 - firePosTimer) * times;
        }
        if (0 < firePosTimer && firePosTimer < 0.454) {
            firePos = (-18.34) * Math.pow(firePosTimer, 2) + 8.58 * firePosTimer;
        }
        if (0.454 <= firePosTimer && firePosTimer < 1) {
            firePos = 4.34 * Math.pow(firePosTimer, 2) - 6.5 * firePosTimer + 2.167;
        }

        if (0 < fireRotTimer && fireRotTimer < 1.732) {
            fireRotTimer += 0.18 * (1.9 - fireRotTimer) * times;
        }

        double rpm = 1;

        if (entity.getMainHandItem().is(ModItems.MINIGUN.get())) {
            rpm = (double) entity.getMainHandItem().getOrCreateTag().getInt("rpm") / 1800;
        }

        float[] shake = {0, 0};
        shake[0] = (float) (1.3 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 1 * Mth.clamp(0.3 - fireRotTimer, 0, 1) * (2 * Math.random() - 1));
        shake[1] = (float) (4.2 * amplitude * (1 / 6.3 * (fireRotTimer - 0.5)) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2)) + 3 * Mth.clamp(0.5 - fireRotTimer, 0, 0.5) * (2 * Math.random() - 1));

        if (0 < fireRotTimer && fireRotTimer < 1.732) {
            fireRot = 1 / 6.3 * (fireRotTimer - 0.5) * Math.sin(6.3 * (fireRotTimer - 0.5)) * (3 - Math.pow(fireRotTimer, 2));
            if (recoilY > 0) {
                event.setYaw((float) (yaw - shake[0] * rpm));
                event.setPitch((float) (pitch + shake[0] * rpm));
                event.setRoll((float) (roll + shake[1] * rpm));
            } else if (recoilY <= 0) {
                event.setYaw((float) (yaw + shake[0] * rpm));
                event.setPitch((float) (pitch - shake[0] * rpm));
                event.setRoll((float) (roll - shake[1] * rpm));
            }
        }

        if (firePosTimer >= 1) {
            firePosTimer = 0;
        }
        if (fireRotTimer >= 1.732) {
            fireRotTimer = 0;
            fireRot = 0;
        }
    }

    private static void handleWeaponShell() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        if (shellIndex >= 5) {
            shellIndex = 0;
            shellIndexTime[0] = 0.001;
        }

        for (int i = 0; i < 5; i++) {
            if (shellIndexTime[i] > 0) {
                shellIndexTime[i] = Math.min(shellIndexTime[i] + 8 * times, 50);
            }
            if (shellIndexTime[i] == 50) {
                shellIndexTime[i] = 0;
            }
        }
    }

    private static void handleGunRecoil() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.getMainHandItem().is(ModTags.Items.GUN)) return;

        CompoundTag tag = player.getMainHandItem().getOrCreateTag();
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 1.6);
        int barrelType = GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.BARREL);
        int gripType = GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.GRIP);

        double recoil = switch (barrelType) {
            case 1 -> 1.5;
            case 2 -> 2.2;
            default -> 2.4;
        };

        double gripRecoilX = switch (gripType) {
            case 1 -> 1.25;
            case 2 -> 0.25;
            default -> 1.5;
        };

        double gripRecoilY = switch (gripType) {
            case 1 -> 0.7;
            case 2 -> 1.75;
            default -> 2.0;
        };

        if (!player.getMainHandItem().is(ModTags.Items.CAN_CUSTOM_GUN)) {
            recoil = 1.6;
            gripRecoilX = 0.75;
            gripRecoilY = 1.25;
        }

        double cusWeight = player.getMainHandItem().getOrCreateTag().getDouble("CustomWeight");

        double rpm = 1;

        if (player.getMainHandItem().is(ModItems.MINIGUN.get())) {
            rpm = (double) player.getMainHandItem().getOrCreateTag().getInt("rpm") / 1800;
        }

        float gunRecoilX = (float) tag.getDouble("recoil_x") * 60;

        recoilHorizon = Mth.lerp(0.2 * times, recoilHorizon, 0) + recoilY;

        recoilY = 0;

        // 计算后坐力
        float pose = 1;
        if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.7f;
        } else if (isProne(player)) {
            if (GunsTool.getAttachmentType(player.getMainHandItem(), GunsTool.AttachmentType.GRIP) == 3) {
                pose = 0.1f;
            } else {
                pose = 0.5f;
            }
        }

        // 水平后座

        float newYaw = player.getYRot() - (float) (0.6 * recoilHorizon * pose * times * (0.5 + fireSpread) * recoil * (1 - 0.06 * cusWeight) * gripRecoilX * rpm);
        player.setYRot(newYaw);
        player.yRotO = player.getYRot();

        double sinRes = 0;

        // 竖直后座
        if (0 < recoilTime && recoilTime < 0.5) {
            float newPitch = (float) (player.getXRot() - 0.02f * gunRecoilX * times * recoil * (1 - 0.06 * cusWeight) * gripRecoilY * rpm);
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();
        }

        if (0 < recoilTime && recoilTime < 2) {
            recoilTime = recoilTime + 0.3 * times;
            sinRes = Math.sin(Math.PI * recoilTime);
        }

        if (2 <= recoilTime && recoilTime < 2.5) {
            recoilTime = recoilTime + 0.17 * times;
            sinRes = 0.4 * Math.sin(2 * Math.PI * recoilTime);
        }

        if (0 < recoilTime && recoilTime < 2.5) {
            float newPitch = player.getXRot() - (float) (1.5 * pose * gunRecoilX * (sinRes + Mth.clamp(0.5 - recoilTime, 0, 0.5)) * times * (0.5 + fireSpread) * recoil * (1 - 0.06 * cusWeight) * gripRecoilY * rpm);
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();
        }

        if (recoilTime >= 2.5) recoilTime = 0;
    }

    private static void handlePlayerBreath(LivingEntity entity) {
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
        boolean breath = (entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).breath;

        breathTime = Mth.lerp(0.2f * times, breathTime, breath ? 1 : 0);
    }

    private static void handleShockCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        if (entity.hasEffect(ModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setYaw(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setPitch(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + (float) Mth.nextDouble(RandomSource.create(), -3, 3));
            event.setRoll((float) Mth.nextDouble(RandomSource.create(), 8, 12));
        }
    }

    public static void shake(double boneRotX, double boneRotY, double boneRotZ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            cameraRot[0] = -boneRotX;
            cameraRot[1] = -boneRotY;
            cameraRot[2] = -boneRotZ;
        }
    }

    private static void handlePlayerCamera(ViewportEvent.ComputeCameraAngles event) {
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        double roll = event.getRoll();
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
        LocalPlayer player = Minecraft.getInstance().player;

        if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            cameraLocation = Mth.clamp(cameraLocation - 0.05 * Minecraft.getInstance().getDeltaFrameTime(), -0.6, 0.6);
        }

        if (GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            cameraLocation = Mth.clamp(cameraLocation + 0.05 * Minecraft.getInstance().getDeltaFrameTime(), -0.6, 0.6);
        }

        if (player == null) return;

        double range;
        Entity lookingEntity = SeekTool.seekEntity(player, player.level(), 520, 5);

        if (lookingEntity != null) {
            range = Math.max(player.distanceTo(lookingEntity), 0.01);
        } else {
            range = Math.max(player.position().distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                    new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(520)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos()))), 0.01);
        }

        lookDistance = Mth.lerp(0.2f * times, lookDistance, range);

        double angle = 0;

        if (lookDistance != 0 && cameraLocation != 0) {
            angle = Math.atan(Mth.abs((float) cameraLocation) / (lookDistance + 2.9)) * Mth.RAD_TO_DEG;
        }

        if (player.getMainHandItem().is(ModTags.Items.GUN) || (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity))) {
            event.setPitch((float) (pitch + cameraRot[0] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.2 : 0) * turnRot[0] + 3 * velocityY));
            if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
                event.setYaw((float) (yaw + cameraRot[1] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.8 : 0) * turnRot[1] - (cameraLocation > 0 ? 1 : -1) * angle * zoomPos));
            } else {
                event.setYaw((float) (yaw + cameraRot[1] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.8 : 0) * turnRot[1]));
            }

            event.setRoll((float) (roll + cameraRot[2] + (DisplayConfig.CAMERA_ROTATE.get() ? 0.35 : 0) * turnRot[2]));
        }
    }

    private static void handleBowPullAnimation(LivingEntity entity) {
        float times = 4 * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        if ((entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).bowPull) {
            pullTimer = Math.min(pullTimer + 0.024 * times, 1.4);
            bowTimer = Math.min(bowTimer + 0.018 * times, 1);
            handTimer = Math.min(handTimer + 0.018 * times, 1);
            handPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(handTimer, 2) - 1, 2)) + 0.5;
        } else {
            pullTimer = Math.max(pullTimer - 0.015 * times, 0);
            bowTimer = Math.max(bowTimer - 1 * times, 0);
            handTimer = Math.max(handTimer - 0.04 * times, 0);
            if (handTimer > 0 && handTimer < 0.5) {
                handPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(handTimer, 2) - 1, 2)) + 0.5;
            }
        }
        pullPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(Mth.clamp(pullTimer, 0, 1), 2) - 1, 2)) + 0.5;
        bowPos = 0.5 * Math.cos(Math.PI * Math.pow(Math.pow(bowTimer, 2) - 1, 2)) + 0.5;
    }

    @SubscribeEvent
    public static void onFovUpdate(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 1.6);
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.GUN)) {
            if (!event.usedConfiguredFov()) {
                return;
            }

            double p;
            if (stack.is(ModItems.BOCEK.get())) {
                p = (pullPos + 0.25) * zoomTime;
            } else {
                p = zoomPos;
            }

            customZoom = Mth.lerp(0.6 * times, customZoom, stack.getOrCreateTag().getDouble("CustomZoom"));

            double zoom = 1.25 + customZoom;

            if (mc.options.getCameraType().isFirstPerson()) {
                event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)) * (1 - 0.4 * breathTime));
            } else if (mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK)
                event.setFOV(event.getFOV() / (1.0 + p * 0.01) * (1 - 0.4 * breathTime));
            fov = event.getFOV();
            return;
        }
        if (player.isPassenger() && player.getVehicle() instanceof ICannonEntity && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !stack.is(ModTags.Items.GUN)) {
            event.setFOV(event.getFOV() / 5);
        }
    }

    @SubscribeEvent
    public static void handleRenderCrossHair(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (mc.player.getMainHandItem().is(ModTags.Items.GUN) || (mc.player.getVehicle() != null && mc.player.getVehicle() instanceof ICannonEntity)) {
            event.setCanceled(true);
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            event.setCanceled(true);
        }
    }

    public static void handleDrawMessage(boolean draw, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            drawTime = 1;
            for (int i = 0; i < 5; i++) {
                shellIndexTime[i] = 0;
            }
        }
    }

    private static void handleWeaponDraw(LivingEntity entity) {
        float times = Minecraft.getInstance().getDeltaFrameTime();
        ItemStack stack = entity.getMainHandItem();
        double weight = stack.getOrCreateTag().getDouble("weight") + stack.getOrCreateTag().getDouble("CustomWeight");
        double speed = 3.2 - (0.13 * weight);
        drawTime = Math.max(drawTime - Math.max(0.2 * speed * times * drawTime, 0.0008), 0);
    }

    public static void handleShells(float x, float y, CoreGeoBone... shells) {
        for (int i = 0; i < shells.length; i++) {
            if (i >= 5) break;

            shells[i].setPosX((float) (-x * shellIndexTime[i] * ((150 - shellIndexTime[i]) / 150)));
            shells[i].setPosY((float) (y * randomShell[0] * shellIndexTime[i] - 0.025 * Math.pow(shellIndexTime[i], 2)));
            shells[i].setRotX((float) (randomShell[1] * shellIndexTime[i]));
            shells[i].setRotY((float) (randomShell[2] * shellIndexTime[i]));
        }
    }
}
