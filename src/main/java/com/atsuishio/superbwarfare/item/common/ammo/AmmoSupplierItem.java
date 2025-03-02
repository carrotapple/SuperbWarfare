package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.GunInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AmmoSupplierItem extends Item {

    public final GunInfo.Type type;
    public final int ammoToAdd;

    public AmmoSupplierItem(GunInfo.Type type, int ammoToAdd, Properties properties) {
        super(properties);
        this.type = type;
        this.ammoToAdd = ammoToAdd;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int count = stack.getCount();
        player.getCooldowns().addCooldown(this, 10);
        stack.shrink(count);

        ItemStack offhandItem = player.getOffhandItem();

        if (offhandItem.is(ModItems.AMMO_BOX.get())) {
            int newAmmoCount = switch (this.type) {
                case HANDGUN -> offhandItem.getOrCreateTag().getInt("HandgunAmmo");
                case RIFLE -> offhandItem.getOrCreateTag().getInt("RifleAmmo");
                case SHOTGUN -> offhandItem.getOrCreateTag().getInt("ShotgunAmmo");
                case SNIPER -> offhandItem.getOrCreateTag().getInt("SniperAmmo");
                case HEAVY -> offhandItem.getOrCreateTag().getInt("HeavyAmmo");
            } + ammoToAdd * count;
            switch (this.type) {
                case HANDGUN -> offhandItem.getOrCreateTag().putInt("HandgunAmmo", newAmmoCount);
                case RIFLE -> offhandItem.getOrCreateTag().putInt("RifleAmmo", newAmmoCount);
                case SHOTGUN -> offhandItem.getOrCreateTag().putInt("ShotgunAmmo", newAmmoCount);
                case SNIPER -> offhandItem.getOrCreateTag().putInt("SniperAmmo", newAmmoCount);
                case HEAVY -> offhandItem.getOrCreateTag().putInt("HeavyAmmo", newAmmoCount);
            }
        } else {
            player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                int newAmmoCount = switch (this.type) {
                    case HANDGUN -> capability.handgunAmmo;
                    case RIFLE -> capability.rifleAmmo;
                    case SHOTGUN -> capability.shotgunAmmo;
                    case SNIPER -> capability.sniperAmmo;
                    case HEAVY -> capability.heavyAmmo;
                } + ammoToAdd * count;
                switch (this.type) {
                    case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                    case RIFLE -> capability.rifleAmmo = newAmmoCount;
                    case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                    case SNIPER -> capability.sniperAmmo = newAmmoCount;
                    case HEAVY -> capability.heavyAmmo = newAmmoCount;
                }
                capability.syncPlayerVariables(player);
            });
        }

        if (!level.isClientSide()) {
            player.displayClientMessage(Component.translatable("item.superbwarfare.ammo_supplier.supply", Component.translatable(this.type.translatableKey), ammoToAdd * count), true);
            level.playSound(null, player.blockPosition(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 1, 1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
