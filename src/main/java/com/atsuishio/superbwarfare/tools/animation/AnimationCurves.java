package com.atsuishio.superbwarfare.tools.animation;


import java.util.function.Function;

public class AnimationCurves {
    public static final Function<Float, Float> LINEAR = progress -> progress;
    public static final Function<Float, Float> EASE_OUT_CIRC = progress -> (float) Math.sqrt(1 - Math.pow(progress - 1, 2));
}
