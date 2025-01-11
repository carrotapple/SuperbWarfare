package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.ClickHandler;
import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.*;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity.WEAPON_TYPE;
import static com.atsuishio.superbwarfare.entity.vehicle.SpeedboatEntity.HEAT;

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

    public static double droneRotZ = 0;
    public static double droneFov = 1;
    public static double droneFovLerp = 1;

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
    public static MillisTimer clientTimerVehicle = new MillisTimer();

    public static boolean holdFire = false;

    public static boolean zoom = false;
    public static int burstFireSize = 0;

    public static int customRpm = 0;

    public static double chamberRot = 0;
    public static double actionMove = 0;

    public static int miniGunRot = 0;

    public static double revolverPreTime = 0;
    public static double revolverWheelPreTime = 0;

    public static double shakeTime = 0;
    public static double shakeRadius = 0;
    public static double shakeAmplitude = 0;
    public static double[] shakePos = {0, 0, 0};
    public static double shakeType = 0;
    public static double vehicleFov = 1;
    public static double vehicleFovLerp = 1;
    public static int lungeAttack;
    public static int lungeDraw;
    public static int lungeSprint;
    public static Entity entity;

    public static int dismountCountdown = 0;
    public static int aimVillagerCountdown = 0;

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
        } else if (stack.is(ModTags.Items.REVOLVER) && (stack.getOrCreateTag().getBoolean("DA") || stack.getOrCreateTag().getBoolean("canImmediatelyShoot"))) {
            return true;
        } else {
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
        if (stack.is(ModItems.MINIGUN.get())) {
            if (holdFire || zoom) {
                miniGunRot = Math.min(miniGunRot + 5, 21);
                float rpm = (float) GunsTool.getGunIntTag(stack, "RPM", 0) / 3600;
                player.playSound(ModSounds.MINIGUN_ROT.get(), 1, 0.7f + rpm);
            }
        }

        if (notInGame() && !ClickHandler.switchZoom) {
            zoom = false;
        }

        isProne(player);
        beamShoot(player, stack);
        handleLungeAttack(player, stack);

        if (event.phase == TickEvent.Phase.END) {
            handleVariableDecrease();
            aimAtVillager(player);
        }
    }

    private static void handleVariableDecrease() {
        if (miniGunRot > 0) {
            miniGunRot--;
        }

        if (dismountCountdown > 0) {
            dismountCountdown--;
        }

        if (aimVillagerCountdown > 0) {
            aimVillagerCountdown--;
        }
    }

    public static boolean isProne(Player player) {
        Level level = player.level();
        if (player.getBbHeight() <= 1) return true;

        return player.isCrouching() && level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 0.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()
                && !level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 1.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude();
    }

    public static void handleLungeAttack(Player player, ItemStack stack) {
        if (stack.is(ModItems.LUNGE_MINE.get()) && lungeAttack == 0 && lungeDraw == 0 && holdFire) {
            lungeAttack = 36;
            holdFire = false;
            player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1);
        }

        if (stack.is(ModItems.LUNGE_MINE.get()) && ((lungeAttack >= 18 && lungeAttack <= 21) || lungeSprint > 0)) {

            boolean lookAtEntity = false;

            Entity lookingEntity = TraceTool.findLookingEntity(player, 3.5);

            BlockHitResult result = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(3.5)),
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

            Vec3 looking = Vec3.atLowerCornerOf(player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(3.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos());
            BlockState blockState = player.level().getBlockState(BlockPos.containing(looking.x(), looking.y(), looking.z()));

            if (lookingEntity != null) {
                lookAtEntity = true;
            }

            if (lookAtEntity) {
                ModUtils.PACKET_HANDLER.sendToServer(new LungeMineAttackMessage(0, lookingEntity.getUUID(), result));
                lungeSprint = 0;
                lungeAttack = 0;
                lungeDraw = 30;
            } else if (blockState.canOcclude() || blockState.getBlock() instanceof DoorBlock || blockState.getBlock() instanceof CrossCollisionBlock || blockState.getBlock() instanceof BellBlock) {
                ModUtils.PACKET_HANDLER.sendToServer(new LungeMineAttackMessage(1, player.getUUID(), result));
                lungeSprint = 0;
                lungeAttack = 0;
                lungeDraw = 30;
            }
        }

        if (lungeSprint > 0) {
            lungeSprint--;
        }

        if (lungeAttack > 0) {
            lungeAttack--;
        }

        if (lungeDraw > 0) {
            lungeDraw--;
        }
    }

    @SubscribeEvent
    public static void handleWeaponFire(TickEvent.RenderTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (player == null) return;
        if (level == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) {
            clientTimer.stop();
            fireSpread = 0;
            gunSpread = 0;
            return;
        }

        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        int mode = GunsTool.getGunIntTag(stack, "FireMode");

        // 精准度
        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        double basicDev = GunsTool.getGunDoubleTag(stack, "Spread");
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

        gunSpread = Mth.lerp(0.14 * times, gunSpread, spread);

        // 开火部分
        double weight = GunsTool.getGunDoubleTag(stack, "Weight") + GunsTool.getGunDoubleTag(stack, "CustomWeight");
        double speed = 1 - (0.04 * weight);

        if (player.getPersistentData().getDouble("noRun") == 0 && player.isSprinting() && !zoom) {
            cantFireTime = Mth.clamp(cantFireTime + 3 * times, 0, 24);
        } else {
            cantFireTime = Mth.clamp(cantFireTime - 6 * speed * times, 0, 40);
        }

        int rpm = GunsTool.getGunIntTag(stack, "RPM", 0) + customRpm;
        if (rpm == 0) {
            rpm = 600;
        }

        if (GunsTool.getPerkIntTag(stack, "DesperadoTimePost") > 0) {
            int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.DESPERADO.get(), stack);
            rpm *= (int) (1.285 + 0.015 * perkLevel);
        }

        double rps = (double) rpm / 60;

        // cooldown in ms
        int cooldown = (int) (1000 / rps);

        //左轮类
        if (clientTimer.getProgress() == 0 && stack.is(ModTags.Items.REVOLVER) && ((holdFire && !stack.getOrCreateTag().getBoolean("DA"))
                || (GunsTool.getGunIntTag(stack, "BoltActionTick") < 7 && GunsTool.getGunIntTag(stack, "BoltActionTick") > 2) || stack.getOrCreateTag().getBoolean("canImmediatelyShoot"))) {
            revolverPreTime = Mth.clamp(revolverPreTime + 0.3 * times, 0, 1);
            revolverWheelPreTime = Mth.clamp(revolverWheelPreTime + 0.32 * times, 0, revolverPreTime > 0.7 ? 1 : 0.55);
        } else if (!stack.getOrCreateTag().getBoolean("DA") && !stack.getOrCreateTag().getBoolean("canImmediatelyShoot")) {
            revolverPreTime = Mth.clamp(revolverPreTime - 1.2 * times, 0, 1);
        }

        if ((holdFire || burstFireSize > 0)
                && (stack.is(ModTags.Items.NORMAL_GUN)
                && cantFireTime == 0
                && drawTime < 0.01
                && !notInGame()
                && !player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit
                && (!(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                && !GunsTool.getGunBooleanTag(stack, "Reloading")
                && !GunsTool.getGunBooleanTag(stack, "Charging")
                && GunsTool.getGunIntTag(stack, "Ammo", 0) > 0
                && !player.getCooldowns().isOnCooldown(stack.getItem())
                && !GunsTool.getGunBooleanTag(stack, "NeedBoltAction", false)
                && revolverPre())
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

        if (mode == 0 && clientTimer.getProgress() >= cooldown) {
            clientTimer.stop();
        }

        if (stack.getItem() == ModItems.DEVOTION.get() && (stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))) {
            customRpm = 0;
        }
    }

    public static void beamShoot(Player player, ItemStack stack) {
        if (stack.is(ModItems.BEAM_TEST.get()) && player.getUseItem() == stack) {
            Entity lookingEntity = TraceTool.laserfindLookingEntity(player, 512);

            if (player.isCrouching()) {
                Entity seekingEntity = SeekTool.seekLivingEntity(player, player.level(), 64, 32);
                if (seekingEntity != null && seekingEntity.isAlive()) {
                    player.lookAt(EntityAnchorArgument.Anchor.EYES, seekingEntity.getEyePosition());
                }
            }

            if (lookingEntity == null) {
                return;
            }

            boolean canAttack = lookingEntity != player && !(lookingEntity instanceof Player player_ && (player_.isCreative() || player_.isSpectator()))
                    && (!player.isAlliedTo(lookingEntity) || lookingEntity.getTeam() == null || lookingEntity.getTeam().getName().equals("TDM"));

            if (canAttack) {
                ModUtils.PACKET_HANDLER.sendToServer(new LaserShootMessage(1, lookingEntity.getUUID(), TraceTool.laserHeadshot));
            }
        }
    }

    public static void shootClient(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModTags.Items.NORMAL_GUN)) {
            if (GunsTool.getGunIntTag(stack, "Ammo", 0) > 0) {
                int mode = GunsTool.getGunIntTag(stack, "FireMode");
                if (mode != 2) {
                    holdFire = false;
                }

                if (mode == 1) {
                    if (GunsTool.getGunIntTag(stack, "Ammo", 0) == 1) {
                        burstFireSize = 1;
                    }
                    if (burstFireSize == 1) {
                        cantFireTime = 40;
                    }
                }

                if (burstFireSize > 0) {
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

                // 判断是否为栓动武器（BoltActionTime > 0），并在开火后给一个需要上膛的状态
                if (GunsTool.getGunIntTag(stack, "BoltActionTime", 0) > 0 && GunsTool.getGunIntTag(stack, "Ammo", 0) > (stack.is(ModTags.Items.REVOLVER) ? 0 : 1)) {
                    GunsTool.setGunBooleanTag(stack, "NeedBoltAction", true);
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

                double shooterHeight = player.getEyePosition().distanceTo((Vec3.atLowerCornerOf(player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(new Vec3(0, -1, 0).scale(10)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

                ModUtils.queueClientWork((int) (1 + 1.5 * shooterHeight), () -> player.playSound(ModSounds.SHELL_CASING_NORMAL.get(), (float) Math.max(1.5 - 0.2 * shooterHeight, 0), 1));
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
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        ModUtils.PACKET_HANDLER.sendToServer(new ShootMessage(gunSpread));
        fireRecoilTime = 10;

        float gunRecoilY = (float) GunsTool.getGunDoubleTag(stack, "RecoilY", 0) * 10;

        recoilY = (float) (2 * Math.random() - 1) * gunRecoilY;

        if (shellIndex < 5) {
            shellIndex++;
        }

        shellIndexTime[shellIndex] = 0.001;

        randomShell[0] = (1 + 0.2 * (Math.random() - 0.5));
        randomShell[1] = (0.2 + (Math.random() - 0.5));
        randomShell[2] = (0.7 + (Math.random() - 0.5));
    }

    public static void handleShakeClient(double time, double radius, double amplitude, double x, double y, double z, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            shakeTime = time;
            shakeRadius = radius;
            shakeAmplitude = amplitude * Mth.DEG_TO_RAD;
            shakePos[0] = x;
            shakePos[1] = y;
            shakePos[2] = z;
            shakeType = 2 * (Math.random() - 0.5);
        }
    }

    public static void playGunClientSounds(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem gunItem)) {
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

        double shooterHeight = player.getEyePosition().distanceTo((Vec3.atLowerCornerOf(player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(new Vec3(0, -1, 0).scale(10)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

        ModUtils.queueClientWork((int) (1 + 1.5 * shooterHeight), () -> {
            if (gunItem.ejectShell(stack)) {
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
    public static void handleVehicleFire(TickEvent.RenderTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (level == null) return;

        if (player.getMainHandItem().getItem() instanceof GunItem) {
            clientTimerVehicle.stop();
            return;
        }

        if (notInGame()) {
            clientTimerVehicle.stop();
            holdFire = false;
        }

        if (player.getVehicle() instanceof IArmedVehicleEntity iVehicle && iVehicle.isDriver(player) && iVehicle.canShoot(player)) {
            int rpm = iVehicle.mainGunRpm();
            if (rpm == 0) {
                rpm = 240;
            }

            double rps = (double) rpm / 60;

            // cooldown in ms
            int cooldown = (int) (1000 / rps);

            if ((holdFire)) {
                if (!clientTimerVehicle.started()) {
                    clientTimerVehicle.start();
                    // 首发瞬间发射
                    clientTimerVehicle.setProgress((cooldown + 1));
                }
                if (clientTimerVehicle.getProgress() >= cooldown) {
                    ModUtils.PACKET_HANDLER.sendToServer(new VehicleFireMessage(0));
                    playVehicleClientSounds(player, iVehicle);
                    clientTimerVehicle.setProgress((clientTimerVehicle.getProgress() - cooldown));
                }
            } else {
                clientTimerVehicle.stop();
            }
        } else {
            clientTimerVehicle.stop();
        }
    }

    public static void playVehicleClientSounds(Player player, IArmedVehicleEntity iVehicle) {
        if (iVehicle instanceof SpeedboatEntity speedboat) {
            float pitch = speedboat.getEntityData().get(HEAT) <= 60 ? 1 : (float) (1 - 0.011 * java.lang.Math.abs(60 - speedboat.getEntityData().get(HEAT)));
            player.playSound(ModSounds.M_2_FIRE_1P.get(), 1f, pitch);
            player.playSound(ModSounds.SHELL_CASING_50CAL.get(), 0.3f, 1);
        }
        if (iVehicle instanceof Ah6Entity ah6Entity) {
            if (ah6Entity.getEntityData().get(WEAPON_TYPE) == 0) {
                player.playSound(ModSounds.HELICOPTER_CANNON_FIRE_1P.get(), 1f, 1);
            } else if (ah6Entity.getEntityData().get(WEAPON_TYPE) == 1) {
                player.playSound(ModSounds.HELICOPTER_ROCKET_FIRE_1P.get(), 1f, 1);
            }

        }
    }

    @SubscribeEvent
    public static void handleWeaponBreathSway(TickEvent.RenderTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;
        if (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity) return;

        float pose;
        float times = 2 * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);

        if (player.isCrouching() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.85f;
        } else if (isProne(player)) {
            pose = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.GRIP) == 3 ? 0 : 0.25f;
        } else {
            pose = 1;
        }

        int stockType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.STOCK);

        double sway = switch (stockType) {
            case 1 -> 1;
            case 2 -> 0.55;
            default -> 0.8;
        };

        double customWeight = GunsTool.getGunDoubleTag(stack, "CustomWeight");

        if (!player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).breath &&
                player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom) {
            float newPitch = (float) (player.getXRot() - 0.01f * Mth.sin((float) (0.03 * player.tickCount)) * pose * Mth.nextDouble(RandomSource.create(), 0.1, 1) * times * sway * (1 - 0.03 * customWeight));
            player.setXRot(newPitch);
            player.xRotO = player.getXRot();

            float newYaw = (float) (player.getYRot() - 0.005f * Mth.cos((float) (0.025 * (player.tickCount + 2 * Math.PI))) * pose * Mth.nextDouble(RandomSource.create(), 0.05, 1.25) * times * sway * (1 - 0.03 * customWeight));
            player.setYRot(newYaw);
            player.yRotO = player.getYRot();
        }
    }

    @SubscribeEvent
    public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = event.getCamera().getEntity();
        handlePlayerCamera(event);

        if (!(entity instanceof LivingEntity living)) return;
        ItemStack stack = living.getMainHandItem();

        if (level != null &&
                (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked"))) {
            handleDroneCamera(event, living);
        } else {
            var effect = Minecraft.getInstance().gameRenderer.currentEffect();
            if (effect != null && effect.getName().equals(ModUtils.MODID + ":shaders/post/scan_pincushion.json")) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }

        if (level != null && stack.is(ModTags.Items.GUN)) {
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

        float times = Minecraft.getInstance().getDeltaFrameTime();
        LocalPlayer player = Minecraft.getInstance().player;

        float yaw = event.getYaw();
        float pitch = event.getPitch();
        float roll = event.getRoll();

        shakeTime = Mth.lerp(0.25 * times, shakeTime, 0);

        if (player != null && shakeTime > 0) {
            float shakeRadiusAmplitude = (float) Mth.clamp(1 - player.position().distanceTo(new Vec3(shakePos[0], shakePos[1], shakePos[2])) / shakeRadius, 0, 1);

            if (shakeType > 0) {
                event.setYaw((float) (yaw + (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude * shakeType)));
                event.setPitch((float) (pitch - (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude * shakeType)));
                event.setRoll((float) (roll - (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude)));
            } else {
                event.setYaw((float) (yaw - (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude * shakeType)));
                event.setPitch((float) (pitch + (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude * shakeType)));
                event.setRoll((float) (roll + (shakeTime * Math.sin(0.5 * Math.PI * shakeTime) * shakeAmplitude * shakeRadiusAmplitude)));
            }
        }
    }

    public static void droneBodyAngle(float RotZ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            droneRotZ = RotZ;

        }
    }

    private static void handleDroneCamera(ViewportEvent.ComputeCameraAngles event, LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        double roll = event.getRoll();

        DroneEntity drone = EntityFindUtil.findDrone(entity.level(), stack.getOrCreateTag().getString("LinkedDrone"));

        if (drone != null) {
            event.setRoll((float) (roll - Mth.RAD_TO_DEG * droneRotZ));
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
            if (EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone")) != null) {
                event.setCanceled(true);
            }
        }

        if (player.getVehicle() instanceof ICannonEntity) {
            event.setCanceled(true);
        }

        if (player.getVehicle() instanceof Ah6Entity ah6Entity && ah6Entity.getFirstPassenger() == player && !stack.getItem().isEdible()) {
            event.setCanceled(true);
        }

        if ((player.getVehicle() instanceof SpeedboatEntity boat && boat.getFirstPassenger() == player) && ClientEventHandler.zoom && stack.is(ItemStack.EMPTY.getItem())) {
            event.setCanceled(true);
        }
    }

    private static void handleWeaponSway(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        if (stack.is(ModTags.Items.GUN) && entity instanceof Player player) {
            float times = 2 * (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 0.8);
            double pose;

            if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && isProne(player)) {
                pose = 0.85;
            } else if (isProne(player)) {
                pose = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.GRIP) == 3 ? 0 : 0.25f;
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

            velocityY = Mth.clamp(Mth.lerp(0.23f * times, velocityY, velocity) * (1 - 0.8 * zoomTime), -0.8, 0.8);
        }
    }

    private static void handleWeaponZoom(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        float times = 5 * Minecraft.getInstance().getDeltaFrameTime();

        double weight = GunsTool.getGunDoubleTag(stack, "Weight") + GunsTool.getGunDoubleTag(stack, "CustomWeight");
        double speed = 1.5 - (0.07 * weight);

        if (zoom && !notInGame()
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
        double amplitude = 15000 * GunsTool.getGunDoubleTag(stack, "RecoilY", 0) * GunsTool.getGunDoubleTag(stack, "RecoilX", 0);

        if (fireRecoilTime > 0) {
            firePosTimer = 0.001;
            fireRotTimer = 0.001;
            fireRecoilTime -= 7 * times;
            fireSpread += 0.1 * times;
            firePosZ += (0.8 * firePosZ + 0.25) * (4 * Math.random() + 0.85) * times;
            recoilTime = 0.01;
        }

        fireSpread = Mth.clamp(fireSpread - 0.1 * (Math.pow(fireSpread, 2) * times), 0, 2);
        firePosZ = Mth.clamp(firePosZ - 1.2 * (Math.pow(firePosZ, 2) * times), 0, 1.5) * 0.95;

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

        if (stack.is(ModItems.MINIGUN.get())) {
            rpm = (double) GunsTool.getGunIntTag(stack, "RPM", 0) / 1800;
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
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem gunItem)) return;

        float times = (float) Math.min(Minecraft.getInstance().getDeltaFrameTime(), 1.6);
        int barrelType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.BARREL);
        int gripType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.GRIP);

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

        if (!gunItem.canCustom(stack)) {
            recoil = 1.6;
            gripRecoilX = 0.75;
            gripRecoilY = 1.25;
        }

        double customWeight = GunsTool.getGunDoubleTag(stack, "CustomWeight");

        double rpm = 1;

        if (stack.is(ModItems.MINIGUN.get())) {
            rpm = (double) GunsTool.getGunIntTag(stack, "RPM", 0) / 1800;
        }

        float gunRecoilX = (float) GunsTool.getGunDoubleTag(stack, "RecoilX", 0) * 60;

        recoilHorizon = Mth.lerp(0.2 * times, recoilHorizon, 0) + recoilY;
        recoilY = 0;

        // 计算后坐力
        float pose = 1;
        if (player.isShiftKeyDown() && player.getBbHeight() >= 1 && !isProne(player)) {
            pose = 0.7f;
        } else if (isProne(player)) {
            if (GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.GRIP) == 3) {
                pose = 0.1f;
            } else {
                pose = 0.5f;
            }
        }

        // 水平后座
        float newYaw = player.getYRot() - (float) (0.6 * recoilHorizon * pose * times * (0.5 + fireSpread) * recoil * (1 - 0.06 * customWeight) * gripRecoilX * rpm);
        player.setYRot(newYaw);
        player.yRotO = player.getYRot();

        double sinRes = 0;

        // 竖直后座
        if (0 < recoilTime && recoilTime < 0.5) {
            float newPitch = (float) (player.getXRot() - 0.02f * gunRecoilX * times * recoil * (1 - 0.06 * customWeight) * gripRecoilY * rpm);
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
            float newPitch = player.getXRot() - (float) (1.5 * pose * gunRecoilX * (sinRes + Mth.clamp(0.5 - recoilTime, 0, 0.5)) * times * (0.5 + fireSpread) * recoil * (1 - 0.06 * customWeight) * gripRecoilY * rpm);
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

        if (player.getMainHandItem().is(ModTags.Items.GUN) || player.getMainHandItem().is(ModItems.LUNGE_MINE.get())) {
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

        if (player.isPassenger() && player.getVehicle() instanceof ICannonEntity && zoom && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setFOV(event.getFOV() / 5);
            return;
        }

        if (player.getVehicle() instanceof Ah6Entity ah6Entity && ah6Entity.getFirstPassenger() == player && zoom && mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            event.setFOV(event.getFOV() / 3);
            return;
        }

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

            customZoom = Mth.lerp(0.6 * times, customZoom, GunsTool.getGunDoubleTag(stack, "CustomZoom", 0));

            double zoomFov = 1.25 + customZoom;

            if (mc.options.getCameraType().isFirstPerson()) {
                event.setFOV(event.getFOV() / (1.0 + p * (zoomFov - 1)) * (1 - 0.4 * breathTime));
            } else if (mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK)
                event.setFOV(event.getFOV() / (1.0 + p * 0.01) * (1 - 0.4 * breathTime));
            fov = event.getFOV();

            if (zoom && !notInGame()
                    && drawTime < 0.01
                    && !player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
                int level = PerkHelper.getItemPerkLevel(ModPerks.INTELLIGENT_CHIP.get(), stack);
                if (level > 0) {
                    if (ClientEventHandler.entity == null || !entity.isAlive()) {
                        ClientEventHandler.entity = SeekTool.seekLivingEntity(player, player.level(), 32 + 8 * (level - 1), 16 / zoomFov);
                    }
                    if (entity != null && entity.isAlive()) {
                        Vec3 targetVec = player.getEyePosition().vectorTo(entity.getEyePosition());
                        Vec3 toVec = player.getViewVector(1).add(targetVec.scale(1.1f * times));
                        look(player, toVec);
                    }
                }
            }
            return;
        }

        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            droneFovLerp = Mth.lerp(0.1 * Minecraft.getInstance().getDeltaFrameTime(), droneFovLerp, droneFov);
            event.setFOV(event.getFOV() / droneFovLerp);
            fov = event.getFOV();
        }

        if (player.getVehicle() instanceof IArmedVehicleEntity && !(player.getVehicle() instanceof ICannonEntity) && zoom) {
            vehicleFovLerp = Mth.lerp(0.1 * Minecraft.getInstance().getDeltaFrameTime(), vehicleFovLerp, vehicleFov);
            event.setFOV(event.getFOV() / vehicleFovLerp);
        }
    }

    public static void look(Player player, Vec3 pTarget) {
        double d0 = pTarget.x;
        double d1 = pTarget.y;
        double d2 = pTarget.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        player.setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 57.2957763671875))));
        player.setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F));
        player.setYHeadRot(player.getYRot());
        player.xRotO = player.getXRot();
        player.yRotO = player.getYRot();
    }

    @SubscribeEvent
    public static void setPlayerInvisible(RenderPlayerEvent.Pre event) {
        var otherPlayer = event.getEntity();

        if (otherPlayer.getVehicle() instanceof ICannonEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void handleRenderCrossHair(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (player.getVehicle() instanceof IHelicopterEntity iHelicopterEntity && iHelicopterEntity.isDriver(player)) {
            event.setCanceled(true);
        }

        if (player.getVehicle() instanceof SpeedboatEntity && zoom) {
            event.setCanceled(true);
        }

        if (stack.is(ModTags.Items.GUN) || (player.getVehicle() != null && player.getVehicle() instanceof ICannonEntity)) {
            event.setCanceled(true);
        }

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
            zoom = false;
            holdFire = false;
            ClickHandler.switchZoom = false;
        }
    }

    private static void handleWeaponDraw(LivingEntity entity) {
        float times = Minecraft.getInstance().getDeltaFrameTime();
        ItemStack stack = entity.getMainHandItem();
        double weight = GunsTool.getGunDoubleTag(stack, "Weight") + GunsTool.getGunDoubleTag(stack, "CustomWeight");
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

    public static void aimAtVillager(Player player) {
        if (aimVillagerCountdown > 0) return;

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom) {
            Entity entity = TraceTool.findLookingEntity(player, 10);
            if (entity instanceof AbstractVillager villager) {
                List<Entity> entities = SeekTool.seekLivingEntities(villager, villager.level(), 16, 120);
                for (var e : entities) {
                    if (e == player) {
                        ModUtils.PACKET_HANDLER.sendToServer(new AimVillagerMessage(villager.getId()));
                        aimVillagerCountdown = 80;
                    }
                }
            }
        }
    }
}
