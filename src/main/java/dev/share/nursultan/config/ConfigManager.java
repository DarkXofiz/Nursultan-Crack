package dev.share.nursultan.config;

import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.module.ModuleManager;
import dev.share.nursultan.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Saves module states, key binds and settings to config/nursultan_opensource.properties. */
public final class ConfigManager {
    private ConfigManager() {
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("nursultan_opensource.properties");
    }

    public static void load(ModuleManager modules) {
        Path file = path();
        if (!Files.exists(file)) {
            return;
        }
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            props.load(in);
        } catch (IOException e) {
            NursultanClient.LOGGER.warn("Could not read config", e);
            return;
        }
        for (Module module : modules.getModules()) {
            String prefix = "module." + module.getName() + ".";
            String bind = props.getProperty(prefix + "bind");
            if (bind != null) {
                try {
                    module.setKeyBind(Integer.parseInt(bind));
                } catch (NumberFormatException ignored) {
                    // keep default
                }
            }
            for (Setting setting : module.getSettings()) {
                String raw = props.getProperty(prefix + "setting." + setting.getName());
                if (raw != null) {
                    setting.setFromString(raw);
                }
            }
            if (module.shouldPersistEnabled() && "true".equals(props.getProperty(prefix + "enabled"))) {
                module.setEnabled(true);
            }
        }
    }

    public static void save(ModuleManager modules) {
        Properties props = new Properties();
        for (Module module : modules.getModules()) {
            String prefix = "module." + module.getName() + ".";
            props.setProperty(prefix + "bind", Integer.toString(module.getKeyBind()));
            if (module.shouldPersistEnabled()) {
                props.setProperty(prefix + "enabled", Boolean.toString(module.isEnabled()));
            }
            for (Setting setting : module.getSettings()) {
                props.setProperty(prefix + "setting." + setting.getName(), setting.serialize());
            }
        }
        try (OutputStream out = Files.newOutputStream(path())) {
            props.store(out, "Nursultan Open Source");
        } catch (IOException e) {
            NursultanClient.LOGGER.warn("Could not save config", e);
        }
    }
}
