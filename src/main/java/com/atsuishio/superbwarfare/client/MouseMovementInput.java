package com.atsuishio.superbwarfare.client;

/**
 * Codes from @getItemFromBlock's Create-Tweaked-Controllers
 */
public class MouseMovementInput {

    public boolean useVelocity = false;
    public boolean isYAxis = false;
    public float minBound = 0.0f;
    public float maxBound = 1000.0f;

    public MouseMovementInput(boolean axis, float min, float max, boolean useVel) {
        this.isYAxis = axis;
        this.minBound = min;
        this.maxBound = max;
        this.useVelocity = useVel;
    }

    public MouseMovementInput() {
    }

    public boolean hasOutput() {
        return getAxisValue() >= 0.5f;
    }

    public float getAxisValue() {
        if (isInputInvalid()) return 0;
        float v = isYAxis ? MouseMovementHandler.getY(useVelocity) : MouseMovementHandler.getX(useVelocity);
        v = (v - minBound) / (maxBound - minBound);
        if (v < 0) v = 0;
        if (v > 1) v = 1;
        return v;
    }

    public boolean isInputInvalid() {
        return minBound == maxBound;
    }

    public int getValue() {
        return isYAxis ? 1 : 0;
    }

    public float getRawInput() {
        if (isInputInvalid()) return 0;
        return isYAxis ? MouseMovementHandler.getY(useVelocity) : MouseMovementHandler.getX(useVelocity);
    }
}
