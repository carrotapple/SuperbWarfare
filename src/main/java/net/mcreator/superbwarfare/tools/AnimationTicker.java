package net.mcreator.superbwarfare.tools;

public class AnimationTicker {

    public static long startTime;

    public static void reset() {
        startTime = System.currentTimeMillis();
    }

    public static long getProgress() {
        return System.currentTimeMillis() - startTime;
    }

}
