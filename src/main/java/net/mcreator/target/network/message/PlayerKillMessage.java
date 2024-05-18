package net.mcreator.target.network.message;

import net.mcreator.target.network.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerKillMessage {
    public final int attackerId;
    public final int targetId;

    public PlayerKillMessage(int attackerId, int targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    public static void encode(PlayerKillMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.attackerId);
        buffer.writeInt(message.targetId);
    }

    public static PlayerKillMessage decode(FriendlyByteBuf buffer) {
        int attackerId = buffer.readInt();
        int targetId = buffer.readInt();
        return new PlayerKillMessage(attackerId, targetId);
    }

    public static void handler(PlayerKillMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                Player player = level.getEntity(message.attackerId) instanceof Player ? (Player) level.getEntity(message.attackerId) : null;
                Entity target = level.getEntity(message.targetId);

                if (player != null && target != null) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePlayerKillMessage(player, target, ctx));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
