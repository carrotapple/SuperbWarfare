package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.GunInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AmmoSupplierItem extends Item {

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

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            var newAmmoCount = switch (this.type) {
                case HANDGUN -> capability.handgunAmmo;
                case RIFLE -> capability.rifleAmmo;
                case SHOTGUN -> capability.shotgunAmmo;
                case SNIPER -> capability.sniperAmmo;
            } + ammoToAdd * count;
            switch (this.type) {
                case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                case RIFLE -> capability.rifleAmmo = newAmmoCount;
                case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                case SNIPER -> capability.sniperAmmo = newAmmoCount;
            }
            capability.syncPlayerVariables(player);
        });

        if (!level.isClientSide()) {
            player.displayClientMessage(Component.translatable("item.superbwarfare.ammo_supplier.supply", Component.translatable(this.type.translatableKey).getString(), ammoToAdd * count), false);
            level.playSound(null, player.blockPosition(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 1, 1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
