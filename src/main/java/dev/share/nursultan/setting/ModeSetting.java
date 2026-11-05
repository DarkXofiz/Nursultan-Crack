package dev.share.nursultan.setting;

import java.util.Arrays;

public final class ModeSetting extends Setting {
    private final String[] modes;
    private int index;

    public ModeSetting(String name, String description, String defaultMode, String... modes) {
        super(name, description);
        this.modes = modes;
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equalsIgnoreCase(defaultMode)) {
                index = i;
            }
        }
    }

    public String get() {
        return modes[index];
    }

    @Override
    public String valueToString() {
        return modes[index] + " (" + String.join(", ", modes) + ")";
    }

    @Override
    public String setFromString(String text) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equalsIgnoreCase(text)) {
                index = i;
                return null;
            }
        }
        return "Unknown mode. Options: " + Arrays.toString(modes);
    }
}
