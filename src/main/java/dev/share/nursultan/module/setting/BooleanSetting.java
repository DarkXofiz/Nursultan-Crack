package dev.share.nursultan.module.setting;

/** An on/off module setting. */
public final class BooleanSetting {
    private final String name;
    private boolean value;

    public BooleanSetting(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean newValue) {
        this.value = newValue;
    }
}
