package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Minigun;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MinigunItemModel extends GeoModel<Minigun> {
    @Override
    public ResourceLocation getAnimationResource(Minigun animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/minigun.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Minigun animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/minigun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Minigun animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/minigun.png");
    }

    @Override
    public void setCustomAnimations(Minigun animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("barrel");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone light = getAnimationProcessor().getBone("light");
        CoreGeoBone heat_barrels = getAnimationProcessor().getBone("heatbarrels");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        float heat = (float) stack.getOrCreateTag().getDouble("heat");

        heat_barrels.setScaleZ(4 * heat);

        gun.setRotZ((float) (gun.getRotZ() + times * -0.008f * stack.getOrCreateTag().getDouble("minigun_rotation")));


        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        shen.setPosY(0.1f * (float) (fp + 2 * fr));
        shen.setPosZ(2.2f * (float) (0.5 * fp + 1.54f * fr));
        shen.setRotX(0.05f * (float) (0.18f * fp + fr));
        shen.setRotZ(-0.02f * (float) (fp + 1.3 * fr));
        shen.setPosX(0.5f * (float) fr * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
            flare.setHidden(false);
            light.setHidden(false);
            flare.setScaleX((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setHidden(true);
            light.setHidden(true);

        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        float PosX = (float) player.getPersistentData().getDouble("gun_move_posX");
        float PosY = (float) player.getPersistentData().getDouble("gun_move_posY");

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
