package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.RpgItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class RpgItemModel extends GeoModel<RpgItem> {
    @Override
    public ResourceLocation getAnimationResource(RpgItem animatable) {
        return new ResourceLocation("target", "animations/rpg.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RpgItem animatable) {
        return new ResourceLocation("target", "geo/rpg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RpgItem animatable) {
        return new ResourceLocation("target", "textures/item/rpg7.png");
    }

    @Override
    public void setCustomAnimations(RpgItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone rocket = getAnimationProcessor().getBone("Rockets");
        CoreGeoBone shen = getAnimationProcessor().getBone("rpg");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getDouble("empty") == 1) {
            rocket.setScaleX(0);
            rocket.setScaleY(0);
            rocket.setScaleZ(0);
        }

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ((float) fp);

        shen.setRotX(0.05f * (float) fp);

        if (Math.random() < 0.5) {
            shen.setRotZ(0.01f * (float) fp);
        } else {
            shen.setRotZ(-0.01f * (float) fp);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(0.91f * (float) p);

        gun.setPosY(-0.04f * (float) p - (float) (0.2f * zp));

        gun.setPosZ(2f * (float) p + (float) (0.15f * zp));

        gun.setRotZ(0.45f * (float) p + (float) (0.02f * zp));

        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
        }

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

        move.setPosY(-1.3f * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);

    }
}
