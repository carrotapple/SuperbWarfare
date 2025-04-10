package com.atsuishio.superbwarfare.network.message;

import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunData;
import com.atsuishio.superbwarfare.item.gun.SpecialFireWeapon;
import com.atsuishio.superbwarfare.item.gun.special.BocekItem;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
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
        if (player.isSpectator()) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        handleGunBolt(player, stack);

        if (type == 0) {
            var tag = stack.getOrCreateTag();
            var data = GunData.from(stack);
            if (tag.getDouble("prepare") == 0 && data.isReloading() && data.getAmmo() > 0) {
                tag.putDouble("force_stop", 1);
            }

            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.edit = false;
                capability.syncPlayerVariables(player);
            });

            // 按下开火
            if (!(stack.getItem() instanceof SpecialFireWeapon specialFireWeapon)) return;
            specialFireWeapon.fireOnPress(player, ClientEventHandler.zoom);
        } else if (type == 1) {
            // 松开开火
            if (stack.getItem() instanceof SpecialFireWeapon specialFireWeapon) {
                if (specialFireWeapon instanceof BocekItem) {
                    specialFireWeapon.fireOnRelease(player, ClientEventHandler.bowTimer, ClientEventHandler.zoom);
                } else {
                    specialFireWeapon.fireOnRelease(player, 0, ClientEventHandler.zoom);
                }
            }
        }
    }

    private static void handleGunBolt(Player player, ItemStack stack) {
        if (!stack.is(ModTags.Items.GUN)) return;
        var data = GunData.from(stack);

        if (data.boltActionTime() > 0
                && data.getAmmo() > (stack.is(ModTags.Items.REVOLVER) ? -1 : 0)
                && GunsTool.getGunIntTag(stack, "BoltActionTick") == 0
                && !(stack.getOrCreateTag().getBoolean("is_normal_reloading")
                || stack.getOrCreateTag().getBoolean("is_empty_reloading"))
                && !data.isReloading()
                && !GunsTool.getGunBooleanTag(stack, "Charging")) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && GunsTool.getGunBooleanTag(stack, "NeedBoltAction")) {
                GunsTool.setGunIntTag(stack, "BoltActionTick", data.boltActionTime() + 1);
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

    public static void spawnBullet(Player player, double power, boolean zoom) {
        ItemStack stack = player.getMainHandItem();
        if (player.level().isClientSide()) return;
        var data = GunData.from(stack);

        var perk = PerkHelper.getPerkByType(stack, Perk.Type.AMMO);
        float headshot = (float) data.headshot();
        float velocity = (float) (24 * power * (float) perkSpeed(stack));
        float bypassArmorRate = (float) data.bypassArmor();
        double damage;

        float spread;
        if (zoom) {
            spread = 0.01f;
            damage = 0.08333333 * data.damage() *
                    12 * power * perkDamage(stack);
        } else {
            spread = perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 0.5f : 2.5f;
            damage = (perk instanceof AmmoPerk ammoPerk && ammoPerk.slug ? 0.08333333 : 0.008333333) *
                    data.damage() * 12 * power * perkDamage(stack);
        }

        ProjectileEntity projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot)
                .zoom(zoom)
                .setGunItemId(stack);

        if (perk instanceof AmmoPerk ammoPerk) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);

            bypassArmorRate += ammoPerk.bypassArmorRate + (perk == ModPerks.AP_BULLET.get() ? 0.05f * (level - 1) : 0);
            projectile.setRGB(ammoPerk.rgb);

            if (!ammoPerk.mobEffects.get().isEmpty()) {
                int amplifier;
                if (perk.descriptionId.equals("blade_bullet")) {
                    amplifier = level / 3;
                } else if (perk.descriptionId.equals("bread_bullet")) {
                    amplifier = 1;
                } else {
                    amplifier = level - 1;
                }

                ArrayList<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                for (MobEffect effect : ammoPerk.mobEffects.get()) {
                    mobEffectInstances.add(new MobEffectInstance(effect, 70 + 30 * level, amplifier));
                }
                projectile.effect(mobEffectInstances);
            }

            if (perk.descriptionId.equals("bread_bullet")) {
                projectile.knockback(level * 0.3f);
                projectile.forceKnockback();
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
            projectile.jhpBullet(level);
        } else if (perk == ModPerks.HE_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.heBullet(level);
        } else if (perk == ModPerks.INCENDIARY_BULLET.get()) {
            int level = PerkHelper.getItemPerkLevel(perk, stack);
            projectile.fireBullet(level, !zoom);
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

}
