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
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled;

    protected Module(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
