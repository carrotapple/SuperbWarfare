package net.mcreator.target.client.screens;

import net.mcreator.target.TargetMod;
import net.mcreator.target.event.KillMessageHandler;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.tools.PlayerKillRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KillMessageOverlay {
    private static final ResourceLocation HEADSHOT = new ResourceLocation(TargetMod.MODID, "textures/screens/headshot.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (KillMessageHandler.QUEUE.isEmpty()) {
            return;
        }

        int index = 0;
        for (PlayerKillRecord record : KillMessageHandler.QUEUE) {
            renderKillMessages(record, event, index);
            index++;
        }

    }

    private static void renderKillMessages(PlayerKillRecord record, RenderGuiEvent.Pre event, int index) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = 10 + index * 10;

        Font font = Minecraft.getInstance().font;

        String attackerName = record.attacker.getDisplayName().getString();
        String targetName = record.target.getDisplayName().getString();

        int attackerNameWidth = font.width(attackerName);
        int targetNameWidth = font.width(targetName);

        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                attackerName,
                w - targetNameWidth - 50 - attackerNameWidth,
                h,
                record.attacker.getTeamColor(),
                false
        );

        if (record.stack.getItem() instanceof GunItem gunItem) {
            ResourceLocation resourceLocation = gunItem.getGunIcon();

            event.getGuiGraphics().blit(resourceLocation,
                    w - targetNameWidth - 46,
                    h,
                    0,
                    0,
                    32,
                    8,
                    -32,
                    8
            );
        }

        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                targetName,
                w - targetNameWidth - 10f,
                h,
                record.target.getTeamColor(),
                false
        );

        if (record.headshot) {
            event.getGuiGraphics().blit(HEADSHOT,
                    w - targetNameWidth - 65 - attackerNameWidth,
                    h,
                    0,
                    0,
                    8,
                    8,
                    8,
                    8
            );
        }
    }
}
