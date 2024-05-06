package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Taser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TaserItemModel extends GeoModel<Taser> {
    @Override
    public ResourceLocation getAnimationResource(Taser animatable) {
        return new ResourceLocation("target", "animations/taser.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Taser animatable) {
        return new ResourceLocation("target", "geo/taser.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Taser animatable) {
        return new ResourceLocation("target", "textures/item/tasergun.png");
    }

    @Override
    public void setCustomAnimations(Taser animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        Player player = Minecraft.getInstance().player;

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ(0.75f * (float) fp);

        shen.setRotX(0.02f * (float) fp);

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(2.6f * (float) p);

        gun.setPosY(1.2f * (float) p - (float) (0.6f * zp));

        gun.setPosZ((float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosY((float) y);
        root.setRotX((float) x);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = 0;
        m = player.getPersistentData().getDouble("move");

        double yaw = 0;
        yaw = player.getPersistentData().getDouble("yaw");

        double pit = 0;
        pit = player.getPersistentData().getDouble("gunpitch");

        double vy = 0;
        vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);

    }
}
