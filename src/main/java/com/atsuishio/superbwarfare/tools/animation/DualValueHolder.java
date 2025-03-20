package com.atsuishio.superbwarfare.tools.animation;

public class DualValueHolder<T> {
    private T oldValue;
    private T newValue;

    public static <T> DualValueHolder<T>[] create(int size, T defaultValue) {
        // 傻逼Java
        @SuppressWarnings("unchecked")
        DualValueHolder<T>[] holders = new DualValueHolder[size];

        for (int i = 0; i < size; i++) {
            holders[i] = new DualValueHolder<>();
            holders[i].reset(defaultValue);
        }

        return holders;
    }

    public void update(T value) {
        this.oldValue = this.newValue;
        this.newValue = value;
    }

    public void reset(T value) {
        this.oldValue = value;
        this.newValue = value;
    }

    public T oldValue() {
        return this.oldValue;
    }

    public T newValue() {
        return this.newValue;
    }

    public boolean changed() {
        return this.oldValue.equals(this.newValue);
    }
}
