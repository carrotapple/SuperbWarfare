package com.atsuishio.superbwarfare.item.gun.launcher;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.client.renderer.item.RpgItemRenderer;
import com.atsuishio.superbwarfare.client.tooltip.component.LauncherImageComponent;
import com.atsuishio.superbwarfare.entity.projectile.RpgRocketEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.gun.SpecialFireWeapon;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.ShootClientMessage;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class RpgItem extends GunItem implements GeoItem, SpecialFireWeapon {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static ItemDisplayContext transformType;

    public RpgItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new RpgItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
                return PoseTool.pose(entityLiving, hand, stack);
            }
        });
    }

    public void getTransformType(ItemDisplayContext type) {
        transformType = type;
    }

    private PlayState idlePredicate(AnimationState<RpgItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return PlayState.STOP;

        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.rpg.reload"));
        }

        if (player.isSprinting() && player.onGround() && player.getPersistentData().getDouble("noRun") == 0 && ClientEventHandler.drawTime < 0.01) {
            if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.rpg.run_fast"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.rpg.run"));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.rpg.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var idleController = new AnimationController<>(this, "idleController", 4, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static int getAmmoCount(Player player) {
        int count = 0;
        for (var inv : player.getInventory().items) {
            if (inv.is(ModItems.CREATIVE_AMMO_BOX.get())) {
                count++;
            }
        }

        if (count == 0) {
            int sum = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = player.getInventory().getItem(i);
                if (check(itemstack)) {
                    sum += itemstack.getCount();
                }
            }
            return sum;
        }
        return (int) Double.POSITIVE_INFINITY;
    }

    @Override
    public Set<SoundEvent> getReloadSound() {
        return Set.of(ModSounds.RPG_RELOAD_EMPTY.get());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (stack.getOrCreateTag().getBoolean("draw")) {
            stack.getOrCreateTag().putBoolean("draw", false);

            if (GunsTool.getGunIntTag(stack, "Ammo") == 0) {
                stack.getOrCreateTag().putDouble("empty", 1);
            }
        }

        if (entity instanceof Player player) {
            GunsTool.setGunIntTag(stack, "MaxAmmo", getAmmoCount(player));
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    protected static boolean check(ItemStack stack) {
        return stack.getItem() == ModItems.ROCKET.get();
    }

    @Override
    public ResourceLocation getGunIcon() {
        return ModUtils.loc("textures/gun_icon/rpg_icon.png");
    }

    @Override
    public String getGunDisplayName() {
        return "RPG-7";
    }

    @Override
    public boolean canApplyPerk(Perk perk) {
        return PerkHelper.LAUNCHER_PERKS.test(perk) || perk == ModPerks.MICRO_MISSILE.get();
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new LauncherImageComponent(pStack));
    }

    @Override
    public boolean isMagazineReload(ItemStack stack) {
        return true;
    }

    @Override
    public void fireOnPress(Player player) {
        Level level = player.level();
        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = stack.getOrCreateTag();

        if (GunsTool.getGunBooleanTag(stack, "Reloading")
                || player.getCooldowns().isOnCooldown(stack.getItem())
                || GunsTool.getGunIntTag(stack, "Ammo") <= 0
        ) return;

        boolean zoom = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> c.zoom).orElse(false);
        double spread = GunsTool.getGunDoubleTag(stack, "Spread");

        if (player.level() instanceof ServerLevel serverLevel) {
            RpgRocketEntity rocket = new RpgRocketEntity(player, level,
                    (float) GunsTool.getGunDoubleTag(stack, "Damage"),
                    (float) GunsTool.getGunDoubleTag(stack, "ExplosionDamage"),
                    (float) GunsTool.getGunDoubleTag(stack, "ExplosionRadius"));

            var dmgPerk = PerkHelper.getPerkByType(stack, Perk.Type.DAMAGE);
            if (dmgPerk == ModPerks.MONSTER_HUNTER.get()) {
                int perkLevel = PerkHelper.getItemPerkLevel(dmgPerk, stack);
                rocket.setMonsterMultiplier(0.1f + 0.1f * perkLevel);
            }

            float velocity = (float) GunsTool.getGunDoubleTag(stack, "Velocity");

            if (PerkHelper.getPerkByType(stack, Perk.Type.AMMO) == ModPerks.MICRO_MISSILE.get()) {
                rocket.setNoGravity(true);

                int perkLevel = PerkHelper.getItemPerkLevel(ModPerks.MICRO_MISSILE.get(), stack);
                if (perkLevel > 0) {
                    rocket.setExplosionRadius(0.5f);
                    rocket.setDamage((float) GunsTool.getGunDoubleTag(stack, "Damage") * (1.1f + perkLevel * 0.1f));
                    velocity *= 1.2f;
                }
            }

            rocket.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            rocket.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity,
                    (float) (zoom ? 0.1 : spread));
            level.addFreshEntity(rocket);

            ParticleTool.sendParticle(serverLevel, ParticleTypes.CLOUD, player.getX() + 1.8 * player.getLookAngle().x,
                    player.getY() + player.getBbHeight() - 0.1 + 1.8 * player.getLookAngle().y,
                    player.getZ() + 1.8 * player.getLookAngle().z,
                    30, 0.4, 0.4, 0.4, 0.005, true);

            var serverPlayer = (ServerPlayer) player;

            SoundTool.playLocalSound(serverPlayer, ModSounds.RPG_FIRE_1P.get(), 2, 1);
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FIRE_3P.get(), SoundSource.PLAYERS, 2, 1);
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_FAR.get(), SoundSource.PLAYERS, 5, 1);
            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), ModSounds.RPG_VERYFAR.get(), SoundSource.PLAYERS, 10, 1);

            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShootClientMessage(10));
        }

        if (GunsTool.getGunIntTag(stack, "Ammo") == 1) {
            tag.putBoolean("empty", true);
            GunsTool.setGunBooleanTag(stack, "CloseHammer", true);
        }

        player.getCooldowns().addCooldown(stack.getItem(), 10);
        GunsTool.setGunIntTag(stack, "Ammo", GunsTool.getGunIntTag(stack, "Ammo") - 1);
    }
}