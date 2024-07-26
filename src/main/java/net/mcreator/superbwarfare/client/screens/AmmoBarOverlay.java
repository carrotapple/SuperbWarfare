package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModKeyMappings;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.minecraft.client.Minecraft;
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
    //    private static final ResourceLocation BUTTON = new ResourceLocation(TargetMod.MODID, "textures/gun_icon/fire_mode/button.png");
    private static final ResourceLocation LINE = new ResourceLocation(ModUtils.MODID, "textures/gun_icon/fire_mode/line.png");
    private static final ResourceLocation SEMI = new ResourceLocation(ModUtils.MODID, "textures/gun_icon/fire_mode/semi.png");
    private static final ResourceLocation BURST = new ResourceLocation(ModUtils.MODID, "textures/gun_icon/fire_mode/burst.png");
    private static final ResourceLocation AUTO = new ResourceLocation(ModUtils.MODID, "textures/gun_icon/fire_mode/auto.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

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
//            event.getGuiGraphics().blit(BUTTON,
//                    w - 115,
//                    h - 20,
//                    0,
//                    0,
//                    10,
//                    10,
//                    10,
//                    10);

            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    "[" + ModKeyMappings.FIRE_MODE.getKey().getDisplayName().getString() + "]",
                    w - 111.5f,
                    h - 20,
                    0xFFFFFF,
                    false
            );

            // 渲染开火模式
            ResourceLocation fireMode = getFireMode(stack);

            event.getGuiGraphics().blit(fireMode,
                    w - 95,
                    h - 21,
                    0,
                    0,
                    8,
                    8,
                    8,
                    8);

            event.getGuiGraphics().blit(LINE,
                    w - 95,
                    h - 16,
                    0,
                    0,
                    8,
                    8,
                    8,
                    8);


            // 渲染当前弹药量
            poseStack.pushPose();
            poseStack.scale(1.5f, 1.5f, 1f);

            event.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    getGunAmmoCount(player) + "",
                    w / 1.5f - 64 / 1.5f,
                    h / 1.5f - 48 / 1.5f,
                    0xFFFFFF,
                    true
            );
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
        return switch (stack.getOrCreateTag().getInt("fire_mode")) {
            case 1 -> BURST;
            case 2 -> AUTO;
            default -> SEMI;
        };
    }

    private static int getGunAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.MINIGUN.get()) {
            return (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo;
        }

        if (stack.getItem() == ModItems.BOCEK.get()) {
            return stack.getOrCreateTag().getInt("max_ammo");
        }

        return stack.getOrCreateTag().getInt("ammo");
    }

    private static String getPlayerAmmoCount(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() == ModItems.MINIGUN.get() || stack.getItem() == ModItems.BOCEK.get()) {
            return "";
        }

        if (stack.getItem() == ModItems.M_79.get() || stack.getItem() == ModItems.RPG.get() || stack.getItem() == ModItems.TASER.get()) {
            return "" + stack.getOrCreateTag().getInt("max_ammo");
        }

        if (stack.is(ModTags.Items.RIFLE)) {
            return "" + (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo;
        }
        if (stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) {
            return "" + (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo;
        }
        if (stack.is(ModTags.Items.SHOTGUN)) {
            return "" + (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo;
        }
        if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
            return "" + (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo;
        }
        return "";
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

        if (stack.getItem() == ModItems.TASER.get()) {
            return "Electrode Rod";
        }

        if (stack.getItem() == ModItems.MINIGUN.get()) {
            return "Rifle Ammo";
        }

        if (stack.is(ModTags.Items.RIFLE)) {
            return "  Rifle Ammo";
        }
        if (stack.is(ModTags.Items.HANDGUN) || stack.is(ModTags.Items.SMG)) {
            return "Handgun Ammo";
        }
        if (stack.is(ModTags.Items.SHOTGUN)) {
            return "Shotgun Ammo";
        }
        if (stack.is(ModTags.Items.SNIPER_RIFLE)) {
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
