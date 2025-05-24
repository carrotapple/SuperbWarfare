package com.atsuishio.superbwarfare.client.sound;

import com.atsuishio.superbwarfare.entity.LoudlyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientSoundHandler {

    public static void playClientSoundInstance(Entity entity) {
        if (entity instanceof LoudlyEntity) {
            Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySound(entity));
            Minecraft.getInstance().getSoundManager().play(new LoudlyEntitySoundInstance.EntitySoundClose(entity));
        } else {
//            Mod.queueClientWork(60, () -> {
//                if (entity instanceof MobileVehicleEntity mobileVehicle) {
//                    if (mobileVehicle instanceof TrackEntity) {
//                        Minecraft.getInstance().getSoundManager().play(new VehicleSoundInstance.TrackSound(mobileVehicle));
//                    }
//                    if (mobileVehicle instanceof A10Entity) {
//                        Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.A10FireSound(mobileVehicle));
//                    }
//                    if (mobileVehicle instanceof Hpj11Entity) {
//                        Minecraft.getInstance().getSoundManager().play(new VehicleFireSoundInstance.HPJ11CloseFireSound(mobileVehicle));
//                    }
//                }
//            });
        }
    }
}
