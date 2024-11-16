package net.mcreator.superbwarfare.client.screens;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.config.client.DisplayConfig;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ArmorPlateOverlay {

    private static final ResourceLocation ICON = ModUtils.loc("textures/screens/armor_plate_icon.png");
    private static final ResourceLocation LEVEL1 = ModUtils.loc("textures/screens/armor_plate_level1.png");
    private static final ResourceLocation LEVEL2 = ModUtils.loc("textures/screens/armor_plate_level2.png");
    private static final ResourceLocation LEVEL3 = ModUtils.loc("textures/screens/armor_plate_level3.png");
    private static final ResourceLocation LEVEL1_FRAME = ModUtils.loc("textures/screens/armor_plate_level1_frame.png");
    private static final ResourceLocation LEVEL2_FRAME = ModUtils.loc("textures/screens/armor_plate_level2_frame.png");
    private static final ResourceLocation LEVEL3_FRAME = ModUtils.loc("textures/screens/armor_plate_level3_frame.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (!DisplayConfig.ARMOR_PLATE_HUD.get()) return;

        int h = event.getWindow().getGuiScaledHeight();
        GuiGraphics guiGraphics = event.getGuiGraphics();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (stack == ItemStack.EMPTY) return;
        if (stack.getTag() == null || !stack.getTag().contains("ArmorPlate")) return;

        double amount = 2 * stack.getTag().getDouble("ArmorPlate");

        int armorLevel = 1;
        if (stack.is(ModTags.Items.MILITARY_ARMOR)) {
            armorLevel = 2;
        } else if (stack.is(ModTags.Items.MILITARY_ARMOR_HEAVY)) {
            armorLevel = 3;
        }

        ResourceLocation texture = switch (armorLevel) {
            case 2 -> LEVEL2;
            case 3 -> LEVEL3;
            default -> LEVEL1;
        };
        ResourceLocation frame = switch (armorLevel) {
            case 2 -> LEVEL2_FRAME;
            case 3 -> LEVEL3_FRAME;
            default -> LEVEL1_FRAME;
        };

        int length = armorLevel * 30;

        guiGraphics.pose().pushPose();
        // 渲染图标
        guiGraphics.blit(ICON, 10, h - 16, 0, 0, 8, 8, 8, 8);

        // 渲染框架
        guiGraphics.blit(frame, 20, h - 15, 0, 0, length, 6, length, 6);

        // 渲染盔甲值
        guiGraphics.blit(texture, 20, h - 15, 0, 0, (int) amount, 6, length, 6);

        guiGraphics.pose().popPose();

    }
}
