package net.mcreator.target.client.model.item;

import net.mcreator.target.init.TargetModMobEffects;
import net.mcreator.target.item.gun.AK47Item;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ViewportEvent;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.network.chat.Component;


public class AK47ItemModel extends GeoModel<AK47Item> {

    @Override
    public ResourceLocation getAnimationResource(AK47Item animatable) {
        return new ResourceLocation("target", "animations/ak.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AK47Item animatable) {
        return new ResourceLocation("target", "geo/ak.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AK47Item animatable) {
        return new ResourceLocation("target", "textures/item/ak47.png");
    }

    @Override
    public void setCustomAnimations(AK47Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("kobra");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        double p = player.getPersistentData().getDouble("zoom_pos");

        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.96f * (float) p);

        gun.setPosY(0.25f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(2.5f * (float) p + (float) (0.5f * zp));

        gun.setScaleZ(1f - (0.2f * (float) p));

        scope.setScaleZ(1f - (0.4f * (float) p));


        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        if (gun.getPosX() > 1.8) {
            holo.setScaleX(1);
            holo.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
        }

        double fp = player.getPersistentData().getDouble("fire_pos");


        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosZ(0.8f * (float) fp);
        } else {
            shen.setPosZ(1.11f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.01f * (float) fp);
        } else {
            shen.setPosY(0.1f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.003f * (float) fp);
        } else {
            shen.setRotX(0.025f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

        shuan.setPosZ(2.4f * (float) fp);

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

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

        root.setPosX(PosX);

        root.setPosY((float) y + PosY);

        root.setRotX((float) x);

        root.setRotZ(0.1f * PosX);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double yaw = player.getPersistentData().getDouble("yaw");

        double pit = player.getPersistentData().getDouble("gun_pitch");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-0.95f * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - (float) m);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
