package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.VectorItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VectorItemModel extends GeoModel<VectorItem> {
    @Override
    public ResourceLocation getAnimationResource(VectorItem animatable) {
        return new ResourceLocation("target", "animations/vector.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(VectorItem animatable) {
        return new ResourceLocation("target", "geo/vector.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VectorItem animatable) {
        return new ResourceLocation("target", "textures/item/vector.png");
    }

    @Override
    public void setCustomAnimations(VectorItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");

        Player player = Minecraft.getInstance().player;

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.62f * (float) p);

        gun.setPosY(0.9f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(3f * (float) p + (float) (0.5f * zp));

        gun.setRotZ(-0.087f * (float) p + (float) (0.05f * zp));

        gun.setScaleZ(1f - (0.5f * (float) p));

        scope.setScaleZ(1f - (0.6f * (float) p));

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming && gun.getPosX() > 2.45) {
            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }

        double fp = 0;
        fp = player.getPersistentData().getDouble("fire_pos");

        shen.setPosZ(0.75f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.0002f * (float) fp);
        } else {
            shen.setRotX(0.02f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosY((float) y);
        root.setRotX((float) x);

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
