package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class AbkrfireProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack usehand = ItemStack.EMPTY;
        if (!(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                } else if (_ent.level().isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                }
                return false;
            }
        }.checkGamemode(entity))) {
            usehand = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
            if (usehand.getItem() == TargetModItems.ABEKIRI.get() && usehand.getOrCreateTag().getDouble("reloading") == 0 && !(entity instanceof Player _plrCldCheck4 && _plrCldCheck4.getCooldowns().isOnCooldown(usehand.getItem()))
                    && usehand.getOrCreateTag().getDouble("ammo") > 0) {
                for (int index0 = 0; index0 < 8; index0++) {
                    BulletfireNormalProcedure.execute(entity);
                }
                {
                    if (usehand.hurt(1, RandomSource.create(), null)) {
                        usehand.shrink(1);
                        usehand.setDamageValue(0);
                    }
                }
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(
                                new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4, entity.getName().getString(), entity.getDisplayName(),
                                        entity.level().getServer(), entity),
                                ("particle minecraft:cloud" + (" " + (entity.getX() + 1.8 * entity.getLookAngle().x)) + (" " + (entity.getY() + entity.getBbHeight() - 0.1 + 1.8 * entity.getLookAngle().y))
                                        + (" " + (entity.getZ() + 1.8 * entity.getLookAngle().z)) + " 0.4 0.4 0.4 0.005 30 force @s"));
                    }
                }
                if (entity instanceof Player _player)
                    _player.getCooldowns().addCooldown(usehand.getItem(), 2);
                {
                    if (!entity.level().isClientSide() && entity.getServer() != null) {
                        entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                                entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:abkr_fire_3p player @a ~ ~ ~ 4 1");
                    }
                }
                usehand.getOrCreateTag().putDouble("fireanim", 2);
                usehand.getOrCreateTag().putDouble("ammo", (usehand.getOrCreateTag().getDouble("ammo") - 1));
            }
        }
    }
}
