package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.capability.LaserCapability;
import com.atsuishio.superbwarfare.capability.LaserHandler;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.entity.projectile.LaserEntity;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.LaserShootMessage;
import com.atsuishio.superbwarfare.network.message.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.TraceTool;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class BeamTest extends Item {

    public BeamTest() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(capability -> {
            player.startUsingItem(hand);

            SoundEvent sound1p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "charge_rifle_fire_1p"));
            if (sound1p != null) {
                player.playSound(sound1p, 1f, 1);
            }

            if (!level.isClientSide) {
                double px = player.getX();
                double py = player.getY() + player.getBbHeight() * 0.6F;
                double pz = player.getZ();
                float yHeadRotAngle = (float) Math.toRadians(player.yHeadRot + 90);
                float xHeadRotAngle = (float) (float) -Math.toRadians(player.getXRot());
                LaserEntity laserEntity = new LaserEntity(player.level(), player, px, py, pz, yHeadRotAngle, xHeadRotAngle, 6000);
                capability.init(new LaserHandler(player, laserEntity));
                capability.start();

                if (!stack.getOrCreateTag().getBoolean("LaserFiring") && !(player.getCooldowns().isOnCooldown(stack.getItem()))) {
                    stack.getOrCreateTag().putBoolean("LaserFiring", true);
                }

                SoundEvent sound3p = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ModUtils.MODID, "charge_rifle_fire_3p"));
                if (sound3p != null) {
                    player.playSound(sound3p, 4, 1f);
                }
            }
        });

        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::stop);
            stack.getOrCreateTag().putBoolean("LaserFiring", false);
        }
        if (livingEntity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof BeamTest beamTest) {
            stopGunChargeSound(serverPlayer,beamTest);
        }


        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    private static void stopGunChargeSound(ServerPlayer player, BeamTest beamTest) {
        beamTest.getChargeSound().forEach(sound -> {
            var clientboundstopsoundpacket = new ClientboundStopSoundPacket(sound.getLocation(), SoundSource.PLAYERS);
            player.connection.send(clientboundstopsoundpacket);
        });
    }

    public Set<SoundEvent> getChargeSound() {
        return Set.of(ModSounds.CHARGE_RIFLE_FIRE_1P.get(), ModSounds.CHARGE_RIFLE_FIRE_3P.get());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof Player player) {
            player.getCapability(ModCapabilities.LASER_CAPABILITY).ifPresent(LaserCapability.ILaserCapability::stop);
            pStack.getOrCreateTag().putBoolean("LaserFiring", false);
            if (player instanceof ServerPlayer serverPlayer) {
                ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage(30,10,20, serverPlayer.getX(), serverPlayer.getEyeY(), serverPlayer.getZ()));
            }
            player.getCooldowns().addCooldown(pStack.getItem(), 20);

            if (player.level().isClientSide()) {
                beamShoot(player);
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    public static void beamShoot(Player player) {
        Entity lookingEntity = TraceTool.laserfindLookingEntity(player, 512);

        if (lookingEntity == null) {
            return;
        }

        boolean canAttack = lookingEntity != player && !(lookingEntity instanceof Player player_ && (player_.isCreative() || player_.isSpectator()))
                && (!player.isAlliedTo(lookingEntity) || lookingEntity.getTeam() == null || lookingEntity.getTeam().getName().equals("TDM"));

        if (canAttack) {
            ModUtils.PACKET_HANDLER.sendToServer(new LaserShootMessage(45, lookingEntity.getUUID()));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}
