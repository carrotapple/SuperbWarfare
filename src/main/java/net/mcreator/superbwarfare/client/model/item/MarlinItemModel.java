package net.mcreator.superbwarfare.client.model.item;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.ClientEventHandler;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.gun.rifle.MarlinItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MarlinItemModel extends GeoModel<MarlinItem> {
    @Override
    public ResourceLocation getAnimationResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "animations/marlin.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "geo/marlin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarlinItem animatable) {
        return new ResourceLocation(ModUtils.MODID, "textures/item/marlin.png");
    }

    @Override
    public void setCustomAnimations(MarlinItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone jichui = getAnimationProcessor().getBone("jichui");

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

        gun.setPosX(1.712f * (float) zp);

        gun.setPosY(1.06f * (float) zp - (float) (0.7f * zpz));

        gun.setPosZ(4f * (float) zp + (float) (0.9f * zpz));

        gun.setRotZ((float) (0.02f * zpz));

        gun.setScaleZ(1f - (0.5f * (float) zp));

        shen.setPosX(-0.2f * (float) (fp + 2 * fr));
        shen.setPosY(0.4f * (float) (fp + 2 * fr));
        shen.setPosZ(1.9f * (float) (1.3 * fp + 0.54f * fr));
        shen.setRotX(0.085f * (float) (1.28f * fp + fr));
        shen.setRotZ(-0.03f * (float) (fp + 1.3 * fr));
        shen.setRotY(-0.05f * (float) fr);


        if (stack.getOrCreateTag().getInt("fire_animation") > 0) {
            jichui.setRotX(-0.52f);
        }

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

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");
        CoreGeoBone main = getAnimationProcessor().getBone("0");

        float numR = (float) (1 - 0.55 * zt);
        float numP = (float) (1 - 0.88 * zt);

        if (stack.getOrCreateTag().getBoolean("reloading")) {
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
