package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HandgunAmmoYouJiKongQiShiShiTiDeWeiZhiProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        {
            double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).handgunammo + 5;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.handgunammo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Handgun Ammo +5"), false);
        {
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:bulletsupply voice @a ~ ~ ~ 1 1");
            }
        }
        if (entity instanceof Player _player) {
            ItemStack _stktoremove = new ItemStack(TargetModItems.HANDGUN_AMMO.get());
            _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
        }
        if (entity instanceof Player _player)
            _player.getCooldowns().addCooldown(itemstack.getItem(), 10);
        if (entity instanceof LivingEntity _entity)
            _entity.swing(InteractionHand.MAIN_HAND, true);
    }
}
