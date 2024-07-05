package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
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
        if (!player.level().isLoaded(player.blockPosition()))
            return;
        if (type == 0) {
            changeFireMode(player);
        }
    }

    private static void setFireMode(Player player, CompoundTag tag) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(TargetModSounds.FIRE_RATE.get()),
                    SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
        }
        tag.putDouble("cg", 10);
    }

    public static void changeFireMode(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();
        Item item = mainHandItem.getItem();
        int fireMode = tag.getInt("fire_mode");

        if (mainHandItem.is(TargetModTags.Items.GUN)) {
            if (tag.getInt("fire_mode") == 0) {
                if (tag.getDouble("burst") == 1) {
                    tag.putInt("fire_mode", 1);
                    setFireMode(player, tag);
                    return;
                }
                if (tag.getDouble("auto") == 1) {
                    tag.putInt("fire_mode", 2);
                    setFireMode(player, tag);
                    return;
                }
            }
            if (tag.getInt("fire_mode") == 1) {
                if (tag.getDouble("auto") == 1) {
                    tag.putInt("fire_mode", 2);
                    setFireMode(player, tag);
                    return;
                }
                if (tag.getDouble("semi") == 1) {
                    tag.putInt("fire_mode", 0);
                    setFireMode(player, tag);
                    return;
                }
            }
            if (tag.getInt("fire_mode") == 2) {
                if (tag.getDouble("semi") == 1) {
                    tag.putInt("fire_mode", 0);
                    setFireMode(player, tag);
                    return;
                }
                if (tag.getDouble("burst") == 1) {
                    tag.putInt("fire_mode", 1);
                    setFireMode(player, tag);
                    return;
                }
            }
        }

        if (item == TargetModItems.SENTINEL.get() && !(player.getCooldowns().isOnCooldown(item)) && !tag.getBoolean("charging") && !tag.getBoolean("reloading")) {
            tag.putBoolean("charging", true);
            tag.putDouble("cid", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
            tag.putDouble("charging_time", 128);
        }
    }
}
