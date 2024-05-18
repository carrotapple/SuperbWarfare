package net.mcreator.target.event;

import net.mcreator.target.tools.PlayerKillRecord;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Queue;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KillMessageHandler {

    public static Queue<PlayerKillRecord> QUEUE = new ArrayDeque<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (PlayerKillRecord record : QUEUE) {
                record.tick++;
                if (record.tick >= 100) {
                    QUEUE.poll();
                }
            }
        }
    }

}
