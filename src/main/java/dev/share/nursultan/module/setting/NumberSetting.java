package dev.share.nursultan.module.setting;

/** A numeric module setting with a minimum and maximum. */
public final class NumberSetting {
    private final String name;
    private final double min;
    private final double max;
    private double value;

    public NumberSetting(String name, double value, double min, double max) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.value = Math.max(min, Math.min(max, value));
    }

    public String getName() {
        return name;
    }

    public double get() {
        return value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public void set(double newValue) {
        this.value = Math.max(min, Math.min(max, newValue));
    }
}
