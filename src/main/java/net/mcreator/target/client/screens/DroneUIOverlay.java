package net.mcreator.target.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.init.TargetModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

import static net.mcreator.target.entity.DroneEntity.AMMO;

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
            if (stack.is(TargetModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
                event.getGuiGraphics().blit(new ResourceLocation("target:textures/screens/drone.png"), w / 2 - 16, h / 2 - 16, 0, 0, 32, 32, 32, 32);

                DroneEntity entity = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                        .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

                if (entity != null) {
                    double distance = player.distanceTo(entity);
                    int color = -1;

                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "MaxDistance:" + new DecimalFormat("##.#").format(MAX_DISTANCE) + "M", w / 2 + 10, h / 2 + 50, color, false);

                    if (distance > MAX_DISTANCE) {
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "WARNING", w / 2 + -18, h / 2 + -47, -65536, false);
                        color = -65536;
                    }

                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "Distance:" + new DecimalFormat("##.#").format(distance) + "M", w / 2 + 10, h / 2 + 33, color, false);
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "Health:" + new DecimalFormat("##.#").format(entity.getHealth()) + "/" + new DecimalFormat("##").format(entity.getMaxHealth()), w / 2 - 77, h / 2 + 33, -1, false);
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, "AMMO:" + new DecimalFormat("##.#").format(entity.getEntityData().get(AMMO)) + " / 6", w / 2 + 12, h / 2 + -37, -1, false);
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
