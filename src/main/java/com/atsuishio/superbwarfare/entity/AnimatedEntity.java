package com.atsuishio.superbwarfare.entity;

public interface AnimatedEntity {
    String getSyncedAnimation();

    void setAnimation(String animation);

    void setAnimationProcedure(String procedure);
}