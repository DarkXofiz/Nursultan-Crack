package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/** Client-side night vision effect (visual only). */
public final class FullbrightModule extends Module {
    public FullbrightModule() {
        super("Fullbright", "Keeps a hidden night vision effect on you.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        StatusEffectInstance current = client.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (current == null || current.getDuration() < 300) {
            client.player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0, false, false));
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
