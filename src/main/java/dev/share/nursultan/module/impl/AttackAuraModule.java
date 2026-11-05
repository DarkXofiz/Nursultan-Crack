package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.BooleanSetting;
import dev.share.nursultan.setting.ModeSetting;
import dev.share.nursultan.setting.NumberSetting;
import dev.share.nursultan.util.Targeting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

/** Attacks the best target in range when the attack cooldown is ready. */
public final class AttackAuraModule extends Module {
    private final NumberSetting range = addSetting(
            new NumberSetting("range", "Attack distance in blocks", 3.0, 1.0, 6.0));
    private final NumberSetting fov = addSetting(
            new NumberSetting("fov", "Field of view for targets (360 = all around)", 360.0, 30.0, 360.0));
    private final ModeSetting targets = addSetting(
            new ModeSetting("targets", "Which entities to attack", "both", Targeting.TARGET_MODES));
    private final ModeSetting sort = addSetting(
            new ModeSetting("sort", "How to pick between targets", "distance", Targeting.SORT_MODES));
    private final BooleanSetting throughWalls = addSetting(
            new BooleanSetting("throughWalls", "Attack targets you cannot see", false));
    private final BooleanSetting critsOnly = addSetting(
            new BooleanSetting("critsOnly", "Only attack while falling (critical hits)", false));

    public AttackAuraModule() {
        super("AttackAura", "Attacks the best target in range.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        LivingEntity target = Targeting.find(client, range.get(), targets.get(), sort.get(),
                fov.get(), throughWalls.get());
        if (target == null || client.player.getAttackCooldownProgress(0.5f) < 1.0f) {
            return;
        }
        if (critsOnly.get() && (client.player.isOnGround() || client.player.getVelocity().y >= 0.0)) {
            return;
        }
        Targeting.faceEntity(client.player, target);
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }
}
