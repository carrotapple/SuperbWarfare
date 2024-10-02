package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.sniper.SentinelItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SentinelItemModel extends GeoModel<SentinelItem> {
    @Override
    public ResourceLocation getAnimationResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/sentinel.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/sentinel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SentinelItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/sentinel.png");
    }

    @Override
    public void setCustomAnimations(SentinelItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone scope = getAnimationProcessor().getBone("scope2");
        CoreGeoBone ammo = getAnimationProcessor().getBone("ammobar");
        CoreGeoBone cb = getAnimationProcessor().getBone("chamber2");

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

        gun.setPosX(2.928f * (float) zp);

        gun.setPosY(-0.062f * (float) zp - (float) (0.1f * zpz));

        gun.setPosZ(10f * (float) zp + (float) (0.3f * zpz));

        gun.setRotZ((float) (0.05f * zpz));

        gun.setScaleZ(1f - (0.7f * (float) zp));

        scope.setScaleZ(1f - (0.8f * (float) zp));

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        cb.setRotZ(cb.getRotZ() + times * 0.03f * (float) (stack.getOrCreateTag().getDouble("chamber_rot")));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        holo.setPosY(0.09f);

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            shen.setPosY(0.1f * (float) (fp + 2 * fr));
            shen.setPosZ(4.6f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.12f * (float) (fp + fr));
            shen.setRotZ(0f);
        } else {
            shen.setPosY(0.2f * (float) (fp + 2 * fr));
            shen.setPosZ(5.2f * (float) (fp + 0.54f * fr));
            shen.setRotX(0.15f * (float) (0.18f * fp + fr));
            shen.setRotZ(-0.01f * (float) (fp + 1.3 * fr));
        }
        shen.setPosX(0.5f * (float) fr * (float) (ClientEventHandler.recoilHorizon * fp));

        CoreGeoBone charge = getAnimationProcessor().getBone("charge");

        charge.setRotZ(charge.getRotZ() + times * 0.05f);

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        CoreGeoBone move = getAnimationProcessor().getBone("move");

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

        if ((stack.getOrCreateTag().getDouble("ammo") <= 5)) {
            ammo.setScaleX((float) (stack.getOrCreateTag().getDouble("ammo") / 5));
        }

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.9 * zt);
        float numP = (float) (1 - 0.98 * zt);

        if (stack.getOrCreateTag().getInt("gun_reloading_time") > 0 || stack.getOrCreateTag().getBoolean("sentinel_is_charging")) {
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
