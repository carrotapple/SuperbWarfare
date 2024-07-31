package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Mle1934Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Mle1934Model extends GeoModel<Mle1934Entity> {
    @Override
    public ResourceLocation getAnimationResource(Mle1934Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/mle1934.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mle1934Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/mle1934.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mle1934Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/mle1934.png");
    }

    @Override
    public void setCustomAnimations(Mle1934Entity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone barrle = getAnimationProcessor().getBone("barrel");
        EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        barrle.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);

        CoreGeoBone camera = getAnimationProcessor().getBone("camera");

        if (animatable.getFirstPassenger() == null) return;
        Entity gunner = animatable.getFirstPassenger();

        gunner.getPersistentData().putDouble("cannon_camera_rot_x", Mth.RAD_TO_DEG * camera.getRotX());
        gunner.getPersistentData().putDouble("cannon_camera_rot_y", Mth.RAD_TO_DEG * camera.getRotY());
        gunner.getPersistentData().putDouble("cannon_camera_rot_z", Mth.RAD_TO_DEG * camera.getRotZ());
    }
}
