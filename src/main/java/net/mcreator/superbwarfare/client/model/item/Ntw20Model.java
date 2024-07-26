package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Ntw20;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Ntw20Model extends GeoModel<Ntw20> {
    @Override
    public ResourceLocation getAnimationResource(Ntw20 animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/ntw_20.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Ntw20 animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/ntw_20.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ntw20 animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/ntw_20.png");
    }

    @Override
    public void setCustomAnimations(Ntw20 animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");
        CoreGeoBone action = getAnimationProcessor().getBone("action");
        CoreGeoBone body = getAnimationProcessor().getBone("body");
        CoreGeoBone jing = getAnimationProcessor().getBone("jing");
        CoreGeoBone base = getAnimationProcessor().getBone("base");
        CoreGeoBone rex = getAnimationProcessor().getBone("rex");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

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
            shen.setRotZ(0f);
        } else {
            shen.setPosY(1.2f * (float) (fp + 2 * fr));
            shen.setPosZ(7.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.1f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }

        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon * fp));

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(4.54f * (float) p);

        gun.setPosY(-0.45f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(10.0f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.02f * zp));

        gun.setScaleZ(1f - (0.8f * (float) p));

        scope.setScaleZ(1f - (0.85f * (float) p));

        if (gun.getPosX() > 4.3f) {
            rex.setHidden(false);
            action.setHidden(true);
            body.setHidden(true);
            jing.setHidden(true);
            base.setHidden(true);
        } else {
            rex.setHidden(true);
            action.setHidden(false);
            body.setHidden(false);
            jing.setHidden(false);
            base.setHidden(false);
        }

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

        float RotZ = (float) player.getPersistentData().getDouble("gun_move_rotZ");

        root.setRotY(0.2f * PosX);

        root.setRotZ(0.2f * PosX + RotZ);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = player.getPersistentData().getDouble("move");

        double vy = player.getPersistentData().getDouble("vy");

        move.setPosX(9.3f * (float) m);

        move.setPosY(-2f * (float) vy);

        double xRot = player.getPersistentData().getDouble("xRot");

        double yRot = player.getPersistentData().getDouble("yRot");

        double zRot = player.getPersistentData().getDouble("zRot");

        move.setRotX(0.8f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(0.6f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
