package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.rifle.Hk416Item;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class Hk416ItemModel extends GeoModel<Hk416Item> {
    @Override
    public ResourceLocation getAnimationResource(Hk416Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/hk416.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Hk416Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/hk416.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hk416Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/hk416.png");
    }

    @Override
    public void setCustomAnimations(Hk416Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("eotech");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double zt = ClientEventHandler.zoomTime;
        double zp = ClientEventHandler.zoomPos;
        double zpz = ClientEventHandler.zoomPosZ;
        double swayX = ClientEventHandler.swayX;
        double swayY = ClientEventHandler.swayY;
        float moveRotZ = (float) ClientEventHandler.moveRotZ;
        float movePosX = (float) ClientEventHandler.movePosX;
        float movePosY = (float) ClientEventHandler.movePosY;
        double mph = ClientEventHandler.movePosHorizon;
        double vY = ClientEventHandler.velocityY;
        double turnRotX = ClientEventHandler.turnRot[0];
        double turnRotY = ClientEventHandler.turnRot[1];
        double turnRotZ = ClientEventHandler.turnRot[2];
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

        gun.setPosX(3.34f * (float) zp);
        gun.setPosY(0.54f * (float) zp - (float) (0.2f * zpz));
        gun.setPosZ(2.5f * (float) zp + (float) (0.3f * zpz));
        gun.setRotZ((float) (0.05f * zpz));

        scope.setScaleZ(1f - (0.7f * (float) zp));

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 3.1));

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(-0.01f * (float) (fp + 2 * fr));
            shen.setPosZ(0.4f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.007f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(-0.03f * (float) (fp + 2 * fr));
            shen.setPosZ(0.8f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.2f * (float) (ClientEventHandler.recoilHorizon * (0.5 + 0.4 * ClientEventHandler.fireSpread)));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 *  ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.88 * zt);
        float numP = (float) (1 - 0.78 * zt);

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());

        CoreGeoBone shell1 = getAnimationProcessor().getBone("shell1");
        CoreGeoBone shell2 = getAnimationProcessor().getBone("shell2");
        CoreGeoBone shell3 = getAnimationProcessor().getBone("shell3");
        CoreGeoBone shell4 = getAnimationProcessor().getBone("shell4");
        CoreGeoBone shell5 = getAnimationProcessor().getBone("shell5");

        ClientEventHandler.handleShells(1f, 0.55f, shell1, shell2, shell3, shell4, shell5);
    }
}
