package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MarlinfireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getOrCreateTag().getBoolean("reloading") && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getInt("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("force_stop", 1);
        }
        if (usehand.getItem() == TargetModItems.MARLIN.get() && !usehand.getOrCreateTag().getBoolean("reloading") && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getInt("ammo") > 0) {
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                player.getCooldowns().addCooldown(usehand.getItem(), 15);
                usehand.getOrCreateTag().putDouble("firing", 15);
                usehand.getOrCreateTag().putDouble("fastfiring", 0);
            } else {
                player.getCooldowns().addCooldown(usehand.getItem(), 10);
                usehand.getOrCreateTag().putDouble("fastfiring", 1);
                usehand.getOrCreateTag().putDouble("firing", 10);
            }
            GunsTool.spawnBullet(player);

            if (!player.level().isClientSide() && player.getServer() != null) {
                SoundTool.playLocalSound(player, TargetModSounds.MARLIN_FIRE_1P.get(), 2, 1);
                SoundTool.playLocalSound(player, TargetModSounds.MARLIN_FIRE_3P.get(), 4, 1);
                SoundTool.playLocalSound(player, TargetModSounds.MARLIN_FAR.get(), 12, 1);
                SoundTool.playLocalSound(player, TargetModSounds.MARLIN_VERYFAR.get(), 24, 1);
            }
            usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
            if (usehand.getOrCreateTag().getDouble("animindex") == 1) {
                usehand.getOrCreateTag().putDouble("animindex", 0);
            } else {
                usehand.getOrCreateTag().putDouble("animindex", 1);
            }
            usehand.getOrCreateTag().putInt("fire_animation", 2);
        }
    }
}
