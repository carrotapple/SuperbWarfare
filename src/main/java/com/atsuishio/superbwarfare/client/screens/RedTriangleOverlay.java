package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.HudUtil;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RedTriangleOverlay {
    private static final ResourceLocation TRIANGLE = ModUtils.loc("textures/screens/red_triangle.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        PoseStack poseStack = event.getGuiGraphics().pose();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModItems.RPG.get())) {
            Entity idf = SeekTool.seekLivingEntity(player, player.level(),128,6);

            if (idf == null) return;

            Vec3 p = RenderHelper.worldToScreen(new Vec3(idf.getX(), idf.getEyeY() + 2,idf.getZ()), cameraPos);

            if (p == null) return;

            poseStack.pushPose();

            int x = (int) p.x;
            int y = (int) p.y;

            HudUtil.blit(poseStack, TRIANGLE, x-4, y-4, 0, 0, 8, 8, 8, 8, -65536);

            poseStack.popPose();

        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
