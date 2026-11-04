package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;

public final class AutoSprintModule extends Module {
    public AutoSprintModule() {
        super("AutoSprint", "Holds the sprint key while enabled.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player != null) {
            client.options.sprintKey.setPressed(true);
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null) {
            client.options.sprintKey.setPressed(false);
        }
    }
}
