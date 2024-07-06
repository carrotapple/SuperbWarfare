package net.mcreator.target.network.message;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.procedures.PlayerReloadProcedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadMessage {
    private final int type;

    public ReloadMessage(int type) {
        this.type = type;
    }

    public ReloadMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(ReloadMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ReloadMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

        if (!world.hasChunkAt(entity.blockPosition()))
            return;
        if (type == 0) {
            PlayerReloadProcedure.execute(entity);

            ItemStack stack = entity.getMainHandItem();
            var capability = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables());

            if (!entity.isSpectator()
            && stack.is(TargetModTags.Items.GUN)
            && !capability.zooming
            && !(entity.getCooldowns().isOnCooldown(stack.getItem()))
            && entity.getPersistentData().getInt("gun_reloading_time") == 0
            ) {
                CompoundTag tag = stack.getOrCreateTag();

                if (stack.is(TargetModTags.Items.SHOTGUN) && capability.shotgunAmmo == 0) {
                    return;
                } else if (stack.is(TargetModTags.Items.SNIPER_RIFLE) && capability.sniperAmmo == 0) {
                    return;
                } else if ((stack.is(TargetModTags.Items.HANDGUN) || stack.is(TargetModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                    return;
                } else if (stack.is(TargetModTags.Items.RIFLE) && capability.rifleAmmo == 0) {
                    return;
                }

                if (stack.is(TargetModTags.Items.OPEN_BOLT) && (tag.getDouble("normal_reload_time") != 0 || tag.getDouble("empty_reload_time") != 0)) {
                    if(tag.getInt("ammo") < tag.getDouble("mag") + 1) {
                        entity.getPersistentData().putBoolean("start_reload",true);
                    }
                } else if (tag.getInt("ammo") < tag.getDouble("mag")){
                    entity.getPersistentData().putBoolean("start_reload",true);
                }
            }
        }
    }
}
