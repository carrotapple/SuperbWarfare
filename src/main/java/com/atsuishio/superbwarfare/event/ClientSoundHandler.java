package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.entity.vehicle.MobileVehicleEntity;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

import java.util.List;

import static com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity.PROPELLER_ROT;
import static com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity.DELTA_ROT;
import static com.atsuishio.superbwarfare.entity.vehicle.MobileVehicleEntity.POWER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientSoundHandler {

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        var options = Minecraft.getInstance().options;
        if (player == null) {
            return;
        }

        List<Entity>  engineVehicle = SeekTool.getVehicleWithinRange(player, player.level(), 192);

        for (var e : engineVehicle) {
            if (e instanceof MobileVehicleEntity mobileVehicle){

                Vec3 listener = player.getEyePosition();
                Vec3 engineRealPos = e.getEyePosition();
                Vec3 toVec = listener.vectorTo(engineRealPos).normalize();
                double distance = listener.distanceTo(engineRealPos);

                var engineSoundPos = new Vec3(listener.x + toVec.x, listener.y + toVec.y, listener.z + toVec.z);
                SoundEvent engineSound = mobileVehicle.getEngineSound();
                float distanceReduce;
                if (e instanceof Ah6Entity ah6Entity) {
                    distanceReduce = (float) Math.max((1 - distance / 128), 0);
                    if (player.getVehicle() == ah6Entity) {
                        player.playSound(ModSounds.HELICOPTER_ENGINE_1P.get(), 2 * (mobileVehicle.getEntityData().get(PROPELLER_ROT) - 0.012f), (float) ((2 * Math.random() - 1) * 0.1f + 1.0f));
                    } else {
                        player.level().playLocalSound(BlockPos.containing(engineSoundPos), engineSound, mobileVehicle.getSoundSource(), 5 * (mobileVehicle.getEntityData().get(PROPELLER_ROT) - 0.012f) * distanceReduce * distanceReduce, (float) ((2 * Math.random() - 1) * 0.1f + 1), false);
                    }
                }
                if (e instanceof Lav150Entity lav150) {
                    distanceReduce = (float) Math.max((1 - distance / 64), 0);
                    if (player.getVehicle() == lav150) {
                        player.playSound(ModSounds.LAV_ENGINE_1P.get(), 2 * (Mth.abs(mobileVehicle.getEntityData().get(POWER)) - 0.006f), (float) ((2 * Math.random() - 1) * 0.1f + 0.95f));
                    } else {
                        player.level().playLocalSound(BlockPos.containing(engineSoundPos), engineSound, mobileVehicle.getSoundSource(), 5 * (Mth.abs(mobileVehicle.getEntityData().get(POWER)) - 0.006f) * distanceReduce * distanceReduce, (float) ((2 * Math.random() - 1) * 0.1f + 1), false);
                    }
                }
                if (e instanceof Bmp2Entity bmp2) {
                    distanceReduce = (float) Math.max((1 - distance / 64), 0);
                    if (player.getVehicle() == bmp2) {
                        player.playSound(ModSounds.BMP_ENGINE_1P.get(), 2 * (Mth.abs(mobileVehicle.getEntityData().get(POWER)) + Mth.abs(0.08f * mobileVehicle.getEntityData().get(DELTA_ROT)) - 0.004f), (float) ((2 * Math.random() - 1) * 0.1f + 0.95f));
                    } else {
                        player.level().playLocalSound(BlockPos.containing(engineSoundPos), engineSound, mobileVehicle.getSoundSource(), 5 * (Mth.abs(mobileVehicle.getEntityData().get(POWER)) + Mth.abs(0.08f * mobileVehicle.getEntityData().get(DELTA_ROT)) - 0.004f) * distanceReduce * distanceReduce, (float) ((2 * Math.random() - 1) * 0.1f + 1), false);
                    }
                }
            }
        }
    }
}
