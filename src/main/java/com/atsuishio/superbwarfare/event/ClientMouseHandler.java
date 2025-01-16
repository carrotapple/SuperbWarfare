package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientMouseHandler {
    public static Vec2 posO = new Vec2(0 , 0);
    public static Vec2 posN = new Vec2(0 , 0);
    public static Vec2 mousePos = new Vec2(0 , 0);
    public static double PosX = 0;
    public static double lerpPosX = 0;
    public static double PosY = 0;
    public static double lerpPosY = 0;

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        posO = posN;
        posN = MouseMovementHandler.getMousePos();

        if (!notInGame()) {
            mousePos = posN.add(posO.scale(-1));

            if (mousePos.x != 0) {
                lerpPosX = Mth.lerp(0.1,PosX,mousePos.x);
            }
            if (mousePos.y != 0) {
                lerpPosY = Mth.lerp(0.1,PosY,mousePos.y);
            }
        }

        lerpPosX = Mth.lerp(0.1,lerpPosX,0);
        lerpPosY = Mth.lerp(0.1,lerpPosY,0);

//        player.displayClientMessage(Component.literal(new DecimalFormat("##.##").format(lerpPosX)), true);
    }
}
