package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.ICannonEntity;
import com.atsuishio.superbwarfare.entity.IChargeEntity;
import com.atsuishio.superbwarfare.entity.IVehicleEntity;
import com.atsuishio.superbwarfare.entity.SpeedboatEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleHudOverlay {

    public static float health = 0;
    public static float maxHealth = 0;

    public static float energy = 0;
    public static float maxEnergy = 0;

    private static final ResourceLocation ARMOR = ModUtils.loc("textures/screens/armor.png");
    private static final ResourceLocation ENERGY = ModUtils.loc("textures/screens/energy.png");
    private static final ResourceLocation HEALTH = ModUtils.loc("textures/screens/armor_value.png");
    private static final ResourceLocation HEALTH_FRAME = ModUtils.loc("textures/screens/armor_value_frame.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (!shouldRenderCrossHair(player)) return;

        Entity vehicle = player.getVehicle();
        if (vehicle == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiGraphics guiGraphics = event.getGuiGraphics();

        guiGraphics.pose().pushPose();
        if (vehicle instanceof IChargeEntity iCharge) {
            energy = iCharge.getEnergy();
            maxEnergy = iCharge.getMaxEnergy();
            guiGraphics.blit(ENERGY, w - 96, h - 28, 0, 0, 12, 12, 12, 12);
            guiGraphics.blit(HEALTH_FRAME, w - 83, h - 26, 0, 0, 80, 8, 80, 8);
            guiGraphics.blit(HEALTH, w - 83, h - 26, 0, 0, (int) (80 * energy / maxEnergy), 8, 80, 8);
        }

        if (vehicle instanceof IVehicleEntity iVehicle) {
            health = iVehicle.getHealth();
            maxHealth = iVehicle.getMaxHealth();
            guiGraphics.blit(ARMOR, w - 96, h - 14, 0, 0, 12, 12, 12, 12);
            guiGraphics.blit(HEALTH_FRAME, w - 83, h - 12, 0, 0, 80, 8, 80, 8);
            guiGraphics.blit(HEALTH, w - 83, h - 12, 0, 0, (int) (80 * health / maxHealth), 8, 80, 8);
        }
        guiGraphics.pose().popPose();

        PoseStack poseStack = event.getGuiGraphics().pose();

        if (vehicle instanceof IVehicleEntity iVehicle && iVehicle.getAmmoCount(player) != -1) {

            boolean iCharge = iVehicle instanceof IChargeEntity;

            // 渲染当前弹药量
            poseStack.pushPose();
            poseStack.scale(1.5f, 1.5f, 1f);

            float v = h / 1.5f - (iCharge ? 42 : 29) / 1.5f;

            if (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) && !(iVehicle instanceof ICannonEntity)) {
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        "∞",
                        w / 1.5f - 41 / 1.5f,
                        v,
                        0xFFFFFF,
                        true
                );
            } else {
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        iVehicle.getAmmoCount(player) + "",
                        w / 1.5f - 41 / 1.5f,
                        v,
                        0xFFFFFF,
                        true
                );
            }

            poseStack.popPose();
            ItemStack stack = player.getMainHandItem();

            // 渲染弹药类型
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    getVehicleAmmoType(stack, iVehicle),
                    w - 90,
                    h - (iCharge ? 38 : 26),
                    0xFFFFFF,
                    true
            );
        }
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;
        return !player.isSpectator()
                && (player.getVehicle() != null && player.getVehicle() instanceof IVehicleEntity);
    }

    private static String getVehicleAmmoType(ItemStack stack, IVehicleEntity iVehicle) {
        if (stack.getItem() == ModItems.AP_5_INCHES.get() && iVehicle instanceof ICannonEntity) {
            return Component.translatable("ammotype.superbwarfare.ap").getString();
        }
        if (stack.getItem() == ModItems.HE_5_INCHES.get() && iVehicle instanceof ICannonEntity) {
            return Component.translatable("ammotype.superbwarfare.he").getString();
        }
        if (iVehicle instanceof SpeedboatEntity) {
            return Component.translatable("ammotype.superbwarfare.cal50").getString();
        }
        return "";
    }
}
