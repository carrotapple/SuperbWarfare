package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.api.event.ReloadEvent;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReloadEventHandler {

    @SubscribeEvent
    public static void onPreReload(ReloadEvent.Pre event) {
        Player player = event.player;
        ItemStack stack = event.stack;
        if (player == null || !(stack.getItem() instanceof GunItem)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        GunData data = GunData.from(stack);
        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                instance.perk().preReload(data, instance, player);
            }
        }

        handleKillingTallyPre(stack);
        handleDesperadoPre(stack);
    }

    @SubscribeEvent
    public static void onPostReload(ReloadEvent.Post event) {
        Player player = event.player;
        ItemStack stack = event.stack;
        if (player == null || !(stack.getItem() instanceof GunItem)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        GunData data = GunData.from(stack);
        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                instance.perk().postReload(data, instance, player);
            }
        }

        handleDesperadoPost(stack);
    }

    private static void handleKillingTallyPre(ItemStack stack) {
        int level = GunData.from(stack).perk.getLevel(ModPerks.KILLING_TALLY);
        if (level == 0) {
            return;
        }

        GunsTool.setPerkIntTag(stack, "KillingTally", 0);
    }

    private static void handleDesperadoPre(ItemStack stack) {
        int time = GunsTool.getPerkIntTag(stack, "DesperadoTime");
        if (time > 0) {
            GunsTool.setPerkIntTag(stack, "DesperadoTime", 0);
            GunsTool.setPerkBooleanTag(stack, "Desperado", true);
        } else {
            GunsTool.setPerkBooleanTag(stack, "Desperado", false);
        }
    }

    private static void handleDesperadoPost(ItemStack stack) {
        if (!GunsTool.getPerkBooleanTag(stack, "Desperado")) {
            return;
        }

        int level = GunData.from(stack).perk.getLevel(ModPerks.DESPERADO);
        GunsTool.setPerkIntTag(stack, "DesperadoTimePost", 110 + level * 10);
    }
}
