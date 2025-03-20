package com.atsuishio.superbwarfare.tools.animation;

import net.minecraft.util.Mth;

import java.util.function.Function;

public class AnimationTimer {
    private final long duration;
    private long startTime;
    private boolean reversed;
    private boolean initialized;
    private boolean isStart = true;

    private Function<Double, Double> forwardAnimationCurve = AnimationCurves.LINEAR;
    private Function<Double, Double> backwardAnimationCurve = AnimationCurves.LINEAR;

    /**
     * 创建一个动画计时器
     *
     * @param duration 动画持续时间，单位为毫秒
     */
    public AnimationTimer(long duration) {
        this.duration = duration;
    }

    public AnimationTimer animation(Function<Double, Double> animationCurve) {
        return this.forwardAnimation(animationCurve).backwardAnimation(animationCurve);
    }

    public AnimationTimer forwardAnimation(Function<Double, Double> animationCurve) {
        this.forwardAnimationCurve = animationCurve;
        return this;
    }

    public AnimationTimer backwardAnimation(Function<Double, Double> animationCurve) {
        this.backwardAnimationCurve = animationCurve;
        return this;
    }

    /**
     * 创建多个线性动画计时器
     *
     * @param size     计时器数量
     * @param duration 动画持续时间，单位为毫秒
     */

    public static AnimationTimer[] createTimers(int size, long duration) {
        return createTimers(size, duration, AnimationCurves.LINEAR);
    }

    /**
     * 创建多个动画计时器
     *
     * @param size           计时器数量
     * @param duration       动画持续时间，单位为毫秒
     * @param animationCurve 动画曲线函数
     */
    public static AnimationTimer[] createTimers(int size, long duration, Function<Double, Double> animationCurve) {
        return createTimers(size, duration, animationCurve, animationCurve);
    }

    /**
     * 创建多个动画计时器
     *
     * @param size                   计时器数量
     * @param duration               动画持续时间，单位为毫秒
     * @param forwardAnimationCurve  正向动画曲线函数
     * @param backwardAnimationCurve 反向动画曲线函数
     */
    public static AnimationTimer[] createTimers(int size, long duration, Function<Double, Double> forwardAnimationCurve, Function<Double, Double> backwardAnimationCurve) {
        var timers = new AnimationTimer[size];
        var currentTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            timers[i] = new AnimationTimer(duration).forwardAnimation(forwardAnimationCurve).backwardAnimation(backwardAnimationCurve);
            timers[i].end();
            timers[i].backward(currentTime);
        }
        return timers;
    }

    public boolean isForward() {
        return !reversed;
    }


    /**
     * 获取当前进度
     *
     * @return 进度值，范围在0到1之间
     */
    public float getProgress(long currentTime) {
        if (reversed) {
            return 1 - backwardAnimationCurve.apply(Mth.clamp(1 - getElapsedTime(currentTime) / (double) duration, 0, 1)).floatValue();
        } else {
            return forwardAnimationCurve.apply(Mth.clamp(getElapsedTime(currentTime) / (double) duration, 0, 1)).floatValue();
        }
    }

    private long getElapsedTime(long currentTime) {
        if (!initialized) return 0;

        if (reversed) {
            return Math.min(duration, Math.max(0, startTime - currentTime));
        } else {
            return Math.min(duration, currentTime - startTime);
        }
    }

    /**
     * 当前动画是否已经结束
     */
    public boolean finished(long currentTime) {
        return getProgress(currentTime) >= 1;
    }

    /**
     * 将计时器设置为开始状态
     */
    public void begin() {
        initialized = false;
        isStart = true;
    }

    /**
     * 将计时器设置为结束状态
     */
    public void end() {
        initialized = false;
        isStart = false;
    }

    /**
     * 正向开始计时
     */
    public void forward(long currentTime) {
        if (!initialized) {
            initialized = true;
            startTime = currentTime + (isStart ? 0 : duration);
        } else {
            startTime = currentTime - getElapsedTime(currentTime);
        }
        reversed = false;
    }

    /**
     * 反向开始计时
     */
    public void backward(long currentTime) {
        if (!initialized) {
            initialized = true;
            startTime = currentTime + (isStart ? duration : 0);
        } else {
            startTime = currentTime + getElapsedTime(currentTime);
        }
        reversed = true;
    }

}
