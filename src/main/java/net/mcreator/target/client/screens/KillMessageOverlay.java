package net.mcreator.target.client.screens;

import net.mcreator.target.TargetMod;
import net.mcreator.target.event.KillMessageHandler;
import net.mcreator.target.item.gun.GunItem;
import net.mcreator.target.tools.PlayerKillRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KillMessageOverlay {
    private static final ResourceLocation HEADSHOT = new ResourceLocation(TargetMod.MODID, "textures/screens/headshot.png");

    private static final ResourceLocation KNIFE = new ResourceLocation(TargetMod.MODID, "textures/screens/knife.png");
    private static final ResourceLocation EXPLOSION = new ResourceLocation(TargetMod.MODID, "textures/screens/explosion.png");
    private static final ResourceLocation CLAYMORE = new ResourceLocation(TargetMod.MODID, "textures/screens/claymore.png");

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

        String targetName = record.target.getDisplayName().getString();
        int targetNameWidth = font.width(targetName);

        // 击杀提示是右对齐的，这里从右向左渲染

        // 渲染被击杀者名称
        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                targetName,
                w - targetNameWidth - 10f,
                h,
                record.target.getTeamColor(),
                false
        );

        // 渲染爆头图标
        if (record.headshot) {
            event.getGuiGraphics().blit(HEADSHOT,
                    w - targetNameWidth - 28,
                    h - 2,
                    0,
                    0,
                    12,
                    12,
                    12,
                    12
            );
        }

        // 如果是爆炸伤害，则渲染爆炸图标
        boolean explosion = false;
        if (record.damageType == DamageTypes.EXPLOSION || record.damageType == DamageTypes.PLAYER_EXPLOSION) {
            explosion = true;
            int explosionW = w - targetNameWidth - 26;
            event.getGuiGraphics().blit(EXPLOSION,
                    explosionW,
                    h - 2,
                    0,
                    0,
                    12,
                    12,
                    12,
                    12
            );
        }

        // 如果是枪械击杀，则渲染枪械图标
        if (record.stack.getItem() instanceof GunItem gunItem) {
            ResourceLocation resourceLocation = gunItem.getGunIcon();
            int gunIconW = (record.headshot || explosion) ? w - targetNameWidth - 64 : w - targetNameWidth - 46;
            event.getGuiGraphics().blit(resourceLocation,
                    gunIconW,
                    h,
                    0,
                    0,
                    32,
                    8,
                    -32,
                    8
            );
        }

        // 渲染击杀者名称
        String attackerName = record.attacker.getDisplayName().getString();
        int attackerNameWidth = font.width(attackerName);
        int nameW = (record.headshot || explosion) ? w - targetNameWidth - 68 - attackerNameWidth : w - targetNameWidth - 50 - attackerNameWidth;

        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                attackerName,
                nameW,
                h,
                record.attacker.getTeamColor(),
                false
        );

    }
}
