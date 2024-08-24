package net.mcreator.superbwarfare.tools;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoseTool {

    private static final HumanoidModel.ArmPose TacticalSprintPose = HumanoidModel.ArmPose.create("TacticalSprintPose", false, (model, entity, arm) -> {
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = 0.2f * model.rightArm.xRot - 155F * Mth.DEG_TO_RAD;
            model.rightArm.zRot = -5F * Mth.DEG_TO_RAD;
        }
    });

    public static HumanoidModel.ArmPose pose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean("is_empty_reloading")
                || stack.getOrCreateTag().getBoolean("is_normal_reloading")
                || stack.getOrCreateTag().getBoolean("reloading")
                || stack.getOrCreateTag().getBoolean("sentinel_is_charging")) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else if (entityLiving.isSprinting() && entityLiving.onGround() && entityLiving.getPersistentData().getDouble("noRun") == 0) {
            if (entityLiving.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                return TacticalSprintPose;
            } else {
                return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            }
        } else {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }
}
