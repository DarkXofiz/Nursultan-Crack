package dev.share.nursultan.setting;

import java.util.Locale;

public final class NumberSetting extends Setting {
    private double value;
    private final double min;
    private final double max;

    public NumberSetting(String name, String description, double value, double min, double max) {
        super(name, description);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public double get() {
        return value;
    }

    /** Moves the value one step up (direction 1) or down (direction -1). */
    public void adjust(int direction) {
        double step = Math.max(0.1, Math.round((max - min) / 20.0 * 10.0) / 10.0);
        double next = value + direction * step;
        next = Math.round(next * 10.0) / 10.0;
        value = Math.max(min, Math.min(max, next));
    }

    @Override
    public String label() {
        return getName() + ": " + String.format(Locale.ROOT, "%.1f", value);
    }

    @Override
    public String valueToString() {
        return String.format(Locale.ROOT, "%.1f", value) + " (" + min + " - " + max + ")";
    }

    @Override
    public String serialize() {
        return Double.toString(value);
    }

    @Override
    public String setFromString(String text) {
        double parsed;
        try {
            parsed = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return "Not a number: " + text;
        }
        if (parsed < min || parsed > max) {
            return "Value must be between " + min + " and " + max;
        }
        value = parsed;
        return null;
    }
}
