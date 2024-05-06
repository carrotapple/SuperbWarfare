package net.mcreator.target.procedures;

import net.mcreator.target.init.ItemRegistry;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class M79uiXianShiYouXiNeiDieJiaCengProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemRegistry.M_79.get() && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && !(entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming;
    }
}
