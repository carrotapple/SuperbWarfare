package net.mcreator.target.item;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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

        if (world.isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            if (!stack.getOrCreateTag().getBoolean("Using")) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            } else {
                mc.options.setCameraType(CameraType.FIRST_PERSON);
            }
        } else {
            stack.getOrCreateTag().putBoolean("Using", !stack.getOrCreateTag().getBoolean("Using"));
        }

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
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addMonitorTips(list, stack.getOrCreateTag().getString(LINKED_DRONE));
    }
}
