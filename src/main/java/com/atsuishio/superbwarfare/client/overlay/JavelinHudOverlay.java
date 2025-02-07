package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.IArmedVehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class JavelinHudOverlay {

    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/javelin/frame.png");
    private static final ResourceLocation FRAME_TARGET = ModUtils.loc("textures/screens/javelin/frame_target.png");
    private static final ResourceLocation FRAME_LOCK = ModUtils.loc("textures/screens/javelin/frame_lock.png");
    private static float scopeScale = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = event.getGuiGraphics().pose();

        if (player != null) {
            ItemStack stack = player.getMainHandItem();

            if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit)
                return;
            if (player.getVehicle() instanceof IArmedVehicleEntity iArmedVehicle && iArmedVehicle.banHand(player)) return;

            if ((stack.getItem() == ModItems.JAVELIN.get() && !stack.getOrCreateTag().getBoolean("HoloHidden")) && Minecraft.getInstance().options.getCameraType().isFirstPerson() && ClientEventHandler.zoom) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                float deltaFrame = Minecraft.getInstance().getDeltaFrameTime();
                float moveX = (float) (-32 * ClientEventHandler.turnRot[1] - (player.isSprinting() ? 100 : 67) * ClientEventHandler.movePosX + 3 * ClientEventHandler.cameraRot[2]);
                float moveY = (float) (-32 * ClientEventHandler.turnRot[0] + 100 * (float) ClientEventHandler.velocityY - (player.isSprinting() ? 100 : 67) * ClientEventHandler.movePosY - 12 * ClientEventHandler.firePos + 3 * ClientEventHandler.cameraRot[1]);
                scopeScale = (float) Mth.lerp(0.5F * deltaFrame, scopeScale, 1.35F + (0.2f * ClientEventHandler.firePos));
                float f = (float) Math.min(w, h);
                float f1 = Math.min((float) w / f, (float) h / f) * scopeScale;
                float i = Mth.floor(f * f1);
                float j = Mth.floor(f * f1);
                float k = ((w - i) / 2) + moveX;
                float l = ((h - j) / 2) + moveY;
                float i1 = k + i;
                float j1 = l + j;
                preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/javelin/javelin_hud.png"), k, l, 0, 0.0F, i, j, i, j);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc(stack.getOrCreateTag().getBoolean("TopMode") ? "textures/screens/javelin/top.png" : "textures/screens/javelin/dir.png"), k, l, 0, 0.0F, i, j, i, j);
                preciseBlit(event.getGuiGraphics(), ModUtils.loc(GunsTool.getGunIntTag(stack, "Ammo", 0) > 0 ? "textures/screens/javelin/missile_green.png" : "textures/screens/javelin/missile_red.png"), k, l, 0, 0.0F, i, j, i, j);
                if (stack.getOrCreateTag().getInt("SeekTime") > 1 && stack.getOrCreateTag().getInt("SeekTime") < 20) {
                    preciseBlit(event.getGuiGraphics(), ModUtils.loc("textures/screens/javelin/seek.png"), k, l, 0, 0.0F, i, j, i, j);
                }

                event.getGuiGraphics().fill(RenderType.guiOverlay(), 0, (int) l, (int) k + 3, (int) j1, -90, -16777216);
                event.getGuiGraphics().fill(RenderType.guiOverlay(), (int) i1, (int) l, w, (int) j1, -90, -16777216);
                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);

                float fovAdjust = (float) Minecraft.getInstance().options.fov().get() / 80;

                Entity targetEntity = EntityFindUtil.findEntity(player.level(), stack.getOrCreateTag().getString("TargetEntity"));
                List<Entity> entities = SeekTool.seekLivingEntities(player, player.level(), 512, 8 * fovAdjust);
                Entity naerestEntity = SeekTool.seekLivingEntity(player, player.level(), 512, 6);

                float fovAdjust2 = (float) (Minecraft.getInstance().options.fov().get() / 30) - 1;

                double zoom = Minecraft.getInstance().options.fov().get() / ClientEventHandler.fov + 0.5 * fovAdjust2;

                for (var e : entities) {
                    Vec3 pos = new Vec3(e.getX(), e.getEyeY(), e.getZ());
                    Vec3 lookAngle = player.getLookAngle().normalize().scale(pos.distanceTo(cameraPos) * (1 - 1.0 / zoom));

                    var cPos = cameraPos.add(lookAngle);
                    Vec3 point = RenderHelper.worldToScreen(pos, cPos);
                    if (point == null) return;

                    boolean lockOn = stack.getOrCreateTag().getInt("SeekTime") > 20 && e == targetEntity;
                    boolean nearest = e == naerestEntity;

                    poseStack.pushPose();
                    float x = (float) point.x;
                    float y = (float) point.y;

                    RenderHelper.blit(poseStack, lockOn ? FRAME_LOCK : nearest ? FRAME_TARGET : FRAME, x - 12, y - 12, 0, 0, 24, 24, 24, 24, 1f);
                    poseStack.popPose();
                }
            } else {
                scopeScale = 1;
            }
        }
    }
}
