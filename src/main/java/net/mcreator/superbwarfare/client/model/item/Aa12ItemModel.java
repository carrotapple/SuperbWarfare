package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.shotgun.Aa12Item;
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

public class Aa12ItemModel extends GeoModel<Aa12Item> {
    @Override
    public ResourceLocation getAnimationResource(Aa12Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/aa12.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Aa12Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/aa12.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Aa12Item animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/aa12_new.png");
    }

    @Override
    public void setCustomAnimations(Aa12Item animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double zt = ClientEventHandler.getZoomTime();
        double zp = ClientEventHandler.getZoomPos();
        double zpz = ClientEventHandler.getZoomPosZ();

        gun.setPosX(2.105f * (float) zp);
        gun.setPosY(0.17f * (float) zp - (float) (0.2f * zpz));
        gun.setPosZ(0.1f * (float) zp + (float) (0.3f * zpz));
        gun.setRotZ((float) (0.02f * zpz));
        gun.setScaleZ(1f - (0.4f * (float) zp));

        double fp = ClientEventHandler.getFirePos();
        double fr = ClientEventHandler.getFireRot();

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(0.02f * (float) (fp + 2 * fr));
            shen.setPosZ(1.3f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.003f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.08f * (float) (fp + 2 * fr));
            shen.setPosZ(1.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.07f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float) fr * (float) ((player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

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
        float numR = (float) (1 - 0.82 * zt);
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
        }
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
