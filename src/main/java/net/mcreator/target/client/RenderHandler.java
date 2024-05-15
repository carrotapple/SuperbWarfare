package net.mcreator.target.client;

import net.mcreator.target.init.TargetModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderHandler {

    @SubscribeEvent
    public static void onFovUpdate(ViewportEvent.ComputeFov event) {
        if (!event.usedConfiguredFov()) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        double p = player.getPersistentData().getDouble("zoompos");
        double zoom = stack.getOrCreateTag().getDouble("zoom");

        if (stack.is(TargetModTags.Items.GUN)) {
            event.setFOV(event.getFOV() / (1.0 + p * (zoom - 1)));
            player.getPersistentData().putDouble("fov", event.getFOV());
        }
    }
}
