package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Supplier;

public class TacticalSprintMessage {
    private final boolean sprint;

    public TacticalSprintMessage(boolean sprint) {
        this.sprint = sprint;
    }

    public static TacticalSprintMessage decode(FriendlyByteBuf buffer) {
        return new TacticalSprintMessage(buffer.readBoolean());
    }

    public static void encode(TacticalSprintMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.sprint);
    }

    public static void handler(TacticalSprintMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                Player player = context.getSender();
                if (message.sprint) {
                    player.setSprinting(true);

                    if (!(player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).tacticalSprintExhaustion) {
                        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.tacticalSprint = true;
                            capability.syncPlayerVariables(player);
                        });
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
