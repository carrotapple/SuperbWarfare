package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArmedVehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
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

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RedTriangleOverlay {

    private static final ResourceLocation TRIANGLE = ModUtils.loc("textures/screens/red_triangle.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getGuiGraphics().pose();

        Player player = mc.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.RPG.get())) return;
        if (player.getVehicle() instanceof ArmedVehicleEntity iArmedVehicle && iArmedVehicle.banHand(player))
            return;

        Entity idf = SeekTool.seekLivingEntity(player, player.level(), 128, 6);
        if (idf == null) return;
        Vec3 playerVec = new Vec3(Mth.lerp(event.getPartialTick(), player.xo, player.getX()), Mth.lerp(event.getPartialTick(), player.yo + player.getEyeHeight(), player.getEyeY()), Mth.lerp(event.getPartialTick(), player.zo, player.getZ()));
        double distance = idf.position().distanceTo(playerVec);
        Vec3 pos = new Vec3(Mth.lerp(event.getPartialTick(), idf.xo, idf.getX()), Mth.lerp(event.getPartialTick(), idf.yo + idf.getEyeHeight() + 0.5 + 0.07 * distance, idf.getEyeY() + 0.5 + 0.07 * distance), Mth.lerp(event.getPartialTick(), idf.zo, idf.getZ()));
        Vec3 point = RenderHelper.worldToScreen(pos, playerVec);
        if (point == null) return;

        poseStack.pushPose();
        float x = (float) point.x;
        float y = (float) point.y;

        RenderHelper.blit(poseStack, TRIANGLE, x - 4, y - 4, 0, 0, 8, 8, 8, 8, -65536);

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        poseStack.popPose();
    }
}
