package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.Minigun;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MinigunItemModel extends GeoModel<Minigun> {
    @Override
    public ResourceLocation getAnimationResource(Minigun animatable) {
        return new ResourceLocation("target", "animations/minigun.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Minigun animatable) {
        return new ResourceLocation("target", "geo/minigun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Minigun animatable) {
        return new ResourceLocation("target", "textures/item/minigun.png");
    }

    @Override
    public void setCustomAnimations(Minigun animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("barrel");
        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        float fps = Minecraft.getInstance().getFps();
        if (fps <= 0) {
            fps = 1f;
        }
        float times = 250f / fps;

        double RotZ = 0;
        RotZ = stack.getOrCreateTag().getDouble("rot");

        gun.setRotZ((float) (gun.getRotZ() + times * -0.008f * RotZ));


        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        shen.setPosZ((float) fp);

        shen.setRotX(0.02f * (float) fp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilHorizon == 1) {
            shen.setRotZ(0.003f * (float) fp);
        } else {
            shen.setRotZ(-0.003f * (float) fp);
        }

        if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
            flare.setScaleX((float) (1 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1 + 0.5 * (Math.random() - 0.5)));
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

        move.setPosY(-1.8f * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(2.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(3.9f * (float) yaw - 1.7f * (float) m);
    }
}
