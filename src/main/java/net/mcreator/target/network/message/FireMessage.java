package net.mcreator.target.network.message;

import net.mcreator.target.entity.*;
import net.mcreator.target.event.GunEventHandler;
import net.mcreator.target.init.*;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

        if (!world.isLoaded(player.blockPosition())) {
            return;
        }

        if (type == 0) {
            handlePlayerShoot(player);
        } else if (type == 1) {
            player.getPersistentData().putBoolean("firing", false);
            player.getPersistentData().putDouble("minigun_firing", 0);
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.syncPlayerVariables(player);
            });

            if (player.getMainHandItem().getItem() == TargetModItems.BOCEK.get()) {
                handleBowShoot(player);
            }
        }
    }

    private static void handlePlayerShoot(Player player) {
        var handItem = player.getMainHandItem();

        if (!handItem.is(TargetModTags.Items.GUN)) {
            return;
        }

        var tag = handItem.getOrCreateTag();

        if (handItem.getItem() == TargetModItems.TASER.get()) {
            handleTaserFire(player);
        }

        if (handItem.getItem() == TargetModItems.M_79.get()) {
            handleM79Fire(player);
        }

        if (handItem.getItem() == TargetModItems.RPG.get()) {
            handleRpgFire(player);
        }

        if (tag.getInt("fire_mode") == 1) {
            player.getPersistentData().putBoolean("firing", false);
            tag.putInt("burst_fire", (int) tag.getDouble("burst_size"));
        } else {
            player.getPersistentData().putBoolean("firing", true);
        }
        if (tag.getDouble("prepare") == 0 && tag.getBoolean("reloading") && tag.getInt("ammo") > 0) {
            tag.putDouble("force_stop", 1);
        }

        if (handItem.getItem() != TargetModItems.BOCEK.get()
                && handItem.getItem() != TargetModItems.MINIGUN.get()
                && tag.getInt("ammo") == 0
                && !tag.getBoolean("reloading")) {
            if (!player.level().isClientSide()) {
                SoundTool.playLocalSound(player, TargetModSounds.TRIGGER_CLICK.get(), 10, 1);
            }
        }

        if (handItem.getItem() == TargetModItems.MINIGUN.get()) {
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

        // 栓动武器左键手动拉栓
        if (tag.getInt("bolt_action_time") > 0 && tag.getInt("ammo") > 0 && tag.getInt("bolt_action_anim") == 0) {
            if (!player.getCooldowns().isOnCooldown(handItem.getItem()) && handItem.getOrCreateTag().getDouble("need_bolt_action") == 1) {
                handItem.getOrCreateTag().putInt("bolt_action_anim", handItem.getOrCreateTag().getInt("bolt_action_time"));
                GunEventHandler.playGunBoltSounds(player);
            }
        }
    }

    private static void handleBowShoot(Player player) {
        ItemStack stack = player.getMainHandItem();

        double power = stack.getOrCreateTag().getDouble("power");

        if (player instanceof ServerPlayer serverPlayer) {
            SoundTool.stopSound(serverPlayer, TargetModSounds.BOCEK_PULL_1P.getId(), SoundSource.PLAYERS);
            SoundTool.stopSound(serverPlayer, TargetModSounds.BOCEK_PULL_3P.getId(), SoundSource.PLAYERS);
        }

        if (stack.getOrCreateTag().getDouble("power") >= 6) {
            stack.getOrCreateTag().putDouble("speed", stack.getOrCreateTag().getDouble("power"));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
                Level level = player.level();
                if (!level.isClientSide()) {
                    int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), stack);
                    float damage = (float) (0.02 * stack.getOrCreateTag().getDouble("damage") * (1 + 0.05 * stack.getOrCreateTag().getInt("level")));

                    BocekArrowEntity arrow = new BocekArrowEntity(player, level, monsterMultiple);
                    arrow.setBaseDamage(damage);
                    arrow.setKnockback(0);
                    arrow.setSilent(true);
                    arrow.setPierceLevel((byte) 2);
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;

                    arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) (4 * power), (float) 0.02);
                    level.addFreshEntity(arrow);
                }

                if (!player.level().isClientSide()) {
                    SoundTool.playLocalSound(player, TargetModSounds.BOCEK_ZOOM_FIRE_1P.get(), 10, 1);
                    player.playSound(TargetModSounds.BOCEK_ZOOM_FIRE_3P.get(), 2, 1);
                }
            } else {
                for (int index0 = 0; index0 < 10; index0++) {
                    spawnBullet(player);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    SoundTool.playLocalSound(player, TargetModSounds.BOCEK_SHATTER_CAP_FIRE_1P.get(), 10, 1);
                    player.playSound(TargetModSounds.BOCEK_SHATTER_CAP_FIRE_3P.get(), 2, 1);
                }
            }

            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
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

    private static void spawnBullet(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (player.level().isClientSide()) return;

        CompoundTag tag = heldItem.getOrCreateTag();
        double damage;
        float headshot = (float) tag.getDouble("headshot");
        float velocity = 4 * (float) tag.getDouble("speed");
        int monster_multiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), heldItem);

        var projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot);
        if (tag.getBoolean("beast")) {
            projectile.beast();
        }

        projectile.monster_multiple(monster_multiple);

        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        damage = 0.008333333 * tag.getDouble("damage") * tag.getDouble("speed") * tag.getDouble("damageadd");
        projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 2.5f);
        projectile.damage((float) damage);
        player.level().addFreshEntity(projectile);
    }

    private static void handleTaserFire(Player player) {
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("reloading")) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getInt("ammo") > 0
                    && ItemNBTTool.getInt(stack, "Power", 1200) > 400) {
                player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                    capability.recoil = 0.1;
                    capability.firing = 1;
                    capability.syncPlayerVariables(player);
                });
                player.getCooldowns().addCooldown(stack.getItem(), 5);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.TASER_FIRE_1P.get(), 1, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.TASER_FIRE_3P.get(), SoundSource.PLAYERS, 1, 1);
                }

                int volt = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.VOLT_OVERLOAD.get(), stack);
                int wire_length = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.LONGER_WIRE.get(), stack);

                Level level = player.level();
                if (!level.isClientSide()) {
                    TaserBulletProjectileEntity taserBulletProjectile = new TaserBulletProjectileEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage"), volt, wire_length);

                    taserBulletProjectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    taserBulletProjectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()));
                    level.addFreshEntity(taserBulletProjectile);
                }

                stack.getOrCreateTag().putInt("fire_animation", 4);
                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
                ItemNBTTool.setInt(stack, "Power", ItemNBTTool.getInt(stack, "Power", 1200) - 400);
            }
        }
    }

    private static void handleM79Fire(Player player) {
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("reloading")) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getInt("ammo") > 0) {
                player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                    capability.recoil = 0.1;
                    capability.firing = 1;
                    capability.syncPlayerVariables(player);
                });

                Level level = player.level();
                if (!level.isClientSide()) {
                    int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), stack);
                    GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage") * (float) stack.getOrCreateTag().getDouble("damageadd"), monsterMultiple);

                    gunGrenadeEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    gunGrenadeEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()));
                    level.addFreshEntity(gunGrenadeEntity);
                }

                // TODO 移除指令
                if (!player.level().isClientSide() && player.getServer() != null) {
                    player.getServer().getCommands().performPrefixedCommand(
                            new CommandSourceStack(CommandSource.NULL, player.position(), player.getRotationVector(), (ServerLevel) player.level(), 4, player.getName().getString(), player.getDisplayName(),
                                    player.getServer(), player),
                            ("particle minecraft:cloud" + (" " + (player.getX() + 1.8 * player.getLookAngle().x)) + (" " + (player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y))
                                    + (" " + (player.getZ() + 1.8 * player.getLookAngle().z)) + " 0.1 0.1 0.1 0.002 4 force @s"));
                }
                player.getCooldowns().addCooldown(stack.getItem(), 2);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, TargetModSounds.M_79_FIRE_1P.get(), 2, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.M_79_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.M_79_FAR.get(), SoundSource.PLAYERS, 6, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.M_79_VERYFAR.get(), SoundSource.PLAYERS, 12, 1);
                }
                stack.getOrCreateTag().putInt("fire_animation", 2);
                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));
            }
        }
    }

    private static void handleRpgFire(Player player) {
        if (player.isSpectator()) return;

        Level level = player.level();
        ItemStack mainHandItem = player.getMainHandItem();
        CompoundTag tag = mainHandItem.getOrCreateTag();

        if (!tag.getBoolean("reloading") && !player.getCooldowns().isOnCooldown(mainHandItem.getItem()) && tag.getInt("ammo") > 0) {
            player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
                capability.recoil = 0.1;
                capability.firing = 1;
                capability.syncPlayerVariables(player);
            });

            if (!level.isClientSide()) {
                int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(TargetModEnchantments.MONSTER_HUNTER.get(), mainHandItem);
                RpgRocketEntity rocketEntity = new RpgRocketEntity(player, level, (float) tag.getDouble("damage") * (float) tag.getDouble("damageadd"), monsterMultiple);
                rocketEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                rocketEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) tag.getDouble("velocity"),
                        (float) player.getAttributeBaseValue(TargetModAttributes.SPREAD.get()));
                level.addFreshEntity(rocketEntity);
            }

            // TODO 移除指令
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

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, TargetModSounds.RPG_FIRE_1P.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 8, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 16, 1);
            }

            tag.putInt("fire_animation", 2);
            tag.putInt("ammo", tag.getInt("ammo") - 1);
        }
    }
}
