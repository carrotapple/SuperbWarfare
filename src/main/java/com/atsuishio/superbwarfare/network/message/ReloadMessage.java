package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.GunsTool;
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
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.edit = false;
                capability.syncPlayerVariables(player);
            });

            ItemStack stack = player.getMainHandItem();
            var capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

            if (!player.isSpectator()
                    && stack.is(ModTags.Items.GUN)
                    && !stack.getOrCreateTag().getBoolean("sentinel_is_charging")
                    && !(player.getCooldowns().isOnCooldown(stack.getItem()))
                    && GunsTool.getGunIntTag(stack, "ReloadTime") == 0
                    && stack.getOrCreateTag().getInt("bolt_action_anim") == 0
            ) {
                CompoundTag tag = stack.getOrCreateTag();

                boolean canSingleReload = GunsTool.getGunIntTag(stack, "IterativeTime", 0) != 0;
                boolean canReload = (GunsTool.getGunIntTag(stack, "NormalReloadTime") != 0 || GunsTool.getGunIntTag(stack, "EmptyReloadTime") != 0)
                        && GunsTool.getGunIntTag(stack, "ClipLoad", 0) != 1;
                boolean clipLoad = tag.getInt("ammo") == 0 && GunsTool.getGunIntTag(stack, "ClipLoad", 0) == 1;

                // 检查备弹
                int count = 0;
                for (var inv : player.getInventory().items) {
                    if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                        count++;
                    }
                }

                if (count == 0) {
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
                }

                if (canReload || clipLoad) {
                    int magazine = GunsTool.getGunIntTag(stack, "Magazine", 0);

                    if (stack.is(ModTags.Items.OPEN_BOLT)) {
                        if (stack.is(ModTags.Items.EXTRA_ONE_AMMO)) {
                            if (tag.getInt("ammo") < magazine + tag.getInt("customMag") + 1) {
                                tag.putBoolean("start_reload", true);
                            }
                        } else {
                            if (tag.getInt("ammo") < magazine + tag.getInt("customMag")) {
                                tag.putBoolean("start_reload", true);
                            }
                        }
                    } else if (tag.getInt("ammo") < magazine + tag.getInt("customMag")) {
                        tag.putBoolean("start_reload", true);
                    }
                    return;
                }

                if (canSingleReload) {
                    if (tag.getInt("ammo") < GunsTool.getGunIntTag(stack, "Magazine", 0) + tag.getInt("customMag")) {
                        tag.putBoolean("start_single_reload", true);
                    }
                }
            }
        }
    }
}
