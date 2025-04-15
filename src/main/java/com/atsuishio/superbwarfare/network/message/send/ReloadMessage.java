package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.Ammo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        if (type == 0) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.edit = false;
                capability.sync(player);
            });

            ItemStack stack = player.getMainHandItem();
            var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());

            if (!player.isSpectator()
                    && stack.getItem() instanceof GunItem gunItem
                    && !GunData.from(stack).charging()
                    && GunData.from(stack).reload.time() == 0
                    && GunData.from(stack).bolt.actionTimer.get() == 0
                    && !GunData.from(stack).reloading()
            ) {
                var data = GunData.from(stack);

                boolean canSingleReload = gunItem.isIterativeReload(stack);
                boolean canReload = gunItem.isMagazineReload(stack) && !gunItem.isClipReload(stack);
                boolean clipLoad = data.ammo.get() == 0 && gunItem.isClipReload(stack);

                // 检查备弹
                boolean hasCreativeAmmoBox = player.getInventory().hasAnyMatching(item -> item.is(ModItems.CREATIVE_AMMO_BOX.get()));

                if (!hasCreativeAmmoBox) {
                    var ammoTypeInfo = data.ammoTypeInfo();

                    if (ammoTypeInfo.type() == GunData.AmmoConsumeType.PLAYER_AMMO) {
                        var ammoType = Ammo.getType(ammoTypeInfo.value());
                        assert ammoType != null;

                        if (ammoType.get(cap) == 0) return;
                    } else if ((ammoTypeInfo.type() == GunData.AmmoConsumeType.ITEM || ammoTypeInfo.type() == GunData.AmmoConsumeType.TAG) && !data.hasAmmo(player)) {
                        return;
                    }
                }

                if (canReload || clipLoad) {
                    int magazine = data.magazine();

                    if (gunItem.isOpenBolt(stack)) {
                        if (gunItem.hasBulletInBarrel(stack)) {
                            if (data.ammo.get() < magazine + 1) {
                                data.reload.reloadStarter.markStart();
                            }
                        } else {
                            if (data.ammo.get() < magazine) {
                                data.reload.reloadStarter.markStart();
                            }
                        }
                    } else if (data.ammo.get() < magazine) {
                        data.reload.reloadStarter.markStart();
                    }
                    return;
                }

                if (canSingleReload) {
                    if (data.ammo.get() < data.magazine()) {
                        data.reload.singleReloadStarter.markStart();
                    }
                }
            }
        }
    }
}
