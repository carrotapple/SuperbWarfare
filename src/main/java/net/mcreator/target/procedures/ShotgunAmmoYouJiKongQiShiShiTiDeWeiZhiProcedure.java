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

public class ShotgunAmmoYouJiKongQiShiShiTiDeWeiZhiProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        {
            double _setval = (entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).shotgunAmmo + 2;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.shotgunAmmo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Shotgun Ammo +2"), false);
        {
            Entity _ent = entity;
            if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                        _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:bulletsupply voice @a ~ ~ ~ 1 1");
            }
        }
        if (entity instanceof Player _player) {
            ItemStack _stktoremove = new ItemStack(TargetModItems.SHOTGUN_AMMO.get());
            _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
        }
        if (entity instanceof Player _player)
            _player.getCooldowns().addCooldown(itemstack.getItem(), 10);
        if (entity instanceof LivingEntity _entity)
            _entity.swing(InteractionHand.MAIN_HAND, true);
    }
}
