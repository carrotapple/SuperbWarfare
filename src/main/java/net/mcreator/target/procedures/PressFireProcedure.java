package net.mcreator.target.procedures;

import net.mcreator.target.event.GunEventHandler;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.world.entity.player.Player;

public class PressFireProcedure {
    public static void execute(Player player) {
        TaserfireProcedure.execute(player);
        M79fireProcedure.execute(player);
        RpgFireProcedure.execute(player);

        MarlinfireProcedure.execute(player);
        M870fireProcedure.execute(player);
        VectorFireProcedure.execute(player);
        player.getPersistentData().putBoolean("firing", true);

        var mainHandItem = player.getMainHandItem();
        var tag = mainHandItem.getOrCreateTag();

        if (mainHandItem.is(TargetModTags.Items.GUN)
                && !(mainHandItem.getItem() == TargetModItems.BOCEK.get())
                && !(mainHandItem.getItem() == TargetModItems.MINIGUN.get())
                && tag.getInt("ammo") == 0
                && !tag.getBoolean("reloading")) {
            if (!player.level().isClientSide()) {
                SoundTool.playLocalSound(player, TargetModSounds.TRIGGER_CLICK.get(), 10, 1);
            }
        }

        if (mainHandItem.getItem() == TargetModItems.MINIGUN.get()) {
            player.getPersistentData().putDouble("mini_firing", 1);

            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).rifleAmmo == 0) {
                if (!player.level().isClientSide()) {
                    SoundTool.playLocalSound(player, TargetModSounds.TRIGGER_CLICK.get(), 10, 1);
                }
            }
        }

        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.bowPullHold = true;
            capability.syncPlayerVariables(player);
        });

        if (tag.getInt("ammo") == 0) {
            PlayerReloadProcedure.execute(player);
        }

        // 栓动武器左键手动拉栓
        if (mainHandItem.is(TargetModTags.Items.GUN) && tag.getDouble("bolt_action_time") > 0 && tag.getInt("ammo") > 0) {
            if (!player.getCooldowns().isOnCooldown(mainHandItem.getItem()) && mainHandItem.getOrCreateTag().getDouble("need_bolt_action") == 1) {
                mainHandItem.getOrCreateTag().putDouble("bolt_action_anim", mainHandItem.getOrCreateTag().getDouble("bolt_action_time"));
                GunEventHandler.playGunBoltSounds(player);
            }
        }
    }
}
