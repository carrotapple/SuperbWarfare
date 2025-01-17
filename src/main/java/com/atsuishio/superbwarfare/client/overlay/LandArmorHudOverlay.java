package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.entity.vehicle.MultiWeaponVehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

import java.text.DecimalFormat;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;
import static com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay.*;
import static com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity.COAX_HEAT;
import static com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity.HEAT;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class LandArmorHudOverlay {

    private static float scopeScale = 1;
    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/land/tv_frame.png");

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        if (player == null) return;
        if (player.getVehicle() instanceof Lav150Entity lav150 && lav150.isDriver(player) && player.getVehicle() instanceof MultiWeaponVehicleEntity multiWeaponVehicle) {
            poseStack.pushPose();

            poseStack.translate(-8 * ClientEventHandler.turnRot[1], -8 * ClientEventHandler.turnRot[0], 0);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            scopeScale = Mth.lerp(event.getPartialTick(), scopeScale, 1F);
            float f = (float) Math.min(w, h);
            float f1 = Math.min((float) w / f, (float) h / f) * scopeScale;
            float i = Mth.floor(f * f1);
            float j = Mth.floor(f * f1);
            float k = ((w - i) / 2);
            float l = ((h - j) / 2);

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle) {
                int addW = (w / h) * 48;
                int addH = (w / h) * 27;
                preciseBlit(guiGraphics, FRAME, (float) -addW / 2, (float) -addH / 2, 10, 0, 0.0F, w + addW, h + addH, w + addW, h + addH);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/lav_cross.png"), k, l, 0, 0.0F, i, j, i, j);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/line.png"), w / 2 - 64, h - 56, 0, 0.0F, 128, 1, 128, 1);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/line.png"), w / 2 + 112, h - 71, 0, 0.0F, 1, 16, 1, 16);

                //指南针

                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/compass.png"), (float) w / 2 - 128, (float) 10, 128 + ((float) 64 / 45 * player.getYRot()), 0, 256, 16, 512, 16);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/roll_ind.png"), w / 2 - 8, 30, 0, 0.0F, 16, 16, 16, 16);

                //炮塔方向

                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(Mth.lerp(event.getPartialTick(), lav150.turretYRotO, lav150.getTurretYRot())),w / 2 + 112, h - 56, 0);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/body.png"), w / 2 + 96, h - 72, 0, 0.0F, 32, 32, 32, 32);
                poseStack.popPose();

                //时速

                guiGraphics.drawString(mc.font, Component.literal(new DecimalFormat("##").format(lav150.getDeltaMovement().length() * 72) + " KM/H"),
                        w / 2 + 160, h / 2 - 48, 0x66FF00, false);

                //低电量警告

                if (lav150.getEnergy() < 0.02 * lav150.getMaxEnergy()) {
                    guiGraphics.drawString(mc.font, Component.literal("NO POWER!"),
                            w / 2 - 144, h / 2 + 14, -65536, false);
                } else if (lav150.getEnergy() < 0.2 * lav150.getMaxEnergy()) {
                    guiGraphics.drawString(mc.font, Component.literal("LOW POWER"),
                            w / 2 - 144, h / 2 + 14, 0xFF6B00, false);
                }

                //测距

                boolean lookAtEntity = false;
                double blockRange = cameraPos.distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                        new ClipContext(player.getEyePosition(), player.getEyePosition().add(lav150.getBarrelVector(1).scale(520)),
                                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

                double entityRange = 0;

                Entity lookingEntity = SeekTool.seekLivingEntity(player, player.level(), 512, 1);
                if (lookingEntity != null) {
                    lookAtEntity = true;
                    entityRange = player.distanceTo(lookingEntity);
                }

                if (lookAtEntity) {
                    guiGraphics.drawString(mc.font, Component.literal(new DecimalFormat("##").format(entityRange)),
                            w / 2 - 6, h - 53, 0x66FF00, false);
                } else {
                    if (blockRange > 512) {
                        guiGraphics.drawString(mc.font, Component.literal("---"), w / 2 - 6, h - 53, 0x66FF00, false);
                    } else {
                        guiGraphics.drawString(mc.font, Component.literal(new DecimalFormat("##").format(blockRange)),
                                w / 2 - 6,  h - 53, 0x66FF00, false);
                    }
                }

                //武器名称

                if (player.getVehicle() instanceof Lav150Entity lav) {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        double heat = 1 - lav.getEntityData().get(HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal("20MM CANNON " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    } else {
                        double heat = 1 - lav.getEntityData().get(COAX_HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal("7.62MM COAX " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    }

                }

                //血量

                double heal = lav150.getHealth() / lav150.getMaxHealth();
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(100 * heal)), w / 2 - 165, h / 2 - 46, Mth.hsvToRgb((float) heal / 3.745318352059925F, 1.0F, 1.0F), false);

                renderKillIndicator(guiGraphics, w, h);

            } else if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK && !ClientEventHandler.zoomVehicle) {
                Vec3 p = RenderHelper.worldToScreen(new Vec3(player.getX(), player.getY(), player.getZ()).add(lav150.getBarrelVector(event.getPartialTick()).scale(192)), cameraPos);

                if (p != null) {
                    poseStack.pushPose();
                    float x = (float) p.x;
                    float y = (float) p.y;

                    poseStack.pushPose();
                    preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone.png"), x - 12, y - 12, 0, 0, 24, 24, 24, 24);
                    renderKillIndicator3P(guiGraphics, x - 7.5f + (float) (2 * (Math.random() - 0.5f)), y - 7.5f + (float) (2 * (Math.random() - 0.5f)));

                    poseStack.pushPose();

                    poseStack.translate(x, y, 0);
                    poseStack.scale(0.75f, 0.75f, 1);

                    if (multiWeaponVehicle instanceof Lav150Entity lav1501) {
                        if (multiWeaponVehicle.getWeaponType() == 0) {
                            double heat = lav1501.getEntityData().get(HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("20MM CANNON " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav1501.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat, 1.0F), false);
                        } else {
                            double heat2 = lav1501.getEntityData().get(COAX_HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("7.62MM COAX " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav1501.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat2, 1.0F), false);
                        }
                    }

                    double heal = 1 - lav150.getHealth() / lav150.getMaxHealth();

                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("HP " + new DecimalFormat("##").format(100 * lav150.getHealth() / lav150.getMaxHealth())), 30, 1, Mth.hsvToRgb(0F, (float) heal, 1.0F), false);
                    poseStack.popPose();
                    poseStack.popPose();


                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        } else {
            scopeScale = 0.7f;
        }
    }

    private static void renderKillIndicator(GuiGraphics guiGraphics, float w, float h) {
        float posX = w / 2f - 7.5f + (float) (2 * (java.lang.Math.random() - 0.5f));
        float posY = h / 2f - 7.5f + (float) (2 * (java.lang.Math.random() - 0.5f));
        float rate = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/headshot_mark.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            float posX1 = w / 2f - 7.5f - 2 + rate;
            float posY1 = h / 2f - 7.5f - 2 + rate;
            float posX2 = w / 2f - 7.5f + 2 - rate;
            float posY2 = h / 2f - 7.5f + 2 - rate;

            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark1.png"), posX1, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark2.png"), posX2, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark3.png"), posX1, posY2, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark4.png"), posX2, posY2, 0, 0, 16, 16, 16, 16);
        }
    }

    private static void renderKillIndicator3P(GuiGraphics guiGraphics, float posX, float posY) {
        float rate = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/headshot_mark.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            float posX1 = posX - 2 + rate;
            float posY1 = posY - 2 + rate;
            float posX2 = posX + 2 - rate;
            float posY2 = posY + 2 - rate;

            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark1.png"), posX1, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark2.png"), posX2, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark3.png"), posX1, posY2, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark4.png"), posX2, posY2, 0, 0, 16, 16, 16, 16);
        }
    }
}
