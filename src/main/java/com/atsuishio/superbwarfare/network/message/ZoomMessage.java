package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.ICannonEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ZoomMessage {
    private final int type;

    public ZoomMessage(int type) {
        this.type = type;
    }

    public static ZoomMessage decode(FriendlyByteBuf buffer) {
        return new ZoomMessage(buffer.readInt());
    }

    public static void encode(ZoomMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ZoomMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                ItemStack stack = player.getMainHandItem();
                Level level = player.level();

                if (!level.isLoaded(player.blockPosition())) {
                    return;
                }

                if (message.type == 0) {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zoom = true;
                        capability.edit = false;
                        capability.syncPlayerVariables(player);
                    });

                    if (player.isPassenger() && player.getVehicle() instanceof ICannonEntity) {
                        SoundTool.playLocalSound(player, ModSounds.CANNON_ZOOM_IN.get(), 2, 1);
                    }
                }

                if (message.type == 1) {
                    player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.zoom = false;
                        capability.breath = false;
                        capability.syncPlayerVariables(player);
                    });

                    if (player.isPassenger() && player.getVehicle() instanceof ICannonEntity) {
                        SoundTool.playLocalSound(player, ModSounds.CANNON_ZOOM_OUT.get(), 2, 1);
                    }

                    if (player.getMainHandItem().getItem() == ModItems.JAVELIN.get()) {
                        var handItem = player.getMainHandItem();
                        var tag = handItem.getOrCreateTag();
                        tag.putBoolean("Seeking",false);
                        tag.putInt("SeekTime",0);
                        tag.putString("TargetEntity","none");
                        var clientboundstopsoundpacket = new ClientboundStopSoundPacket(new ResourceLocation(ModUtils.MODID, "javelin_lock"), SoundSource.PLAYERS);
                        player.connection.send(clientboundstopsoundpacket);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
