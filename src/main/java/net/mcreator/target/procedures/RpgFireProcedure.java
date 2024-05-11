package net.mcreator.target.procedures;

import net.mcreator.target.entity.RpgRocketEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RpgFireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) return;

        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();

        if (mainHandItem.getItem() == TargetModItems.RPG.get() && tag.getDouble("reloading") == 0 && !player.getCooldowns().isOnCooldown(mainHandItem.getItem())
                && tag.getDouble("ammo") > 0) {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilhorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });

            Level projectileLevel = player.level();
            if (!projectileLevel.isClientSide()) {
                Projectile projectile = getArrow(player, projectileLevel, tag);
                projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) tag.getDouble("velocity"),
                        (float) player.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                projectileLevel.addFreshEntity(projectile);
            }

            if (player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4, player.getName().getString(), player.getDisplayName(),
                                player.level().getServer(), player),
                        ("particle minecraft:cloud" + (" " + (player.getX() + 1.8 * player.getLookAngle().x)) + (" " + (player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y))
                                + (" " + (player.getZ() + 1.8 * player.getLookAngle().z)) + " 0.4 0.4 0.4 0.005 30 force @s"));
            }
            if (tag.getDouble("ammo") == 1) {
                tag.putDouble("empty", 1);
            }
            player.getCooldowns().addCooldown(mainHandItem.getItem(), 10);
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FIRE_1P.get(), SoundSource.PLAYERS, 2, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 8, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 16, 1);
            }
            tag.putDouble("fireanim", 2);
            tag.putDouble("ammo", tag.getDouble("ammo") - 1);
        }
    }

    private static Projectile getArrow(Player player, Level projectileLevel, CompoundTag tag) {
        return new Object() {
            public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                AbstractArrow entityToSpawn = new RpgRocketEntity(TargetModEntities.RPG_ROCKET.get(), level);
                entityToSpawn.setOwner(shooter);
                entityToSpawn.setBaseDamage(damage);
                entityToSpawn.setKnockback(knockback);
                entityToSpawn.setSilent(true);
                return entityToSpawn;
            }
        }.getArrow(projectileLevel, player, (float) ((tag.getDouble("damage") / tag.getDouble("velocity")) * (1 + 0.05 * tag.getDouble("level"))), 0);
    }

}
