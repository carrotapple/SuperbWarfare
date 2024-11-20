package net.mcreator.superbwarfare.tools;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class EntityFindUtil {
    /**
     * 查找当前已知实体，对ClientLevel和ServerLevel均有效
     *
     * @param level      实体所在世界
     * @param uuidString 目标实体UUID字符串
     * @return 目标实体或null
     */
    public static Entity findEntity(Level level, String uuidString) {
        try {
            var uuid = UUID.fromString(uuidString);
            Entity target;

            if (level instanceof ServerLevel serverLevel) {
                target = serverLevel.getEntity(uuid);
            } else {
                var clientLevel = (ClientLevel) level;
                target = clientLevel.getEntities().get(uuid);
            }
            return target;
        } catch (Exception ignored) {
        }

        return null;
    }

    public static Player findPlayer(Level level, String uuidString) {
        var target = findEntity(level, uuidString);
        if (target instanceof Player player) {
            return player;
        }

        return null;
    }

    public static DroneEntity findDrone(Level level, String uuidString) {
        var target = findEntity(level, uuidString);
        if (target instanceof DroneEntity drone) {
            return drone;
        }

        return null;
    }

}
