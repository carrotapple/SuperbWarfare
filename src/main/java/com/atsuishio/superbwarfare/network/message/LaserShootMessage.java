package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class LaserShootMessage {
    private final double damage;

    private final UUID uuid;

    public LaserShootMessage(double damage, UUID uuid) {
        this.damage = damage;
        this.uuid = uuid;
    }

    public static LaserShootMessage decode(FriendlyByteBuf buffer) {
        return new LaserShootMessage(buffer.readDouble(), buffer.readUUID());
    }

    public static void encode(LaserShootMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.damage);
        buffer.writeUUID(message.uuid);
    }

    public static void handler(LaserShootMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.damage, message.uuid);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, double damage, UUID uuid) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        Entity entity = EntityFindUtil.findEntity(level, String.valueOf(uuid));

        if (entity != null) {
            entity.hurt(ModDamageTypes.causeGunFireDamage(level.registryAccess(), player, player), (float) damage);
            entity.invulnerableTime = 0;
            if (player instanceof ServerPlayer serverPlayer) {
                player.level().playSound(null, player.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 0.1f, 1);
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientIndicatorMessage(0, 5));
            }
        }

    }
}
