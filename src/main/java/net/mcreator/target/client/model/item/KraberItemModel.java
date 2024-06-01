package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Kraber;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class KraberItemModel extends GeoModel<Kraber> {
    @Override
    public ResourceLocation getAnimationResource(Kraber animatable) {
        return new ResourceLocation("target", "animations/kraber.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Kraber animatable) {
        return new ResourceLocation("target", "geo/kraber.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Kraber animatable) {
        return new ResourceLocation("target", "textures/item/kraber.png");
    }

    @Override
    public void setCustomAnimations(Kraber animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(-1.5f);
            r.setRotX(-1.5f);
        }

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.4f * (float) (fp + 2 * fr));
            shen.setPosZ(3.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.1f * (float) (fp + fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.025f * (float) fr);
            } else {
                shen.setRotY(-0.025f * (float) fr);
            }
        } else {
            shen.setPosY(1.2f * (float) (fp + 2 * fr));
            shen.setPosZ(7.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.1f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.045f * (float) fr);
            } else {
                shen.setRotY(-0.045f * (float) fr);
            }
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(6.298f * (float) p);

        gun.setPosY(0.32f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(10.4f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.02f * zp));

        gun.setScaleZ(1f - (0.8f * (float) p));

        scope.setScaleZ(1f - (0.9f * (float) p));

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

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {

            move.setPosX(0);

            move.setRotX(0);

            move.setRotZ(0);

            move.setRotY(0);

            move.setPosY(-0.2f * (float) vy);

        } else {

            move.setPosY(-1 * (float) vy);

            move.setPosX(9.3f * (float) m);

            move.setRotX(2.0f * (float) pit);

            move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

            move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
        }
    }
}
