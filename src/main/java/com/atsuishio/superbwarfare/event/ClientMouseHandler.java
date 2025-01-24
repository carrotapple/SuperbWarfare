package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import com.atsuishio.superbwarfare.network.message.VehicleMovementMessage;
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

        lerpPosX = Mth.clamp(Mth.lerp(0.1,lerpPosX,0), -0.11, 0.11);
        lerpPosY = Mth.clamp(Mth.lerp(0.1,lerpPosY,0), -0.11, 0.11);

        int typeX = 0;

        if (lerpPosX < -0.05) {
            typeX = -1;
        } else if (lerpPosX > 0.05) {
            typeX = 1;
        }

        int typeY = 0;

        if (lerpPosY < -0.05) {
            typeY = 1;
        } else if (lerpPosY > 0.05) {
            typeY = -1;
        }

        if (typeX == -1) {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(7, true));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(8, false));
        } else if (typeX == 1) {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(7, false));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(8, true));
        } else {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(7, false));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(8, false));
        }


        if (typeY == 1) {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(9, true));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(10, false));
        } else if (typeY == -1) {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(9, false));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(10, true));
        } else {
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(9, false));
            ModUtils.PACKET_HANDLER.sendToServer(new VehicleMovementMessage(10, false));
        }

//        player.displayClientMessage(Component.literal(typeX + " " + typeY), true);
    }
}
