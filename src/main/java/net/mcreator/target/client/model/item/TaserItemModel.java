package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Taser;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(0.5f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.02f * (float) (fp + fr));
            shen.setRotZ(0f);
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.0025f * (float) fr);
            } else {
                shen.setRotY(-0.0025f * (float) fr);
            }
        } else {
            shen.setPosY(0.03f * (float) (fp + 2 * fr));
            shen.setPosZ(0.7f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.03f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.001f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.0045f * (float) fr);
            } else {
                shen.setRotY(-0.0045f * (float) fr);
            }
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.6f * (float) p);

        gun.setPosY(1.2f * (float) p - (float) (0.6f * zp));

        gun.setPosZ((float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

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

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

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

    }
}
