package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
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

    public static void encode(FireModeMessage message, FriendlyByteBuf buffer) {
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
            serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ModSounds.FIRE_RATE.get()),
                    SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
        }
        tag.putDouble("cg", 10);
    }

    public static void changeFireMode(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();
        int fireMode = tag.getInt("fire_mode");

        if (mainHandItem.is(ModTags.Items.GUN)) {
            if (fireMode == 0) {
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
            if (fireMode == 1) {
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
            if (fireMode == 2) {
                if (tag.getDouble("semi") == 1) {
                    tag.putInt("fire_mode", 0);
                    setFireMode(player, tag);
                    return;
                }
                if (tag.getDouble("burst") == 1) {
                    tag.putInt("fire_mode", 1);
                    setFireMode(player, tag);
                }
            }


            if (mainHandItem.getItem() == ModItems.SENTINEL.get()
                    && !player.isSpectator()
                    && !(player.getCooldowns().isOnCooldown(mainHandItem.getItem()))
                    && mainHandItem.getOrCreateTag().getInt("gun_reloading_time") == 0
                    && !mainHandItem.getOrCreateTag().getBoolean("sentinel_is_charging")) {

                int count = 0;
                for (var inv : player.getInventory().items) {
                    if (inv.is(ModItems.SHIELD_CELL.get())) {
                        count++;
                    }
                }

                if (count > 0) {
                    tag.putBoolean("start_sentinel_charge", true);
                }
            }

            if (mainHandItem.getItem() == ModItems.JAVELIN.get()) {
                tag.putBoolean("TopMode", !tag.getBoolean("TopMode"));
            }
        }
    }
}
