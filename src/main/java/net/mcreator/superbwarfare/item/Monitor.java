package net.mcreator.superbwarfare.item;

import net.mcreator.superbwarfare.entity.DroneEntity;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EquipmentSlot;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

import java.util.List;

public class Monitor extends Item {
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();

        if (!ItemNBTTool.getBoolean(stack, LINKED, false)) {
            return super.use(world, player, hand);
        }

        stack.getOrCreateTag().putBoolean("Using", !stack.getOrCreateTag().getBoolean("Using"));

        DroneEntity drone = player.level().getEntitiesOfClass(DroneEntity.class, player.getBoundingBox().inflate(512))
                .stream().filter(e -> e.getStringUUID().equals(stack.getOrCreateTag().getString("LinkedDrone"))).findFirst().orElse(null);

        if (drone != null) {
            drone.getPersistentData().putBoolean("left", false);
            drone.getPersistentData().putBoolean("right", false);
            drone.getPersistentData().putBoolean("forward", false);
            drone.getPersistentData().putBoolean("backward", false);
            drone.getPersistentData().putBoolean("up", false);
            drone.getPersistentData().putBoolean("down", false);
        }

        return super.use(world, player, hand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 2d, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addMonitorTips(list, stack.getOrCreateTag().getString(LINKED_DRONE));
    }
}
