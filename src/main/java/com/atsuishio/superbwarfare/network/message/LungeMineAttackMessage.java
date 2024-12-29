package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.LungeMine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LungeMineAttackMessage {

    private final int type;

    public LungeMineAttackMessage(int type) {
        this.type = type;
    }

    public static LungeMineAttackMessage decode(FriendlyByteBuf buffer) {
        return new LungeMineAttackMessage(buffer.readInt());
    }

    public static void encode(LungeMineAttackMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(LungeMineAttackMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();

                ItemStack stack = player.getMainHandItem();

                if (stack.is(ModItems.LUNGE_MINE.get())) {
                    if (message.type == 0) {
                        LungeMine.attack(stack,player);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
