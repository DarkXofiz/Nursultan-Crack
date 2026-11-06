package dev.share.nursultan.module;

import dev.share.nursultan.util.KeyUtil;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Module, Boolean> bindWasDown = new HashMap<>();

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> byCategory(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    public Optional<Module> find(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
    }

    public void tick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }

    /** Toggles modules whose bound key was just pressed (only while no screen is open). */
    public void handleBinds(MinecraftClient client) {
        long handle = client.getWindow().getHandle();
        for (Module module : modules) {
            int key = module.getKeyBind();
            if (key == 0) {
                continue;
            }
            boolean down = KeyUtil.isDown(handle, key);
            boolean was = bindWasDown.getOrDefault(module, false);
            bindWasDown.put(module, down);
            if (down && !was && client.currentScreen == null) {
                module.toggle();
            }
        }
    }
}
