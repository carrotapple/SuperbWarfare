package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
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

        if (type == 0) {
            changeFireMode(player);
        }
    }

    public static void changeFireMode(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem gunItem) {
            CompoundTag data = stack.getOrCreateTag().getCompound("GunData");
            int fireMode = data.getInt("FireMode");

            CompoundTag tag = stack.getOrCreateTag();

            int mode = gunItem.getFireMode();
            mode &= 0b111;

            if (fireMode == 0) {
                if ((mode & 2) != 0) {
                    GunsTool.setGunIntTag(stack, "FireMode", 1);
                    playChangeModeSound(player);
                    return;
                }
                if ((mode & 4) != 0) {
                    GunsTool.setGunIntTag(stack, "FireMode", 2);
                    playChangeModeSound(player);
                    return;
                }
            }

            if (fireMode == 1) {
                if ((mode & 4) != 0) {
                    GunsTool.setGunIntTag(stack, "FireMode", 2);
                    playChangeModeSound(player);
                    return;
                }
                if ((mode & 1) != 0) {
                    GunsTool.setGunIntTag(stack, "FireMode", 0);
                    playChangeModeSound(player);
                    return;
                }
            }

            if (fireMode == 2) {
                if ((mode & 1) != 0) {
                    GunsTool.setGunIntTag(stack, "FireMode", 0);
                    playChangeModeSound(player);
                    return;
                }
                if ((mode & 2) != 0) {
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

                for (var cell : player.getInventory().items) {
                    if (cell.is(ModItems.CELL.get())) {
                        AtomicBoolean flag = new AtomicBoolean(false);
                        cell.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                                iEnergyStorage -> flag.set(iEnergyStorage.getEnergyStored() >= 0)
                        );

                        if (flag.get()) {
                            tag.putBoolean("start_sentinel_charge", true);
                        }
                    }
                }
            }

            if (stack.getItem() == ModItems.JAVELIN.get()) {
                tag.putBoolean("TopMode", !tag.getBoolean("TopMode"));
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSoundPacket(new Holder.Direct<>(ModSounds.CANNON_ZOOM_OUT.get()),
                            SoundSource.PLAYERS, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1f, 1f, serverPlayer.level().random.nextLong()));
                }
            }

            if (stack.getItem() == ModItems.TRACHELIUM.get() && !GunsTool.getGunBooleanTag(stack, "NeedBoltAction", false)) {
                tag.putBoolean("DA", !tag.getBoolean("DA"));
                if (!tag.getBoolean("canImmediatelyShoot")) {
                    GunsTool.setGunBooleanTag(stack, "NeedBoltAction", true);
                }
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
