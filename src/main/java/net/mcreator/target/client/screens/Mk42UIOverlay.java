package net.mcreator.target.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.target.entity.Mk42Entity;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.RenderTool;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Mk42UIOverlay {
    public static final int TEXTURE_WIDTH = 771;
    public static final int TEXTURE_HEIGHT = 1006;

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
            RenderTool.preciseBlit(event.getGuiGraphics(),
                new ResourceLocation("target:textures/screens/mk_42_rex.png"),
                (float) w / 2 - (float) TEXTURE_WIDTH / 10f, (float) h / 2 - (float) TEXTURE_HEIGHT / 10f,
                0, 0, TEXTURE_WIDTH / 5f, TEXTURE_HEIGHT / 5f, TEXTURE_WIDTH / 5f, TEXTURE_HEIGHT / 5f);
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
//                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && (player.getVehicle() != null && player.getVehicle() instanceof Mk42Entity)
                && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zoom;
    }
}
