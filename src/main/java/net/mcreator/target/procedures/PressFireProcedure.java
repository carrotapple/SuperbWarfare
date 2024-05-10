package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;

public class PressFireProcedure {
    public static void execute(Player player) {
        TaserfireProcedure.execute(player);
        M79fireProcedure.execute(player);
        M98bfireProcedure.execute(player);
        RpgfireProcedure.execute(player);
        KraberfireProcedure.execute(player);
        MinigunfireProcedure.execute(player);
        SentinelFireProcedure.execute(player);
        MarlinfireProcedure.execute(player);
        M870fireProcedure.execute(player);
        VecfireProcedure.execute(player);
        player.getPersistentData().putDouble("firing", 1);
        if (player.getMainHandItem().is(ItemTags.create(new ResourceLocation("target:gun")))
                && !(player.getMainHandItem().getItem() == TargetModItems.BOCEK.get())
                && !(player.getMainHandItem().getItem() == TargetModItems.MINIGUN.get())
                && player.getMainHandItem().getOrCreateTag().getDouble("ammo") == 0
                && player.getMainHandItem().getOrCreateTag().getDouble("reloading") != 1) {
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
            }
        }
        if (player.getMainHandItem().getItem() == TargetModItems.MINIGUN.get()
                && (player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleammo == 0) {
            if (!player.level().isClientSide() && player.getServer() != null) {
                player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                        player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:triggerclick player @s ~ ~ ~ 10 1");
            }
        }
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.bowpullhold = true;
            capability.syncPlayerVariables(player);
        });
        if (player.getMainHandItem().getOrCreateTag().getDouble("ammo") == 0) {
            PlayerReloadProcedure.execute(player);
        }
    }
}
