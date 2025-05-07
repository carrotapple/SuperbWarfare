package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.client.LoudlyEntitySoundInstance;
import com.atsuishio.superbwarfare.client.VehicleFireSoundInstance;
import com.atsuishio.superbwarfare.client.VehicleSoundInstance;
import com.atsuishio.superbwarfare.entity.LoudlyEntity;
import com.atsuishio.superbwarfare.entity.vehicle.A10Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Hpj11Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.TrackEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientSoundHandler {

    @SubscribeEvent
    public static void handleJoinLevelEvent(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            if (event.getEntity() instanceof MobileVehicleEntity mobileVehicle) {
                Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.EngineSound(mobileVehicle, mobileVehicle.getEngineSound()));
                Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.SwimSound(mobileVehicle));
            }
            if (event.getEntity() instanceof MobileVehicleEntity mobileVehicle && mobileVehicle instanceof TrackEntity) {
                Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.TrackSound(mobileVehicle));
            }
            if (event.getEntity() instanceof LoudlyEntity) {
                Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySound(event.getEntity()));
                Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySoundClose(event.getEntity()));
            }
            if (event.getEntity() instanceof MobileVehicleEntity mobileVehicle && mobileVehicle instanceof A10Entity) {
                Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.A10FireSound(mobileVehicle));
            }
            if (event.getEntity() instanceof MobileVehicleEntity mobileVehicle && mobileVehicle instanceof Hpj11Entity) {
                Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.HPJ11CloseFireSound(mobileVehicle));
            }
        }
    }
}
