package com.atsuishio.superbwarfare.item.common.ammo;

import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.atsuishio.superbwarfare.init.ModSounds;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class AmmoBox extends Item {
    public AmmoBox() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        player.getCooldowns().addCooldown(this, 10);
        int type = stack.getOrCreateTag().getInt("Type");

        var cap = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables());
        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (!player.isCrouching()) {
                if (type == 0 || type == 1) {
                    capability.rifleAmmo = cap.rifleAmmo + tag.getInt("RifleAmmo");
                    tag.putInt("RifleAmmo", 0);
                }

                if (type == 0 || type == 2) {
                    capability.handgunAmmo = cap.handgunAmmo + tag.getInt("HandgunAmmo");
                    tag.putInt("HandgunAmmo", 0);
                }

                if (type == 0 || type == 3) {
                    capability.shotgunAmmo = cap.shotgunAmmo + tag.getInt("ShotgunAmmo");
                    tag.putInt("ShotgunAmmo", 0);
                }

                if (type == 0 || type == 4) {
                    capability.sniperAmmo = cap.sniperAmmo + tag.getInt("SniperAmmo");
                    tag.putInt("SniperAmmo", 0);
                }

                capability.syncPlayerVariables(player);

                if (!level.isClientSide()) {
                    level.playSound(null, player.blockPosition(), ModSounds.BULLET_SUPPLY.get(), SoundSource.PLAYERS, 1, 1);
                }

                if (tag.getBoolean("IsDrop")) {
                    stack.shrink(1);
                }
            } else {
                if (type == 0 || type == 1) {
                    tag.putInt("RifleAmmo", tag.getInt("RifleAmmo") + cap.rifleAmmo);
                    capability.rifleAmmo = 0;
                }

                if (type == 0 || type == 2) {
                    tag.putInt("HandgunAmmo", tag.getInt("HandgunAmmo") + cap.handgunAmmo);
                    capability.handgunAmmo = 0;
                }

                if (type == 0 || type == 3) {
                    tag.putInt("ShotgunAmmo", tag.getInt("ShotgunAmmo") + cap.shotgunAmmo);
                    capability.shotgunAmmo = 0;
                }

                if (type == 0 || type == 4) {
                    tag.putInt("SniperAmmo", tag.getInt("SniperAmmo") + cap.sniperAmmo);
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
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && player.isCrouching()) {
            int type = stack.getOrCreateTag().getInt("Type");
            ++type;
            type %= 5;

            switch (type) {
                case 0 ->
                        player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.all").withStyle(ChatFormatting.WHITE), true);
                case 1 ->
                        player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.rifle").withStyle(ChatFormatting.GREEN), true);
                case 2 ->
                        player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.handgun").withStyle(ChatFormatting.AQUA), true);
                case 3 ->
                        player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.shotgun").withStyle(ChatFormatting.RED), true);
                case 4 ->
                        player.displayClientMessage(Component.translatable("des.superbwarfare.tips.ammo_type.sniper").withStyle(ChatFormatting.GOLD), true);
            }

            entity.playSound(ModSounds.FIRE_RATE.get(), 1f, 1f);
            stack.getOrCreateTag().putInt("Type", type);
        }

        return super.onEntitySwing(stack, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int type = stack.getOrCreateTag().getInt("Type");

        tooltip.add(Component.translatable("des.superbwarfare.use_tip.ammo_box").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("des.superbwarfare.tips.rifle_ammo").withStyle(ChatFormatting.GREEN)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getInt(stack, "RifleAmmo", 0)) + ((type == 0 || type == 1) ? " ←-" : " ")).withStyle(ChatFormatting.BOLD)));

        tooltip.add(Component.translatable("des.superbwarfare.tips.handgun_ammo").withStyle(ChatFormatting.AQUA)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getInt(stack, "HandgunAmmo", 0)) + ((type == 0 || type == 2) ? " ←-" : " ")).withStyle(ChatFormatting.BOLD)));

        tooltip.add(Component.translatable("des.superbwarfare.tips.shotgun_ammo").withStyle(ChatFormatting.RED)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getInt(stack, "ShotgunAmmo", 0)) + ((type == 0 || type == 3) ? " ←-" : " ")).withStyle(ChatFormatting.BOLD)));

        tooltip.add(Component.translatable("des.superbwarfare.tips.sniper_ammo").withStyle(ChatFormatting.GOLD)
                .append(Component.literal("").withStyle(ChatFormatting.RESET))
                .append(Component.literal(new DecimalFormat("##").format(ItemNBTTool.getInt(stack, "SniperAmmo", 0)) + ((type == 0 || type == 4) ? " ←-" : " ")).withStyle(ChatFormatting.BOLD)));
    }

}
