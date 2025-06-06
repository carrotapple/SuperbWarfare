package com.atsuishio.superbwarfare.compat.coldsweat;

import com.atsuishio.superbwarfare.compat.CompatHolder;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArmedVehicleEntity;
import com.momosoftworks.coldsweat.api.util.Temperature;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModList;

public class ColdSweatCompatHandler {

    public static void onPlayerInVehicle(TickEvent.PlayerTickEvent event) {
        var player = event.player;
        if (player == null) return;
        if (player.getVehicle() instanceof ArmedVehicleEntity vehicle && vehicle.hidePassenger(player)) {
            Temperature.set(player, Temperature.Trait.WORLD, 1);
        }
    }

    public static boolean hasMod() {
        return ModList.get().isLoaded(CompatHolder.COLD_SWEAT);
    }
}
