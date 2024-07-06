package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunReloadResultMessage {
    private final int need_to_add;

    public GunReloadResultMessage(int need_to_add) {
        this.need_to_add = need_to_add;
    }

    public static void encode(GunReloadResultMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(message.need_to_add);
    }

    public static GunReloadResultMessage decode(FriendlyByteBuf byteBuf) {
        return new GunReloadResultMessage(byteBuf.readInt());
    }

    public static void handler(GunReloadResultMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            ItemStack stack = player.getMainHandItem();
            if (!stack.is(TargetModTags.Items.GUN)) {
                return;
            }
            var tag = stack.getOrCreateTag();

            tag.putInt("ammo", message.need_to_add);
            tag.putBoolean("is_normal_reloading", false);
            tag.putBoolean("is_empty_reloading", false);
        });
        context.get().setPacketHandled(true);
    }

}
