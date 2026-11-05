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

    public abstract String valueToString();

    /** Parses and applies the text. Returns null on success, otherwise an error message. */
    public abstract String setFromString(String text);
}
