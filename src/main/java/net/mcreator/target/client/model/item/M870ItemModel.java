package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.M870Item;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M870ItemModel extends GeoModel<M870Item> {
    @Override
    public ResourceLocation getAnimationResource(M870Item animatable) {
        return new ResourceLocation("target", "animations/m870.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M870Item animatable) {
        return new ResourceLocation("target", "geo/m870.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M870Item animatable) {
        return new ResourceLocation("target", "textures/item/m870.png");
    }

    @Override
    public void setCustomAnimations(M870Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone shell = getAnimationProcessor().getBone("shell");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getBoolean("reloading")) {
            if (stack.getOrCreateTag().getDouble("prepare") == 0) {
                if (stack.getOrCreateTag().getDouble("loading") > 10 || stack.getOrCreateTag().getDouble("loading") < 2) {
                    shell.setScaleX(0);
                    shell.setScaleY(0);
                    shell.setScaleZ(0);
                }
            }
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(5.22f * (float) p);

        gun.setPosY(3.00f * (float) p - (float) (0.7f * zp));

        gun.setPosZ(1.5f * (float) p + (float) (0.9f * zp));

        gun.setRotZ((float) (0.02f * zp));

        gun.setScaleZ(1f - (0.2f * (float) p));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.4f * (float) (fp + 2 * fr));
            shen.setPosZ(3.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.12f * (float) (fp + fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.025f * (float) fr);
            } else {
                shen.setRotY(-0.025f * (float) fr);
            }
        } else {
            shen.setPosY(0.7f * (float) (fp + 2 * fr));
            shen.setPosZ(4.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.15f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
            if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon > 0) {
                shen.setRotY(0.045f * (float) fr);
            } else {
                shen.setRotY(-0.045f * (float) fr);
            }
        }

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

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

        root.setPosY((float) y + 2.5f * PosY);

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
