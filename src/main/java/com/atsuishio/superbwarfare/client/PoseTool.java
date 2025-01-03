package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoseTool {

    public static HumanoidModel.ArmPose pose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")
                || stack.getOrCreateTag().getBoolean("is_normal_reloading")
                || GunsTool.getGunBooleanTag(stack, "Reloading")
                || GunsTool.getGunBooleanTag(stack, "Charging")) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else if (entityLiving.isSprinting() && entityLiving.onGround() && entityLiving.getPersistentData().getDouble("noRun") == 0) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }
}
