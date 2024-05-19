package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M870fireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack usehand = player.getMainHandItem();
        if (usehand.getOrCreateTag().getBoolean("reloading") && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getInt("ammo") > 0) {
            usehand.getOrCreateTag().putDouble("force_stop", 1);
        }
        if (usehand.getItem() == TargetModItems.M_870.get() && !usehand.getOrCreateTag().getBoolean("reloading") && !player.getCooldowns().isOnCooldown(usehand.getItem())
                && usehand.getOrCreateTag().getInt("ammo") > 0) {
            for (int index0 = 0; index0 < 12; index0++) {
                GunsTool.spawnBullet(player);
            }
            player.getCooldowns().addCooldown(usehand.getItem(), 13);
            usehand.getOrCreateTag().putDouble("firing", 13);

            if (!player.level().isClientSide()) {
                SoundTool.playLocalSound(player, TargetModSounds.M_870_FIRE_1P.get(), 2, 1);
                SoundTool.playLocalSound(player, TargetModSounds.M_870_FIRE_3P.get(), 4, 1);
                SoundTool.playLocalSound(player, TargetModSounds.M_870_FAR.get(), 12, 1);
                SoundTool.playLocalSound(player, TargetModSounds.M_870_VERYFAR.get(), 24, 1);
            }
            usehand.getOrCreateTag().putInt("ammo", (usehand.getOrCreateTag().getInt("ammo") - 1));
            usehand.getOrCreateTag().putInt("fire_animation", 2);
        }
    }
}
