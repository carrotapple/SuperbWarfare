package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class M870fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack usehand = player.getMainHandItem();
            if (usehand.getOrCreateTag().getDouble("reloading") == 1 && usehand.getOrCreateTag().getDouble("prepare") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0) {
                usehand.getOrCreateTag().putDouble("forcestop", 1);
            }
            if (usehand.getItem() == TargetModItems.M870.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck8 && _plrCldCheck8.getCooldowns().isOnCooldown(usehand.getItem()))
                    && usehand.getOrCreateTag().getDouble("ammo") > 0) {
                for (int index0 = 0; index0 < 12; index0++) {
                    BulletFireNormalProcedure.execute(entity);
                }
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 13);
                usehand.getOrCreateTag().putDouble("recoil", 1);
                usehand.getOrCreateTag().putDouble("firing", 13);

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m870_fire_1p player @s ~ ~ ~ 2 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m870_fire_3p player @a ~ ~ ~ 4 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m870_far player @s ~ ~ ~ 12 1");
                    entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                            entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:m870_veryfar player @a ~ ~ ~ 24 1");
                }
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
                usehand.getOrCreateTag().putDouble("fireanim", 2);
            }
        }
    }
}
