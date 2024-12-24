package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.projectile.*;
import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class FireMessage {

    private final int type;

    public FireMessage(int type) {
        this.type = type;
    }

    public static FireMessage decode(FriendlyByteBuf buffer) {
        return new FireMessage(buffer.readInt());
    }

    public static void encode(FireMessage message, FriendlyByteBuf buffer) {
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

        handleGunBolt(player, player.getMainHandItem());

        if (type == 0) {
            handlePlayerShoot(player);
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.edit = false;
                capability.holdFire = true;
                capability.syncPlayerVariables(player);
            });
        } else if (type == 1) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = false;
                capability.holdFire = false;
                capability.syncPlayerVariables(player);
            });

            if (player.getMainHandItem().getItem() == ModItems.BOCEK.get()) {
                handleBowShoot(player);
            }

            if (player.getMainHandItem().getItem() == ModItems.JAVELIN.get()) {
                var handItem = player.getMainHandItem();
                var tag = handItem.getOrCreateTag();
                handleJavelinFire(player);
                tag.putBoolean("Seeking", false);
                tag.putInt("SeekTime", 0);
                tag.putString("TargetEntity", "none");
                if (player instanceof ServerPlayer serverPlayer) {
                    var clientboundstopsoundpacket = new ClientboundStopSoundPacket(new ResourceLocation(ModUtils.MODID, "javelin_lock"), SoundSource.PLAYERS);
                    serverPlayer.connection.send(clientboundstopsoundpacket);
                }
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

        if (handItem.getItem() == ModItems.JAVELIN.get() && player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom && tag.getInt("ammo") > 0) {
            Entity seekingEntity = SeekTool.seekEntity(player, player.level(), 512, 8);
            if (seekingEntity != null) {
                tag.putString("TargetEntity", seekingEntity.getStringUUID());
                tag.putBoolean("Seeking", true);
                tag.putInt("SeekTime", 0);
            }
        }

        if (tag.getDouble("prepare") == 0 && tag.getBoolean("reloading") && tag.getInt("ammo") > 0) {
            tag.putDouble("force_stop", 1);
        }

        if (handItem.getItem() == ModItems.BOCEK.get()) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = true;
                capability.syncPlayerVariables(player);
            });
        }
    }

    private static void handleGunBolt(Player player, ItemStack stack) {
        if (stack.getOrCreateTag().getInt("bolt_action_time") > 0 && stack.getOrCreateTag().getInt("ammo") > (stack.is(ModTags.Items.REVOLVER) ? -1 : 0) && stack.getOrCreateTag().getInt("bolt_action_anim") == 0
                && !(stack.getOrCreateTag().getBoolean("is_normal_reloading") || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                && !stack.getOrCreateTag().getBoolean("reloading")
                && !stack.getOrCreateTag().getBoolean("charging")) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getBoolean("need_bolt_action")) {
                stack.getOrCreateTag().putInt("bolt_action_anim", stack.getOrCreateTag().getInt("bolt_action_time") + 1);
                GunEventHandler.playGunBoltSounds(player);
            }
        }
    }

    public static double perkDamage(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            return ammoPerk.damageRate;
        }
        return 1;
    }

    public static double perkSpeed(ItemStack stack) {
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        if (perk instanceof AmmoPerk ammoPerk) {
            return ammoPerk.speedRate;
        }
        return 1;
    }

    private static void handleBowShoot(Player player) {
        if (player.level().isClientSide()) return;

        ItemStack stack = player.getMainHandItem();
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);

        if (player instanceof ServerPlayer serverPlayer) {
            SoundTool.stopSound(serverPlayer, ModSounds.BOCEK_PULL_1P.getId(), SoundSource.PLAYERS);
            SoundTool.stopSound(serverPlayer, ModSounds.BOCEK_PULL_3P.getId(), SoundSource.PLAYERS);
        }

        if (stack.getOrCreateTag().getDouble("power") >= 6) {
            stack.getOrCreateTag().putDouble("speed", stack.getOrCreateTag().getDouble("power"));
            if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
                spawnBullet(player);

                SoundTool.playLocalSound(player, ModSounds.BOCEK_ZOOM_FIRE_1P.get(), 10, 1);
                player.playSound(ModSounds.BOCEK_ZOOM_FIRE_3P.get(), 2, 1);
            } else {
                for (int index0 = 0; index0 < (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 1 : 10); index0++) {
                    spawnBullet(player);
                }

                SoundTool.playLocalSound(player, ModSounds.BOCEK_SHATTER_CAP_FIRE_1P.get(), 10, 1);
                player.playSound(ModSounds.BOCEK_SHATTER_CAP_FIRE_3P.get(), 2, 1);
            }

            if (perk == ModPerks.BEAST_BULLET.get()) {
                player.playSound(ModSounds.HENG.get(), 4f, 1f);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.HENG.get(), 4f, 1f);
                }
            }

            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 7);
            player.getMainHandItem().getOrCreateTag().putInt("arrow_empty", 7);
            player.getMainHandItem().getOrCreateTag().putDouble("power", 0);

            int count = 0;
            for (var inv : player.getInventory().items) {
                if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                    count++;
                }
            }

            if (count == 0 && !player.isCreative()) {
                player.getInventory().clearOrCountMatchingItems(p -> Items.ARROW == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            }

            if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
            }
        }
    }

    private static void spawnBullet(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (player.level().isClientSide()) return;

        CompoundTag tag = stack.getOrCreateTag();
        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        float headshot = (float) GunsTool.getGunDoubleTag(stack, "Headshot", 0);
        float velocity = 2 * (float) tag.getDouble("speed") * (float) perkSpeed(stack);
        float bypassArmorRate = (float) GunsTool.getGunDoubleTag(stack, "BypassesArmor", 0);
        double damage;
        boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;

        float spread;
        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
            spread = 0.01f;
            damage = 0.08333333 * GunsTool.getGunDoubleTag(stack, "Damage", 0) * tag.getDouble("speed") * perkDamage(stack);
        } else {
            spread = perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 0.5f : 2.5f;
            damage = (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 0.08333333 : 0.008333333) *
                    GunsTool.getGunDoubleTag(stack, "Damage", 0) * tag.getDouble("speed") * perkDamage(stack);
        }

        ProjectileEntity projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot)
                .zoom(zoom);

        if (perk instanceof AmmoPerk ammoPerk) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);

            bypassArmorRate += ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
            projectile.setRGB(ammoPerk.rgb);

            if (!ammoPerk.mobEffects.get().isEmpty()) {
                ArrayList<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                for (MobEffect effect : ammoPerk.mobEffects.get()) {
                    mobEffectInstances.add(new MobEffectInstance(effect, 70 + 30 * level, level - 1));
                }
                projectile.effect(mobEffectInstances);
            }
        }

        bypassArmorRate = Math.max(bypassArmorRate, 0);
        projectile.bypassArmorRate(bypassArmorRate);

        if (perk == ModPerks.SILVER_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.undeadMultiple(1.0f + 0.5f * level);
        } else if (perk == ModPerks.BEAST_BULLET.get()) {
            projectile.beast();
        } else if (perk == ModPerks.JHP_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.jhpBullet(true, level);
        } else if (perk == ModPerks.HE_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.heBullet(true, level);
        } else if (perk == ModPerks.INCENDIARY_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.fireBullet(true, level, !zoom);
        }

        var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
        if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
            int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
            projectile.monsterMultiple(0.1f + 0.1f * perkLevel);
        }

        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        projectile.shoot(player, player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (!zoom && perk == ModPerks.INCENDIARY_BULLET.get() ? 0.2f : 1) * velocity, spread);

        projectile.damage((float) damage);

        player.level().addFreshEntity(projectile);
    }

    private static void handleTaserFire(Player player) {
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("reloading")) {
            int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.VOLT_OVERLOAD.get(), stack);
            AtomicBoolean flag = new AtomicBoolean(false);
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                    iEnergyStorage -> flag.set(iEnergyStorage.getEnergyStored() >= 400 + 100 * perkLevel)
            );

            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getInt("ammo") > 0
                    && flag.get()) {

                player.getCooldowns().addCooldown(stack.getItem(), 5);

                if (player instanceof ServerPlayer serverPlayer) {
                    SoundTool.playLocalSound(serverPlayer, ModSounds.TASER_FIRE_1P.get(), 1, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.TASER_FIRE_3P.get(), SoundSource.PLAYERS, 1, 1);
                }

                int volt = PerkHelper.getItemPerkLevel(ModPerks.VOLT_OVERLOAD.get(), stack);
                int wireLength = PerkHelper.getItemPerkLevel(ModPerks.LONGER_WIRE.get(), stack);

                boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
                double spread = GunsTool.getGunDoubleTag(stack, "Spread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    TaserBulletProjectileEntity taserBulletProjectile = new TaserBulletProjectileEntity(player, level,
                            (float) GunsTool.getGunDoubleTag(stack, "Damage", 0), volt, wireLength);

                    taserBulletProjectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    taserBulletProjectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) GunsTool.getGunDoubleTag(stack, "Velocity", 0),
                            (float) (zoom ? 0.1 : spread));
                    level.addFreshEntity(taserBulletProjectile);
                }

                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));

                stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                        energy -> energy.extractEnergy(400 + 100 * perkLevel, false)
                );

                stack.getOrCreateTag().putBoolean("shoot", true);
                if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
                }
            }
        }
    }

    private static void handleM79Fire(Player player) {
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("reloading")) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.getOrCreateTag().getInt("ammo") > 0) {
                boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
                double spread = GunsTool.getGunDoubleTag(stack, "Spread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, level,
                            (float) GunsTool.getGunDoubleTag(stack, "Damage", 0),
                            (float) GunsTool.getGunDoubleTag(stack, "ExplosionDamage", 0),
                            (float) GunsTool.getGunDoubleTag(stack, "ExplosionRadius", 0));

                    var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
                    if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                        int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                        gunGrenadeEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
                    }

                    gunGrenadeEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    gunGrenadeEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) GunsTool.getGunDoubleTag(stack, "Velocity", 0),
                            (float) (zoom ? 0.1 : spread));
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
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_FIRE_3P.get(), SoundSource.PLAYERS, 2, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_FAR.get(), SoundSource.PLAYERS, 5, 1);
                    serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.M_79_VERYFAR.get(), SoundSource.PLAYERS, 10, 1);
                }

                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));

                if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
                    ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
                }
            }
        }
    }

    private static void handleRpgFire(Player player) {
        if (player.isSpectator()) return;

        Level level = player.level();
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.getBoolean("reloading") && !player.getCooldowns().isOnCooldown(stack.getItem()) && tag.getInt("ammo") > 0) {
            boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;
            double spread = GunsTool.getGunDoubleTag(stack, "Spread");

            if (!level.isClientSide()) {
                RpgRocketEntity rocketEntity = new RpgRocketEntity(player, level,
                        (float) GunsTool.getGunDoubleTag(stack, "Damage", 0),
                        (float) GunsTool.getGunDoubleTag(stack, "ExplosionDamage", 0),
                        (float) GunsTool.getGunDoubleTag(stack, "ExplosionRadius", 0));

                var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
                if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                    int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                    rocketEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
                }

                rocketEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                rocketEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) GunsTool.getGunDoubleTag(stack, "Velocity", 0),
                        (float) (zoom ? 0.1 : spread));
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

            player.getCooldowns().addCooldown(stack.getItem(), 10);

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, ModSounds.RPG_FIRE_1P.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 5, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 10, 1);
            }

            tag.putInt("ammo", tag.getInt("ammo") - 1);

            if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
            }
        }
    }

    private static void handleJavelinFire(Player player) {
        if (player.isSpectator()) return;

        Level level = player.level();
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.getInt("SeekTime") < 20) return;

        float yRot = player.getYRot();
        if (yRot < 0) {
            yRot += 360;
        }
        yRot = yRot + 90 % 360;

        var firePos = new Vector3d(0, -0.2, 0.15);
        firePos.rotateZ(-player.getXRot() * Mth.DEG_TO_RAD);
        firePos.rotateY(-yRot * Mth.DEG_TO_RAD);

        if (!level.isClientSide()) {
            JavelinMissileEntity missileEntity = new JavelinMissileEntity(player, level,
                    (float) GunsTool.getGunDoubleTag(stack, "Damage", 0),
                    (float) GunsTool.getGunDoubleTag(stack, "ExplosionDamage", 0),
                    (float) GunsTool.getGunDoubleTag(stack, "ExplosionRadius", 0));

            var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
            if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                missileEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
            }

            Entity targetEntity = EntityFindUtil.findEntity(player.level(), tag.getString("TargetEntity"));

            missileEntity.setPos(player.getX() + firePos.x, player.getEyeY() + firePos.y, player.getZ() + firePos.z);
            missileEntity.shoot(player.getLookAngle().x, player.getLookAngle().y + 0.3, player.getLookAngle().z, 3f, 1);
            missileEntity.setTargetUuid(tag.getString("TargetEntity"));
            missileEntity.setAttackMode(tag.getBoolean("TopMode"));
            if (targetEntity != null) {
                missileEntity.setTargetPosition((float) targetEntity.getX(), (float) targetEntity.getEyeY(), (float) targetEntity.getZ());
            }
            level.addFreshEntity(missileEntity);
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x,
                    player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                    player.getZ() + 1.8 * player.getLookAngle().z,
                    30, 0.4, 0.4, 0.4, 0.005, true);
        }

        player.getCooldowns().addCooldown(stack.getItem(), 10);

        if (player instanceof ServerPlayer serverPlayer) {
            SoundTool.playLocalSound(serverPlayer, ModSounds.JAVELIN_FIRE_1P.get(), 2, 1);
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.JAVELIN_FIRE_3P.get(), SoundSource.PLAYERS, 4, 1);
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.JAVELIN_FAR.get(), SoundSource.PLAYERS, 10, 1);
        }

        tag.putInt("ammo", tag.getInt("ammo") - 1);

        if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
        }
    }
}
