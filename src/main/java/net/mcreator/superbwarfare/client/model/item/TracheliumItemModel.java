package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Trachelium;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TracheliumItemModel extends GeoModel<Trachelium> {
    @Override
    public ResourceLocation getAnimationResource(Trachelium animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/trachelium.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Trachelium animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/trachelium.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Trachelium animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/trachelium_texture.png");
    }

    @Override
    public void setCustomAnimations(Trachelium animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(1.48f * (float) p);

        gun.setPosY(3.2f * (float) p - (float) (0.6f * zp));

        gun.setPosZ((float) p + (float) (0.5f * zp));

        gun.setRotZ(-0.087f * (float) p + (float) (0.05f * zp));

        gun.setScaleZ(1f - (0.2f * (float) p));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosX(-0.4f * (float) (fp + 0.2 * fr));
        shen.setPosY(0.6f * (float) (fp + 2 * fr));
        shen.setPosZ(4.2f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.18f * (float) (1.28f * fp + fr));
        shen.setRotY(0.12f * (float) fr);
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

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
