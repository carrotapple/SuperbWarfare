package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.entity.*;
import net.mcreator.superbwarfare.event.GunEventHandler;
import net.mcreator.superbwarfare.init.*;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
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
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.syncPlayerVariables(player);
            });

            if (player.getMainHandItem().getItem() == ModItems.BOCEK.get()) {
                handleBowShoot(player);
            }
        }
    }

    private static void handlePlayerShoot(Player player) {
        var handItem = player.getMainHandItem();

        if (!handItem.is(ModTags.Items.GUN)) {
            return;
        }

        var tag = handItem.getOrCreateTag();


        if (handItem.getItem() == ModItems.TASER.get()) {
            handleTaserFire(player);
        }

        if (handItem.getItem() == ModItems.M_79.get()) {
            handleM79Fire(player);
        }

        if (handItem.getItem() == ModItems.RPG.get()) {
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

        if (handItem.getItem() != ModItems.BOCEK.get()
                && handItem.getItem() != ModItems.MINIGUN.get()
                && tag.getInt("ammo") == 0
                && !tag.getBoolean("reloading")) {
            if (!player.level().isClientSide()) {
                SoundTool.playLocalSound(player, ModSounds.TRIGGER_CLICK.get(), 10, 1);
            }
        }

        if (handItem.getItem() == ModItems.MINIGUN.get()) {
            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).rifleAmmo == 0) {
                if (!player.level().isClientSide()) {
                    SoundTool.playLocalSound(player, ModSounds.TRIGGER_CLICK.get(), 10, 1);
                }
            }
        }

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.bowPullHold = true;
            capability.syncPlayerVariables(player);
        });

        // 栓动武器左键手动拉栓
        if (tag.getInt("bolt_action_time") > 0 && tag.getInt("ammo") > 0 && tag.getInt("bolt_action_anim") == 0) {
            if (!player.getCooldowns().isOnCooldown(handItem.getItem()) && handItem.getOrCreateTag().getBoolean("need_bolt_action")) {
                handItem.getOrCreateTag().putInt("bolt_action_anim", handItem.getOrCreateTag().getInt("bolt_action_time"));
                GunEventHandler.playGunBoltSounds(player);
            }
        }
    }

    private static void handleBowShoot(Player player) {
        ItemStack stack = player.getMainHandItem();

        double power = stack.getOrCreateTag().getDouble("power");

        if (player instanceof ServerPlayer serverPlayer) {
            SoundTool.stopSound(serverPlayer, ModSounds.BOCEK_PULL_1P.getId(), SoundSource.PLAYERS);
            SoundTool.stopSound(serverPlayer, ModSounds.BOCEK_PULL_3P.getId(), SoundSource.PLAYERS);
        }

        if (stack.getOrCreateTag().getDouble("power") >= 6) {
            stack.getOrCreateTag().putDouble("speed", stack.getOrCreateTag().getDouble("power"));
            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
                Level level = player.level();
                if (!level.isClientSide()) {
                    int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.MONSTER_HUNTER.get(), stack);
                    float damage = (float) (0.02 * stack.getOrCreateTag().getDouble("damage") * (1 + 0.05 * stack.getOrCreateTag().getInt("level")));
                    float bypassArmorRate = (float) stack.getOrCreateTag().getDouble("BypassArmor");

                    BocekArrowEntity arrow = new BocekArrowEntity(player, level, monsterMultiple).bypassArmorRate(bypassArmorRate);
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
                    SoundTool.playLocalSound(player, ModSounds.BOCEK_ZOOM_FIRE_1P.get(), 10, 1);
                    player.playSound(ModSounds.BOCEK_ZOOM_FIRE_3P.get(), 2, 1);
                }
            } else {
                stack.getOrCreateTag().putBoolean("shoot", true);

                for (int index0 = 0; index0 < 10; index0++) {
                    spawnBullet(player);
                }

                if (!player.level().isClientSide() && player.getServer() != null) {
                    SoundTool.playLocalSound(player, ModSounds.BOCEK_SHATTER_CAP_FIRE_1P.get(), 10, 1);
                    player.playSound(ModSounds.BOCEK_SHATTER_CAP_FIRE_3P.get(), 2, 1);
                }
            }

            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
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

        if (player.level().isClientSide()) return;

        CompoundTag tag = heldItem.getOrCreateTag();
        double damage;
        float headshot = (float) tag.getDouble("headshot");
        float velocity = 4 * (float) tag.getDouble("speed");
        int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.MONSTER_HUNTER.get(), heldItem);
        boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
        float bypassArmorRate = (float) heldItem.getOrCreateTag().getDouble("BypassesArmor");


        ProjectileEntity projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot)
                .zoom(zoom)
                .monsterMultiple(monsterMultiple);

        var perk = PerkHelper.getPerkByType(heldItem, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            bypassArmorRate += ammoPerk.bypassArmorRate;
            projectile.setRGB(ammoPerk.rgb);

            if (ammoPerk.mobEffect.get() != null) {
                int level = PerkHelper.getItemPerkLevel(perk, heldItem);
                projectile.effect(() -> new MobEffectInstance(ammoPerk.mobEffect.get(), 100, level - 1));
            }
        }
        bypassArmorRate = Mth.clamp(bypassArmorRate, 0, 1);

        projectile.bypassArmorRate(bypassArmorRate);

        if (perk == ModPerks.SILVER_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, heldItem);
            projectile.undeadMultiple(1.0f + 0.5f * level);
        } else if (perk == ModPerks.BEAST_BULLET.get()) {
            projectile.beast();
        }

        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        damage = 0.008333333 * tag.getDouble("damage") * tag.getDouble("speed") * tag.getDouble("levelDamageMultiple");
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

                player.getCooldowns().addCooldown(stack.getItem(), 5);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.TASER_FIRE_1P.get(), 1, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.TASER_FIRE_3P.get(), SoundSource.PLAYERS, 1, 1);
                }

                int volt = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.VOLT_OVERLOAD.get(), stack);
                int wire_length = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.LONGER_WIRE.get(), stack);

                boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zooming;
                double spread = stack.getOrCreateTag().getDouble("spread");
                double zoomSpread = stack.getOrCreateTag().getDouble("zoomSpread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    TaserBulletProjectileEntity taserBulletProjectile = new TaserBulletProjectileEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage"), volt, wire_length);

                    taserBulletProjectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    taserBulletProjectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) (zoom? zoomSpread : spread));
                    level.addFreshEntity(taserBulletProjectile);
                }

                stack.getOrCreateTag().putBoolean("shoot", true);

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

                boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zooming;
                double spread = stack.getOrCreateTag().getDouble("spread");
                double zoomSpread = stack.getOrCreateTag().getDouble("zoomSpread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.MONSTER_HUNTER.get(), stack);
                    GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage") * (float) stack.getOrCreateTag().getDouble("levelDamageMultiple"), monsterMultiple);

                    gunGrenadeEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    gunGrenadeEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) (zoom? zoomSpread : spread));
                    level.addFreshEntity(gunGrenadeEntity);
                }

                if (player.level() instanceof ServerLevel serverLevel) {
                    ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x,
                            player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                            player.getZ() + 1.8 * player.getLookAngle().z,
                            4, 0.1, 0.1, 0.1, 0.002, true);
                }
                player.getCooldowns().addCooldown(stack.getItem(), 2);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.M_79_FIRE_1P.get(), 2, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_FAR.get(), SoundSource.PLAYERS, 6, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_VERYFAR.get(), SoundSource.PLAYERS, 12, 1);
                }

                stack.getOrCreateTag().putBoolean("shoot", true);

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

            boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zooming;
            double spread = mainHandItem.getOrCreateTag().getDouble("spread");
            double zoomSpread = mainHandItem.getOrCreateTag().getDouble("zoomSpread");

            if (!level.isClientSide()) {
                int monsterMultiple = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.MONSTER_HUNTER.get(), mainHandItem);
                RpgRocketEntity rocketEntity = new RpgRocketEntity(player, level, (float) tag.getDouble("damage") * (float) tag.getDouble("levelDamageMultiple"), monsterMultiple);
                rocketEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                rocketEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) tag.getDouble("velocity"),
                        (float) (zoom? zoomSpread : spread));
                level.addFreshEntity(rocketEntity);
            }

            if (player.level() instanceof ServerLevel serverLevel) {
                ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x,
                        player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                        player.getZ() + 1.8 * player.getLookAngle().z,
                        30, 0.4, 0.4, 0.4, 0.005, true);
            }

            if (tag.getInt("ammo") == 1) {
                tag.putBoolean("empty", true);
                tag.putBoolean("close_hammer", true);
            }

            player.getCooldowns().addCooldown(mainHandItem.getItem(), 10);

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.RPG_FIRE_1P.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 8, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 16, 1);
            }

            tag.putBoolean("shoot", true);

            tag.putInt("fire_animation", 2);
            tag.putInt("ammo", tag.getInt("ammo") - 1);
        }
    }
}
