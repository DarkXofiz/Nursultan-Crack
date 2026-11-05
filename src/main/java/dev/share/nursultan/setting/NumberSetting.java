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

    @Override
    public String valueToString() {
        return String.format(Locale.ROOT, "%.1f", value) + " (" + min + " - " + max + ")";
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
