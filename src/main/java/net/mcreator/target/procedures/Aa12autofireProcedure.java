package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Aa12autofireProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand = ItemStack.EMPTY;
        usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        if (entity.getPersistentData().getDouble("firing") == 1 && usehand.getOrCreateTag().getDouble("firemode") == 2) {
            if (usehand.getItem() == TargetModItems.AA_12.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                    && !(entity instanceof Player _plrCldCheck6 && _plrCldCheck6.getCooldowns().isOnCooldown(usehand.getItem()))) {
                for (int index0 = 0; index0 < 8; index0++) {
                    BulletFireNormalProcedure.execute(entity);
                }
                As12DsProcedure.execute(entity);
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 4);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:aa12_fire_1p player @s ~ ~ ~ 2 1");
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:aa12fire player @a ~ ~ ~ 6 1");
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:aa12fire");
                    }
                }
                usehand.getOrCreateTag().putDouble("fireanim", 2);
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
            }
        } else if (entity.getPersistentData().getDouble("firing") == 1 && usehand.getOrCreateTag().getDouble("firemode") == 0) {
            entity.getPersistentData().putDouble("firing", 0);
            if (usehand.getItem() == TargetModItems.AA_12.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && usehand.getOrCreateTag().getDouble("ammo") > 0
                    && !(entity instanceof Player _plrCldCheck20 && _plrCldCheck20.getCooldowns().isOnCooldown(usehand.getItem()))) {
                for (int index1 = 0; index1 < 8; index1++) {
                    BulletFireNormalProcedure.execute(entity);
                }
                As12DsProcedure.execute(entity);
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 4);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:aa12_fire_1p player @s ~ ~ ~ 2 1");
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:aa12fire player @a ~ ~ ~ 6 1");
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "stopsound @s player target:aa12fire");
                    }
                }
                usehand.getOrCreateTag().putDouble("fireanim", 2);
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
            }
        }
    }
}
