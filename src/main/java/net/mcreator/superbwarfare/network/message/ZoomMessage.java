package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.mcreator.superbwarfare.init.TargetModItems;
import net.mcreator.superbwarfare.init.TargetModSounds;
import net.mcreator.superbwarfare.init.TargetModTags;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ZoomMessage {
    private final int type;

    public static double zoom_spread = 1;

    public ZoomMessage(int type) {
        this.type = type;
    }

    public ZoomMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(ZoomMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ZoomMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type) {
        Level world = entity.level();

        if (!world.isLoaded(entity.blockPosition())) {
            return;
        }

        if (type == 0) {
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zoom = true;
                capability.syncPlayerVariables(entity);
            });

            ItemStack stack = entity.getMainHandItem();

            if (stack.is(TargetModTags.Items.SNIPER_RIFLE)) {
                zoom_spread = 0;
            } else if (stack.is(TargetModTags.Items.SHOTGUN)) {
                zoom_spread = 0.9;
            }  else if (stack.is(TargetModItems.MINIGUN.get())) {
                zoom_spread = 1;
            } else {
                zoom_spread = 0.00001;
            }

            if (entity.isPassenger() && entity.getVehicle() instanceof Mk42Entity) {
                if (entity instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.CANNON_ZOOM_IN.get(), 2, 1);
                }
            }
        }
        if (type == 1) {
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zoom = false;
                capability.syncPlayerVariables(entity);
            });
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = false;
                capability.syncPlayerVariables(entity);
            });

            zoom_spread = 1;
            if (entity.isPassenger() && entity.getVehicle() instanceof Mk42Entity) {
                if (entity instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.CANNON_ZOOM_OUT.get(), 2, 1);
                }
            }

        }
    }
}
