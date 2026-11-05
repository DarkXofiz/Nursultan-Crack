package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

/** Shows the name and health of the creature under your crosshair. */
public final class TargetInfoModule extends Module {
    public TargetInfoModule() {
        super("TargetInfo", "Shows name and health of the entity you look at.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        Entity target = client.targetedEntity;
        if (target instanceof LivingEntity living) {
            String info = living.getName().getString() + " | HP "
                    + String.format("%.1f", living.getHealth()) + "/"
                    + String.format("%.1f", living.getMaxHealth());
            client.inGameHud.setOverlayMessage(Text.literal(info), false);
        }
    }
}
