package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.SksItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SksItemModel extends GeoModel<SksItem> {
    @Override
    public ResourceLocation getAnimationResource(SksItem animatable) {
        return new ResourceLocation("target", "animations/sks.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SksItem animatable) {
        return new ResourceLocation("target", "geo/sks.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SksItem animatable) {
        return new ResourceLocation("target", "textures/item/sks.png");
    }


    @Override
    public void setCustomAnimations(SksItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");
        CoreGeoBone shuan = getAnimationProcessor().getBone("bolt2");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getDouble("gj") == 1) {
            bolt.setPosZ(3f);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.09f * (float) p);

        gun.setPosY(0.83f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(4f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if (gun.getPosX() > 1.2) {
            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(-0.01f * (float) (fp + 2 * fr));
            shen.setPosZ(0.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.015f * (float) fr);
            } else {
                shen.setRotY(-0.015f * (float) fr);
            }
        } else {
            shen.setPosY(-0.03f * (float) (fp + 2 * fr));
            shen.setPosZ(1.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.03f * (float) fr);
            } else {
                shen.setRotY(-0.03f * (float) fr);
            }
        }

        shuan.setPosZ(2.5f * (float) fp);

        if (stack.getOrCreateTag().getDouble("flash_time") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
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


        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
