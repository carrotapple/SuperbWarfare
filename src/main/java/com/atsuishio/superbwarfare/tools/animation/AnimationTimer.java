package com.atsuishio.superbwarfare.tools.animation;

import net.minecraft.util.Mth;

import java.util.function.Function;

public class AnimationTimer {
    private final long duration;
    private long startTime;
    private boolean reversed;
    private boolean initialized;
    private boolean isStart = true;

    private final Function<Float, Float> animationCurve;

    /**
     * 创建一个反向线性动画计时器
     *
     * @param duration 动画持续时间，单位为毫秒
     */
    public AnimationTimer(long duration) {
        this(duration, AnimationCurves.LINEAR);
    }

    /**
     * 创建一个反向动画计时器
     *
     * @param duration       动画持续时间，单位为毫秒
     * @param animationCurve 动画曲线函数
     */
    public AnimationTimer(long duration, Function<Float, Float> animationCurve) {
        this.duration = duration;
        this.animationCurve = animationCurve;
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
    public static AnimationTimer[] createTimers(int size, long duration, Function<Float, Float> animationCurve) {
        var timers = new AnimationTimer[size];
        var currentTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            timers[i] = new AnimationTimer(duration, animationCurve);
            timers[i].end();
            timers[i].backward(currentTime);
        }
        return timers;
    }


    /**
     * 获取当前进度
     *
     * @return 进度值，范围在0到1之间
     */
    public float getProgress(long currentTime) {
        if (reversed) {
            return 1 - animationCurve.apply(Mth.clamp(1 - getElapsedTime(currentTime) / (float) duration, 0, 1));
        } else {
            return animationCurve.apply(Mth.clamp(getElapsedTime(currentTime) / (float) duration, 0, 1));
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
