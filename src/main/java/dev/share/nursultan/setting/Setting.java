package dev.share.nursultan.setting;

/** A named, user-adjustable value of a module. */
public abstract class Setting {
    private final String name;
    private final String description;

    protected Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** Human-readable value, may include the allowed range or options. */
    public abstract String valueToString();

    /** Raw value used for saving the config. */
    public abstract String serialize();

    /** Short text for buttons, e.g. "range: 3.0". */
    public abstract String label();

    /** Click action for toggle-like settings (boolean, mode). Numbers ignore it. */
    public void cycle() {
    }

    /** Parses and applies the text. Returns null on success, otherwise an error message. */
    public abstract String setFromString(String text);
}
