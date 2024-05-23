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

                if (player.getMainHandItem().getItem() == TargetModItems.TASER.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 1
                        && tag.getInt("max_ammo") > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reload_time", 55);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.TRACHELIUM.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("empty_reload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reload_time", 57);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.HUNTING_RIFLE.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 1
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("reload_time", 61);
                    tag.putDouble("empty_reload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_79.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 1
                        && tag.getInt("max_ammo") > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("empty_reload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reload_time", 86);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.RPG.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 1
                        && tag.getInt("max_ammo") > 0) {
                    tag.putBoolean("reloading", true);
                    tag.putDouble("empty_reload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reload_time", 91);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.ABEKIRI.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 2
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getInt("ammo") == 1) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 83);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 99);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_98B.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 6
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 57);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 79);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.KRABER.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 5
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 65);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 83);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.VECTOR.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 34
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 47);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 61);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.MK_14.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 21
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 45);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 55);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SKS.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 21
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 41);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 57);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.AK_47.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 31
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 41);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 57);
                    }
                }
                if ((player.getMainHandItem().getItem() == TargetModItems.M_4.get()
                        || player.getMainHandItem().getItem() == TargetModItems.HK_416.get())
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 31
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 53);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 61);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.AA_12.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 26
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 44);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 55);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.DEVOTION.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getInt("ammo") < 56) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 51);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 71);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.RPK.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getInt("ammo") < 76) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 66);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 83);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SENTINEL.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 6
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 53);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 73);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SVD.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && tag.getInt("ammo") < 11
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 55);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 66);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_60.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && !tag.getBoolean("reloading")
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getInt("ammo") < 100) {
                    if (tag.getInt("ammo") > 0) {
                        tag.putBoolean("reloading", true);
                        tag.putBoolean("empty_reload", false);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 111);
                    } else if (tag.getInt("ammo") == 0) {
                        tag.putBoolean("reloading", true);
                        tag.putDouble("empty_reload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reload_time", 129);
                    }
                }
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
