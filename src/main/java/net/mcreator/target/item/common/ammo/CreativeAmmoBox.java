package net.mcreator.target.item.common.ammo;

import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class CreativeAmmoBox extends Item {
    public CreativeAmmoBox() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 6;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("des.target.creative_ammo_box").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 20);
        stack.shrink(1);

        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.handgunAmmo = 2147483647;
            capability.rifleAmmo = 2147483647;
            capability.shotgunAmmo = 2147483647;
            capability.sniperAmmo = 2147483647;
            capability.syncPlayerVariables(player);
        });

        if (!level.isClientSide()) {
            player.displayClientMessage(Component.literal("All Ammo +2147483647"), false);
            level.playSound(null, player.blockPosition(), TargetModSounds.BULLETSUPPLY.get(), SoundSource.VOICE, 1, 1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
