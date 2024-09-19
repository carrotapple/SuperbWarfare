package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.launcher.JavelinItem;
import net.mcreator.superbwarfare.network.ModVariables;
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

        double zt = ClientEventHandler.getZoomTime();
        double zp = ClientEventHandler.getZoomPos();
        double zpz = ClientEventHandler.getZoomPosZ();

        gun.setPosX(1.66f * (float) zp + (float) (0.2f * zpz));
        gun.setPosY(5.5f * (float) zp + (float) (0.8f * zpz));
        gun.setPosZ(15.9f * (float) zp);
        gun.setScaleZ(1f - (0.8f * (float) zp));
        gun.setRotZ(-4.75f * Mth.DEG_TO_RAD * (float) zp + (float) (0.02f * zpz));

        javelin.setHidden(gun.getPosZ() > 15.85);
        stack.getOrCreateTag().putBoolean("HoloHidden", !(gun.getPosZ() > 15.85));

        double fp = ClientEventHandler.getFirePos();
        double fr = ClientEventHandler.getFireRot();

        shen.setPosY(0.28f * (float) (fp + 2 * fr));
        shen.setPosZ(3.8f * (float) (fp + 0.54f * fr));
        shen.setRotX(0.17f * (float) (0.18f * fp + fr));
        shen.setRotZ(-0.04f * (float) (fp + 1.3 * fr));
        shen.setPosX((float)(0.75f * fr * (player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables())).recoilHorizon * fp));

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
