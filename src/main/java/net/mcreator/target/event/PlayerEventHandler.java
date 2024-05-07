package net.mcreator.target.event;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        if (!TargetModVariables.MapVariables.get(player.level()).pvpmode) {
            return;
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(TargetModTags.Items.GUN)) {
                stack.getOrCreateTag().putDouble("ammo", stack.getOrCreateTag().getDouble("mag"));
            }
        }
    }

}
