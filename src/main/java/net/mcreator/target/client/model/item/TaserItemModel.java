package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Taser;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TaserItemModel extends GeoModel<Taser> {
    @Override
    public ResourceLocation getAnimationResource(Taser animatable) {
        return new ResourceLocation("target", "animations/taser.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Taser animatable) {
        return new ResourceLocation("target", "geo/taser.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Taser animatable) {
        return new ResourceLocation("target", "textures/item/tasergun.png");
    }

    @Override
    public void setCustomAnimations(Taser animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        Player player = Minecraft.getInstance().player;

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.05f * (float) (fp + 0.2 * fr));
        shen.setPosY(0.1f * (float) (fp + 2 * fr));
        shen.setPosZ(1.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.04f * (float) (1.28f * fp + fr));
        shen.setRotY(0.02f * (float) fr);
        shen.setRotZ(-0.02f * (float) (fp + 1.3 * fr));

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.82f * (float) p);

        gun.setPosY(1.3f * (float) p - (float) (0.3f * zp));

        gun.setPosZ((float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float)player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float)player.getPersistentData().getDouble("gun_move_posY");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-2 * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
