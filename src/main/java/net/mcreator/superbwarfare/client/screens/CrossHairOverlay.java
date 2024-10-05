package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.mcreator.superbwarfare.tools.RenderTool.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CrossHairOverlay {
    private static final ResourceLocation REX_HORIZONTAL = ModUtils.loc("textures/screens/rex_horizontal.png");
    private static final ResourceLocation REX_VERTICAL = ModUtils.loc("textures/screens/rex_vertical.png");

    public static int HIT_INDICATOR = 0;
    public static int HEAD_INDICATOR = 0;
    public static int KILL_INDICATOR = 0;
    private static float scopeScale = 1f;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();

        ItemStack stack = player.getMainHandItem();
        double spread = ClientEventHandler.gunSpread + 3 * ClientEventHandler.firePos;
        float deltaFrame = Minecraft.getInstance().getDeltaFrameTime();
        float moveX = 0;
        float moveY = 0;
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        if (DisplayConfig.FLOAT_CROSS_HAIR.get()) {
            moveX = (float) (-6 * ClientEventHandler.turnRot[1] - (player.isSprinting() ? 10 : 6) * ClientEventHandler.movePosX);
            moveY = (float) (-6 * ClientEventHandler.turnRot[0] + 6 * (float) ClientEventHandler.velocityY - (player.isSprinting() ? 10 : 6) * ClientEventHandler.movePosY - 2 * ClientEventHandler.firePos);
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        scopeScale = (float) Mth.lerp(0.5F * deltaFrame, scopeScale, 1 + 1.5f * spread);
        float minLength = (float) Math.min(w, h);
        float scaledMinLength = Math.min((float) w / minLength, (float) h / minLength) * 0.012f * scopeScale;
        float finLength = Mth.floor(minLength * scaledMinLength);
        float finPosX = ((w - finLength) / 2) + moveX;
        float finPosY = ((h - finLength) / 2) + moveY;

        if (shouldRenderCrossHair(player) || (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON && stack.is(ModItems.MINIGUN.get())) || (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK && (ClientEventHandler.zoomTime > 0 || ClientEventHandler.pullPos > 0))) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/point.png"), w / 2f - 7.5f + moveX, h / 2f - 7.5f + moveY, 0, 0, 16, 16, 16, 16);
            if (!player.isSprinting() || player.getPersistentData().getDouble("noRun") > 0) {
                if (stack.is(ModTags.Items.SHOTGUN)) {
                    if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
                        normalCrossHair(guiGraphics, w ,h , spread, moveX, moveY);
                    } else {
                        shotgunCrossHair(guiGraphics, finPosX, finPosY, finLength);
                    }
                } else {
                    normalCrossHair(guiGraphics, w ,h , spread, moveX, moveY);
                }
            }
        }

        if (stack.is(ModItems.BOCEK.get())) {
            if (stack.getOrCreateTag().getBoolean("HoloHidden")) {
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/point.png"), w / 2f - 7.5f + moveX, h / 2f - 7.5f + moveY, 0, 0, 16, 16, 16, 16);
                if (!player.isSprinting() || player.getPersistentData().getDouble("noRun") > 0 || ClientEventHandler.pullPos > 0) {
                    if (ClientEventHandler.zoomTime < 0.1) {
                        if (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug) {
                            normalCrossHair(guiGraphics, w ,h , spread, moveX, moveY);
                        } else {
                            shotgunCrossHair(guiGraphics, finPosX, finPosY, finLength);
                        }
                    } else {
                        normalCrossHair(guiGraphics, w ,h , spread, moveX, moveY);
                    }
                }
            }
        }



        // 在开启伤害指示器时才进行渲染
        if (DisplayConfig.KILL_INDICATION.get()) {
            renderKillIndicator(guiGraphics, w, h, moveX, moveY);
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getBoolean("need_bolt_action")) {
            Font font = Minecraft.getInstance().font;
            Component component = Component.translatable("des.superbwarfare.need_bolt_action");

            guiGraphics.drawString(font, component, w / 2 - font.width(component) / 2, h / 2 + 50, 0xFF6969);
        }

    }

    private static void normalCrossHair(GuiGraphics guiGraphics, int w, int h, double spread, float moveX, float moveY ) {
        preciseBlit(guiGraphics, REX_HORIZONTAL, (float) (w / 2f - 13.5f - 2.8f * spread) + moveX, h / 2f - 7.5f + moveY, 0, 0, 16, 16, 16, 16);
        preciseBlit(guiGraphics, REX_HORIZONTAL, (float) (w / 2f - 2.5f + 2.8f * spread) + moveX, h / 2f - 7.5f + moveY, 0, 0, 16, 16, 16, 16);
        preciseBlit(guiGraphics, REX_VERTICAL, w / 2f - 7.5f + moveX, (float) (h / 2f - 2.5f + 2.8f * spread) + moveY, 0, 0, 16, 16, 16, 16);
        preciseBlit(guiGraphics, REX_VERTICAL, w / 2f - 7.5f + moveX, (float) (h / 2f - 13.5f - 2.8f * spread) + moveY, 0, 0, 16, 16, 16, 16);
    }

    private static void shotgunCrossHair(GuiGraphics guiGraphics, float finPosX, float finPosY, float finLength) {
        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/shotgun_hud.png"), finPosX, finPosY, 0, 0.0F, finLength, finLength, finLength, finLength);
    }

    private static boolean shouldRenderCrossHair(Player player) {
        if (player == null) return false;

        if (player.isSpectator()) return false;
        if (!player.getMainHandItem().is(ModTags.Items.GUN) || ClientEventHandler.zoomTime > 0.8)
            return false;

        return !(player.getMainHandItem().getItem() == ModItems.M_79.get() || player.getMainHandItem().getItem() == ModItems.BOCEK.get())
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
    }

    private static void renderKillIndicator(GuiGraphics guiGraphics, int w, int h, float moveX, float moveY) {
        float posX = w / 2f - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float posY = h / 2f - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float rate = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker.png"), posX + moveX, posY + moveY, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/headshot_mark.png"), posX + moveX, posY + moveY, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            float posX1 = w / 2f - 7.5f - 2 + rate + moveX;
            float posY1 = h / 2f - 7.5f - 2 + rate + moveY;
            float posX2 = w / 2f - 7.5f + 2 - rate + moveX;
            float posY2 = h / 2f - 7.5f + 2 - rate + moveY;

            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark1.png"), posX1, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark2.png"), posX2, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark3.png"), posX1, posY2, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark4.png"), posX2, posY2, 0, 0, 16, 16, 16, 16);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        handleRenderDamageIndicator();
    }

    private static void handleRenderDamageIndicator() {
        HEAD_INDICATOR = Math.max(0, HEAD_INDICATOR - 1);
        HIT_INDICATOR = Math.max(0, HIT_INDICATOR - 1);
        KILL_INDICATOR = Math.max(0, KILL_INDICATOR - 1);
    }
}
