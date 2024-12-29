package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.HudUtil;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;
import java.util.List;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;
import static com.atsuishio.superbwarfare.entity.DroneEntity.AMMO;
import static com.atsuishio.superbwarfare.entity.DroneEntity.KAMIKAZE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DroneUIOverlay {
    public static int MAX_DISTANCE = 256;
    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/javelin/frame.png");
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                event.getGuiGraphics().blit(ModUtils.loc("textures/screens/drone.png"), w / 2 - 16, h / 2 - 16, 0, 0, 32, 32, 32, 32);
                event.getGuiGraphics().blit(ModUtils.loc("textures/screens/drone_fov.png"), w / 2 + 100, h / 2 - 64, 0, 0, 64, 129, 64, 129);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone_fov_move.png"), (float) w / 2 + 100, (float) (h / 2 - 64 - ((ClientEventHandler.droneFovLerp - 1) * 23.8)), 0, 0, 64, 129, 64, 129);
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(new DecimalFormat("##.#").format(ClientEventHandler.droneFovLerp) + "x"),
                        w / 2 + 144, h / 2 + 56 - (int) ((ClientEventHandler.droneFovLerp - 1) * 23.8), -1, false);

                DroneEntity entity = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));

                if (entity != null) {
                    boolean lookAtEntity = false;
                    double distance = player.distanceTo(entity);
                    double block_range = entity.position().distanceTo((Vec3.atLowerCornerOf(entity.level().clip(
                            new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getViewVector(event.getPartialTick()).scale(520)),
                                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos())));

                    double entity_range = 0;

                    Entity lookingEntity = SeekTool.seekLivingEntity(entity, entity.level(), 512, 2);

                    if (lookingEntity != null) {
                        lookAtEntity = true;
                        entity_range = entity.distanceTo(lookingEntity);
                    }

                    int color = -1;

                    if (distance > MAX_DISTANCE - 48) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.warning"),
                                w / 2 - 18, h / 2 - 47, -65536, false);
                        color = -65536;
                    }

                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.distance")
                                    .append(Component.literal(new DecimalFormat("##.#").format(distance) + "M")),
                            w / 2 + 10, h / 2 + 33, color, false);

                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.health")
                                    .append(Component.literal(new DecimalFormat("##.#").format(entity.getHealth()) + "/" + new DecimalFormat("##.#").format(entity.getMaxHealth()))),
                            w / 2 - 77, h / 2 + 33, -1, false);
                    if (!entity.getEntityData().get(KAMIKAZE)) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.ammo")
                                        .append(Component.literal(new DecimalFormat("##.#").format(entity.getEntityData().get(AMMO)) + " / 6")),
                                w / 2 + 12, h / 2 - 37, -1, false);
                    } else {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.kamikaze"),
                                w / 2 + 12, h / 2 - 37, -65536, false);
                    }

                    if (lookAtEntity) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                        .append(Component.literal(new DecimalFormat("##.#").format(entity_range) + "M " + lookingEntity.getDisplayName().getString())),
                                w / 2 + 12, h / 2 - 28, color, false);
                    } else {
                        if (block_range > 512) {
                            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                    .append(Component.literal("---M")), w / 2 + 12, h / 2 - 28, color, false);
                        } else {
                            event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                            .append(Component.literal(new DecimalFormat("##.#").format(block_range) + "M")),
                                    w / 2 + 12, h / 2 - 28, color, false);
                        }
                    }
                    Minecraft mc = Minecraft.getInstance();
                    Camera camera = mc.gameRenderer.getMainCamera();
                    Vec3 cameraPos = camera.getPosition();
                    PoseStack poseStack = event.getGuiGraphics().pose();

                    List<Entity> entities = SeekTool.seekLivingEntities(entity, entity.level(), 256, 30);
                    float fovAdjust2 = (float) (Minecraft.getInstance().options.fov().get() / 30) - 1;
                    double zoom = 0.975 * ClientEventHandler.droneFovLerp + 0.06 * fovAdjust2;

                    for (var e : entities) {
                        Vec3 pos = new Vec3(e.getX(), e.getEyeY(), e.getZ());
                                    Vec3 lookAngle = entity.getLookAngle().normalize().scale(pos.distanceTo(cameraPos) * (1 - 1.0 / zoom));

                        var cPos = cameraPos.add(lookAngle);
                        Vec3 p = RenderHelper.worldToScreen(pos, cPos);
                        if (p == null) return;

                        poseStack.pushPose();
                        int x = (int) p.x;
                        int y = (int) p.y;

                        HudUtil.blit(poseStack, FRAME, x - 12, y - 12, 0, 0, 24, 24, 24, 24, 1f);
                        poseStack.popPose();
                    }
                }
            }

            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }
}
