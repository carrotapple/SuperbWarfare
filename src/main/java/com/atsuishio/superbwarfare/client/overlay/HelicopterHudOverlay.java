package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity;
import com.atsuishio.superbwarfare.entity.vehicle.IHelicopterEntity;
import com.atsuishio.superbwarfare.entity.vehicle.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.network.ModVariables;
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
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.text.DecimalFormat;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;
import static com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay.*;
import static com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity.WEAPON_TYPE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HelicopterHudOverlay {

    private static float scopeScale = 1;
    private static float lerpVy = 1;
    private static float lerpPower = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();

        if (player == null) return;

        if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit)
            return;

        if (player.getVehicle() instanceof IHelicopterEntity iHelicopterEntity && player.getVehicle() instanceof MobileVehicleEntity mobileVehicle && iHelicopterEntity.isDriver(player)) {
            poseStack.pushPose();

            poseStack.translate(-6 * ClientEventHandler.turnRot[1],-6 * ClientEventHandler.turnRot[0],0);
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

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_base.png"), k, l, 0, 0.0F, i, j, i, j);
                renderDriverAngle(guiGraphics, player, mobileVehicle, k, l, i, j);
                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(-iHelicopterEntity.getRotZ(event.getPartialTick())), w / 2f, h / 2f, 0);
                float pitch = iHelicopterEntity.getRotX(event.getPartialTick());

                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_line.png"), (float) w / 2 - 128, (float) h / 2 - 512 - 5.475f * pitch, 0, 0, 256, 1024, 256, 1024);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(-iHelicopterEntity.getRotZ(event.getPartialTick())), w / 2f, h / 2f - 56, 0);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/roll_ind.png"), (float) w / 2 - 8, (float) h / 2 - 88, 0, 0, 16, 16, 16, 16);
                poseStack.popPose();

                guiGraphics.blit(ModUtils.loc("textures/screens/helicopter/heli_power_ruler.png"), w / 2 + 100, h / 2 - 64, 0, 0, 64, 128, 64, 128);

                double height = mobileVehicle.position().distanceTo((Vec3.atLowerCornerOf(mobileVehicle.level().clip(new ClipContext(mobileVehicle.position(), mobileVehicle.position().add(new Vec3(0, -1, 0).scale(100)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, mobileVehicle)).getBlockPos())));
                double blockInWay = mobileVehicle.position().distanceTo((Vec3.atLowerCornerOf(mobileVehicle.level().clip(new ClipContext(mobileVehicle.position(), mobileVehicle.position().add(new Vec3(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y + 0.06, mobileVehicle.getDeltaMovement().z).normalize().scale(100)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, mobileVehicle)).getBlockPos())));

                float power = iHelicopterEntity.getPower();
                lerpPower = Mth.lerp(0.001f * event.getPartialTick(), lerpPower, power);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_power.png"), (float) w / 2 + 130f, ((float) h / 2 - 64 + 124 - power * 980), 0, 0, 4, power * 980, 4, power * 980);
                lerpVy = (float) Mth.lerp(0.021f * event.getPartialTick(), lerpVy, mobileVehicle.getDeltaMovement().y());
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_vy_move.png"), (float) w / 2 + 138, ((float) h / 2 - 3 - Math.max(lerpVy * 20, -24) * 2.5f), 0, 0, 8, 8, 8, 8);
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(lerpVy * 20) + "m/s"),
                        w / 2 + 146, (int) (h / 2 - 3 - Math.max(lerpVy * 20, -24) * 2.5), (lerpVy * 20 < -24 || ((lerpVy * 20 < -10 || (lerpVy * 20 < -1 && length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72 > 100)) && height < 36) || (length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72 > 40 && blockInWay < 72) ? -65536 : 0x66FF00), false);
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(mobileVehicle.getY())),
                        w / 2 + 104, h / 2, 0x66FF00, false);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/speed_frame.png"), (float) w / 2 - 144, (float) h / 2 - 6, 0, 0, 50, 18, 50, 18);
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##").format(length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72) + "KM/H"),
                        w / 2 - 140, h / 2, 0x66FF00, false);

                if (mobileVehicle instanceof Ah6Entity ah6Entity) {
                    if (ah6Entity.getEntityData().get(WEAPON_TYPE) == 0) {
                        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("20MM CANNON " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : ah6Entity.getAmmoCount(player))), w / 2 - 160, h / 2 - 60, 0x66FF00, false);
                    } else {
                        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("70MM ROCKET " + ah6Entity.getAmmoCount(player)), w / 2 - 160, h / 2 - 60, 0x66FF00, false);
                    }
                }

                if (lerpVy * 20 < -24) {
                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("SINK RATE，PULL UP!"),
                            w / 2 - 53, h / 2 + 24, -65536, false);
                } else if (((lerpVy * 20 < -10 || (lerpVy * 20 < -1 && length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72 > 100)) && height < 36)
                        || (length(mobileVehicle.getDeltaMovement().x, mobileVehicle.getDeltaMovement().y, mobileVehicle.getDeltaMovement().z) * 72 > 40 && blockInWay < 72)) {
                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("TERRAIN TERRAIN"),
                            w / 2 - 42, h / 2 + 24, -65536, false);
                }
            }

            Matrix4f transform = getVehicleTransform(mobileVehicle);
            float x0 = 0f;
            float y0 = 0.65f;
            float z0 = 0.8f;

            Vector4f worldPosition = transformPosition(transform, x0, y0, z0);

            float fovAdjust2 = (float) (Minecraft.getInstance().options.fov().get() / 30) - 1;
            double zoom = 0.96 * 3 + 0.06 * fovAdjust2;

            Vec3 pos = new Vec3(worldPosition.x, worldPosition.y, worldPosition.z).add(mobileVehicle.getViewVector(event.getPartialTick()).scale(500));
            Vec3 lookAngle = player.getLookAngle().normalize().scale(pos.distanceTo(cameraPos) * (1 - 1.0 / zoom));

            var cPos = cameraPos.add(lookAngle);

            Vec3 p = RenderHelper.worldToScreen(new Vec3(worldPosition.x, worldPosition.y, worldPosition.z).add(mobileVehicle.getViewVector(event.getPartialTick()).scale(500)), ClientEventHandler.zoom ? cPos : cameraPos);

            Vec3 p3 = RenderHelper.worldToScreen(new Vec3(worldPosition.x, worldPosition.y, worldPosition.z).add(mobileVehicle.getViewVector(event.getPartialTick()).scale(500)), cameraPos);
            if (p != null && p3 != null) {
                poseStack.pushPose();
                float x = (float) p.x;
                float y = (float) p.y;

                float x3 = (float) p3.x;
                float y3 = (float) p3.y;
                if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                    preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/crosshair_ind.png"), x - 8, y - 8, 0, 0, 16, 16, 16, 16);
                    renderKillIndicator(guiGraphics, x - 7.5f + (float) (2 * (Math.random() - 0.5f)), y - 7.5f + (float) (2 * (Math.random() - 0.5f)));
                } else if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
                    preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone.png"), x3 - 8, y3 - 8, 0, 0, 16, 16, 16, 16);
                    renderKillIndicator(guiGraphics, x3 - 7.5f + (float) (2 * (Math.random() - 0.5f)), y3 - 7.5f + (float) (2 * (Math.random() - 0.5f)));
                }

                poseStack.popPose();
            }

            poseStack.popPose();
        } else {
            scopeScale = 0.7f;
        }
    }

    private static void renderKillIndicator(GuiGraphics guiGraphics, float posX, float posY) {
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

    private static void renderDriverAngle(GuiGraphics guiGraphics, Player player, Entity heli, float k, float l, float i, float j) {
        float diffY = Mth.wrapDegrees(player.getYHeadRot() - heli.getYRot()) * 0.35f;
        float diffX = Mth.wrapDegrees(player.getXRot() - heli.getXRot()) * 0.072f;

        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/heli_driver_angle.png"), k + diffY, l + diffX, 0, 0.0F, i, j, i, j);
    }

    public static Matrix4f getVehicleTransform(VehicleEntity vehicle) {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) vehicle.getX(), (float) vehicle.getY(), (float) vehicle.getZ());
        transform.rotate(Axis.YP.rotationDegrees(-vehicle.getYRot()));
        transform.rotate(Axis.XP.rotationDegrees(vehicle.getXRot()));
        transform.rotate(Axis.ZP.rotationDegrees(vehicle.getRoll()));
        return transform;
    }

    public static Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }

    public static double length(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
