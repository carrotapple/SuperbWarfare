package net.mcreator.superbwarfare.network.message;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.projectile.*;
import net.mcreator.superbwarfare.event.GunEventHandler;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.AmmoPerk;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.ParticleTool;
import net.mcreator.superbwarfare.tools.SeekTool;
import net.mcreator.superbwarfare.tools.SoundTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3d;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class FireMessage {
    private final int type;

    public FireMessage(int type) {
        this.type = type;
    }

    public FireMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
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

        if (type == 0) {
            handlePlayerShoot(player);
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
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

            if (player.getMainHandItem().getItem() == ModItems.DEVOTION.get()) {
                player.getMainHandItem().getOrCreateTag().putDouble("customRpm", 0);
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
            Entity seekingEntity = SeekTool.seekEntity(player, player.level(), 384, 8);
            if (seekingEntity != null) {
                tag.putString("TargetEntity", seekingEntity.getStringUUID());
                tag.putBoolean("Seeking", true);
                tag.putInt("SeekTime", 0);
            }

        }

        if (tag.getInt("fire_mode") == 1) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.holdFire = false;
                capability.syncPlayerVariables(player);
            });
            tag.putInt("burst_fire", (int) tag.getDouble("burst_size"));
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

        if (handItem.getItem() == ModItems.BOCEK.get()) {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.bowPullHold = true;
                capability.syncPlayerVariables(player);
            });
        }

        // 栓动武器左键手动拉栓
        if (tag.getInt("bolt_action_time") > 0 && tag.getInt("ammo") > 0 && tag.getInt("bolt_action_anim") == 0) {
            if (!player.getCooldowns().isOnCooldown(handItem.getItem()) && handItem.getOrCreateTag().getBoolean("need_bolt_action")) {
                handItem.getOrCreateTag().putInt("bolt_action_anim", handItem.getOrCreateTag().getInt("bolt_action_time") + 1);
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
                spawnFakeArrow(player);

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
            stack.getOrCreateTag().putInt("fire_animation", 2);

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
        ItemStack heldItem = player.getMainHandItem();

        if (player.level().isClientSide()) return;

        CompoundTag tag = heldItem.getOrCreateTag();
        var perk = PerkHelper.getPerkByType(heldItem, Perk.Type.AMMO);
        float headshot = (float) tag.getDouble("headshot");
        float velocity = 2 * (float) tag.getDouble("speed") * (float)perkSpeed(heldItem);
        float bypassArmorRate = (float) heldItem.getOrCreateTag().getDouble("BypassesArmor");
        double damage;
        boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).zoom;


        float spread;

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zoom) {
            spread = 0.01f;
            damage = 0.08333333 * tag.getDouble("damage") * tag.getDouble("speed") * perkDamage(heldItem);
        } else {
            spread = perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 1 : 2.5f;
            damage = (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 0.08333333 : 0.008333333) * tag.getDouble("damage") * tag.getDouble("speed") * perkDamage(heldItem);
        }

        ProjectileEntity projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot)
                .zoom(zoom);


        if (perk instanceof AmmoPerk ammoPerk) {
            int level = PerkHelper.getItemPerkLevel(perk, heldItem);

            bypassArmorRate += ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
            projectile.setRGB(ammoPerk.rgb);

            if (ammoPerk.mobEffect.get() != null) {
                projectile.effect(() -> new MobEffectInstance(ammoPerk.mobEffect.get(), 70 + 30 * level, level - 1));
            }
        }

        bypassArmorRate = Math.max(bypassArmorRate, 0);
        projectile.bypassArmorRate(bypassArmorRate);

        if (perk == ModPerks.SILVER_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, heldItem);
            projectile.undeadMultiple(1.0f + 0.5f * level);
        } else if (perk == ModPerks.BEAST_BULLET.get()) {
            projectile.beast();
        } else if (perk == ModPerks.JHP_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, heldItem);
            projectile.jhpBullet(true, level);
        } else if (perk == ModPerks.HE_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, heldItem);
            projectile.heBullet(true, level);
        }

        var dmgPerk = PerkHelper.getPerkByType(heldItem, Perk.Type.DAMAGE);
        if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
            int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, heldItem);
            projectile.monsterMultiple(0.1f + 0.1f * perkLevel);
        }

        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, spread);

        projectile.damage((float) damage);

        player.level().addFreshEntity(projectile);
    }

    private static void spawnFakeArrow(Player player) {

        ItemStack heldItem = player.getMainHandItem();
        CompoundTag tag = heldItem.getOrCreateTag();

        float velocity = 2 * (float) tag.getDouble("speed") * (float)perkSpeed(heldItem);

        BocekArrowEntity arrow = new BocekArrowEntity(player, player.level());
        arrow.setBaseDamage(0);
        arrow.setKnockback(0);
        arrow.setSilent(true);
        arrow.setPierceLevel((byte) 2);
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;

        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 0);
        player.level().addFreshEntity(arrow);
    }

    private static void handleTaserFire(Player player) {
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("reloading")) {
            int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.VOLT_OVERLOAD.get(), stack);
            AtomicBoolean flag = new AtomicBoolean(false);
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                    iEnergyStorage -> flag.set(iEnergyStorage.getEnergyStored() > 2000 + 200 * perkLevel)
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
                double spread = stack.getOrCreateTag().getDouble("spread");
                double zoomSpread = stack.getOrCreateTag().getDouble("zoomSpread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    TaserBulletProjectileEntity taserBulletProjectile = new TaserBulletProjectileEntity(player, level, (float) stack.getOrCreateTag().getDouble("damage"), volt, wireLength);

                    taserBulletProjectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    taserBulletProjectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) (zoom ? zoomSpread : spread));
                    level.addFreshEntity(taserBulletProjectile);
                }

                stack.getOrCreateTag().putInt("fire_animation", 4);
                stack.getOrCreateTag().putInt("ammo", (stack.getOrCreateTag().getInt("ammo") - 1));

                stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(
                        energy -> energy.extractEnergy(2000 + 200 * perkLevel, false)
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
                double spread = stack.getOrCreateTag().getDouble("spread");
                double zoomSpread = stack.getOrCreateTag().getDouble("zoomSpread");

                Level level = player.level();
                if (!level.isClientSide()) {
                    GunGrenadeEntity gunGrenadeEntity = new GunGrenadeEntity(player, level,
                            (float) stack.getOrCreateTag().getDouble("damage"));

                    var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
                    if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                        int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                        gunGrenadeEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
                    }

                    gunGrenadeEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    gunGrenadeEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) stack.getOrCreateTag().getDouble("velocity"),
                            (float) (zoom ? zoomSpread : spread));
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

                stack.getOrCreateTag().putInt("fire_animation", 2);
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
            double spread = stack.getOrCreateTag().getDouble("spread");
            double zoomSpread = stack.getOrCreateTag().getDouble("zoomSpread");

            if (!level.isClientSide()) {
                RpgRocketEntity rocketEntity = new RpgRocketEntity(player, level,
                        (float) tag.getDouble("damage"));

                var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
                if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                    int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                    rocketEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
                }

                rocketEntity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                rocketEntity.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) tag.getDouble("velocity"),
                        (float) (zoom ? zoomSpread : spread));
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

            tag.putInt("fire_animation", 2);
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
                    (float) tag.getDouble("damage"));

            var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
            if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                missileEntity.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
            }

            missileEntity.setPos(player.getX() + firePos.x, player.getEyeY() + firePos.y, player.getZ() + firePos.z);
            missileEntity.shoot(player.getLookAngle().x, player.getLookAngle().y + 0.3, player.getLookAngle().z, 3f, 1);
            missileEntity.setTargetUuid(tag.getString("TargetEntity"));
            missileEntity.setAttackMode(tag.getBoolean("TopMode"));
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

        tag.putInt("fire_animation", 2);
        tag.putInt("ammo", tag.getInt("ammo") - 1);

        if (player.level() instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
        }
    }
}
