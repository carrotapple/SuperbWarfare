package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.Ammo;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerVariablesSyncMessage {
    private final int target;
    private final ModVariables.PlayerVariables data;

    public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
        this.data = new ModVariables.PlayerVariables();
        this.data.readNBT(buffer.readNbt());
        this.target = buffer.readInt();
    }

    public PlayerVariablesSyncMessage(ModVariables.PlayerVariables data, int entityId) {
        this.data = data;
        this.target = entityId;
    }

    public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) message.data.writeNBT());
        buffer.writeInt(message.target);
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

            ModVariables.PlayerVariables variables = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

            for (var type : Ammo.values()) {
                type.set(variables, type.get(message.data));
            }

            variables.tacticalSprint = message.data.tacticalSprint;
            variables.edit = message.data.edit;
        });
    }
}
