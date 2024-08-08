package net.mcreator.superbwarfare.entity.model;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.Target1Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import static net.mcreator.superbwarfare.entity.Target1Entity.DOWN_TIME;

public class Target1Model extends GeoModel<Target1Entity> {
    @Override
    public ResourceLocation getAnimationResource(Target1Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "animations/target.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Target1Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "geo/target.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Target1Entity entity) {
        return new ResourceLocation(ModUtils.MODID, "textures/entity/target.png");
    }

    @Override
    public void setCustomAnimations(Target1Entity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("ba");
        if (animatable.getEntityData().get(DOWN_TIME) > 20) {
            head.setRotX(Mth.clamp(90 - animatable.getEntityData().get(DOWN_TIME),0,3) * 30 * Mth.DEG_TO_RAD);
        } else {
            head.setRotX(4.5f * animatable.getEntityData().get(DOWN_TIME) * Mth.DEG_TO_RAD);
        }
    }
}
