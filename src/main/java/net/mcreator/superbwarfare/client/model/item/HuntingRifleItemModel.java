package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.HuntingRifle;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class HuntingRifleItemModel extends GeoModel<HuntingRifle> {
    @Override
    public ResourceLocation getAnimationResource(HuntingRifle animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/hunting_rifle.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(HuntingRifle animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/hunting_rifle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HuntingRifle animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/huntingrifle.png");
    }

    @Override
    public void setCustomAnimations(HuntingRifle animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("lieqiang");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.95f * (float) p);

        gun.setPosY(1.32f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(3f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.5f * (float) p));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.05f * (float) (fp + 0.2 * fr));
        shen.setPosY(0.3f * (float) (fp + 2 * fr));
        shen.setPosZ(4.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.06f * (float) (1.28f * fp + fr));
        shen.setRotY(0.06f * (float) fr);
        shen.setRotZ(-0.1f * (float) (fp + 1.3 * fr));

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        if (stack.getOrCreateTag().getDouble("flash_time") > 0) {
            flare.setHidden(false);
            flare.setScaleX((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setHidden(true);
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

        move.setRotX(Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        var data = player.getPersistentData();
        float numR = (float) (1 - 0.82 * data.getDouble("zoom_time"));
        float numP = (float) (1 - 0.78 * data.getDouble("zoom_time"));

        if (stack.getOrCreateTag().getInt("gun_reloading_time") > 0) {
            main.setRotX(numR * main.getRotX());
            main.setRotY(numR * main.getRotY());
            main.setRotZ(numR * main.getRotZ());
            main.setPosX(numP * main.getPosX());
            main.setPosY(numP * main.getPosY());
            main.setPosZ(numP * main.getPosZ());
            camera.setRotX(numR * camera.getRotX());
            camera.setRotY(numR * camera.getRotY());
            camera.setRotZ(numR * camera.getRotZ());
        }

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
