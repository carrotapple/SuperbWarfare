package net.mcreator.target.network;

import net.mcreator.target.entity.BocekarrowEntity;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.procedures.PressFireProcedure;
import net.mcreator.target.tools.GunsTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FireMessage {
    private final int type;

    public FireMessage(int type) {
        this.type = type;
    }

    public FireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(FireMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(FireMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                pressAction(context.getSender(), message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level world = player.level();

        if (!world.hasChunkAt(player.blockPosition())) {
            return;
        }

        if (type == 0) {
            PressFireProcedure.execute(player);
        } else if (type == 1) {
            player.getPersistentData().putBoolean("firing", false);
            player.getPersistentData().putDouble("mini_firing", 0);
            player.getPersistentData().putDouble("minigun_firing", 0);
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.syncPlayerVariables(player);
            });

            handleBowShoot(player);
        }
    }

    private static void handleBowShoot(Player player) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() != TargetModItems.BOCEK.get()) {
            return;
        }

        double power = stack.getOrCreateTag().getDouble("power");

        if (!player.level().isClientSide() && player.getServer() != null) {
            player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                    player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "stopsound @a player target:bocek_pull_1p");
            player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                    player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "stopsound @a player target:bocek_pull_3p");
        }

        if (stack.getOrCreateTag().getDouble("power") >= 6) {
            stack.getOrCreateTag().putDouble("speed", stack.getOrCreateTag().getDouble("power"));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                Level level = player.level();
                if (!level.isClientSide()) {
                    float damage = (float) (0.02 * stack.getOrCreateTag().getDouble("damage") * (1 + 0.05 * stack.getOrCreateTag().getInt("level")));

                    BocekarrowEntity arrow = new BocekarrowEntity(TargetModEntities.BOCEKARROW.get(), level);
                    arrow.setOwner(player);
                    arrow.setBaseDamage(damage);
                    arrow.setKnockback(0);
                    arrow.setSilent(true);
                    arrow.setPierceLevel((byte) 2);
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;

                    arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) (4 * power), (float) 0.02);
                    level.addFreshEntity(arrow);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_zoom_fire_1p player @s ~ ~ ~ 10 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_zoom_fire_3p player @a ~ ~ ~ 2 1");
                }
            } else {
                for (int index0 = 0; index0 < 10; index0++) {
                    GunsTool.spawnBullet(player);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_shatter_cap_fire_1p player @s ~ ~ ~ 10 1");
                    player.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4,
                            player.getName().getString(), player.getDisplayName(), player.level().getServer(), player), "playsound target:bocek_shatter_cap_fire_3p player @a ~ ~ ~ 2 1");
                }
            }

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });

            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 7);
            player.getMainHandItem().getOrCreateTag().putInt("arrow_empty", 7);
            player.getMainHandItem().getOrCreateTag().putDouble("power", 0);
            stack.getOrCreateTag().putInt("fire_animation", 2);

            if (!player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> Items.ARROW == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }
        }
    }
}
