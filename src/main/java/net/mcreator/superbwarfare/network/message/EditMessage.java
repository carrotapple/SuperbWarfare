package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.nbt.CompoundTag;
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

    public static EditMessage decode(FriendlyByteBuf buffer) {
        return new EditMessage(buffer.readInt());
    }

    public static void encode(EditMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(EditMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (player == null) return;
        if (!player.level().isLoaded(player.blockPosition()))
            return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.CAN_CUSTOM_GUN) && player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).edit) {
            CompoundTag tag = stack.getOrCreateTag().getCompound("Attachments");
            switch (type) {
                case 0 -> {
                    int att = tag.getInt("Scope");
                    att++;
                    att %= 4;
                    tag.putInt("Scope", att);
                }
                case 1 -> {
                    int att = tag.getInt("Barrel");
                    att++;
                    att %= 3;
                    tag.putInt("Barrel", att);
                }
                case 2 -> {
                    int att = tag.getInt("Magazine");
                    att++;
                    att %= 3;
                    tag.putInt("Magazine", att);
                }
                case 3 -> {
                    int att = tag.getInt("Stock");
                    att++;
                    att %= 3;
                    tag.putInt("Stock", att);
                }
            }
            stack.addTagElement("Attachments", tag);
            SoundTool.playLocalSound(player, ModSounds.EDIT.get(), 1f, 1f);
        }
    }
}


