package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
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

import static net.mcreator.superbwarfare.entity.DroneEntity.AMMO;
import static net.mcreator.superbwarfare.entity.DroneEntity.KAMIKAZE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DroneUIOverlay {
    public static int MAX_DISTANCE = 256;

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
                event.getGuiGraphics().blit(new ResourceLocation("superbwarfare:textures/screens/drone.png"), w / 2 - 16, h / 2 - 16, 0, 0, 32, 32, 32, 32);

                DroneEntity entity = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                        .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

                if (entity != null) {

                    boolean lookAtEntity = false;
                    double distance = player.distanceTo(entity);
                    double block_range = entity.position().distanceTo((Vec3.atLowerCornerOf(entity.level().clip(
                            new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(520)),
                                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos())));

                    double entity_range = 0;

                    Entity lookingEntity = TraceTool.findLookingEntity(entity, 520);

                    if (lookingEntity != null) {
                        lookAtEntity = true;
                        entity_range = entity.distanceTo(lookingEntity);
                    }

                    int color = -1;


                    if (distance > MAX_DISTANCE - 48) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "WARNING", w / 2 + -18, h / 2 + -47, -65536, false);
                        color = -65536;
                    }

                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "Distance:" + new DecimalFormat("##.#").format(distance) + "M", w / 2 + 10, h / 2 + 33, color, false);
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "Health:" + new DecimalFormat("##.#").format(entity.getHealth()) + "/" + new DecimalFormat("##").format(entity.getMaxHealth()), w / 2 - 77, h / 2 + 33, -1, false);
                    if (!entity.getEntityData().get(KAMIKAZE)) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "AMMO:" + new DecimalFormat("##.#").format(entity.getEntityData().get(AMMO)) + " / 6", w / 2 + 12, h / 2 + -37, -1, false);
                    } else {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "KAMIKAZE", w / 2 + 12, h / 2 + -37, -65536, false);
                    }

                    if (lookAtEntity) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "Range：" + new DecimalFormat("##.#").format(entity_range) + "M " + lookingEntity.getDisplayName().getString(), w / 2 + 12, h / 2 - 28, color, false);
                    } else {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, block_range > 512 ? "Range：---M" : "Range：" + new DecimalFormat("##.#").format(block_range) + "M", w / 2 + 12, h / 2 - 28, color, false);
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
