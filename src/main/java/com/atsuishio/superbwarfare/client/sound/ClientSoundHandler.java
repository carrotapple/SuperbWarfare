package com.atsuishio.superbwarfare.client.sound;

import com.atsuishio.superbwarfare.entity.LoudlyEntity;
import com.atsuishio.superbwarfare.entity.vehicle.A10Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Hpj11Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.TrackEntity;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientSoundHandler {

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        List<Entity> engineVehicle = SeekTool.getEntityWithinRange(player, player.level(), 384);

        for (var e : engineVehicle) {
            if (e instanceof MobileVehicleEntity mobileVehicle){
                if (!mobileVehicle.engineSoundStart) {
                    Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.EngineSound(mobileVehicle, mobileVehicle.getEngineSound()));
                    mobileVehicle.engineSoundStart = true;
                }
                if (!mobileVehicle.swimSoundStart) {
                    Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.SwimSound(mobileVehicle));
                    mobileVehicle.swimSoundStart = true;
                }
                if (mobileVehicle instanceof TrackEntity && !mobileVehicle.trackSoundStart) {
                    Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.TrackSound(mobileVehicle));
                    mobileVehicle.trackSoundStart = true;
                }
                if (!mobileVehicle.shootSoundStart) {
                    if (mobileVehicle instanceof A10Entity) {
                        Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.A10FireSound(mobileVehicle));
                    }
                    if (mobileVehicle instanceof Hpj11Entity) {
                        Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.HPJ11CloseFireSound(mobileVehicle));
                    }
                    mobileVehicle.shootSoundStart = true;
                }
            }
            if (e instanceof LoudlyEntity loudlyEntity && !loudlyEntity.isStarted()) {
                Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySound(e));
                Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySoundClose(e));
                loudlyEntity.setStart(true);
            }
        }
    }
}
