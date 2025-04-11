package com.atsuishio.superbwarfare.client.model.entity;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.entity.projectile.SmallCannonShellEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SmallCannonShellModel extends GeoModel<SmallCannonShellEntity> {

    @Override
    public ResourceLocation getAnimationResource(SmallCannonShellEntity entity) {
        return Mod.loc("animations/cannon_shell.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SmallCannonShellEntity entity) {
        return Mod.loc("geo/cannon_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmallCannonShellEntity entity) {
        return Mod.loc("textures/entity/cannon_shell.png");
    }

    @Override
    public void setCustomAnimations(SmallCannonShellEntity animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone bone = getAnimationProcessor().getBone("bone");
        bone.setScaleX(0.17f);
        bone.setScaleY(0.17f);
        bone.setScaleZ(0.17f);
    }
}
