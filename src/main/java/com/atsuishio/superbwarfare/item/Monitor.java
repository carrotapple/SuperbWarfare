package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.entity.DroneEntity;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;
import java.util.List;

public class Monitor extends Item {
    private static CameraType lastCameraType;
    public static final String LINKED = "Linked";
    public static final String LINKED_DRONE = "LinkedDrone";

    public Monitor() {
        super(new Properties().stacksTo(1));
    }

    public static void link(ItemStack itemstack, String id) {
        ItemNBTTool.setBoolean(itemstack, LINKED, true);
        itemstack.getOrCreateTag().putString(LINKED_DRONE, id);
    }

    public static void disLink(ItemStack itemstack) {
        ItemNBTTool.setBoolean(itemstack, LINKED, false);
        itemstack.getOrCreateTag().putString(LINKED_DRONE, "none");
    }

    private void resetDroneData(DroneEntity drone) {
        if (drone == null) return;

        drone.getPersistentData().putBoolean("left", false);
        drone.getPersistentData().putBoolean("right", false);
        drone.getPersistentData().putBoolean("forward", false);
        drone.getPersistentData().putBoolean("backward", false);
        drone.getPersistentData().putBoolean("up", false);
        drone.getPersistentData().putBoolean("down", false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();

        if (!ItemNBTTool.getBoolean(stack, LINKED, false)) {
            return super.use(world, player, hand);
        }

        if (stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", false);
            if (world.isClientSide) {
                if (lastCameraType != null) {
                    Minecraft.getInstance().options.setCameraType(lastCameraType);
                }
            }
        } else {
            stack.getOrCreateTag().putBoolean("Using", true);
            if (world.isClientSide) {
                lastCameraType = Minecraft.getInstance().options.getCameraType();
                Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
        }

        DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString(LINKED_DRONE));
        this.resetDroneData(drone);

        return super.use(world, player, hand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 2d, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        if (stack.getOrCreateTag().getString(LINKED_DRONE).equals("none")) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString(LINKED_DRONE));
        if (drone == null) return;

        list.add(Component.translatable("des.superbwarfare.tips.distance", new DecimalFormat("##.#").format(player.distanceTo(drone))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        DroneEntity drone = EntityFindUtil.findDrone(entity.level(), itemstack.getOrCreateTag().getString(LINKED_DRONE));

        if (!selected) {
            if (itemstack.getOrCreateTag().getBoolean("Using")) {
                itemstack.getOrCreateTag().putBoolean("Using", false);
                if (entity.level().isClientSide) {
                    if (lastCameraType != null) {
                        Minecraft.getInstance().options.setCameraType(lastCameraType);
                    }
                }
            }
            this.resetDroneData(drone);
        } else if (drone == null) {
            if (itemstack.getOrCreateTag().getBoolean("Using")) {
                itemstack.getOrCreateTag().putBoolean("Using", false);
                if (entity.level().isClientSide) {
                    if (lastCameraType != null) {
                        Minecraft.getInstance().options.setCameraType(lastCameraType);
                    }
                }
            }
        }
    }
}
