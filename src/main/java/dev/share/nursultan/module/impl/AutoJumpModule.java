package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;

/** Jumps automatically while you walk forward on the ground. */
public final class AutoJumpModule extends Module {
    private boolean holding;

    public AutoJumpModule() {
        super("AutoJump", "Jumps automatically while walking forward.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) {
            return;
        }
        boolean shouldJump = client.options.forwardKey.isPressed() && client.player.isOnGround();
        if (shouldJump) {
            client.options.jumpKey.setPressed(true);
            holding = true;
        } else if (holding) {
            client.options.jumpKey.setPressed(false);
            holding = false;
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (holding && client != null && client.options != null) {
            client.options.jumpKey.setPressed(false);
        }
        holding = false;
    }
}
