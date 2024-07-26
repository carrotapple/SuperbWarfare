package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Mk42Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class Mk42Model extends GeoModel<Mk42Entity> {
    @Override
    public ResourceLocation getAnimationResource(Mk42Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/mk_42.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Mk42Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/sherman.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mk42Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/sherman.png");
    }

    @Override
    public void setCustomAnimations(Mk42Entity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone barrle = getAnimationProcessor().getBone("maingun");
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
