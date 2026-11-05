package dev.share.nursultan.setting;

public final class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, String description, boolean value) {
        super(name, description);
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    @Override
    public void cycle() {
        value = !value;
    }

    @Override
    public String label() {
        return getName() + ": " + (value ? "ON" : "OFF");
    }

    @Override
    public String valueToString() {
        return value ? "true" : "false";
    }

    @Override
    public String serialize() {
        return value ? "true" : "false";
    }

    @Override
    public String setFromString(String text) {
        String t = text.toLowerCase();
        if (t.equals("true") || t.equals("on") || t.equals("yes")) {
            value = true;
            return null;
        }
        if (t.equals("false") || t.equals("off") || t.equals("no")) {
            value = false;
            return null;
        }
        return "Use true or false.";
    }
}
