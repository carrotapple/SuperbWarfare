package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.machinegun.M60Item;
import net.mcreator.superbwarfare.network.ModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M60ItemModel extends GeoModel<M60Item> {
    @Override
    public ResourceLocation getAnimationResource(M60Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/m60.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M60Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/m60.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M60Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/m60.png");
    }

    @Override
    public void setCustomAnimations(M60Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone tiba = getAnimationProcessor().getBone("tiba");
        CoreGeoBone b1 = getAnimationProcessor().getBone("b1");
        CoreGeoBone b2 = getAnimationProcessor().getBone("b2");
        CoreGeoBone b3 = getAnimationProcessor().getBone("b3");
        CoreGeoBone b4 = getAnimationProcessor().getBone("b4");
        CoreGeoBone b5 = getAnimationProcessor().getBone("b5");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(1.5f);
            r.setRotX(1.5f);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 5 && stack.getOrCreateTag().getBoolean("bullet_chain")) {
            b5.setScaleX(0);
            b5.setScaleY(0);
            b5.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 4 && stack.getOrCreateTag().getBoolean("bullet_chain")) {
            b4.setScaleX(0);
            b4.setScaleY(0);
            b4.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 3 && stack.getOrCreateTag().getBoolean("bullet_chain")) {
            b3.setScaleX(0);
            b3.setScaleY(0);
            b3.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 2 && stack.getOrCreateTag().getBoolean("bullet_chain")) {
            b2.setScaleX(0);
            b2.setScaleY(0);
            b2.setScaleZ(0);
        }

        if (stack.getOrCreateTag().getInt("ammo") < 1 && stack.getOrCreateTag().getBoolean("bullet_chain")) {
            b1.setScaleX(0);
            b1.setScaleY(0);
            b1.setScaleZ(0);
        }

        double zt = ClientEventHandler.getZoomTime();
        double zp = ClientEventHandler.getZoomPos();
        double zpz = ClientEventHandler.getZoomPosZ();

        gun.setPosX(3.74f * (float) zp);

        gun.setPosY(-0.1f * (float) zp - (float) (0.1f * zpz));

        gun.setPosZ((float) zp + (float) (0.3f * zpz));

        gun.setRotZ(-0.087f * (float) zp + (float) (0.05f * zpz));

        double fp = player.getPersistentData().getDouble("fire_pos");
        double fr = player.getPersistentData().getDouble("fire_rot");

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(-0.03f * (float) (fp + 2 * fr));
            shen.setPosZ(0.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(-0.05f * (float) (fp + 2 * fr));
            shen.setPosZ(0.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.04f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float)fr * (float)((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

        tiba.setRotZ((float) (-0.25f * fp + 0.4 * fr));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double swayX = ClientEventHandler.getSwayX();
        double swayY = ClientEventHandler.getSwayY();
        float moveRotZ = (float) ClientEventHandler.getMoveRotZ();
        float movePosX = (float) ClientEventHandler.getMovePosX();
        float movePosY = (float) ClientEventHandler.getMovePosY();
        double mph = ClientEventHandler.getMovePosHorizon();
        double vY = ClientEventHandler.getVelocityY();
        double turnRotX = ClientEventHandler.getTurnRotX();
        double turnRotY = ClientEventHandler.getTurnRotY();
        double turnRotZ = ClientEventHandler.getTurnRotZ();

        root.setPosX(movePosX);
        root.setPosY((float) swayY + movePosY);
        root.setRotX((float) swayX);
        root.setRotY(0.2f * movePosX);
        root.setRotZ(0.2f * movePosX + moveRotZ);

        move.setPosX(9.3f * (float) mph);
        move.setPosY(-2f * (float) vY);
        move.setRotX(Mth.DEG_TO_RAD * (float) turnRotX - 0.15f * (float) vY);
        move.setRotY(Mth.DEG_TO_RAD * (float) turnRotY);
        move.setRotZ(2.7f * (float) mph + Mth.DEG_TO_RAD * (float) turnRotZ);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.88 * zt);
        float numP = (float) (1 - 0.28 * zt);

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
