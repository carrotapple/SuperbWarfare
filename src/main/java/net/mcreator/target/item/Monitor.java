package net.mcreator.target.item;

import net.mcreator.target.entity.DroneEntity;
import net.mcreator.target.tools.ItemNBTTool;
import net.mcreator.target.tools.TooltipTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
        InteractionResultHolder<ItemStack> ar = super.use(world, player, hand);
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = player.getMainHandItem();
        if (!stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", true);
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        } else {
            stack.getOrCreateTag().putBoolean("Using", false);
            mc.options.setCameraType(CameraType.FIRST_PERSON);
        }

        return ar;
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addMonitorTips(list, stack.getOrCreateTag().getString(LINKED_DRONE));
    }
}
