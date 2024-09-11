package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.ICannonEntity;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.mcreator.superbwarfare.tools.RenderTool.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CannonHudOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (shouldRenderCrossHair(player)) {
            float yRotOffset = Mth.lerp(event.getPartialTick(), player.yRotO, player.getYRot());
            float xRotOffset = Mth.lerp(event.getPartialTick(), player.xRotO, player.getXRot());
            float diffY = Objects.requireNonNull(player.getVehicle()).getViewYRot(event.getPartialTick()) - yRotOffset;
            float diffX = Objects.requireNonNull(player.getVehicle()).getViewXRot(event.getPartialTick()) - xRotOffset + 1.3f;
            if (diffY > 180.0f) {
                diffY -= 360.0f;
            } else if (diffY < -180.0f) {
                diffY += 360.0f;
            }
            float f = (float)Math.min(w, h);
            float f1 = Math.min((float)w / f, (float)h / f);
            int i = Mth.floor(f * f1);
            int j = Mth.floor(f * f1);
            int k = (w - i) / 2;
            int l = (h - j) / 2;
            preciseBlit(event.getGuiGraphics(), new ResourceLocation(ModUtils.MODID, "textures/screens/cannon/cannon_crosshair.png"), k, l, 0, 0.0F, i, j, i, j);
            preciseBlit(event.getGuiGraphics(), new ResourceLocation(ModUtils.MODID, "textures/screens/cannon/indicator.png"), k + 13 * diffY, l + 17 * diffX, 0, 0.0F, i, j, i, j);
        }
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && !(player.getMainHandItem().getItem() instanceof GunItem)
                && (player.getVehicle() != null && (player.getVehicle() instanceof ICannonEntity))
                && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
    }
}
