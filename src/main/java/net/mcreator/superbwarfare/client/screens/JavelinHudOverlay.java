package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.mcreator.superbwarfare.client.RenderHelper.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class JavelinHudOverlay {

    private static float scopeScale = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();

            if (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) return;

            if ((stack.getItem() == ModItems.JAVELIN.get() && !stack.getOrCreateTag().getBoolean("HoloHidden")) && Minecraft.getInstance().options.getCameraType().isFirstPerson() && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
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
                preciseBlit(event.getGuiGraphics(), ModUtils.loc(stack.getOrCreateTag().getInt("ammo") > 0 ? "textures/screens/javelin/missile_green.png" : "textures/screens/javelin/missile_red.png"), k, l, 0, 0.0F, i, j, i, j);
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
            } else {
                scopeScale = 1;
            }
        }
    }
}
