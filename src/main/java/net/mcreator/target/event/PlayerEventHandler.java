package net.mcreator.target.event;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            Level level = player.level();

            if (player.getBbHeight() <= 1) {
                player.getPersistentData().putDouble("prone", 3);
            }

            if (player.isShiftKeyDown() && level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 0.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()
                    && !level.getBlockState(BlockPos.containing(player.getX() + 0.7 * player.getLookAngle().x, player.getY() + 1.5, player.getZ() + 0.7 * player.getLookAngle().z)).canOcclude()) {
                player.getPersistentData().putDouble("prone", 3);
            }

            if (player.getPersistentData().getDouble("prone") > 0) {
                player.getPersistentData().putDouble("prone", (player.getPersistentData().getDouble("prone") - 1));
            }

            boolean flag = !(player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).refresh;

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.refresh = flag;
                capability.syncPlayerVariables(player);
            });
        }
    }
}
