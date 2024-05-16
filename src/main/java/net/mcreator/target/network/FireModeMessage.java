package net.mcreator.target.network;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FireModeMessage {
    private final int type;

    public FireModeMessage(int type) {
        this.type = type;
    }

    public FireModeMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(FireModeMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(FireModeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (player == null) return;
        // security measure to prevent arbitrary chunk generation
        if (!player.level().hasChunkAt(player.blockPosition()))
            return;
        if (type == 0) {
            changeFireMode(player);
        }
    }

    private static void setFireMode(Player player, CompoundTag tag, int mode) {
        if (player.getServer() == null) return;
        var text = switch (mode) {
            case 0 -> "Semi";
            case 1 -> "Burst";
            case 2 -> "Auto";
            default -> "";
        };

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(TargetModSounds.FIRERATE.get()),
                    SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
        }

        tag.putInt("firemode", mode);
        tag.putDouble("cg", 10);
    }

    public static void changeFireMode(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();
        Item item = mainHandItem.getItem();
        int fireMode = (int) tag.getInt("firemode");

        if (item == TargetModItems.AK_47.get()
                || item == TargetModItems.M_4.get()
                || item == TargetModItems.AA_12.get()
                || item == TargetModItems.HK_416.get()
                || item == TargetModItems.RPK.get()
                || item == TargetModItems.MK_14.get()) {
            setFireMode(player, tag, fireMode == 0 ? 2 : 0);
        }
        if (item == TargetModItems.VECTOR.get()) {
            setFireMode(player, tag, (fireMode + 1) % 3);
        }
        if (item == TargetModItems.SENTINEL.get() && !(player.getCooldowns().isOnCooldown(item)) && tag.getDouble("charging") == 0) {
            tag.putDouble("charging", 1);
            tag.putDouble("cid", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
            tag.putDouble("chargingtime", 128);
        }
    }
}
