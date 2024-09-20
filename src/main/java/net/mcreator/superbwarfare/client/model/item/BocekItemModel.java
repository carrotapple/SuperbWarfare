package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.special.BocekItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BocekItemModel extends GeoModel<BocekItem> {
    @Override
    public ResourceLocation getAnimationResource(BocekItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/bocek.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(BocekItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/bocek.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BocekItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/bocek.png");
    }

    @Override
    public void setCustomAnimations(BocekItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone arrow = getAnimationProcessor().getBone("arrow");
        CoreGeoBone rh = getAnimationProcessor().getBone("ys");
        CoreGeoBone lun = getAnimationProcessor().getBone("hualun1");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone shen_pos = getAnimationProcessor().getBone("shen_pos");
        CoreGeoBone xian = getAnimationProcessor().getBone("xian1");
        CoreGeoBone xian2 = getAnimationProcessor().getBone("xian2");
        CoreGeoBone fire = getAnimationProcessor().getBone("fire");
        CoreGeoBone deng = getAnimationProcessor().getBone("deng");
        CoreGeoBone deng2 = getAnimationProcessor().getBone("deng2");
        CoreGeoBone deng3 = getAnimationProcessor().getBone("deng3");
        CoreGeoBone lh = getAnimationProcessor().getBone("lh");
        CoreGeoBone r = getAnimationProcessor().getBone("r");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

        double pp = ClientEventHandler.getPullPos();
        double bp = ClientEventHandler.getBowPos();
        double hp = ClientEventHandler.getHandPos();

        arrow.setPosZ(9f * (float) bp);
        rh.setPosZ(9f * (float) hp);
        lun.setRotX(1.6f * (float) bp);

        xian.setRotX(0.56f * (float) bp);
        xian2.setRotX(-0.56f * (float) bp);
        xian.setScaleY(1f + (0.25f * (float) bp));
        xian2.setScaleY(1f + (0.25f * (float) bp));
        xian.setPosZ(9f * (float) bp);
        xian2.setPosZ(9f * (float) bp);

        gun.setScaleZ(1f - (0.2f * (float) pp));
        gun.setRotZ(0.2f * (float) pp);
        gun.setRotX(0.01f * (float) pp);
        gun.setPosZ(-3f * (float) pp);
        gun.setPosY(0.1f * (float) pp);
        r.setScaleZ(1f - (0.2f * (float) pp));
        deng2.setRotX(1.6f * (float) bp);
        deng2.setPosZ(0.05f * (float) bp);
        deng3.setRotX(-1.6f * (float) bp);
        deng3.setPosZ(0.05f * (float) bp);
        deng.setScaleZ(1f + (0.07f * (float) bp));

        double zt = ClientEventHandler.getZoomTime();
        double zp = ClientEventHandler.getZoomPos();
        double zpz = ClientEventHandler.getZoomPosZ();

        lh.setRotX(0.2f * (float) zp);
        shen_pos.setPosX(-3.4f * (float) zp);
        shen_pos.setPosY(6.76f * (float) zp - (float) (0.2f * zpz));
        shen_pos.setPosZ(6.4f * (float) zp + (float) (0.3f * zpz));
        r.setScaleZ(1f - (0.31f * (float) zp));
        shen.setRotZ(60 * Mth.DEG_TO_RAD * (float) zp + (float) (0.05f * zpz) - 0.2f);

        stack.getOrCreateTag().putBoolean("HoloHidden", !((shen_pos.getPosX() < -0.7 && gun.getPosZ() < -2.6)));


        double fp = ClientEventHandler.getFirePos();
        double fr = ClientEventHandler.getFireRot();

        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
            fire.setPosY(-0.01f * (float) (fp + 2 * fr));
            fire.setPosZ(3f * (float) (fp + 0.54f * fr));
            fire.setRotX(0.003f * (float) (fp + fr));
            fire.setRotZ(0f);
        } else {
            fire.setPosY(-0.03f * (float) (fp + 2 * fr));
            fire.setPosZ(4f * (float) (fp + 0.54f * fr));
            fire.setRotX(0.07f * (float) (0.18f * fp + fr));
            fire.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        }

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
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
