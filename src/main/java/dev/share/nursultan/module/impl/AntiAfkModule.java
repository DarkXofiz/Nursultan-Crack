package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;

/** Jumps briefly every 30 seconds so you are not kicked for being idle. */
public final class AntiAfkModule extends Module {
    private static final int INTERVAL_TICKS = 600;
    private int ticks;

    public AntiAfkModule() {
        super("AntiAFK", "Jumps every 30 seconds to avoid AFK kicks.");
    }

    @Override
    protected void onEnable() {
        ticks = 0;
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) {
            return;
        }
        ticks++;
        int phase = ticks % INTERVAL_TICKS;
        if (phase == 0) {
            client.options.jumpKey.setPressed(true);
        } else if (phase == 2) {
            client.options.jumpKey.setPressed(false);
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null) {
            client.options.jumpKey.setPressed(false);
        }
    }
}
