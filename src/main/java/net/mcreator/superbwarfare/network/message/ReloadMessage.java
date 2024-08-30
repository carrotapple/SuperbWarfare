package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
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

    public static ReloadMessage decode(FriendlyByteBuf buffer) {
        return new ReloadMessage(buffer.readInt());
    }

    public static void encode(ReloadMessage message, FriendlyByteBuf buffer) {
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

    public static void pressAction(Player player, int type) {
        Level level = player.level();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        if (type == 0) {
            ItemStack stack = player.getMainHandItem();
            var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

            if (!player.isSpectator()
                    && stack.is(ModTags.Items.GUN)
                    && !stack.getOrCreateTag().getBoolean("sentinel_is_charging")
                    && !(player.getCooldowns().isOnCooldown(stack.getItem()))
                    && stack.getOrCreateTag().getInt("gun_reloading_time") == 0
            ) {
                CompoundTag tag = stack.getOrCreateTag();

                boolean canSingleReload = tag.getDouble("iterative_time") != 0;
                boolean canReload = (tag.getDouble("normal_reload_time") != 0 || tag.getDouble("empty_reload_time") != 0) && tag.getDouble("clipLoad") != 1;
                boolean clipLoad = tag.getInt("ammo") == 0 && tag.getDouble("clipLoad") == 1;

                // 检查备弹
                if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO) && capability.shotgunAmmo == 0) {
                    return;
                } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO) && capability.sniperAmmo == 0) {
                    return;
                } else if ((stack.is(ModTags.Items.USE_HANDGUN_AMMO) || stack.is(ModTags.Items.SMG)) && capability.handgunAmmo == 0) {
                    return;
                } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO) && capability.rifleAmmo == 0) {
                    return;
                } else if (stack.getItem() == ModItems.TASER.get() && tag.getInt("max_ammo") == 0) {
                    return;
                } else if (stack.getItem() == ModItems.M_79.get() && tag.getInt("max_ammo") == 0) {
                    return;
                } else if (stack.getItem() == ModItems.RPG.get() && tag.getInt("max_ammo") == 0) {
                    return;
                } else if (stack.getItem() == ModItems.JAVELIN.get() && tag.getInt("max_ammo") == 0) {
                    return;
                }

                if (canReload || clipLoad) {
                    if (stack.is(ModTags.Items.OPEN_BOLT)) {
                        if (stack.getItem() == ModItems.M_60.get() || stack.getItem() == ModItems.ABEKIRI.get()) {
                            if (tag.getInt("ammo") < tag.getDouble("mag")) {
                                tag.putBoolean("start_reload", true);
                            }
                        } else {
                            if (tag.getInt("ammo") < tag.getDouble("mag") + 1) {
                                tag.putBoolean("start_reload", true);
                            }
                        }
                    } else if (tag.getInt("ammo") < tag.getDouble("mag")) {
                        tag.putBoolean("start_reload", true);
                    }
                    return;
                }

                if (canSingleReload) {
                    if (tag.getInt("ammo") < tag.getDouble("mag")) {
                        tag.putBoolean("start_single_reload", true);
                    }
                }
            }
        }
    }
}
