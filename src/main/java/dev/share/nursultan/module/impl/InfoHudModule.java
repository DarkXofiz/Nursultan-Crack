package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/** Shows FPS and coordinates in the action bar. */
public final class InfoHudModule extends Module {
    private int ticks;

    public InfoHudModule() {
        super("InfoHUD", "Shows FPS and coordinates above the hotbar.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        ticks++;
        if (ticks % 10 != 0) {
            return;
        }
        String info = "FPS " + client.getCurrentFps()
                + " | XYZ " + client.player.getBlockX()
                + " " + client.player.getBlockY()
                + " " + client.player.getBlockZ();
        client.inGameHud.setOverlayMessage(Text.literal(info), false);
    }
}
