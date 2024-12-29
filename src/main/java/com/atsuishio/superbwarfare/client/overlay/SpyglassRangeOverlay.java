package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.tools.TraceTool;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SpyglassRangeOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;
        if (player != null && (player.getMainHandItem().getItem() == Items.SPYGLASS || player.getOffhandItem().getItem() == Items.SPYGLASS) && player.isUsingItem()) {
            boolean lookAtEntity = false;
            double block_range = player.position().distanceTo((Vec3.atLowerCornerOf(player.level().clip(
                    new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(520)),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos())));

            double entity_range = 0;

            Entity lookingEntity = TraceTool.findLookingEntity(player, 520);

            if (lookingEntity != null) {
                lookAtEntity = true;
                entity_range = player.distanceTo(lookingEntity);
            }

            if (lookAtEntity) {
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                .append(Component.literal(new DecimalFormat("##.#").format(entity_range) + "M " + lookingEntity.getDisplayName().getString())),
                        w / 2 + 12, h / 2 - 28, -1, false);
            } else {
                if (block_range > 512) {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                            .append(Component.literal("---M")), w / 2 + 12, h / 2 - 28, -1, false);
                } else {
                    event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("des.superbwarfare.drone.range")
                                    .append(Component.literal(new DecimalFormat("##.#").format(block_range) + "M")),
                            w / 2 + 12, h / 2 - 28, -1, false);
                }
            }
        }
    }
}
