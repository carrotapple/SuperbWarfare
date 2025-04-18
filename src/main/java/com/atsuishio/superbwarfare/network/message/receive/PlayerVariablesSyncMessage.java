package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.PlayerVariable;
import com.atsuishio.superbwarfare.tools.Ammo;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerVariablesSyncMessage {
    private final int target;
    private final PlayerVariable data;

    public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
        this.target = buffer.readVarInt();
        this.data = new PlayerVariable();

        while (buffer.isReadable()) {
            var type = buffer.readByte();
            switch (type) {
                case -1 -> this.data.tacticalSprint = buffer.readBoolean();
                case -2 -> this.data.edit = buffer.readBoolean();
                default -> {
                    if (type < Ammo.values().length) {
                        var ammoType = Ammo.values()[type];
                        ammoType.set(this.data, buffer.readVarInt());
                    }
                }
            }
        }
    }

    public PlayerVariablesSyncMessage(PlayerVariable data, int entityId) {
        this.data = data;
        this.target = entityId;
    }

    public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.target);
        for (var type : Ammo.values()) {
            buffer.writeByte(type.ordinal());
            buffer.writeVarInt(type.get(message.data));
        }
        buffer.writeByte(-1);
        buffer.writeBoolean(message.data.tacticalSprint);
        buffer.writeByte(-2);
        buffer.writeBoolean(message.data.edit);
    }

    public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            context.setPacketHandled(true);
            if (context.getDirection().getReceptionSide().isServer() || Minecraft.getInstance().player == null) {
                return;
            }

            var entity = Minecraft.getInstance().player.level().getEntity(message.target);
            if (entity == null) {
                return;
            }

            PlayerVariable variables = entity.getCapability(ModVariables.PLAYER_VARIABLE, null).orElse(new PlayerVariable());

            for (var type : Ammo.values()) {
                type.set(variables, type.get(message.data));
            }

            variables.tacticalSprint = message.data.tacticalSprint;
            variables.edit = message.data.edit;
        });
    }
}
