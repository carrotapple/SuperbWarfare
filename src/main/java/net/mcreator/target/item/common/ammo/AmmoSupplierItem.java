package net.mcreator.target.item.common.ammo;

import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunInfo;
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
        player.getCooldowns().addCooldown(this, 20);
        stack.shrink(1);

        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            var newAmmoCount = player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> switch (this.type) {
                case HANDGUN -> c.handgunAmmo;
                case RIFLE -> c.rifleAmmo;
                case SHOTGUN -> c.shotgunAmmo;
                case SNIPER -> c.sniperAmmo;
            }).orElse(0) + ammoToAdd;
            switch (this.type) {
                case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                case RIFLE -> capability.rifleAmmo = newAmmoCount;
                case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                case SNIPER -> capability.sniperAmmo = newAmmoCount;
            }
            capability.syncPlayerVariables(player);
        });

        if (!level.isClientSide()) {
            player.displayClientMessage(Component.translatable("item.target.ammo_supplier.supply", Component.translatable(this.type.translatableKey).getString(), ammoToAdd), false);
            level.playSound(null, player.blockPosition(), TargetModSounds.BULLETSUPPLY.get(), SoundSource.PLAYERS, 1, 1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
