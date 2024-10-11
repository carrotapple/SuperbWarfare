package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.M98bItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class M98bItemModel extends GeoModel<M98bItem> {
    @Override
    public ResourceLocation getAnimationResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/m98b.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/m98b.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M98bItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/m98b.png");
    }

    @Override
    public void setCustomAnimations(M98bItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope2");

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

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(0.2f * (float) (fp + 2 * fr));
            shen.setPosZ(4.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.1f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.3f * (float) (fp + 2 * fr));
            shen.setPosZ(5.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.15f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        scope.setPosZ(75.2f * (float) (fp + 0.54f * fr));
        shen.setPosX(0.2f * (float) (ClientEventHandler.recoilHorizon * (0.5 + 0.4 * ClientEventHandler.fireSpread)));

        gun.setPosX(2.245f * (float) zp);
        gun.setPosY(0.3f * (float) zp - (float) (0.2f * zpz));
        gun.setPosZ(4.2f * (float) zp + (float) (0.3f * zpz));
        gun.setRotZ((float) (0.02f * zpz));

        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosX() > 1.8));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        CoreGeoBone zhunxing = getAnimationProcessor().getBone("shi");

        zhunxing.setPosX(75 * movePosX);
        zhunxing.setPosY(75 * movePosY);

        root.setPosX((float) (movePosX + 20 *  ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        float numR = (float) (1 - 0.88 * zt);
        float numP = (float) (1 - 0.68 * zt);

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

            scope.setRotX(numR * scope.getRotX());
            scope.setRotY(numR * scope.getRotY());
            scope.setRotZ(numR * scope.getRotZ());
            scope.setPosX(numP * scope.getPosX());
            scope.setPosY(numP * scope.getPosY());
            scope.setPosZ(numP * scope.getPosZ());
        }
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
