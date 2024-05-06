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

public class CreativeAmmoBoxWanJiaWanChengShiYongWuPinShiProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        if (entity instanceof Player _player)
            _player.getCooldowns().addCooldown(itemstack.getItem(), 20);
        if (entity instanceof LivingEntity _entity)
            _entity.swing(InteractionHand.MAIN_HAND, true);
        if (entity instanceof Player _player) {
            ItemStack _stktoremove = new ItemStack(TargetModItems.CREATIVE_AMMO_BOX.get());
            _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
        }
        {
            double _setval = 2147483647;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.rifleammo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        {
            double _setval = 2147483647;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.handgunammo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        {
            double _setval = 2147483647;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.shotgunammo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        {
            double _setval = 2147483647;
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.sniperammo = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Rifle Ammo +2147483647"), false);
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Handgun Ammo +2147483647"), false);
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Shotgun Ammo +2147483647"), false);
        if (entity instanceof Player _player && !_player.level().isClientSide())
            _player.displayClientMessage(Component.literal("Sniper Ammo +2147483647"), false);
        {
            if (!entity.level().isClientSide() && entity.getServer() != null) {
                entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                        entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), "playsound target:bulletsupply voice @a ~ ~ ~ 1 1");
            }
        }
    }
}
