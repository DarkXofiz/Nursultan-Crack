package dev.share.nursultan.module;

import dev.share.nursultan.setting.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Base class for a toggleable client feature. */
public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled;
    private int keyBind;

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    /** GLFW key code that toggles this module, or 0 for none. */
    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    /** Combat modules always start disabled; the others remember their state. */
    public boolean shouldPersistEnabled() {
        return category != Category.COMBAT;
    }

    protected <T extends Setting> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    public Optional<Setting> findSetting(String settingName) {
        return settings.stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findFirst();
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    /** Called once per client tick while the module is enabled. */
    public void onTick(MinecraftClient client) {
    }
}
