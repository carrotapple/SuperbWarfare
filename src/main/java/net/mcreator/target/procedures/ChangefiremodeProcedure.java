package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ChangefiremodeProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand = ItemStack.EMPTY;
        usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (usehand.getItem() == TargetModItems.AK_47.get() || usehand.getItem() == TargetModItems.M_4.get() || usehand.getItem() == TargetModItems.AA_12.get() || usehand.getItem() == TargetModItems.HK_416.get()
                || usehand.getItem() == TargetModItems.RPK.get() || usehand.getItem() == TargetModItems.MK_14.get()) {
            if (usehand.getOrCreateTag().getDouble("firemode") == 2) {
                usehand.getOrCreateTag().putDouble("firemode", 0);
                usehand.getOrCreateTag().putDouble("cg", 10);
                entity.getPersistentData().putDouble("firing", 0);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:firerate player @s ~ ~ ~ 10 1");
                    }
                }
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("Semi"), true);
            } else if (usehand.getOrCreateTag().getDouble("firemode") == 0) {
                usehand.getOrCreateTag().putDouble("firemode", 2);
                usehand.getOrCreateTag().putDouble("cg", 10);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:firerate player @s ~ ~ ~ 10 1");
                    }
                }
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("Auto"), true);
            }
        }
        if (usehand.getItem() == TargetModItems.VECTOR.get()) {
            if (usehand.getOrCreateTag().getDouble("firemode") == 0) {
                usehand.getOrCreateTag().putDouble("firemode", 1);
                usehand.getOrCreateTag().putDouble("cg", 10);
                entity.getPersistentData().putDouble("firing", 0);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:firerate player @s ~ ~ ~ 10 1");
                    }
                }
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("Burst"), true);
            } else if (usehand.getOrCreateTag().getDouble("firemode") == 1) {
                usehand.getOrCreateTag().putDouble("firemode", 2);
                usehand.getOrCreateTag().putDouble("cg", 10);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:firerate player @s ~ ~ ~ 10 1");
                    }
                }
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("Auto"), true);
            } else if (usehand.getOrCreateTag().getDouble("firemode") == 2) {
                usehand.getOrCreateTag().putDouble("firemode", 0);
                usehand.getOrCreateTag().putDouble("cg", 10);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:firerate player @s ~ ~ ~ 10 1");
                    }
                }
                if (entity instanceof Player _player && !_player.level().isClientSide())
                    _player.displayClientMessage(Component.literal("Semi"), true);
            }
        }
        if (usehand.getItem() == TargetModItems.SENTINEL.get() && !(entity instanceof Player _plrCldCheck36 && _plrCldCheck36.getCooldowns().isOnCooldown(usehand.getItem())) && usehand.getOrCreateTag().getDouble("charging") == 0) {
            usehand.getOrCreateTag().putDouble("charging", 1);
            usehand.getOrCreateTag().putDouble("cid", (Mth.nextDouble(RandomSource.create(), 1, 1919810)));
            usehand.getOrCreateTag().putDouble("chargingtime", 128);
        }
    }
}
