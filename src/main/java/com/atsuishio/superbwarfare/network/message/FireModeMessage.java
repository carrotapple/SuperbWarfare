package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

    public static FireModeMessage decode(FriendlyByteBuf buffer) {
        return new FireModeMessage(buffer.readInt());
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

    public static void changeFireMode(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        CompoundTag data = stack.getOrCreateTag().getCompound("GunData");
        int fireMode = data.getInt("FireMode");

        CompoundTag tag = stack.getOrCreateTag();

        if (fireMode == 0) {
            if (tag.getBoolean("burst")) {
                GunsTool.setGunIntTag(stack, "FireMode", 1);
                playChangeModeSound(player);
                return;
            }
            if (tag.getBoolean("auto")) {
                GunsTool.setGunIntTag(stack, "FireMode", 2);
                playChangeModeSound(player);
                return;
            }
        }

        if (fireMode == 1) {
            if (tag.getBoolean("auto")) {
                GunsTool.setGunIntTag(stack, "FireMode", 2);
                playChangeModeSound(player);
                return;
            }
            if (tag.getBoolean("semi")) {
                GunsTool.setGunIntTag(stack, "FireMode", 0);
                playChangeModeSound(player);
                return;
            }
        }

        if (fireMode == 2) {
            if (tag.getBoolean("semi")) {
                GunsTool.setGunIntTag(stack, "FireMode", 0);
                playChangeModeSound(player);
                return;
            }
            if (tag.getBoolean("burst")) {
                GunsTool.setGunIntTag(stack, "FireMode", 1);
                playChangeModeSound(player);
                return;
            }
        }

        if (stack.getItem() == ModItems.SENTINEL.get()
                && !player.isSpectator()
                && !(player.getCooldowns().isOnCooldown(stack.getItem()))
                && GunsTool.getGunIntTag(stack, "ReloadTime") == 0
                && !stack.getOrCreateTag().getBoolean("sentinel_is_charging")) {

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

        if (stack.getItem() == ModItems.JAVELIN.get()) {
            tag.putBoolean("TopMode", !tag.getBoolean("TopMode"));
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ModSounds.CANNON_ZOOM_OUT.get()),
                        SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
            }
        }

        if (stack.getItem() == ModItems.TRACHELIUM.get() && !tag.getBoolean("need_bolt_action")) {
            if (!tag.getBoolean("DA")) {
                tag.putBoolean("DA", true);
                player.displayClientMessage(Component.translatable("des.superbwarfare.revolver.sa").withStyle(ChatFormatting.BOLD), true);
            } else {
                tag.putBoolean("DA", false);
                player.displayClientMessage(Component.translatable("des.superbwarfare.revolver.da").withStyle(ChatFormatting.BOLD), true);
            }
            if (!tag.getBoolean("canImmediatelyShoot")) {
                tag.putBoolean("need_bolt_action", true);
            }
        }

    }

    private static void playChangeModeSound(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ModSounds.FIRE_RATE.get()),
                    SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
        }
    }
}
