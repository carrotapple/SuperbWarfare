package net.mcreator.superbwarfare.item.common.ammo;

import net.mcreator.superbwarfare.init.ModSounds;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmmoBox extends Item {
    public AmmoBox() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        player.getCooldowns().addCooldown(this, 10);

        var cap =player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

            if (!player.isCrouching()) {

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 1) {
                    capability.rifleAmmo = cap.rifleAmmo + tag.getInt("rifleAmmo");
                    tag.putInt("rifleAmmo",0);
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 2) {
                    capability.handgunAmmo = cap.handgunAmmo + tag.getInt("handgunAmmo");
                    tag.putInt("handgunAmmo",0);
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 3) {
                    capability.shotgunAmmo = cap.shotgunAmmo + tag.getInt("shotgunAmmo");
                    tag.putInt("shotgunAmmo",0);
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 4) {
                    capability.sniperAmmo = cap.sniperAmmo + tag.getInt("sniperAmmo");
                    tag.putInt("sniperAmmo",0);
                }

                capability.syncPlayerVariables(player);

                if (!level.isClientSide()) {
                    level.playSound(null, player.blockPosition(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 1, 1);
                }

                if (tag.getBoolean("isDrop")) {
                    stack.shrink(1);
                }

            } else {

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 1) {
                    tag.putInt("rifleAmmo",tag.getInt("rifleAmmo") + cap.rifleAmmo);
                    capability.rifleAmmo = 0;
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 2) {
                    tag.putInt("handgunAmmo",tag.getInt("handgunAmmo") + cap.handgunAmmo);
                    capability.handgunAmmo = 0;
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 3) {
                    tag.putInt("shotgunAmmo",tag.getInt("shotgunAmmo") + cap.shotgunAmmo);
                    capability.shotgunAmmo = 0;
                }

                if (stack.getOrCreateTag().getInt("type") == 0 || stack.getOrCreateTag().getInt("type") == 4) {
                    tag.putInt("sniperAmmo",tag.getInt("sniperAmmo") + cap.sniperAmmo);
                    capability.sniperAmmo = 0;
                }

                 capability.syncPlayerVariables(player);

                if (!level.isClientSide()) {
                    level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1, 1);
                }

            }

        });
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
        boolean retval = super.onEntitySwing(itemstack, entity);

        if (entity instanceof Player player && player.isCrouching()) {

            itemstack.getOrCreateTag().putInt("type",itemstack.getOrCreateTag().getInt("type") + 1);
            if (itemstack.getOrCreateTag().getInt("type") > 4) {
                itemstack.getOrCreateTag().putInt("type",0);
            }

            if (itemstack.getOrCreateTag().getInt("type") == 0) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.all").withStyle(ChatFormatting.WHITE),true);
            }
            if (itemstack.getOrCreateTag().getInt("type") == 1) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.rifle").withStyle(ChatFormatting.GREEN),true);
            }
            if (itemstack.getOrCreateTag().getInt("type") == 2) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.handgun").withStyle(ChatFormatting.AQUA),true);
            }
            if (itemstack.getOrCreateTag().getInt("type") == 3) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.shotgun").withStyle(ChatFormatting.RED),true);
            }
            if (itemstack.getOrCreateTag().getInt("type") == 4) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.sniper").withStyle(ChatFormatting.GOLD),true);
            }

            entity.playSound(ModSounds.FIRE_RATE.get(), 1f, 1f);
        }
        return retval;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        TooltipTool.ammoBoxTips(list, stack);
    }

}
