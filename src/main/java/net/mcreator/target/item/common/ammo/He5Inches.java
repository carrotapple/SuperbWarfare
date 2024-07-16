package net.mcreator.target.item.common.ammo;

import net.mcreator.target.entity.Mk42Entity;
import net.mcreator.target.entity.MortarShellEntity;
import net.mcreator.target.init.TargetModEntities;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModSounds;
import net.mcreator.target.tools.SoundTool;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class He5Inches extends Item {
    public He5Inches() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(entity.getItemInHand(hand));
        entity.startUsingItem(hand);


        ItemStack stack = entity.getMainHandItem();

        if (entity.getVehicle() != null && entity.getVehicle() instanceof Mk42Entity && stack.is(TargetModItems.HE_5_INCHES.get())) {
            Entity cannon = entity.getVehicle();
            entity.getCooldowns().addCooldown(stack.getItem(), 30);
            entity.getInventory().clearOrCountMatchingItems(p -> p.getItem() == TargetModItems.HE_5_INCHES.get(), 1, entity.inventoryMenu.getCraftSlots());
            cannonShoot(entity, cannon);

        }
        return ar;
    }

    public static void cannonShoot(Player player, Entity cannon) {

        Level level = player.level();
        if (level instanceof ServerLevel server) {
            MortarShellEntity entityToSpawn = new MortarShellEntity(TargetModEntities.MORTAR_SHELL.get(), player, level);
            entityToSpawn.setPos(cannon.getX(), cannon.getEyeY(), cannon.getZ());
            entityToSpawn.shoot(cannon.getLookAngle().x, cannon.getLookAngle().y, cannon.getLookAngle().z, 20, 0.1f);
            level.addFreshEntity(entityToSpawn);

            if (player instanceof ServerPlayer serverPlayer) {
                SoundTool.playLocalSound(serverPlayer, TargetModSounds.MK_42_FIRE_1P.get(), 2, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_FIRE_3P.get(), SoundSource.PLAYERS, 6, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_FAR.get(), SoundSource.PLAYERS, 16, 1);
                serverPlayer.level().playSound(null, serverPlayer.getOnPos(), TargetModSounds.MK_42_VERYFAR.get(), SoundSource.PLAYERS, 32, 1);
            }

//            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (player.getX() + 3 * player.getLookAngle().x), (player.getY() + 0.1 + 3 * player.getLookAngle().y), (player.getZ() + 3 * player.getLookAngle().z), 40, 0.4, 0.4, 0.4,
//                    0.01);
//            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY(), player.getZ(), 100, 2.5, 0.04, 2.5, 0.005);

        }
    }

}
