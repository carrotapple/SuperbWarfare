package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.mcreator.target.event.GunEventHandler;

public class PressFireProcedure {
    public static void execute(Player player) {
        TaserfireProcedure.execute(player);
        M79fireProcedure.execute(player);
        RpgFireProcedure.execute(player);
        MinigunfireProcedure.execute(player);
        MarlinfireProcedure.execute(player);
        M870fireProcedure.execute(player);
        VectorFireProcedure.execute(player);
        player.getPersistentData().putDouble("firing", 1);

        var mainHandItem = player.getMainHandItem();
        var tag = mainHandItem.getOrCreateTag();

        if (mainHandItem.is(TargetModTags.Items.GUN)
                && !(mainHandItem.getItem() == TargetModItems.BOCEK.get())
                && !(mainHandItem.getItem() == TargetModItems.MINIGUN.get())
                && tag.getInt("ammo") == 0
                && tag.getDouble("reloading") != 1) {
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
            }
        }
        if (mainHandItem.getItem() == TargetModItems.MINIGUN.get()
                && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo == 0) {
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
            }
        }
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.bowPullHold = true;
            capability.syncPlayerVariables(player);
        });
        if (tag.getInt("ammo") == 0) {
            PlayerReloadProcedure.execute(player);
        }
        /**
         * 栓动武器左键手动拉栓
         */
        if (mainHandItem.is(TargetModTags.Items.GUN) && tag.getDouble("bolt_action_time") > 0 && tag.getInt("ammo") > 0) {
            if (!player.getCooldowns().isOnCooldown(mainHandItem.getItem()) && mainHandItem.getOrCreateTag().getDouble("need_bolt_action") == 1) {
                mainHandItem.getOrCreateTag().putDouble("bolt_action_anim", mainHandItem.getOrCreateTag().getDouble("bolt_action_time"));
                GunEventHandler.playGunBoltSounds(player);
            }
        }
    }
}
