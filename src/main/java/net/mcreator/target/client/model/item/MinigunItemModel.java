package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Minigun;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MinigunItemModel extends GeoModel<Minigun> {
    @Override
    public ResourceLocation getAnimationResource(Minigun animatable) {
        return new ResourceLocation("target", "animations/minigun.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Minigun animatable) {
        return new ResourceLocation("target", "geo/minigun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Minigun animatable) {
        return new ResourceLocation("target", "textures/item/minigun.png");
    }

    @Override
    public void setCustomAnimations(Minigun animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("barrel");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;


        gun.setRotZ((float) (gun.getRotZ() + times * -0.008f * stack.getOrCreateTag().getDouble("minigun_rotation")));


        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(0.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.015f * (float) fr);
            } else {
                shen.setRotY(-0.015f * (float) fr);
            }
        } else {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            shen.setPosZ(1.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.03f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.045f * (float) fr);
            } else {
                shen.setRotY(-0.045f * (float) fr);
            }
        }

        if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
            flare.setScaleX((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);

        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float)player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float)player.getPersistentData().getDouble("gun_move_posY");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        root.setRotZ(0.1f * PosX);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = 0;
        m = player.getPersistentData().getDouble("move");

        double yaw = 0;
        yaw = player.getPersistentData().getDouble("yaw");

        double pit = 0;
        pit = player.getPersistentData().getDouble("gun_pitch");

        double vy = 0;
        vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1.8f * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(2.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(3.9f * (float) yaw - 1.7f * (float) m);
    }
}
