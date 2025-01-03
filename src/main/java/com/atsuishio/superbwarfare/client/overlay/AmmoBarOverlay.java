package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModKeyMappings;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AmmoBarOverlay {

    private static final ResourceLocation LINE = ModUtils.loc("textures/gun_icon/fire_mode/line.png");
    private static final ResourceLocation SEMI = ModUtils.loc("textures/gun_icon/fire_mode/semi.png");
    private static final ResourceLocation BURST = ModUtils.loc("textures/gun_icon/fire_mode/burst.png");
    private static final ResourceLocation AUTO = ModUtils.loc("textures/gun_icon/fire_mode/auto.png");
    private static final ResourceLocation TOP = ModUtils.loc("textures/gun_icon/fire_mode/top.png");
    private static final ResourceLocation DIR = ModUtils.loc("textures/gun_icon/fire_mode/dir.png");
    private static final ResourceLocation MOUSE = ModUtils.loc("textures/gun_icon/fire_mode/mouse.png");

    private static boolean creativeAmmo() {
        Player player = Minecraft.getInstance().player;
        int count = 0;
        if (player != null) {
            for (var inv : player.getInventory().items) {
                if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                    count++;
                }
            }
        }
        return count > 0;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (!DisplayConfig.AMMO_HUD.get()) return;

        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem gunItem) {
            PoseStack poseStack = event.getGuiGraphics().pose();

            // 渲染图标
            event.getGuiGraphics().blit(gunItem.getGunIcon(),
                    w - 135,
                    h - 40,
                    0,
                    0,
                    64,
                    16,
                    64,
                    16);

            // 渲染开火模式切换按键
            if (stack.getItem() != ModItems.MINIGUN.get()) {
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        "[" + ModKeyMappings.FIRE_MODE.getKey().getDisplayName().getString() + "]",
                        w - 111.5f,
                        h - 20,
                        0xFFFFFF,
                        false
                );
            }

            // 渲染开火模式
            ResourceLocation fireMode = getFireMode(stack);

            if (stack.getItem() == ModItems.JAVELIN.get()) {
                fireMode = stack.getOrCreateTag().getBoolean("TopMode") ? TOP : DIR;
            }

            if (stack.getItem() == ModItems.MINIGUN.get()) {
                fireMode = MOUSE;
                // 渲染加特林射速
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        GunsTool.getGunIntTag(stack, "RPM", 0) + " RPM",
                        w - 111f,
                        h - 20,
                        0xFFFFFF,
                        false
                );

                event.getGuiGraphics().blit(fireMode,
                        w - 126,
                        h - 22,
                        0,
                        0,
                        12,
                        12,
                        12,
                        12);
            } else {
                if (stack.getItem() != ModItems.TRACHELIUM.get()) {
                    event.getGuiGraphics().blit(fireMode,
                            w - 95,
                            h - 21,
                            0,
                            0,
                            8,
                            8,
                            8,
                            8);
                } else {
                    event.getGuiGraphics().drawString(
                            Minecraft.getInstance().font,
                            stack.getOrCreateTag().getBoolean("DA") ? Component.translatable("des.superbwarfare.revolver.sa").withStyle(ChatFormatting.BOLD) : Component.translatable("des.superbwarfare.revolver.da").withStyle(ChatFormatting.BOLD),
                            w - 96,
                            h - 20,
                            0xFFFFFF,
                            false
                    );
                }
            }

            if (stack.getItem() != ModItems.MINIGUN.get() && stack.getItem() != ModItems.TRACHELIUM.get()) {
                event.getGuiGraphics().blit(LINE,
                        w - 95,
                        h - 16,
                        0,
                        0,
                        8,
                        8,
                        8,
                        8);
            }

            // 渲染当前弹药量
            poseStack.pushPose();
            poseStack.scale(1.5f, 1.5f, 1f);

            if ((stack.getItem() == ModItems.MINIGUN.get() || stack.getItem() == ModItems.BOCEK.get()) && creativeAmmo()) {
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        "∞",
                        w / 1.5f - 64 / 1.5f,
                        h / 1.5f - 48 / 1.5f,
                        0xFFFFFF,
                        true
                );
            } else {
                event.getGuiGraphics().drawString(
                        Minecraft.getInstance().font,
                        getGunAmmoCount(player) + "",
                        w / 1.5f - 64 / 1.5f,
                        h / 1.5f - 48 / 1.5f,
                        0xFFFFFF,
                        true
                );
            }

            poseStack.popPose();

            // 渲染备弹量
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    getPlayerAmmoCount(player),
                    w - 64,
                    h - 35,
                    0xCCCCCC,
                    true
            );

            poseStack.pushPose();
            poseStack.scale(0.9f, 0.9f, 1f);

            // 渲染物品名称
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    centerString(gunItem.getGunDisplayName(), 20),
                    w / 0.9f - 144 / 0.9f,
                    h / 0.9f - 60 / 0.9f,
                    0xFFFFFF,
                    true
            );

            // 渲染弹药类型
            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    centerString(getGunAmmoType(stack), 20),
                    w / 0.9f - 144 / 0.9f,
                    h / 0.9f - 51 / 0.9f,
                    0xC8A679,
                    true
            );

            poseStack.popPose();
        }
    }

    private static ResourceLocation getFireMode(ItemStack stack) {
        return switch (GunsTool.getGunIntTag(stack, "FireMode")) {
            case 1 -> BURST;
            case 2 -> AUTO;
            default -> SEMI;
        };
    }

    private static int getGunAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.MINIGUN.get()) {
            return (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo;
        }

        if (stack.getItem() == ModItems.BOCEK.get()) {
            return stack.getOrCreateTag().getInt("max_ammo");
        }

        return GunsTool.getGunIntTag(stack, "Ammo", 0);
    }

    private static String getPlayerAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.MINIGUN.get() || stack.getItem() == ModItems.BOCEK.get()) {
            return "";
        }

        if (!creativeAmmo()) {
            if (stack.is(ModTags.Items.LAUNCHER) || stack.getItem() == ModItems.TASER.get()) {
                return "" + stack.getOrCreateTag().getInt("max_ammo");
            }
            if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                return "" + (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo;
            }
            if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                return "" + (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).handgunAmmo;
            }
            if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                return "" + (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).shotgunAmmo;
            }
            if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                return "" + (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).sniperAmmo;
            }
            return "";
        }

        return "∞";
    }

    private static String getGunAmmoType(ItemStack stack) {
        if (stack.getItem() == ModItems.BOCEK.get()) {
            return "   Arrow";
        }
        if (stack.getItem() == ModItems.M_79.get()) {
            return "40mm Grenade";
        }
        if (stack.getItem() == ModItems.RPG.get()) {
            return "Yassin105 TBG";
        }
        if (stack.getItem() == ModItems.JAVELIN.get()) {
            return "Javelin Missile";
        }
        if (stack.getItem() == ModItems.TASER.get()) {
            return "Electrode Rod";
        }
        if (stack.getItem() == ModItems.MINIGUN.get()) {
            return "Rifle Ammo";
        }
        if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
            return "  Rifle Ammo";
        }
        if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
            return "Handgun Ammo";
        }
        if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
            return "Shotgun Ammo";
        }
        if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
            return "Sniper Ammo";
        }
        return "";
    }

    private static String centerString(String string, int length) {
        int spaceBefore = (length - string.length()) / 2;
        int spaceAfter = length - string.length() - spaceBefore;

        return String.join("",
                repeatChar(spaceBefore),
                string,
                repeatChar(spaceAfter));
    }

    private static String repeatChar(int count) {
        return new String(new char[count]).replace('\0', ' ');
    }

}
