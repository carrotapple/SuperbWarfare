package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PlayerReloadProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            if (!(entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                CompoundTag tag = player.getMainHandItem().getOrCreateTag();

                if (player.getMainHandItem().getItem() == TargetModItems.MARLIN.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 5);
                    tag.putDouble("prepare", 5);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        SoundTool.playLocalSound(serverPlayer, TargetModSounds.MARLIN_START.get(), 100, 1);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_870.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getInt("ammo") == 0) {
                        tag.putDouble("empty_reload", 1);
                        tag.putBoolean("reloading", true);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 36);
                        tag.putDouble("prepare", 36);

                        if (entity instanceof ServerPlayer serverPlayer) {
                            SoundTool.playLocalSound(serverPlayer, TargetModSounds.M_870_PREPARE_ALT.get(), 100, 1);
                        }
                    } else {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 7);
                        tag.putDouble("prepare", 7);
                    }
                }
            }
        }
    }
}
