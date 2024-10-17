package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.launcher.JavelinItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class JavelinItemModel extends GeoModel<JavelinItem> {
    @Override
    public ResourceLocation getAnimationResource(JavelinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/javelin.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(JavelinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/javelin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JavelinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/javelin.png");
    }

    @Override
    public void setCustomAnimations(JavelinItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone javelin = getAnimationProcessor().getBone("javelin");

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.GUN)) return;

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
        double fpz = ClientEventHandler.firePosZ;
        double fp = ClientEventHandler.firePos;
        double fr = ClientEventHandler.fireRot;

        gun.setPosX(1.66f * (float) zp + (float) (0.2f * zpz));
        gun.setPosY(5.5f * (float) zp + (float) (0.8f * zpz));
        gun.setPosZ(15.9f * (float) zp);
        gun.setScaleZ(1f - (0.8f * (float) zp));
        gun.setRotZ(-4.75f * Mth.DEG_TO_RAD * (float) zp + (float) (0.02f * zpz));

        javelin.setHidden(gun.getPosZ() > 15.85);
        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosZ() > 15.85));

        shen.setPosX((float) (0.75f * ClientEventHandler.recoilHorizon * fpz * fp));
        shen.setPosY((float) (-0.03f * fp - 0.06f * fr));
        shen.setPosZ((float) (0.725 * fp + 0.34f * fr + 0.95 * fpz));
        shen.setRotX((float) (0.03f * fp + 0.02f * fr + 0.02f * fpz));
        shen.setRotY((float) (0.07f * ClientEventHandler.recoilHorizon * fpz));
        shen.setRotZ((float) ((0.08f + 0.1 * fr) * ClientEventHandler.recoilHorizon));

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        root.setPosX((float) (movePosX + 20 *  ClientEventHandler.drawTime + 9.3f * mph));
        root.setPosY((float) (swayY + movePosY - 40 * ClientEventHandler.drawTime - 2f * vY));
        root.setRotX((float) (swayX - Mth.DEG_TO_RAD * 60 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotX - 0.15f * vY));
        root.setRotY((float) (0.2f * movePosX + Mth.DEG_TO_RAD * 300 * ClientEventHandler.drawTime + Mth.DEG_TO_RAD * turnRotY));
        root.setRotZ((float) (0.2f * movePosX + moveRotZ + Mth.DEG_TO_RAD * 90 * ClientEventHandler.drawTime + 2.7f * mph + Mth.DEG_TO_RAD * turnRotZ));

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        ClientEventHandler.shake(Mth.RAD_TO_DEG * camera.getRotX(),Mth.RAD_TO_DEG * camera.getRotY(), Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
