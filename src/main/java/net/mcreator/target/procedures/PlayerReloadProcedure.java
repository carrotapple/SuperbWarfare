package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 1
                        && tag.getDouble("maxammo") > 0)  {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reloadtime", 55);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.TRACHELIUM.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo > 0) {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("emptyreload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reloadtime", 57);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.HUNTING_RIFLE.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 1
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("reloadtime", 61);
                    tag.putDouble("emptyreload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_79.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 1
                        && tag.getDouble("maxammo") > 0) {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("emptyreload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reloadtime", 86);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.RPG.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 1
                        && tag.getDouble("maxammo") > 0) {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("emptyreload", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    tag.putDouble("reloadtime", 91);
                }
                if (player.getMainHandItem().getItem() == TargetModItems.ABEKIRI.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 2
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getDouble("ammo") == 1) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 83);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 99);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_98B.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 6
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 57);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 79);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.KRABER.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 5
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 65);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 83);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.VECTOR.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 34
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 47);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 61);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.MK_14.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 21
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 45);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 55);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SKS.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 21
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 41);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 57);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.AK_47.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 31
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 41);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 57);
                    }
                }
                if ((player.getMainHandItem().getItem() == TargetModItems.M_4.get()
                        || player.getMainHandItem().getItem() == TargetModItems.HK_416.get())
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 31
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 41);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 55);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.AA_12.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 26
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 44);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 55);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.DEVOTION.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getDouble("ammo") < 56) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 51);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 71);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.RPK.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getDouble("ammo") < 51) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 41);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 57);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SENTINEL.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 6
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 53);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 73);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.SVD.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 11
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).sniperAmmo > 0) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 55);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 66);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_60.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0
                        && tag.getDouble("ammo") < 100) {
                    if (tag.getDouble("ammo") > 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 0);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 111);
                    } else if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("reloading", 1);
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        tag.putDouble("reloadtime", 129);
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.MARLIN.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo > 0) {
                    tag.putDouble("reloading", 1);
                    tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 5);
                    tag.putDouble("prepare", 5);
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:marlin_start player @s ~ ~ ~ 100 1");
                    }
                }
                if (player.getMainHandItem().getItem() == TargetModItems.M_870.get()
                        && !(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem()))
                        && tag.getDouble("reloading") == 0
                        && tag.getDouble("ammo") < 8
                        && (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo > 0) {
                    if (tag.getDouble("ammo") == 0) {
                        tag.putDouble("emptyreload", 1);
                        tag.putDouble("reloading", 1);
                        tag.putDouble("id", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
                        player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 36);
                        tag.putDouble("prepare", 36);

                        if (!entity.level().isClientSide() && entity.getServer() != null) {
                            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m_870_preparealt player @s ~ ~ ~ 100 1");
                        }
                    } else {
                        tag.putDouble("reloading", 1);
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
