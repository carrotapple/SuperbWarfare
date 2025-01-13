package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.entity.vehicle.*;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity.WEAPON_TYPE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleHudOverlay {

    private static final ResourceLocation ARMOR = ModUtils.loc("textures/screens/armor.png");
    private static final ResourceLocation ENERGY = ModUtils.loc("textures/screens/energy.png");
    private static final ResourceLocation HEALTH = ModUtils.loc("textures/screens/armor_value.png");
    private static final ResourceLocation HEALTH_FRAME = ModUtils.loc("textures/screens/armor_value_frame.png");

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (!shouldRenderHud(player)) return;

        Entity vehicle = player.getVehicle();
        if (vehicle == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int compatHeight = getArmorPlateCompatHeight(player);

        if (vehicle instanceof EnergyVehicleEntity energyVehicleEntity) {
            float energy = energyVehicleEntity.getEnergy();
            float maxEnergy = energyVehicleEntity.getMaxEnergy();
            guiGraphics.blit(ENERGY, 10, h - 22 - compatHeight, 100, 0, 0, 8, 8, 8, 8);
            guiGraphics.blit(HEALTH_FRAME, 20, h - 21 - compatHeight, 100, 0, 0, 60, 6, 60, 6);
            guiGraphics.blit(HEALTH, 20, h - 21 - compatHeight, 100, 0, 0, (int) (60 * energy / maxEnergy), 6, 60, 6);
        }

        if (vehicle instanceof VehicleEntity pVehicle) {
            float health = pVehicle.getHealth();
            float maxHealth = pVehicle.getMaxHealth();
            guiGraphics.blit(ARMOR, 10, h - 13 - compatHeight, 100, 0, 0, 8, 8, 8, 8);
            guiGraphics.blit(HEALTH_FRAME, 20, h - 12 - compatHeight, 100, 0, 0, 60, 6, 60, 6);
            guiGraphics.blit(HEALTH, 20, h - 12 - compatHeight, 100, 0, 0, (int) (60 * health / maxHealth), 6, 60, 6);
        }
        poseStack.popPose();

        if (vehicle instanceof IArmedVehicleEntity iVehicle && iVehicle.getAmmoCount(player) != -1) {
            boolean iCharge = iVehicle instanceof IChargeEntity;

            // 渲染当前弹药量
            poseStack.pushPose();
            poseStack.scale(1.5f, 1.5f, 1f);
            float v = h / 1.5f - (iCharge ? 42 : 29) / 1.5f;

            if (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))
                    && !(iVehicle instanceof ICannonEntity
                    || (iVehicle instanceof Ah6Entity ah6Entity && ah6Entity.getEntityData().get(WEAPON_TYPE) == 1))
            ) {
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

    private static boolean shouldRenderHud(Player player) {
        if (player == null) return false;
        return !player.isSpectator() && (player.getVehicle() != null && player.getVehicle() instanceof VehicleEntity);
    }

    private static String getVehicleAmmoType(ItemStack stack, IArmedVehicleEntity iVehicle) {
        if (stack.getItem() == ModItems.AP_5_INCHES.get() && iVehicle instanceof ICannonEntity) {
            return Component.translatable("des.superbwarfare.tips.ammo_type.ap").getString();
        }
        if (stack.getItem() == ModItems.HE_5_INCHES.get() && iVehicle instanceof ICannonEntity) {
            return Component.translatable("des.superbwarfare.tips.ammo_type.he").getString();
        }
        if (iVehicle instanceof SpeedboatEntity) {
            return Component.translatable("des.superbwarfare.tips.ammo_type.cal50").getString();
        }
        if (iVehicle instanceof Ah6Entity ah6Entity) {
            if (ah6Entity.getEntityData().get(WEAPON_TYPE) == 0) {
                return Component.translatable("des.superbwarfare.tips.ammo_type.20mm_cannon").getString();
            } else {
                return Component.translatable("des.superbwarfare.tips.ammo_type.rocket").getString();
            }

        }
        return "";
    }

    private static int getArmorPlateCompatHeight(Player player) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (stack == ItemStack.EMPTY) return 0;
        if (stack.getTag() == null || !stack.getTag().contains("ArmorPlate")) return 0;
        if (!DisplayConfig.ARMOR_PLATE_HUD.get()) return 0;
        return 9;
    }
}
