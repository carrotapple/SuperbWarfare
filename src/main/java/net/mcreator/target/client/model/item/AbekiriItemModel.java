package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Abekiri;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AbekiriItemModel extends GeoModel<Abekiri> {
    @Override
    public ResourceLocation getAnimationResource(Abekiri animatable) {
        return new ResourceLocation("target", "animations/abekiri.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Abekiri animatable) {
        return new ResourceLocation("target", "geo/abekiri.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Abekiri animatable) {
        return new ResourceLocation("target", "textures/item/abekiri.png");
    }

    @Override
    public void setCustomAnimations(Abekiri animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");


        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(2.5f * (float) p);

        gun.setPosY(1.7f * (float) p - (float) (0.4f * zp));

        gun.setPosZ(2f * (float) p + (float) (0.3f * zp));

        gun.setRotZ((float) (0.05f * zp));

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ(4f * (float) fp);

        shen.setRotX(0.08f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

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

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);
    }
}
