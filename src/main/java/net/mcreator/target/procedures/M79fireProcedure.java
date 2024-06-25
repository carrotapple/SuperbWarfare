package net.mcreator.target.procedures;

import net.mcreator.target.entity.GunGrenadeEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModEnchantments;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

// TODO 内联这个类
public class M79fireProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (entity instanceof Player player && !player.isSpectator()) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() == TargetModItems.M_79.get() && !stack.getOrCreateTag().getBoolean("reloading") && !(entity instanceof Player _plrCldCheck4 && _plrCldCheck4.getCooldowns().isOnCooldown(stack.getItem()))
                    && stack.getOrCreateTag().getInt("ammo") > 0) {
                entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                    capability.recoil = 0.1;
                    capability.firing = 1;
                    capability.syncPlayerVariables(entity);
                });

                Level level = entity.level();
                if (!level.isClientSide()) {

                    int monster_multiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), stack);
                    GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage"), monster_multiple);

                    gunGrenadeEntity.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                    gunGrenadeEntity.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) ((LivingEntity) entity).getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
                    level.addFreshEntity(gunGrenadeEntity);
                }

                if (!entity.level().isClientSide() && entity.getServer() != null) {
                    entity.getServer().getCommands().performPrefixedCommand(
                            new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), (ServerLevel) entity.level(), 4, entity.getName().getString(), entity.getDisplayName(),
                                    entity.getServer(), entity),
                            ("particle minecraft:cloud" + (" " + (entity.getX() + 1.8 * entity.getLookAngle().x)) + (" " + (entity.getY() + entity.getBbHeight() - 0.1 + 1.8 * entity.getLookAngle().y))
                                    + (" " + (entity.getZ() + 1.8 * entity.getLookAngle().z)) + " 0.1 0.1 0.1 0.002 4 force @s"));
                }
                player.getCooldowns().addCooldown(stack.getItem(), 2);

                if (entity instanceof ServerPlayer) {
                    SoundTool.playLocalSound(player, TargetModSounds.M_79_FIRE_1P.get(), 2, 1);
                    SoundTool.playLocalSound(player, TargetModSounds.M_79_FIRE_3P.get(), 4, 1);
                    SoundTool.playLocalSound(player, TargetModSounds.M_79_FAR.get(), 6, 1);
                    SoundTool.playLocalSound(player, TargetModSounds.M_79_VERYFAR.get(), 12, 1);
                }
                stack.getOrCreateTag().putInt("fire_animation", 2);
                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
            }
        }
    }
}
