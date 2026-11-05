package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;

public final class AutoWalkModule extends Module {
    public AutoWalkModule() {
        super("AutoWalk", "Holds the forward key while enabled.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player != null && client.currentScreen == null) {
            client.options.forwardKey.setPressed(true);
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null) {
            client.options.forwardKey.setPressed(false);
        }
    }
}
