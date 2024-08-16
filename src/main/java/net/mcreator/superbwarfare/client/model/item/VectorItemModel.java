package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.VectorItem;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VectorItemModel extends GeoModel<VectorItem> {
    @Override
    public ResourceLocation getAnimationResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/vector.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/vector.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VectorItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/vector.png");
    }

    @Override
    public void setCustomAnimations(VectorItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");
        CoreGeoBone kmj = getAnimationProcessor().getBone("kuaimanji");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (stack.getOrCreateTag().getInt("fire_mode") == 0) {
            kmj.setRotX(-120 * Mth.DEG_TO_RAD);
        }
        if (stack.getOrCreateTag().getInt("fire_mode") == 1) {
            kmj.setRotX(-60 * Mth.DEG_TO_RAD);
        }
        if (stack.getOrCreateTag().getInt("fire_mode") == 2) {
            kmj.setRotX(0);
        }

        double p = player.getPersistentData().getDouble("zoom_pos");
        double zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(2.35f * (float) p);

        gun.setPosY(0.74f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(5f * (float) p + (float) (0.3f * zp));

        gun.setScaleZ(1f - (0.5f * (float) p));

        scope.setScaleZ(1f - (0.2f * (float) p));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 2));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.05f * (float) (fp + 2 * fr));
            holo.setPosY(-0.43f * (float) (fp + 2.3 * fr));
            shen.setPosZ(0.9f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.006f * (float) (fp + fr));
        } else {
            shen.setPosY(0.08f * (float) (fp + 2 * fr));
            shen.setPosZ(0.9f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.03f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float) fr * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

        double y = player.getPersistentData().getDouble("y");
        double x = player.getPersistentData().getDouble("x");

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

        move.setRotX(0.7f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(0.7f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
