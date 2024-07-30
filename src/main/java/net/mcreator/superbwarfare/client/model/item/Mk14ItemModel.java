package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.Mk14Item;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Mk14ItemModel extends GeoModel<Mk14Item> {
    @Override
    public ResourceLocation getAnimationResource(Mk14Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/mk14ebr.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mk14Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/mk14ebr.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mk14Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/mk14.png");
    }

    @Override
    public void setCustomAnimations(Mk14Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bones");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone rex = getAnimationProcessor().getBone("rex");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");
        CoreGeoBone yugu = getAnimationProcessor().getBone("yugu");
        CoreGeoBone jing = getAnimationProcessor().getBone("jing");
        CoreGeoBone qiangguan = getAnimationProcessor().getBone("qiangguan");
        CoreGeoBone action = getAnimationProcessor().getBone("action");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(-1.5f);
            r.setRotX(-1.5f);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoom_pos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoom_pos_z");

        gun.setPosX(3.105f * (float) p);

        gun.setPosY(0.53f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(3.7f * (float) p + (float) (0.2f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.7f * (float) p));

        scope.setScaleZ(1f - (0.7f * (float) p));

        yugu.setScaleZ(1f - (0.7f * (float) p));

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.06f * (float) (fp + 2 * fr));
            shen.setPosZ(0.9f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.005f * (float) (fp + fr));
            shen.setRotZ(0.01f * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp) * (float) fp);
        } else {
            shen.setPosY(0.04f * (float) (fp + 2 * fr));
            shen.setPosZ(1.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }

        rex.setRotZ(0.01f * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp) * (float) fp);

        rex.setPosY(-0.23f * (float) (fp + 2.3 * fr));

        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        action.setPosZ(2.5f * (float) fp);

        if (gun.getPosX() > 2.5f) {
            rex.setHidden(false);
            jing.setHidden(true);
            qiangguan.setHidden(true);
        } else {
            rex.setHidden(true);
            jing.setHidden(false);
            qiangguan.setHidden(false);
        }

        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");

        if (stack.getOrCreateTag().getBoolean("HoldOpen")) {
            bolt.setPosZ(2.5f);
        }

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

        root.setPosY((float) y + 1.2f * PosY);

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

        move.setRotX(0.9f * Mth.DEG_TO_RAD * (float) xRot - 0.15f * (float) vy);

        move.setRotY(0.9f * Mth.DEG_TO_RAD * (float) yRot);

        move.setRotZ(2.7f * (float) m + Mth.DEG_TO_RAD * (float) zRot);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        player.getPersistentData().putDouble("camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());

        player.getPersistentData().putDouble("camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());

        player.getPersistentData().putDouble("camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
