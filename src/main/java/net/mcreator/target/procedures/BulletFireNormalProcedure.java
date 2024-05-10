package net.mcreator.target.procedures;

import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BulletFireNormalProcedure {
    public static void execute(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.recoilhorizon = Math.random() < 0.5 ? -1 : 1;
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (!player.level().isClientSide()) {
            float damage;
            float headshot = (float) heldItem.getOrCreateTag().getDouble("headshot");
            float velocity = 4 * (float) heldItem.getOrCreateTag().getDouble("speed");

            if (heldItem.getItem() == TargetModItems.BOCEK.get()) {

                damage = 0.008333333f * (float) heldItem.getOrCreateTag().getDouble("damage")  * (float) heldItem.getOrCreateTag().getDouble("speed") * (float) heldItem.getOrCreateTag().getDouble("damageadd");

                ProjectileEntity projectile = new ProjectileEntity(player.level(), player, damage, headshot);

                projectile.setPos((player.getX() + (-0.5) * player.getLookAngle().x), (player.getEyeY() - 0.1 + (-0.5) * player.getLookAngle().y), (player.getZ() + (-0.5) * player.getLookAngle().z));
                projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 1 * velocity, 2.5f);
                player.level().addFreshEntity(projectile);

            } else {
                damage = (float) (heldItem.getOrCreateTag().getDouble("damage") + heldItem.getOrCreateTag().getDouble("adddamage"))
                        * (float) heldItem.getOrCreateTag().getDouble("damageadd");

                ProjectileEntity projectile = new ProjectileEntity(player.level(), player, damage, headshot);

                projectile.setPos((player.getX() + (-0.5) * player.getLookAngle().x), (player.getEyeY() - 0.1 + (-0.5) * player.getLookAngle().y), (player.getZ() + (-0.5) * player.getLookAngle().z));
                projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 1 * (float) heldItem.getOrCreateTag().getDouble("velocity"),
                        (float) player.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                player.level().addFreshEntity(projectile);
            }
        }
    }
}
