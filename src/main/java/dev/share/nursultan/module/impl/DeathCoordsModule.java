package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;

/** Prints the coordinates where you died. */
public final class DeathCoordsModule extends Module {
    private boolean reported;

    public DeathCoordsModule() {
        super("DeathCoords", "Writes your death coordinates in chat.", Category.PLAYER);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.currentScreen instanceof DeathScreen) {
            if (!reported) {
                reported = true;
                client.player.sendMessage(Text.literal("You died at XYZ "
                        + client.player.getBlockX() + " "
                        + client.player.getBlockY() + " "
                        + client.player.getBlockZ()), false);
            }
        } else {
            reported = false;
        }
    }
}
