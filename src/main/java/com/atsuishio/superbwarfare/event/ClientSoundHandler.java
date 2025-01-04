package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.entity.Ah6Entity;
import com.atsuishio.superbwarfare.entity.MobileVehicleEntity;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

import java.util.List;

import static com.atsuishio.superbwarfare.entity.MobileVehicleEntity.POWER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientSoundHandler {

    @SubscribeEvent
    public static void handleClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        var options = Minecraft.getInstance().options;
        if (player == null) {
            return;
        }

        List<Entity>  engineVehicle = SeekTool.getVehicleWithinRange(player, player.level(), 196);

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
                    distanceReduce = (float) (1 - distance / 64);
                    player.level().playLocalSound(BlockPos.containing(engineSoundPos), engineSound, mobileVehicle.getSoundSource(), 5 * (mobileVehicle.getEntityData().get(POWER) - 0.012f) * distanceReduce * distanceReduce, (float) ((2 * Math.random() - 1) * 0.1f + 1.0f), false);
//                    player.displayClientMessage(Component.literal("Angle:" + engineSoundPos), true);
                }
            }
        }
    }
}
