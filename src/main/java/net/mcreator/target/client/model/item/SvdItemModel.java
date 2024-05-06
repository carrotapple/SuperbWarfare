package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.SvdItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SvdItemModel extends GeoModel<SvdItem> {
    @Override
    public ResourceLocation getAnimationResource(SvdItem animatable) {
        return new ResourceLocation("target", "animations/svd.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SvdItem animatable) {
        return new ResourceLocation("target", "geo/svd.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SvdItem animatable) {
        return new ResourceLocation("target", "textures/item/svd.png");
    }

    @Override
    public void setCustomAnimations(SvdItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt");
        CoreGeoBone scope = getAnimationProcessor().getBone("pso1");
        CoreGeoBone sight = getAnimationProcessor().getBone("handguard");
        CoreGeoBone bt1 = getAnimationProcessor().getBone("bullton1");
        CoreGeoBone bt2 = getAnimationProcessor().getBone("bullton2");
        CoreGeoBone shuan = getAnimationProcessor().getBone("shuan");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().getDouble("gj") == 1) {
            bolt.setPosZ(3.25f);
        }

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(2.52f * (float) p);

        gun.setPosY(0.25f * (float) p - (float) (0.6f * zp));

        gun.setPosZ(13.4f * (float) p + (float) (0.5f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.8f * (float) p));

        scope.setScaleZ(1f - (0.95f * (float) p));

        bt1.setScaleY(1f - (0.5f * (float) p));

        bt2.setScaleX(1f - (0.5f * (float) p));

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        if (gun.getPosX() > 1.9) {
            holo.setPosY(0.05f);
            holo.setScaleX(0.45f);
            holo.setScaleY(0.45f);
            sight.setScaleX(0f);
            sight.setScaleY(0f);
        } else {
            holo.setPosY(0);
            holo.setScaleX(0);
            holo.setScaleY(0);
            sight.setScaleX(1f);
            sight.setScaleY(1f);
        }

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ(1.8f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosY(0.1f * (float) fp);
        } else {
            shen.setPosY(0.2f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.002f * (float) fp);
        } else {
            shen.setRotX(0.08f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

        shuan.setPosZ(2.4f * (float) fp);

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

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
    }
}
