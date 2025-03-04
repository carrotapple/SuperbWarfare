package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

import static com.atsuishio.superbwarfare.client.RenderHelper.preciseBlit;
import static com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay.*;
import static com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity.LOADED_MISSILE;
import static com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity.COAX_HEAT;
import static com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity.HEAT;
import static com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity.AMMO;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VehicleHudOverlay {

    private static float scopeScale = 1;
    private static final ResourceLocation FRAME = ModUtils.loc("textures/screens/land/tv_frame.png");
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

        // 渲染地面武装HUD
        renderLandArmorHud(event, w, h);

        int compatHeight = getArmorPlateCompatHeight(player);

        if (vehicle instanceof EnergyVehicleEntity energyVehicleEntity) {
            float energy = energyVehicleEntity.getEnergy();
            float maxEnergy = energyVehicleEntity.getMaxEnergy();
            preciseBlit(guiGraphics, ENERGY, 10, h - 22 - compatHeight, 100, 0, 0, 8, 8, 8, 8);
            preciseBlit(guiGraphics, HEALTH_FRAME, 20, h - 21 - compatHeight, 100, 0, 0, 60, 6, 60, 6);
            preciseBlit(guiGraphics, HEALTH, 20, h - 21 - compatHeight, 100, 0, 0, (int) (60 * energy / maxEnergy), 6, 60, 6);
        }

        if (vehicle instanceof VehicleEntity pVehicle) {
            float health = pVehicle.getHealth();
            float maxHealth = pVehicle.getMaxHealth();
            preciseBlit(guiGraphics, ARMOR, 10, h - 13 - compatHeight, 100, 0, 0, 8, 8, 8, 8);
            preciseBlit(guiGraphics, HEALTH_FRAME, 20, h - 12 - compatHeight, 100, 0, 0, 60, 6, 60, 6);
            preciseBlit(guiGraphics, HEALTH, 20, h - 12 - compatHeight, 100, 0, 0, (int) (60 * health / maxHealth), 6, 60, 6);
        }
        poseStack.popPose();

        if (vehicle instanceof IArmedVehicleEntity iVehicle && iVehicle.getAmmoCount(player) != -1 && iVehicle.isDriver(player)) {
            boolean iCharge = iVehicle instanceof EnergyVehicleEntity;

            // 渲染当前弹药量
            poseStack.pushPose();
            poseStack.scale(1.5f, 1.5f, 1f);
            float v = h / 1.5f - (iCharge ? 42 : 29) / 1.5f;

            if (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get()))
                    && !(iVehicle instanceof ICannonEntity
                    || (iVehicle instanceof Ah6Entity ah6Entity && ah6Entity.getWeaponType() == 1))
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
            if (ah6Entity.getWeaponType() == 0) {
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

    public static void renderLandArmorHud(RenderGuiEvent.Pre event, int w, int h) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        if (player == null) return;

        if (player.getVehicle() instanceof ILandArmorEntity iLand && iLand.isDriver(player)
                && iLand instanceof MultiWeaponVehicleEntity multiWeaponVehicle
                && iLand instanceof MobileVehicleEntity mobileVehicle) {
            poseStack.pushPose();



            poseStack.translate(Mth.clamp(-8 * ClientEventHandler.turnRot[1], -10, 10), Mth.clamp(-8 * ClientEventHandler.turnRot[0], -10, 10) - 0.3 * ClientEventHandler.shakeTime + 5 * ClientEventHandler.cameraRoll, 0);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(-0.5f * ClientEventHandler.cameraRoll), w / 2f, h / 2f, 0);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            scopeScale = Mth.lerp(event.getPartialTick(), scopeScale, 1F);
            float f = (float) Math.min(w, h);
            float f1 = Math.min((float) w / f, (float) h / f) * scopeScale;
            float i = Mth.floor(f * f1);
            float j = Mth.floor(f * f1);
            float k = ((w - i) / 2);
            float l = ((h - j) / 2);

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle) {
                int addW = (w / h) * 48;
                int addH = (w / h) * 27;
                preciseBlit(guiGraphics, FRAME, (float) -addW / 2, (float) -addH / 2, 10, 0, 0.0F, w + addW, h + addH, w + addW, h + addH);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/line.png"), w / 2f - 64, h - 56, 0, 0.0F, 128, 1, 128, 1);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/line.png"), w / 2f + 112, h - 71, 0, 0.0F, 1, 16, 1, 16);

                // 不同武器种类的准星
                if (multiWeaponVehicle instanceof Yx100Entity) {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/tank_cannon_cross_ap.png"), k, l, 0, 0.0F, i, j, i, j);
                    } else if (multiWeaponVehicle.getWeaponType() == 1) {
                        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/tank_cannon_cross_he.png"), k, l, 0, 0.0F, i, j, i, j);
                    }

                } else {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/lav_cannon_cross.png"), k, l, 0, 0.0F, i, j, i, j);
                    } else if (multiWeaponVehicle.getWeaponType() == 1) {
                        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/lav_gun_cross.png"), k, l, 0, 0.0F, i, j, i, j);
                    } else if (multiWeaponVehicle.getWeaponType() == 2) {
                        preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/lav_missile_cross.png"), k, l, 0, 0.0F, i, j, i, j);
                    }
                }

                // 指南针
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/compass.png"), (float) w / 2 - 128, (float) 10, 128 + ((float) 64 / 45 * player.getYRot()), 0, 256, 16, 512, 16);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/helicopter/roll_ind.png"), w / 2f - 8, 30, 0, 0.0F, 16, 16, 16, 16);

                // 炮塔方向
                poseStack.pushPose();
                poseStack.rotateAround(Axis.ZP.rotationDegrees(Mth.lerp(event.getPartialTick(), iLand.turretYRotO(), iLand.turretYRot())), w / 2f + 112, h - 56, 0);
                preciseBlit(guiGraphics, ModUtils.loc("textures/screens/land/body.png"), w / 2f + 96, h - 72, 0, 0.0F, 32, 32, 32, 32);
                poseStack.popPose();

                // 时速
                guiGraphics.drawString(mc.font, Component.literal(FormatTool.format0D(mobileVehicle.getDeltaMovement().length() * 72, " km/h")),
                        w / 2 + 160, h / 2 - 48, 0x66FF00, false);

                // 低电量警告

                if (mobileVehicle.getEnergy() < 0.02 * mobileVehicle.getMaxEnergy()) {
                    guiGraphics.drawString(mc.font, Component.literal("NO POWER!"),
                            w / 2 - 144, h / 2 + 14, -65536, false);
                } else if (mobileVehicle.getEnergy() < 0.2 * mobileVehicle.getMaxEnergy()) {
                    guiGraphics.drawString(mc.font, Component.literal("LOW POWER"),
                            w / 2 - 144, h / 2 + 14, 0xFF6B00, false);
                }

                // 测距
                boolean lookAtEntity = false;
                double blockRange = cameraPos.distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                        new ClipContext(player.getEyePosition(), player.getEyePosition().add(iLand.getBarrelVec(event.getPartialTick()).scale(520)),
                                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

                double entityRange = 0;

                Entity lookingEntity = SeekTool.seekLivingEntity(player, player.level(), 512, 1);
                if (lookingEntity != null) {
                    lookAtEntity = true;
                    entityRange = player.distanceTo(lookingEntity);
                }

                if (lookAtEntity) {
                    guiGraphics.drawString(mc.font, Component.literal(FormatTool.format1D(entityRange, "m")),
                            w / 2 - 6, h - 53, 0x66FF00, false);
                } else {
                    if (blockRange > 512) {
                        guiGraphics.drawString(mc.font, Component.literal("---m"), w / 2 - 6, h - 53, 0x66FF00, false);
                    } else {
                        guiGraphics.drawString(mc.font, Component.literal(FormatTool.format1D(blockRange, "m")),
                                w / 2 - 6, h - 53, 0x66FF00, false);
                    }
                }

                // 武器名称
                //LAV-150
                if (player.getVehicle() instanceof Lav150Entity lav) {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        double heat = 1 - lav.getEntityData().get(HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal("20MM CANNON " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    } else {
                        double heat = 1 - lav.getEntityData().get(COAX_HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal("7.62MM COAX " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    }
                }

                //BMP-2
                if (player.getVehicle() instanceof Bmp2Entity bmp2) {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        double heat = 1 - bmp2.getEntityData().get(HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal(" 30MM 2A42 " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp2.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    } else if (multiWeaponVehicle.getWeaponType() == 1) {
                        double heat = 1 - bmp2.getEntityData().get(COAX_HEAT) / 100.0F;
                        guiGraphics.drawString(mc.font, Component.literal(" 7.62MM ПКТ " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp2.getAmmoCount(player))), w / 2 - 33, h - 65, Mth.hsvToRgb((float) heat / 3.745318352059925F, 1.0F, 1.0F), false);
                    } else {
                        guiGraphics.drawString(mc.font, Component.literal("    9M113  " + bmp2.getEntityData().get(LOADED_MISSILE) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp2.getAmmoCount(player))), w / 2 - 33, h - 65, 0x66FF00, false);
                    }

                }

                //YX-100
                if (player.getVehicle() instanceof Yx100Entity yx100) {
                    if (multiWeaponVehicle.getWeaponType() == 0) {
                        guiGraphics.drawString(mc.font, Component.literal("AP SHELL  " + yx100.getAmmoCount(player) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : yx100.getEntityData().get(AMMO))), w / 2 - 33, h - 65, 0x66FF00, false);
                    } else {
                        guiGraphics.drawString(mc.font, Component.literal("HE SHELL  " + yx100.getAmmoCount(player) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : yx100.getEntityData().get(AMMO))), w / 2 - 33, h - 65, 0x66FF00, false);
                    }
                }

                // 血量
                double heal = mobileVehicle.getHealth() / mobileVehicle.getMaxHealth();
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(FormatTool.format0D(100 * heal)), w / 2 - 165, h / 2 - 46, Mth.hsvToRgb((float) heal / 3.745318352059925F, 1.0F, 1.0F), false);

                renderKillIndicator(guiGraphics, w, h);

            } else if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK && !ClientEventHandler.zoomVehicle) {
                Vec3 p = RenderHelper.worldToScreen(new Vec3(player.getX(), player.getY(), player.getZ()).add(iLand.getBarrelVec(event.getPartialTick()).scale(192)), cameraPos);
                // 第三人称准星
                if (p != null) {
                    poseStack.pushPose();
                    float x = (float) p.x;
                    float y = (float) p.y;

                    poseStack.pushPose();
                    preciseBlit(guiGraphics, ModUtils.loc("textures/screens/drone.png"), x - 12, y - 12, 0, 0, 24, 24, 24, 24);
                    renderKillIndicator3P(guiGraphics, x - 7.5f + (float) (2 * (Math.random() - 0.5f)), y - 7.5f + (float) (2 * (Math.random() - 0.5f)));

                    poseStack.pushPose();

                    poseStack.translate(x, y, 0);
                    poseStack.scale(0.75f, 0.75f, 1);

                    //LAV-150
                    if (multiWeaponVehicle instanceof Lav150Entity lav1501) {
                        if (multiWeaponVehicle.getWeaponType() == 0) {
                            double heat = lav1501.getEntityData().get(HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("20MM CANNON " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav1501.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat, 1.0F), false);
                        } else {
                            double heat2 = lav1501.getEntityData().get(COAX_HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("7.62MM COAX " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : lav1501.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat2, 1.0F), false);
                        }
                    }
                    //BMP-2
                    if (multiWeaponVehicle instanceof Bmp2Entity bmp201) {
                        if (multiWeaponVehicle.getWeaponType() == 0) {
                            double heat = bmp201.getEntityData().get(HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("30MM 2A42 " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp201.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat, 1.0F), false);
                        } else if (multiWeaponVehicle.getWeaponType() == 1) {
                            double heat2 = bmp201.getEntityData().get(COAX_HEAT) / 100.0F;
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("7.62MM ПКТ " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp201.getAmmoCount(player))), 30, -9, Mth.hsvToRgb(0F, (float) heat2, 1.0F), false);
                        } else {
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("9M113 "  + bmp201.getEntityData().get(LOADED_MISSILE) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : bmp201.getAmmoCount(player))), 30, -9, -1, false);
                        }
                    }
                    //YX-100
                    if (multiWeaponVehicle instanceof Yx100Entity yx100) {
                        if (multiWeaponVehicle.getWeaponType() == 0) {
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("AP SHELL " + yx100.getAmmoCount(player) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : yx100.getEntityData().get(AMMO))), 30, -9, -1, false);
                        } else {
                            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("HE SHELL " + yx100.getAmmoCount(player) + " " + (player.getInventory().hasAnyMatching(s -> s.is(ModItems.CREATIVE_AMMO_BOX.get())) ? "∞" : yx100.getEntityData().get(AMMO))), 30, -9, -1, false);
                        }
                    }

                    double heal = 1 - mobileVehicle.getHealth() / mobileVehicle.getMaxHealth();

                    guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("HP " +
                            FormatTool.format0D(100 * mobileVehicle.getHealth() / mobileVehicle.getMaxHealth())), 30, 1, Mth.hsvToRgb(0F, (float) heal, 1.0F), false);

                    poseStack.popPose();
                    poseStack.popPose();
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        } else {
            scopeScale = 0.7f;
        }
    }

    public static void renderKillIndicator(GuiGraphics guiGraphics, float w, float h) {
        float posX = w / 2f - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float posY = h / 2f - 7.5f + (float) (2 * (Math.random() - 0.5f));
        float rate = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (VEHICLE_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker_vehicle.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/headshot_mark.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            float posX1 = w / 2f - 7.5f - 2 + rate;
            float posY1 = h / 2f - 7.5f - 2 + rate;
            float posX2 = w / 2f - 7.5f + 2 - rate;
            float posY2 = h / 2f - 7.5f + 2 - rate;

            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark1.png"), posX1, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark2.png"), posX2, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark3.png"), posX1, posY2, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark4.png"), posX2, posY2, 0, 0, 16, 16, 16, 16);
        }
    }

    private static void renderKillIndicator3P(GuiGraphics guiGraphics, float posX, float posY) {
        float rate = (40 - KILL_INDICATOR * 5) / 5.5f;

        if (HIT_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (VEHICLE_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/hit_marker_vehicle.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (HEAD_INDICATOR > 0) {
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/headshot_mark.png"), posX, posY, 0, 0, 16, 16, 16, 16);
        }

        if (KILL_INDICATOR > 0) {
            float posX1 = posX - 2 + rate;
            float posY1 = posY - 2 + rate;
            float posX2 = posX + 2 - rate;
            float posY2 = posY + 2 - rate;

            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark1.png"), posX1, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark2.png"), posX2, posY1, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark3.png"), posX1, posY2, 0, 0, 16, 16, 16, 16);
            preciseBlit(guiGraphics, ModUtils.loc("textures/screens/kill_mark4.png"), posX2, posY2, 0, 0, 16, 16, 16, 16);
        }
    }
}
