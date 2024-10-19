package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EditMessage {
    private final int type;

    public EditMessage(int type) {
        this.type = type;
    }

    public EditMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void encode(net.mcreator.superbwarfare.network.message.EditMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(net.mcreator.superbwarfare.network.message.EditMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (player == null) return;
        if (!player.level().isLoaded(player.blockPosition()))
            return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.GUN) && player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
            switch (type) {
                case 0:
                    stack.getOrCreateTag().putInt("scope_type", stack.getOrCreateTag().getInt("scope_type") + 1);
                    if (stack.getOrCreateTag().getInt("scope_type") == 4) {
                        stack.getOrCreateTag().putInt("scope_type", 0);
                    }
                    break;
                case 1:
                    stack.getOrCreateTag().putInt("barrel_type", stack.getOrCreateTag().getInt("barrel_type") + 1);
                    if (stack.getOrCreateTag().getInt("barrel_type") == 3) {
                        stack.getOrCreateTag().putInt("barrel_type", 0);
                    }
                    break;
                case 2:
                    stack.getOrCreateTag().putInt("magazine_type", stack.getOrCreateTag().getInt("magazine_type") + 1);
                    if (stack.getOrCreateTag().getInt("magazine_type") == 3) {
                        stack.getOrCreateTag().putInt("magazine_type", 0);
                    }
                    break;
                case 3:
                    stack.getOrCreateTag().putInt("stock_type", stack.getOrCreateTag().getInt("stock_type") + 1);
                    if (stack.getOrCreateTag().getInt("stock_type") == 3) {
                        stack.getOrCreateTag().putInt("stock_type", 0);
                    }
                    break;
            }
        }
    }
}


