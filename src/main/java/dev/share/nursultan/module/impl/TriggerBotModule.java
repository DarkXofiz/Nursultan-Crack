package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.ModeSetting;
import dev.share.nursultan.util.Targeting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/** Attacks whatever your crosshair points at once the attack cooldown is ready. */
public final class TriggerBotModule extends Module {
    private final ModeSetting targets = addSetting(
            new ModeSetting("targets", "Which entities to attack", "both", Targeting.TARGET_MODES));

    public TriggerBotModule() {
        super("TriggerBot", "Attacks the entity under your crosshair.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult)) {
            return;
        }
        Entity entity = ((EntityHitResult) hit).getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) entity;
        if (!living.isAlive() || !Targeting.matches(living, targets.get())) {
            return;
        }
        if (client.player.getAttackCooldownProgress(0.5f) < 1.0f) {
            return;
        }
        client.interactionManager.attackEntity(client.player, living);
        client.player.swingHand(Hand.MAIN_HAND);
    }
}
