package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;

public final class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("AutoRespawn", "Respawns automatically when you die.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player != null && client.currentScreen instanceof DeathScreen) {
            client.player.requestRespawn();
        }
    }
}
