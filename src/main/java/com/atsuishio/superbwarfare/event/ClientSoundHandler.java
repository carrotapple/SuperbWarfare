package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.client.LoudlyEntitySoundInstance;
import com.atsuishio.superbwarfare.client.VehicleSoundInstance;
import com.atsuishio.superbwarfare.entity.LoudlyEntity;
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
            }
            if (event.getEntity() instanceof MobileVehicleEntity mobileVehicle && mobileVehicle instanceof TrackEntity) {
                Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.TrackSound(mobileVehicle));
            }

            if (event.getEntity() instanceof LoudlyEntity loudlyEntity) {
                Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySound(event.getEntity()));
                if (loudlyEntity.getCloseSound() != null) {
                    Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySoundClose(event.getEntity()));
                }
            }
        }
    }
}
