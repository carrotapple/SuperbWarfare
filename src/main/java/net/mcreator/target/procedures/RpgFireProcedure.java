package net.mcreator.target.procedures;

import net.mcreator.target.entity.RpgRocketEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEnchantments;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

// TODO 内联这个类
public class RpgFireProcedure {
    public static void execute(Player player) {
        if (player.isSpectator()) {
            return;
        }

        Level level = player.level();
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();

        if (mainHandItem.getItem() == TargetModItems.RPG.get() && !tag.getBoolean("reloading") && !player.getCooldowns().isOnCooldown(mainHandItem.getItem())
                && tag.getInt("ammo") > 0) {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });


            if (!level.isClientSide()) {
                int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), mainHandItem);
                RpgRocketEntity rocketEntity = new RpgRocketEntity(player, level, (float) tag.getDouble("damage"), monsterMultiple);
                rocketEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                rocketEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) tag.getDouble("velocity"),
                        (float) player.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                level.addFreshEntity(rocketEntity);
            }

            if (player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4, player.getName().getString(), player.getDisplayName(),
                                player.level().getServer(), player),
                        ("particle minecraft:cloud" + (" " + (player.getX() + 1.8 * player.getLookAngle().x)) + (" " + (player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y))
                                + (" " + (player.getZ() + 1.8 * player.getLookAngle().z)) + " 0.4 0.4 0.4 0.005 30 force @s"));
            }

            if (tag.getInt("ammo") == 1) {
                tag.putBoolean("empty", true);
                tag.putBoolean("close_hammer", true);
            }

            player.getCooldowns().addCooldown(mainHandItem.getItem(), 10);

            if (!player.level().isClientSide() && player.getServer() != null) {
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FIRE_1P.get(), SoundSource.PLAYERS, 2, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 8, 1);
                player.level().playSound(null, player.blockPosition(), TargetModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 16, 1);
            }

            tag.putInt("fire_animation", 2);
            tag.putInt("ammo", tag.getInt("ammo") - 1);
        }
    }
}
